package com.xpenditure.www.xpenditure;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment implements View.OnClickListener{

    Button buttonlogout;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewEmailid;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

//        firebaseAuth = FirebaseAuth.getInstance();

        textViewEmailid = (TextView) rootView.findViewById(R.id.viewEmailId);
        buttonlogout = (Button) rootView.findViewById(R.id.buttonlogout);

//        if(firebaseAuth.getCurrentUser() == null){
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frameLayout,new RegisterFragment() );
//            fragmentTransaction.commit();
//        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewEmailid.setText("Welcome :" + user.getEmail() );

        buttonlogout.setOnClickListener(this);
//        progressDialog.setMessage("Signng Out ");





        return rootView;

    }

    @Override
    public void onClick(View view) {
        if(view == buttonlogout){
            progressDialog.show();
            firebaseAuth.signOut();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new LoginFragment());
            fragmentTransaction.commit();

//            progressDialog.dismiss();
        }
    }
}


