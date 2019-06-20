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
    BottomNavigationView navView;
    TextView exemplos;
    View linha;
    View linha2;
    int leve, leveSelec, med, medSelec, grave, graveSelec;
    String title;
    SpannableString s;
    private TextView mTextMessage;
    private Button chamar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_leve:
                    mTextMessage.setText("Ocorrência Leve");

                    if (servico.equals("samu")) {
                        desselecionarTudo();
                        trocarParaSAMU();
                        selecionar(R.id.nav_leve, leveSelec);
                        img.setImageResource(leve);
                        exemplos.setText("Exemplos: Mal-estar, casos de um único paciente.");
                    }

                    if (servico.equals("bomb")) {
                        desselecionarTudo();
                        trocarParaBombeiros();
                        selecionar(R.id.nav_leve, leveSelec);
                        img.setImageResource(leve);
                        exemplos.setText("Exemplos: Incêndios domésticos.");
                    }

                    if (servico.equals("pm")) {
                        desselecionarTudo();
                        trocarParaPM();
                        selecionar(R.id.nav_leve, leveSelec);
                        img.setImageResource(leve);
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
                        map.put("gravidade", "Leve");
                        db.collection("usuarios")
                                .document(email.substring(0, email.indexOf("@")))
                                .update(map)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(i);
                                    GravidadeActivity.this.finish();
                                });
                    });
                    return true;
                case R.id.nav_mediana:
                    mTextMessage.setText("Ocorrência Moderada");

                    if (servico.equals("samu")) {
                        desselecionarTudo();
                        trocarParaSAMU();
                        selecionar(R.id.nav_mediana, medSelec);
                        img.setImageResource(med);
                        exemplos.setText("Exemplos: Acidente de trânsito simples, ocorrências sem grandes ferimentos.");
                    }

                    if (servico.equals("bomb")) {
                        desselecionarTudo();
                        trocarParaBombeiros();
                        selecionar(R.id.nav_mediana, medSelec);
                        img.setImageResource(med);
                        exemplos.setText("Exemplos: Incêndios industriais ou comerciais.");
                    }

                    if (servico.equals("pm")) {
                        desselecionarTudo();
                        trocarParaPM();
                        selecionar(R.id.nav_mediana, medSelec);
                        img.setImageResource(med);
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
                        map.put("gravidade", "Moderada");
                        db.collection("usuarios")
                                .document(email.substring(0, email.indexOf("@")))
                                .update(map)
                                .addOnSuccessListener(aVoid -> {
                                    startActivity(i);
                                    GravidadeActivity.this.finish();
                                });
                    });
                    return true;
                case R.id.nav_grave:
                    mTextMessage.setText("Ocorrência Grave");

                    if (servico.equals("samu")) {
                        desselecionarTudo();
                        trocarParaSAMU();
                        selecionar(R.id.nav_grave, graveSelec);
                        img.setImageResource(grave);
                        exemplos.setText("Exemplos: Grandes acidentes envolvendo várias pessoas, ocorrências com ferimentos graves.");
                    }

                    if (servico.equals("bomb")) {
                        desselecionarTudo();
                        trocarParaBombeiros();
                        selecionar(R.id.nav_grave, graveSelec);
                        img.setImageResource(grave);
                        exemplos.setText("Exemplos: Incêndios ambiental.");
                    }

                    if (servico.equals("pm")) {
                        desselecionarTudo();
                        trocarParaPM();
                        selecionar(R.id.nav_grave, graveSelec);
                        img.setImageResource(grave);
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
                        map.put("gravidade", "Grave");
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
        navView = findViewById(R.id.nav_view);


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

        navView.setItemIconTintList(null);
        selecionar(R.id.nav_leve, R.drawable.ic_samu_leve_selec);

        img = findViewById(R.id.img_gravidade);

        if (servico.equals("samu")) {
            desselecionarTudo();
            trocarParaSAMU();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
            exemplos.setText("Exemplos: Mal-estar, casos de um único paciente.");
        }

        if (servico.equals("bomb")) {
            desselecionarTudo();
            trocarParaBombeiros();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
            exemplos.setText("Exemplos: Incêndios domésticos.");
        }

        if (servico.equals("pm")) {
            desselecionarTudo();
            trocarParaPM();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
            exemplos.setText("Exemplos: Brigas, pequenos furtos.");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mTextMessage.setTextColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));
        exemplos.setTextColor(Color.parseColor(GravidadeActivity.this.getIntent().getStringExtra("cor")));

        navView.setItemIconTintList(null);
        selecionar(R.id.nav_leve, R.drawable.ic_samu_leve_selec);
        img = findViewById(R.id.img_gravidade);

        if (servico.equals("samu")) {
            desselecionarTudo();
            trocarParaSAMU();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
            exemplos.setText("Exemplos: Mal-estar, casos de um único paciente.");
        }

        if (servico.equals("bomb")) {
            desselecionarTudo();
            trocarParaBombeiros();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
            exemplos.setText("Exemplos: Incêndios domésticos.");
        }

        if (servico.equals("pm")) {
            desselecionarTudo();
            trocarParaPM();
            selecionar(R.id.nav_leve, leveSelec);
            img.setImageResource(leve);
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
            map.put("gravidade", "Leve");
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

    void desselecionarTudo() {
        navView.getMenu().findItem(R.id.nav_leve).setIcon(leve);
        navView.getMenu().findItem(R.id.nav_mediana).setIcon(med);
        navView.getMenu().findItem(R.id.nav_grave).setIcon(grave);
    }

    void selecionar(int id, int iconSelecionado) {
        navView.getMenu().findItem(id).setIcon(iconSelecionado);
    }

    void trocarParaPM() {
        leve = R.drawable.ic_pm_leve;
        leveSelec = R.drawable.ic_pm_leve_selec;
        med = R.drawable.ic_pm_mediana;
        medSelec = R.drawable.ic_pm_mediana_selec;
        grave = R.drawable.ic_pm_grave;
        graveSelec = R.drawable.ic_pm_grave_selec;

        img.setImageResource(leve);
    }

    void trocarParaSAMU() {
        leve = R.drawable.ic_samu_leve;
        leveSelec = R.drawable.ic_samu_leve_selec;
        med = R.drawable.ic_samu_mediana;
        medSelec = R.drawable.ic_samu_mediana_selec;
        grave = R.drawable.ic_samu_grave;
        graveSelec = R.drawable.ic_samu_grave_selec;

        img.setImageResource(leve);
    }

    void trocarParaBombeiros() {
        leve = R.drawable.ic_bomb_leve;
        leveSelec = R.drawable.ic_bomb_leve_selec;
        med = R.drawable.ic_bomb_mediana;
        medSelec = R.drawable.ic_bomb_mediana_selec;
        grave = R.drawable.ic_bomb_grave;
        graveSelec = R.drawable.ic_bomb_grave_selec;

        img.setImageResource(leve);
    }
}
