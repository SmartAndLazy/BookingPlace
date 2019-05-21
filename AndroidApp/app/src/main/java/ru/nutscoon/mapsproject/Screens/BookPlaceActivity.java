package ru.nutscoon.mapsproject.Screens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.BookPlaceViewModel;

public class BookPlaceActivity extends AppCompatActivity {

    BookPlaceViewModel viewModel;

    private int orgId;

    private TextView date;
    private TextView time;
    private EditText name;
    private EditText surname;
    private EditText phone;

    private TextInputLayout nameInputLayout;
    private TextInputLayout surnameInputLayout;
    private TextInputLayout phoneInputLayout;

    private Button btn;

    private InputMethodManager imm;

    private boolean isDateSelected;
    private boolean isTimeSelected;

    Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_place);
        setupViewModel();

        date = findViewById(R.id.book_date);
        time = findViewById(R.id.book_time);
        name = findViewById(R.id.book_name);
        surname = findViewById(R.id.book_surname);
        phone = findViewById(R.id.book_phone);

        nameInputLayout = findViewById(R.id.textInputLayoutName);
        surnameInputLayout = findViewById(R.id.textInputLayoutSurname);
        phoneInputLayout = findViewById(R.id.textInputLayoutPhone);

        Intent intent = getIntent();
        orgId = intent.getIntExtra("orgId", -1);

        btn = findViewById(R.id.book_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    viewModel.bookPlace(orgId, parseDate(date.getText().toString()),
                            time.getText().toString() + ":00",
                            name.getText().toString(),
                            surname.getText().toString(),
                            phone.getText().toString());
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(BookPlaceActivity.this, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(BookPlaceActivity.this, t, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true).show();
            }
        });
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.ready();
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this).get(BookPlaceViewModel.class);
        viewModel.getBookPlaceResultLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Toast.makeText(getApplicationContext(), "Место забронированно", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                phone.setText(user.getPhone());
                name.setText(user.getName());
                surname.setText(user.getSurname());
            }
        });
    }

    private boolean validate(){
        String userPhone = phone.getText().toString();

        boolean nameValid = !name.getText().toString().equals("");
        boolean surnameValid = !surname.getText().toString().equals("");
        boolean phoneValid = checkPhone(userPhone);

        setErrorOnEditText(nameInputLayout, name, nameValid, "Введите имя");
        setErrorOnEditText(surnameInputLayout, surname, surnameValid, "Введите фамилию");
        setErrorOnEditText(phoneInputLayout, phone, phoneValid, "Введите телефон");

        return nameValid && surnameValid && phoneValid && isDateSelected && isDateSelected;
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

    private Date parseDate(String s){
        SimpleDateFormat parser = new SimpleDateFormat("dd.yyyy.MM");
        try {
            return parser.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            time.setText(i + ":" + i1);
            isTimeSelected = true;
        }
    };

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String d = i2 + "." + i1 + "." + i;
            date.setText(d);
            isDateSelected = true;
            Intent intent = new Intent(BookPlaceActivity.this, HoursActivity.class);
            //add userId
          //  intent.putExtra("id", user)
            intent.putExtra("id", orgId);
            intent.putExtra("date", d);
            startActivityForResult(intent,1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String name = data.getStringExtra("time");
        time.setText(name);
        isTimeSelected = true;
    }

}
