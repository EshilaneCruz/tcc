package br.feevale.projetofinal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.feevale.projetofinal.R;

public class CityDetailActivity extends AppCompatActivity {

    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        cityId = getIntent().getStringExtra("cityId");
    }
}
