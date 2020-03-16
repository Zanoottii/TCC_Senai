package br.senai.tcc.nursecarework;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

public class CadastroPaciente1Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText dataNacimento, nomePaciente, sobrenomePaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte1);
        dataNacimento = findViewById(R.id.dataNascimentoPaciente);
        nomePaciente = findViewById(R.id.nomePaciente);
        sobrenomePaciente = findViewById(R.id.sobrenomePaciente);
        voltar = findViewById(R.id.pacienteVoltar1);
        proximo = findViewById(R.id.btnCadPaciente1);

        SimpleMaskFormatter smfData = new SimpleMaskFormatter("NN/NN/NNNN");
        SimpleMaskTextWatcher smtData = new SimpleMaskTextWatcher(dataNacimento, smfData);
        dataNacimento.addTextChangedListener(smtData);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente1Activity.this, CooperativaLogadoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nomePaciente.getText().toString().isEmpty()) {
                    nomePaciente.setError("Preencha o nome");
                    nomePaciente.requestFocus();
                } else if (sobrenomePaciente.getText().toString().isEmpty()) {
                    sobrenomePaciente.setError("Preencha o sobrenome");
                    sobrenomePaciente.requestFocus();
                } else if (dataNacimento.getText().toString().isEmpty()) {
                    dataNacimento.setError("Preencha a data de nascimento");
                    dataNacimento.requestFocus();
                } else {
                    Intent intent = new Intent(CadastroPaciente1Activity.this, CadastroPaciente2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
