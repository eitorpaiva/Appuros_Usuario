package com.eitor.tcc.appuros;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GravidadeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    String servico;
    FirebaseFirestore db;
    String email;
    ImageView img;
    TextView exemplos;
    View linha;
    View linha2;
    String title;
    SpannableString s;
    private TextView mTextMessage;
    private Button chamar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_leve:
                    mTextMessage.setText("Ocorrência Leve");

                    if (servico.equals("samu")) {
                        img.setImageResource(R.drawable.ic_samu_leve);
                        exemplos.setText("Exemplos: Mal-estar, casos de um único paciente.");
                    }

                    if (servico.equals("bomb")) {
                        img.setImageResource(R.drawable.ic_bomb_leve);
                        exemplos.setText("Exemplos: Incêndios domésticos.");
                    }

                    if (servico.equals("pm")) {
                        img.setImageResource(R.drawable.ic_pm_leve);
                        exemplos.setText("Exemplos: Brigas, pequenos furtos.");
                    }


                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().setElevation(0);

                    title = "Appuros";
                    s = new SpannableString(title);
                    s.setSpan(new ForegroundColorSpan(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    getSupportActionBar().setTitle(s);

                    linha = findViewById(R.id.linha_gravidade);

                    linha.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));


                    linha2 = findViewById(R.id.linha_gravidade2);
                    linha2.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

                    chamar.setBackgroundResource(GravidadeActivity.this.getIntent().getIntExtra("btn", 0));

                    chamar.setOnClickListener(view -> {
                        Intent i = new Intent(GravidadeActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", GravidadeActivity.this.getIntent().getStringExtra("cor"));
                        i.putExtra("btn", GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
                        i.putExtra("img", GravidadeActivity.this.getIntent().getIntExtra("img", 0));
                        enviarServico(servico);
                        fetchLastLocation();
                        Map<String, Object> map = new HashMap<>();
                        map.put("gravidade", 1);
                        db.collection("usuarios")
                                .document(email.substring(0, email.indexOf("@")))
                                .update(map)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(i);
                                    GravidadeActivity.this.finish();
                                });
                    });
                    return true;
                case R.id.navigation_mediana:
                    mTextMessage.setText("Ocorrência Moderada");

                    if (servico.equals("samu")) {
                        img.setImageResource(R.drawable.ic_samu_mediana);
                        exemplos.setText("Exemplos: Acidente de trânsito simples, ocorrências sem grandes ferimentos.");
                    }

                    if (servico.equals("bomb")) {
                        img.setImageResource(R.drawable.ic_bomb_mediana);
                        exemplos.setText("Exemplos: Incêndios industriais ou comerciais.");
                    }

                    if (servico.equals("pm")) {
                        img.setImageResource(R.drawable.ic_pm_mediana);
                        exemplos.setText("Exemplos: Briga generalizada, assalto de médio porte.");
                    }


                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().setElevation(0);

                    title = "Appuros";
                    s = new SpannableString(title);
                    s.setSpan(new ForegroundColorSpan(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    getSupportActionBar().setTitle(s);

                    linha = findViewById(R.id.linha_gravidade);

                    linha.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));


                    linha2 = findViewById(R.id.linha_gravidade2);
                    linha2.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

                    chamar.setBackgroundResource(GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
                    chamar.setOnClickListener(view -> {
                        Intent i = new Intent(GravidadeActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", GravidadeActivity.this.getIntent().getStringExtra("cor"));
                        i.putExtra("btn", GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
                        i.putExtra("img", GravidadeActivity.this.getIntent().getIntExtra("img", 0));
                        enviarServico(servico);
                        fetchLastLocation();
                        Map<String, Object> map = new HashMap<>();
                        map.put("gravidade", 2);
                        db.collection("usuarios")
                                .document(email.substring(0, email.indexOf("@")))
                                .update(map)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(i);
                                    GravidadeActivity.this.finish();
                                });
                    });
                    return true;
                case R.id.navigation_grave:
                    mTextMessage.setText("Ocorrência Grave");

                    if (servico.equals("samu")) {
                        img.setImageResource(R.drawable.ic_samu_grave);
                        exemplos.setText("Exemplos: Grandes acidentes envolvendo várias pessoas, ocorrências com ferimentos graves.");
                    }

                    if (servico.equals("bomb")) {
                        img.setImageResource(R.drawable.ic_bomb_grave);
                        exemplos.setText("Exemplos: Incêndios ambiental.");
                    }

                    if (servico.equals("pm")) {
                        img.setImageResource(R.drawable.ic_pm_grave);
                        exemplos.setText("Exemplos: Sequestro, assalto com refém, troca de tiros.");

                    }


                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getSupportActionBar().setElevation(0);

                    title = "Appuros";
                    s = new SpannableString(title);
                    s.setSpan(new ForegroundColorSpan(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    getSupportActionBar().setTitle(s);

                    linha = findViewById(R.id.linha_gravidade);
                    linha.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

                    linha2 = findViewById(R.id.linha_gravidade2);
                    linha2.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

                    chamar.setBackgroundResource(GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
                    chamar.setOnClickListener(view -> {
                        Intent i = new Intent(GravidadeActivity.this, EsperandoActivity.class);
                        i.putExtra("cor", GravidadeActivity.this.getIntent().getStringExtra("cor"));
                        i.putExtra("btn", GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
                        i.putExtra("img", GravidadeActivity.this.getIntent().getIntExtra("img", 0));
                        enviarServico(servico);
                        fetchLastLocation();
                        Map<String, Object> map = new HashMap<>();
                        map.put("gravidade", 3);
                        db.collection("usuarios")
                                .document(email.substring(0, email.indexOf("@")))
                                .update(map)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(i);
                                    GravidadeActivity.this.finish();
                                });
                    });
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravidade);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        img = findViewById(R.id.img_gravidade);
        exemplos = findViewById(R.id.exemplos);
        mTextMessage = findViewById(R.id.titulo);
        chamar = findViewById(R.id.chamar);
        servico = getIntent().getStringExtra("servico");
        db = FirebaseFirestore.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        email = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)).getEmail();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextMessage.setTextColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));


        if (servico.equals("samu")) {
            navView.setItemIconTintList(null);
            navView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_samu_leve));
            navView.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_samu_mediana));
            navView.getMenu().getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_samu_grave));
        }

        if (servico.equals("bomb")) {
            navView.setItemIconTintList(null);
            navView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_bomb_leve));
            navView.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_bomb_mediana));
            navView.getMenu().getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_bomb_grave));
        }

        if (servico.equals("pm")) {
            navView.setItemIconTintList(null);
            navView.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pm_leve));
            navView.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pm_mediana));
            navView.getMenu().getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pm_grave));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextMessage.setTextColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));
        exemplos.setTextColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

        if (servico.equals("samu")) {
            img.setImageResource(R.drawable.ic_samu_leve);
            exemplos.setText("Exemplos: Mal-estar, casos de um único paciente.");
        }

        if (servico.equals("bomb")) {
            img.setImageResource(R.drawable.ic_bomb_leve);
            exemplos.setText("Exemplos: Incêndios domésticos.");
        }

        if (servico.equals("pm")) {
            img.setImageResource(R.drawable.ic_pm_leve);
            exemplos.setText("Exemplos: Brigas, pequenos furtos.");
        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        title = "Appuros";
        s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        linha = findViewById(R.id.linha_gravidade);
        linha.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

        linha2 = findViewById(R.id.linha_gravidade2);
        linha2.setBackgroundColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

        chamar.setBackgroundResource(GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
        chamar.setOnClickListener(view -> {
            Intent i = new Intent(GravidadeActivity.this, EsperandoActivity.class);
            i.putExtra("cor", GravidadeActivity.this.getIntent().getStringExtra("cor"));
            i.putExtra("btn", GravidadeActivity.this.getIntent().getIntExtra("btn", 0));
            i.putExtra("img", GravidadeActivity.this.getIntent().getIntExtra("img", 0));
            enviarServico(servico);
            fetchLastLocation();
            Map<String, Object> map = new HashMap<>();
            map.put("gravidade", 1);
            db.collection("usuarios")
                    .document(email.substring(0, email.indexOf("@")))
                    .update(map)
                    .addOnSuccessListener(aVoid -> {
                        startActivity(i);
                        GravidadeActivity.this.finish();
                    });
        });
    }

    void enviarServico(String servico) {
        String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference df = db
                .collection("usuarios")
                .document(email.substring(0, email.indexOf("@")));
        Log.e("usuario", email.substring(0, email.indexOf("@")));
        df.update("servico", servico).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("task", "bem-sucedida");
            } else {
                task.getException().printStackTrace();
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
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                String email = GoogleSignIn.getLastSignedInAccount(GravidadeActivity.this).getEmail();
                final DocumentReference docRef = db
                        .collection("usuarios")
                        .document(email.substring(0, email.indexOf("@")));
                Map<String, Object> map = new HashMap<>();
                map.put("gps", location.getLatitude() + "," + location.getLongitude());
                Log.e("gps", map.get("gps").toString());
                docRef.update(map);
            }
            Log.e("localizacao", String.valueOf(location));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // kkkk
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
