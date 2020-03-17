package br.senai.tcc.nursecarework.Views.Cooperativa;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.Views.MainActivity;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.Helper.Validacao;

public class CadastroCooperativa2Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button cadastrar;
    private EditText email, municipio, cnpj;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner uf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cooperativa2);

        voltar = findViewById(R.id.voltar);
        cadastrar = findViewById(R.id.cadastrar);
        cnpj = findViewById(R.id.cnpj);
        email = findViewById(R.id.emailCooperativa);
        uf = findViewById(R.id.estadoCooperativa);
        municipio = findViewById(R.id.municipioCooperativa);

        //mascara para o campo cnpj
        SimpleMaskFormatter simpleMaskCnpj = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN");
        SimpleMaskTextWatcher maskCnpj = new SimpleMaskTextWatcher(cnpj, simpleMaskCnpj);
        cnpj.addTextChangedListener(maskCnpj);

        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_estados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uf.setAdapter(adapter);
        uf.setSelection(0, true);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroCooperativa2Activity.this, CadastroCooperativa1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches() || email.getText().toString().isEmpty()) {
                    email.setError("Preencha o email corretamente");
                    email.requestFocus();
                } else if (municipio.getText().toString().isEmpty()) {
                    municipio.setError("Preencha o municipio");
                    municipio.requestFocus();
                } else if (cnpj.getText().toString().isEmpty()) {
                    cnpj.setError("Preencha o CNPJ");
                    cnpj.requestFocus();
                } else if (!Validacao.isCNPJ(cnpj.getText().toString())) {
                    cnpj.setError("O CNPJ digitado não é valido");
                    cnpj.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroCooperativa2Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });
    }
}
