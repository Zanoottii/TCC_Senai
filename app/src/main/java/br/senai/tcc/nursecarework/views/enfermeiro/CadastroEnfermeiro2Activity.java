package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Validacao;
import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro2Activity extends AppCompatActivity {
    private EditText etNome, etSobrenome, etCpf, etCelular;
    private ServicosFirebase servicosFirebase;
    private Enfermeiro enfermeiro;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte2);

        servicosFirebase = new ServicosFirebase(this);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        etNome = findViewById(R.id.etNomeEnfermeiro);
        etSobrenome = findViewById(R.id.etSobrenomeEnfermeiro);
        etCpf = findViewById(R.id.etCpfEnfermeiro);
        etCelular = findViewById(R.id.etCelularEnfermeiro);

        etCpf.addTextChangedListener(new SimpleMaskTextWatcher(etCpf, new SimpleMaskFormatter("NNN.NNN.NNN-NN")));
        etCelular.addTextChangedListener(new SimpleMaskTextWatcher(etCelular, new SimpleMaskFormatter("(NN) NNNNN-NNNN")));

        findViewById(R.id.ivVoltarEnfermeiro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro1Activity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().trim().length() < 2) {
                    etNome.setError("Preencha o nome");
                    etNome.requestFocus();
                } else if (etSobrenome.getText().toString().trim().length() < 2) {
                    etSobrenome.setError("Preencha o sobrenome");
                    etSobrenome.requestFocus();
                } else if (!Validacao.isCPF(etCpf.getText().toString())) {
                    etCpf.setError("Preencha o número do CPF corretamente");
                    etCpf.requestFocus();
                } else if (etCelular.getText().toString().trim().length() < 15) {
                    etCelular.setError("Preencha o número do celular");
                    etCelular.requestFocus();
                } else {
                    enfermeiro = new Enfermeiro();
                    enfermeiro.setNome(etNome.getText().toString().trim());
                    enfermeiro.setSobrenome(etSobrenome.getText().toString().trim());
                    enfermeiro.setCpf(etCpf.getText().toString());
                    enfermeiro.setCelular(etCelular.getText().toString());

                    servicosFirebase.obterId(enfermeiro, new ServicosFirebase.ResultadoListener<String>() {
                        @Override
                        public void onSucesso(String id) {
                            if (id.isEmpty()) {
                                Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro3Activity.class);
                                intent.putExtra("Enfermeiro", enfermeiro);
                                intent.putExtra("email", email);
                                intent.putExtra("senha", senha);
                                startActivity(intent);
                                finish();
                            } else {
                                etCpf.setError("CPF já cadastrado");
                                etCpf.requestFocus();
                            }
                        }

                        @Override
                        public void onErro(String mensagem) {
                            Toast.makeText(CadastroEnfermeiro2Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}