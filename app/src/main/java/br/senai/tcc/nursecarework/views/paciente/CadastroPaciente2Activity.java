package br.senai.tcc.nursecarework.views.paciente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Paciente;

public class CadastroPaciente2Activity extends AppCompatActivity {
    private EditText etCep, etBairro, etRua, etNumero, etComlemento, etCidade;
    private Spinner sUf;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progress;
    private Paciente paciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte2);

        paciente = (Paciente) getIntent().getSerializableExtra("Paciente");

        etCep = findViewById(R.id.etCepPaciente);
        etBairro = findViewById(R.id.etBairroPaciente);
        etRua = findViewById(R.id.etRuaPaciente);
        etNumero = findViewById(R.id.etNumeroPaciente);
        etComlemento = findViewById(R.id.etComplementoPaciente);
        etCidade = findViewById(R.id.etMunicipioPaciente);
        sUf = findViewById(R.id.sUfPaciente);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUf.setAdapter(adapter);

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        etCep.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                EditText campo = (EditText) view;
                if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    String texto = campo.getText().toString().replaceAll("[^0-9]", "");
                    campo.setText("");
                    campo.append(texto);
                    if (texto.length() == 8) {
                        progress.show();
                        campo.setEnabled(false);
                        buscaCep(campo.getText().toString());
                    }
                }
                return false;
            }
        });

        findViewById(R.id.ivVoltarPaciente2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroPaciente2Activity.this, CadastroPaciente1Activity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoPaciente2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCep.getText().toString().length() < 9) {
                    etCep.setError("Preencha o CEP");
                    etCep.requestFocus();
                } else if (etRua.getText().toString().trim().length() < 6) {
                    etRua.setError("Preencha a rua");
                    etRua.requestFocus();
                } else if (etBairro.getText().toString().trim().length() < 3) {
                    etBairro.setError("Preencha o bairro");
                    etBairro.requestFocus();
                } else if (etNumero.getText().toString().isEmpty()) {
                    etNumero.setError("Preencha o número");
                    etNumero.requestFocus();
                } else if (etCidade.getText().toString().trim().length() < 3) {
                    etCidade.setError("Preencha o município");
                    etCidade.requestFocus();
                } else {
                    String endereco = etRua.getText().toString() + ", " +
                            etNumero.getText().toString() + " - " +
                            etBairro.getText().toString() + " - " +
                            etCidade.getText().toString() + " - " +
                            sUf.getSelectedItem().toString();
                    progress.show();
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(endereco, 1);
                    } catch (IOException e) {
                    }
                    progress.dismiss();
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        paciente.setCep(etCep.getText().toString());
                        paciente.setLogradouro(etRua.getText().toString());
                        paciente.setBairro(etBairro.getText().toString());
                        paciente.setNumero(etNumero.getText().toString());
                        paciente.setComplemento(etComlemento.getText().toString());
                        paciente.setMunicipio(etCidade.getText().toString());
                        paciente.setUf(sUf.getSelectedItem().toString());
                        paciente.setLatitude(address.getLatitude());
                        paciente.setLongitude(address.getLongitude());

                        Intent intent = new Intent(CadastroPaciente2Activity.this, CadastroPaciente3Activity.class);
                        intent.putExtra("Paciente", paciente);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CadastroPaciente2Activity.this, "Endereço inválido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void buscaCep(String cepUsuario) {
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://viacep.com.br/ws/" + cepUsuario + "/json/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            etCep.clearFocus();
                            etCep.setText(response.getString("cep"));
                            etRua.setText(response.getString("logradouro"));
                            etBairro.setText(response.getString("bairro"));
                            etCidade.setText(response.getString("localidade"));
                            sUf.setSelection(adapter.getPosition(response.getString("uf")));
                            etNumero.requestFocus();
                        } catch (JSONException e) {
                            etCep.setText("");
                            etRua.setText("");
                            etNumero.setText("");
                            etComlemento.setText("");
                            etBairro.setText("");
                            etCidade.setText("");
                            sUf.setSelection(0);
                        }
                        etCep.setEnabled(true);
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                    }
                }
        );
        rq.add(request);
    }
}
