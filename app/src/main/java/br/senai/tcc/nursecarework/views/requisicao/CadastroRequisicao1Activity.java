package br.senai.tcc.nursecarework.views.paciente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.helpers.Validacao;
import br.senai.tcc.nursecarework.R;

public class CadastroPacienteLixoActivity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText edtCPF;
    private EditText edtConvenio;
    private EditText edtAgenda;
    private EditText edtHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte_lixo);

        voltar = findViewById(R.id.ivVoltarEnfermeiro1);
        proximo = findViewById(R.id.btProximo);
        edtCPF = findViewById(R.id.edtCPF);
        edtConvenio = findViewById(R.id.edtConvenio);
        edtAgenda = findViewById(R.id.edtAgenda);
        edtHorario = findViewById(R.id.edtHorario);

        //mascara para o campo da agenda
        SimpleMaskFormatter simpleMaskAgenda = new SimpleMaskFormatter("NN/NN/NNNN");
        SimpleMaskTextWatcher maskAgenda = new SimpleMaskTextWatcher(edtAgenda, simpleMaskAgenda);
        edtAgenda.addTextChangedListener(maskAgenda);

        //mascara para o campo da horario
        SimpleMaskFormatter simpleMaskHorario = new SimpleMaskFormatter("NN:NN");
        SimpleMaskTextWatcher maskHorario = new SimpleMaskTextWatcher(edtHorario, simpleMaskHorario);
        edtHorario.addTextChangedListener(maskHorario);

        //mascara para o campo da cpf
        SimpleMaskFormatter simpleMaskCPF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        SimpleMaskTextWatcher maskCPF = new SimpleMaskTextWatcher(edtCPF, simpleMaskCPF);
        edtCPF.addTextChangedListener(maskCPF);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPacienteLixoActivity.this, CadastroPaciente1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtCPF.getText().toString().length() == 0 || !Validacao.isCPF(edtCPF.getText().toString()) || edtCPF.getText().equals("1")) {
                    edtCPF.setError("Preencha o CPF corretamente");
                    edtCPF.requestFocus();
                } else if (edtConvenio.getText().toString().isEmpty()) {
                    edtConvenio.setError("Preencha o campo Convenio");
                    edtConvenio.requestFocus();
                } else if (edtAgenda.getText().toString().isEmpty()) {
                    edtAgenda.setError("Preencha o campo Data Atendimento");
                    edtAgenda.requestFocus();
                } else if (edtHorario.getText().toString().isEmpty()) {
                    edtHorario.setError("Preencha o campo Horario");
                    edtHorario.requestFocus();
                } else {

                    startActivity(new Intent(CadastroPacienteLixoActivity.this, CadastroPacienteServicoActivity.class));
                    finish();
                }
            }
        });

    }
}