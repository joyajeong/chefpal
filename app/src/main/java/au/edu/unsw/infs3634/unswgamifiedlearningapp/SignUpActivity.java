package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,password, confirmPass;
    private Button signUpB;
    private ImageView backB;
    private FirebaseAuth mAuth;
    private String emailStr, passwordStr, confirmPassStr, nameStr;
    private static String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.userName);
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        backB = findViewById(R.id.backB);
        signUpB = findViewById(R.id.signupB);
        mAuth = FirebaseAuth.getInstance();


        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //connect to firebase
                if(validate()){
                    signupNewUser();
                }
            }
        });

    }

    private boolean validate(){
        //delete start or end space of userInputs
        nameStr = name.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if(nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }
        if(emailStr.isEmpty()){
            email.setError("Enter E-Mail ID");
            return false;
        }

        if(passwordStr.isEmpty()){
            password.setError("Enter Password");
            return false;
        }

        if(confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Password");
            return false;
        }
        //check password
        if(passwordStr.compareTo(confirmPassStr) != 0){
            Toast.makeText(SignUpActivity.this,"Password and confirm password should be same!"
            ,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signupNewUser(){
        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(SignUpActivity.this, "Sign Up Successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            SignUpActivity.this.finish();
                            //Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}