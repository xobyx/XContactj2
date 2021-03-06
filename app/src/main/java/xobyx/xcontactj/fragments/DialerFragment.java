package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import xobyx.xcontactj.R;
import xobyx.xcontactj.base.IDialerHandler;
import xobyx.xcontactj.until.AppAnimations;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.m;

/**
 * f Link
 */
public class DialerFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_NET = "net";


    private TableLayout mRel;

    public DialerActionModeHelper getActionMode() {
        if(actionMode==null)
            actionMode=dialerHandler.getDialerAction();

        return actionMode;
    }

    private DialerActionModeHelper actionMode;
    private IDialerHandler dialerHandler;
    private boolean visible = true;
    private View.OnClickListener CallClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialerHandler.onCall(getActionMode().getNumber());
        }
    };

    public static DialerFragment newInstance(int worked_net) {
        DialerFragment in = new DialerFragment();
        Bundle s = new Bundle();
        s.putInt(ARG_NET, worked_net);

        in.setArguments(s);


        return in;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialer, null);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppAnimations.Rotate3dAnimation df = new AppAnimations.Rotate3dAnimation(180, 0, 0, 0, 20, false);
        df.setDuration(500);

        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.support.v7.appcompat.R.anim.abc_slide_in_bottom));

        mRel = (TableLayout) view.findViewById(R.id.dialer_numbers);
        ImageView dial = (ImageView) view.findViewById(R.id.dill_call);

        dial.setOnClickListener(CallClickListener);
        view.findViewById(R.id.dill_collsop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mRel.getChildCount()-1; i++)
                    mRel.getChildAt(i).setVisibility(visible ? View.GONE : View.VISIBLE);
                visible = !visible;

            }
        });

        for (int i = 0; i < mRel.getChildCount()-1; i++) {
            for (int s = 0; s < 3; s++)
                ((TableRow) mRel.getChildAt(i)).getVirtualChildAt(s).setOnClickListener(this);

        }
        view.findViewById(R.id.dell_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionMode().backspace();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialerHandler=((IDialerHandler) getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialerHandler.onVisibilityChange(true);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        dialerHandler.onVisibilityChange(false);
    }

    @Override
    public void onClick(View v) {
        if (getActionMode() != null)
            actionMode.setNumber((String) v.getTag(),false);
        else {
            Toast d = Toast.makeText(
                    getActivity().getBaseContext()
                    , "--Device is Not connected to any Network.."
                    , Toast.LENGTH_SHORT);
            d.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            d.show();
        }
    }


    public void setColor(int net) {
        m view = ((m) getView().findViewById(R.id.dialer_network));
        if(net==3)
            view.setColor(0xFF4081);
        else
            view.setColor(ME.nColors[net]);
    }
}
