package com.example.mysqlimagesuploaderlistview;

import static com.example.mysqlimagesuploaderlistview.PasswordDatabase.COL_3;
import static com.example.mysqlimagesuploaderlistview.PasswordDatabase.COL_4;
import static com.example.mysqlimagesuploaderlistview.PasswordDatabase.COL_6;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    ImageView goto_reg;
    Button loginButton;
    EditText emailET;
    EditText passwordET;
    PasswordDatabase passwordDB;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordDB = new PasswordDatabase(this);

        goto_reg = findViewById(R.id.goto_reg);
        loginButton = findViewById(R.id.login);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);

        loginButton.setBackgroundColor(Color.YELLOW);

        goto_reg.setOnClickListener(v -> {
            Log.d(TAG, "Clicked Go To Register");
            final Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            Cursor cursor = passwordDB.login_user(email);
            int passwordPos = cursor.getColumnIndex(COL_6);
            int firstNamePos = cursor.getColumnIndex(COL_3);
            int lastNamePos = cursor.getColumnIndex(COL_4);

            if (cursor.moveToFirst()) {
                String pass = cursor.getString(passwordPos);
                String fname = cursor.getString(firstNamePos);
                String lname = cursor.getString(lastNamePos);
                if (pass.equals(password)) {
                    //Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(MainActivity.FNAME,fname);
                            intent.putExtra(MainActivity.LNAME,lname);
                            startActivity(intent);
                            finish();
                        }
                    }, 500);
                } else {
                    Toast.makeText(this, "No email matches password!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No account found!", Toast.LENGTH_SHORT).show();
            }
        });
    }

/*    public void sendData() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        Cursor cursor = passwordDB.login_user(email);
        int passwordPos = cursor.getColumnIndex(COL_3);
    }*/

}
