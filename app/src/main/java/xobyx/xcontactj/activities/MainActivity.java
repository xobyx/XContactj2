package xobyx.xcontactj.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.preference.PreferenceManager; // AndroidX
import com.google.android.material.floatingactionbutton.FloatingActionButton; // AndroidX
import androidx.viewpager.widget.ViewPager; // AndroidX
import androidx.appcompat.app.AlertDialog; // AndroidX
import androidx.appcompat.app.AppCompatActivity; // AndroidX
import androidx.appcompat.widget.SearchView; // AndroidX
import androidx.appcompat.widget.Toolbar; // AndroidX
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

// Removed Fabric and old Crashlytics imports

import xobyx.xcontactj.MyApp; // Assuming MyApp is updated or compatible
import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.SectionsPagerAdapter;
import xobyx.xcontactj.adapters.SmAdapter;
import xobyx.xcontactj.base.IDialerHandler;
import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.fragments.NetFragmentPick;
// Removed import for xobyx.xcontactj.gcm.RegistrationIntentService;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.MDatabase;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.views.HeaderTabs;
import xobyx.xcontactj.views.xViewPager; // This custom view might need internal updates for AndroidX ViewPager

import static xobyx.xcontactj.until.ME.NET_N;

// Removed unclear TODO: @{@link Se}
public class MainActivity extends AppCompatActivity implements IDialerHandler, SearchView.OnQueryTextListener {

    // Instance variables instead of static
    private String wn_name = null;
    private MDatabase DB;
    private boolean pick_mode = false; // Default to false
    private int wn_id;

    // pick_mode_local was already an instance variable. Initialize explicitly.
    public boolean pick_mode_local = false;
    public FloatingActionButton call_button;
    SectionsPagerAdapter mSectionsPagerAdapter;

    HeaderTabs tabs;
    xViewPager mViewPager; // Custom ViewPager, ensure it's compatible with AndroidX ViewPager if it extends it.
    private int default_network = 0; // Default to a valid network index if possible
    private DialerActionModeHelper DialerHelper;
    private boolean is_dialer_open = false;

    private FirebaseAnalytics mFirebaseAnalytics;
    private NetFragmentPick netFragmentPick; // For pick_mode_local
    private Toolbar mToolbar;

