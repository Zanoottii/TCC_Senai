package br.senai.tcc.nursecarework.Views;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.Models.Usuario;
import br.senai.tcc.nursecarework.Views.Cooperativa.CooperativaLogadoActivity;
import br.senai.tcc.nursecarework.Views.Enfermeiro.EnfermeiroLogadoActivity;
import br.senai.tcc.nursecarework.R;

public class MainActivity extends AppCompatActivity implements ServicosFirebase.ResultadoListener {

    private TextView novoCad;
    private Button btnLogar;
    private EditText editEmail, editSenha;

    private ServicosFirebase servicosFirebase;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicosFirebase = new ServicosFirebase(this);
        usuario = Usuario.getInstance();

        if (servicosFirebase.isLogado()) servicosFirebase.carregarUsuario(this);

        novoCad = findViewById(R.id.novoCadastro);
        btnLogar = findViewById(R.id.btnLogar);
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);

        novoCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NovoCadastroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches() || editEmail.getText().toString().isEmpty()) {
                    editEmail.setError("Preencha o email corretamente");
                    editEmail.requestFocus();
                } else if (editSenha.getText().toString().isEmpty() || editSenha.length() < 6) {
                    editSenha.setError("Preencha a senha corretamente com no minimo 6 caracteres");
                    editSenha.requestFocus();
                } else {
                    servicosFirebase.logar(editEmail.getText().toString(), editSenha.getText().toString(), MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onSucesso(Object objeto) {
        startActivity(new Intent(this, (usuario.getEnfermeiro() == null) ? CooperativaLogadoActivity.class : EnfermeiroLogadoActivity.class));
        finish();
    }

    @Override
    public void onErro(String mensagem) {
        switch (mensagem) {
            case "ERROR_INVALID_EMAIL":
                editEmail.setError("E-mail inválido");
                editEmail.requestFocus();
                break;
            case "ERROR_USER_NOT_FOUND":
                editEmail.setError("E-mail não cadastrado");
                editEmail.requestFocus();
                break;
            case "ERROR_WRONG_PASSWORD":
                editSenha.setError("Senha incorreta");
                editSenha.setText("");
                editSenha.requestFocus();
                break;
            default:
                Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        }
    }
}
