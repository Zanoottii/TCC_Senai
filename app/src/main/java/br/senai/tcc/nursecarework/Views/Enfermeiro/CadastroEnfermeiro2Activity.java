package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Ordering;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.senai.tcc.nursecarework.Helper.Validacao;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro2Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText edtcpf, edtcelular, edtnome,edtsobrenome;
    private static final String TAG = "ENFERMEIRO";
    private QueryDocumentSnapshot documentSnapshot;
    private String cpf, celular, nome, sobrenome;

    List<String> id = new ArrayList();


    //acessando o firestore por essa instancia
    FirebaseFirestore db = FirebaseFirestore.getInstance();

   private DocumentReference ref = db.collection("enfermeiros").document();
    private String myId = ref.getId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte2);

        voltar = findViewById(R.id.voltar3);
        proximo = findViewById(R.id.btnProximo);
        edtcpf = findViewById(R.id.cpfEnfermeiro);
        edtcelular = findViewById(R.id.foneEnfermeiro);
        edtnome = findViewById(R.id.nomeEnfermeiro);
        edtsobrenome = findViewById(R.id.sobrenomeEnfer);

        cpf = edtcpf.getText().toString();
        celular = edtcelular.getText().toString();
        nome = edtnome.getText().toString();
        sobrenome = edtsobrenome.getText().toString();

        //mascara para o campo cpf
        SimpleMaskFormatter simpleMaskCpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        SimpleMaskTextWatcher maskcpf = new SimpleMaskTextWatcher(edtcpf, simpleMaskCpf);
        edtcpf.addTextChangedListener(maskcpf);

        //mascara para o campo telefone
        SimpleMaskFormatter simpleMaskFone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskTextWatcher maskfone = new SimpleMaskTextWatcher(edtcelular, simpleMaskFone);
        edtcelular.addTextChangedListener(maskfone);

        //Botão para voltar para a tele de cadastro parte 1
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para ir para a próxima etapa
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtnome.getText().toString().isEmpty()) {
                    edtnome.setError("Preencha o nome");
                    edtnome.requestFocus();
                } else if (edtsobrenome.getText().toString().isEmpty()) {
                    edtsobrenome.setError("Preencha o sobrenome");
                    edtsobrenome.requestFocus();
                } else if (edtcpf.getText().toString().length() == 0 || !Validacao.isCPF(edtcpf.getText().toString()) || edtcpf.getText().equals("1")) {
                    edtcpf.setError("Preencha o CPF corretamente");
                    edtcpf.requestFocus();
                } else if (edtcelular.getText().toString().isEmpty()) {
                    edtcelular.setError("Preencha o numero do celular");
                    edtcelular.requestFocus();
                } else {
                    Map<String, Object> enfermeiro = new HashMap<>();
                    enfermeiro.put("nome", edtnome.getText().toString());
                    enfermeiro.put("sobrenome", edtsobrenome.getText().toString());
                    enfermeiro.put("celular", edtcelular.getText().toString());
                    enfermeiro.put("cpf", edtcpf.getText().toString());

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


                    Intent intent = new Intent(CadastroEnfermeiro2Activity.this, CadastroEnfermeiro5Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}