package br.feevale.projetofinal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.Trip;
import br.feevale.projetofinal.models.TripPart;
import br.feevale.projetofinal.utils.UtilMethods;


public class TripManagementActivity extends AppCompatActivity{

    private static final String TAG = "Firestore";
    private ArrayList<TripPart> tripPartList;
    private FirebaseFirestore db;
    private Trip trip = new Trip();
    String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        db = FirebaseFirestore.getInstance();
        tripId = this.getIntent().getStringExtra("tripId");
        loadUserTrip(tripId);
        ImageButton editTripDetailsButton = findViewById(R.id.editTripDetailsButton);
        editTripDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
                intent.putExtra("tripId", tripId);
                startActivity(intent);
            }
        });
        Button addTripPartButton = findViewById(R.id.addTripPartButton);
        addTripPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTripPartActivity.class);
                intent.putExtra("tripId", tripId);
                startActivity(intent);
            }
        });
    }

    private void loadUserTrip(final String id) {
        db.collection("trip")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                                trip.setId(document.getId());
                                trip.setTripName(document.get("name").toString());
                                trip.setTripStartDate(document.get("startdate").toString());
                                trip.setTripEndDate(document.get("enddate").toString());
                                trip.setTripBudget(document.get("budget").toString());
                                loadTripParts(id);
                                setBasicDataOnScreen();
                        }
                    }
                });
    }

    private void loadTripParts(String id) {
        String collectionPath = "trip/" + id + "/tripparts";
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tripPartList = new ArrayList<>();
                            for(DocumentSnapshot document : task.getResult()){
                                TripPart part = new TripPart(
                                        document.getId(),
                                        document.get("arrivaldate").toString(),
                                        document.get("departuredate").toString(),
                                        document.get("destination").toString(),
                                        "0"
                                );
                                tripPartList.add(part);
                            }
                        }
                        ListView listView = findViewById(R.id.listTripParts_view);
                        TripPartAdapter adapter = new TripPartAdapter();
                        listView.setAdapter( adapter );
                    }
                });
    }

    private void setBasicDataOnScreen() {
        TextView tripNameView = findViewById(R.id.tripNameView);
        TextView tripPeriodView = findViewById(R.id.tripPeriodView);
        TextView budgetView = findViewById(R.id.tripBudgetView);
        tripNameView.setText(trip.getTripName());
        String formatedDate = UtilMethods.formatDate(trip.getTripStartDate()) + " - " +UtilMethods.formatDate(trip.getTripEndDate());
        tripPeriodView.setText(formatedDate);
        String formatedValue = "$" + trip.getTripBudget();
        budgetView.setText(formatedValue);
    }

    private class TripPartAdapter extends ArrayAdapter {

        TripPartAdapter() {
            super( TripManagementActivity.this, R.layout.view_trip_part, tripPartList );
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_trip_part, null );
            }

            final TripPart tripPart = tripPartList.get( position );

            TextView cityView = result.findViewById( R.id.cityPartView );
            cityView.setText( tripPart.getDestination() );

            TextView date = result.findViewById( R.id.tripPartPeriodView );
            String formattedDate = UtilMethods.formatDate(tripPart.getPartStartDate()) + " - " + UtilMethods.formatDate(tripPart.getPartEndDate());
            date.setText( formattedDate );

            ImageButton partEditButton = result.findViewById(R.id.partEditButton);
            ImageButton eventsEditButton = result.findViewById(R.id.eventsEditButton);
            ImageButton deleteButton = result.findViewById(R.id.deletePartButton);

            partEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EditPartActivity.class);
                    intent.putExtra("partId", tripPart.getPartId());
                    intent.putExtra("tripId", tripId);
                    startActivity(intent);
                }
            });
            eventsEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
                    intent.putExtra("partId", tripPart.getPartId());
                    intent.putExtra("tripId", tripId);
                    startActivity(intent);
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO DELETE PART FROM FIRESTORE
                }
            });

            return result;
        }
    }

}
