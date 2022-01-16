package com.example.dotaapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuideListFragment extends Fragment implements GuideAdapter.OnGuideListener{

    String TAG = "GUIDE_FRAGMENT";
    RecyclerView recyclerView;
    DatabaseReference database;
    GuideAdapter adapter;
    ArrayList<Guide> guideList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_list, container, false);

        //get hero that was clicked
        Intent i = getActivity().getIntent();
        Hero h = (Hero) i.getParcelableExtra("hero");

        recyclerView = view.findViewById(R.id.guideRecyclerView);
        FloatingActionButton floatingCreateGuideButton = (FloatingActionButton) view.findViewById(R.id.createGuideButton);
        TextView noGuide = (TextView) view.findViewById(R.id.noGuideText);

        //When the floating (+) button is clicked, a new fragment will be created and the corresponding Hero object will be passed to it
        floatingCreateGuideButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateGuideFragment fragment = new CreateGuideFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, fragment).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putParcelable("hero", h);
                fragment.setArguments(bundle);
            }
        });

        //Create the recycler view with a grid layout
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        guideList = new ArrayList<>();
        adapter = new GuideAdapter(this.guideList, this);
        recyclerView.setAdapter(adapter);

        //Get the information from the database and create a list of Guide Objects with it
        database = FirebaseDatabase.getInstance("https://dotaapp-29182-default-rtdb.europe-west1.firebasedatabase.app").getReference("Guides");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Guide guide = new Guide();
                    for (DataSnapshot sc : snap.getChildren()){
                        String key = sc.getKey();
                        String value = sc.getValue().toString();
                        if (key.equals("title")){
                            guide.setTitle(value);
                        }
                        if (key.equals("string")){
                            guide.setString(value);
                        }
                        if (key.equals("username")){
                            guide.setUsername(value);
                        }
                        if (key.equals("id")){
                            guide.setId(Integer.valueOf(value));
                        }
                        if (key.equals("location")){
                            guide.setLocation(value);
                        }
                    }
                    //only add to the list if the id in the guide object matches the heroes id
                    if(guide.getId() == h.getId()){
                        guideList.add(guide);
                    }
                }
                adapter.notifyDataSetChanged();
                //if a guide exists, then the text will disappear
                if(guideList.size() > 0){
                    noGuide.setText("");
                }else {
                    noGuide.setText(R.string.noGuideString);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error with database");
            }
        });
        return view;
    }

    //create the fragment to better view a guide when clicked
    @Override
    public void onGuideClick(int position) {
        FullGuideFragment fragment = new FullGuideFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Bundle bundle = new Bundle();
        bundle.putParcelable("guide", guideList.get(position));
        fragment.setArguments(bundle);
        ft.replace(R.id.fragmentFrame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}