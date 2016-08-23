package xobyx.xcontactj.until;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.fragments.DialerFragment;

/**
 * Created by xobyx on 8/6/2015.
 * For xobyx.xcontactj.until/XContactj
 */
public class DialerActionModeHelper  {


    private final Vibrator vibrator;
    private ActionMode mActionMode;
    private TextView number;
    private ImageView img;
    private MainActivity mContext;
    private NumberChangeListener mNumberChangeListener;
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

            if (net < 3) {
                img.setImageResource(ME.NetDrawables[net][0]);
                if (mNumberChangeListener != null)
                    mNumberChangeListener.onNumberChange(s.toString());
            } else
                img.setImageDrawable(null);


        }


    };

    public DialerActionModeHelper(MainActivity i) {
        mContext = i;
        mNumberChangeListener=i.NumberChangeListener;
        vibrator= (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    public void StartDialerActionMode(String dataString) {


        setNumber(dataString, false);
        ((AppCompatActivity) mContext).getSupportActionBar().startActionMode(callback);
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


            } else {
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

    ActionMode.Callback callback=new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            final DialerFragment fragment = DialerFragment.newInstance(MainActivity.WN_ID);
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
            mNumberChangeListener.onNumberChange("");
            final FragmentManager fm = mContext.getSupportFragmentManager();
            fm.beginTransaction().setCustomAnimations(android.support.design.R.anim.abc_slide_in_top,android.support.design.R.anim.abc_slide_out_bottom).remove(fm.findFragmentByTag("Dialer")).commit();

        }
    };
    public String getNumber() {
        return String.valueOf(number.getText());
    }

    public interface NumberChangeListener {
        void onNumberChange(String v);

    }
}
