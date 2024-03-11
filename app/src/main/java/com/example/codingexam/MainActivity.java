package com.example.codingexam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Period;

public class MainActivity extends AppCompatActivity {

    Button submitButton;
    EditText fullname, email, mobileNumber;
    TextView tvAge;
    DatePicker dateOfBirth;
    Spinner spinnerGender;
    Button getSubmitButton;
    Integer age = 0;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fullname = findViewById(R.id.et_fullname);

         email = findViewById(R.id.et_email_address);
         mobileNumber = findViewById(R.id.et_mobile_number);
         dateOfBirth = findViewById(R.id.et_date_of_birth);
         tvAge = findViewById(R.id.tv_Age);
         spinnerGender = findViewById(R.id.spinner_gender);
         submitButton = findViewById(R.id.submit_button);
         scrollView = findViewById(R.id.scrollView);
        LocalDate currentDate = LocalDate.now();

         dateOfBirth.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
             tvAge.setError(null);

                if (currentDate.minus(Period.of(year,monthOfYear,dayOfMonth)).getYear()<=0){
                    age = 0;
                    tvAge.setText("Age: " + age);
                }else{
                    age = currentDate.minus(Period.of(year,monthOfYear,dayOfMonth)).getYear();
                    tvAge.setText("Age: " + age);
             }

         });



        String[] genders = {"Male", "Female"};

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genders);
        spinnerGender.setAdapter(ad);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFullnameValid();
                isEmailValid();
                isNumberValid();
                isAgeValid();
            }
        });
    }

    public void submit() {
        Toast.makeText(getApplicationContext(),
                        "Clicked",
                        Toast.LENGTH_SHORT)
                .show();
    }

    private void isEmailValid() {
        String emailInput = email.getText().toString();
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {

        } else {
            email.setError("Enter a valid email");
        }
    }

    private void isFullnameValid() {
        String fullnameText = fullname.getText().toString();
        if (fullnameText.isEmpty()) {
            fullname.setError("Fullname should not be empty");

        } else if (!fullnameText.matches(".,[a-zA-Z]")){
            fullname.setError("Only letters, (,) and (.) are accepted");

        } else {}
    }

    private void isNumberValid() {
        String numbers = mobileNumber.getText().toString();
        if((!numbers.matches("[0-9]")) || (numbers.length()!=11)){
            mobileNumber.setError("Enter a valid number");
        }
    }


    private void isAgeValid() {
        if (age<=17){
            tvAge.setError("");
            tvAge.setText("Age: " + age + ". User must be 18 and above");
        }else {tvAge.setError(null);
        }

    }




}
