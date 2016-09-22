package xobyx.xcontactj.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.internal.widget.ThemeUtils;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.FontTextView;

public class SendBalanceActivity extends Activity implements View.OnClickListener {

    String LookUP;
    CharacterStyle r = new CharacterStyle() {
        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(ThemeUtils.getThemeAttrColor(SendBalanceActivity.this, R.attr.colorPrimary));

            tp.setTypeface(FontTextView.getFont(SendBalanceActivity.this,"bein.ttf"));

        }
    };
    private int WORK_NETWORK = -1;
    private String[] mNet;

    private String _number = "";
    private TextView _tLabelName;

    private AppCompatCheckBox _tCheck;
    private String _name = "";
    // TextView _test;
    TextWatcher nChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }


        @Override
        public void afterTextChanged(Editable s) {


            boolean t = false;
            if (!s.toString().isEmpty() && s.toString().length() > 9) {
                Cursor ser = getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, s.toString()), new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                String mName=getString(R.string.not_in_contact);
                if (ser != null) {
                    mName = ser.getString(0);
                    if (ser.moveToFirst()&&mName!=null) {
                        setNameTitle(mName);
                    }
                    ser.close();
                }

                if (ME.getNetForNumber(s.toString()) != WORK_NETWORK) {

                    send_to.setError(getString(R.string.number_not_match_net));
                    setNameTitle(mName);

                } else {


                }
            } else {
                setNameTitle(getString(R.string.invalid_number));
            }

        }
    };

    private PopupMenu popupMenu;
    private TextInputLayout amount;
    private TextInputLayout send_to;
    private TextInputLayout pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        WORK_NETWORK = ME.getCurrentNetwork(this);
        ME.setTheme(this, WORK_NETWORK);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_blance);

        amount = (TextInputLayout) findViewById(R.id.sendb_amountlayout);
        amount.setHint("Amount");
        send_to = (TextInputLayout) findViewById(R.id.sendb_sendtolayout);
        send_to.setHint("Send to");
        pin = (TextInputLayout) findViewById(R.id.sendb_pinlayout);
        pin.setHint("Pin");


        if (WORK_NETWORK == 3) {
            Toast.makeText(this, "No Network Found ,canceled", Toast.LENGTH_LONG).show();
            finish();

        } else {
            // if (getActionBar() != null)
            //     getActionBar().setDisplayOptions(ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
            SetupLayout();


            Intent me = getIntent();
            if (me.hasExtra("NUMBER")) {
                _number = getIntent().getStringExtra("NUMBER");
                _name = getIntent().getStringExtra("NAME");
            } else if (me.getAction().equals(Intent.ACTION_SEND)) {
                StartFromAction(me);

            }

            send_to.getEditText().setText(_number);
            setNameTitle(_name);

            if (WORK_NETWORK == 0) {
                _tCheck.setChecked(true);
                _tCheck.setEnabled(false);
                pin.getEditText().setEnabled(true);
            } else {
                pin.getEditText().setText("0000");
            }


            _tCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pin.getEditText().setEnabled(isChecked);
                }
            });
            findViewById(R.id.trb_ok).setOnClickListener(this);


            send_to.getEditText().addTextChangedListener(nChange);


            findViewById(R.id.trb_pik_button).setOnClickListener(this);

        }
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (popupMenu != null)
            popupMenu.show();
    }


    private void SetupLayout() {
        _tLabelName = (TextView) findViewById(R.id.textView1);

        _tCheck = (AppCompatCheckBox) findViewById(R.id.trb_ser_use_chaek);


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void StartFromAction(Intent me) {
        if (me.getType().equals("text/plain")) {
            if (me.getClipData().getItemCount() == 1) {
                String h = me.getClipData().getItemAt(0).toString();
                //Pattern d = Pattern.compile("(((\\+249|00249)([,\\s])?)?\\d\\d\\d([,\\s])?\\d+)+");
                Pattern d = Pattern.compile("(\\+249|00249)?(|09|01)(\\d{8,12})(?:.*?)");
                Matcher matcher = d.matcher(h);
                // check all occurance
                int i = 0;

                String[] n = new String[4];
                while (matcher.find()) {
                    String a = matcher.group();
                    Cursor cur = getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, a), new String[]{"lookup", "display_name", "number"}, null, null, null);
                    if (cur.moveToFirst()) {
                        LookUP = cur.getString(cur.getColumnIndex("lookup"));
                        _name = cur.getString(cur.getColumnIndex("display_name"));
                        String dx = cur.getString(cur.getColumnIndex("Number"));

                        if (ME.getNetForNumber(dx) == WORK_NETWORK) {
                            n[i++] = dx;

                        }

                    }
                    cur.close();

                }
                if (i > 1) {
                    popupMenu = new PopupMenu(this, send_to);
                    for (String s : n) {
                        if (s != null && !s.isEmpty())
                            popupMenu.getMenu().add(s);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            _number = String.valueOf(item.getTitle());
                            send_to.getEditText().setText(item.getTitle());
                            return true;
                        }
                    });


                } else if (i == 1) {
                    _number = n[0];
                }


            }

        }
    }

    private void setNameTitle(String name) {


        String d = String.format(getString(R.string.send_blancd), name);
        SpannableString j = new SpannableString(d);
        j.setSpan(r, 16, d.length(), 0);
        _tLabelName.setText(j, TextView.BufferType.SPANNABLE);
        _name = name;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.send_blance, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify TouchedItem parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trb_pik_button:
                Intent y = new Intent(this, MainActivity.class);
                y.putExtra("local", true);
                ///FIXME:is this right
                y.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                y.setAction(Intent.ACTION_PICK);
                Toast.makeText(getBaseContext(), "Select a contact", Toast.LENGTH_LONG).show();
                startActivityForResult(y, 0);
                break;
            case R.id.trb_ok:
                if (CheckInput()) {
                    new AlertDialog.Builder(this).setMessage(String.format("Send %s SDG to %s with Number %s ", amount.getEditText().getText(), _name, String.valueOf(send_to.getEditText().getText()))).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (CheckInput()) {

                                Intent n = SendBalanceActivity.this.Trans_intent(WORK_NETWORK, String.valueOf(send_to.getEditText().getText()), amount.getEditText().getText().toString(), pin.getEditText().getText().toString());
                                startActivity(n);
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                break;

        }
    }

    private boolean CheckInput() {
        final String s = amount.getEditText().getText().toString();
        if (s.isEmpty()) {
            amount.setError("Invalid..");
            return false;
        }
        if (ME.getNetForNumber(send_to.getEditText().getText().toString()) != WORK_NETWORK) {
            send_to.setError("you can't send to a different network..");
            return false;
        }

        int g = Integer.parseInt(s);
        if (g <= 0) {
            amount.setError("Must be greater than zero..");
            return false;
        }
        final String s1 = send_to.getEditText().getText().toString();
        if (s1.isEmpty() || s1.length() < 9) {
            send_to.setError("Number is Invalid ");
            return false;
        }
        final String s2 = pin.getEditText().getText().toString();
        if (_tCheck.isChecked() && (s2.isEmpty() || s2.length() < 4)) {
            pin.setError("Invalid PIN code");
            return false;
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            final Cursor s = getContentResolver().query(data.getData(), new String[]{ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);

            if (s != null && s.moveToFirst()) {
                String d = s.getString(s.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (d != null && !d.isEmpty()) {
                    send_to.getEditText().setText(d);

                    return;
                }
                s.close();


            }


        }

        Toast.makeText(this, " can't get Number", Toast.LENGTH_LONG).show();

    }

    public Intent Trans_intent(int j, String toPhone, String amount, String pin) {
        Intent k = new Intent(Intent.ACTION_CALL);
        switch (j) {
            case 1:
                k.setData(Uri.fromParts("tel", String.format("*333*%S*%S*%S###", pNum(amount), pNum(toPhone), pNum(pin)), "#"));
                break;
            case 0:
                k.setData(Uri.fromParts("tel", String.format("*200*%S*%S*%S*%S####", pNum(pin), pNum(amount), pNum(toPhone), pNum(toPhone)), "#"));
                break;
            case 2:
                k.setData(Uri.fromParts("tel", String.format("*121*%S*%S*%S###", pNum(toPhone), pNum(amount), pNum(pin)), "#"));
                break;
            default:
                return null;

        }
        return k;
    }

    private String pNum(String s) {
        if (s.startsWith("+249"))
            s = s.replace("+249", "0");
        s = s.replace(" ", "");
        return s;
    }
}
