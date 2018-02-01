package com.xpenditure.www.xpenditure;

/**
 * Created by ADMIN on 30-01-2018.
 */
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
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
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class AddMoneyFragment extends Fragment {

    private RadioGroup categories_selection;
    private TextView amount;
    private Button addButton;
    private EditText Value;
    Integer total;
    Firebase mref;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.add_money_fragment,container, false);
         Value = (EditText) rootView.findViewById(R.id.Value);
         addButton= (Button) rootView.findViewById(R.id.addButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mref = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Total");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total= dataSnapshot.getValue(Integer.class);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

         addButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String entered = Value.getText().toString();
                 Integer Value= Integer.parseInt(entered);
                 total= total + Value;
                 mref.setValue(total);
                 HomeFragment homeFragment = new HomeFragment();
                 FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.frameLayout, homeFragment);
                 fragmentTransaction.commit();

             }
         });








        return rootView;

    }
}
