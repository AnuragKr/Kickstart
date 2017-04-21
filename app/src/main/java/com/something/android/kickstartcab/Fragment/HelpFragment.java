package com.something.android.kickstartcab.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.something.android.kickstartcab.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment implements View.OnClickListener {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        try {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_left);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Config.Help_Category);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }catch(Throwable e){
            e.printStackTrace();
        }
        Button b1 = (Button) view.findViewById(R.id.expandableButton1);
        expandableLayout1 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout1);
        b1.setOnClickListener(this);

        Button b2 = (Button) view.findViewById(R.id.expandableButton2);
        expandableLayout2 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout2);
        b2.setOnClickListener(this);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.expandableButton1:
                try{
                    expandableLayout1.toggle();
                } catch(Throwable e){
                    e.printStackTrace();
                }
                break;
            case R.id.expandableButton2:
                try{
                    expandableLayout2.toggle();
                } catch(Throwable e){
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

}
