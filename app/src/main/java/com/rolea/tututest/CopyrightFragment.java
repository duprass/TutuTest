package com.rolea.tututest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rolea.tututest.helpers.ToolbarManipulation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CopyrightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CopyrightFragment extends Fragment {

    private ToolbarManipulation mListener;

    public CopyrightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CopyrightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CopyrightFragment newInstance() {
        CopyrightFragment fragment = new CopyrightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_copyright, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarManipulation) {
            mListener = (ToolbarManipulation) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChooseStationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.setTitle(getString(R.string.copyright));
    }
}
