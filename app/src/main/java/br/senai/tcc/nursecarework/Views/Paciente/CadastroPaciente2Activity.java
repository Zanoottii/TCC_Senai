package br.senai.tcc.nursecarework.Views.Paciente;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.senai.tcc.nursecarework.Models.Paciente;
import br.senai.tcc.nursecarework.R;

public class CadastroPaciente2Activity extends AppCompatActivity {

    private ListView lvListaOpcoes;
    private List<Paciente> listaOpcoes;
    private ArrayAdapter<Paciente> listaOpcoesAdapter;
    private Button proximo;
    private ImageView voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte2);

        voltar = findViewById(R.id.pacienteVoltar2);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente2Activity.this, CadastroPaciente5Activity.class);
                startActivity(intent);
                finish();
            }
        });

        listaOpcoes = new ArrayList<>();
        listaOpcoes.add(new Paciente("OP1", "Aplicação de medicamentos"));
        listaOpcoes.add(new Paciente("OP2", "Realização de curativos "));
        listaOpcoes.add(new Paciente("OP3", "Passagem de sondas de alimentação, sonda vesical"));
        listaOpcoes.add(new Paciente("OP4", "Tratamento de lesões de pele"));
        listaOpcoes.add(new Paciente("OP5", "Retirada de pontos"));
        listaOpcoes.add(new Paciente("OP6", "Clistes/lavagem intestinal"));
        listaOpcoes.add(new Paciente("OP8", "Preparo de pacientes para exames(colonoscopia)"));
        listaOpcoes.add(new Paciente("OP9", "Tricotomia"));
        listaOpcoes.add(new Paciente("OP10", "Realização de banhos de aspersão, banho de leito"));
        listaOpcoes.add(new Paciente("OP11", "Aspirações"));
        listaOpcoes.add(new Paciente("OP12", "Cuidados com drenos e cateteres"));
        listaOpcoes.add(new Paciente("OP13", "Troca de colostomias"));

        listaOpcoesAdapter = new ListaOpcoesServicosAdapter(this, listaOpcoes);

        lvListaOpcoes = findViewById(R.id.listaServicosPaciente);
        lvListaOpcoes.setAdapter(listaOpcoesAdapter);
        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listaOpcoes.get(i).change();
                listaOpcoesAdapter.notifyDataSetChanged();
            }
        });

        proximo = findViewById(R.id.btnCadPaciente2);
        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selecionados = new StringBuilder();
                for (Paciente opcao : listaOpcoes)
                    if (opcao.isChecked()) selecionados.append(opcao.getId());
                if (selecionados.length() == 0) {
                    Toast.makeText(CadastroPaciente2Activity.this, "Selecione o atendimento desejado", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CadastroPaciente2Activity.this, CadastroPaciente3Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
