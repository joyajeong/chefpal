package au.edu.unsw.infs3634.unswgamifiedlearningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,password, confirmPass;
    private Button signUpB;
    private ImageView backB;

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
            }
        });

    }
}