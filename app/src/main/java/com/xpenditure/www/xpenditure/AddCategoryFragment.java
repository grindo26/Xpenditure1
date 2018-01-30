package com.xpenditure.www.xpenditure;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategoryFragment extends Fragment implements View.OnClickListener {
    private static final int GALLERY_INTENT = 100;
    Firebase firebase;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Button buttonAddCategories;
    TextView chooseImage;
    private LinearLayout categoryList;
    ImageView categoryImage;
    EditText category_name;
    private StorageReference mStorageRefrence;
    private FirebaseAuth.AuthStateListener mAuthListener;



    private static final int MIN_Category = 2;
    private static final int MAX_Category = 10;



    Uri uri;


    public AddCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_category, container, false);

        buttonAddCategories = (Button) rootView.findViewById(R.id.btnAddCategories);
//        categoryList = (LinearLayout) rootView.findViewById(CategoryView);
        chooseImage = (TextView) rootView.findViewById(R.id.chooseImage);
        categoryImage = (ImageView) rootView.findViewById(R.id.categoryImage);
        category_name = (EditText) rootView.findViewById(R.id.category_name);
        mStorageRefrence = FirebaseStorage.getInstance().getReference();
        final CountManager countManager = new CountManager();

//        CategoryView view;
//        if (savedInstanceState != null) {
//            List<String> CategoryNames = savedInstanceState.getStringArrayList("categoryNames");
//            if (CategoryNames != null) {
//                for (String categoryName : CategoryNames) {
//                    view = addcategoryRow();
//                    view.CategoryName.setText(categoryName);
//                }
//            }
//        }
//        createOrDeletePersonRows();


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();


        buttonAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<String> catagories = getCategoryNames();
//
//                validate();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users/" + uid + "/Category");
                mStorageRefrence = FirebaseStorage.getInstance().getReference();

//                DatabaseReference currentCategory = databaseReference.child("Category");

//                Set<String> categoryNames = new HashSet<>();


                Uri image = uri;
                final String name = category_name.getText().toString().trim();
//
// for (int i = 0; i < categoryViews.size(); i++) {
//                    String name = getCategoryName(i);


                if (name.length() > 0 && image != null) {
                    mAuth = FirebaseAuth.getInstance();
                    user = FirebaseAuth.getInstance().getCurrentUser();

                    firebase = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/" + uid);

                    firebase.addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                                           Map<Integer, Integer> map = dataSnapshot.getValue(Map.class);
                                                           int countCat = map.get("Category Count");


                                                           Log.v("E_VALUE", "Category Count " + countCat);

                                                           countManager.getCountCategories(countCat);

                                                       }

                                                       @Override
                                                       public void onCancelled(FirebaseError firebaseError) {

                                                       }
                                                   });


                        StorageReference filePath = mStorageRefrence.child("Photos").child(image.getLastPathSegment());
                    filePath.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            int count = countManager.returnCategoryCount();
                            Uri downloaduri = taskSnapshot.getDownloadUrl();
                            String imageuri = downloaduri.toString().trim();
                            DatabaseReference currentCategory = databaseReference.child("Category" + count);
                            currentCategory.child("Title").setValue(name);
                            currentCategory.child("Image").setValue(imageuri);
                            count++;
                            countManager.setCountCategories( count );
                        }
                    });
                    Toast.makeText(AddCategoryFragment.this.getActivity(), "Category Added Sucessfully", Toast.LENGTH_SHORT).show();


                    CategoriesFragment categoriesFragment = new CategoriesFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, categoriesFragment);
                    fragmentTransaction.commit();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Categories");

                } else {
                    Toast.makeText(AddCategoryFragment.this.getActivity(), "Choose Image & Fill the Details", Toast.LENGTH_SHORT).show();

                }
//
//                }

            }


        });


        chooseImage.setOnClickListener(this);

        return rootView;
    }


    // NAYA PERSON ADD KARTE H TO ROW GENRATE HOTA H JATA H NAA USKE LIYE
