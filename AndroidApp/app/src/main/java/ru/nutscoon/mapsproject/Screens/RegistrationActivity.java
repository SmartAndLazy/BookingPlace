package ru.nutscoon.mapsproject.Screens;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.RegistrationViewModel;

public class RegistrationActivity extends AppCompatActivity {

    RegistrationViewModel viewModel;

    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText password;
    private EditText confirmPassword;

    private TextInputLayout nameInputLayout;
    private TextInputLayout surnameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout phoneInputLayout;
    private TextInputLayout confirmPasswordInputLayout;

    private Button regBtn;

    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupViewModel();

        name = findViewById(R.id.reg_name);
        surname = findViewById(R.id.reg_surname);
        phone = findViewById(R.id.reg_phone);
        password = findViewById(R.id.reg_password);
        confirmPassword = findViewById(R.id.reg_confirm_password);

        nameInputLayout = findViewById(R.id.textInputLayoutName);
        surnameInputLayout = findViewById(R.id.textInputLayoutSurname);
        phoneInputLayout = findViewById(R.id.textInputLayoutPhone);
        passwordInputLayout = findViewById(R.id.textInputLayoutPassword);
        confirmPasswordInputLayout = findViewById(R.id.textInputLayoutConfirmPassword);

        regBtn = findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    viewModel.registerUser(name.getText().toString(),
                            surname.getText().toString(),
                            phone.getText().toString(),
                            password.getText().toString());

                }
            }
        });

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        viewModel.getRegistratinResultLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null){
                    Toast.makeText(getApplicationContext(), "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Не удаслось выполнить регистрацию.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate(){
        String userPhone = phone.getText().toString();
        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        boolean nameValid = !name.getText().toString().equals("");
        boolean surnameValid = !surname.getText().toString().equals("");
        boolean phoneValid = checkPhone(userPhone);
        boolean passValid = pass.length() >= 6;
        boolean confirmPassValid = pass.equals(confirmPass);

        setErrorOnEditText(nameInputLayout, name, nameValid, "Введите имя");
        setErrorOnEditText(surnameInputLayout, surname, surnameValid, "Введите фамилию");
        setErrorOnEditText(phoneInputLayout, phone, phoneValid, "Введите телефон");
        setErrorOnEditText(passwordInputLayout, password, passValid, "Пароль должен одеожать не меньше шести символов");
        setErrorOnEditText(confirmPasswordInputLayout, confirmPassword, confirmPassValid, "Пароли не совпадают");

        return nameValid && surnameValid && phoneValid && passValid && confirmPassValid;
    }

    private void setErrorOnEditText(TextInputLayout textInput, EditText et, boolean isDataValid, String error){
        if(isDataValid){
            textInput.setError(null);
        }
        else{
            textInput.setError(error);
            setFocusOnEditText(et);
        }
    }


    private void setFocusOnEditText(EditText editText){
        editText.requestFocusFromTouch();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private boolean checkPhone(String phone){
        return (phone.length() == 12 && phone.matches("[+]\\d+"))
                || (phone.length() == 11 && phone.matches("\\d+"));
    }
}
