package com.example.quizappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyprofileActivity extends AppCompatActivity {
    private EditText name,email;
    private LinearLayout editB;
    private Button cancelB,saveB;
    private TextView profileText;
    private String nameStr;
    private LinearLayout button_layout;
    private Dialog progressDialog;
    private TextView dialogText;
    private ImageView backB3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        name=findViewById(R.id.mp_name);
        email=findViewById(R.id.mp_email);
       // phone=findViewById(R.id.mp_phone);
        profileText=findViewById(R.id.profile_text);
        editB=findViewById(R.id.editB);
        cancelB=findViewById(R.id.cancelB);
        saveB=findViewById(R.id.saveB);
        backB3=findViewById(R.id.backB3);
        button_layout=findViewById(R.id.button_layout);

        progressDialog = new Dialog(MyprofileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating Data...");

        disableEditing();
        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                    saveData();
            }
        });
        backB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MyprofileActivity.this,MainActivity.class);
                startActivity(intent);
                MyprofileActivity.this.finish();
            }
        });
    }

    private void disableEditing() {

        name.setEnabled(false);
        email.setEnabled(false);
        //phone.setEnabled(false);

        button_layout.setVisibility(View.GONE);
        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());
        String profileName=DbQuery.myProfile.getName();
        profileText.setText(profileName.toUpperCase().substring(0,1));



    }
    private void enableEditing()
    {
        name.setEnabled(true);


        button_layout.setVisibility(View.VISIBLE);
    }
    private boolean validate()
    {
        nameStr=name.getText().toString();
        if(nameStr.isEmpty())
        {
            name.setError("Name cannot be empty");
            return false;
        }
        return true;
    }
    private void saveData()
    {
        progressDialog.show();
        DbQuery.saveProfileData(nameStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyprofileActivity.this, "Updated!!", Toast.LENGTH_SHORT).show();
                disableEditing();
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                Toast.makeText(MyprofileActivity.this, "Something Went wrong! Please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            MyprofileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}