package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.Helper.Validacao;
import br.senai.tcc.nursecarework.Models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro2Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText edtCpf, edtCelular, edtNome, edtSobrenome;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte2);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        voltar = findViewById(R.id.voltar3);
        proximo = findViewById(R.id.btnProximo);
        edtCpf = findViewById(R.id.cpfEnfermeiro);
        edtCelular = findViewById(R.id.foneEnfermeiro);
        edtNome = findViewById(R.id.nomeEnfermeiro);
        edtSobrenome = findViewById(R.id.sobrenomeEnfer);

        //mascara para o campo cpf
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        SimpleMaskTextWatcher maskcpf = new SimpleMaskTextWatcher(edtCpf, simpleMaskCpf);
        edtCpf.addTextChangedListener(maskcpf);

        //mascara para o campo telefone
        SimpleMaskFormatter simpleMaskFone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskTextWatcher maskfone = new SimpleMaskTextWatcher(edtCelular, simpleMaskFone);
        edtCelular.addTextChangedListener(maskfone);

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
                if (edtNome.getText().toString().isEmpty()) {
                    edtNome.setError("Preencha o nome");
                    edtNome.requestFocus();
                } else if (edtSobrenome.getText().toString().isEmpty()) {
                    edtSobrenome.setError("Preencha o sobrenome");
                    edtSobrenome.requestFocus();
                } else if (edtCpf.getText().toString().length() == 0 || !Validacao.isCPF(edtCpf.getText().toString()) || edtCpf.getText().equals("1")) {
                    edtCpf.setError("Preencha o CPF corretamente");
                    edtCpf.requestFocus();
                } else if (edtCelular.getText().toString().isEmpty()) {
                    edtCelular.setError("Preencha o numero do celular");
                    edtCelular.requestFocus();
                } else {
                    Enfermeiro enfermeiro = new Enfermeiro();
                    enfermeiro.setNome(edtNome.getText().toString());
                    enfermeiro.setSobrenome(edtSobrenome.getText().toString());
                    enfermeiro.setCelular(edtCelular.getText().toString());
                    enfermeiro.setCpf(edtCpf.getText().toString());

                    Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro5Activity.class);
                    intent.putExtra("Enfermeiro", enfermeiro);
                    intent.putExtra("email", email);
                    intent.putExtra("senha", senha);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}