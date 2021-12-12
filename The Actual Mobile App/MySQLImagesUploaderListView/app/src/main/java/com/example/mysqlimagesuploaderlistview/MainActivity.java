package com.example.mysqlimagesuploaderlistview;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

        final int PICK_IMAGE_REQUEST = 234;
        private Uri filePath;
        EditText nameEditText,descriptionEditText;
        private ImageView teacherImageView;
        private Button showChooserBtn,sendToMySQLBtn,takeSavePhotoBtn;
        private ProgressBar uploadProgressBar;
        String [] permission = { READ_EXTERNAL_STORAGE };
        ActivityResultLauncher<Intent> activityResultLauncher;
        private TextView textViewFirstName, textViewLastName;
        private String first_name, last_name;

        public static final String FNAME = "FNAME";
        public static final String LNAME = "LNAME";
        /*
        Our data object
         */
        class SpiritualTeacher{
            private String name,description,imageURL;
            public SpiritualTeacher(String name, String description) {
                this.name = name;
                this.description = description;
                this.imageURL = imageURL;
            }
            public String getName() {return name;}
            public String getDescription() {return description;}
        }
        /*
        Uploader
         */
        public class MyUploader {
            //YOU CAN USE EITHER YOUR IP ADDRESS OR  10.0.2.2 I depends on the Emulator. Make sure you append
            //the `index.php` when making a POST request
            //Most emulators support this
            private static final String DATA_UPLOAD_URL="http://aquagauge.co.ke/spiritualteachers/index.php";
            //if you use genymotion you can use this
            //private static final String DATA_UPLOAD_URL="http://10.0.3.2/php/spiritualteachers/index.php";
            //You can get your ip address by typing ipconfig/all in cmd
            //private static final String DATA_UPLOAD_URL="http://192.168.12.2/php/spiritualteachers/index.php";

            //INSTANCE FIELDS
            private final Context c;
            public MyUploader(Context c) {this.c = c;}
            /*
            SAVE/INSERT
             */
            public void upload(SpiritualTeacher s, final View...inputViews)
            {
                if(s == null){Toast.makeText(c, "No Data To Save", Toast.LENGTH_SHORT).show();}
                else {
                    File imageFile;
                    try {
                        imageFile = new File(getImagePath(filePath));

                    }catch (Exception e){
                        Toast.makeText(c, "Please pick an Image From Right Place, maybe Gallery or File Explorer so that we can get its path."+e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    uploadProgressBar.setVisibility(View.VISIBLE);

                    AndroidNetworking.upload(DATA_UPLOAD_URL)
                            .addMultipartFile("image",imageFile)
                            .addMultipartParameter("teacher_name",s.getName())
                            .addMultipartParameter("teacher_description",s.getDescription())
                            .addMultipartParameter("name","upload")
                            .setTag("MYSQL_UPLOAD")
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    if(response != null) {
                                        try{
                                            //SHOW RESPONSE FROM SERVER
                                            String responseString = response.get("message").toString();
                                            Toast.makeText(c, "PHP SERVER RESPONSE : " + responseString, Toast.LENGTH_LONG).show();

                                            if (responseString.equalsIgnoreCase("Success")) {
                                                //RESET VIEWS
                                                EditText nameEditText = (EditText) inputViews[0];
                                                EditText descriptionEditText = (EditText) inputViews[1];
                                                ImageView teacherImageView = (ImageView) inputViews[2];

                                                //nameEditText.setText("");
                                                descriptionEditText.setText("");
                                                teacherImageView.setImageResource(R.drawable.imgplaceholder);

                                            } else {
                                                Toast.makeText(c, "PHP WASN'T SUCCESSFUL. ", Toast.LENGTH_LONG).show();
                                            }
                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            Toast.makeText(c, "JSONException "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(c, "NULL RESPONSE. ", Toast.LENGTH_LONG).show();
                                    }
                                    uploadProgressBar.setVisibility(View.GONE);
                                }
                                @Override
                                public void onError(ANError error) {
                                    error.printStackTrace();
                                    uploadProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(c, "UNSUCCESSFUL :  ERROR IS : n"+error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        }

        private void showFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image To Upload"), PICK_IMAGE_REQUEST);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                //getData() will return the URI for that particular path
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    teacherImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getImagePath(Uri uri)
        {
            String[] projection={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,projection,null,null,null);
            if(cursor == null){
                return null;
            }
            int columnIndex= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s=cursor.getString(columnIndex);
            cursor.close();
            return s;
        }
        private boolean validateData()
        {
            String name=nameEditText.getText().toString();
            String description=descriptionEditText.getText().toString();
            if( name == null || description == null){  return false;  }

            if(name == "" || description == ""){  return false;  }

            if(filePath == null){return false;}

            return true;
        }




    void requestPermission(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                    activityResultLauncher.launch(intent);
                } catch (Exception e)
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    activityResultLauncher.launch(intent);
                }
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,permission,30);
            }
        }

        boolean checkPermission(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            {
                return Environment.isExternalStorageManager();
            }
            else {
                int readcheck = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
                return readcheck == PackageManager.PERMISSION_GRANTED;
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode)
            {
                case 30:
                    if (grantResults.length > 0)
                    {
                        boolean readper = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                        if (readper){
                            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "You denied permission", Toast.LENGTH_SHORT).show();
                    }
                break;
            }
        }

        /*
        OnCreate method. When activity is created
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            //getSupportActionBar().hide();

            textViewFirstName = findViewById(R.id.textViewFirstName);
            textViewLastName = findViewById(R.id.textViewLastName);

            Intent intent = getIntent();
            first_name = intent.getStringExtra(FNAME);
            last_name = intent.getStringExtra(LNAME);

            textViewFirstName.setText(first_name);
            textViewLastName.setText(last_name);

            nameEditText=findViewById(R.id.nameEditText);
            nameEditText.setText(first_name+ " " + last_name);
            descriptionEditText=findViewById(R.id.descriptionEditText);
            showChooserBtn=findViewById(R.id.chooseBtn);
            sendToMySQLBtn=findViewById(R.id.sendBtn);
            takeSavePhotoBtn = findViewById(R.id.takePhotoBtn);
            Button openActivityBtn=findViewById(R.id.openActivityBtn);
            Button openSlideShowBtn = findViewById(R.id.imageSliderActivityBtn);
            teacherImageView=findViewById(R.id.imageView);
            uploadProgressBar=findViewById(R.id.myProgressBar);

            showChooserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPermission()) {
                        showFileChooser();
                    }
                    else {
                        requestPermission();
                    }
                }



            });

            sendToMySQLBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validateData()) {
                        //GET VALUES
                        String teacher_name = nameEditText.getText().toString();
                        String teacher_description = descriptionEditText.getText().toString();

                        SpiritualTeacher s = new SpiritualTeacher(teacher_name, teacher_description);

                        //upload data to mysql
                        new MyUploader(MainActivity.this).upload(s, nameEditText, descriptionEditText, teacherImageView);
                    } else {
                        Toast.makeText(MainActivity.this, "PLEASE ENTER ALL FIELDS CORRECTLY ", Toast.LENGTH_LONG).show();
                    }
                }
            });

            openActivityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,ItemsActivity.class);
                    startActivity(intent);
                }
            });

            takeSavePhotoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,ImageCaptureActivity.class);
                    startActivity(intent);
                }
            });

            openSlideShowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ImageSliderMainActivity.class);
                            intent.putExtra(ImageSliderMainActivity.FNAME,first_name);
                            intent.putExtra(ImageSliderMainActivity.LNAME,last_name);
                            startActivity(intent);
                }
            });
        }

        @Override
        public  boolean onCreateOptionsMenu (Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, menu);
            return true;
        }

}

