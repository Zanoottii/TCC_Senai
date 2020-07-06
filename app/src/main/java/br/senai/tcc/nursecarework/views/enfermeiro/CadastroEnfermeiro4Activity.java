package br.senai.tcc.nursecarework.views.enfermeiro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
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

import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro5Activity extends AppCompatActivity {

    private ImageView voltar;
    private EditText edtCep, edtBairro, edtRua, edtNumCasa, edtCidade;
    private Button proximo;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progress;
    private Spinner spinnerUf;
    private Enfermeiro enfermeiro;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte5);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        voltar = findViewById(R.id.volta);
        edtCep = findViewById(R.id.cep);
        edtBairro = findViewById(R.id.bairro);
        edtRua = findViewById(R.id.rua);
        edtNumCasa = findViewById(R.id.numCasa);
        edtCidade = findViewById(R.id.cidade);
        spinnerUf = findViewById(R.id.uf);
        proximo = findViewById(R.id.proximo);

        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUf.setAdapter(adapter);
        spinnerUf.setSelection(0, true);

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro5Activity.this, CadastroEnfermeiro2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        edtCep.setOnKeyListener(new View.OnKeyListener() {
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

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtCep.getText().toString().isEmpty()) {
                    edtCep.setError("Preencha o CEP");
                    edtCep.requestFocus();
                } else if (edtNumCasa.getText().toString().isEmpty()) {
                    edtNumCasa.setError("Preencha o numero da casa");
                    edtNumCasa.requestFocus();
                } else {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    progress.show();
                    try {
                        String endereco = edtRua.getText().toString() + ", " +
                                edtNumCasa.getText().toString() + " - " +
                                edtBairro.getText().toString() + " - " +
                                edtCidade.getText().toString() + " - " +
                                spinnerUf.getSelectedItem().toString();
                        addresses = geocoder.getFromLocationName(endereco, 1);
                    } catch (IOException e) {
                    }
                    progress.dismiss();
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        enfermeiro.setLatitude(address.getLatitude());
                        enfermeiro.setLongitude(address.getLongitude());

                        Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiro6Activity.class);
                        intent.putExtra("Enfermeiro", enfermeiro);
                        intent.putExtra("email", email);
                        intent.putExtra("senha", senha);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CadastroEnfermeiro5Activity.this, "Endereço inválido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void buscaCep(String cepUsuario) {
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://viacep.com.br/ws/" + cepUsuario + "/json/", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            edtCep.clearFocus();
                            edtCep.setText(response.getString("cep"));
                            edtRua.setText(response.getString("logradouro"));
                            edtBairro.setText(response.getString("bairro"));
                            edtCidade.setText(response.getString("localidade"));
                            spinnerUf.setSelection(adapter.getPosition(response.getString("uf")));
                            edtNumCasa.requestFocus();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            edtCep.setText("");
                            edtRua.setText("");
                            edtNumCasa.setText("");
                            edtBairro.setText("");
                            edtCidade.setText("");
                            spinnerUf.setSelection(adapter.getPosition("AC"));
                        }
                        edtCep.setEnabled(true);
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOG", "Erro");
                        progress.dismiss();
                    }
                }
        );
        rq.add(request);
    }
}
