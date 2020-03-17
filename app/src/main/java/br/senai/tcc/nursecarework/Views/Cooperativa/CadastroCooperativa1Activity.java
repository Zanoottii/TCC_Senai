package br.senai.tcc.nursecarework.Views.Cooperativa;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.Views.NovoCadastroActivity;
import br.senai.tcc.nursecarework.R;

public class CadastroCooperativa1Activity extends AppCompatActivity {

    private ImageView voltar;
    private EditText nome, senha, confirmSenha;
    private Button proximaEtapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cooperativa1);

        voltar = findViewById(R.id.voltar);
        proximaEtapa = findViewById(R.id.proximaEtapa);
        nome = findViewById(R.id.nomeCooperativa);
        senha = findViewById(R.id.senhaCooperativa);
        confirmSenha = findViewById(R.id.confirmSenha);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroCooperativa1Activity.this, NovoCadastroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        proximaEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nome.getText().toString().isEmpty()) {
                    nome.setError("Preencha o nome");
                    nome.requestFocus();
                } else if (senha.getText().toString().isEmpty() || senha.length() < 6) {
                    senha.setError("Preencha a senha com no minimo 6 caracteres");
                    senha.requestFocus();
                } else if (confirmSenha.getText().toString().isEmpty()) {
                    confirmSenha.setError("Preencha a confirmação de senha");
                    confirmSenha.requestFocus();
                } else if (!confirmSenha.getText().toString().equals(senha.getText().toString())) {
                    confirmSenha.setError("As senhas não coincidem");
                    confirmSenha.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroCooperativa1Activity.this, CadastroCooperativa2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
