package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro5Activity extends AppCompatActivity {
    private SeekBar sbDistancia;
    private TextView tvDistancia;
    private Enfermeiro enfermeiro;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte5);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        sbDistancia = findViewById(R.id.sbDistanciaEnfermeiro);
        tvDistancia = findViewById(R.id.tvDistanciaEnfermeiro);

        sbDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvDistancia.setText("Distância: " + i + " Km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.ivVoltarEnfermeiro5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro5Activity.this, CadastroEnfermeiro4Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                intent.putExtra("email", email);
                intent.putExtra("senha", senha);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enfermeiro.setDistancia(sbDistancia.getProgress());

                Intent intent = new Intent(CadastroEnfermeiro5Activity.this, CadastroEnfermeiro6Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                intent.putExtra("email", email);
                intent.putExtra("senha", senha);
                startActivity(intent);
                finish();
            }
        });
    }
}
