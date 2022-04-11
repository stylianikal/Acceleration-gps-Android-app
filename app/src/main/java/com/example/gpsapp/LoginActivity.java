package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gpsapp.User.Authentication;
import com.example.gpsapp.User.User;

import java.sql.Timestamp;


public class LoginActivity extends AppCompatActivity {

    //private EditText InputName, InputPassword;
    private EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        //initializate of objects
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
    }


    public void loginAccount(View v){

        String username_;
        username_ = username.getText().toString();

        if (username_.isEmpty()){
            Toast.makeText(this, "Username is empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        new Database().user_login(username_,this);

    }
    public void user_authentication(String username_, Authentication authentication){
        String password_ = password.getText().toString();

        if (password_.isEmpty()){
            Toast.makeText(this, "Password is empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (authentication == null){
            Toast.makeText(this,"Wrong cretentials", Toast.LENGTH_SHORT).show();
            return;
        }
        if (authentication.getPassword().equals(password_)){
            new Database().load_user_data(username_,this);
        }
    }
    public void next(User user){
        if (user == null){
            Toast.makeText(this, "something went wrong, try to restart the application", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        Intent speedTracker = new Intent(this,SpeedTracker.class);
        speedTracker.putExtra("User", user);
        startActivity(speedTracker);
    }
}

