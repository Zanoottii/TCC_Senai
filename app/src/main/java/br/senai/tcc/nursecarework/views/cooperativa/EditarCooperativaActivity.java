package br.senai.tcc.nursecarework.views.cooperativa;

import android.app.ProgressDialog;
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

public class EditarCooperativaActivity extends AppCompatActivity {
    private EditText etNome, etMunicipio, etCnpj;
    private Spinner sUf;
    private ServicosFirebase servicosFirebase;
    private Usuario usuario;
    private Cooperativa cooperativa;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cooperativa);

        servicosFirebase = new ServicosFirebase(this);
        usuario = Usuario.getInstance();

        cooperativa = usuario.getCooperativa();

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

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        findViewById(R.id.ivVoltarEditarCooperativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditarCooperativaActivity.this, CooperativaLogadoActivity.class));
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
                    cooperativa.setMunicipio(etMunicipio.getText().toString().trim());
                    cooperativa.setUf(sUf.getSelectedItem().toString());

                    if (!cooperativa.getCnpj().equals(etCnpj.getText().toString())) {
                        cooperativa.setCnpj(etCnpj.getText().toString());
                        servicosFirebase.obterId(cooperativa, new ServicosFirebase.ResultadoListener<String>() {
                            @Override
                            public void onSucesso(String id) {
                                if (id.isEmpty()) {
                                    continuar();
                                } else {
                                    etCnpj.setError("CNPJ já cadastrado");
                                    etCnpj.requestFocus();
                                }
                            }

                            @Override
                            public void onErro(String mensagem) {
                                Toast.makeText(EditarCooperativaActivity.this, mensagem, Toast.LENGTH_SHORT).show();

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
        progress.show();
        servicosFirebase.gravarCooperativa(cooperativa, new ServicosFirebase.ResultadoListener() {
            @Override
            public void onSucesso(Object objeto) {
                usuario.setCooperativa(cooperativa);
                progress.dismiss();
                Toast.makeText(EditarCooperativaActivity.this, "Salvo", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditarCooperativaActivity.this, CooperativaLogadoActivity.class));
                finish();
            }

            @Override
            public void onErro(String mensagem) {
                progress.dismiss();
                Toast.makeText(EditarCooperativaActivity.this, mensagem, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
