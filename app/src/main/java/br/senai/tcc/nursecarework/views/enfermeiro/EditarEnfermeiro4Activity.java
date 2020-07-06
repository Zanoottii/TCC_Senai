package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Enfermeiro;

public class EditarEnfermeiro4Activity extends AppCompatActivity {
    private SeekBar sbDistancia;
    private TextView tvDistancia;
    private Enfermeiro enfermeiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte5);

        enfermeiro = (Enfermeiro) getIntent().getSerializableExtra("Enfermeiro");

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

        sbDistancia.setProgress(enfermeiro.getDistancia());

        findViewById(R.id.ivVoltarEnfermeiro5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarEnfermeiro4Activity.this, EditarEnfermeiro3Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enfermeiro.setDistancia(sbDistancia.getProgress());

                Intent intent = new Intent(EditarEnfermeiro4Activity.this, EditarEnfermeiro5Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                startActivity(intent);
                finish();
            }
        });
    }
}
