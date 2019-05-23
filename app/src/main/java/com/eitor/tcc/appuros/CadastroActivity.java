package com.eitor.tcc.appuros;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView
        _nome, _cpf, _endereco, _telefone, _contatoEmergencia, _resmed;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        ArrayList<String> sangues = new ArrayList<>();
        sangues.add("A+");
        sangues.add("A-");
        sangues.add("B+");
        sangues.add("B-");
        sangues.add("AB+");
        sangues.add("AB-");
        sangues.add("O+");
        sangues.add("O-");

        ArrayAdapter<String> a;
        a = new ArrayAdapter<>(this, R.layout.spinner_item, sangues);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _nome = findViewById(R.id.nome);
        _cpf = findViewById(R.id.cpf);
        _endereco = findViewById(R.id.endereco);
        _contatoEmergencia = findViewById(R.id.contatoEmergencia);
        _resmed = findViewById(R.id.resmed);
        _telefone = findViewById(R.id.telefone);

        final Spinner tipoSanguineo = findViewById(R.id.spinnerSangues);

        tipoSanguineo.setAdapter(a);

        Button salvar = findViewById(R.id.btnCadastro);

        db = FirebaseFirestore.getInstance();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(CadastroActivity.this);
                assert conta != null;
                String
                        nome = conta.getDisplayName(),
                        cpf = _cpf.getEditableText().toString(),
                        endereco = _endereco.getEditableText().toString(),
                        telefone = _telefone.getEditableText().toString(),
                        contatoEmergencia = _contatoEmergencia.getEditableText().toString(),
                        resmed = _resmed.getEditableText().toString();
                if(CNP.isValidCPF(cpf) && !(nome.isEmpty()) && !(telefone.isEmpty()) && !(endereco.isEmpty())
                        && !(contatoEmergencia.isEmpty()) && !(resmed.isEmpty())) {
                    db = FirebaseFirestore.getInstance();
                    email = conta.getEmail();
                    Map<String, String> dados = new HashMap<>();

                    dados.put("nome", nome);
                    dados.put("cpf", cpf);
                    dados.put("endereco", endereco);
                    dados.put("telefone", telefone);
                    dados.put("contatoEmergencia", contatoEmergencia);
                    dados.put("restricoesMedicas", resmed);
                    dados.put("tipoSanguineo", tipoSanguineo.getSelectedItem().toString());

                    if (db.collection("usuarios").document(email.substring(0, email.indexOf("@"))).set(dados).isSuccessful())
                        Log.i("sucesso", "deu");
                    else
                        Log.i("falha", "fodeu");

                    startActivity(new Intent(CadastroActivity.this, ChamadasActivity.class));
                    CadastroActivity.this.finish();

                }if (!(CNP.isValidCPF(cpf))){
                    new AlertDialog.Builder(CadastroActivity.this)
                            .setTitle("Erro no Cadastro")
                            .setMessage("CPF inválido.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

                if ((nome.isEmpty()) || (telefone.isEmpty()) || (endereco.isEmpty())
                        || (contatoEmergencia.isEmpty()) || (resmed.isEmpty())){
                    new AlertDialog.Builder(CadastroActivity.this)
                            .setTitle("Erro no Cadastro")
                            .setMessage("Preencha todos os campos!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }

        });
    }
    

}
