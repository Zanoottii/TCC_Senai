package br.senai.tcc.nursecarework;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CadastroPaciente2Activity extends AppCompatActivity {

    private ListView lvListaOpcoes;
    private List<OpcoesServicos> listaOpcoes;
    private ArrayAdapter<OpcoesServicos> listaOpcoesAdapter;
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
                Intent intent = new Intent(CadastroPaciente2Activity.this, CadastroPaciente1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        listaOpcoes = new ArrayList<>();
        listaOpcoes.add(new OpcoesServicos("OP1", "Aplicação de medicamentos"));
        listaOpcoes.add(new OpcoesServicos("OP2", "Realização de curativos "));
        listaOpcoes.add(new OpcoesServicos("OP3", "Passagem de sondas de alimentação, sonda vesical"));
        listaOpcoes.add(new OpcoesServicos("OP4", "Tratamento de lesões de pele"));
        listaOpcoes.add(new OpcoesServicos("OP5", "Retirada de pontos"));
        listaOpcoes.add(new OpcoesServicos("OP6", "Clistes/lavagem intestinal"));
        listaOpcoes.add(new OpcoesServicos("OP8", "Preparo de pacientes para exames(colonoscopia)"));
        listaOpcoes.add(new OpcoesServicos("OP9", "Tricotomia"));
        listaOpcoes.add(new OpcoesServicos("OP10", "Realização de banhos de aspersão, banho de leito"));
        listaOpcoes.add(new OpcoesServicos("OP11", "Aspirações"));
        listaOpcoes.add(new OpcoesServicos("OP12", "Cuidados com drenos e cateteres"));
        listaOpcoes.add(new OpcoesServicos("OP13", "Troca de colostomias"));

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
