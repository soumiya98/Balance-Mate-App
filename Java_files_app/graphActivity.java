package com.example.brahma.bmtetrial1;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class graphActivity extends AppCompatActivity {
    private TextView tv2;
    GraphView graph;
    private static final String TAG = "MainActivity";
    LineGraphSeries<DataPoint> series, series1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // tv2=(TextView)findViewById(R.id.textView2);
        graph = (GraphView) findViewById(R.id.graph1);
        series = new LineGraphSeries();
        series1 = new LineGraphSeries();
        graph.addSeries(series);
        graph.addSeries(series1);
        series.setColor(Color.RED);
        final TextView dl = (TextView)findViewById(R.id.dominantleg);
        final TextView impr = (TextView)findViewById(R.id.imp);
        final DatabaseReference reference = database.getReference("chartTable");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                DataPoint[] dp1 = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int x=0;

                int index = 0;
                int i=0,j=0;
                int arrR[]=new int[(int) dataSnapshot.getChildrenCount()];
                int arrL[]=new int[(int) dataSnapshot.getChildrenCount()];

                int count=(int)dataSnapshot.getChildrenCount();
                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    //Log.d(TAG, "x:" + pointValue.getxValue());
                  //  Log.d(TAG, "y:" + pointValue.getyValue());
                    arrR[i++]=pointValue.getyValue();
                    arrL[j++]=pointValue.getxValue();
                    dp[index] = new DataPoint(x, pointValue.getxValue());
                    dp1[index] = new DataPoint(x, pointValue.getyValue());
                    x=x+15;
                    index++;
                }
                series.resetData(dp);
                series1.resetData(dp1);
                int maxR=0,maxL=0,m=0,n=0;
                for(m=0;m<3;m++)
                {
                    if(arrR[m]>maxR)
                        maxR=arrR[m];
                  //  Log.d(TAG,"maxR:"+maxR);
                }
                for(n=0;n<3;n++)
                {
                    if(arrL[n]>maxL)
                        maxL=arrL[n];
                   // Log.d(TAG,"maxL:"+maxL);
                }
                if(maxL>maxR){
                  //  Log.d(TAG,"Max value:"+maxL);
                    int val=0;
                   // val = (int) (((double) arrL[count-1]/ (double) arrL[count-3]) * 100);
                    //Log.d(TAG,"final L value:"+val);
                    dl.setText("Dominant leg is left leg");
                }
                else {
                    // Log.d(TAG, "Max value:" + maxR);
                    int val = 0;
                    //val = (int) (((double) arrR[count-1]/ (double) arrR[count-3]) * 100);
                    // Log.d(TAG,"final R value:"+val);
                    dl.setText("Dominant leg is right leg");
                    //  Log.d(TAG,"arrR 1:"+arrR[count-1]);
                    // Log.d(TAG,"arrR 3:"+arrR[count-3]);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}