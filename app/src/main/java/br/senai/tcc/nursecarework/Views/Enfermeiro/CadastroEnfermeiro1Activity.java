package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import  br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.Views.NovoCadastroActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class CadastroEnfermeiro1Activity extends AppCompatActivity {

    private Button proximo;
    private EditText email, senha, conSenha;
    private ImageView voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte1);

        proximo = findViewById(R.id.btnProximo1);
        email = findViewById(R.id.emailEnfermeiro);
        senha = findViewById(R.id.senhaEnfermeiro);
        conSenha = findViewById(R.id.conSenhaEnfer);
        voltar = findViewById(R.id.voltar);

        //Botão para ir para a próxima etapa
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() || email.getText().toString().isEmpty()) {
                    email.setError("Preencha o email corretamente");
                    email.requestFocus();
                } else if (senha.getText().toString().isEmpty() || senha.length() < 6) {
                    senha.setError("Preencha a senha Com no minimo 6 caracteres");
                    senha.requestFocus();
                } else if (conSenha.getText().toString().isEmpty()) {
                    conSenha.setError("Preencha o campo");
                    conSenha.requestFocus();
                } else if (!conSenha.getText().toString().equals(senha.getText().toString())) {
                    conSenha.setError("As senhas não coincidem");
                    conSenha.requestFocus();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiro2Activity.class);
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("senha", senha.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

        //Botão para voltar para a tela de novo cadastro
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NovoCadastroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
