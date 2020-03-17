package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.senai.tcc.nursecarework.Models.InfoPacientes;
import br.senai.tcc.nursecarework.Views.Paciente.ListaPacientesAdapter;
import br.senai.tcc.nursecarework.R;

public class PacientesDispFragment extends Fragment {
    private ListView lvListaOpcoes;
    private ArrayList<InfoPacientes> listaOpcoes;
    private ListaPacientesAdapter adapter;

    public PacientesDispFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_pacientes_disp, container, false);

        lvListaOpcoes = view.findViewById(R.id.listaPacientesDisp);
        listaOpcoes = getListaOpcoes();

        adapter = new ListaPacientesAdapter(getActivity(), listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoPacientes infoPacientesObj = listaOpcoes.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setTitle("Informações do paciente")
                        .setMessage("Nome: " + infoPacientesObj.getNome() +
                                "\nSobrenome: " + infoPacientesObj.getSobrenome() +
                                "\nData de nascimento: " + infoPacientesObj.getDataNasc() +
                                "\nServiço: " + infoPacientesObj.getTipoServico())
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getActivity(), "Paciente aceito", Toast.LENGTH_SHORT).show();
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

    private ArrayList<InfoPacientes> getListaOpcoes() {
        ArrayList<InfoPacientes> infoClientesArray = new ArrayList<>();
        InfoPacientes infoPacientesObj = new InfoPacientes();

        infoPacientesObj.setNome("Carlos");
        infoPacientesObj.setSobrenome("Zanotti");
        infoPacientesObj.setDataNasc("03/03/2020");
        infoPacientesObj.setTipoServico("retirada de pontos");
        infoClientesArray.add(infoPacientesObj);

        return infoClientesArray;
    }
}
