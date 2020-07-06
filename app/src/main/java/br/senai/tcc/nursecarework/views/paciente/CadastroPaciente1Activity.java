package br.senai.tcc.nursecarework.views.paciente;

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
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.views.cooperativa.CooperativaLogadoActivity;
import br.senai.tcc.nursecarework.R;

public class CadastroPaciente1Activity extends AppCompatActivity {
    private EditText etDataNascimento, etNome, etSobrenome, etCpf, etCelular;
    private ServicosFirebase servicosFirebase;
    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte1);

        servicosFirebase = new ServicosFirebase(this);

        etDataNascimento = findViewById(R.id.etDataNascimentoPaciente);
        etNome = findViewById(R.id.etNomePaciente);
        etSobrenome = findViewById(R.id.etSobrenomePaciente);
        etCpf = findViewById(R.id.etCpfPaciente);
        etCelular = findViewById(R.id.etCelularPaciente);

        etDataNascimento.addTextChangedListener(new SimpleMaskTextWatcher(etDataNascimento, new SimpleMaskFormatter("NN/NN/NNNN")));
        etCpf.addTextChangedListener(new SimpleMaskTextWatcher(etCpf, new SimpleMaskFormatter("NNN.NNN.NNN-NN")));
        etCelular.addTextChangedListener(new SimpleMaskTextWatcher(etCelular, new SimpleMaskFormatter("(NN) NNNNN-NNNN")));

        findViewById(R.id.ivVoltarPaciente1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroPaciente1Activity.this, CooperativaLogadoActivity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoPaciente1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().trim().length() < 2) {
                    etNome.setError("Preencha o nome");
                    etNome.requestFocus();
                } else if (etSobrenome.getText().toString().trim().length() < 2) {
                    etSobrenome.setError("Preencha o sobrenome");
                    etSobrenome.requestFocus();
                } else if (!Validacao.isCPF(etCpf.getText().toString())) {
                    etCpf.setError("Preencha o CPF corretamente");
                    etCpf.requestFocus();
                } else if (!Validacao.isData(etDataNascimento.getText().toString(), "dd/MM/yyyy")) {
                    etDataNascimento.setError("Preencha a data corretamente");
                    etDataNascimento.requestFocus();
                } else if (etCelular.getText().toString().length() < 15) {
                    etCelular.setError("Preencha o numero do celular");
                    etCelular.requestFocus();
                } else {
                    paciente = new Paciente();
                    paciente.setNome(etNome.getText().toString().trim());
                    paciente.setSobrenome(etSobrenome.getText().toString().trim());
                    paciente.setCpf(etCpf.getText().toString());
                    paciente.setNascimento(etDataNascimento.getText().toString());
                    paciente.setCelular(etCelular.getText().toString());

                    servicosFirebase.obterId(paciente, new ServicosFirebase.ResultadoListener<String>() {
                        @Override
                        public void onSucesso(String id) {
                            if (id.isEmpty()) {
                                Intent intent = new Intent(CadastroPaciente1Activity.this, CadastroPaciente2Activity.class);
                                intent.putExtra("Paciente", paciente);
                                startActivity(intent);
                                finish();
                            } else {
                                etCpf.setError("CPF já cadastrado");
                                etCpf.requestFocus();
                            }
                        }

                        @Override
                        public void onErro(String mensagem) {
                            Toast.makeText(CadastroPaciente1Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
