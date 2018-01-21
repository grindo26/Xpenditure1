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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    Button buttonlogout;
    EditText editTextEmail;
    EditText editTextPassword;
    ;
    TextView textViewEmailid;
    TextView textViewChangeDP;
    TextView textViewFnmae;
    TextView textViewLname;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    ImageView imageViewprofileImage;
    private FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference mStorage;
    private DatabaseReference databaseReference;
    Firebase firebase;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);


        textViewEmailid = (TextView) rootView.findViewById(R.id.viewEmailId);
        textViewChangeDP = (TextView) rootView.findViewById(R.id.ChangeDP);
        textViewLname = (TextView) rootView.findViewById(R.id.viewLname);
        textViewFnmae = (TextView) rootView.findViewById(R.id.viewFname);
        buttonlogout = (Button) rootView.findViewById(R.id.buttonlogout);
        imageViewprofileImage = (ImageView) rootView.findViewById(R.id.profileImage);
        Map<String, String> map = new Map<String, String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object o) {
                return false;
            }

            @Override
            public boolean containsValue(Object o) {
                return false;
            }

            @Override
            public String get(Object o) {
                return null;
            }

            @Override
            public String put(String s, String s2) {
                return null;
            }

            @Override
            public String remove(Object o) {
                return null;
            }

            @Override
            public void putAll(Map<? extends String, ? extends String> map) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<String> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, String>> entrySet() {
                return null;
            }
        };
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
        String uid = user.getUid();

        buttonlogout.setOnClickListener(this);
        textViewChangeDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountFragment.this.getActivity(), "This function is under construction", Toast.LENGTH_LONG).show();
            }
        });
//        progressDialog.setMessage("Signng Out ");

        mAuth = FirebaseAuth.getInstance();
        firebase = new Firebase("https://xpenditurespr.firebaseio.com/users/" + uid);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String Fname = map.get("First Name");
                String Email = map.get("Email ID");
                String Image = map.get("Image");
                String Lname = map.get("Last Name");

                Log.v("E_VALUE", "First Name : " + Fname);
                Log.v("E_VALUE", "Email ID : " + Email);
                Log.v("E_VALUE", "Image : " + Image);
                Log.v("E_VALUE", "Last Name : " + Lname);

                textViewLname.setText(Lname);
                textViewFnmae.setText(Fname);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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


