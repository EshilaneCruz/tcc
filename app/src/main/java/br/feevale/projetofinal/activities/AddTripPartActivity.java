package br.feevale.projetofinal.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.feevale.projetofinal.R;
import br.feevale.projetofinal.models.TripPart;
import br.feevale.projetofinal.utils.UtilMethods;

public class AddTripPartActivity extends AppCompatActivity {

    List<String> prepItemsDefault;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_part);
        db = FirebaseFirestore.getInstance();
        loadPrepItemsList();
    }

    private void loadPrepItemsList() {
        db.collection("prepitensdefault")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()){
                                prepItemsDefault = (List<String>) document.get("itemsList");
                            }
                            ListView listView = findViewById(R.id.list_prep_items_view);
                            PrepItemsAdapter adapter = new PrepItemsAdapter();
                            listView.setAdapter( adapter );
                        }
                    }
                });
    }

    private class PrepItemsAdapter extends ArrayAdapter {

        PrepItemsAdapter() {
            super( AddTripPartActivity.this, R.layout.view_prep_item_item, prepItemsDefault );
        }

        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent ) {

            View result = convertView;

            if( result == null ) {
                LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );
                result = inflater.inflate( R.layout.view_prep_item_item, null );
            }

            final String item = prepItemsDefault.get( position );

            CheckBox prepItemLabel = result.findViewById( R.id.checkbox );
            prepItemLabel.setText( item );

            return result;
        }
    }
}
