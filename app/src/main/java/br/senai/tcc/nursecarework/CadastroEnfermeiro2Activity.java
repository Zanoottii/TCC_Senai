package br.senai.tcc.nursecarework;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

public class CadastroEnfermeiro2Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText cpf, telefone, nome, sobrenome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte2);

        voltar = findViewById(R.id.voltar3);
        proximo = findViewById(R.id.btnProximo);
        cpf = findViewById(R.id.cpfEnfermeiro);
        telefone = findViewById(R.id.foneEnfermeiro);
        nome = findViewById(R.id.nomeEnfermeiro);
        sobrenome = findViewById(R.id.sobrenomeEnfer);

        //mascara para o campo cpf
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        SimpleMaskTextWatcher maskcpf = new SimpleMaskTextWatcher(cpf, simpleMaskCpf);
        cpf.addTextChangedListener(maskcpf);

        //mascara para o campo telefone
        SimpleMaskFormatter simpleMaskFone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskTextWatcher maskfone = new SimpleMaskTextWatcher(telefone, simpleMaskFone);
        telefone.addTextChangedListener(maskfone);

        //Botão para voltar para a tele de cadastro parte 1
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para ir para a próxima etapa
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nome.getText().toString().isEmpty()) {
                    nome.setError("Preencha o nome");
                    nome.requestFocus();
                } else if (sobrenome.getText().toString().isEmpty()) {
                    sobrenome.setError("Preencha o sobrenome");
                    sobrenome.requestFocus();
                } else if (cpf.getText().toString().length() == 0 || !Validacao.isCPF(cpf.getText().toString()) || cpf.getText().equals("1")) {
                    cpf.setError("Preencha o CPF corretamente");
                    cpf.requestFocus();
                } else if (telefone.getText().toString().isEmpty()) {
                    telefone.setError("Preencha o numero do celular");
                    telefone.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro5Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}