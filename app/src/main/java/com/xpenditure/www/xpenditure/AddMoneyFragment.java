package com.xpenditure.www.xpenditure;

/**
 * Created by ADMIN on 30-01-2018.
 */
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.w3c.dom.Text;

public class AddMoneyFragment extends Fragment {

    private RadioGroup categories_selection;
    private EditText amount;
    private Button addButton;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_money_fragment,container, false);

        categories_selection = (RadioGroup) categories_selection;
        amount = (EditText) amount;
        addButton = (Button) addButton;



        return view;

    }
}
