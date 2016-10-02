/*
 *  Copyright 2010 Kevin Gaudin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package xobyx.xcontactj.until;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.IOUtils;
import org.acra.util.JSONReportBuilder.JSONReportException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.acra.ACRA.LOG_TAG;


public class A implements ReportSender {

    /**
     * Available HTTP methods to send data. Only POST and PUT are currently
     * supported.
     */


    private final ACRAConfiguration config;


    public A(@NonNull ACRAConfiguration config) {
        this.config = config;

    }


    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData report) throws ReportSenderException {

        try {


            final String mReport = this.buildJSONReport(report).toString();


            if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "Report is " + mReport);
            // Generate report body depending on requested type


            // Adjust URL depending on method
            /*reportUrl = new URL(reportUrl.toString() + "/ps/classes/crashes/");
            request.send(context, reportUrl, POST, reportAsString, mType);*/
            request(mReport);

        } catch (@NonNull IOException | JSONReportException | IllegalStateException e) {
            throw new ReportSenderException("Error while sending " + config.reportType()
                    + " report via Http POST", e);
        }
    }

    private void request(String reportAsString) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, reportAsString);
        Request requestx = new Request.Builder()
                .url("http://ps-xobyx.rhcloud.com/ps/classes/crashes")
                .post(body)
                .addHeader("x-parse-application-id", "1")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")

                .build();

        Response response = client.newCall(requestx).execute();
        int responseCode = response.code();
        if (ACRA.DEV_LOGGING)
            ACRA.log.d(LOG_TAG, "Request response : " + responseCode + " : " + response.message());
        if ((responseCode >= 200) && (responseCode < 300)) {
            // All is good
            ACRA.log.i(LOG_TAG, "Request received by server");
        } else if (responseCode == 401) {
            //401 means the server rejected the authentication. The request must not be repeated. Discard it.
            //This probably means that nothing can be sent with this configuration, maybe ACRA should disable itself after it?
            ACRA.log.w(LOG_TAG, "401: Login validation error on server - request will be discarded");
        } else if (responseCode == 403) {
            // 403 is an explicit data validation refusal from the server. The request must not be repeated. Discard it.
            ACRA.log.w(LOG_TAG, "403: Data validation error on server - request will be discarded");
        } else if (responseCode == 405) {
            //405 means the server doesn't allow this request method. The request must not be repeated. Discard it.
            //This probably means that nothing can be sent with this configuration, maybe ACRA should disable itself after it?
            ACRA.log.w(LOG_TAG, "405: Server rejected Http POST - request will be discarded");
        } else if (responseCode == 409) {
            // 409 means that the report has been received already. So we can discard it.
            ACRA.log.w(LOG_TAG, "409: Server has already received this post - request will be discarded");
        } else if ((responseCode >= 400) && (responseCode < 600)) {
            ACRA.log.w(LOG_TAG, "Could not send ACRA Post responseCode=" + responseCode + " message=" + response.message());
            throw new IOException("Host returned error code " + responseCode);
        } else {
            ACRA.log.w(LOG_TAG, "Could not send ACRA Post - request will be discarded. responseCode=" + responseCode + " message=" + response.message());
        }

    }


    @NonNull
    private JSONObject buildJSONReport(@NonNull CrashReportData errorContent) throws JSONReportException {
        final JSONObject jsonReport = new JSONObject();
        BufferedReader reader = null;
        for (ReportField key : errorContent.keySet()) {
            try {
                // Each ReportField can be identified as a substructure and not
                // a simple String value.
                if (key.containsKeyValuePairs()) {
                    final JSONObject subObject = new JSONObject();
                    final String strContent = errorContent.getProperty(key);
                    reader = new BufferedReader(new StringReader(strContent), 1024); //TODO: 1024 should be a constant. Use ACRAConstants.DEFAULT_BUFFER_SIZE_IN_BYTES ?
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            addJSONFromProperty(subObject, line);
                        }
                    } catch (IOException e) {
                        ACRA.log.w(LOG_TAG, "Error while converting " + key.name() + " to JSON.", e);
                    }
                    final String $ = key.name().replace("$", "").replace(".", "_");
                    jsonReport.accumulate($, subObject);
                } else {
                    final String $ = key.name().replace("$", "").replace(".", "_");
                    // This field is a simple String value, store it as it is
                    jsonReport.accumulate($, guessType(errorContent.getProperty(key)));
                }
            } catch (JSONException e) {
                throw new JSONReportException("Could not create JSON object for key " + key, e);
            } finally {
                IOUtils.safeClose(reader);
            }
        }
        return jsonReport;
    }

    private void addJSONFromProperty(@NonNull JSONObject destination, @NonNull String propertyString) throws JSONException {
        final int equalsIndex = propertyString.indexOf('=');
        if (equalsIndex > 0) {
            final String currentKey = propertyString.substring(0, equalsIndex).trim().replace("$", "").replace(".", "_");
            final String currentValue = propertyString.substring(equalsIndex + 1).trim();
            Object value = guessType(currentValue);
            if (value instanceof String) {
                value = ((String) value).replaceAll("\\\\n", "\n");
            }
            final String[] splitKey = currentKey.split("\\.");
            if (splitKey.length > 1) {
                addJSONSubTree(destination, splitKey, value);
            } else {
                destination.accumulate(currentKey, value);
            }
        } else {
            destination.put(propertyString.trim().replace("$", "").replace(".", "_"), true);
        }
    }

    private static void addJSONSubTree(@NonNull JSONObject destination, @NonNull String[] keys, Object value) throws JSONException {
        for (int i = 0; i < keys.length; i++) {
            final String subKey = keys[i];
            if (i < keys.length - 1) {
                JSONObject intermediate = null;
                if (destination.isNull(subKey)) {
                    intermediate = new JSONObject();
                    destination.accumulate(subKey, intermediate);
                } else {
                    final Object target = destination.get(subKey);
                    if (target instanceof JSONObject) {
                        intermediate = destination.getJSONObject(subKey);
                    } else if (target instanceof JSONArray) {
                        // Unexpected JSONArray, see issue #186
                        final JSONArray wildCard = destination.getJSONArray(subKey);
                        for (int j = 0; j < wildCard.length(); j++) {
                            intermediate = wildCard.optJSONObject(j);
                            if (intermediate != null) {
                                // Found the original JSONObject we were looking for
                                break;
                            }
                        }
                    }

                    if (intermediate == null) {
                        ACRA.log.w(LOG_TAG, "Unknown json subtree type, see issue #186");
                        // We should never get here, but if we do, drop this value to still send the report
                        return;
                    }
                }
                destination = intermediate;
            } else {
                destination.accumulate(subKey, value);
            }
        }
    }

    private Object guessType(String property) {
        return null;
    }
}