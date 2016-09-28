package xobyx.xcontactj.until;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.base.IDialerHandler;
import xobyx.xcontactj.fragments.DialerFragment;

/**
 * Created by xobyx on 8/6/2015.
 * For xobyx.xcontactj.until/XContactj
 */
public class DialerActionModeHelper {


    private final Vibrator vibrator;
    private ActionMode mActionMode;
    private TextView number;
    private ImageView img;
    private AppCompatActivity mContext;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        //TODO:add search function also...
        @Override
        public void afterTextChanged(Editable s) {


            int net = ME.getNetForNumber(s.toString());


            img.setImageResource(ME.NetDrawables[net][0]);
            if (mContext != null)
                ((IDialerHandler) mContext).onNumberChange(s.toString(),net);
                ((DialerFragment) mContext.getSupportFragmentManager().findFragmentByTag("Dialer")).setColor(net);


        }


    };
    private Toolbar actionBar;

    public DialerActionModeHelper(AppCompatActivity i) {
        mContext = i;

        vibrator = (Vibrator) i.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public void StartDialerActionMode(String dataString) {


        //mContext.getSupportActionBar().hide();

        ((IDialerHandler) mContext).getToolBar().setVisibility(View.GONE);
        mContext.startSupportActionMode(callback);
        setNumber(dataString, false);

        //startSupportActionMode(callback);


    }

    public void finish() {

        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    public void setNumber(String f, boolean replace) {

        if (f != null && !f.equals("")) {
            if (replace) {
                number.setText(f);


            }
            else {
                number.append(f);
                vibrator.vibrate(20);
            }
            //mNumberChangeListener.onNumberChange(f);
        }

    }

    public void backspace() {
        vibrator.vibrate(20);
        final CharSequence text = number.getText();

        if (text.length() > 0) {
            final CharSequence subSequence = text.subSequence(0, text.length() - 1);
            number.setText(subSequence);

        }

    }

    ActionMode.Callback callback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            final DialerFragment fragment = DialerFragment.newInstance(MainActivity.wn_id);
            mContext.getSupportFragmentManager().beginTransaction().replace(R.id.di_ground, fragment, "Dialer").commit();
            mode.setCustomView(LayoutInflater.from(mContext).inflate(R.layout.cot_action_mode, null));
            number = (TextView) mode.getCustomView().findViewById(R.id.cot_ac_textView);
            img = (ImageView) mode.getCustomView().findViewById(R.id.cot_ac_netImage);
            img.setImageDrawable(null);
            number.addTextChangedListener(mTextWatcher);


            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ((IDialerHandler) mContext).onNumberChange("",4);
            ((IDialerHandler) mContext).getToolBar().setVisibility(View.VISIBLE);
            final FragmentManager fm = mContext.getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(R.anim.snackbar_in, R.anim.snackbar_out).remove(fm.findFragmentByTag("Dialer")).commit();

        }
    };

    public String getNumber() {
        return String.valueOf(number.getText());
    }

    public interface NumberChangeListener {


    }
}
