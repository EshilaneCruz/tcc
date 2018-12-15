package br.feevale.projetofinal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Objects;
import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.TripResume;
import br.feevale.projetofinal.utils.UtilMethods;


public class PresentTripActivity extends AppCompatActivity{

    private static final String TAG = "Firestore";
    private ArrayList<TripResume> tripResumeList;
    private FirebaseFirestore db;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_trip);
        db = FirebaseFirestore.getInstance();
        loadUserTripList();
    }

    private void loadUserTripList() {
        db.collection("trip")
                .whereEqualTo("owner", auth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            tripResumeList = new ArrayList<>();
                            for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String id = document.getId();
                                String name = document.get("name").toString();
                                String startDate = UtilMethods.formatDate(document.get("startdate").toString());
                                String enddate = UtilMethods.formatDate(document.get("enddate").toString());
                                String formatedDate = startDate + " to " + enddate;
                                tripResumeList.add(new TripResume(id, name, formatedDate));
                            }
                            ListView listView = findViewById(R.id.list_view);
                            TripResumeAdapter adapter = new TripResumeAdapter();
                            listView.setAdapter( adapter );
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private class TripResumeAdapter extends ArrayAdapter {

        TripResumeAdapter() {
            super( PresentTripActivity.this, R.layout.view_trip_resume, tripResumeList );
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_trip_resume, null );
            }

            final TripResume tripResume = tripResumeList.get( position );
            TextView tripName = result.findViewById( R.id.tripNameView );
            TextView date = result.findViewById( R.id.tripPeriodView );
            tripName.setText( tripResume.getTripName() );
            date.setText( tripResume.getTripDate() );
            Button goToTripPageBt = result.findViewById(R.id.goToTripPageButton);
            goToTripPageBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TripManagementActivity.class);
                    intent.putExtra("tripId", tripResume.getTripId());
                    startActivity(intent);
                }
            });
            return result;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
