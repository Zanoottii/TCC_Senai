package br.senai.tcc.nursecarework.Views.Paciente;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.senai.tcc.nursecarework.Models.Paciente;
import br.senai.tcc.nursecarework.R;

public class ListaOpcoesServicosAdapter extends ArrayAdapter<Paciente> {

    private Activity context;
    private List<Paciente> listaOpcoes;

    public ListaOpcoesServicosAdapter(Activity context, List<Paciente> listaOpcoes) {
        super(context, R.layout.adapter_paciente_servicos, listaOpcoes);
        this.context = context;
        this.listaOpcoes = listaOpcoes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.adapter_paciente_servicos, null, true);

        ImageView imageView = listViewItem.findViewById(R.id.naoChecado);
        TextView textView = listViewItem.findViewById(R.id.txtDescricao);

        Paciente opcao = listaOpcoes.get(position);
        imageView.setImageResource((opcao.isChecked()) ? R.drawable.chk : R.drawable.unchk);
        textView.setText(opcao.getDescricao());

        return listViewItem;
    }
}
