package br.feevale.projetofinal.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.feevale.projetofinal.R;
import br.feevale.projetofinal.dialogs.DatePickerFragment;
import br.feevale.projetofinal.models.PreparationItem;
import br.feevale.projetofinal.services.SharedPreferencesService;

public class AddTripPartActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    private FirebaseFirestore db;
    private ArrayList<String> destinations = new ArrayList<>();
    private String tripId = "";
    EditText arrivalDateView;
    EditText departureDateView;
    String arrivalDate;
    String departureDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_part);
        db = FirebaseFirestore.getInstance();
        tripId = this.getIntent().getStringExtra("tripId");
        loadCities();
        arrivalDateView = findViewById(R.id.arrival_date_view);
        departureDateView = findViewById(R.id.departure_date_view);
        ImageButton start_date_button = findViewById(R.id.arrival_date_button);
        start_date_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerFragment datePickerFragment = new DatePickerFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        datePickerFragment.show(ft, "datePicker");
                    }
                });
        ImageButton end_date_button = findViewById(R.id.departure_date_button);
        end_date_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerFragment datePickerFragment = new DatePickerFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        datePickerFragment.show(ft, "datePicker");
                    }
                });
        Button addPartButton = findViewById(R.id.addTripPartButton);
        addPartButton.setOnClickListener(this);
    }

    private void loadCities() {
        db.collection("city")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String city = document.get("name").toString() + " - " + document.get("country").toString();
                                destinations.add(city);
                            }
                            AutoCompleteTextView textView = findViewById(R.id.destinationSearchBar);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTripPartActivity.this, android.R.layout.simple_list_item_1, destinations);
                            textView.setAdapter(adapter);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        String collectionPathTripPart = "trip/" + tripId + "/tripparts";
        //final String partId = db.collection(collectionPathTripPart).document().getId();
        //final String collectionPathPrepItems = collectionPathTripPart + "/" + partId;
        AutoCompleteTextView city = findViewById(R.id.destinationSearchBar);
        CheckBox visaCheck = findViewById(R.id.visaCheck);
        CheckBox hostingCheck = findViewById(R.id.hostingCheck);
        CheckBox transportCheck = findViewById(R.id.transportCheck);
        CheckBox luggageCheck = findViewById(R.id.luggageCheck);
        CheckBox flightsCheck = findViewById(R.id.flightsCheck);
        CheckBox restaurantCheck = findViewById(R.id.restaurantCheck);
        ArrayList<PreparationItem> items = new ArrayList<>();
        Map<String, Object> tripPart = new HashMap<>();
        tripPart.put("destination", city.getText().toString());
        tripPart.put("arrivaldate", arrivalDateView.getText().toString());
        tripPart.put("departuredate", departureDateView.getText().toString());
        Map<String, Object> prepItemVisa = new HashMap<>();
        prepItemVisa.put("cost", "0");
        prepItemVisa.put("status", "false");
        prepItemVisa.put("name", visaCheck.getText().toString());
        if(visaCheck.isChecked()){
            prepItemVisa.put("enabled", "true");
        }else{
            prepItemVisa.put("enabled", "false");
        }
        Map<String, Object> prepItemHosting = new HashMap<>();
        prepItemHosting.put("cost", "0");
        prepItemHosting.put("status", "false");
        prepItemHosting.put("name", visaCheck.getText().toString());
        if(hostingCheck.isChecked()){
            prepItemHosting.put("enabled", "true");
        }else{
            prepItemHosting.put("enabled", "false");
        }
        Map<String, Object> prepItemTransport = new HashMap<>();
        prepItemTransport.put("cost", "0");
        prepItemTransport.put("status", "false");
        prepItemTransport.put("name", visaCheck.getText().toString());
        if(transportCheck.isChecked()){
            prepItemTransport.put("enabled", "true");
        }else{
            prepItemTransport.put("enabled", "false");
        }
        Map<String, Object> prepItemLuggage = new HashMap<>();
        prepItemLuggage.put("cost", "0");
        prepItemLuggage.put("status", "false");
        prepItemLuggage.put("name", visaCheck.getText().toString());
        if(luggageCheck.isChecked()){
            prepItemLuggage.put("enabled", "true");
        }else{
            prepItemLuggage.put("enabled", "false");
        }
        Map<String, Object> prepItemFlights = new HashMap<>();
        prepItemFlights.put("cost", "0");
        prepItemFlights.put("status", "false");
        prepItemFlights.put("name", visaCheck.getText().toString());
        if(flightsCheck.isChecked()){
            prepItemFlights.put("enabled", "true");
        }else{
            prepItemFlights.put("enabled", "false");
        }
        Map<String, Object> prepItemRestaurant = new HashMap<>();
        prepItemRestaurant.put("cost", "0");
        prepItemRestaurant.put("status", "false");
        prepItemRestaurant.put("name", visaCheck.getText().toString());
        if(restaurantCheck.isChecked()){
            prepItemRestaurant.put("enabled", "true");
        }else{
            prepItemRestaurant.put("enabled", "false");
        }

        tripPart.put("prepitems", Arrays.asList(prepItemVisa, prepItemHosting, prepItemTransport, prepItemLuggage, prepItemFlights, prepItemRestaurant));

        db.collection(collectionPathTripPart).add(tripPart).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        });
    }

    @Override
    public void onDismiss(@Nullable DialogInterface dialog) {
        if(arrivalDateView.getText().toString().isEmpty()){
            setArrivalDate();
        }else{
            setDepartureDate();
        }
    }

    private void setArrivalDate() {
        arrivalDate = SharedPreferencesService.retrieveString("DatePickerData");
        arrivalDateView.setText(arrivalDate);
    }

    private void setDepartureDate() {
        departureDate = SharedPreferencesService.retrieveString("DatePickerData");
        departureDateView.setText(departureDate);
    }
}