    private final View.OnClickListener call_handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // KitKat check is quite old
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d(MainActivity.class.getSimpleName(), "dial anim end");
                        StartDialer("");
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                v.startAnimation(animation);
            } else {
                StartDialer("");
            }
        }
    };

    // TODO: Ensure this listener is registered in onResume/onStart and unregistered in onPause/onStop if used.
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_IDLE) return;
            String c = "";
            try {
                c = ME.getTelephonyService(MainActivity.this).getCallerName(); // ME class might need review
            } catch (RemoteException e) {
                e.printStackTrace(); // Consider logging to Crashlytics
            }
            int net = ME.getNetForNumber(incomingNumber);
            String s = getNetworkNameForListener(net); // Use instance method
            Toast.makeText(MainActivity.this, s + " " + c, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            int i = serviceState.getState();
            if (i == ServiceState.STATE_EMERGENCY_ONLY || i == ServiceState.STATE_OUT_OF_SERVICE) {
                MainActivity.this.wn_id = 3; // Update instance variable
                MainActivity.this.wn_name = "out of service"; // Update instance variable
                Toast.makeText(MainActivity.this, "No Network,out of service " + getNetworkName(), Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.this.wn_id = ME.getCurrentNetwork(MainActivity.this); // Update instance variable
                if (MainActivity.this.wn_id != 3) {
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(MainActivity.this.wn_id, false);
                    }
                    Toast.makeText(MainActivity.this, "Found Network: " + getNetworkName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_main_1);

        DB = new MDatabase(getApplicationContext()); // Use ApplicationContext for DB

        initViews();
        handleIntentAction(getIntent()); // Sets pick_mode, pick_mode_local, wn_id, wn_name
        setupModeSpecificUi();

        // TODO: If PhoneStateListener is to be used, register it here or in onResume, and unregister in onPause/onStop.
        // Example:
        // TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // if (telephonyManager != null) {
        //     telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_SERVICE_STATE);
        // }
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) { // Guard against null if layout is incorrect
            mToolbar.inflateMenu(R.menu.main_activity);
            setSupportActionBar(mToolbar);
        }

        call_button = findViewById(R.id.main_call);
        if (call_button != null) {
            call_button.setOnClickListener(call_handler);
        }

        View vb = findViewById(R.id.rep_me);
        if (vb != null) {
            vb.setBackgroundColor(SettingHelp.getBackground(this)); // 'this' context is fine
        }
        tabs = findViewById(R.id.mhrader);
        mViewPager = findViewById(R.id.pager);
    }

    private void handleIntentAction(Intent intent) {
        this.wn_id = ME.getCurrentNetwork(this);
        if (this.wn_id != 3) { // Assuming 3 means "no network" or "error"
            this.wn_name = getNetworkName();
        } else {
            this.wn_name = "No Network"; // Default name
        }

        if (intent != null && intent.getAction() != null) {
            if (Intent.ACTION_DIAL.equals(intent.getAction())) {
                StartDialer(intent.getDataString());
            } else if (Intent.ACTION_PICK.equals(intent.getAction())) {
                this.pick_mode = true;
                this.pick_mode_local = intent.hasExtra("local");
            }
        }
    }

    private void setupModeSpecificUi() {
        if (this.pick_mode_local) {
            setupPickModeUi();
        } else {
            setupNormalModeUi();
        }
    }

    private void setupPickModeUi() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Pick Contact:");
        }
        if (call_button != null) call_button.setVisibility(View.GONE);
        if (tabs != null) tabs.setVisibility(View.GONE);

        netFragmentPick = NetFragmentPick.newInstance(this.wn_id);
        getSupportFragmentManager().beginTransaction().replace(R.id.repl, netFragmentPick).commit();
    }

    private void setupNormalModeUi() {
        if (mViewPager == null || tabs == null) {
            Log.e("MainActivity", "ViewPager or Tabs not initialized for Normal Mode");
            return;
        }
        mViewPager.setMoveEnabled(!this.pick_mode); // pick_mode_local is often the same as pick_mode here

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabs.setViewPager(mViewPager);
        DialerHelper = new DialerActionModeHelper(this);

        if (this.wn_id == 3) { // No network
            if (PreferenceManager.getDefaultSharedPreferences(this).contains("default_Network")) {
                default_network = PreferenceManager.getDefaultSharedPreferences(this).getInt("default_Network", 0);
            } else {
                setDefaultNetwork(); // This shows a dialog which might set default_network
            }
        }
        mViewPager.setCurrentItem(this.wn_id != 3 ? this.wn_id : default_network);
    }

    private String getNetworkName() {
        if (this.wn_id < 0 || this.wn_id >= ME.NET_N.length) return "Unknown";
        return ME.NET_N[this.wn_id];
    }

    private String getNetworkNameForListener(int id) { // Used by listener to avoid instance field access timing issues
        if (id < 0 || id >= ME.NET_N.length) return "Unknown";
        return ME.NET_N[id];
    }

    private void setDefaultNetwork() {
        final int[] selectedNetwork = new int[]{-1}; // Effectively final for lambda
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog);

        // KitKat check for this specific preference behavior seems too granular for current Android versions.
        // If this was a workaround for an old bug, it might no longer be needed.
        // Forcing default to 0 if no network on KitKat+
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //     PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("default_Network", 0).apply();
        //     this.default_network = 0; // Update instance variable
        //     if (mViewPager != null && this.wn_id == 3) mViewPager.setCurrentItem(this.default_network);
        //     return;
        // }

        SmAdapter dialogAdapter = new SmAdapter();
        dialogAdapter.newInstance(this).SetupItems(R.array.net_names).SetupLayout(R.layout.r_header).setInflater(
            (view, pos, item) -> {
                if (view.getId() == android.R.id.text1) {
                    ((TextView) view).setText((String) item);
                } else {
                    ((ImageView) view).setImageResource(ME.NetDrawables[pos][0]);
                }
            });

        builder.setCancelable(false)
            .setMessage("No Network Founded plz Select your default Network..") // Consider string resources
            .setTitle("No Network Founded") // Consider string resources
            .setSingleChoiceItems(dialogAdapter.BuildAdapter(), -1, (dialog, which) -> selectedNetwork[0] = which)
            .setPositiveButton("Select", (dialog, which) -> { // Consider string resources
                if (selectedNetwork[0] != -1) {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                        .edit()
                        .putInt("default_Network", selectedNetwork[0])
                        .apply();
                    this.default_network = selectedNetwork[0];
                    if (mViewPager != null && this.wn_id == 3) { // If still no actual network, use this default
                        mViewPager.setCurrentItem(this.default_network);
                    }
                    dialog.dismiss();
                }
            })
            .show();
    }

    private void StartDialer(String dataString) {
        if (this.wn_id != 3) { // Not "no network"
            if (!is_dialer_open) {
                if (DialerHelper != null) {
                    DialerHelper.StartDialerActionMode(dataString);
                } else {
                    Log.e("MainActivity", "DialerHelper not initialized in StartDialer");
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "No Network..", Toast.LENGTH_SHORT).show(); // Consider string resource
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false; // Typically true if handled
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        NetFragment item = null;
        if (!this.pick_mode_local) {
            if (mViewPager != null && mSectionsPagerAdapter != null && mSectionsPagerAdapter.Fragments != null &&
                mViewPager.getCurrentItem() < mSectionsPagerAdapter.Fragments.length) {
                mViewPager.setMoveEnabled(newText.isEmpty());
                item = mSectionsPagerAdapter.Fragments[mViewPager.getCurrentItem()];
            }
        } else {
            item = netFragmentPick;
        }

        try {
            if (item != null) {
                item.SearchFor(newText);
            }
        } catch (Exception a) { // Catching generic Exception is broad
            Toast.makeText(this, a.getMessage(), Toast.LENGTH_LONG).show();
            // ACRA.getErrorReporter().handleException(a); // Old ACRA, consider FirebaseCrashlytics.logException(a);
            Log.e("MainActivity", "Error in onQueryTextChange: " + newText, a);
        }
        return true; // Usually true if the action is handled
    }

    // onDestroy, onCreateOptionsMenu, onOptionsItemSelected, onActivityResult, onBackPressed,
    // onVisibilityChange, getDialerAction, onCall, getToolBar, getDialerState, onNumberChange
    // remain largely the same but now use instance variables where appropriate.

    @Override
    protected void onDestroy() {
        // Unregister PhoneStateListener if it was registered
        // Example:
        // TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // if (telephonyManager != null) {
        //     telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        // }

        if (DB != null) {
            DB.Close(); // Instance DB
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(() -> {
                if (mViewPager != null) {
                    mViewPager.setMoveEnabled(true);
                }
                return false;
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AboutActivity.ShowAboutActivity(this);
            return true;
        } else if (id == R.id.action_search) {
            // Search action is handled by SearchView setup
            return true;
        } else if (id == R.id.action_settings) {
            // TODO: Migrate from startActivityForResult to ActivityResultLauncher
            // Example:
            // final ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            //        new ActivityResultContracts.StartActivityForResult(),
            //        result -> { if (result.getResultCode() == Activity.RESULT_OK) { recreate(); } });
            // Intent intent = new Intent(this, SettingsActivity.class);
            // settingsLauncher.launch(intent);
            Intent i = new Intent(this, SettingsActivity.class); // Use 'this' for context
            startActivityForResult(i, 0); // Deprecated: Consider using ActivityResultLauncher
            return true;
        } else if (id == R.id.action_all_contact) {
            Intent d = new Intent(this, AllMainActivity.class);
            startActivity(d);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // TODO: Migrate from onActivityResult to ActivityResultLauncher (see settingsLauncher example in onOptionsItemSelected)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Parameters updated to standard names
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // requestCode check might be needed if there are multiple startActivityForResult calls
            // The recreate() call is a heavy way to refresh UI.
            // Consider more targeted updates if possible, e.g., via LiveData or event bus.
            Bundle t = new Bundle(); // This bundle seems unused before onSaveInstanceState
            t.putInt("ListMode", 0); // If this is to set a default, it's unusual here
            // onSaveInstanceState(t); // Calling onSaveInstanceState directly is not standard practice.
            recreate();
        }
    }

    @Override
    public void onBackPressed() {
        if (is_dialer_open) {
            if (DialerHelper != null) DialerHelper.finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVisibilityChange(boolean isopen) {
        this.is_dialer_open = isopen;
        View mainCallButton = findViewById(R.id.main_call); // Re-fetch or ensure call_button is not null
        if (mainCallButton != null) {
            if (isopen) {
                if (mViewPager != null) mViewPager.setMoveEnabled(true); // Should be false if dialer is open?
                mainCallButton.setVisibility(View.GONE);
            } else {
                mainCallButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public DialerActionModeHelper getDialerAction() {
        return DialerHelper;
    }

    @Override
    public void onCall(CharSequence number) {
        // TODO: CRITICAL - Add runtime permission check for Manifest.permission.CALL_PHONE before dispatching ACTION_CALL.
        // Example:
        // if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
        //     Intent callIntent = new Intent(Intent.ACTION_CALL);
        //     callIntent.setData(Uri.fromParts("tel", String.valueOf(number), null));
        //     startActivity(callIntent);
        // } else {
        //     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, YOUR_REQUEST_CODE_CALL_PHONE);
        // }
        // onRequestPermissionsResult would then handle the result and make the call if granted.

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.fromParts("tel", String.valueOf(number), null));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Log.e("MainActivity", "CALL_PHONE permission not granted or intent couldn't be handled.", e);
            Toast.makeText(this, "Cannot make call. Permission missing or no app can handle the call.", Toast.LENGTH_LONG).show();
            // Optionally, could redirect to ACTION_DIAL here if ACTION_CALL fails due to permission (though DIAL is less direct)
            // Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            // dialIntent.setData(Uri.fromParts("tel", String.valueOf(number), null));
            // startActivity(dialIntent);
        }
    }

    @Override
    public Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    public boolean getDialerState() {
        return is_dialer_open;
    }

    @Override
    public void onNumberChange(String v, int n) {
        if (mViewPager != null && n < 3) { // Assuming 3 is a valid upper bound for network tabs
            mViewPager.setCurrentItem(n);
        }
        onQueryTextChange(v); // This might trigger search based on number change
    }
}
