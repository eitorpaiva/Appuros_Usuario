package com.eitor.tcc.appuros;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfigActivity extends AppCompatActivity {

    TextView
            nome, cpf, endereco, telefone, contatoEmergencia, resmed, sangue;
    ImageView foto;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        db = FirebaseFirestore.getInstance();
        final String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        final Uri fotoURL = GoogleSignIn.getLastSignedInAccount(this).getPhotoUrl();


        foto = findViewById(R.id.perfil);
        nome = findViewById(R.id.nomeConfig);
        cpf = findViewById(R.id.cpfConfig);
        endereco = findViewById(R.id.enderecoConfig);
        telefone = findViewById(R.id.telefoneConfig);
        contatoEmergencia = findViewById(R.id.emergenciaConfig);
        resmed = findViewById(R.id.resmedConfig);
        sangue = findViewById(R.id.sangueConfig);

        db.collection("usuarios").document(email.substring(0, email.indexOf("@"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!(fotoURL == null)) {
                    new DownloadImageTask(foto, ConfigActivity.this).execute(fotoURL.toString());
                }
                foto.setImageURI(fotoURL);
                nome.setText("Nome: " + documentSnapshot.get("nome").toString());
                cpf.setText("CPF: " + documentSnapshot.get("cpf").toString());
                endereco.setText("Endereço: " + documentSnapshot.get("endereco").toString());
                telefone.setText("Telefone: " + documentSnapshot.get("telefone").toString());
                contatoEmergencia.setText("Contato de Emergência: " + documentSnapshot.get("contatoEmergencia").toString());
                resmed.setText("Restrições Médicas: " + documentSnapshot.get("restricoesMedicas").toString());
                sangue.setText("Tipo Sanguíneo: " + documentSnapshot.get("tipoSanguineo").toString());
            }
        });


        Button btnEditar = findViewById(R.id.btnEditarConfig);
        Button btnLogout = findViewById(R.id.btnLogoutConfig);
        Button btnApagar = findViewById(R.id.btnApagarConfig);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference editarDocRef = db
                        .collection("usuarios")
                        .document(email.substring(0, email.indexOf("@")));

                editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent i = new Intent(ConfigActivity.this, CadastroActivity.class);
                        i.putExtra("nome", documentSnapshot.get("nome").toString());
                        i.putExtra("cpf", documentSnapshot.get("cpf").toString());
                        i.putExtra("endereco", documentSnapshot.get("endereco").toString());
                        i.putExtra("contatoEmergencia", documentSnapshot.get("contatoEmergencia").toString());
                        i.putExtra("restricoesMedicas", documentSnapshot.get("restricoesMedicas").toString());
                        i.putExtra("telefone", documentSnapshot.get("telefone").toString());
                        startActivity(i);
                    }
                });
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("usuarios")
                        .document(email.substring(0, email.indexOf("@")))
                        .delete();
                fazerLogout();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogout();
            }
        });
    }

    private void fazerLogout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ConfigActivity.this, LoginActivity.class));
                    }
                });
    }
}
