package br.feevale.projetofinal.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.PreparationItem;
import br.feevale.projetofinal.models.TripPart;

import static br.feevale.projetofinal.R.drawable.ic_check_box_green;

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
                                        map.get("cost"),
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
                                visaCheckbox.setActivated(true);
                                if(tripPart.partPrepItens.get(0).getStatus()){
                                    visaStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(0).getCost().equals("0")){
                                    visaCostEditView.setText(tripPart.partPrepItens.get(0).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(1).getEnabled()){
                                hostingCheckbox.setActivated(true);
                                if(tripPart.partPrepItens.get(0).getStatus()){
                                    hostingStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(1).getCost().equals("0")){
                                    hostingCostEditView.setText(tripPart.partPrepItens.get(1).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(2).getEnabled()){
                                transportCheckbox.setActivated(true);
                                if(tripPart.partPrepItens.get(2).getStatus()){
                                    transportStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(2).getCost().equals("0")){
                                    transportCostEditView.setText(tripPart.partPrepItens.get(2).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(3).getEnabled()){
                                luggageCheckbox.setActivated(true);
                                if(tripPart.partPrepItens.get(3).getStatus()){
                                    luggageStatusIcon.setActivated(true);
                                }
                            }
                            if(tripPart.partPrepItens.get(4).getEnabled()){
                                flightsCheckbox.setActivated(true);
                                if(tripPart.partPrepItens.get(4).getStatus()){
                                    flightsStatusIcon.setActivated(true);
                                }
                                if(!tripPart.partPrepItens.get(4).getCost().equals("0")){
                                    flightsCostEditView.setText(tripPart.partPrepItens.get(4).getCost());
                                }
                            }
                            if(tripPart.partPrepItens.get(5).getEnabled()){
                                restaurantCheckbox.setActivated(true);
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
                visaStatusIcon.setActivated(!visaStatusIcon.isActivated());
            }
        });
        hostingStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostingStatusIcon.setActivated(!hostingStatusIcon.isActivated());
            }
        });
        transportStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportStatusIcon.setActivated(!transportStatusIcon.isActivated());
            }
        });
        luggageStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luggageStatusIcon.setActivated(!luggageStatusIcon.isActivated());
            }
        });
        flightsStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightsStatusIcon.setActivated(!flightsStatusIcon.isActivated());
            }
        });
        restaurantStatusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantStatusIcon.setActivated(!restaurantStatusIcon.isActivated());
            }
        });
    }


}
