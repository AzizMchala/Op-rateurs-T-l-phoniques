package com.example.oprateurstlphoniques;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText login,password;
    Button connexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        password=findViewById(R.id.password);
        connexion=findViewById(R.id.connexion);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=password.getText().toString();
                String userLogin = login.getText().toString();
                if(pass.equalsIgnoreCase("123456")){
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("USER_LOGIN", userLogin);
                    startActivity(intent);
                }
            }
        });

    }
}