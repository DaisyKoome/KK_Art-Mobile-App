package com.example.mysqlimagesuploaderlistview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    ImageView back_to_login;
    EditText editTextNationalId, editTextFirstName, editTextLastName, editTextEmail,
            editTextPassword, editTextConPassword, editTextPhoneNo;
    Button register_user;
    PasswordDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDatabase = new PasswordDatabase(this);

        editTextNationalId  = findViewById(R.id.national_id);
        editTextFirstName = findViewById(R.id.fname);
        editTextLastName = findViewById(R.id.lname);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConPassword = findViewById(R.id.password_con);
        editTextPhoneNo = findViewById(R.id.pno);
        register_user = findViewById(R.id.register);

        back_to_login = findViewById(R.id.back_to_login);

        back_to_login.setOnClickListener(v -> {
            final Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });

        register_user.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String national_id = editTextNationalId.getText().toString().trim();
        String first_name = editTextFirstName.getText().toString().trim();
        String last_name = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String con_pass = editTextConPassword.getText().toString().trim();
        String phone_no = editTextPhoneNo.getText().toString().trim();

        if (!password.equals(con_pass)) {
            Toast.makeText(RegisterActivity.this, "The passwords are not matching", Toast.LENGTH_SHORT).show();
        } else if (!isEmailValid(email)) {
            Toast.makeText(RegisterActivity.this, "The email is not valid. Try again.", Toast.LENGTH_SHORT).show();
        } else if (!isPasswordValid(password)) {
            Toast.makeText(RegisterActivity.this, "The password is not long enough", Toast.LENGTH_SHORT).show();
        } else if (national_id.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "National ID field is required", Toast.LENGTH_SHORT).show();
        } else if (first_name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "First name field is required", Toast.LENGTH_SHORT).show();
        } else if (last_name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Last name field is required", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email field is required", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password field is required", Toast.LENGTH_SHORT).show();
        } else if (phone_no.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Phone number field is required", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = myDatabase.insertData(national_id, first_name, last_name, email, password, phone_no);

            if (isInserted) {
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText("You are registered")
                        .setConfirmText("OK")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        })
                        .show();
            } else {
                Toast.makeText(this, "Could not register, try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }
}
