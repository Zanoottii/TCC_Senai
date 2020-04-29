package br.senai.tcc.nursecarework.Views.Enfermeiro;

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

import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro5Activity extends AppCompatActivity {

    private ImageView voltar;
    private EditText edtcep, edtbairro, edtrua, edtnumCasa, edtcidade;
    private Button proximo;
    private ArrayAdapter<CharSequence> adapter;
    private ProgressDialog progress;
    private Spinner spinnerUf;
    private String cep, bairro, rua, numCasa, cidade,uf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte5);

        voltar = findViewById(R.id.volta);
        edtcep = findViewById(R.id.cep);
        edtbairro = findViewById(R.id.bairro);
        edtrua = findViewById(R.id.rua);
        edtnumCasa = findViewById(R.id.numCasa);
        edtcidade = findViewById(R.id.cidade);
        spinnerUf = findViewById(R.id.uf);
        proximo = findViewById(R.id.proximo);

        cep = edtcep.getText().toString();
        bairro = edtbairro.getText().toString();
        rua = edtrua.getText().toString();
        numCasa = edtnumCasa.getText().toString();
        cidade = edtcidade.getText().toString();
        uf = spinnerUf.getSelectedItem().toString();


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

        edtcep.setOnKeyListener(new View.OnKeyListener() {
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

                if (edtcep.getText().toString().isEmpty()) {
                    edtcep.setError("Preencha o CEP");
                    edtcep.requestFocus();
                } else if (edtnumCasa.getText().toString().isEmpty()) {
                    edtnumCasa.setError("Preencha o numero da casa");
                    edtnumCasa.requestFocus();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CadastroEnfermeiro6Activity.class);
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
                            edtcep.clearFocus();
                            edtcep.setText(response.getString("cep"));
                            edtrua.setText(response.getString("logradouro"));
                            edtbairro.setText(response.getString("bairro"));
                            edtcidade.setText(response.getString("localidade"));
                            spinnerUf.setSelection(adapter.getPosition(response.getString("uf")));
                            edtnumCasa.requestFocus();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            edtcep.setText("");
                            edtrua.setText("");
                            edtnumCasa.setText("");
                            edtbairro.setText("");
                            edtcidade.setText("");
                            spinnerUf.setSelection(adapter.getPosition("AC"));
                        }
                        edtcep.setEnabled(true);
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
