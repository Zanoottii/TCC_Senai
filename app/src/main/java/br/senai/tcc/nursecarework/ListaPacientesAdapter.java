package br.senai.tcc.nursecarework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaPacientesAdapter extends ArrayAdapter<InfoPacientes> {

    private Activity context;
    private List<InfoPacientes> listaOpcoes;

    public ListaPacientesAdapter(Activity context, List<InfoPacientes> listaOpcoes) {
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

        InfoPacientes opcao = listaOpcoes.get(position);
        nome.setText(opcao.getNome());
        sobrenome.setText(opcao.getSobrenome());

        return listViewItem;
    }
}
