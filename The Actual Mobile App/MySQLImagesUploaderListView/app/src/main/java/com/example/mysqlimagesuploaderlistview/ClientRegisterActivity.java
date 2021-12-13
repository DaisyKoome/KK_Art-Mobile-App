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

public class ClientRegisterActivity extends AppCompatActivity {

    ImageView back_to_login2;
    EditText editTextNationalId2, editTextFirstName2, editTextLastName2, editTextEmail2,
            editTextPassword2, editTextConPassword2, editTextPhoneNo2;
    Button register_user2;
    ClientPasswordDatabase myDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        myDatabase2 = new ClientPasswordDatabase(this);

        editTextNationalId2  = findViewById(R.id.national_id2);
        editTextFirstName2 = findViewById(R.id.fname2);
        editTextLastName2 = findViewById(R.id.lname2);
        editTextEmail2 = findViewById(R.id.email2);
        editTextPassword2 = findViewById(R.id.password2);
        editTextConPassword2 = findViewById(R.id.password_con2);
        editTextPhoneNo2 = findViewById(R.id.pno2);
        register_user2 = findViewById(R.id.register2);

        back_to_login2 = findViewById(R.id.back_to_login2);

        back_to_login2.setOnClickListener(v -> {
            final Intent i = new Intent(ClientRegisterActivity.this, ClientLoginActivity.class);
            startActivity(i);
        });

        register_user2.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String national_id2 = editTextNationalId2.getText().toString().trim();
        String first_name2 = editTextFirstName2.getText().toString().trim();
        String last_name2 = editTextLastName2.getText().toString().trim();
        String email2 = editTextEmail2.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String con_pass2 = editTextConPassword2.getText().toString().trim();
        String phone_no2 = editTextPhoneNo2.getText().toString().trim();

        if (!password2.equals(con_pass2)) {
            Toast.makeText(ClientRegisterActivity.this, "The passwords are not matching", Toast.LENGTH_SHORT).show();
        } else if (!isEmailValid(email2)) {
            Toast.makeText(ClientRegisterActivity.this, "The email is not valid. Try again.", Toast.LENGTH_SHORT).show();
        } else if (!isPasswordValid(password2)) {
            Toast.makeText(ClientRegisterActivity.this, "The password is not long enough", Toast.LENGTH_SHORT).show();
        } else if (national_id2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "National ID field is required", Toast.LENGTH_SHORT).show();
        } else if (first_name2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "First name field is required", Toast.LENGTH_SHORT).show();
        } else if (last_name2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "Last name field is required", Toast.LENGTH_SHORT).show();
        } else if (email2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "Email field is required", Toast.LENGTH_SHORT).show();
        } else if (password2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "Password field is required", Toast.LENGTH_SHORT).show();
        } else if (phone_no2.isEmpty()) {
            Toast.makeText(ClientRegisterActivity.this, "Phone number field is required", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = myDatabase2.insertData(national_id2, first_name2, last_name2, email2, password2, phone_no2);

            if (isInserted) {
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText("You are registered")
                        .setConfirmText("OK")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            Intent i = new Intent(ClientRegisterActivity.this, ClientLoginActivity.class);
                            startActivity(i);
                        })
                        .show();
            } else {
                Toast.makeText(this, "Could not register, try again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email2) {
        return Patterns.EMAIL_ADDRESS.matcher(email2).matches();
    }

    private boolean isPasswordValid(String password2) {
        return password2.length() > 5;
    }
}
