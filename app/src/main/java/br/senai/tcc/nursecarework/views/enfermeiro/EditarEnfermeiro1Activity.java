package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.helpers.Validacao;
import br.senai.tcc.nursecarework.models.Enfermeiro;

public class EditarEnfermeiro1Activity extends AppCompatActivity {
    private EditText etNome, etSobrenome, etCpf, etCelular;
    private ServicosFirebase servicosFirebase;
    private Enfermeiro enfermeiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte2);

        servicosFirebase = new ServicosFirebase(this);

        Intent intent = getIntent();
        enfermeiro = (intent.hasExtra("Enfermeiro")) ?
                (Enfermeiro) intent.getSerializableExtra("Enfermeiro") :
                Usuario.getInstance().getEnfermeiro();

        etNome = findViewById(R.id.etNomeEnfermeiro);
        etSobrenome = findViewById(R.id.etSobrenomeEnfermeiro);
        etCpf = findViewById(R.id.etCpfEnfermeiro);
        etCelular = findViewById(R.id.etCelularEnfermeiro);

        etCpf.addTextChangedListener(new SimpleMaskTextWatcher(etCpf, new SimpleMaskFormatter("NNN.NNN.NNN-NN")));
        etCelular.addTextChangedListener(new SimpleMaskTextWatcher(etCelular, new SimpleMaskFormatter("(NN) NNNNN-NNNN")));

        etNome.setText(enfermeiro.getNome());
        etSobrenome.setText(enfermeiro.getSobrenome());
        etCpf.setText(enfermeiro.getCpf());
        etCelular.setText(enfermeiro.getCelular());

        findViewById(R.id.ivVoltarEnfermeiro2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarEnfermeiro1Activity.this, EnfermeiroLogadoActivity.class));
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
                    enfermeiro.setNome(etNome.getText().toString().trim());
                    enfermeiro.setSobrenome(etSobrenome.getText().toString().trim());
                    enfermeiro.setCelular(etCelular.getText().toString());

                    if (!enfermeiro.getCpf().equals(etCpf.getText().toString())) {
                        enfermeiro.setCpf(etCpf.getText().toString());
                        servicosFirebase.obterId(enfermeiro, new ServicosFirebase.ResultadoListener<String>() {
                            @Override
                            public void onSucesso(String id) {
                                if (id.isEmpty()) {
                                    continuar();
                                } else {
                                    etCpf.setError("CPF já cadastrado");
                                    etCpf.requestFocus();
                                }
                            }

                            @Override
                            public void onErro(String mensagem) {
                                Toast.makeText(EditarEnfermeiro1Activity.this, mensagem, Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        continuar();
                    }
                }
            }
        });
    }

    private void continuar() {
        Intent intent = new Intent(EditarEnfermeiro1Activity.this, EditarEnfermeiro2Activity.class);
        intent.putExtra("Enfermeiro", enfermeiro);
        startActivity(intent);
        finish();
    }
}
