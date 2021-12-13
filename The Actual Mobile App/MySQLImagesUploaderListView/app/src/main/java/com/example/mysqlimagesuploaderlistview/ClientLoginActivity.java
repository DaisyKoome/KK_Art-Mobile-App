package com.example.mysqlimagesuploaderlistview;

import static com.example.mysqlimagesuploaderlistview.ClientPasswordDatabase.COL_3;
import static com.example.mysqlimagesuploaderlistview.ClientPasswordDatabase.COL_4;
import static com.example.mysqlimagesuploaderlistview.ClientPasswordDatabase.COL_6;

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

public class ClientLoginActivity extends AppCompatActivity {

    private static final String TAG = "ClientLoginActivity";

    ImageView goto_reg2;
    Button loginButton2;
    EditText emailET2;
    EditText passwordET2;
    ClientPasswordDatabase passwordDB2;

    Timer timer2 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        passwordDB2 = new ClientPasswordDatabase(this);

        goto_reg2 = findViewById(R.id.goto_reg2);
        loginButton2 = findViewById(R.id.login2);
        emailET2 = findViewById(R.id.email2);
        passwordET2 = findViewById(R.id.password2);

        loginButton2.setBackgroundColor(Color.YELLOW);

        goto_reg2.setOnClickListener(v -> {
            Log.d(TAG, "Clicked Go To Register");
            final Intent i = new Intent(ClientLoginActivity.this, ClientRegisterActivity.class);
            startActivity(i);
        });

        loginButton2.setOnClickListener(v -> {
            String email2 = emailET2.getText().toString().trim();
            String password2 = passwordET2.getText().toString().trim();

            Cursor cursor2 = passwordDB2.login_user(email2);
            int passwordPos2 = cursor2.getColumnIndex(COL_6);
            int firstNamePos2 = cursor2.getColumnIndex(COL_3);
            int lastNamePos2 = cursor2.getColumnIndex(COL_4);

            if (cursor2.moveToFirst()) {
                String pass2 = cursor2.getString(passwordPos2);
                String fname2 = cursor2.getString(firstNamePos2);
                String lname2 = cursor2.getString(lastNamePos2);
                if (pass2.equals(password2)) {
                    //Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ClientLoginActivity.this, ImageSliderMainActivity.class);
                            intent.putExtra(MainActivity.FNAME,fname2);
                            intent.putExtra(MainActivity.LNAME,lname2);
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
