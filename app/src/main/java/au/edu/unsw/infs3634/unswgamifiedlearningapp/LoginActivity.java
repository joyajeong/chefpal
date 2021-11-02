package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginB;
    private TextView forgotPassB, singupB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginB = findViewById(R.id.loginB);
        singupB = findViewById(R.id.signupB);
        forgotPassB = findViewById(R.id.forgot_pass);

        //loginButton
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connect to firebase
                //check validation
                if(validateData()){
                    login();

                }
            }
        });

        singupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validateData(){
        //boolean status = false;
        if(email.getText().toString().isEmpty()){
            email.setError("Enter E-Mail ID");
            return false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Enter Password");
            return false;
        }
        return true;
    }
    private void login(){
        // next tim...sdfsdfsd
    }



}