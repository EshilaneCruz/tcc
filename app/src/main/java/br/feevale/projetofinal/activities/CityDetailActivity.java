package br.feevale.projetofinal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.City;

public class CityDetailActivity extends AppCompatActivity {

    private String cityId;
    private FirebaseFirestore db;
    private City city;
    private FirebaseAuth auth;
    private List<String> tripNames = new ArrayList<>();
    private List<String> tripIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        cityId = getIntent().getStringExtra("cityId");

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadCityDetails();

        Button newTripPartDestButton = findViewById(R.id.new_trip_part_dest_button);
        newTripPartDestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToTripDialog();
            }
        });
    }

    private void showAddToTripDialog() {
        db.collection("trip")
                .whereEqualTo("owner", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String date = document.get("startdate").toString().replace("-", "/");
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date startingDate = null;
                                try {
                                    startingDate = sdf.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Long startDateInMillis = startingDate.getTime();
                                if(System.currentTimeMillis() < startDateInMillis){
                                    tripIds.add(document.getId());
                                    tripNames.add(document.get("name").toString());
                                }
                                callDialog();
                            }
                        }
                    }
                });
    }

    private void callDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.add_city_to_trip_title);
        alertDialogBuilder.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, tripNames), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), AddTripPartActivity.class);
                intent.putExtra("tripId", tripIds.get(which));
                String destination = city.getName() + " - " + city.getCountry();
                intent.putExtra("city", destination);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadCityDetails() {
        db.collection("city")
                .document(cityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            city = new City(
                                    document.getId(),
                                    Boolean.parseBoolean(document.get("capital").toString()),
                                    document.get("country").toString(),
                                    document.get("image").toString(),
                                    document.get("name").toString(),
                                    document.get("population").toString(),
                                    document.get("state").toString(),
                                    (List<String>) document.get("touristsite")
                            );
                            setDataOnScreen();
                        }
                    }
                });
    }

    private void setDataOnScreen() {
        ImageView cityImage = findViewById( R.id.cityImageDetailView );
        final int resourceId = getResources().getIdentifier(city.getImage(), "drawable", this.getPackageName());
        cityImage.setImageResource(resourceId);
        TextView cityNameDetailView = findViewById(R.id.cityNameDetailView);
        cityNameDetailView.setText(city.getName());
        TextView cityCountryView = findViewById(R.id.cityCountryView);
        if(city.getState().isEmpty()){
            cityCountryView.setText(city.getCountry());
        }else{
            String compose = city.getCountry() + " [ " + city.getState() + " State]";
            cityCountryView.setText(compose);
        }
        TextView cityCapitalView = findViewById(R.id.cityCapitalView);
        if(city.getCapital()){
            cityCapitalView.setText("Country Capital: Yes");
        }else{
            cityCapitalView.setText("Country Capital: No");
        }
        TextView cityPopulationView = findViewById(R.id.cityPopulationView);
        String populationCompose = "Population: " + city.getPopulation();
        cityPopulationView.setText(populationCompose);

        ListView cityTouristSitesListView = findViewById(R.id.cityTouristSitesListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, city.getTouristSites());
        cityTouristSitesListView.setAdapter(adapter);
    }
}
