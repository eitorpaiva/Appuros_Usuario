package com.eitor.tcc.appuros;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EsperandoActivity extends AppCompatActivity {
    double lat, lon;
    LocationManager lm;
    FirebaseFirestore db;
    DocumentReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        String title = "Appuros";
        Bundle extras = getIntent().getExtras();
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor(extras.getString("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esperando);

        View linha = (View) findViewById(R.id.linha);
        linha.setBackgroundColor(Color.parseColor(extras.getString("cor")));

        TextView usuario = (TextView) findViewById(R.id.usuario);
        usuario.setTextColor(Color.parseColor(extras.getString("cor")));
        usuario.setText("Usuário: " + GoogleSignIn.getLastSignedInAccount(this).getGivenName());

        ImageView imagem = (ImageView) findViewById(R.id.imgEsperando);
        imagem.setImageResource(extras.getInt("img"));

        Button btn = (Button) findViewById(R.id.voltar);
        btn.setBackgroundResource(extras.getInt("btn"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = GoogleSignIn.getLastSignedInAccount(EsperandoActivity.this).getEmail();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference df = db
                        .collection("usuarios")
                        .document(email.substring(0, email.indexOf("@")));
                Log.e("usuario", email.substring(0, email.indexOf("@")));
                df.update("servico", FieldValue.delete()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        df.update("gps", FieldValue.delete()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(EsperandoActivity.this, ChamadasActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }
}
