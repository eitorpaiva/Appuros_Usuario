package com.eitor.tcc.appuros;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class EsperandoActivity extends AppCompatActivity {
    double lat, lon;
    LocationManager lm;
    FirebaseFirestore db;
    DocumentReference df;
    TextView status;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        extras = getIntent().getExtras();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        db = FirebaseFirestore.getInstance();

        String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();

        df = db.collection("usuarios").document(email.substring(0, email.indexOf("@")));

        df.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                status = findViewById(R.id.status);
                status.setTextColor(Color.parseColor(extras.getString("cor")));

                if (doc.get("nome").toString().endsWith("(em atendimento)")) {
                    status.setText("Atendente a caminho!");
                } else {
                    status.setText("Procurando atendentes...");
                }

                if (!doc.contains("servico")) {
                    startActivity(new Intent(EsperandoActivity.this, ChamadasActivity.class));
                    EsperandoActivity.this.finish();
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        String title = "Appuros";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor(extras.getString("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esperando);

        View linha = findViewById(R.id.linha);
        linha.setBackgroundColor(Color.parseColor(extras.getString("cor")));

        final TextView usuario = findViewById(R.id.usuario);
        usuario.setTextColor(Color.parseColor(extras.getString("cor")));

        db.collection("usuarios").document(email.substring(0, email.indexOf("@")))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario.setText("Usuário: " + documentSnapshot.get("nome").toString().split(" ")[0]);
            }
        });

        ImageView imagem = findViewById(R.id.imgEsperando);
        imagem.setImageResource(extras.getInt("img"));

        Button btn = findViewById(R.id.voltar);
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
                if (!status.getText().equals("Atendente a caminho!"))
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
                else
                    new AlertDialog.Builder(EsperandoActivity.this)
                            .setTitle("Acesso negado!")
                            .setMessage("Um atendente já está a caminho, não é possível cancelar a chamada.")
                            .setPositiveButton("OK", null)
                            .show();
            }
        });
    }
}
