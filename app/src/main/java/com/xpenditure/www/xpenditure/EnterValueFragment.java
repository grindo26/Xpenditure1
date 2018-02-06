package com.xpenditure.www.xpenditure;

import android.content.Intent;
import android.media.Image;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterValueFragment extends Fragment {
    TextView Cat_name;
    EditText Value_edit;
    EditText Notes;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd-MM-yyyy");
    String note;
    Integer Value;
    Integer Expenses;
    String Entered;
    Firebase mref;
    Button Spent;
    Integer total;
    String Date;
    String uid;
    String title,selected;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter_value,container,false);
        Cat_name= (TextView)rootView.findViewById(R.id.selected_category);
        Notes = (EditText)rootView.findViewById(R.id.Notes_edit);
        Value_edit = (EditText) rootView.findViewById(R.id.Value_edit);
        Spent = (Button)rootView.findViewById(R.id.button_spent);
        Date = simpleDateFormat.format(c.getTime());
        Bundle bundle = getArguments();
        title= String.valueOf(bundle.getString("title"));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        mref = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Category/"+title+"/Title");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                selected= dataSnapshot.getValue(String.class);
                Cat_name.setText(selected);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Spent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Firebase mref1 = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Total");
                Entered = Value_edit.getText().toString();
                Value = Integer.parseInt(Entered);
                note = Notes.getText().toString();
                mref1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        total = dataSnapshot.getValue(Integer.class);
                        if (total-Value<0)
                        {
                            Toast.makeText(EnterValueFragment.this.getActivity(), "Warning your balance goes into negative", Toast.LENGTH_SHORT).show();
                        }
                        total-=Value;
                        mref1.setValue(total);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                final Firebase mref2 = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Category/"+title+"/Expenses");
                mref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     Expenses = dataSnapshot.getValue(Integer.class);
                     Expenses++;
                     mref2.setValue(Expenses);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/Category/"+title);
                final Firebase mref3 = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Category/"+title);
                mref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            if(data.child(Date).exists()){
                                final DatabaseReference dateReference = FirebaseDatabase.getInstance().getReference().child("users/"+uid+"/Category/"+title+"/"+Date);
                                dateReference.child("Amount:").setValue(Entered);
                                dateReference.child("Notes:").setValue(note);
                                break;
                            }
                            else {
                                DatabaseReference entry = databaseReference.child(Date);
                                entry.child("Amount:").setValue(Entered);
                                entry.child("Notes:").setValue(note);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Toast.makeText(EnterValueFragment.this.getActivity(),"Successfully added the Record", Toast.LENGTH_SHORT).show();
                HomeFragment homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, homeFragment);
                fragmentTransaction.commit();
            }

        });



        return rootView;
    }
}

