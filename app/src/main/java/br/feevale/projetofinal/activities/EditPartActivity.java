package br.feevale.projetofinal.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.PreparationItem;
import br.feevale.projetofinal.models.TripPart;


public class EditPartActivity extends AppCompatActivity {
    private String partId;
    private String tripId;
    private FirebaseFirestore db;
    private TripPart tripPart;
    private AutoCompleteTextView destination;
    private EditText arrivalDate;
    private EditText departureDate;
    private CheckBox visaCheckbox;
    private CheckBox hostingCheckbox;
    private CheckBox transportCheckbox;
    private CheckBox luggageCheckbox;
    private CheckBox flightsCheckbox;
    private CheckBox restaurantCheckbox;
    private ImageButton visaStatusIcon;
    private ImageButton hostingStatusIcon;
    private ImageButton transportStatusIcon;
    private ImageButton luggageStatusIcon;
    private ImageButton flightsStatusIcon;
    private ImageButton restaurantStatusIcon;
    private EditText visaCostEditView;
    private EditText hostingCostEditView;
    private EditText transportCostEditView;
    private EditText flightsCostEditView;
    private EditText restaurantCostEditView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_part);

        partId = getIntent().getStringExtra("partId");
        tripId = getIntent().getStringExtra("tripId");

        db = FirebaseFirestore.getInstance();

        destination = findViewById(R.id.destination_autocomplete_edit_part_view);
        arrivalDate = findViewById(R.id.arrival_date_edit_part_view);
        departureDate = findViewById(R.id.departure_date_edit_part_view);
        visaCheckbox = findViewById(R.id.visa_edit_part_check);
        hostingCheckbox = findViewById(R.id.hosting_edit_part_check);
        transportCheckbox = findViewById(R.id.transport_edit_part_check);
        luggageCheckbox = findViewById(R.id.luggage_edit_part_check);
        flightsCheckbox = findViewById(R.id.flights_edit_part_check);
        restaurantCheckbox = findViewById(R.id.restaurant_edit_part_check);
        visaStatusIcon = findViewById(R.id.visa_status_icon);
        hostingStatusIcon = findViewById(R.id.hosting_status_icon);
        transportStatusIcon = findViewById(R.id.transport_status_icon);
        luggageStatusIcon = findViewById(R.id.luggage_status_icon);
        flightsStatusIcon = findViewById(R.id.flights_status_icon);
        restaurantStatusIcon = findViewById(R.id.restaurant_status_icon);
        visaCostEditView = findViewById(R.id.visa_cost_edit_view);
        hostingCostEditView = findViewById(R.id.hosting_cost_edit_view);
        transportCostEditView = findViewById(R.id.transport_cost_edit_view);
        flightsCostEditView = findViewById(R.id.flights_cost_edit_view);
        restaurantCostEditView = findViewById(R.id.restaurant_cost_edit_view);

        setPartDataOnScreen();

        Button updateButton = findViewById(R.id.commitChangesToTripPartButton);
        updateButton.setOnClickListener(commitChanges);
    }

    private void setPartDataOnScreen() {
        final ArrayList<PreparationItem> partItems = new ArrayList<>();
        String collectionPath = "trip/" + tripId + "/tripparts";
        db.collection(collectionPath)
                .document(partId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            ArrayList<HashMap<String, String>> prepItens = (ArrayList<HashMap<String, String>>) document.get("prepitems");
                            for(HashMap<String, String> map : prepItens){
                                partItems.add(new PreparationItem(
                                        map.getOrDefault("cost", "0"),
                                        Boolean.parseBoolean(map.get("enabled")),
                                        map.get("name"),
                                        Boolean.parseBoolean(map.get("status"))
                                ));
                            }
                            tripPart = new TripPart(
                                    document.getId(),
                                    document.get("arrivaldate").toString(),
                                    document.get("departuredate").toString(),
                                    document.get("destination").toString(),
                                    "0");
                            tripPart.setPartPrepItens(partItems);
                            destination.setText(tripPart.getDestination());
                            arrivalDate.setText(tripPart.getPartStartDate());
                            departureDate.setText(tripPart.getPartEndDate());
                            if(tripPart.partPrepItens.get(0).getEnabled()){
                                visaCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(0).getStatus()){
                                    visaStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(0).getCost().equals("0")){
                                    visaCostEditView.setText(tripPart.partPrepItens.get(0).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(1).getEnabled()){
                                hostingCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(0).getStatus()){
                                    hostingStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(1).getCost().equals("0")){
                                    hostingCostEditView.setText(tripPart.partPrepItens.get(1).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(2).getEnabled()){
                                transportCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(2).getStatus()){
                                    transportStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(2).getCost().equals("0")){
                                    transportCostEditView.setText(tripPart.partPrepItens.get(2).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(3).getEnabled()){
                                luggageCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(3).getStatus()){
                                    luggageStatusIcon.setActivated(true);
                                }
                            }
                            if(tripPart.partPrepItens.get(4).getEnabled()){
                                flightsCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(4).getStatus()){
                                    flightsStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(4).getCost().equals("0")){
                                    flightsCostEditView.setText(tripPart.partPrepItens.get(4).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(5).getEnabled()){
                                restaurantCheckbox.setChecked(true);
                                if(tripPart.partPrepItens.get(5).getStatus()){
                                    restaurantStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(5).getCost().equals("0")){
                                    restaurantCostEditView.setText(tripPart.partPrepItens.get(5).getCost());
                                }
                            }
                            addListeners();
                        }
                    }
                });

    }

    private void addListeners() {
        visaStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visaCheckbox.isChecked()){
                    visaCheckbox.setChecked(true);
                }
                visaStatusIcon.setActivated(!visaStatusIcon.isActivated());
            }
        });
        hostingStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hostingCheckbox.isChecked()){
                    hostingCheckbox.setChecked(true);
                }
                hostingStatusIcon.setActivated(!hostingStatusIcon.isActivated());
            }
        });
        transportStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!transportCheckbox.isChecked()){
                    transportCheckbox.setChecked(true);
                }
                transportStatusIcon.setActivated(!transportStatusIcon.isActivated());
            }
        });
        luggageStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!luggageCheckbox.isChecked()){
                    luggageCheckbox.setChecked(true);
                }
                luggageStatusIcon.setActivated(!luggageStatusIcon.isActivated());
            }
        });
        flightsStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flightsCheckbox.isChecked()){
                    flightsCheckbox.setChecked(true);
                }
                flightsStatusIcon.setActivated(!flightsStatusIcon.isActivated());
            }
        });
        restaurantStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!restaurantCheckbox.isChecked()){
                    restaurantCheckbox.setChecked(true);
                }
                restaurantStatusIcon.setActivated(!restaurantStatusIcon.isActivated());
            }
        });
    }


    View.OnClickListener commitChanges = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String collectionPathTripPart = "trip/" + tripId + "/tripparts";

            Map<String, Object> tripPart = new HashMap<>();
            tripPart.put("destination", destination.getText().toString());
            tripPart.put("arrivaldate", arrivalDate.getText().toString());
            tripPart.put("departuredate", departureDate.getText().toString());

            Map<String, Object> prepItemVisa = new HashMap<>();
            prepItemVisa.put("name", getText(R.string.visa_prep_item_label));
            if(visaCheckbox.isChecked()){
                prepItemVisa.put("enabled", "true");
                if(!visaCostEditView.getText().toString().isEmpty()) {
                    prepItemVisa.put("cost", visaCostEditView.getText().toString());
                }else{
                    prepItemVisa.put("cost", "0");
                }
                if(visaStatusIcon.isActivated()){
                    prepItemVisa.put("status", "true");
                }else{
                    prepItemVisa.put("status", "false");
                }
            }
            else{
                prepItemVisa.put("enabled", "false");
                prepItemVisa.put("cost", "0");
                prepItemVisa.put("status", "false");
            }

            Map<String, Object> prepItemHosting = new HashMap<>();
            prepItemHosting.put("name", getText(R.string.hosting_prep_item_label));
            if(hostingCheckbox.isChecked()){
                prepItemHosting.put("enabled", "true");
                if(!hostingCostEditView.getText().toString().isEmpty()) {
                    prepItemHosting.put("cost", hostingCostEditView.getText().toString());
                }else{
                    prepItemHosting.put("cost", "0");
                }
                if(hostingStatusIcon.isActivated()){
                    prepItemHosting.put("status", "true");
                }else{
                    prepItemHosting.put("status", "false");
                }
            }
            else{
                prepItemHosting.put("enabled", "false");
                prepItemHosting.put("cost", "0");
                prepItemHosting.put("status", "false");
            }


            Map<String, Object> prepItemTransport = new HashMap<>();
            prepItemTransport.put("name", getText(R.string.transport_prep_item_label));
            if(transportCheckbox.isChecked()){
                prepItemTransport.put("enabled", "true");
                if(!transportCostEditView.getText().toString().isEmpty()) {
                    prepItemTransport.put("cost", transportCostEditView.getText().toString());
                }else{
                    prepItemTransport.put("cost", "0");
                }
                if(transportStatusIcon.isActivated()){
                    prepItemTransport.put("status", "true");
                }else{
                    prepItemTransport.put("status", "false");
                }
            }
            else{
                prepItemTransport.put("enabled", "false");
                prepItemTransport.put("cost", "0");
                prepItemTransport.put("status", "false");
            }

            Map<String, Object> prepItemLuggage = new HashMap<>();
            prepItemLuggage.put("name", getText(R.string.luggage_prep_item_label));
            if(luggageCheckbox.isChecked()){
                prepItemLuggage.put("enabled", "true");
                if(luggageStatusIcon.isActivated()){
                    prepItemLuggage.put("status", "true");
                }else{
                    prepItemLuggage.put("status", "false");
                }
            }
            else{
                prepItemLuggage.put("enabled", "false");
                prepItemLuggage.put("status", "false");
            }

            Map<String, Object> prepItemFlights = new HashMap<>();
            prepItemFlights.put("name", getText(R.string.flights_prep_item_label));
            if(flightsCheckbox.isChecked()){
                prepItemFlights.put("enabled", "true");
                if(!flightsCostEditView.getText().toString().isEmpty()) {
                    prepItemFlights.put("cost", flightsCostEditView.getText().toString());
                }else{
                    prepItemFlights.put("cost", "0");
                }
                if(flightsStatusIcon.isActivated()){
                    prepItemFlights.put("status", "true");
                }else{
                    prepItemFlights.put("status", "false");
                }
            }
            else{
                prepItemFlights.put("enabled", "false");
                prepItemFlights.put("cost", "0");
                prepItemFlights.put("status", "false");
            }


            Map<String, Object> prepItemRestaurant = new HashMap<>();
            prepItemRestaurant.put("name", getText(R.string.restaurant_prep_item_label));
            if(restaurantCheckbox.isChecked()){
                prepItemRestaurant.put("enabled", "true");
                if(!restaurantCostEditView.getText().toString().isEmpty()) {
                    prepItemRestaurant.put("cost", restaurantCostEditView.getText().toString());
                }else{
                    prepItemRestaurant.put("cost", "0");
                }
                if(restaurantStatusIcon.isActivated()){
                    prepItemRestaurant.put("status", "true");
                }else{
                    prepItemRestaurant.put("status", "false");
                }
            }
            else{
                prepItemRestaurant.put("enabled", "false");
                prepItemRestaurant.put("cost", "0");
                prepItemRestaurant.put("status", "false");
            }

            tripPart.put("prepitems", Arrays.asList(prepItemVisa, prepItemHosting, prepItemTransport, prepItemLuggage, prepItemFlights, prepItemRestaurant));

            db.collection(collectionPathTripPart).document(partId).set(tripPart, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            });
        }

    };


}
