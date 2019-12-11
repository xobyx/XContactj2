package com.andevstudioth.changedns.common;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.VpnService;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat.BigTextStyle;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationCompat.Style;
import com.andevstudioth.changedns.services.LocalVPN;
import com.andevstudioth.changedns.utils.DNSUtils;
import com.andevstudioth.changedns.utils.MyLog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity
  extends AppCompatActivity
  implements AdapterView.OnItemSelectedListener
{
  private static final String TAG = MainActivity.class.getSimpleName();
  static LocalVPN vpn;
  private boolean DEBUG = true;
  private Button btnGetPremium;
  private Button btnRateUs;
  private Button btnStart;
  private EditText edtDNS1;
  private EditText edtDNS2;
  private AdView mAdView;
  InterstitialAd mInterstitialAd;
  InterstitialAd mInterstitialAdIntro;
  RelativeLayout main;
  Dialog pleaseWaitDialog;
  private SharedPreferences prefs_main_settings;
  ProgressDialog progressDialog;
  private Spinner spnDNS;
  private TextView tvConnectStatus;
  
  static
  {
    LocalVPN localLocalVPN = new com/andevstudioth/changedns/services/LocalVPN;
    localLocalVPN.<init>();
    vpn = localLocalVPN;
  }
  
  private boolean isValidDNS()
  {
    String str1 = getDNS1();
    String str2 = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    boolean bool = str1.matches(str2);
    if (bool)
    {
      str1 = getDNS2();
      str2 = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
      bool = str1.matches(str2);
      if (bool)
      {
        bool = this.DEBUG;
        if (bool)
        {
          str1 = "Andrew";
          str2 = "regex is matched";
          Log.d(str1, str2);
        }
        return true;
      }
    }
    return false;
  }
  
  private void requestNewInterstitial()
  {
    Object localObject = this.mInterstitialAd;
    boolean bool = ((InterstitialAd)localObject).isLoading();
    if (!bool)
    {
      localObject = this.mInterstitialAd;
      bool = ((InterstitialAd)localObject).isLoaded();
      if (!bool)
      {
        localObject = new com/google/android/gms/ads/AdRequest$Builder;
        ((AdRequest.Builder)localObject).<init>();
        localObject = ((AdRequest.Builder)localObject).build();
        InterstitialAd localInterstitialAd = this.mInterstitialAd;
        localInterstitialAd.loadAd((AdRequest)localObject);
      }
    }
  }
  
  private void restorePreferences()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences("PrefsDNS", 0);
    this.prefs_main_settings = localSharedPreferences;
    int i = this.prefs_main_settings.getInt("providerKey", 0);
    String str1 = this.prefs_main_settings.getString("dns1Key", "");
    String str2 = this.prefs_main_settings.getString("dns2Key", "");
    boolean bool = this.prefs_main_settings.getBoolean("statusKey", false);
    this.spnDNS.setSelection(i);
    this.edtDNS1.setText(str1);
    this.edtDNS2.setText(str2);
    updateUIStatus(bool);
  }
  
  private void savePreferences()
  {
    Object localObject = getSharedPreferences("PrefsDNS", 0);
    this.prefs_main_settings = ((SharedPreferences)localObject);
    localObject = this.prefs_main_settings.edit();
    int i = this.spnDNS.getSelectedItemPosition();
    ((SharedPreferences.Editor)localObject).putInt("providerKey", i);
    String str = this.edtDNS1.getText().toString();
    ((SharedPreferences.Editor)localObject).putString("dns1Key", str);
    str = this.edtDNS2.getText().toString();
    ((SharedPreferences.Editor)localObject).putString("dns2Key", str);
    boolean bool = LocalVPN.isRunning();
    ((SharedPreferences.Editor)localObject).putBoolean("statusKey", bool);
    ((SharedPreferences.Editor)localObject).apply();
  }
  
  private void setDNS(int paramInt)
  {
    String str1 = null;
    Object localObject = getSharedPreferences("PrefsDNS", 0);
    this.prefs_main_settings = ((SharedPreferences)localObject);
    localObject = this.prefs_main_settings;
    String str2 = "providerKey";
    int i = ((SharedPreferences)localObject).getInt(str2, 0);
    int j = 1;
    EditText localEditText;
    if (paramInt == 0)
    {
      if (i == 0)
      {
        localEditText = this.edtDNS1;
        localObject = this.prefs_main_settings.getString("dns1Key", "");
        localEditText.setText((CharSequence)localObject);
        localEditText = this.edtDNS2;
        localObject = this.prefs_main_settings;
        str1 = "dns2Key";
        String str3 = "";
        localObject = ((SharedPreferences)localObject).getString(str1, str3);
        localEditText.setText((CharSequence)localObject);
      }
      else
      {
        this.edtDNS1.setText("");
        localEditText = this.edtDNS2;
        localObject = "";
        localEditText.setText((CharSequence)localObject);
      }
      this.edtDNS1.setEnabled(j);
      localEditText = this.edtDNS2;
      localEditText.setEnabled(j);
    }
    else if (paramInt == j)
    {
      this.edtDNS1.setText("8.8.8.8");
      localEditText = this.edtDNS2;
      localObject = "8.8.4.4";
      localEditText.setText((CharSequence)localObject);
      this.edtDNS1.setEnabled(false);
      localEditText = this.edtDNS2;
      localEditText.setEnabled(false);
    }
    else
    {
      i = 2;
      if (paramInt == i)
      {
        this.edtDNS1.setText("208.67.222.222");
        localEditText = this.edtDNS2;
        localObject = "208.67.220.220";
        localEditText.setText((CharSequence)localObject);
        this.edtDNS1.setEnabled(false);
        localEditText = this.edtDNS2;
        localEditText.setEnabled(false);
      }
      else
      {
        i = 3;
        if (paramInt == i)
        {
          this.edtDNS1.setText("77.88.8.8");
          localEditText = this.edtDNS2;
          localObject = "77.88.8.1";
          localEditText.setText((CharSequence)localObject);
          this.edtDNS1.setEnabled(false);
          localEditText = this.edtDNS2;
          localEditText.setEnabled(false);
        }
        else
        {
          i = 4;
          if (paramInt == i)
          {
            this.edtDNS1.setText("209.244.0.3");
            localEditText = this.edtDNS2;
            localObject = "209.244.0.4";
            localEditText.setText((CharSequence)localObject);
            this.edtDNS1.setEnabled(false);
            localEditText = this.edtDNS2;
            localEditText.setEnabled(false);
          }
          else
          {
            i = 5;
            if (paramInt == i)
            {
              this.edtDNS1.setText("107.150.40.234");
              localEditText = this.edtDNS2;
              localObject = "50.116.23.211";
              localEditText.setText((CharSequence)localObject);
              this.edtDNS1.setEnabled(false);
              localEditText = this.edtDNS2;
              localEditText.setEnabled(false);
            }
            else
            {
              i = 6;
              if (paramInt == i)
              {
                this.edtDNS1.setText("208.76.50.50");
                localEditText = this.edtDNS2;
                localObject = "208.76.51.51";
                localEditText.setText((CharSequence)localObject);
                this.edtDNS1.setEnabled(false);
                localEditText = this.edtDNS2;
                localEditText.setEnabled(false);
              }
              else
              {
                i = 7;
                if (paramInt == i)
                {
                  this.edtDNS1.setText("195.46.39.39");
                  localEditText = this.edtDNS2;
                  localObject = "195.46.39.40";
                  localEditText.setText((CharSequence)localObject);
                  this.edtDNS1.setEnabled(false);
                  localEditText = this.edtDNS2;
                  localEditText.setEnabled(false);
                }
                else
                {
                  i = 8;
                  if (paramInt == i)
                  {
                    this.edtDNS1.setText("208.67.222.123");
                    localEditText = this.edtDNS2;
                    localObject = "208.67.220.123";
                    localEditText.setText((CharSequence)localObject);
                    this.edtDNS1.setEnabled(false);
                    localEditText = this.edtDNS2;
                    localEditText.setEnabled(false);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  private void showFullScreenAds()
  {
    Object localObject1 = new com/google/android/gms/ads/InterstitialAd;
    ((InterstitialAd)localObject1).<init>(this);
    this.mInterstitialAdIntro = ((InterstitialAd)localObject1);
    localObject1 = this.mInterstitialAdIntro;
    Object localObject2 = "ca-app-pub-5207043292593377/3652739519";
    ((InterstitialAd)localObject1).setAdUnitId((String)localObject2);
    localObject1 = this.mInterstitialAdIntro;
    boolean bool = ((InterstitialAd)localObject1).isLoading();
    if (!bool)
    {
      localObject1 = this.mInterstitialAdIntro;
      bool = ((InterstitialAd)localObject1).isLoaded();
      if (!bool)
      {
        localObject1 = new com/google/android/gms/ads/AdRequest$Builder;
        ((AdRequest.Builder)localObject1).<init>();
        localObject1 = ((AdRequest.Builder)localObject1).addTestDevice("79CDE917D0148170236A2B06E9C93AC4").build();
        localObject2 = this.mInterstitialAdIntro;
        ((InterstitialAd)localObject2).loadAd((AdRequest)localObject1);
      }
    }
    localObject1 = this.mInterstitialAdIntro;
    localObject2 = new com/andevstudioth/changedns/common/MainActivity$2;
    ((MainActivity.2)localObject2).<init>(this);
    ((InterstitialAd)localObject1).setAdListener((AdListener)localObject2);
    localObject1 = new android/app/Dialog;
    ((Dialog)localObject1).<init>(this);
    this.pleaseWaitDialog = ((Dialog)localObject1);
    this.pleaseWaitDialog.requestWindowFeature(1);
    this.pleaseWaitDialog.setCancelable(false);
    this.pleaseWaitDialog.setContentView(2131427361);
    this.pleaseWaitDialog.show();
    localObject1 = new android/os/Handler;
    ((Handler)localObject1).<init>();
    localObject2 = new com/andevstudioth/changedns/common/-$$Lambda$MainActivity$eLL-QX7RtE_KKWXx05A3gdYMgEE;
    ((-..Lambda.MainActivity.eLL-QX7RtE_KKWXx05A3gdYMgEE)localObject2).<init>(this);
    ((Handler)localObject1).postDelayed((Runnable)localObject2, 3000L);
  }
  
  private void showStopDialog()
  {
    Object localObject1 = getResources().getString(2131689574);
    Object localObject2 = ProgressDialog.show(this, "", (CharSequence)localObject1);
    this.progressDialog = ((ProgressDialog)localObject2);
    localObject2 = new android/os/Handler;
    ((Handler)localObject2).<init>();
    localObject1 = new com/andevstudioth/changedns/common/-$$Lambda$MainActivity$qVMUdHTTDuR9AI2CeRBM7lkJf-4;
    ((-..Lambda.MainActivity.qVMUdHTTDuR9AI2CeRBM7lkJf-4)localObject1).<init>(this);
    ((Handler)localObject2).postDelayed((Runnable)localObject1, 2000L);
  }
  
  private void startVpnService()
  {
    Object localObject = VpnService.prepare(this);
    int i = 0;
    String str1;
    int j;
    if (localObject != null)
    {
      str1 = TAG;
      String str2 = "startVpnService - vpnIntent != null";
      MyLog.d(str1, str2);
      try
      {
        startActivityForResult((Intent)localObject, 0);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        localObject = getApplicationContext();
        i = 2131689500;
        j = 1;
        localObject = Toast.makeText((Context)localObject, i, j);
        ((Toast)localObject).show();
      }
    }
    else
    {
      localObject = TAG;
      MyLog.d((String)localObject, "startVpnService - vpnIntent == null");
      int k = -1;
      j = 0;
      str1 = null;
      onActivityResult(0, k, null);
    }
  }
  
  private void stopVpnService()
  {
    Intent localIntent = new android/content/Intent;
    Context localContext = getApplicationContext();
    Class localClass = LocalVPN.class;
    localIntent.<init>(localContext, localClass);
    stopService(localIntent);
    boolean bool = DNSUtils.isShowNotification(this);
    if (bool)
    {
      int i = Build.VERSION.SDK_INT;
      int j = 26;
      if (i >= j) {
        updateStopNotificationFromO();
      } else {
        updateStopNotification();
      }
    }
    updateUIStatus(false);
    showStopDialog();
  }
  
  private void updateStopNotification()
  {
    Object localObject1 = DNSUtils.getDNSProviderName(getBaseContext());
    Object localObject2 = new android/content/Intent;
    ((Intent)localObject2).<init>(this, MainActivity.class);
    int i = 0;
    localObject2 = PendingIntent.getActivity(this, 0, (Intent)localObject2, 134217728);
    NotificationCompat.Builder localBuilder = new androidx/core/app/NotificationCompat$Builder;
    localBuilder.<init>(this);
    int j = 2131165300;
    localBuilder = localBuilder.setSmallIcon(j);
    Object localObject3 = getResources().getString(2131689551);
    localBuilder = localBuilder.setContentTitle((CharSequence)localObject3);
    localObject3 = getResources();
    int k = 2131689548;
    localObject3 = ((Resources)localObject3).getString(k);
    localBuilder = localBuilder.setContentText((CharSequence)localObject3);
    localObject3 = new androidx/core/app/NotificationCompat$BigTextStyle;
    ((NotificationCompat.BigTextStyle)localObject3).<init>();
    StringBuilder localStringBuilder = new java/lang/StringBuilder;
    localStringBuilder.<init>();
    Object localObject4 = getResources();
    int m = 2131689549;
    localObject4 = ((Resources)localObject4).getString(m);
    localStringBuilder.append((String)localObject4);
    localObject4 = " ";
    localStringBuilder.append((String)localObject4);
    localStringBuilder.append((String)localObject1);
    localObject1 = getResources();
    int n = 2131689552;
    localObject1 = ((Resources)localObject1).getString(n);
    localStringBuilder.append((String)localObject1);
    localObject1 = localStringBuilder.toString();
    localObject1 = ((NotificationCompat.BigTextStyle)localObject3).bigText((CharSequence)localObject1);
    localObject1 = localBuilder.setStyle((NotificationCompat.Style)localObject1);
    ((NotificationCompat.Builder)localObject1).setContentIntent((PendingIntent)localObject2);
    localObject2 = (NotificationManager)getSystemService("notification");
    if (localObject2 != null)
    {
      localObject1 = ((NotificationCompat.Builder)localObject1).build();
      i = 1;
      ((NotificationManager)localObject2).notify(i, (Notification)localObject1);
    }
  }
  
  private void updateStopNotificationFromO()
  {
    Object localObject1 = DNSUtils.getDNSProviderName(getBaseContext());
    Object localObject2 = new android/content/Intent;
    ((Intent)localObject2).<init>(this, MainActivity.class);
    int i = 134217728;
    localObject2 = PendingIntent.getActivity(this, 0, (Intent)localObject2, i);
    int j = 2131689508;
    Object localObject3 = getString(j);
    NotificationChannel localNotificationChannel = new android/app/NotificationChannel;
    localNotificationChannel.<init>("mychannel", (CharSequence)localObject3, 3);
    localObject3 = new androidx/core/app/NotificationCompat$Builder;
    ((NotificationCompat.Builder)localObject3).<init>(this);
    int k = 2131165300;
    localObject3 = ((NotificationCompat.Builder)localObject3).setSmallIcon(k);
    Object localObject4 = getResources().getString(2131689551);
    localObject3 = ((NotificationCompat.Builder)localObject3).setContentTitle((CharSequence)localObject4).setChannelId("mychannel");
    localObject4 = getResources();
    int m = 2131689548;
    localObject4 = ((Resources)localObject4).getString(m);
    localObject3 = ((NotificationCompat.Builder)localObject3).setContentText((CharSequence)localObject4);
    localObject4 = new androidx/core/app/NotificationCompat$BigTextStyle;
    ((NotificationCompat.BigTextStyle)localObject4).<init>();
    StringBuilder localStringBuilder = new java/lang/StringBuilder;
    localStringBuilder.<init>();
    Object localObject5 = getResources();
    int n = 2131689549;
    localObject5 = ((Resources)localObject5).getString(n);
    localStringBuilder.append((String)localObject5);
    localObject5 = " ";
    localStringBuilder.append((String)localObject5);
    localStringBuilder.append((String)localObject1);
    localObject1 = getResources();
    int i1 = 2131689552;
    localObject1 = ((Resources)localObject1).getString(i1);
    localStringBuilder.append((String)localObject1);
    localObject1 = localStringBuilder.toString();
    localObject1 = ((NotificationCompat.BigTextStyle)localObject4).bigText((CharSequence)localObject1);
    localObject1 = ((NotificationCompat.Builder)localObject3).setStyle((NotificationCompat.Style)localObject1);
    ((NotificationCompat.Builder)localObject1).setContentIntent((PendingIntent)localObject2);
    localObject2 = (NotificationManager)getSystemService("notification");
    if (localObject2 != null)
    {
      ((NotificationManager)localObject2).createNotificationChannel(localNotificationChannel);
      localObject1 = ((NotificationCompat.Builder)localObject1).build();
      j = 1;
      ((NotificationManager)localObject2).notify(j, (Notification)localObject1);
    }
  }
  
  public String getDNS1()
  {
    return this.edtDNS1.getText().toString();
  }
  
  public String getDNS2()
  {
    return this.edtDNS2.getText().toString();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 0)
    {
      paramInt1 = -1;
      if (paramInt2 == paramInt1)
      {
        MyLog.d(TAG, "onActivityResult - request Code = 0 && resultCode = ok");
        localObject1 = new android/content/Intent;
        localObject2 = LocalVPN.class;
        ((Intent)localObject1).<init>(this, (Class)localObject2);
        startService((Intent)localObject1);
        paramInt1 = 1;
        updateUIStatus(paramInt1);
        return;
      }
    }
    Object localObject1 = TAG;
    Object localObject2 = "onActivityResult - jump to else";
    MyLog.d((String)localObject1, (String)localObject2);
  }
  
  public void onBackPressed()
  {
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool1 = this.DEBUG;
    if (bool1)
    {
      paramBundle = "Andrew";
      localObject = "onCreate()";
      Log.d(paramBundle, (String)localObject);
    }
    setContentView(2131427356);
    paramBundle = (RelativeLayout)findViewById(2131230780);
    this.main = paramBundle;
    paramBundle = this.main;
    int j = 8;
    paramBundle.setVisibility(j);
    paramBundle = (Spinner)findViewById(2131230823);
    this.spnDNS = paramBundle;
    paramBundle = (EditText)findViewById(2131230824);
    this.edtDNS1 = paramBundle;
    paramBundle = (EditText)findViewById(2131230825);
    this.edtDNS2 = paramBundle;
    paramBundle = (TextView)findViewById(2131230926);
    this.tvConnectStatus = paramBundle;
    paramBundle = (Button)findViewById(2131230802);
    this.btnStart = paramBundle;
    paramBundle = (AdView)findViewById(2131230781);
    this.mAdView = paramBundle;
    paramBundle = new com/google/android/gms/ads/AdRequest$Builder;
    paramBundle.<init>();
    paramBundle = paramBundle.build();
    Object localObject = this.mAdView;
    ((AdView)localObject).loadAd(paramBundle);
    int i = 2131230801;
    try
    {
      paramBundle = findViewById(i);
      paramBundle = (Button)paramBundle;
      this.btnRateUs = paramBundle;
      paramBundle = this.btnRateUs;
      localObject = new com/andevstudioth/changedns/common/-$$Lambda$MainActivity$l42HB6bO6_8hMSxloXRNvG088jQ;
      ((-..Lambda.MainActivity.l42HB6bO6_8hMSxloXRNvG088jQ)localObject).<init>(this);
      paramBundle.setOnClickListener((View.OnClickListener)localObject);
      i = 2131230800;
      paramBundle = findViewById(i);
      paramBundle = (Button)paramBundle;
      this.btnGetPremium = paramBundle;
      paramBundle = this.btnGetPremium;
      localObject = new com/andevstudioth/changedns/common/-$$Lambda$MainActivity$fsdrga0A1aN0ciGXpuYInmkbK7s;
      ((-..Lambda.MainActivity.fsdrga0A1aN0ciGXpuYInmkbK7s)localObject).<init>(this);
      paramBundle.setOnClickListener((View.OnClickListener)localObject);
    }
    catch (Exception paramBundle)
    {
      localObject = "Andrew";
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      String str = "exception = ";
      localStringBuilder.append(str);
      paramBundle = paramBundle.getMessage();
      localStringBuilder.append(paramBundle);
      paramBundle = localStringBuilder.toString();
      MyLog.d((String)localObject, paramBundle);
    }
    setRequestedOrientation(1);
    paramBundle = getIntent();
    localObject = "isFirstTime";
    StringBuilder localStringBuilder = null;
    boolean bool2 = paramBundle.getBooleanExtra((String)localObject, false);
    if (bool2) {
      startVpnService();
    }
    paramBundle = new com/google/android/gms/ads/InterstitialAd;
    paramBundle.<init>(this);
    this.mInterstitialAd = paramBundle;
    this.mInterstitialAd.setAdUnitId("ca-app-pub-5207043292593377/4317623043");
    requestNewInterstitial();
    paramBundle = this.mInterstitialAd;
    localObject = new com/andevstudioth/changedns/common/MainActivity$1;
    ((MainActivity.1)localObject).<init>(this);
    paramBundle.setAdListener((AdListener)localObject);
    paramBundle = getApplicationContext();
    DNSUtils.setNumberOpenApp(paramBundle);
    bool2 = DNSUtils.isShowFullScreenAds(this);
    if (bool2)
    {
      showFullScreenAds();
    }
    else
    {
      paramBundle = this.main;
      paramBundle.setVisibility(0);
    }
    restorePreferences();
    this.spnDNS.setOnItemSelectedListener(this);
    paramBundle = this.btnStart;
    localObject = new com/andevstudioth/changedns/common/-$$Lambda$MainActivity$q7gBf7wmKbFPUFDTnmOJGW6-7iI;
    ((-..Lambda.MainActivity.q7gBf7wmKbFPUFDTnmOJGW6-7iI)localObject).<init>(this);
    paramBundle.setOnClickListener((View.OnClickListener)localObject);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131492864, paramMenu);
    return true;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    Object localObject = this.mAdView;
    if (localObject != null) {
      ((AdView)localObject).destroy();
    }
    boolean bool = this.DEBUG;
    if (bool)
    {
      localObject = "Andrew";
      String str = "onDestroy() is called";
      Log.d((String)localObject, str);
    }
  }
  
  public void onItemSelected(AdapterView paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    setDNS(paramInt);
  }
  
  public void onNothingSelected(AdapterView paramAdapterView) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    int j = 2131230726;
    boolean bool = true;
    if (i != j)
    {
      j = 2131230890;
      if (i != j) {
        return false;
      }
      paramMenuItem = new android/content/Intent;
      paramMenuItem.<init>(this, SettingActivity.class);
      startActivity(paramMenuItem);
      return bool;
    }
    paramMenuItem = new android/app/AlertDialog$Builder;
    paramMenuItem.<init>(this);
    paramMenuItem.setMessage(2131689528).setTitle(2131689529);
    -..Lambda.MainActivity.ZrA0HtubWr2t2MjXRew9j48xXwA localZrA0HtubWr2t2MjXRew9j48xXwA = -..Lambda.MainActivity.ZrA0HtubWr2t2MjXRew9j48xXwA.INSTANCE;
    paramMenuItem.setPositiveButton(2131689530, localZrA0HtubWr2t2MjXRew9j48xXwA);
    paramMenuItem.create().show();
    return bool;
  }
  
  protected void onPause()
  {
    super.onPause();
    Object localObject = this.mAdView;
    if (localObject != null) {
      ((AdView)localObject).pause();
    }
    savePreferences();
    boolean bool = this.DEBUG;
    if (bool)
    {
      localObject = "Andrew";
      String str = "onPause() is called";
      Log.d((String)localObject, str);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    Object localObject = this.mAdView;
    if (localObject != null) {
      ((AdView)localObject).resume();
    }
    boolean bool = this.DEBUG;
    if (bool)
    {
      localObject = "Andrew";
      String str = "onResume() is called";
      Log.d((String)localObject, str);
    }
    bool = LocalVPN.isRunning();
    updateUIStatus(bool);
  }
  
  protected void onStop()
  {
    super.onStop();
    savePreferences();
    boolean bool = this.DEBUG;
    if (bool)
    {
      String str1 = "Andrew";
      String str2 = "onStop() is called";
      Log.d(str1, str2);
    }
  }
  
  public void updateUIStatus(boolean paramBoolean)
  {
    Object localObject = this.tvConnectStatus;
    int i;
    if (paramBoolean) {
      i = 2131689588;
    } else {
      i = 2131689589;
    }
    ((TextView)localObject).setText(i);
    localObject = this.btnStart;
    if (paramBoolean) {
      paramBoolean = 2131689507;
    } else {
      paramBoolean = 2131689506;
    }
    ((Button)localObject).setText(paramBoolean);
  }
}
