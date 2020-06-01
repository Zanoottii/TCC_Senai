package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.senai.tcc.nursecarework.Models.InfoPacientes;
import br.senai.tcc.nursecarework.Models.Paciente;
import br.senai.tcc.nursecarework.Models.Requisicao;
import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.Views.Paciente.ListaPacientesAdapter;
import br.senai.tcc.nursecarework.R;

public class PacientesDispFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<InfoPacientes> listaOpcoes;
    private ArrayList<String> listaIDs;
    private ListaPacientesAdapter adapter;
    private ServicosFirebase servicosFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_pacientes_disp, container, false);

        servicosFirebase = ((EnfermeiroLogadoActivity) getActivity()).getServicosFirebase();

        lvListaOpcoes = view.findViewById(R.id.listaPacientesDisp);
        listaOpcoes = new ArrayList<>();
        listaIDs = new ArrayList<>();

        servicosFirebase.listarRequisicaoEnfermeiro(new ServicosFirebase.ResultadoListener<List<Requisicao>>() {
            @Override
            public void onSucesso(List<Requisicao> requisicoes) {
                for (final Requisicao requisicao : requisicoes) {
                    servicosFirebase.carregarPaciente(requisicao.getPaciente(), new ServicosFirebase.ResultadoListener<Paciente>() {
                        @Override
                        public void onSucesso(Paciente paciente) {
                            InfoPacientes infoPacientes = new InfoPacientes();
                            infoPacientes.setNome(paciente.getNome());
                            infoPacientes.setSobrenome(paciente.getSobrenome());
                            infoPacientes.setDataNasc(paciente.getNascimento());
                            infoPacientes.setTipoServico(TextUtils.join("\n", requisicao.getServico()));
                            listaOpcoes.add(infoPacientes);
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
                final InfoPacientes infoPacientesObj = listaOpcoes.get(i);
                final String idRequisicao = listaIDs.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setTitle("Informações do paciente")
                        .setMessage("Nome: " + infoPacientesObj.getNome() +
                                "\nSobrenome: " + infoPacientesObj.getSobrenome() +
                                "\nData de nascimento: " + infoPacientesObj.getDataNasc() +
                                "\nServiço: " + infoPacientesObj.getTipoServico())
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                servicosFirebase.aceitarRequisicao(idRequisicao, new ServicosFirebase.ResultadoListener() {
                                    @Override
                                    public void onSucesso(Object objeto) {
                                        listaOpcoes.remove(i);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "Paciente aceito", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Rejeitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getActivity(), "Paciente rejeitado", Toast.LENGTH_SHORT).show();
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
        return view;
    }
}
