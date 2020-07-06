package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.views.NovoCadastroActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CadastroEnfermeiro1Activity extends AppCompatActivity {
    private EditText etEmail, etSenha, etSenha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte1);

        etEmail = findViewById(R.id.etEmailEnfermeiro);
        etSenha = findViewById(R.id.etSenhaEnfermeiro);
        etSenha2 = findViewById(R.id.etSenha2Enfermeiro);

        findViewById(R.id.ivVoltarEnfermeiro1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroEnfermeiro1Activity.this, NovoCadastroActivity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
                    etEmail.setError("Preencha o e-mail corretamente");
                    etEmail.requestFocus();
                } else if (etSenha.getText().toString().trim().length() < 6) {
                    etSenha.setError("Preencha a senha com no minimo 6 caracteres");
                    etSenha.requestFocus();
                } else if (etSenha2.getText().toString().trim().length() < 6) {
                    etSenha2.setError("Repita a senha com no minimo 6 caracteres");
                    etSenha2.requestFocus();
                } else if (!etSenha2.getText().toString().equals(etSenha.getText().toString())) {
                    etSenha2.setError("As senhas não coincidem");
                    etSenha2.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroEnfermeiro1Activity.this, CadastroEnfermeiro2Activity.class);
                    intent.putExtra("email", etEmail.getText().toString().trim());
                    intent.putExtra("senha", etSenha.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
