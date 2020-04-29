package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.content.Intent;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.senai.tcc.nursecarework.Helper.MaskAgencia;
import br.senai.tcc.nursecarework.Helper.MaskConta;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro3Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private TextWatcher itauAgencia, bradescoAgencia, bancoSantanderAgencia, bancoDoBrasilAgencia, caixaEconomicaAgencia;
    private TextWatcher itauConta, bradescoConta, bancoSantanderConta, bancoDoBrasilConta, caixaEconomicaConta;
    private Spinner spinner_banco;
    private String agencia,conta,banco;
    private static final String TAG = "ENFERMEIRO";
    private QueryDocumentSnapshot documentSnapshot;

    List<String> id = new ArrayList();


    //acessando o firestore por essa instancia
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseFirestore teste = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte3);

        voltar = findViewById(R.id.voltar4);
        proximo = findViewById(R.id.btnProximo3);
        final EditText editText_Conta = findViewById(R.id.numConta);
        final EditText editText_Agencia = findViewById(R.id.agenciaBD);
        spinner_banco = findViewById(R.id.banco);

        agencia = editText_Conta.getText().toString();
        conta = editText_Conta.getText().toString();
        banco = spinner_banco.getSelectedItem().toString();

        /****************** spinner *****************/
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_banco, android.R.layout.simple_spinner_item);//Montando o spinner no layout
        spinner_banco.setAdapter(adapter);

        //Método automatico que pego a posição e texto que está escrito no spinner
        AdapterView.OnItemSelectedListener escolha_itemSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {
                    //Mascara das agencias
                    itauAgencia = MaskAgencia.insertUm("####", editText_Agencia);
                    editText_Agencia.addTextChangedListener(itauAgencia);

                    editText_Agencia.removeTextChangedListener(bradescoAgencia);
                    editText_Agencia.removeTextChangedListener(bancoSantanderAgencia);
                    editText_Agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editText_Agencia.removeTextChangedListener(caixaEconomicaAgencia);

                    //Mascara das contas
                    itauConta = MaskConta.insertUm("#####-#", editText_Conta);
                    editText_Conta.addTextChangedListener(itauConta);

                    editText_Conta.removeTextChangedListener(bradescoConta);
                    editText_Conta.removeTextChangedListener(bancoSantanderConta);
                    editText_Conta.removeTextChangedListener(bancoDoBrasilConta);
                    editText_Conta.removeTextChangedListener(caixaEconomicaConta);

                    editText_Agencia.setText("");
                    editText_Conta.setText("");

                } else if (position == 1) {
                    //Mascara das agencias
                    bradescoAgencia = MaskAgencia.insertUm("####-#", editText_Agencia);
                    editText_Agencia.addTextChangedListener(bradescoAgencia);

                    editText_Agencia.removeTextChangedListener(itauAgencia);
                    editText_Agencia.removeTextChangedListener(bancoSantanderAgencia);
                    editText_Agencia.removeTextChangedListener(caixaEconomicaAgencia);
                    editText_Agencia.removeTextChangedListener(bancoDoBrasilAgencia);

                    //Mascara das contas
                    bradescoConta = MaskConta.insertUm("#######-#", editText_Conta);
                    editText_Conta.addTextChangedListener(bradescoConta);

                    editText_Conta.removeTextChangedListener(itauConta);
                    editText_Conta.removeTextChangedListener(bancoSantanderConta);
                    editText_Conta.removeTextChangedListener(bancoDoBrasilConta);
                    editText_Conta.removeTextChangedListener(caixaEconomicaConta);


                    editText_Conta.setText("");
                    editText_Agencia.setText("");

                } else if (position == 2) {
                    //Mascara das agencias
                    bancoSantanderAgencia = MaskAgencia.insertUm("####", editText_Agencia);
                    editText_Agencia.addTextChangedListener(bancoSantanderAgencia);

                    editText_Agencia.removeTextChangedListener(itauAgencia);
                    editText_Agencia.removeTextChangedListener(bradescoAgencia);
                    editText_Agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editText_Agencia.removeTextChangedListener(caixaEconomicaAgencia);

                    //Mascara das contas
                    bancoSantanderConta = MaskConta.insertUm("########-#", editText_Conta);
                    editText_Conta.addTextChangedListener(bancoSantanderConta);

                    editText_Conta.removeTextChangedListener(bradescoConta);
                    editText_Conta.removeTextChangedListener(itauConta);
                    editText_Conta.removeTextChangedListener(bancoDoBrasilConta);
                    editText_Conta.removeTextChangedListener(caixaEconomicaConta);

                    editText_Conta.setText("");
                    editText_Agencia.setText("");

                } else if (position == 3) {
                    //Mascara das agencias
                    bancoDoBrasilAgencia = MaskAgencia.insertUm("####-#", editText_Agencia);
                    editText_Agencia.addTextChangedListener(bancoDoBrasilAgencia);

                    editText_Agencia.removeTextChangedListener(itauAgencia);
                    editText_Agencia.removeTextChangedListener(caixaEconomicaAgencia);
                    editText_Agencia.removeTextChangedListener(bradescoAgencia);
                    editText_Agencia.removeTextChangedListener(bancoSantanderAgencia);

                    //Mascara das contas
                    bancoDoBrasilConta = MaskConta.insertUm("########-#", editText_Conta);
                    editText_Conta.addTextChangedListener(bancoDoBrasilConta);

                    editText_Conta.removeTextChangedListener(bradescoConta);
                    editText_Conta.removeTextChangedListener(bancoSantanderConta);
                    editText_Conta.removeTextChangedListener(itauConta);
                    editText_Conta.removeTextChangedListener(caixaEconomicaConta);

                    editText_Conta.setText("");
                    editText_Agencia.setText("");

                } else if (position == 4) {
                    //Mascara das agencias
                    caixaEconomicaAgencia = MaskAgencia.insertUm("####", editText_Agencia);
                    editText_Agencia.addTextChangedListener(caixaEconomicaAgencia);

                    editText_Agencia.removeTextChangedListener(bancoDoBrasilAgencia);
                    editText_Agencia.removeTextChangedListener(itauAgencia);
                    editText_Agencia.removeTextChangedListener(bancoSantanderAgencia);
                    editText_Agencia.removeTextChangedListener(bradescoAgencia);

                    //Mascara das contas
                    caixaEconomicaConta = MaskConta.insertUm("###########-#", editText_Conta);
                    editText_Conta.addTextChangedListener(caixaEconomicaConta);

                    editText_Conta.removeTextChangedListener(bradescoConta);
                    editText_Conta.removeTextChangedListener(bancoSantanderConta);
                    editText_Conta.removeTextChangedListener(bancoDoBrasilConta);
                    editText_Conta.removeTextChangedListener(itauConta);

                    editText_Conta.setText("");
                    editText_Agencia.setText("");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        /* Isso é importante */
        spinner_banco.setOnItemSelectedListener(escolha_itemSelected); // Aqui que eu faço a integração do spinner os métodos dele

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
                if (editText_Agencia.getText().toString().isEmpty()) {
                    editText_Agencia.setError("Preencha a agencia");
                    editText_Agencia.requestFocus();
                } else if (editText_Conta.getText().toString().isEmpty()) {
                    editText_Conta.setError("Preencha o numero da conta");
                    editText_Conta.requestFocus();
                } else {

                    Map<String, Object> enfermeiro = new HashMap<>();
                    enfermeiro.put("agencia", editText_Agencia.getText().toString());
                    enfermeiro.put("conta", editText_Conta.getText().toString());

                    CadastroEnfermeiro2Activity cadastroEnfermeiro2Activity = new CadastroEnfermeiro2Activity();

                    Object vai;

                    vai = db.collection("enfermeiros").document().getId();

                    db.collection("enfermeiros").document(vai.toString())
                            .set(enfermeiro, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DEU BOM");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "DEU MERDA", e);
                                }
                            });

                    Intent intent = new Intent(CadastroEnfermeiro3Activity.this, CadastroEnfermeiro4Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
