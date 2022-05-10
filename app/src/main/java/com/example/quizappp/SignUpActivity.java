package com.example.quizappp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,pass,confirmpass;
    private Button signupb;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr,passStr,confirmpassStr,nameStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.username);
        email=findViewById(R.id.emailID);
        pass=findViewById(R.id.password);
        confirmpass=findViewById(R.id.confirm_pass);
        signupb=findViewById(R.id.signupb);
        backB=findViewById(R.id.backB);

        progressDialog=new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User...");
        mAuth = FirebaseAuth.getInstance();
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        signupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    signupNewUser();
                }
            }
        });
    }
    private boolean validate()
    {
        nameStr=name.getText().toString().trim();
        passStr=pass.getText().toString().trim();
        emailStr=email.getText().toString().trim();
        confirmpassStr=confirmpass.getText().toString().trim();

        if(nameStr.isEmpty())
        {
            name.setError("Enter your Name");
            return false;
        }
        if(emailStr.isEmpty())
        {
            email.setError("Enter EMail ID");
            return false;
        }
        if(passStr.isEmpty())
        {
            pass.setError("Enter Password");
            return false;
        }
        if(confirmpassStr.isEmpty())
        {
            confirmpass.setError("Enter Password");
            return false;
        }
        if(passStr.compareTo(confirmpassStr)!=0)
        {
            Toast.makeText(SignUpActivity.this, "Password and confirm Password should be same !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void signupNewUser(){
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign Up Successfull ", Toast.LENGTH_SHORT).show();
                            DbQuery.createUserData(emailStr,nameStr, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadCategories(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignUpActivity.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
}