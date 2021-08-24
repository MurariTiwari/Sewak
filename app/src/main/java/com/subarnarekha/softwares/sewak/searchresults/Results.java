package com.subarnarekha.softwares.sewak.searchresults;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.addService.ImageUploadAdapter;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    SharedPreferences preferences;
    float latitude,longitude;
    FirebaseFirestore db;
    RecyclerView searchResult;
    ImageView filterMenu;
    TextView header;
    String prefessionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        header = findViewById(R.id.search_header);
        prefessionName = getIntent().getStringExtra("profession");
        searchResult = findViewById(R.id.search_results);
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        filterMenu = findViewById(R.id.filter);
        filterMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this,filterMenu);
            popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.km10: fetchData(10);
                            break;
                        case R.id.km25: fetchData(25);
                            break;
                        case R.id.km50: fetchData(50);
                            break;
                        case R.id.km100: fetchData(100);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        });
        if(preferences.contains("lat")&&!(preferences.getFloat("lat",0)==0)){
            if(preferences.contains("long")&&!(preferences.getFloat("long",0)==0))
            {
                fetchData(50);
            }
        }
    }
    public  void fetchData(int radius)
    {

        List<String> address = new ArrayList<>();
        List<String> desc = new ArrayList<>();
        List<String> distance = new ArrayList<>();
        List<String> images = new ArrayList<>();
        latitude=preferences.getFloat("lat",0);
        longitude=preferences.getFloat("long",0);
        header.setText(prefessionName);
        final GeoLocation center = new GeoLocation(latitude, longitude);
        final double radiusInM = radius * 1000;
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection(prefessionName.replaceAll("\\s", ""))
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }
        searchResult.setLayoutManager(new LinearLayoutManager(this));
        //add adapter to searchResult



        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(t -> {

                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            double lat = doc.getDouble("latitude");
                            double lng = doc.getDouble("longitude");

                            // We have to filter out a few false positives due to GeoHash
                            // accuracy, but most will match
                            GeoLocation docLocation = new GeoLocation(lat, lng);
                            double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                            if (distanceInM <= radiusInM) {
                                desc.add( doc.getString("biography"));
                                address.add(doc.getString("address"));
                                distance.add(String.format("%.1f",(distanceInM/1000))+"Km");
                                ArrayList<String> temp = (ArrayList<String>) doc.get("images");
                                images.add(temp.get(0));
                            }
                        }
                    }

                    // matchingDocs contains the results
                    // ...
                    Adapter adapter = new Adapter(getApplicationContext(),desc,address,distance,images);
                    searchResult.setAdapter(adapter);
                });

    }
}