package com.example.dotaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HeroActivity extends AppCompatActivity {
    String TAG = "HERO_ACTIVITY";
    private ArrayList<Hero> heroes;
    private RecyclerView recyclerView;
    private HeroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        TextView tv1 = (TextView) findViewById(R.id.heroTitle);
        TextView tv2 = (TextView) findViewById(R.id.attrText);
        TextView tv3 = (TextView) findViewById(R.id.attackTypeText);
        ImageView img = (ImageView) findViewById(R.id.guideImage);
        FloatingActionButton home = (FloatingActionButton) findViewById(R.id.homeButton);

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //Get the Hero object from the previous activity
        Bundle extras = getIntent().getExtras();
        Intent i = getIntent();
        Hero h = (Hero) i.getParcelableExtra("hero");
        GuideListFragment fragment = new GuideListFragment();
        fragment.setArguments(extras);

        tv1.setText(h.getLocalized_name());
        tv2.setText(h.getPrimary_attr());
        tv3.setText(h.getAttack_type());
        Glide.with(this).load(h.getFullImg()).into(img);

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentFrame, new GuideListFragment()).commit();
    }
}