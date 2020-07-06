package br.senai.tcc.nursecarework.views.paciente;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.senai.tcc.nursecarework.models.OpcoesServicos;
import br.senai.tcc.nursecarework.R;

public class CadastroPacienteServicoActivity extends AppCompatActivity {

    private ListView lvListaOpcoes;
    private List<OpcoesServicos> listaOpcoes;
    private ArrayAdapter<OpcoesServicos> listaOpcoesAdapter;
    private Button proximo;
    private ImageView voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte_servico);

        voltar = findViewById(R.id.pacienteVoltar2);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPacienteServicoActivity.this, CadastroPacienteLixoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listaOpcoes = new ArrayList<>();
        listaOpcoes.add(new OpcoesServicos("Aplicação de medicamentos"));
        listaOpcoes.add(new OpcoesServicos("Realização de curativos "));
        listaOpcoes.add(new OpcoesServicos("Passagem de sondas de alimentação, sonda vesical"));
        listaOpcoes.add(new OpcoesServicos("Tratamento de lesões de pele"));
        listaOpcoes.add(new OpcoesServicos("Retirada de pontos"));
        listaOpcoes.add(new OpcoesServicos("Clistes/lavagem intestinal"));
        listaOpcoes.add(new OpcoesServicos("Preparo de pacientes para exames(colonoscopia)"));
        listaOpcoes.add(new OpcoesServicos("Tricotomia"));
        listaOpcoes.add(new OpcoesServicos("Realização de banhos de aspersão, banho de leito"));
        listaOpcoes.add(new OpcoesServicos("Aspirações"));
        listaOpcoes.add(new OpcoesServicos("Cuidados com drenos e cateteres"));
        listaOpcoes.add(new OpcoesServicos("Troca de colostomias"));

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
                for (OpcoesServicos opcao : listaOpcoes)
                    if (opcao.isChecked()) selecionados.append(opcao.getDescricao());
                if (selecionados.length() == 0) {
                    Toast.makeText(CadastroPacienteServicoActivity.this, "Selecione o atendimento desejado", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CadastroPacienteServicoActivity.this, CadastroPaciente2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
