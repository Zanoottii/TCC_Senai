package br.senai.tcc.nursecarework.helpers;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Requisicao;

public class ListaRequisicoesCooperativaAdapter extends ArrayAdapter<Requisicao> {
    private Activity context;
    private List<Requisicao> requisicoes;
    private long datahora;

    public ListaRequisicoesCooperativaAdapter(Activity context, List<Requisicao> requisicoes, long datahora) {
        super(context, R.layout.adapter_lista_requisicoes_cooperativa, requisicoes);
        this.context = context;
        this.requisicoes = requisicoes;
        this.datahora = datahora;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_lista_requisicoes_cooperativa, null, true);

        TextView tvData = view.findViewById(R.id.tvDataRequisicaoCooperativa);
        TextView tvHora = view.findViewById(R.id.tvHoraRequisicaoCooperativa);
        TextView tvServicos = view.findViewById(R.id.tvServicosRequisicaoCooperativa);
        TextView tvEndereco = view.findViewById(R.id.tvEnderecoRequisicaoCooperativa);
        ImageView ivEstado = view.findViewById(R.id.ivEstadoRequisicaoCooperativa);

        Requisicao requisicao = requisicoes.get(position);
        Locale locale = new Locale("pt", "BR");
        tvData.setText(new SimpleDateFormat("dd/MM", locale).format(requisicao.getDatahora()));
        tvHora.setText(new SimpleDateFormat("HH:mm", locale).format(requisicao.getDatahora()));
        tvServicos.setText("Serviços: " + requisicao.getServico().size());
        tvEndereco.setText("Endereço: " + requisicao.getEndereco());

        if (datahora < requisicao.getDatahora()) {
            ivEstado.setImageResource(TextUtils.isEmpty(requisicao.getEnfermeiro()) ? R.drawable.ic_interrogacao : R.drawable.ic_data);
        } else {
            ivEstado.setImageResource(TextUtils.isEmpty(requisicao.getEnfermeiro()) ? R.drawable.ic_nao_atendido : R.drawable.ic_atendido);
        }

        return view;
    }
}
