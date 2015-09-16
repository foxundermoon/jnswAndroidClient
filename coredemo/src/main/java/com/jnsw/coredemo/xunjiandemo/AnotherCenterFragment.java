package com.jnsw.coredemo.xunjiandemo;

import android.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jnsw.coredemo.R;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by fox on 2015/9/16.
 */

@EFragment(R.layout.fragment_another_center)
public class AnotherCenterFragment extends Fragment {
    @ViewById(R.id.radio_group)
    RadioGroup radioGroup;
    @ViewById(R.id.your_mother)
    RadioButton yourmother;
    @ViewById
    RadioButton your_father, your_gf, your_self;


    public  RadioButton  getSelectedRadioButton() {
        int id=   radioGroup.getCheckedRadioButtonId();
        return (RadioButton) getView().findViewById(id);
    }


}
