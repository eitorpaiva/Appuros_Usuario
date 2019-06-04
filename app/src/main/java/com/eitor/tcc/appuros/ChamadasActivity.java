package com.eitor.tcc.appuros;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChamadasActivity extends AppCompatActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    TextView frufru;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();

        final String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamadas);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    String email = GoogleSignIn.getLastSignedInAccount(ChamadasActivity.this).getEmail();
                    final DocumentReference docRef = db
                            .collection("usuarios")
                            .document(email.substring(0, email.indexOf("@")));
                    Map<String, Object> map = new HashMap<>();
                    map.put("gps", location.getLatitude() + "," + location.getLongitude());
                    Log.e("gps", map.get("gps").toString());
                    docRef.update(map);
                }
                Log.e("localizacao", String.valueOf(location));
            }
        });
    }

    public void abrirSAMU(View view) {
        Intent i = new Intent(ChamadasActivity.this,EsperandoActivity.class);
        i.putExtra("cor","#FF6F00");
        i.putExtra("btn",R.drawable.my_button_samu);
        i.putExtra("img",R.drawable.ic_samu_icon);
        enviarServico("samu");
        fetchLastLocation();
        startActivity(i);
    }

    public void abrirBomb(View view) {
        Intent i = new Intent(ChamadasActivity.this,EsperandoActivity.class);
        i.putExtra("cor","#C62828");
        i.putExtra("btn",R.drawable.my_button_bomb);
        i.putExtra("img",R.drawable.ic_bombeiros_icon);
        enviarServico("bomb");
        fetchLastLocation();
        startActivity(i);
    }

    public void abrirPM(View view) {
        Intent i = new Intent(ChamadasActivity.this, EsperandoActivity.class);
        i.putExtra("cor", "#385eaa");
        i.putExtra("btn", R.drawable.my_button_pm);
        i.putExtra("img", R.drawable.ic_pm_icon);
        enviarServico("pm");
        fetchLastLocation();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // kkkk
                }
                break;
        }
    }
}