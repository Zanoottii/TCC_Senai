package br.senai.tcc.nursecarework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MeusClientesFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<InfoClientes> listaOpcoes;
    private ArrayAdapter<InfoClientes> listaOpcoesAdapter;

    public MeusClientesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_clientes, container, false);

        lvListaOpcoes = view.findViewById(R.id.listaMeusClientes);
        listaOpcoes = getListaOpcoes();

        ListaClientesAdapter adapter = new ListaClientesAdapter(getActivity(), listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoClientes infoClientessObj = listaOpcoes.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setMessage("Selecione uma opção")
                        .setPositiveButton("Remover paciente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listaOpcoesAdapter.notifyDataSetChanged();
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

                                txtNome.setText("Nome: " + infoClientessObj.getNomeCliente());
                                txtSobrenome.setText("Sobrenome: " + infoClientessObj.getSobrenomeCliente());
                                txtNasc.setText("Data de nascimento: " + infoClientessObj.getDataNascCliente());
                                txtServico.setText("Tipo de serviço: " + infoClientessObj.getTipoServicoCliente());

                                final AlertDialog alert2 = builder2.create();
                                alert2.setTitle("Informações do cliente");
                                alert2.show();
                            }
                        })
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
        return view;
    }

    private ArrayList<InfoClientes> getListaOpcoes() {
        ArrayList<InfoClientes> infoClientesArray = new ArrayList<>();
        InfoClientes infoClientesObj = new InfoClientes();

        infoClientesObj.setNomeCliente("Carlos");
        infoClientesObj.setSobrenomeCliente("Zanotti");
        infoClientesObj.setDataNascCliente("03/03/2020");
        infoClientesObj.setTipoServicoCliente("retirada de pontos");
        infoClientesArray.add(infoClientesObj);

        return infoClientesArray;
    }
}
