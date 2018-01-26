package com.xpenditure.www.xpenditure;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategoryFragment extends Fragment {
    Firebase firebase;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    EditText etinputCategories;
    Button buttonAddCategories;

    public AddCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_category, container, false);

        etinputCategories = (EditText) rootView.findViewById(R.id.etaddCategory);
        buttonAddCategories =(Button) rootView.findViewById(R.id.btnAddCategories);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        buttonAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String category = etinputCategories.getText().toString().trim();

                if(TextUtils.isEmpty(category))
                {
                    Toast.makeText(AddCategoryFragment.this.getActivity(), "Please enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/Category");
                DatabaseReference currentUser = databaseReference.child(mAuth.getCurrentUser().getUid());
                databaseReference.child("category").setValue(category);
                Toast.makeText(AddCategoryFragment.this.getActivity(), "Category Added Sucessfully", Toast.LENGTH_SHORT).show();

               CategoriesFragment categoriesFragment = new CategoriesFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, categoriesFragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categories");

            }
        });


        return rootView;
    }

}
