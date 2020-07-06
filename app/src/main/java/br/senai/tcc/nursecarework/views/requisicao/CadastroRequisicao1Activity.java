package br.senai.tcc.nursecarework.views.requisicao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.senai.tcc.nursecarework.helpers.Validacao;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.views.cooperativa.CooperativaLogadoActivity;

public class CadastroRequisicao1Activity extends AppCompatActivity {
    private EditText edtData, edtHorario;
    private Paciente paciente;
    private Calendar calendar;
    private Locale locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_requisicao_parte1);

        paciente = (Paciente) getIntent().getSerializableExtra("Paciente");

        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        locale = new Locale("pt", "BR");

        edtData = findViewById(R.id.etDataRequisicao);
        edtHorario = findViewById(R.id.etHorarioRequisicao);

        edtData.addTextChangedListener(new SimpleMaskTextWatcher(edtData, new SimpleMaskFormatter("NN/NN/NNNN")));
        edtHorario.addTextChangedListener(new SimpleMaskTextWatcher(edtHorario, new SimpleMaskFormatter("NN:NN")));

        edtData.setText(new SimpleDateFormat("dd/MM/yyyy", locale).format(calendar.getTimeInMillis()));
        edtHorario.setText(new SimpleDateFormat("HH:mm", locale).format(calendar.getTimeInMillis()));

        findViewById(R.id.ibDataRequisicao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                DatePickerDialog dataPickerDialog = new DatePickerDialog(CadastroRequisicao1Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        edtData.setText((day < 10 ? "0" : "") + day + "/" + (month < 9 ? "0" : "") + (month + 1) + "/" + year);
                    }
                }, ano, mes, dia);

                dataPickerDialog.setTitle("Selecione a data");
                dataPickerDialog.show();
                edtData.requestFocus();
            }
        });

        findViewById(R.id.ibHorarioRequisicao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CadastroRequisicao1Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        edtHorario.setText((hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute);
                    }
                }, hora, minuto, true);

                timePickerDialog.setTitle("Selecione o horário");
                timePickerDialog.show();
                edtHorario.requestFocus();
            }
        });

        findViewById(R.id.ivVoltarRequisicao1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroRequisicao1Activity.this, CooperativaLogadoActivity.class));
                finish();
            }
        });

        findViewById(R.id.bProximoRequisicao1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validacao.isData(edtData.getText().toString(), "dd/MM/yyyy")) {
                    edtData.setError("Preencha a data corretamente");
                    edtData.requestFocus();
                } else if (!Validacao.isData(edtHorario.getText().toString(), "HH:mm")) {
                    edtHorario.setError("Preencha o horário corretamente");
                    edtHorario.requestFocus();
                } else {
                    long datahora = 0;
                    try {
                        datahora = new SimpleDateFormat("dd/MM/yyyy HH:mm", locale).parse(edtData.getText().toString() + " " + edtHorario.getText().toString()).getTime();
                    } catch (ParseException e) {}
                    if (datahora > 0) {
                        Calendar minimoAceito = Calendar.getInstance();
                        minimoAceito.add(Calendar.MINUTE, 59);
                        if (datahora > minimoAceito.getTimeInMillis()) {
                            Requisicao requisicao = new Requisicao();
                            requisicao.setDatahora(datahora);
                            requisicao.setEnfermeiro("");
                            requisicao.setPaciente(paciente.getId());
                            requisicao.setLatitude(paciente.getLatitude());
                            requisicao.setLongitude(paciente.getLongitude());
                            String endereco = paciente.getLogradouro() + ", " + paciente.getNumero() +
                                    (paciente.getComplemento().length() > 0 ? " " + paciente.getComplemento() : "") + " - " +
                                    paciente.getBairro() + " - " + paciente.getMunicipio() + " - " + paciente.getUf();
                            requisicao.setEndereco(endereco);

                            Intent intent = new Intent(CadastroRequisicao1Activity.this, CadastroRequisicao2Activity.class);
                            intent.putExtra("Requisicao", requisicao);
                            intent.putExtra("Paciente", paciente);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(CadastroRequisicao1Activity.this, "A data e o horário devem ser daqui 1 hora no mínimo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}