package br.senai.tcc.nursecarework;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TextView novoCad;
    private Button btnLogar;
    private EditText editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Intent intent = new Intent(MainActivity.this, CooperativaLogadoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
