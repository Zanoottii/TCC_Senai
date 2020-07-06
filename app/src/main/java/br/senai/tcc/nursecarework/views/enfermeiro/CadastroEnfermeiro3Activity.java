package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.helpers.Mascara;
import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro3Activity extends AppCompatActivity {
    private TextWatcher twAgenciaItau, twAgenciaBradesco, twAgenciaSantander, twAgenciaBancoDoBrasil, twAgenciaCaixaEconomica,
                        twContaItau, twContaBradesco, twContaSantander, twContaBancoDoBrasil, twContaCaixaEconomica;
    private Spinner sBanco;
    private EditText etAgencia, etConta;
    private Enfermeiro enfermeiro;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte3);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        sBanco = findViewById(R.id.sBancoEnfermeiro);
        etAgencia = findViewById(R.id.etAgenciaEnfermeiro);
        etConta = findViewById(R.id.etContaEnfermeiro);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_banco, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBanco.setAdapter(adapter);

        sBanco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etAgencia.setText("");
                etConta.setText("");
                removeMascara(position);
                insereMascara(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.ivVoltarEnfermeiro3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro3Activity.this, CadastroEnfermeiro2Activity.class);
                intent.putExtra("email", email);
                intent.putExtra("senha", senha);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAgencia.getText().toString().isEmpty()) {
                    etAgencia.setError("Preencha o número da agencia");
                    etAgencia.requestFocus();
                } else if (etConta.getText().toString().isEmpty()) {
                    etConta.setError("Preencha o número da conta");
                    etConta.requestFocus();
                } else {
                    enfermeiro.setBanco(sBanco.getSelectedItem().toString());
                    enfermeiro.setAgencia(etAgencia.getText().toString());
                    enfermeiro.setConta(etConta.getText().toString());

                    Intent intent = new Intent(CadastroEnfermeiro3Activity.this, CadastroEnfermeiro4Activity.class);
                    intent.putExtra("Enfermeiro", enfermeiro);
                    intent.putExtra("email", email);
                    intent.putExtra("senha", senha);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void insereMascara(int position) {
        if (position == 0) {
            twAgenciaItau = Mascara.insert("####", etAgencia);
            twContaItau = Mascara.insert("#####-#", etConta);
            etAgencia.addTextChangedListener(twAgenciaItau);
            etConta.addTextChangedListener(twContaItau);
        } else if (position == 1) {
            twAgenciaBradesco = Mascara.insert("####-#", etAgencia);
            twContaBradesco = Mascara.insert("#######-#", etConta);
            etAgencia.addTextChangedListener(twAgenciaBradesco);
            etConta.addTextChangedListener(twContaBradesco);
        } else if (position == 2) {
            twAgenciaSantander = Mascara.insert("####", etAgencia);
            twContaSantander = Mascara.insert("########-#", etConta);
            etAgencia.addTextChangedListener(twAgenciaSantander);
            etConta.addTextChangedListener(twContaSantander);
        } else if (position == 3) {
            twAgenciaBancoDoBrasil = Mascara.insert("####-#", etAgencia);
            twContaBancoDoBrasil = Mascara.insert("########-#", etConta);
            etAgencia.addTextChangedListener(twAgenciaBancoDoBrasil);
            etConta.addTextChangedListener(twContaBancoDoBrasil);
        } else if (position == 4) {
            twAgenciaCaixaEconomica = Mascara.insert("####", etAgencia);
            twContaCaixaEconomica = Mascara.insert("###########-#", etConta);
            etAgencia.addTextChangedListener(twAgenciaCaixaEconomica);
            etConta.addTextChangedListener(twContaCaixaEconomica);
        }
    }

    private void removeMascara(int position) {
        if (position != 0) {
            etAgencia.removeTextChangedListener(twAgenciaItau);
            etConta.removeTextChangedListener(twContaItau);
        }
        if (position != 1) {
            etAgencia.removeTextChangedListener(twAgenciaBradesco);
            etConta.removeTextChangedListener(twContaBradesco);
        }
        if (position != 2) {
            etAgencia.removeTextChangedListener(twAgenciaSantander);
            etConta.removeTextChangedListener(twContaSantander);
        }
        if (position != 3) {
            etAgencia.removeTextChangedListener(twAgenciaBancoDoBrasil);
            etConta.removeTextChangedListener(twContaBancoDoBrasil);
        }
        if (position != 4) {
            etAgencia.removeTextChangedListener(twAgenciaCaixaEconomica);
            etConta.removeTextChangedListener(twContaCaixaEconomica);
        }
    }
}
