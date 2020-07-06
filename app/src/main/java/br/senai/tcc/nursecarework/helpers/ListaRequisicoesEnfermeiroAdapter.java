package br.senai.tcc.nursecarework.helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;

public class ListaRequisicoesAdapter extends ArrayAdapter<Requisicao> {

    private Activity context;
    private List<Paciente> pacientes;

    public ListaRequisicoesAdapter(Activity context, List<Paciente> pacientes) {
        super(context, R.layout.adapter_lista_pacientes, pacientes);
        this.context = context;
        this.pacientes = pacientes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_lista_pacientes, null, true);

        TextView tvNome = view.findViewById(R.id.tvNomePacienteLista);
        TextView tvCpf = view.findViewById(R.id.tvCpfPacienteLista);

        Paciente paciente = pacientes.get(position);
        tvNome.setText(paciente.getNome() + " " + paciente.getSobrenome());
        tvCpf.setText("CPF: " + paciente.getCpf());

        return view;
    }
}
