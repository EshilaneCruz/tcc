package br.feevale.projetofinal.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
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
import br.feevale.projetofinal.models.Partner;


public class PresentPartnersActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    private List<Partner> partners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_partners);

        db = FirebaseFirestore.getInstance();
        loadParceiros();
    }

    private void loadParceiros() {

        db.collection("partners")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            partners = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Partner partner = new Partner(
                                        document.get("category").toString(),
                                        document.get("name").toString(),
                                        document.get("description").toString(),
                                        Integer.parseInt(document.get("rating").toString())
                                );
                                partners.add(partner);
                            }
                            listView = findViewById(R.id.partnersListView);
                            PartnerAdapter adapter = new PartnerAdapter();
                            listView.setAdapter( adapter );
                        }
                    }
                });
    }

    private class PartnerAdapter extends ArrayAdapter {

        PartnerAdapter() {
            super( PresentPartnersActivity.this, R.layout.view_partner, partners );
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_partner, null );
            }

            final Partner partner = partners.get( position );
            TextView partnerNameView = result.findViewById( R.id.partnerNameView );
            partnerNameView.setText(partner.getName());
            ImageView partnerCategoryIconView = result.findViewById(R.id.partnerCategoryIconView);
            Resources resources = getContext().getResources();
            final int resourceId = resources.getIdentifier(partner.getCategory(), "drawable", getContext().getPackageName());
            partnerCategoryIconView.setImageResource(resourceId);
            TextView partnerDescriptionView = result.findViewById(R.id.partnerDescriptionView);
            partnerDescriptionView.setText(partner.getDescription());
            RatingBar ratingBar = result.findViewById(R.id.ratingBar);
            ratingBar.setProgress(partner.getRating(), true);
            return result;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
