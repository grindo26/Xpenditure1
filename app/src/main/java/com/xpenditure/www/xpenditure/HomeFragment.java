package com.xpenditure.www.xpenditure;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    PieChart pieChart;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        pieChart = (PieChart) rootView.findViewById(R.id.pieChart);
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

        yValues.add(new PieEntry(12f , "Catagory 1"));
        yValues.add(new PieEntry(19f , "Catagory 2"));
        yValues.add(new PieEntry(45f , "Catagory 3"));
        yValues.add(new PieEntry(13f , "Catagory 4"));
        yValues.add(new PieEntry(25f , "Catagory 5"));
        yValues.add(new PieEntry(50f , "Catagory 6"));

        pieChart.animateY(3000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues , "Catagories");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);


    return rootView;
    }

}
