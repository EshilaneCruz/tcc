package br.feevale.projetofinal.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
    EditText startDateView;
    EditText endDateView;
    String startDate;
    String endDate;
    DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_part);
        db = FirebaseFirestore.getInstance();
        tripId = this.getIntent().getStringExtra("tripId");
        loadCities();
        startDateView = findViewById(R.id.arrival_date_view);
        endDateView = findViewById(R.id.departure_date_view);
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
        String collectionPath = "trip/" + tripId;
        AutoCompleteTextView city = findViewById(R.id.destinationSearchBar);
        EditText arrivalDate = findViewById(R.id.arrival_date_view);
        EditText departureDate = findViewById(R.id.departure_date_view);
        CheckBox visaCheck = findViewById(R.id.visaCheck);
        CheckBox hostingCheck = findViewById(R.id.hostingCheck);
        CheckBox transportCheck = findViewById(R.id.transportCheck);
        CheckBox luggageCheck = findViewById(R.id.luggageCheck);
        CheckBox flightsCheck = findViewById(R.id.flightsCheck);
        CheckBox restaurantCheck = findViewById(R.id.restaurantCheck);
        List<PreparationItem> items = new ArrayList<>();
        Map<String, Object> tripPart = new HashMap<>();
        tripPart.put("destination", city.getText());
        tripPart.put("arrivaldate", arrivalDate.getText());
        tripPart.put("departuredate", departureDate.getText());

        if(visaCheck.isChecked()){
            items.add(new PreparationItem(visaCheck.getText().toString(), false, "0"));
        }
        if(hostingCheck.isChecked()){
            items.add(new PreparationItem(hostingCheck.getText().toString(), false, "0"));
        }
        if(transportCheck.isChecked()){
            items.add(new PreparationItem(transportCheck.getText().toString(), false, "0"));
        }
        if(luggageCheck.isChecked()){
            items.add(new PreparationItem(luggageCheck.getText().toString(), false, "0"));
        }
        if(flightsCheck.isChecked()){
            items.add(new PreparationItem(flightsCheck.getText().toString(), false, "0"));
        }
        if(restaurantCheck.isChecked()){
            items.add(new PreparationItem(restaurantCheck.getText().toString(), false, "0"));
        }

        tripPart.put("prepitens", items);

        db.collection(collectionPath).add(tripPart).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                getParentActivityIntent();
            }
        });
    }

    @Override
    public void onDismiss(@Nullable DialogInterface dialog) {
        if(startDateView.getText().toString().isEmpty()){
            setArrivalDate();
        }else{
            setDepartureDate();
        }
    }

    private void setArrivalDate() {
        startDate = SharedPreferencesService.retrieveString("DatePickerData");
        EditText arrivalDateView = findViewById(R.id.arrival_date_view);
        arrivalDateView.setText(startDate);
    }

    private void setDepartureDate() {
        endDate = SharedPreferencesService.retrieveString("DatePickerData");
        EditText departureDateView = findViewById(R.id.departure_date_view);
        departureDateView.setText(endDate);
    }
}
