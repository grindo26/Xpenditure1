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
public class CategoriesFragment extends Fragment {

    Firebase firebase;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Toolbar toolbar;
    Button btnaddred;
    public RecyclerView recyclerView;

    DatabaseReference databaseReferance;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        //code idhar
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView) ;
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
        btnaddred = (Button) rootView.findViewById(R.id.Add_category);

        btnaddred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCategoryFragment addCategoryFragment= new AddCategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, addCategoryFragment);
                // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Categories");
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        databaseReferance = FirebaseDatabase.getInstance().getReference().child("/users/"+uid+"/Category");
        FirebaseRecyclerAdapter<CategoriesRecycler ,CategoriesRecyclerViewHolder > adapter= new FirebaseRecyclerAdapter<CategoriesRecycler, CategoriesRecyclerViewHolder>(
                CategoriesRecycler.class,
                R.layout.category_cards,
                CategoriesRecyclerViewHolder.class,
                databaseReferance
        ) {
            @Override
            protected void populateViewHolder(CategoriesRecyclerViewHolder viewHolder, CategoriesRecycler model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(model.getImage());
            }
        };
            recyclerView.setAdapter(adapter);

        return rootView;
    }

    public static class CategoriesRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView CatTitle;
        ImageView CatImage;


        public CategoriesRecyclerViewHolder(View catgview){
            super(catgview);
            CatTitle = (TextView) catgview.findViewById(R.id.categoryNameDisplay);
            CatImage = (ImageView) catgview.findViewById(R.id.categoryImageDisplay);

        }


        public void setTitle(String title) {
            CatTitle.setText(title);
        }

        public void setImage(String image) {
            Picasso.with(itemView.getContext()).load(image).into(CatImage);
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




