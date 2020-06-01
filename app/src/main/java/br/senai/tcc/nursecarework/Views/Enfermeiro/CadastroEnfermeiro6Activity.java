package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.Models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro6Activity extends AppCompatActivity {

    private SeekBar distancia;
    private TextView txtKm;
    private ImageView voltar;
    private Button proximo;
    private Enfermeiro enfermeiro;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte6);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        distancia = findViewById(R.id.distancia);
        txtKm = findViewById(R.id.txtKm);
        voltar = findViewById(R.id.voltar);
        proximo = findViewById(R.id.proximo);

        distancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtKm.setText("Distancia: " + i + "Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiro5Activity.class);
                startActivity(intent);
                finish();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enfermeiro.setDistancia(String.valueOf(distancia.getProgress()));

                Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiro3Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                intent.putExtra("email", email);
                intent.putExtra("senha", senha);
                startActivity(intent);
                finish();
            }
        });

    }
}
