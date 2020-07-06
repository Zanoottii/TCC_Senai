package br.senai.tcc.nursecarework.views.paciente;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.R;

public class ListaPacientesAdapter extends ArrayAdapter<Paciente> {

    private Activity context;
    private List<Paciente> listaOpcoes;

    public ListaPacientesAdapter(Activity context, List<Paciente> listaOpcoes) {
        super(context, R.layout.adapter_lista_pacientes, listaOpcoes);
        this.context = context;
        this.listaOpcoes = listaOpcoes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.adapter_lista_pacientes, null, true);

        TextView nome = listViewItem.findViewById(R.id.nomePacienteList);
        TextView sobrenome = listViewItem.findViewById(R.id.sobrenomePacienteList);

        Paciente opcao = listaOpcoes.get(position);
        nome.setText(opcao.getNome());
        sobrenome.setText(opcao.getSobrenome());

        return listViewItem;
    }
}
