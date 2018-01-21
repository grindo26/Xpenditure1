package com.xpenditure.www.xpenditure;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    Button buttonReg;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextLname;
    EditText editTextFnmae;
    EditText editTextMobile;
    TextView textViewLoginText;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        //code idhar


        buttonReg = (Button) rootView.findViewById(R.id.buttonReg);
        editTextEmail = (EditText) rootView.findViewById(R.id.regemail);
        editTextPassword = (EditText) rootView.findViewById(R.id.regpassword);
        editTextFnmae = (EditText) rootView.findViewById(R.id.Fname);
        editTextLname = (EditText) rootView.findViewById(R.id.Lname);
//        editTextMobile = (EditText) rootView.findViewById(R.id.phoneNo);
        textViewLoginText = (TextView) rootView.findViewById(R.id.loginText);
        progressDialog = new ProgressDialog(this.getActivity());


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };


        buttonReg.setOnClickListener(this);
        textViewLoginText.setOnClickListener(this);



        return rootView;

    }

    @Override
    public void onClick(View view) {

        if (view == buttonReg) {
            registeruser();
        }

        if (view == textViewLoginText) {
            //login activity
            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, loginFragment);
            fragmentTransaction.commit();
        }


    }

    private void registeruser() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String Fname = editTextFnmae.getText().toString().trim();
        final String Lname = editTextLname.getText().toString().trim();
//        String mobile= editTextMobile.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            //eamil is empty
            Toast.makeText(this.getActivity(), "Password shoulde be grater than 8 characters!", Toast.LENGTH_SHORT).show();
            return;
        } if (TextUtils.isEmpty(Fname)) {
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Password!", Toast.LENGTH_SHORT).show();
            return;
        } if (TextUtils.isEmpty(Lname)) {
            //eamil is empty
            Toast.makeText(this.getActivity(), "Please enter Password!", Toast.LENGTH_SHORT).show();
            return;
        }
// if (TextUtils.isEmpty(mobile)) {
//            //eamil is empty
//            Toast.makeText(this.getActivity(), "Please enter Password!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (mobile.length() != 10) {
//            //eamil is empty
//            Toast.makeText(this.getActivity(), "Password shoulde be grater than 8 characters!", Toast.LENGTH_SHORT).show();
//            return;
//        }


        // if validation is ok
        progressDialog.setMessage("Registering user");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterFragment.this.getActivity(), "Registered sucessfully", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();


                    String Fname = editTextFnmae.getText().toString().trim();
                    String Lname = editTextLname.getText().toString().trim();
//                    String mobile= editTextFnmae.getText().toString().trim();
                    String email = editTextEmail.getText().toString().trim();

//                    userInformation userInformation= new userInformation();

//                    FirebaseUser user = mAuth.getCurrentUser();


                    //saving user data into database
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    DatabaseReference currentUser = databaseReference.child(mAuth.getCurrentUser().getUid());
                    currentUser.child("First Name").setValue(Fname);
                    currentUser.child("Last Name").setValue(Lname);
                    currentUser.child("Email ID").setValue(email);
                    currentUser.child("Image").setValue("Default");
                   Toast.makeText(RegisterFragment.this.getActivity(),"Information Saved!!",Toast.LENGTH_LONG).show();


                    //Going to login Fragment
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, loginFragment);
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else {
                    Toast.makeText(RegisterFragment.this.getActivity(), "Registeration unsucessful", Toast.LENGTH_SHORT).show();
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

    private void saveUserInformation() {


    }


}

