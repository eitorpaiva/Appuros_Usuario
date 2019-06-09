package com.eitor.tcc.appuros;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    final int CODIGO_LOGIN = 666;
    private static final int REQUEST_CODE = 101;
    AlertDialog carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        carregando = new AlertDialog.Builder(this)
        .setTitle("Aguarde")
        .setMessage("Carregando...")
        .show();

        FirebaseApp.initializeApp(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(this);
            String e = conta.getEmail();
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("usuarios").document(e.substring(0, e.indexOf("@")));
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                      carregando.dismiss();
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(LoginActivity.this, ChamadasActivity.class));
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
          carregando.dismiss();
            Button login = findViewById(R.id.sign_in);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(intent, CODIGO_LOGIN);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount conta = task.getResult(ApiException.class);
            String e = conta.getEmail();
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("usuarios").document(e.substring(0, e.indexOf("@")));
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(LoginActivity.this, ChamadasActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
        } catch (ApiException e) {
            Log.i("Erro no login", "ApiException encontrada");
        }
    }
}
