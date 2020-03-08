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

public class MeusPacientesFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<InfoPacientes> listaOpcoes;
    private ArrayAdapter<InfoPacientes> listaOpcoesAdapter;

    public MeusPacientesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_pacientes, container, false);

        lvListaOpcoes = view.findViewById(R.id.listaMeusPacientes);
        listaOpcoes = getListaOpcoes();

        ListaPacientesAdapter adapter = new ListaPacientesAdapter(getActivity(), listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoPacientes infoPacientesObj = listaOpcoes.get(i);
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
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
        return view;
    }

    private ArrayList<InfoPacientes> getListaOpcoes() {
        ArrayList<InfoPacientes> infoPacientesArray = new ArrayList<>();
        InfoPacientes infoPacientesObj = new InfoPacientes();

        infoPacientesObj.setNome("Jonathan");
        infoPacientesObj.setSobrenome("Araujo");
        infoPacientesObj.setDataNasc("10/01/2003");
        infoPacientesObj.setTipoServico("retirada de pontos");
        infoPacientesArray.add(infoPacientesObj);

        return infoPacientesArray;
    }
}
