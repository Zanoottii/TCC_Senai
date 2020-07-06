package br.senai.tcc.nursecarework.views.enfermeiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.senai.tcc.nursecarework.helpers.ListaRequisicoesEnfermeiroAdapter;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;

public class MinhasRequisicoesFragment extends Fragment {
    private ListView lvLista;
    private List<Requisicao> requisicoes;
    private ListaRequisicoesEnfermeiroAdapter adapter;
    private ServicosFirebase servicosFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        servicosFirebase = ((EnfermeiroLogadoActivity) getActivity()).getServicosFirebase();

        lvLista = view.findViewById(R.id.lvLista);

        servicosFirebase.listarRequisicaoEnfermeiro(Usuario.getInstance().getUid(), new ServicosFirebase.ResultadoListener<List<Requisicao>>() {
            @Override
            public void onSucesso(List<Requisicao> listaRequisicoes) {
                requisicoes = listaRequisicoes;
                adapter = new ListaRequisicoesEnfermeiroAdapter(getActivity(), requisicoes);
                lvLista.setAdapter(adapter);
            }

            @Override
            public void onErro(String mensagem) {
                Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
            }
        });

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Requisicao requisicao = requisicoes.get(i);
                AlertDialog.Builder alertDialogOpcoes = new AlertDialog.Builder(getContext()).setMessage("Selecione uma opção");
                if (Calendar.getInstance().getTimeInMillis() < requisicao.getDatahora())
                    alertDialogOpcoes.setPositiveButton("Cancelar Atendimento", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            servicosFirebase.cancelarRequisicao(requisicao.getId(), new ServicosFirebase.ResultadoListener() {
                                @Override
                                public void onSucesso(Object objeto) {
                                    requisicoes.remove(i);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Atendimento cancelado", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onErro(String mensagem) {
                                    Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                alertDialogOpcoes.setNegativeButton("Ver mais informações", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //alert
                        }
                    })
                    .setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .create()
                    .show();
            }
        });
        return view;
    }
}
