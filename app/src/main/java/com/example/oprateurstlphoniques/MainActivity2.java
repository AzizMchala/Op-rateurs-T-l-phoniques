package com.example.oprateurstlphoniques;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView7, ligne, premierCode;
    private ImageView imageView;
    private Button buttonTakePhoto, buttonAppeler, button2;
    private EditText editNumero, editCode, codeComplet;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALL_PERMISSION_REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView7 = findViewById(R.id.textView7);
        imageView = findViewById(R.id.imageView);
        buttonTakePhoto = findViewById(R.id.buttonTakePhoto);
        buttonAppeler = findViewById(R.id.buttonAppeler);
        button2=findViewById(R.id.button2);
        editNumero = findViewById(R.id.editNumero);
        editCode = findViewById(R.id.editCode);
        codeComplet = findViewById(R.id.codeComplet);
        ligne = findViewById(R.id.ligne);
        premierCode = findViewById(R.id.premiereCode);

        String userLogin = getIntent().getStringExtra("USER_LOGIN");
        textView7.setText("Bonjour, " + userLogin);

        // Gestion du clic pour prendre une photo
        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity2.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                } else {
                    takePhoto();
                }
            }
        });

        // Ajout du TextWatcher pour gérer les modifications du numéro
        editNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String num = editNumero.getText().toString();
                if (num.length() != 8) {
                    Toast.makeText(MainActivity2.this, "La longueur du numéro doit être de 8 chiffres", Toast.LENGTH_SHORT).show();
                } else {
                    String prefix = num.substring(0, 2);
                    int prefixInt = Integer.parseInt(prefix);
                    updateOperatorAndCode(prefixInt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String code = editCode.getText().toString();
                if (code.length() == 14) {
                    updateCodeComplete();
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        buttonAppeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passerAppel();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passerCode();
            }
        });
    }

    private void passerAppel() {
        String ussdCode = codeComplet.getText().toString();
        if (ussdCode.isEmpty()) {
            Toast.makeText(MainActivity2.this, "Veuillez entrer un code USSD complet", Toast.LENGTH_SHORT).show();
        } else {
            // Encodage correct du code USSD
            String encodedUssd = Uri.encode(ussdCode);
            String uri = "tel:" + encodedUssd;

            // Utilisation d'ACTION_DIAL pour éviter le problème MMI
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
            startActivity(callIntent);
        }
    }
    private void passerCode() {
        String ussdCode = premierCode.getText().toString();
        if (ussdCode.isEmpty()) {
            Toast.makeText(MainActivity2.this, "Veuillez entrer un code", Toast.LENGTH_SHORT).show();
        } else {
            // Encodage correct du code USSD
            String encodedUssd = Uri.encode(ussdCode);
            String uri = "tel:" + encodedUssd;

            // Utilisation d'ACTION_DIAL pour éviter le problème MMI
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
            startActivity(callIntent);
        }
    }

    private void updateOperatorAndCode(int prefixInt) {
        if (prefixInt >= 50 && prefixInt <= 59) {
            ligne.setText("Votre opérateur est : Orange");
            ligne.setTextColor(Color.YELLOW);
            premierCode.setBackgroundColor(Color.YELLOW);
            premierCode.setText("*111#");
        } else if (prefixInt >= 90 && prefixInt <= 99) {
            ligne.setText("Votre opérateur est : Tunisie Télécom");
            ligne.setTextColor(Color.BLUE);
            premierCode.setBackgroundColor(Color.BLUE);
            premierCode.setText("*100#");
        } else if (prefixInt >= 20 && prefixInt <= 29) {
            ligne.setText("Votre opérateur est : Ooredoo");
            ligne.setTextColor(Color.RED);
            premierCode.setBackgroundColor(Color.RED);
            premierCode.setText("*122#");
        } else {
            ligne.setText("Opérateur inconnu");
        }
    }

    private void updateCodeComplete() {
        String code = editCode.getText().toString();
        String operator = ligne.getText().toString();
        if (operator.contains("Orange")) {
            codeComplet.setText("*100*" + code + "#");
            codeComplet.setBackgroundColor(Color.YELLOW);
        } else if (operator.contains("Tunisie Télécom")) {
            codeComplet.setText("*123*" + code + "#");
            codeComplet.setBackgroundColor(Color.BLUE);
        } else if (operator.contains("Ooredoo")) {
            codeComplet.setText("*101*" + code + "#");
            codeComplet.setBackgroundColor(Color.RED);
        }
    }

    private void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Permission de la caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
