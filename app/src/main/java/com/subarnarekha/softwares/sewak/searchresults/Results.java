package com.subarnarekha.softwares.sewak.searchresults;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.subarnarekha.softwares.sewak.BottomSheetFilter;
import com.subarnarekha.softwares.sewak.R;
import com.subarnarekha.softwares.sewak.addService.ImageUploadAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Results extends AppCompatActivity implements BottomSheetFilter.OnDistanceSelectedListener {
 public int radius = 50;
    SharedPreferences preferences;
    float latitude,longitude;
    FirebaseFirestore db;
    RecyclerView searchResult;
    ImageView filterMenu,backBtn;
    TextView header,tv1,tv2;
    LottieAnimationView l1;
    String prefessionName;
    ShimmerFrameLayout shimmer;
    FirebaseUser loggedInUser;
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
        shimmer = findViewById(R.id.search_result_shimmer);
        backBtn = findViewById(R.id.backbtn);
        tv1 = findViewById(R.id.textView4);
        tv2 = findViewById(R.id.textView5);
        l1 = findViewById(R.id.lottieAnimationView);
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        filterMenu.setOnClickListener(v -> {
            /*PopupMenu popupMenu = new PopupMenu(this,filterMenu);
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
            popupMenu.show();*/
            BottomSheetFilter bottomSheetFilter = new BottomSheetFilter(radius);
            bottomSheetFilter.show(getSupportFragmentManager(),bottomSheetFilter.getTag());
        });
        if(preferences.contains("lat")&&!(preferences.getFloat("lat",0)==0)){
            if(preferences.contains("long")&&!(preferences.getFloat("long",0)==0))
            {
                fetchData(50);
            }else{
                finish();
                Toast.makeText(getApplicationContext(),"Please select your Location",Toast.LENGTH_LONG).show();
            }
        }else {
            finish();
            Toast.makeText(getApplicationContext(),"Please select your Location",Toast.LENGTH_LONG).show();
        }
        backBtn.setOnClickListener(v -> finish());
    }
    public  void fetchData(int radius)
    {
        showShimmer();

        List<String> address = new ArrayList<>();
        List<String> desc = new ArrayList<>();
        List<String> from = new ArrayList<>();
        List<String> service = new ArrayList<>();
        List<String> distance = new ArrayList<>();
        List<String> images = new ArrayList<>();
        List<String> user = new ArrayList<>();
        List<String> allowPhone = new ArrayList<>();
        List<List<String>> allImages = new ArrayList<>();
        ArrayList<ArrayList<Map<String,Object>>> serviceMenu = new ArrayList<>();

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
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(t -> {
                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        if(snap.getDocuments().size()!=0)
                        {
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = doc.getDouble("latitude");
                                double lng = doc.getDouble("longitude");
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radiusInM
                                         && !(doc.getString("user")).equals(loggedInUser.getUid())) {
                                    desc.add( doc.getString("biography"));
                                    address.add(doc.getString("address"));
                                    from.add(doc.getString("workStart"));
                                    user.add(doc.getString("user"));
                                    service.add(doc.getString("service"));
                                    allowPhone.add(doc.getString("allowPhone"));
                                    distance.add(String.format("%.1f",(distanceInM/1000))+"Km");
                                    ArrayList<String> temp = (ArrayList<String>) doc.get("images");
                                    images.add(temp.get(0));
                                    allImages.add(temp);
                                    ArrayList<Map<String,Object>> serviceItemModel = (ArrayList<Map<String, Object>>) doc.get("serviceMenu");
                                    serviceMenu.add(serviceItemModel);
                                }
                            }
                        }
                    }
                    if(desc.size()!=0)
                    {
                        Adapter adapter = new Adapter(
                                this,
                                desc,
                                address,
                                distance,
                                images,
                                from,
                                service,
                                allImages,
                                serviceMenu,
                                user,
                                allowPhone );
                        searchResult.setAdapter(adapter);
                        showData();
                    }
                    else showNoData();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData(radius);
    }

    private void showNoData() {
        searchResult.setVisibility(View.GONE);
        tv1.setText("No "+prefessionName+" In this Area !");
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        l1.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
    }

    private void showData() {
        searchResult.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        l1.setVisibility(View.GONE);
        shimmer.setVisibility(View.GONE);
    }

    private void showShimmer() {
        shimmer.setVisibility(View.VISIBLE);
        searchResult.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        l1.setVisibility(View.GONE);
    }

    @Override
    public void onSearchRadiusChange(int r) {
        radius=r;
        fetchData(r);
    }
}