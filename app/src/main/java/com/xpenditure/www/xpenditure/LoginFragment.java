package com.xpenditure.www.xpenditure;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment  implements View.OnClickListener{

    private Button buttonLogin;
    private EditText editTextemail;
    private EditText editTextpassword;
    private TextView textViewreg;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    FragmentTransaction fragmentTransaction;
    Toolbar toolbar;
    ActionBar actionBar;



    public LoginFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
//        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        progressDialog = new ProgressDialog(this.getActivity());
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    AccountFragment accountFragment = new AccountFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, accountFragment);
                   // fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Account");

                }
                // ...
            }
        };

            editTextemail = (EditText) rootView.findViewById(R.id.loginemail);
            editTextpassword = (EditText) rootView.findViewById(R.id.loginpassword);
            textViewreg = (TextView) rootView.findViewById(R.id.RegText);
            buttonLogin = (Button) rootView.findViewById(R.id.buttonlogin);

            buttonLogin.setOnClickListener(this);
            textViewreg.setOnClickListener(this);








        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            loginUser();
        }

        if (view == textViewreg) {
            //reg activity
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, registerFragment );
            fragmentTransaction.commit();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Register");

        }
    }

    private void loginUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Email!" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Password!" ,Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loggin In");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){

                    Toast.makeText(LoginFragment.this.getActivity(), "login Sucessfull!!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();

                    AccountFragment accountFragment = new AccountFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, accountFragment);
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Account");



            }
            else {
                    Toast.makeText(LoginFragment.this.getActivity(), "login unsucessfull!!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }

            }

        });


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
