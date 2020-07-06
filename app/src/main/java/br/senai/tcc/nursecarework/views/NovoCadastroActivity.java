package br.senai.tcc.nursecarework.Views;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.Views.Cooperativa.CadastroCooperativa1Activity;
import br.senai.tcc.nursecarework.Views.Enfermeiro.CadastroEnfermeiro1Activity;
import br.senai.tcc.nursecarework.R;

public class NovoCadastroActivity extends AppCompatActivity {

    ImageView voltar;
    Button cadastroEnfermeiro, cadastroCooperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_cadastro);

        voltar = findViewById(R.id.voltar1);
        cadastroEnfermeiro = findViewById(R.id.cadastroEnfermeiro);
        cadastroCooperativa = findViewById(R.id.cadastroCooperativa);

        //Botão para voltar para a tela de login
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NovoCadastroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para cadastro do enfermeiro
        cadastroEnfermeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NovoCadastroActivity.this, CadastroEnfermeiro1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para cadastro da Cooperativas
        cadastroCooperativa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NovoCadastroActivity.this, CadastroCooperativa1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
