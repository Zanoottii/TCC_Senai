package br.senai.tcc.nursecarework.views.cooperativa;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.models.Cooperativa;
import br.senai.tcc.nursecarework.views.NovoCadastroActivity;
import br.senai.tcc.nursecarework.R;

public class CadastroCooperativa1Activity extends AppCompatActivity {
    private EditText etNome, etSenha, etSenha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cooperativa_parte1);

        etNome = findViewById(R.id.etNomeCooperativa);
        etSenha = findViewById(R.id.etSenhaCooperativa);
        etSenha2 = findViewById(R.id.etSenha2Cooperativa);

        findViewById(R.id.ivVoltarCooperativa1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroCooperativa1Activity.this, NovoCadastroActivity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoCooperativa1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().trim().length() < 2) {
                    etNome.setError("Preencha o nome");
                    etNome.requestFocus();
                } else if (etSenha.getText().toString().length() < 6) {
                    etSenha.setError("Preencha a senha com no minimo 6 caracteres");
                    etSenha.requestFocus();
                } else if (etSenha2.getText().toString().length() < 6) {
                    etSenha2.setError("Preencha a confirmação de senha");
                    etSenha2.requestFocus();
                } else if (!etSenha2.getText().toString().equals(etSenha.getText().toString())) {
                    etSenha2.setError("As senhas não coincidem");
                    etSenha2.requestFocus();
                } else {
                    Cooperativa cooperativa = new Cooperativa();
                    cooperativa.setNome(etNome.getText().toString().trim());

                    Intent intent = new Intent(CadastroCooperativa1Activity.this, CadastroCooperativa2Activity.class);
                    intent.putExtra("Cooperativa", cooperativa);
                    intent.putExtra("senha", etSenha.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
