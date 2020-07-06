package br.senai.tcc.nursecarework.views.requisicao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.views.cooperativa.CooperativaLogadoActivity;

public class CadastroRequisicao2Activity extends AppCompatActivity {
    private ListView lvLista;
    private List<Servico> servicos;
    private ArrayAdapter<Servico> adapter;
    private Requisicao requisicao;
    private Paciente paciente;
    private ServicosFirebase servicosFirebase;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_requisicao_parte2);

        Intent intent = getIntent();
        requisicao = (Requisicao) intent.getSerializableExtra("Requisicao");
        paciente = (Paciente) intent.getSerializableExtra("Paciente");

        servicosFirebase = new ServicosFirebase(this);

        lvLista = findViewById(R.id.lvServicos);

        servicos = new ArrayList<>();
        for (String servico : getResources().getStringArray(R.array.servicos))
            servicos.add(new Servico(servico));

        adapter = new ListaServicosAdapter(this);
        lvLista.setAdapter(adapter);

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                servicos.get(i).inverter();
                adapter.notifyDataSetChanged();
            }
        });

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        findViewById(R.id.ivVoltarRequisicao2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroRequisicao2Activity.this, CadastroRequisicao1Activity.class);
                intent.putExtra("Paciente", paciente);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bCadastrarRequisicao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> servicosSelecionados = new ArrayList<>();
                for (Servico servico : servicos)
                    if (servico.isMarcado())
                        servicosSelecionados.add(servico.getDescricao());
                int quantidade = servicosSelecionados.size();
                if (quantidade > 0) {
                    requisicao.setServico(servicosSelecionados);
                    requisicao.setValor(quantidade * 55);
                    requisicao.setPagamento("Pago pela cooperativa");

                    progress.show();
                    servicosFirebase.cadastrarRequisicao(requisicao, new ServicosFirebase.ResultadoListener() {
                        @Override
                        public void onSucesso(Object objeto) {
                            progress.dismiss();
                            startActivity(new Intent(CadastroRequisicao2Activity.this, CooperativaLogadoActivity.class));
                            finish();
                        }

                        @Override
                        public void onErro(String mensagem) {
                            progress.dismiss();
                            Toast.makeText(CadastroRequisicao2Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CadastroRequisicao2Activity.this, "Selecione pelo menos 1 serviço", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ListaServicosAdapter extends ArrayAdapter<Servico> {
        Activity context;

        ListaServicosAdapter(Activity context) {
            super(context, R.layout.adapter_lista_servicos, servicos);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listView = inflater.inflate(R.layout.adapter_lista_servicos, null, true);

            ImageView ivServico = listView.findViewById(R.id.ivServicoLista);
            TextView tvServico = listView.findViewById(R.id.tvServicoLista);

            Servico servico = servicos.get(position);
            ivServico.setImageResource((servico.isMarcado()) ? R.drawable.indicador_checked : R.drawable.indicador_unchecked);
            tvServico.setText(servico.getDescricao());

            return listView;
        }
    }

    private class Servico {
        private String descricao;
        private boolean marcado;

        Servico(String descricao) {
            this.descricao = descricao;
            this.marcado = false;
        }

        String getDescricao() {
            return descricao;
        }

        boolean isMarcado() {
            return marcado;
        }

        void inverter() {
            this.marcado = !this.marcado;
        }
    }
}
