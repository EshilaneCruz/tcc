package br.feevale.projetofinal.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.Event;



public class EventsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<Event> eventsList;
    private String tripId;
    private String partId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tripId = getIntent().getStringExtra("tripId");
        partId = getIntent().getStringExtra("partId");

        db = FirebaseFirestore.getInstance();

        loadEvents();

        ImageButton addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                    intent.putExtra("partId", partId);
                    intent.putExtra("tripId", tripId);
                    intent.putExtra("eventId", "");
                    startActivity(intent);
            }
        });

    }

    private void loadEvents() {
        String collectionPath = "trip/" + tripId + "/tripparts/" + partId + "/events";

        db.collection(collectionPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    eventsList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Event event = new Event(
                                document.get("category").toString(),
                                document.get("cost").toString(),
                                document.get("date").toString(),
                                document.get("endingtime").toString(),
                                document.getId(),
                                document.get("location").toString(),
                                document.get("name").toString(),
                                document.get("obs").toString(),
                                Boolean.parseBoolean(document.get("partner").toString()),
                                document.get("serviceprovider").toString(),
                                document.get("startingtime").toString()
                        );
                        eventsList.add(event);
                    }
                }
                ListView listView = findViewById(R.id.listEvents_view);
                EventsActivity.EventAdapter adapter = new EventsActivity.EventAdapter();
                listView.setAdapter(adapter);
            }
        });
    }

    private class EventAdapter extends ArrayAdapter {

        EventAdapter() {
            super( EventsActivity.this, R.layout.view_event, eventsList );
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_event, null );
            }

            final Event event = eventsList.get( position );

            TextView eventNameView = result.findViewById( R.id.eventNameView );
            TextView eventCategoryView = result.findViewById( R.id.eventCategoryView );
            TextView eventDateView = result.findViewById( R.id.eventDateView );
            TextView eventTimeFromView = result.findViewById( R.id.eventTimeFromView );
            TextView eventTimeToView = result.findViewById( R.id.eventTimeToView );
            TextView eventProviderView = result.findViewById( R.id.eventProviderView );
            TextView eventLocationView = result.findViewById( R.id.eventLocationView );
            TextView eventObservationView = result.findViewById( R.id.eventObservationView );
            TextView eventCostView = result.findViewById( R.id.eventCostView );
            ImageButton eventEditButton = result.findViewById( R.id.eventEditButton );
            ImageButton eventDeleteButton = result.findViewById( R.id.eventDeleteButton );

            eventNameView.setText( event.getName() );
            eventCategoryView.setText( event.getCategory() );
            eventDateView.setText( event.getDate() );
            eventTimeFromView.setText( event.getStartingtime() );
            eventTimeToView.setText( event.getEndingtime() );
            eventProviderView.setText( event.getServiceprovider() );
            eventLocationView.setText( event.getLocation() );
            eventObservationView.setText( event.getObservation() );
            eventCostView.setText( event.getCost() );

            eventEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                    intent.putExtra("eventId", event.getEventId());
                    intent.putExtra("partId", partId);
                    intent.putExtra("tripId", tripId);
                    startActivity(intent);
                }
            });
            eventDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String collectionPath = "trip/" + tripId + "/tripparts/" + partId + "/events";
                    db.collection(collectionPath).document(event.getEventId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EventsActivity.this, "Event removed successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return result;
        }
    }
}
