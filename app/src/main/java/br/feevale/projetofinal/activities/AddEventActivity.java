package br.feevale.projetofinal.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.dialogs.DatePickerFragment;
import br.feevale.projetofinal.dialogs.TimePickerFragment;
import br.feevale.projetofinal.models.Event;
import br.feevale.projetofinal.services.SharedPreferencesService;

public class AddEventActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private FirebaseFirestore db;
    private ArrayList<String> partners = new ArrayList<>();
    private String tripId = "";
    private String partId = "";
    private Event event;
    TextView eventDateView;
    String eventDate;
    TextView eventStartTimeView;
    String eventStartTime;
    TextView eventEndTimeView;
    String eventEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = FirebaseFirestore.getInstance();
        tripId = this.getIntent().getStringExtra("tripId");
        partId = this.getIntent().getStringExtra("partId");

        ImageButton event_date_button = findViewById(R.id.event_date_button);
        event_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runDatePicker();
            }
        });

        ImageButton startTimeButton = findViewById(R.id.event_start_time_button);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePicker();
            }
        });

        ImageButton endTimeButton = findViewById(R.id.event_end_time_button);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTimePicker();
            }
        });

        loadPartners();

        Button saveEventButton = findViewById(R.id.saveEventButton);
        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
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

    private void loadPartners() {
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
                            AutoCompleteTextView textView = findViewById(R.id.partnerSearchBar);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEventActivity.this, android.R.layout.simple_list_item_1, partners);
                            textView.setAdapter(adapter);
                        }
                    }
                });
    }

    private void saveEvent() {

        String collectionPath = "trip/" + tripId + "/tripparts/" + partId + "/events";
        db.collection(collectionPath).add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(dialog.getClass().getName().equals("android.app.DatePickerDialog")){
            eventDate = SharedPreferencesService.retrieveString("DatePickerData");
            eventDateView = findViewById(R.id.event_date_view);
            eventDateView.setText(eventDate);
        }
        else{
            eventStartTimeView = findViewById(R.id.event_start_time_view);
            eventEndTimeView = findViewById(R.id.event_end_time_view);
            if(eventStartTimeView.getText().toString().isEmpty()){
                eventStartTime = SharedPreferencesService.retrieveString("TimePickerData");
                eventStartTimeView.setText(eventStartTime);
            }else{
                eventEndTime = SharedPreferencesService.retrieveString("TimePickerData");
                eventEndTimeView.setText(eventEndTime);
            }
        }
    }

}
