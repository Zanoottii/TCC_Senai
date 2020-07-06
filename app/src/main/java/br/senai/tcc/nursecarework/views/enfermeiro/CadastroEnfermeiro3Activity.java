package br.senai.tcc.nursecarework.views.Enfermeiro;

import android.content.Intent;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.senai.tcc.nursecarework.Helper.MaskAgencia;
import br.senai.tcc.nursecarework.Helper.MaskConta;
import br.senai.tcc.nursecarework.Models.Enfermeiro;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro3Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private TextWatcher itauAgencia, bradescoAgencia, bancoSantanderAgencia, bancoDoBrasilAgencia, caixaEconomicaAgencia;
    private TextWatcher itauConta, bradescoConta, bancoSantanderConta, bancoDoBrasilConta, caixaEconomicaConta;
    private Spinner spinnerBanco;
    private EditText editTextConta, editTextAgencia;
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

        voltar = findViewById(R.id.voltar4);
        proximo = findViewById(R.id.btnProximo3);
        editTextConta = findViewById(R.id.numConta);
        editTextAgencia = findViewById(R.id.agenciaBD);
        spinnerBanco = findViewById(R.id.banco);

        /****************** spinner *****************/
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_banco, android.R.layout.simple_spinner_item);//Montando o spinner no layout
        spinnerBanco.setAdapter(adapter);

        //Método automatico que pego a posição e texto que está escrito no spinner
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    //Mascara das agencias
                    itauAgencia = MaskAgencia.insertUm("####", editTextAgencia);
                    editTextAgencia.addTextChangedListener(itauAgencia);

                    editTextAgencia.removeTextChangedListener(bradescoAgencia);
                    editTextAgencia.removeTextChangedListener(bancoSantanderAgencia);
                    editTextAgencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editTextAgencia.removeTextChangedListener(caixaEconomicaAgencia);

                    //Mascara das contas
                    itauConta = MaskConta.insertUm("#####-#", editTextConta);
                    editTextConta.addTextChangedListener(itauConta);

                    editTextConta.removeTextChangedListener(bradescoConta);
                    editTextConta.removeTextChangedListener(bancoSantanderConta);
                    editTextConta.removeTextChangedListener(bancoDoBrasilConta);
                    editTextConta.removeTextChangedListener(caixaEconomicaConta);

                    editTextAgencia.setText("");
                    editTextConta.setText("");

                } else if (position == 1) {
                    //Mascara das agencias
                    bradescoAgencia = MaskAgencia.insertUm("####-#", editTextAgencia);
                    editTextAgencia.addTextChangedListener(bradescoAgencia);

                    editTextAgencia.removeTextChangedListener(itauAgencia);
                    editTextAgencia.removeTextChangedListener(bancoSantanderAgencia);
                    editTextAgencia.removeTextChangedListener(caixaEconomicaAgencia);
                    editTextAgencia.removeTextChangedListener(bancoDoBrasilAgencia);

                    //Mascara das contas
                    bradescoConta = MaskConta.insertUm("#######-#", editTextConta);
                    editTextConta.addTextChangedListener(bradescoConta);

                    editTextConta.removeTextChangedListener(itauConta);
                    editTextConta.removeTextChangedListener(bancoSantanderConta);
                    editTextConta.removeTextChangedListener(bancoDoBrasilConta);
                    editTextConta.removeTextChangedListener(caixaEconomicaConta);


                    editTextConta.setText("");
                    editTextAgencia.setText("");

                } else if (position == 2) {
                    //Mascara das agencias
                    bancoSantanderAgencia = MaskAgencia.insertUm("####", editTextAgencia);
                    editTextAgencia.addTextChangedListener(bancoSantanderAgencia);

                    editTextAgencia.removeTextChangedListener(itauAgencia);
                    editTextAgencia.removeTextChangedListener(bradescoAgencia);
                    editTextAgencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editTextAgencia.removeTextChangedListener(caixaEconomicaAgencia);

                    //Mascara das contas
                    bancoSantanderConta = MaskConta.insertUm("########-#", editTextConta);
                    editTextConta.addTextChangedListener(bancoSantanderConta);

                    editTextConta.removeTextChangedListener(bradescoConta);
                    editTextConta.removeTextChangedListener(itauConta);
                    editTextConta.removeTextChangedListener(bancoDoBrasilConta);
                    editTextConta.removeTextChangedListener(caixaEconomicaConta);

                    editTextConta.setText("");
                    editTextAgencia.setText("");

                } else if (position == 3) {
                    //Mascara das agencias
                    bancoDoBrasilAgencia = MaskAgencia.insertUm("####-#", editTextAgencia);
                    editTextAgencia.addTextChangedListener(bancoDoBrasilAgencia);

                    editTextAgencia.removeTextChangedListener(itauAgencia);
                    editTextAgencia.removeTextChangedListener(caixaEconomicaAgencia);
                    editTextAgencia.removeTextChangedListener(bradescoAgencia);
                    editTextAgencia.removeTextChangedListener(bancoSantanderAgencia);

                    //Mascara das contas
                    bancoDoBrasilConta = MaskConta.insertUm("########-#", editTextConta);
                    editTextConta.addTextChangedListener(bancoDoBrasilConta);

                    editTextConta.removeTextChangedListener(bradescoConta);
                    editTextConta.removeTextChangedListener(bancoSantanderConta);
                    editTextConta.removeTextChangedListener(itauConta);
                    editTextConta.removeTextChangedListener(caixaEconomicaConta);

                    editTextConta.setText("");
                    editTextAgencia.setText("");

                } else if (position == 4) {
                    //Mascara das agencias
                    caixaEconomicaAgencia = MaskAgencia.insertUm("####", editTextAgencia);
                    editTextAgencia.addTextChangedListener(caixaEconomicaAgencia);

                    editTextAgencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editTextAgencia.removeTextChangedListener(itauAgencia);
                    editTextAgencia.removeTextChangedListener(bancoSantanderAgencia);
                    editTextAgencia.removeTextChangedListener(bradescoAgencia);

                    //Mascara das contas
                    caixaEconomicaConta = MaskConta.insertUm("###########-#", editTextConta);
                    editTextConta.addTextChangedListener(caixaEconomicaConta);

                    editTextConta.removeTextChangedListener(bradescoConta);
                    editTextConta.removeTextChangedListener(bancoSantanderConta);
                    editTextConta.removeTextChangedListener(bancoDoBrasilConta);
                    editTextConta.removeTextChangedListener(itauConta);

                    editTextConta.setText("");
                    editTextAgencia.setText("");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        /* Isso é importante */
        spinnerBanco.setOnItemSelectedListener(onItemSelectedListener); // Aqui que eu faço a integração do spinner os métodos dele

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
                if (editTextAgencia.getText().toString().isEmpty()) {
                    editTextAgencia.setError("Preencha a agencia");
                    editTextAgencia.requestFocus();
                } else if (editTextConta.getText().toString().isEmpty()) {
                    editTextConta.setError("Preencha o numero da conta");
                    editTextConta.requestFocus();
                } else {
                    enfermeiro.setBanco(spinnerBanco.getSelectedItem().toString());
                    enfermeiro.setAgencia(editTextAgencia.getText().toString());
                    enfermeiro.setConta(editTextConta.getText().toString());

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
}
