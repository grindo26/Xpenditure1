package com.xpenditure.www.xpenditure;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCategoryFragment extends Fragment {
    Firebase firebase;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Button buttonAddCategories;
    private LinearLayout categoryList;


    private static final int MIN_Category = 2;
    private static final int MAX_Category = 10;

    private class CategoryView {
        public View view;
        public EditText CategoryName;
        public ImageView deleteButton;
    }
    private final List<CategoryView> categoryViews = new ArrayList<>();


    public AddCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_category, container, false);

        buttonAddCategories =(Button) rootView.findViewById(R.id.btnAddCategories);
        categoryList = (LinearLayout) rootView.findViewById(R.id.CategoryView);


        if (savedInstanceState != null) {
            List<String> CategoryNames = savedInstanceState.getStringArrayList("categoryNames");
            if (CategoryNames != null) {
                for (String categoryName : CategoryNames) {
                    CategoryView view = addcategoryRow();
                    view.CategoryName.setText(categoryName);
                }
            }
        }
        createOrDeletePersonRows();




        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();



        buttonAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> catagories = getCategoryNames();

                validate();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/Category");
                DatabaseReference currentCategory = databaseReference.child("Category");
                Set<String> categoryNames = new HashSet<>();
                for (int i = 0; i < categoryViews.size(); i++) {
                    String name = getCategoryName(i);
                    if(name.length() > 0)
                    {
                        databaseReference.child("category"+i).setValue(name);
                    }

                }
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
//        private void save() {
//            String title = getCalculationTitle();
//            Currency currency = (Currency) currencyField.getSelectedItem();
//            List<String> personNames = getPersonNames();
//
//            DataBaseHelper dbHelper = new DataBaseHelper(this);
//            CalculationDataSource dataSource = new CalculationDataSource(dbHelper);
//            Calculation calculation = dataSource.createCalculation(title, currency.getCurrencyCode(), personNames);
//            dbHelper.close();
//
//            Intent intent = new Intent(this, ExpenseListActivity.class);
//            intent.putExtra(ExpenseListActivity.PARAM_CALCULATION_ID, calculation.getId());
//            startActivity(intent);
//            finish();
//        }





        return rootView;
    }

    // NAYA PERSON ADD KARTE H TO ROW GENRATE HOTA H JATA H NAA USKE LIYE
    private CategoryView addcategoryRow() {
        //NEW ROW ADD KARTA JAYGA
        final View view = getActivity().getLayoutInflater().inflate(R.layout.categoryview, categoryList, false);
        categoryList.addView(view);
        //VIEW CREATE KIYA
        final CategoryView CategoryView = new CategoryView();
        CategoryView.view = view;
        //VIEW ADD KIYA
        CategoryView.CategoryName = (EditText) view.findViewById(R.id.category_name);
        //VIEW DELETE KIYA
        CategoryView.deleteButton = (ImageView) view.findViewById(R.id.delete_button);
        categoryViews.add(CategoryView);

        CategoryView.CategoryName.setId(-1); // do not restore in onRestoreInstanceState()
        //DEKHTA H KI KAB ADD KARNEKA NAYA ROW
        CategoryView.CategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            //BAKWAS BT MANDATORY ISLEA CODE ME H
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            //BAKWAS BT MANDATORY ISLEA CODE ME H
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            //CREATE AND DELETE ROWS(UPAR H)VALE FUNCTION KO CALL KIYA
            public void afterTextChanged(Editable s) {
                createOrDeletePersonRows();
            }
        });

        CategoryView.CategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            //YE NAI SAMJHA
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    createOrDeletePersonRows();
            }
        });

        // X VALA RED DELETE KA BUTTON (APP DEKH)
        CategoryView.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePersonRow(CategoryView);
                createOrDeletePersonRows();
            }
        });
        return CategoryView;
    }

    private void createOrDeletePersonRows() {
        int numEmpty = 0;
        int i = 0;
        //CHK KAREGA KI KAM SE KAM 2 PERSON CHAHIYE KARKE
        while (i < categoryViews.size() &&/*2 ROWS KAM SE KAM STARTING ME DISPLAY KAREGA*/ categoryViews.size() >= 1) {
            CategoryView categoryView = categoryViews.get(i++);
            String name = categoryView.CategoryName.getText().toString().trim();
            if (name.length() == 0) {//AGAR KUCH NAAM NAI BHARA TO DELETE KAREGA ROWS KO
                if (!categoryView.CategoryName.hasFocus()) {
                    deletePersonRow(categoryView);
                    i--;
                } else {
                    numEmpty++;
                }
            }
        }
        //VIEWS KA SIZE MIN PERSON SE JYADA N MAX PERSON SE KAM HUA TO ROW ADD KAREGA
        while ((categoryViews.size() < MIN_Category) ||
                (numEmpty < 1 && categoryViews.size() < MAX_Category))
        {
            addPersonRow();
            numEmpty++;
        }
        //ROW VISIBLE INVISIBLE
        for (i = 0; i < categoryViews.size(); i++) {
            CategoryView categoryView = categoryViews.get(i);
            boolean last = (i + 1 == categoryViews.size());
            categoryView.deleteButton.setVisibility(last ? View.INVISIBLE : View.VISIBLE);
        }
    }
    private void deletePersonRow(CategoryView row) {
        categoryList.removeView(row.view);
        categoryViews.remove(row);
    }
    private CategoryView addPersonRow() {
        //NEW ROW ADD KARTA JAYGA
        final View view = getActivity().getLayoutInflater().inflate(R.layout.categoryview, categoryList, false);
        categoryList.addView(view);
        //VIEW CREATE KIYA
        final CategoryView categoryView = new CategoryView();
        categoryView.view = view;
        //VIEW ADD KIYA
        categoryView.CategoryName = (EditText) view.findViewById(R.id.category_name);
        //VIEW DELETE KIYA
        categoryView.deleteButton = (ImageView) view.findViewById(R.id.delete_button);
        categoryViews.add(categoryView);

        categoryView.CategoryName.setId(-1); // do not restore in onRestoreInstanceState()
        //DEKHTA H KI KAB ADD KARNEKA NAYA ROW
        categoryView.CategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            //BAKWAS BT MANDATORY ISLEA CODE ME H
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            //BAKWAS BT MANDATORY ISLEA CODE ME H
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            //CREATE AND DELETE ROWS(UPAR H)VALE FUNCTION KO CALL KIYA
            public void afterTextChanged(Editable s) {
                createOrDeletePersonRows();
            }
        });

        categoryView.CategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            //YE NAI SAMJHA
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    createOrDeletePersonRows();
            }
        });

        // X VALA RED DELETE KA BUTTON (APP DEKH)
        categoryView.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePersonRow(categoryView);
                createOrDeletePersonRows();
            }
        });
        return categoryView;
    }
    private String getCategoryName(int i) {
        CategoryView categoryView = categoryViews.get(i);
        return categoryView.CategoryName.getText().toString().trim();
    }

    private ArrayList<String> getCategoryNames() {
        ArrayList<String> categorynames = new ArrayList<>();
        for (int i = 0; i <categoryViews.size(); i++) {
            String name = getCategoryName(i);
            if (name.length() > 0)
                categorynames.add(name);
        }
        return categorynames ;
    }

    private boolean validate() {
        final Resources res = getResources();

        boolean valid = true;

        //AGAR TRIP ME PEHELE SI HI LOG H TO VO SHOW KAREGA
        Set<String> categoryNames = new HashSet<>();
        for (int i = 0; i < categoryViews.size(); i++) {
            String name = getCategoryName(i);
            if (name.length() > 0) {
                if (categoryNames.contains(name)) {
                    CategoryView categoryView = categoryViews.get(i);
                    categoryView.CategoryName.setError(res.getString(R.string.validate_duplicate_name));
                    valid = false;
                } else {
                    categoryNames.add(name);
                }
            }
        }
        //AGAR 2 SE KAM PERSON K NAAM BHARE TO ERROR DIKHAYGA
        if (valid && categoryNames.size() < MIN_Category) {
            CategoryView categoryView = categoryViews.get(categoryViews.size() - 1);
            String format = res.getString(R.string.validate_min_names);
            categoryView.CategoryName.setError(String.format(format, MIN_Category));
            valid = false;
        }

        return valid;
    }

}
