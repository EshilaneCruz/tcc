package br.feevale.projetofinal.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.dialogs.DatePickerFragment;
import br.feevale.projetofinal.dialogs.TimePickerFragment;
import br.feevale.projetofinal.models.Event;
import br.feevale.projetofinal.services.SharedPreferencesService;

public class AddEventActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private FirebaseFirestore db;
    private ArrayList<String> partners = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> dbcategories;
    private String tripId = "";
    private String partId = "";
    private String eventId = "";
    private boolean editing = false;
    String previousCategory = "";
    TextView screenTitle;
    Button saveEventButton;
    ImageButton event_date_button;
    ImageButton startTimeButton;
    ImageButton endTimeButton;
    ImageButton infoProviderButton;
    TextView eventDateView;
    TextView eventStartTimeView;
    TextView eventEndTimeView;
    EditText event_name_edit_view;
    Spinner categorySpinner;
    EditText costEventView;
    EditText locationEventView;
    EditText obsEventView;
    AutoCompleteTextView partnersSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        screenTitle = findViewById(R.id.navBarEventTitle);
        saveEventButton = findViewById(R.id.saveEventButton);
        event_date_button = findViewById(R.id.event_date_button);
        startTimeButton = findViewById(R.id.event_start_time_button);
        endTimeButton = findViewById(R.id.event_end_time_button);
        infoProviderButton = findViewById(R.id.partner_info_button);
        eventDateView = findViewById(R.id.event_date_view);
        eventStartTimeView = findViewById(R.id.event_start_time_view);
        eventEndTimeView = findViewById(R.id.event_end_time_view);
        event_name_edit_view = findViewById(R.id.event_name_edit_view);
        categorySpinner = findViewById(R.id.event_category_spinner);
        costEventView = findViewById(R.id.costEventView);
        locationEventView = findViewById(R.id.locationEventView);
        obsEventView = findViewById(R.id.obsEventView);
        partnersSearchView = findViewById(R.id.partnerSearchBar);

        db = FirebaseFirestore.getInstance();
        tripId = this.getIntent().getStringExtra("tripId");
        partId = this.getIntent().getStringExtra("partId");
        eventId = this.getIntent().getStringExtra("eventId");

        editing = !eventId.isEmpty();

        if(editing){
            screenTitle.setText(R.string.editEvent_title);
            saveEventButton.setText(R.string.edit_event_label_button);
            loadPreviousEventInformation();
        }else{
            screenTitle.setText(R.string.addEvent_title);
            saveEventButton.setText(R.string.add_event_label_button);
        }

        event_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDatePicker();
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePicker();
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePicker();
            }
        });

        infoProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoAlert();
            }
        });

        loadPartnersToAutoCompleteField();
        if(!editing){
            loadCategoriesToSpinner();
        }

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

    }

    private void loadPreviousEventInformation() {
        String collectionPath = "trip/" + tripId + "/tripparts/" + partId + "/events";
        db.collection(collectionPath)
                .document(eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            eventDateView.setText(documentSnapshot.get("date").toString());
                            eventStartTimeView.setText(documentSnapshot.get("startingtime").toString());
                            eventEndTimeView.setText(documentSnapshot.get("endingtime").toString());
                            event_name_edit_view.setText(documentSnapshot.get("name").toString());
                            costEventView.setText(documentSnapshot.get("cost").toString());
                            previousCategory = documentSnapshot.get("category").toString();
                            locationEventView.setText(documentSnapshot.get("location").toString());
                            obsEventView.setText(documentSnapshot.get("obs").toString());
                            partnersSearchView.setText(documentSnapshot.get("serviceprovider").toString());
                            categories.add(0, previousCategory);
                            loadCategoriesToSpinner();
                        }
                    }
                });
    }

    private void showInfoAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.tooltip_event_partner);
                alertDialogBuilder.setPositiveButton("Got it!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void loadCategoriesToSpinner() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                dbcategories = (ArrayList<String>) document.get("name");
                            }
                            if(dbcategories != null){
                                for(String cat : dbcategories){
                                    categories.add(cat);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEventActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinner.setAdapter(adapter);
                        }
                    }
                });
    }

    private void runDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        datePickerFragment.show(ft, "datePicker");
    }

    private void runTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        timePickerFragment.show(ft, "timePicker");
    }

    private void loadPartnersToAutoCompleteField() {
        db.collection("partners")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String partner = document.get("name").toString();
                                partners.add(partner);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEventActivity.this, android.R.layout.simple_list_item_1, partners);
                            partnersSearchView.setAdapter(adapter);
                        }
                    }
                });
    }

    private void saveEvent() {

        Boolean providerIsAPartner = partners.contains(partnersSearchView.getText().toString());

        Map<String, Object> event = new HashMap<>();
        event.put("category", categorySpinner.getSelectedItem().toString());
        event.put("cost", costEventView.getText().toString());
        event.put("date", eventDateView.getText().toString());
        event.put("endingtime", eventEndTimeView.getText().toString());
        event.put("location", locationEventView.getText().toString());
        event.put("name", event_name_edit_view.getText().toString());
        event.put("obs", obsEventView.getText().toString());
        event.put("partner", providerIsAPartner);
        event.put("serviceprovider", partnersSearchView.getText().toString());
        event.put("startingtime", eventStartTimeView.getText().toString());

        String collectionPath = "trip/" + tripId + "/tripparts/" + partId + "/events";
        if(editing){
            db.collection(collectionPath).document(eventId).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                }
            });
        }else {
            db.collection(collectionPath).add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(dialog.getClass().getName().equals("android.app.DatePickerDialog")){
            String eventDate = SharedPreferencesService.retrieveString("DatePickerData");
            eventDateView.setText(eventDate);
        }
        else{
            if(eventStartTimeView.getText().toString().isEmpty()){
                String eventStartTime = SharedPreferencesService.retrieveString("TimePickerData");
                eventStartTimeView.setText(eventStartTime);
            }else{
                String eventEndTime = SharedPreferencesService.retrieveString("TimePickerData");
                eventEndTimeView.setText(eventEndTime);
            }
        }
    }

}
