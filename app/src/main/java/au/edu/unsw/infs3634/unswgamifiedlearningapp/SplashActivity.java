package au.edu.unsw.infs3634.unswgamifiedlearningapp;
// opening

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.TypeAdapterFactory;

public class SplashActivity extends AppCompatActivity {
    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName = findViewById(R.id.app_name);

        //change font
        Typeface typeface = ResourcesCompat.getFont(this,)

    }
}