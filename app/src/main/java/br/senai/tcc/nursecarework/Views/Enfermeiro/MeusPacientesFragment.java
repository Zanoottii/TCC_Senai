package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.senai.tcc.nursecarework.Models.Paciente;
import br.senai.tcc.nursecarework.Models.Requisicao;
import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.Views.Paciente.ListaPacientesAdapter;
import br.senai.tcc.nursecarework.R;

public class MeusPacientesFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<Paciente> listaOpcoes;
    private ArrayList<String> listaIDs;
    private ListaPacientesAdapter adapter;
    private ServicosFirebase servicosFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_pacientes, container, false);

        servicosFirebase = ((EnfermeiroLogadoActivity) getActivity()).getServicosFirebase();

        lvListaOpcoes = view.findViewById(R.id.listaMeusPacientes);
        listaOpcoes = new ArrayList<>();
        listaIDs = new ArrayList<>();

        servicosFirebase.requisicaoAceitaEnfermeiro(new ServicosFirebase.ResultadoListener<List<Requisicao>>() {
            @Override
            public void onSucesso(List<Requisicao> requisicoes) {
                for (final Requisicao requisicao : requisicoes) {
                    servicosFirebase.carregarPaciente(requisicao.getPaciente(), new ServicosFirebase.ResultadoListener<Paciente>() {
                        @Override
                        public void onSucesso(Paciente paciente) {
                            String id = null;
                            String descricao = null;
                            Paciente pacientes = new Paciente(id,descricao);
                            pacientes.setNome(paciente.getNome());
                            pacientes.setSobrenome(paciente.getSobrenome());
                            pacientes.setDataNasc(paciente.getNascimento());
                            pacientes.setTipoServico(TextUtils.join("\n", requisicao.getServico()));
                            listaOpcoes.add(pacientes);
                            listaIDs.add(requisicao.getId());
                            adapter = new ListaPacientesAdapter(getActivity(), listaOpcoes);
                            lvListaOpcoes.setAdapter(adapter);
                        }

                        @Override
                        public void onErro(String mensagem) {
                            Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onErro(String mensagem) {
                Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
            }
        });

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                final Paciente infoPacientesObj = listaOpcoes.get(i);
                final String idRequisicao = listaIDs.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setMessage("Selecione uma opção")
                        .setPositiveButton("Remover paciente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                servicosFirebase.cancelarRequisicao(idRequisicao, new ServicosFirebase.ResultadoListener() {
                                    @Override
                                    public void onSucesso(Object objeto) {
                                        listaOpcoes.remove(i);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Paciente removido", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Ver informações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());

                                View layoutInformacoes = getLayoutInflater().inflate(R.layout.alert_info_pacientes, null);
                                builder2.setView(layoutInformacoes);

                                final TextView txtNome = layoutInformacoes.findViewById(R.id.nomePacienteAlert);
                                final TextView txtSobrenome = layoutInformacoes.findViewById(R.id.sobrenomePacienteAlert);
                                final TextView txtNasc = layoutInformacoes.findViewById(R.id.dataNascPacienteALert);
                                final TextView txtServico = layoutInformacoes.findViewById(R.id.tipoServicoAlert);

                                txtNome.setText("Nome: " + infoPacientesObj.getNome());
                                txtSobrenome.setText("Sobrenome: " + infoPacientesObj.getSobrenome());
                                txtNasc.setText("Data de nascimento: " + infoPacientesObj.getDataNasc());
                                txtServico.setText("Tipo de serviço: " + infoPacientesObj.getTipoServico());

                                final AlertDialog alert2 = builder2.create();
                                alert2.setTitle("Informações do paciente");
                                alert2.show();
                            }
                        })
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
        return view;
    }
}
