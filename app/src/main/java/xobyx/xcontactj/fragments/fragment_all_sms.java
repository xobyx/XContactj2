package xobyx.xcontactj.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xobyx.xcontactj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_all_sms extends Fragment {


    public fragment_all_sms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_all_sms, container, false);
    }


    public static Fragment newInstance() {
        return new fragment_all_sms();
    }
}
