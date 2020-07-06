package br.senai.tcc.nursecarework.views;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.views.cooperativa.CadastroCooperativa1Activity;
import br.senai.tcc.nursecarework.views.enfermeiro.CadastroEnfermeiro1Activity;
import br.senai.tcc.nursecarework.R;

public class NovoCadastroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_cadastro);

        findViewById(R.id.ivVoltarCadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NovoCadastroActivity.this, MainActivity.class));
                finish();
            }
        });

        findViewById(R.id.bCadastroEnfermeiro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NovoCadastroActivity.this, CadastroEnfermeiro1Activity.class));
                finish();
            }
        });

        findViewById(R.id.bCadastroCooperativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NovoCadastroActivity.this, CadastroCooperativa1Activity.class));
                finish();
            }
        });
    }
}
