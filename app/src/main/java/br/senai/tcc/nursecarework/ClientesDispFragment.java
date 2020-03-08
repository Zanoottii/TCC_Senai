package br.senai.tcc.nursecarework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ClientesDispFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<InfoClientes> listaOpcoes;
    private ArrayAdapter<InfoClientes> listaOpcoesAdapter;

    public ClientesDispFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_clientes_disp, container, false);

        lvListaOpcoes = view.findViewById(R.id.listaClientesDisp);
        listaOpcoes = getListaOpcoes();

        ListaClientesAdapter adapter = new ListaClientesAdapter(getActivity(), listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoClientes infoClientessObj = listaOpcoes.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setTitle("Informações do cliente")
                        .setMessage("Nome: " + infoClientessObj.getNomeCliente() +
                                "\nSobrenome: " + infoClientessObj.getSobrenomeCliente() +
                                "\nData de nascimento: " + infoClientessObj.getDataNascCliente() +
                                "\nServiço: " + infoClientessObj.getTipoServicoCliente())
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getActivity(), "Cliente aceito", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Rejeitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getActivity(), "Cliente rejeitado", Toast.LENGTH_SHORT).show();
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
