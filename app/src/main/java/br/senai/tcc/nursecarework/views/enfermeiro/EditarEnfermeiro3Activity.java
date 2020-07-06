package br.senai.tcc.nursecarework.views.enfermeiro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import br.senai.tcc.nursecarework.models.Enfermeiro;

public class EditarEnfermeiro3Activity extends AppCompatActivity {
    private EditText etCep, etBairro, etRua, etNumero, etCidade;
    private Spinner sUf;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progress;
    private Enfermeiro enfermeiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte4);

        enfermeiro = (Enfermeiro) getIntent().getSerializableExtra("Enfermeiro");

        etCep = findViewById(R.id.etCepEnfermeiro);
        etBairro = findViewById(R.id.etBairroEnfermeiro);
        etRua = findViewById(R.id.etRuaEnfermeiro);
        etNumero = findViewById(R.id.etNumeroEnfermeiro);
        etCidade = findViewById(R.id.etMunicipioEnfermeiro);
        sUf = findViewById(R.id.sUfEnfermeiro);

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

        findViewById(R.id.ivVoltarEnfermeiro4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarEnfermeiro3Activity.this, EditarEnfermeiro2Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bProximoEnfermeiro4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCep.getText().toString().length() < 9) {
                    etCep.setError("Preencha o CEP");
                    etCep.requestFocus();
                } else if (etRua.getText().toString().trim().length() < 4) {
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
                        enfermeiro.setLatitude(address.getLatitude());
                        enfermeiro.setLongitude(address.getLongitude());

                        Intent intent = new Intent(EditarEnfermeiro3Activity.this, EditarEnfermeiro4Activity.class);
                        intent.putExtra("Enfermeiro", enfermeiro);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditarEnfermeiro3Activity.this, "Endereço inválido", Toast.LENGTH_SHORT).show();
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
