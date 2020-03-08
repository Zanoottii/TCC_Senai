package br.senai.tcc.nursecarework;

import android.content.Intent;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CadastroEnfermeiro3Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private Spinner spinner;
    // private TextWatcher itauAgencia, bradescoAgencia, santanderAgencia, bancoDoBrasilAgencia, caixaEconomicaAgencia;
    //private TextWatcher itauConta, bradescoConta, santanderConta, bancoDoBrasilConta, caixaEconomicaConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte3);

        voltar = findViewById(R.id.voltar4);
        proximo = findViewById(R.id.btnProximo3);
        final EditText numConta = findViewById(R.id.numConta);
        final EditText agencia = findViewById(R.id.agenciaBD);
        spinner = findViewById(R.id.banco);

        //Mascaras das agências
        //O "Mask.insert" é um comando que eu estou chamando da classe que chama "Mask", ele que é responsavel por inserir a mascara no editText
        /*itauAgencia = MaskBanco.insert("####", agencia);
        bradescoAgencia = MaskBanco.insert("####-#", agencia);
        santanderAgencia = MaskBanco.insert("####", agencia);
        bancoDoBrasilAgencia = MaskBanco.insert("####-#", agencia);
        caixaEconomicaAgencia = MaskBanco.insert("####", agencia);

        //Adicionando a mascara do Banco Bradesco e Banco do Brasil, deixando ela ativa(padrão)
        agencia.addTextChangedListener(itauAgencia);
        agencia.addTextChangedListener(bradescoAgencia);
        agencia.addTextChangedListener(santanderAgencia);
        agencia.addTextChangedListener(bancoDoBrasilAgencia);
        agencia.addTextChangedListener(caixaEconomicaAgencia);*/

        //***************** spinner *****************//
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_banco, android.R.layout.simple_spinner_item); //Montando o spinner no layout
        spinner.setAdapter(adapter);

       /* AdapterView.OnItemSelectedListener escolheBanco = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int posicao, long l) {

                if (posicao == 0) {
                    agencia.removeTextChangedListener(bradescoAgencia);
                    agencia.removeTextChangedListener(santanderAgencia);
                    agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    agencia.removeTextChangedListener(caixaEconomicaAgencia);

                    agencia.addTextChangedListener(itauAgencia);
                } else if (posicao == 1) {
                    agencia.removeTextChangedListener(itauAgencia);
                    agencia.removeTextChangedListener(santanderAgencia);
                    agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    agencia.removeTextChangedListener(caixaEconomicaAgencia);

                    agencia.addTextChangedListener(bradescoAgencia);
                } else if (posicao == 2) {
                    agencia.removeTextChangedListener(bradescoAgencia);
                    agencia.removeTextChangedListener(itauAgencia);
                    agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    agencia.removeTextChangedListener(caixaEconomicaAgencia);

                    agencia.addTextChangedListener(santanderAgencia);
                } else if (posicao == 3) {
                    agencia.removeTextChangedListener(bradescoAgencia);
                    agencia.removeTextChangedListener(santanderAgencia);
                    agencia.removeTextChangedListener(itauAgencia);
                    agencia.removeTextChangedListener(caixaEconomicaAgencia);
                    agencia.addTextChangedListener(bancoDoBrasilAgencia);
                } else if (posicao == 4) {
                    agencia.removeTextChangedListener(bradescoAgencia);
                    agencia.removeTextChangedListener(santanderAgencia);
                    agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    agencia.removeTextChangedListener(itauAgencia);

                    agencia.addTextChangedListener(caixaEconomicaAgencia);
                } else {
                    agencia.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };*/

        /* Isso é importante */
        //spinner.setOnItemSelectedListener(escolheBanco); // Aqui que eu faço a integração do spinner os métodos dele

        //Botão para voltar para a tele de cadastro parte 2
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro3Activity.this, CadastroEnfermeiro6Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para ir para a próxima etapa
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agencia.getText().toString().isEmpty()) {
                    agencia.setError("Preencha a agencia");
                    agencia.requestFocus();
                } else if (numConta.getText().toString().isEmpty()) {
                    numConta.setError("Preencha o numero da conta");
                    numConta.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroEnfermeiro3Activity.this, CadastroEnfermeiro4Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
