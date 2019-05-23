package com.eitor.tcc.appuros;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChamadasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamadas);

        TextView frufru = findViewById(R.id.usuario);

        frufru.setText("Usuário: " + GoogleSignIn.getLastSignedInAccount(this).getGivenName());
    }

    public void abrirSAMU(View view) {
        Intent i = new Intent(ChamadasActivity.this,EsperandoActivity.class);
        i.putExtra("cor","#FF6F00");
        i.putExtra("btn",R.drawable.my_button_samu);
        i.putExtra("img",R.drawable.ic_samu_icon);
        enviarServico("samu");
        startActivity(i);
    }

    public void abrirBomb(View view) {
        Intent i = new Intent(ChamadasActivity.this,EsperandoActivity.class);
        i.putExtra("cor","#C62828");
        i.putExtra("btn",R.drawable.my_button_bomb);
        i.putExtra("img",R.drawable.ic_bombeiros_icon);
        enviarServico("bomb");
        startActivity(i);
    }

    public void abrirPM(View view) {
        Intent i = new Intent(ChamadasActivity.this,EsperandoActivity.class);
        i.putExtra("cor","#385eaa");
        i.putExtra("btn",R.drawable.my_button_pm);
        i.putExtra("img",R.drawable.ic_pm_icon);
        enviarServico("pm");
        startActivity(i);
    }

    void enviarServico(String servico) {
        String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference df = db
                .collection("usuarios")
                .document(email.substring(0, email.indexOf("@")));
        Log.e("usuario", email.substring(0, email.indexOf("@")));
        df.update("servico", servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("task", "bem-sucedida");
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
    }
}
