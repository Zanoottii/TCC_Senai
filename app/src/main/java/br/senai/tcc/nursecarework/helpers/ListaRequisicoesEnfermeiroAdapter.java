package br.senai.tcc.nursecarework.helpers;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.models.Requisicao;

public class ListaRequisicoesEnfermeiroAdapter extends ArrayAdapter<Requisicao> {
    private Activity context;
    private List<Requisicao> requisicoes;
    private long datahora;

    public ListaRequisicoesEnfermeiroAdapter(Activity context, List<Requisicao> requisicoes, long datahora) {
        super(context, R.layout.adapter_lista_requisicoes_enfermeiro, requisicoes);
        this.context = context;
        this.requisicoes = requisicoes;
        this.datahora = datahora;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_lista_requisicoes_enfermeiro, null, true);

        TextView tvData = view.findViewById(R.id.tvDataRequisicaoEnfermeiro);
        TextView tvHora = view.findViewById(R.id.tvHoraRequisicaoEnfermeiro);
        TextView tvSolicitante = view.findViewById(R.id.tvSolicitanteRequisicaoEnfermeiro);
        TextView tvDistancia = view.findViewById(R.id.tvDistanciaRequisicaoEnfermeiro);
        TextView tvServicos = view.findViewById(R.id.tvServicosRequisicaoEnfermeiro);
        TextView tvValor = view.findViewById(R.id.tvValorRequisicaoEnfermeiro);
        ImageView ivSolicitante = view.findViewById(R.id.ivSolicitanteRequisicaoEnfermeiro);
        ImageView ivEstado = view.findViewById(R.id.ivEstadoRequisicaoEnfermeiro);

        Locale locale = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        Requisicao requisicao = requisicoes.get(position);
        tvData.setText(new SimpleDateFormat("dd/MM", locale).format(requisicao.getDatahora()));
        tvHora.setText(new SimpleDateFormat("HH:mm", locale).format(requisicao.getDatahora()));

        if (requisicao.getCooperativa().isEmpty()) {
            tvSolicitante.setText("Solicitante: Paciente");
            ivSolicitante.setImageResource(R.drawable.ic_usuario);
        } else {
            tvSolicitante.setText("Solicitante: Cooperativa");
            ivSolicitante.setImageResource(R.drawable.ic_casa);
        }

        int servicos = requisicao.getServico().size();
        if (requisicao.getDistancia() > 0) {
            String distancia = (requisicao.getDistancia() > 999) ?
                    String.format("%.1f Km", (float) requisicao.getDistancia() / 1000) :
                    requisicao.getDistancia() + " m";
            tvDistancia.setText("Distância: " + distancia);
        } else {
            tvDistancia.setVisibility(View.GONE);
        }
        tvServicos.setText("Serviços: " + servicos);
        tvValor.setText("R$ " + numberFormat.format(servicos * 45));

        if (TextUtils.isEmpty(requisicao.getEnfermeiro())) {
            ivEstado.setImageResource(R.drawable.ic_interrogacao);
        } else {
            ivEstado.setImageResource(datahora < requisicao.getDatahora() ? R.drawable.ic_data : R.drawable.ic_atendido);
        }

        return view;
    }
}
