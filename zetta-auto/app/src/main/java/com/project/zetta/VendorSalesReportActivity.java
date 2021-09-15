package com.project.zetta;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.project.zetta.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;

public class VendorSalesReportActivity extends AppCompatActivity {

    private TextView stock, sold, totalSales, avgSales;
    private lecho.lib.hellocharts.view.PieChartView pieChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sales_report);
        stock = (TextView)findViewById(R.id.stock);
        sold = (TextView)findViewById(R.id.sold);
        totalSales = (TextView)findViewById(R.id.totalSales);
        avgSales = (TextView)findViewById(R.id.avgSales);

        pieChartView =(lecho.lib.hellocharts.view.PieChartView) findViewById(R.id.chart);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        Query query = databaseReference.orderByChild("seller").startAt(Prevalent.currentOnlineUser.getPhone()).endAt(Prevalent.currentOnlineUser.getPhone());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    // make the array list;


                    int thetotal = 0, thesold = 0, thestock = 0;
                    float price = 0, totalprice = 0, avg = 0;
                    float stockprice = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();

                        //String rating = String.format("%.2f", );

                        //avgSales.setText(rating);

                        Object objPrice = map.get("price");
                        Object objQuantity = map.get("quantity");
                        Object objSold = map.get("sold");

                        thetotal += Integer.parseInt(String.valueOf(objQuantity));
                        thesold += Integer.parseInt(String.valueOf(objSold));
                        thestock = thetotal - thesold;

                        float thePrice = Float.parseFloat(String.valueOf(objPrice))* Float.parseFloat(String.valueOf(objSold));
                        totalprice += thePrice;

                        float theStockPrice = Float.parseFloat(String.valueOf(objPrice))* (Float.parseFloat(String.valueOf(objQuantity)) -  Float.parseFloat(String.valueOf(objSold)) );
                        stockprice += theStockPrice;
                        float pieTotal = totalprice;
                        float pieStock = stockprice;

                        // this is for pie graph
                        List<SliceValue> pieData = new ArrayList<>();
                        pieData.add(new SliceValue(pieTotal, Color.GREEN).setLabel("Sold : RM " + String.format("%.2fk", pieTotal/1000)));
                        pieData.add(new SliceValue(pieStock, Color.RED).setLabel("Stock : RM " + String.format("%.2fk", pieStock/1000)));
                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true);
                        pieChartView.setPieChartData(pieChartData);


                        String priceRange = String.format("%.2f", totalprice);
                        stock.setText(String.valueOf(thestock));
                        sold.setText(String.valueOf(thesold));
                        if(totalprice>1000)
                        {
                            float setprice = totalprice/1000;

                            String newRange = String.format("%.2f", setprice);
                            totalSales.setText("RM " + newRange + "K");

                        }
                        else totalSales.setText("RM " + priceRange);

                        avg = totalprice/thesold;
                        String avgFormat = String.format("%.2f", avg);

                        if(avg>1000)
                        {
                            float setprice = avg/1000;

                            String newRange = String.format("%.2f", setprice);
                            avgSales.setText("RM " + newRange + "K");

                        }
                        else avgSales.setText("RM " + avgFormat);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

//databaseReference.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(final DataSnapshot dataSnapshot) {
//        if(dataSnapshot.exists()) {
//        Toast.makeText(VendorSalesReportActivity.this, "hi", Toast.LENGTH_SHORT).show();
//        int thetotal = 0, thesold = 0, thestock = 0;
//        float price = 0, totalprice = 0, avg = 0;
//        for (DataSnapshot data : dataSnapshot.getChildren()) {
//        Map<String, Object> map = (Map<String, Object>) data.getValue();
//        Object objPrice = map.get("price");
//        Object objQuantity = map.get("quantity");
//        Object objSold = map.get("sold");
//        thetotal += Integer.parseInt(String.valueOf("5"));
//        thesold += Integer.parseInt(String.valueOf("2"));
//        thestock = thetotal - thesold;
//        VendorSalesReportActivity.this.stock.setText(String.valueOf(thestock));
//        VendorSalesReportActivity.this.sold.setText(String.valueOf(thesold));
//        }
//        }
//        }
//
//@Override
//public void onCancelled(final DatabaseError databaseError) {
//        }
//        });