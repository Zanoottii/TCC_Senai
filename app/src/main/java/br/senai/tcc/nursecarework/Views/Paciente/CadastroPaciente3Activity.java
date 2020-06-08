package br.senai.tcc.nursecarework.Views.Paciente;

import android.app.ProgressDialog;
import android.content.Intent;
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

import br.senai.tcc.nursecarework.Views.Enfermeiro.CadastroEnfermeiro2Activity;
import br.senai.tcc.nursecarework.R;

public class CadastroPaciente3Activity extends AppCompatActivity {

    private ImageView voltar;
    private EditText cep, bairro, rua, numCasa, cidade;
    private Button proximo;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progress;
    private Spinner uf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte5);

        voltar = findViewById(R.id.volta);
        cep = findViewById(R.id.cep);
        bairro = findViewById(R.id.bairro);
        rua = findViewById(R.id.rua);
        numCasa = findViewById(R.id.numCasa);
        cidade = findViewById(R.id.cidade);
        uf = findViewById(R.id.uf);
        proximo = findViewById(R.id.proximo);

        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uf.setAdapter(adapter);
        uf.setSelection(0, true);

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente3Activity.this, CadastroPaciente2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        cep.setOnKeyListener(new View.OnKeyListener() {
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
                if (cep.getText().toString().isEmpty()) {
                    cep.setError("Preencha o CEP");
                    cep.requestFocus();
                } else if (numCasa.getText().toString().isEmpty()) {
                    numCasa.setError("Preencha o numero da casa");
                    numCasa.requestFocus();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CadastroPaciente4Activity.class);
                    startActivity(intent);
                    finish();
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
                            cep.clearFocus();
                            cep.setText(response.getString("cep"));
                            rua.setText(response.getString("logradouro"));
                            bairro.setText(response.getString("bairro"));
                            cidade.setText(response.getString("localidade"));
                            uf.setSelection(adapter.getPosition(response.getString("uf")));
                            numCasa.requestFocus();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cep.setText("");
                            rua.setText("");
                            numCasa.setText("");
                            bairro.setText("");
                            cidade.setText("");
                            uf.setSelection(adapter.getPosition("AC"));
                        }
                        cep.setEnabled(true);
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
