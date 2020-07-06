package br.senai.tcc.nursecarework.views.cooperativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.helpers.Validacao;
import br.senai.tcc.nursecarework.models.Cooperativa;

public class EditarCooperativa1Activity extends AppCompatActivity {
    private EditText etNome, etMunicipio, etCnpj;
    private Spinner sUf;
    private ServicosFirebase servicosFirebase;
    private Cooperativa cooperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cooperativa_parte1);

        servicosFirebase = new ServicosFirebase(this);
        cooperativa = Usuario.getInstance().getCooperativa();

        etNome = findViewById(R.id.etNomeEditarCooperativa);
        etCnpj = findViewById(R.id.etCnpjEditarCooperativa);
        sUf = findViewById(R.id.sUfEditarCooperativa);
        etMunicipio = findViewById(R.id.etMunicipioEditarCooperativa);

        etCnpj.addTextChangedListener(new SimpleMaskTextWatcher(etCnpj, new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN")));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUf.setAdapter(adapter);

        etNome.setText(cooperativa.getNome());
        etCnpj.setText(cooperativa.getCnpj());
        sUf.setSelection(adapter.getPosition(cooperativa.getUf()));
        etMunicipio.setText(cooperativa.getMunicipio());

        findViewById(R.id.ivVoltarCooperativa2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarCooperativa1Activity.this, CooperativaLogadoActivity.class));
                finish();
            }
        });

        findViewById(R.id.bSalvarCooperativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNome.getText().toString().trim().length() < 2) {
                    etNome.setError("Preencha o nome");
                    etNome.requestFocus();
                } else if (etMunicipio.getText().toString().trim().length() < 2) {
                    etMunicipio.setError("Preencha o município");
                    etMunicipio.requestFocus();
                } else if (!Validacao.isCNPJ(etCnpj.getText().toString())) {
                    etCnpj.setError("Preencha o CNPJ corretamente");
                    etCnpj.requestFocus();
                } else {
                    cooperativa.setNome(etNome.getText().toString().trim());
                    cooperativa.setCnpj(etCnpj.getText().toString());
                    cooperativa.setMunicipio(etMunicipio.getText().toString().trim());
                    cooperativa.setUf(sUf.getSelectedItem().toString());

                    servicosFirebase.gravarCooperativa(cooperativa, new ServicosFirebase.ResultadoListener() {
                        @Override
                        public void onSucesso(Object objeto) {
                            Toast.makeText(EditarCooperativa1Activity.this, "Salvo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditarCooperativa1Activity.this, CooperativaLogadoActivity.class));
                            finish();
                        }

                        @Override
                        public void onErro(String mensagem) {
                            Toast.makeText(EditarCooperativa1Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }
}
