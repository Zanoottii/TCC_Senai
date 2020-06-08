package br.senai.tcc.nursecarework.Views.Paciente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import br.senai.tcc.nursecarework.Helper.Validacao;
import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.Views.Enfermeiro.CadastroEnfermeiro3Activity;
import br.senai.tcc.nursecarework.Views.Enfermeiro.CadastroEnfermeiro4Activity;
import br.senai.tcc.nursecarework.Views.Enfermeiro.EnfermeiroLogadoActivity;

public class CadastroPaciente5Activity extends AppCompatActivity {

    private ImageView voltar;
    private Button proximo;
    private EditText edtCPF;
    private EditText edtConvenio;
    private EditText edtAgenda;
    private EditText edtHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte5);

        voltar = findViewById(R.id.voltar);
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
                Intent intent = new Intent(CadastroPaciente5Activity.this, CadastroPaciente1Activity.class);
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

                    startActivity(new Intent(CadastroPaciente5Activity.this, CadastroPaciente2Activity.class));
                    finish();
                }
            }
        });

    }
}