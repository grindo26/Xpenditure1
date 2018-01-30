package com.xpenditure.www.xpenditure;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.xpenditure.www.xpenditure.R.id.profileImage;


/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_INTENT = 100;
    Button buttonlogout;
    EditText editTextEmail;
    EditText editTextPassword;
    ;
    TextView textViewEmailid;
    Button textViewChangeDP;
    TextView textViewFnmae;
    TextView textViewLname;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    ImageView imageViewprofileImage;
    private FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference mStorageRefrence;
    private DatabaseReference databaseReference;
    Firebase firebase;
    Uri uri;

    public AccountFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);


        textViewEmailid = (TextView) rootView.findViewById(R.id.viewEmailId);
        textViewChangeDP = (Button) rootView.findViewById(R.id.ChangeDP);
        textViewLname = (TextView) rootView.findViewById(R.id.viewLname);
        textViewFnmae = (TextView) rootView.findViewById(R.id.viewFname);
        buttonlogout = (Button) rootView.findViewById(R.id.buttonlogout);
        imageViewprofileImage = (ImageView) rootView.findViewById(profileImage);
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
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");

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

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT);

            }
        });
//        progressDialog.setMessage("Signng Out ");

        mAuth = FirebaseAuth.getInstance();
        firebase = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/" + uid);






        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                String Fname = map.get("First Name");
                String Email = map.get("Email ID");
                String Image = map.get("Image");
                String Lname = map.get("Last Name");

                Log.v("E_VALUE", "First Name: " + Fname);
                Log.v("E_VALUE", "Email ID: " + Email);
                Log.v("E_VALUE", "Image: " + Image);
                Log.v("E_VALUE", "Last Name: " + Lname);

                textViewLname.setText("Last Name : "+Lname);
                textViewFnmae.setText("First Name : "+Fname);
                Picasso.with(AccountFragment.this.getActivity()).load(Image).into(imageViewprofileImage);



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            uri = data.getData();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + uid );
            mStorageRefrence = FirebaseStorage.getInstance().getReference();
            StorageReference filePath = mStorageRefrence.child("uid").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri = taskSnapshot.getDownloadUrl();
                    String imageuri = downloaduri.toString().trim();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    DatabaseReference currentUser = databaseReference.child(mAuth.getCurrentUser().getUid());
                    currentUser.child("Image").setValue(imageuri);
                    Picasso.with(AccountFragment.this.getActivity()).load(imageuri).into(imageViewprofileImage);


                }
            });


        }
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


