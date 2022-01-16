package com.example.dotaapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateGuideFragment extends Fragment {
    String TAG = "CREATE_GUIDE";
    DatabaseReference database;
    int guideCount = 0;
    FusedLocationProviderClient fuse;
    DatabaseHelper databaseHelper;
    String username;
    String location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_guide, container, false);

        //Get the Hero object from the previous fragment
        Bundle bundle = getArguments();
        Hero h = (Hero) bundle.getParcelable("hero");

        EditText editGuideTitle = (EditText) view.findViewById(R.id.editTitle);
        EditText editGuideString = (EditText) view.findViewById(R.id.editString);
        EditText editGuideUsername = (EditText) view.findViewById(R.id.editName);
        Button createButton = (Button) view.findViewById(R.id.createGuideButton);
        Button locationButton = (Button) view.findViewById(R.id.locationButton);
        TextView locationText = (TextView) view.findViewById(R.id.locationTextView);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TextView autofill = (TextView) view.findViewById(R.id.autofillText);

        databaseHelper = new DatabaseHelper(getContext());
        Cursor data = databaseHelper.getData();
        while (data.moveToNext()) {
            username = data.getString(0);
            location = data.getString(1);
        }
        editGuideUsername.setText(username);
        if(location != null){
            locationText.setText(location);
            autofill.setText(R.string.autofilledText);
        }

        database = FirebaseDatabase.getInstance("https://dotaapp-29182-default-rtdb.europe-west1.firebasedatabase.app").getReference("Guides");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get reference of how many guides are in the database which is used to create to know the id of the next guide
                // e.g if there is 10 guides, program knows next guide will be 11
                guideCount = (int) snapshot.getChildrenCount() + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "Error with database");
            }
        });

        //onClick for the button that will get the last location of the device
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fuse = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fuse.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                locationText.setText(address.get(0).getCountryName() + ", " + address.get(0).getSubAdminArea());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(TAG, "No location found");
                        }
                    }
                });
            }
        });

        //onClick for the button that will submit the guide
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Check if the user has filled out the fields in the guide
                if (editGuideTitle.getText().toString().isEmpty() || editGuideString.getText().toString().isEmpty() || editGuideUsername.getText().toString().isEmpty() || locationText.getText().toString().equals("Click the button")) {
                    hideKeyboard(editGuideString, editGuideTitle, editGuideUsername);
                    Snackbar.make(view, "A field(s) were left empty. Please fill them in.", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (checkBox.isChecked()) {
                        if(username == null){
                        AddData(editGuideUsername.getText().toString(), locationText.getText().toString());
                        } else {
                            databaseHelper.updateName(editGuideUsername.getText().toString(), locationText.getText().toString(), username);
                        }
                    }
                    //create guide and return to previous fragment
                    hideKeyboard(editGuideString, editGuideTitle, editGuideUsername);
                    writeNewGuide(guideCount, h.getId(), editGuideTitle.getText().toString(), editGuideString.getText().toString(), editGuideUsername.getText().toString(), locationText.getText().toString());
                    Snackbar.make(view, "Your guide was successfully created!", Snackbar.LENGTH_SHORT).show();

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.fragmentFrame, new GuideListFragment());
                    ft.commit();
                }
            }
        });

        return view;
    }

    //Create Guide object and push it to the database
    public void writeNewGuide(int guideId, int id, String title, String string, String username, String location) {
        DatabaseReference db = FirebaseDatabase.getInstance("https://dotaapp-29182-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        Guide guide = new Guide(id, title, string, username, location);
        db.child("Guides").child(String.valueOf(guideId)).setValue(guide);
    }

    //Will hide the keyboard once the submit guide button is pressed no matter which EditText field the user is on
    public void hideKeyboard(EditText a, EditText b, EditText c) {
        a.onEditorAction(EditorInfo.IME_ACTION_DONE);
        b.onEditorAction(EditorInfo.IME_ACTION_DONE);
        c.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }

    public void AddData(String username, String location) {
        boolean insertData = databaseHelper.addData(username, location);
        if (insertData) {
            Log.d(TAG, "Data successful");
        } else {
            Log.d(TAG, "Data bork");
        }
    }
}