//    private CategoryView addcategoryRow() {
//        //NEW ROW ADD KARTA JAYGA
//        final View view = getActivity().getLayoutInflater().inflate(R.layout.categoryview, categoryList, false);
//        categoryList.addView(view);
//        //VIEW CREATE KIYA
//        final CategoryView CategoryView = new CategoryView();
//        CategoryView.view = view;
//        //VIEW ADD KIYA
//        CategoryView.CategoryName = (EditText) view.findViewById(R.id.category_name);
//        //VIEW DELETE KIYA
//        CategoryView.deleteButton = (ImageView) view.findViewById(R.id.delete_button);
//        categoryViews.add(CategoryView);
//
//        CategoryView.CategoryName.setId(-1); // do not restore in onRestoreInstanceState()
//        //DEKHTA H KI KAB ADD KARNEKA NAYA ROW
//        CategoryView.CategoryName.addTextChangedListener(new TextWatcher() {
//            @Override
//            //BAKWAS BT MANDATORY ISLEA CODE ME H
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            //BAKWAS BT MANDATORY ISLEA CODE ME H
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            //CREATE AND DELETE ROWS(UPAR H)VALE FUNCTION KO CALL KIYA
//            public void afterTextChanged(Editable s) {
//                createOrDeletePersonRows();
//            }
//        });
//
//        CategoryView.CategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            //YE NAI SAMJHA
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    createOrDeletePersonRows();
//            }
//        });
//
//        // X VALA RED DELETE KA BUTTON (APP DEKH)
//        CategoryView.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletePersonRow(CategoryView);
//                createOrDeletePersonRows();
//            }
//        });
//        return CategoryView;
//    }
//
//    private void createOrDeletePersonRows() {
//        int numEmpty = 0;
//        int i = 0;
//        //CHK KAREGA KI KAM SE KAM 2 PERSON CHAHIYE KARKE
//        while (i < categoryViews.size() &&/*2 ROWS KAM SE KAM STARTING ME DISPLAY KAREGA*/ categoryViews.size() >= 1) {
//            CategoryView categoryView = categoryViews.get(i++);
//            String name = categoryView.CategoryName.getText().toString().trim();
//            if (name.length() == 0) {//AGAR KUCH NAAM NAI BHARA TO DELETE KAREGA ROWS KO
//                if (!categoryView.CategoryName.hasFocus()) {
//                    deletePersonRow(categoryView);
//                    i--;
//                } else {
//                    numEmpty++;
//                }
//            }
//        }
//        //VIEWS KA SIZE MIN PERSON SE JYADA N MAX PERSON SE KAM HUA TO ROW ADD KAREGA
//        while ((categoryViews.size() < MIN_Category) ||
//                (numEmpty < 1 && categoryViews.size() < MAX_Category))
//        {
//            addPersonRow();
//            numEmpty++;
//        }
//        //ROW VISIBLE INVISIBLE
//        for (i = 0; i < categoryViews.size(); i++) {
//            CategoryView categoryView = categoryViews.get(i);
//            boolean last = (i + 1 == categoryViews.size());
//            categoryView.deleteButton.setVisibility(last ? View.INVISIBLE : View.VISIBLE);
//        }
//    }
//    private void deletePersonRow(CategoryView row) {
//        categoryList.removeView(row.view);
//        categoryViews.remove(row);
//    }
//    private CategoryView addPersonRow() {
//        //NEW ROW ADD KARTA JAYGA
//        final View view = getActivity().getLayoutInflater().inflate(R.layout.categoryview, categoryList, false);
//        categoryList.addView(view);
//        //VIEW CREATE KIYA
//        final CategoryView categoryView = new CategoryView();
//        categoryView.view = view;
//        //VIEW ADD KIYA
//        categoryView.CategoryName = (EditText) view.findViewById(R.id.category_name);
//        //VIEW DELETE KIYA
//        categoryView.deleteButton = (ImageView) view.findViewById(R.id.delete_button);
//        categoryViews.add(categoryView);
//
//        categoryView.CategoryName.setId(-1); // do not restore in onRestoreInstanceState()
//        //DEKHTA H KI KAB ADD KARNEKA NAYA ROW
//        categoryView.CategoryName.addTextChangedListener(new TextWatcher() {
//            @Override
//            //BAKWAS BT MANDATORY ISLEA CODE ME H
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            //BAKWAS BT MANDATORY ISLEA CODE ME H
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            //CREATE AND DELETE ROWS(UPAR H)VALE FUNCTION KO CALL KIYA
//            public void afterTextChanged(Editable s) {
//                createOrDeletePersonRows();
//            }
//        });
//
//        categoryView.CategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            //YE NAI SAMJHA
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    createOrDeletePersonRows();
//            }
//        });
//
//        // X VALA RED DELETE KA BUTTON (APP DEKH)
//        categoryView.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletePersonRow(categoryView);
//                createOrDeletePersonRows();
//            }
//        });
//        return categoryView;
//    }
//    private String getCategoryName(int i) {
//        CategoryView categoryView = categoryViews.get(i);
//        return categoryView.CategoryName.getText().toString().trim();
//    }
//
//    private ArrayList<String> getCategoryNames() {
//        ArrayList<String> categorynames = new ArrayList<>();
//        for (int i = 0; i <categoryViews.size(); i++) {
//            String name = getCategoryName(i);
//            if (name.length() > 0)
//                categorynames.add(name);
//        }
//        return categorynames ;
//    }
//
//    private boolean validate() {
//        final Resources res = getResources();
//
//        boolean valid = true;
//
//        //AGAR TRIP ME PEHELE SI HI LOG H TO VO SHOW KAREGA
//        Set<String> categoryNames = new HashSet<>();
//        for (int i = 0; i < categoryViews.size(); i++) {
//            String name = getCategoryName(i);
//            if (name.length() > 0) {
//                if (categoryNames.contains(name)) {
//                    CategoryView categoryView = categoryViews.get(i);
//                    categoryView.CategoryName.setError(res.getString(R.string.validate_duplicate_name));
//                    valid = false;
//                } else {
//                    categoryNames.add(name);
//                }
//            }
//        }
//        //AGAR 2 SE KAM PERSON K NAAM BHARE TO ERROR DIKHAYGA
//        if (valid && categoryNames.size() < MIN_Category) {
//            CategoryView categoryView = categoryViews.get(categoryViews.size() - 1);
//            String format = res.getString(R.string.validate_min_names);
//            categoryView.CategoryName.setError(String.format(format, MIN_Category));
//            valid = false;
//        }
//
//        return valid;
//    }

    @Override
    public void onClick(View view) {
        if (view == chooseImage) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");

            startActivityForResult(intent, GALLERY_INTENT);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            uri = data.getData();
            Picasso.with(this.getActivity()).load(uri).into(categoryImage);

        }
    }
}

