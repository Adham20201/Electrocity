package com.example.electrocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {



    private FirebaseDatabase rootNode;

    private FirebaseUser user;
    private DatabaseReference reference;
    private DatabaseReference reference2;

    private String userID;

    LinearLayout cardViews;
    CardView MaximumComfort, MediumComfort, MinimumComfort;

    private ExtendedFloatingActionButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        MaximumComfort = findViewById(R.id.MaximumComfort);
        MaximumComfort.setOnClickListener(this);
        MediumComfort = findViewById(R.id.MediumComfort);
        MediumComfort.setOnClickListener(this);
        MinimumComfort = findViewById(R.id.MinimumComfort);
        MinimumComfort.setOnClickListener(this);



        logout = findViewById(R.id.signOut);
        logout.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://electrocity-f377d-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
        userID = user.getUid();

        final TextView fullNameTextView = findViewById(R.id.fullName);
        final TextView phoneNumberTextView = findViewById(R.id.phoneNumber);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.phoneNumber;

                    fullNameTextView.setText(fullName);
                    phoneNumberTextView.setText(age);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginUser.class));
                break;
            case R.id.MaximumComfort:
                rootNode = FirebaseDatabase.getInstance("https://electrocity-f377d-default-rtdb.europe-west1.firebasedatabase.app/");
                reference2 = rootNode.getReference("Users");
                reference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("option").setValue("Maximum Comfort, Minimum Cost-efficiency");
                break;
            case R.id.MediumComfort:
                rootNode = FirebaseDatabase.getInstance("https://electrocity-f377d-default-rtdb.europe-west1.firebasedatabase.app/");
                reference2 = rootNode.getReference("Users");
                reference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("option").setValue("Medium Comfort, Medium Cost-efficiency");

                break;
            case R.id.MinimumComfort:
                rootNode = FirebaseDatabase.getInstance("https://electrocity-f377d-default-rtdb.europe-west1.firebasedatabase.app/");
                reference2 = rootNode.getReference("Users");
                reference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("option").setValue("Minimum Comfort, Maximum Cost-efficiency");
                break;

        }
    }
}