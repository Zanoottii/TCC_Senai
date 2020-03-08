package br.senai.tcc.nursecarework;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListaClientesAdapter extends ArrayAdapter<InfoClientes> {

    private Activity context;
    private List<InfoClientes> listaOpcoes;

    public ListaClientesAdapter(Activity context, List<InfoClientes> listaOpcoes) {
        super(context, R.layout.adapter_lista_pacientes, listaOpcoes);
        this.context = context;
        this.listaOpcoes = listaOpcoes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.adapter_lista_clientes, null, true);

        TextView nome = listViewItem.findViewById(R.id.nomeClienteList);
        TextView sobrenome = listViewItem.findViewById(R.id.sobrenomeClienteList);

        InfoClientes opcao = listaOpcoes.get(position);
        nome.setText(opcao.getNomeCliente());
        sobrenome.setText(opcao.getSobrenomeCliente());

        return listViewItem;
    }
}
