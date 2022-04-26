package com.example.electrocity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    private TextView register , forgotPassword;
    private EditText editTextEmail , editTextPassword;
    private ExtendedFloatingActionButton signIn;
    private ImageView newUser;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_login_user);

        mAuth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        newUser = findViewById(R.id.addNewUser);
        newUser.setOnClickListener(this);

        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.register:
            case R.id.addNewUser:
                startActivity(new Intent(this,RegisterUser.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                break;
            case  R.id.signIn:
                useLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this,ForgotPassword.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                break;
        }
    }

    private void useLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        startActivity(new Intent(LoginUser.this, ProfileActivity.class));
                    }
                    else {
                        Toast.makeText(LoginUser.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(LoginUser.this,"Failed to login! please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}