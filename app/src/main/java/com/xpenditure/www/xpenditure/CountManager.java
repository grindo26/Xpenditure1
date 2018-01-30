package com.xpenditure.www.xpenditure;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Swaraj on 30-01-2018.
 */


public class CountManager {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Firebase firebase;
    DatabaseReference databaseReferance;

    private static int countCat;
    private static int countExp;


    public static int countCategories;
    public static int countExpenses;

    CountManager() {
       
    }



    public void getCountCategories(int countCategories) {
        countCat = countCategories;
    }

    public int returnCategoryCount(){
        return countCat;
    }

    public static int getCountExpenses(int countExpenses) {
        return countExpenses;
    }

    public void setCountCategories(int countCategories) {
        int catCount = countCategories;
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
        databaseReferance = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference currentuser = databaseReferance.child(mAuth.getCurrentUser().getUid());
        currentuser.child("Category Count").setValue(catCount);
    }

    public  void setCountExpenses(int countExpenses) {

    }


}