package com.example.electrocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private ExtendedFloatingActionButton registerUser;
    private TextView haveAccount ;
    private EditText editTextFullName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ImageView back;

    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));


        mAuth = FirebaseAuth.getInstance();



        registerUser = findViewById(R.id.RegisterUser);
        registerUser.setOnClickListener(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);

        haveAccount = findViewById(R.id.haveAccount);
        haveAccount.setOnClickListener(this);

        editTextFullName = findViewById(R.id.fullName);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
            case R.id.haveAccount:
                startActivity(new Intent(this, LoginUser.class));
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.RegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()){
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (age.isEmpty()){
            editTextPhoneNumber.setError("Age is required!");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6){
            editTextPassword.setError("Min password length be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    User user = new User(fullName,age,email);
                    rootNode = FirebaseDatabase.getInstance("https://electrocity-f377d-default-rtdb.europe-west1.firebasedatabase.app/");
                    reference = rootNode.getReference("Users");
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterUser.this,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterUser.this, SetupActivity.class));

                                        }else {
                                            Toast.makeText(RegisterUser.this,"Failed to register!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(RegisterUser.this,"Failed to register!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterUser.this, "Failed to register!" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}