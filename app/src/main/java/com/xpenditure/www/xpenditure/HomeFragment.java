package com.xpenditure.www.xpenditure;


        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;


        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;
        import com.github.mikephil.charting.animation.Easing;
        import com.github.mikephil.charting.charts.PieChart;
        import com.github.mikephil.charting.data.PieData;
        import com.github.mikephil.charting.data.PieDataSet;
        import com.github.mikephil.charting.data.PieEntry;
        import com.github.mikephil.charting.utils.ColorTemplate;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;

        import static com.google.android.gms.internal.zzs.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button addMoney;
    private Button removeMoney;
    Firebase mref;
    Integer total_money;
    TextView money;
    Toolbar toolbar;
    DatabaseReference databaseReference;
    FragmentTransaction fragmentTransaction;
    ActionBar actionBar = null;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);


//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        addMoney= (Button) rootView.findViewById(R.id.addMoney);
        removeMoney = (Button)rootView.findViewById(R.id.removeMoney);

        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);

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
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");

                }
                // ...
            }
        };


        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.getLegend().setEnabled(false);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(35f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(45f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(12f, "Catagory 1"));
        yValues.add(new PieEntry(19f, "Catagory 2"));
        yValues.add(new PieEntry(45f, "Catagory 3"));
        yValues.add(new PieEntry(13f, "Catagory 4"));
        yValues.add(new PieEntry(25f, "Catagory 5"));
        yValues.add(new PieEntry(50f, "Catagory 6"));

        pieChart.animateY(3000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "Catagories");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mref = new Firebase("https://xpenditure-7d2a5.firebaseio.com/users/"+uid+"/Total");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total_money= dataSnapshot.getValue(Integer.class);

                money = (TextView) rootView.findViewById(R.id.money);
                money.setText(total_money.toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoneyFragment addMoneyFragment = new AddMoneyFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, addMoneyFragment);

                fragmentTransaction.commit();

            }
        });

        removeMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveMoneyFragment removeMoneyFragment = new RemoveMoneyFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, removeMoneyFragment);

                fragmentTransaction.commit();

            }
        });








        return rootView;
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
