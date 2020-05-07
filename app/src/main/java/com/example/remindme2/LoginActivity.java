package com.example.remindme2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    ProgressDialog progress;
    EditText emailEt,passwordEt;
    Button loginBtn;
    TextView createAccountTv,forgetPassTv;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    private static final String TAG_G= "GoogleActivity";
    private static final String USER_PREF= "User";

    private static final int RC_SIGN_IN = 9001;
    com.google.android.gms.common.SignInButton gBtn;

    // [START declare_auth]
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;


    //int REQUEST_OVERLAY_PERMISSION=11;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();




    }



    public  void initialization(){

        emailEt=findViewById(R.id.emailEt);
        passwordEt=findViewById(R.id.passwordEt);
        loginBtn=findViewById(R.id.loginBtn);
        createAccountTv=findViewById(R.id.createAccountTv);
        forgetPassTv=findViewById(R.id.forgetPassTv);
        gBtn=findViewById(R.id.googleSignBtn);
        mAuth = FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                signIn();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(emailEt.getText().toString(),passwordEt.getText().toString());

            }
        });

        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotToRegister=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(gotToRegister);


            }
        });
        forgetPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(emailEt.getText().toString())) {
                    emailEt.setError("Required.");
                } else {
                    emailEt.setError(null);
                    sendPasswordReset(emailEt.getText().toString());

                }



            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }
public void login(String email,String pass){
    if (!validateForm()) {
        return;
    }

    progress.show();

    mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, user.getUid());
                        saveToShared(user.getUid());

                      //  Toast.makeText(getApplicationContext(), "Created Successfully please Login here !",
                        //        Toast.LENGTH_SHORT).show();
                        Intent goToMain=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(goToMain);
                        progress.hide();
                        finish();



                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed,"+  task.getException().getMessage(),
                                                      Toast.LENGTH_SHORT).show();


                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful()) {


                    }
                    progress.hide();
                }
            });




}

    @Override
    protected void onStart() {
        super.onStart();

       if( getFromShared()!=null){

           Intent goToMain=new Intent(getApplicationContext(),MainActivity.class);
             startActivity(goToMain);
              finish();

       }
//        if(FirebaseAuth.getInstance().getUid()!=null){
//            Intent goToMain=new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(goToMain);
//            finish();
//
//            Toast.makeText(getApplicationContext(),"ToMainYad",Toast.LENGTH_LONG).show();
//
//        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_G, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }



    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//      progress.show();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_G, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_G, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
progress.hide();                        // [END_EXCLUDE]
                    }
                });
    }

    public void sendPasswordReset(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {



                            showAlert();

                        }
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

    public void showAlert(){
        builder = new AlertDialog.Builder(this);
        builder.setMessage("We send Reset Link to Your Email:"+emailEt.getText().toString() +"\n"+"if yoi" +
                "if you want to open email press yes")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            getApplicationContext().startActivity(intent);
                        } catch (android.content.ActivityNotFoundException anfe) {

                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();



                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Reset Password");
        alert.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }



private void signIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
}
    private void updateUI(FirebaseUser user) {
progress.hide();
if (user != null) {

//    FirebaseUser user = mAuth.getCurrentUser();
    Log.d(TAG_G,"GUK:..." +user.getUid());
    saveToShared(user.getUid());
    Intent goToMain=new Intent(getApplicationContext(),MainActivity.class);
    startActivity(goToMain);
    finish();



        } else {






}
    }

    @Override
    protected void onPause() {
        super.onPause();
        progress.dismiss();
    }


    public  void saveToShared(String userId){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(USER_PREF,userId);


        editor.commit();



    }


    public  String getFromShared(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String  userId = settings.getString(USER_PREF, null);


  return  userId;
    }








}
