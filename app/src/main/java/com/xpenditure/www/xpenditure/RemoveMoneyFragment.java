package com.xpenditure.www.xpenditure;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveMoneyFragment extends Fragment {

    Firebase firebase;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Toolbar toolbar;
    public RecyclerView recyclerView;

    DatabaseReference databaseReferance;

    public RemoveMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        //code idhar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView) ;
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


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
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");

                }
                // ...
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        databaseReferance = FirebaseDatabase.getInstance().getReference().child("/users/"+uid+"/Category");
        FirebaseRecyclerAdapter<RemoveRecycler ,RemoveRecyclerViewHolder > adapter= new FirebaseRecyclerAdapter<RemoveRecycler, RemoveRecyclerViewHolder>(
                RemoveRecycler.class,
                R.layout.remove_cards,
                RemoveRecyclerViewHolder.class,
                databaseReferance
        ) {
            @Override
            protected void populateViewHolder(RemoveRecyclerViewHolder viewHolder, RemoveRecycler model, int position) {
                viewHolder.setTitle(model.getTitle());
            }
        };
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    public static class RemoveRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView CatTitle;


        public RemoveRecyclerViewHolder(View catgview) {
            super(catgview);
            CatTitle = (TextView) catgview.findViewById(R.id.categoryNameDisplay);


        }


        public void setTitle(String title) {
            CatTitle.setText(title);
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




