package br.senai.tcc.nursecarework.views.cooperativa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.models.Cooperativa;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.helpers.Validacao;

public class CadastroCooperativa2Activity extends AppCompatActivity {
    private EditText etEmail, etMunicipio, etCnpj;
    private Spinner sUf;
    private ServicosFirebase servicosFirebase;
    private Cooperativa cooperativa;
    private String senha;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cooperativa_parte2);

        servicosFirebase = new ServicosFirebase(this);

        Intent intent = getIntent();
        cooperativa = (Cooperativa) intent.getSerializableExtra("Cooperativa");
        senha = intent.getStringExtra("senha");

        etCnpj = findViewById(R.id.etCnpjCooperativa);
        etEmail = findViewById(R.id.etEmailCooperativa);
        sUf = findViewById(R.id.sUfCooperativa);
        etMunicipio = findViewById(R.id.etMunicipioCooperativa);

        etCnpj.addTextChangedListener(new SimpleMaskTextWatcher(etCnpj, new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN")));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUf.setAdapter(adapter);

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        findViewById(R.id.ivVoltarCooperativa2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroCooperativa2Activity.this, CadastroCooperativa1Activity.class));
                finish();
            }
        });

        findViewById(R.id.bCadastrarCooperativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
                    etEmail.setError("Preencha o e-mail corretamente");
                    etEmail.requestFocus();
                } else if (etMunicipio.getText().toString().trim().length() < 2) {
                    etMunicipio.setError("Preencha o município");
                    etMunicipio.requestFocus();
                } else if (!Validacao.isCNPJ(etCnpj.getText().toString())) {
                    etCnpj.setError("Preencha o CNPJ corretamente");
                    etCnpj.requestFocus();
                } else {
                    cooperativa.setCnpj(etCnpj.getText().toString());
                    cooperativa.setMunicipio(etMunicipio.getText().toString().trim());
                    cooperativa.setUf(sUf.getSelectedItem().toString());

                    servicosFirebase.obterId(cooperativa, new ServicosFirebase.ResultadoListener<String>() {
                        @Override
                        public void onSucesso(String id) {
                            if (id.isEmpty()) {
                                progress.show();
                                servicosFirebase.cadastrarCooperativa(etEmail.getText().toString().trim(), senha, cooperativa, new ServicosFirebase.ResultadoListener() {
                                    @Override
                                    public void onSucesso(Object objeto) {
                                        progress.dismiss();
                                        startActivity(new Intent(CadastroCooperativa2Activity.this, CooperativaLogadoActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        progress.dismiss();
                                        Toast.makeText(CadastroCooperativa2Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                etCnpj.setError("CNPJ já cadastrado");
                                etCnpj.requestFocus();
                            }
                        }

                        @Override
                        public void onErro(String mensagem) {

                        }
                    });
                }
            }

        });
    }
}
