package com.example.remindme2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
ProgressDialog progress;
EditText emailEt,passwordEt;
Button registerBtn;
TextView toLoginBtn;
private FirebaseAuth mAuth;
private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        initialization();



    }


   public  void initialization(){

       emailEt=findViewById(R.id.emailEt);
       passwordEt=findViewById(R.id.passwordEt);
       registerBtn=findViewById(R.id.registerBtn);
       toLoginBtn=findViewById(R.id.toLoginBtn);
       mAuth = FirebaseAuth.getInstance();
       progress=new ProgressDialog(this);
       progress.setMessage("Creating Account...");
       progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       progress.setIndeterminate(true);
       progress.setCancelable(false);

       registerBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               register(emailEt.getText().toString(),passwordEt.getText().toString());
           }
       });

       toLoginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent gotToLogin=new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(gotToLogin);
               finish();
           }
       });


   }


   public  void register(String email,String pass){
       if (!validateForm()) {
           return;
       }

      progress.show();

       mAuth.createUserWithEmailAndPassword(email, pass)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Log.d(TAG, "signInWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           progress.hide();
                           Intent goToLogin=new Intent(getApplicationContext(),LoginActivity.class);
                           startActivity(goToLogin);
                           finish();

                       } else {
                           Log.w(TAG, "signInWithEmail:failure", task.getException());
                           Toast.makeText(getApplicationContext(), "Authentication failed,"+  task.getException().getMessage(),
                                   Toast.LENGTH_SHORT).show();




                       }

                       if (!task.isSuccessful()) {

                       }
                    progress.hide();

                   }
               });


   }
    private boolean validateForm() {
        boolean valid = true;

        String email = emailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Required.");
            valid = false;
        } else {
            emailEt.setError(null);
        }

        String password = passwordEt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEt.setError("Required.");
            valid = false;
        } else {
            passwordEt.setError(null);
        }

        return valid;
    }




}
