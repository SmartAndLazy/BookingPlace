package ru.nutscoon.mapsproject.Screens;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    LoginViewModel viewModel;

    private EditText phone;
    private EditText pass;
    private Button login;
    private Button registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        setupViewModel();
        phone = findViewById(R.id.auth_phone);
        pass = findViewById(R.id.auth_password);
        login = findViewById(R.id.auth_btn);
        registration = findViewById(R.id.auth_reg_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.login(phone.getText().toString(), pass.getText().toString());
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null){
                    Toast.makeText(getApplicationContext(), "Вы вошли как" + user.getName(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
