package com.xpenditure.www.xpenditure;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    Button buttonlogout;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewEmailid;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);


        textViewEmailid = (TextView) rootView.findViewById(R.id.viewEmailId);
        buttonlogout = (Button) rootView.findViewById(R.id.buttonlogout);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, loginFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                // ...
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        textViewEmailid.setText("Welcome :" + user.getEmail());

        buttonlogout.setOnClickListener(this);
//        progressDialog.setMessage("Signng Out ");


        return rootView;

    }

    @Override
    public void onClick(View view) {
        if (view == buttonlogout) {
//            progressDialog.show();
            mAuth.signOut();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, new LoginFragment());
            fragmentTransaction.commit();

//         progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

}


