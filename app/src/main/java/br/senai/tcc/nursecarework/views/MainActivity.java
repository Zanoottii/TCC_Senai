package br.senai.tcc.nursecarework.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.views.cooperativa.CooperativaLogadoActivity;
import br.senai.tcc.nursecarework.views.enfermeiro.EnfermeiroLogadoActivity;
import br.senai.tcc.nursecarework.R;

public class MainActivity extends AppCompatActivity implements ServicosFirebase.ResultadoListener {
    private EditText etEmail, etSenha;
    private ProgressDialog progress;
    private ServicosFirebase servicosFirebase;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicosFirebase = new ServicosFirebase(this);
        usuario = Usuario.getInstance();

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        if (servicosFirebase.isLogado()) {
            progress.show();
            servicosFirebase.carregarUsuario(this);
        }

        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);

        findViewById(R.id.tvNovoCadastro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NovoCadastroActivity.class));
                finish();
            }
        });

        findViewById(R.id.bLogar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    etEmail.setError("Preencha o e-mail corretamente");
                    etEmail.requestFocus();
                } else if (etSenha.length() < 6) {
                    etSenha.setError("Preencha a senha corretamente");
                    etSenha.requestFocus();
                } else {
                    progress.show();
                    servicosFirebase.logar(etEmail.getText().toString(), etSenha.getText().toString(), MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onSucesso(Object objeto) {
        progress.dismiss();
        startActivity(new Intent(this, (usuario.getEnfermeiro() == null) ? CooperativaLogadoActivity.class : EnfermeiroLogadoActivity.class));
        finish();
    }

    @Override
    public void onErro(String mensagem) {
        progress.dismiss();
        switch (mensagem) {
            case "ERROR_INVALID_EMAIL":
                etEmail.setError("E-mail inválido");
                etEmail.requestFocus();
                break;
            case "ERROR_USER_NOT_FOUND":
                etEmail.setError("E-mail não cadastrado");
                etEmail.requestFocus();
                break;
            case "ERROR_WRONG_PASSWORD":
                etSenha.setError("Senha incorreta");
                etSenha.setText("");
                etSenha.requestFocus();
                break;
            default:
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        }
    }
}
