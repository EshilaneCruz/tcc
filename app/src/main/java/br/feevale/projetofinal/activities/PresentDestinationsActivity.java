package br.feevale.projetofinal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.City;


public class PresentDestinationsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private List<City> cities;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_destinations);

        db = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.destinationListView);

        loadDestinations();
    }

    private void loadDestinations() {

        db.collection("city")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cities = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                City city = new City(
                                    document.getId(),
                                    Boolean.parseBoolean(document.get("capital").toString()),
                                    document.get("country").toString(),
                                    document.get("image").toString(),
                                    document.get("name").toString(),
                                    document.get("population").toString(),
                                    document.get("state").toString(),
                                    (List<String>) document.get("touristsite")
                                );
                                cities.add(city);
                            }

                            PresentDestinationsActivity.CityAdapter adapter = new PresentDestinationsActivity.CityAdapter();
                            listView.setAdapter( adapter );
                        }
                    }
                });
    }


    private class CityAdapter extends ArrayAdapter {

        CityAdapter() {
            super( PresentDestinationsActivity.this, R.layout.view_city, cities );
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_city, null );
            }

            final City city = cities.get( position );
            TextView cityName = result.findViewById( R.id.cityNameView );
            ImageView cityImage = result.findViewById( R.id.cityImageView );
            String cityTitle = city.getName().toUpperCase() + "  [ " + city.getCountry() + " ]";
            cityName.setText(cityTitle);
            Resources resources = getContext().getResources();
            final int resourceId = resources.getIdentifier(city.getImage(), "drawable", getContext().getPackageName());
            cityImage.setImageResource(resourceId);
            LinearLayout cityResumeView = result.findViewById(R.id.showCityDetailView);
            cityResumeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CityDetailActivity.class);
                    intent.putExtra("cityId", city.getCityId());
                    startActivity(intent);
                }
            });
            return result;
        }
    }
}
