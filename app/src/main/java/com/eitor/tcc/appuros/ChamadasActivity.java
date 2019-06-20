package com.eitor.tcc.appuros;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChamadasActivity extends AppCompatActivity {


    TextView frufru;

    FirebaseFirestore db;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        db = FirebaseFirestore.getInstance();

        email = GoogleSignIn.getLastSignedInAccount(this).getEmail();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamadas);


        // fetchLastLocation();

        final TextView frufru = findViewById(R.id.usuario);

        db.collection("usuarios").document(email.substring(0, email.indexOf("@"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                frufru.setText("Usuário: " + documentSnapshot.get("nome").toString().split(" ")[0]);
            }
        });

        frufru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference editarDocRef = db
                        .collection("usuarios")
                        .document(email.substring(0, email.indexOf("@")));

                editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent i = new Intent(ChamadasActivity.this, CadastroActivity.class);
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
    }

    public void abrirSAMU(View view) {
        Intent i = new Intent(ChamadasActivity.this, GravidadeActivity.class);
        i.putExtra("cor","#FF6F00");
        i.putExtra("btn",R.drawable.my_button_samu);
        i.putExtra("img",R.drawable.ic_samu_icon);
        i.putExtra("servico", "samu");

        DocumentReference editarDocRef = db
                .collection("usuarios")
                .document(email.substring(0, email.indexOf("@")));

        editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("servico") == null || documentSnapshot.get("servico").toString().equals("samu")) {
                    if (!documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ChamadasActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", "#FF6F00");
                        i.putExtra("btn", R.drawable.my_button_samu);
                        i.putExtra("img", R.drawable.ic_samu_icon);
                        i.putExtra("servico", "samu");
                        startActivity(i);
                    }
                } else if (!documentSnapshot.get("servico").toString().equals("samu")) {
                    if (documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento enquanto estiver em atendimento!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento se já tiver solicitado algum!\nCancele a chamada e tente novamente!")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }
        });

    }

    public void abrirBomb(View view) {
        Intent i = new Intent(ChamadasActivity.this, GravidadeActivity.class);
        i.putExtra("cor","#C62828");
        i.putExtra("btn",R.drawable.my_button_bomb);
        i.putExtra("img",R.drawable.ic_bombeiros_icon);
        i.putExtra("servico", "bomb");

        DocumentReference editarDocRef = db
                .collection("usuarios")
                .document(email.substring(0, email.indexOf("@")));

        editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("servico") == null || documentSnapshot.get("servico").toString().equals("bomb")) {
                    if (!documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ChamadasActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", "#C62828");
                        i.putExtra("btn", R.drawable.my_button_bomb);
                        i.putExtra("img", R.drawable.ic_bombeiros_icon);
                        i.putExtra("servico", "bomb");
                        startActivity(i);
                    }
                } else if (!documentSnapshot.get("servico").toString().equals("bomb")) {
                    if (documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento enquanto estiver em atendimento!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento se já tiver solicitado algum!\nCancele a chamada e tente novamente!")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }
        });
    }

    public void abrirPM(View view) {
        Intent i = new Intent(ChamadasActivity.this, GravidadeActivity.class);
        i.putExtra("cor", "#385eaa");
        i.putExtra("btn", R.drawable.my_button_pm);
        i.putExtra("img", R.drawable.ic_pm_icon);
        i.putExtra("servico", "pm");

        DocumentReference editarDocRef = db
                .collection("usuarios")
                .document(email.substring(0, email.indexOf("@")));

        editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("servico") == null || documentSnapshot.get("servico").toString().equals("pm")) {
                    if (!documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        startActivity(i);
                    } else {
                        Intent i = new Intent(ChamadasActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", "#385eaa");
                        i.putExtra("btn", R.drawable.my_button_pm);
                        i.putExtra("img", R.drawable.ic_pm_icon);
                        i.putExtra("servico", "pm");
                        startActivity(i);
                    }
                } else if (!documentSnapshot.get("servico").toString().equals("pm")) {
                    if (documentSnapshot.get("nome").toString().endsWith("(em atendimento)")) {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento enquanto estiver em atendimento!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(ChamadasActivity.this)
                                .setTitle("Acesso negado!")
                                .setMessage("Não é possível solicitar outro atendimento se já tiver solicitado algum!\nCancele a chamada e tente novamente!")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mdica:
                Toast.makeText(this,
                        "193 - Corpo de Bombeiros\n" +
                                "192 - Serviço de Atendimento Médico de Urgência (SAMU)\n" +
                                "190 - Polícia Militar (PM)\n" +
                                "191 - Polícia Rodoviária Federal (PRF)\n" +
                                "197 - Polícia Civil (PC)\n" +
                                "180 - Delegacia da Mulher", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.msobre:
                Intent i = new Intent(ChamadasActivity.this, SobreActivity.class);
                startActivity(i);
                break;
            case R.id.mconfig:
                Intent j = new Intent(ChamadasActivity.this, ConfigActivity.class);
                startActivity(j);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}