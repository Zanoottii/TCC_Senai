package br.senai.tcc.nursecarework.views.enfermeiro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.senai.tcc.nursecarework.helpers.ListaRequisicoesEnfermeiroAdapter;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.models.Cooperativa;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;

public class MeusAtendimentosFragment extends Fragment {
    private ListView lvLista;
    private List<Requisicao> requisicoes;
    private ListaRequisicoesEnfermeiroAdapter adapter;
    private ServicosFirebase servicosFirebase;
    private long datahora;
    private Locale locale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        servicosFirebase = ((EnfermeiroLogadoActivity) getActivity()).getServicosFirebase();
        locale = new Locale("pt", "BR");

        lvLista = view.findViewById(R.id.lvLista);

        servicosFirebase.listarRequisicaoEnfermeiro(Usuario.getInstance().getUid(), new ServicosFirebase.ResultadoListener<List<Requisicao>>() {
            @Override
            public void onSucesso(List<Requisicao> listaRequisicoes) {
                requisicoes = listaRequisicoes;
                datahora = Calendar.getInstance().getTimeInMillis();
                adapter = new ListaRequisicoesEnfermeiroAdapter(getActivity(), requisicoes, datahora);
                lvLista.setAdapter(adapter);
            }

            @Override
            public void onErro(String mensagem) {
                Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
            }
        });

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Requisicao requisicao = requisicoes.get(i);
                AlertDialog.Builder alertDialogOpcao = new AlertDialog.Builder(getActivity()).setMessage("Selecione uma opção");
                if (Calendar.getInstance().getTimeInMillis() < requisicao.getDatahora()) {
                    alertDialogOpcao.setPositiveButton("Cancelar atendimento", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            servicosFirebase.cancelarRequisicao(requisicao, new ServicosFirebase.ResultadoListener() {
                                @Override
                                public void onSucesso(Object objeto) {
                                    requisicoes.remove(i);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Atendimento cancelado", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onErro(String mensagem) {
                                    Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                alertDialogOpcao.setNegativeButton("Ver mais informações", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        View layout = getLayoutInflater().inflate(R.layout.alert_informacoes_requisicao_enfermeiro, null);
                        AlertDialog.Builder alertDialogInformacoes = new AlertDialog.Builder(getActivity());

                        ConstraintLayout clCooperativa = layout.findViewById(R.id.clCooperativaRequisicaoEnfermeiroAlert);
                        final CircularImageView civFotoPaciente = layout.findViewById(R.id.civFotoPacienteRequisicaoEnfermeiroAlert);
                        TextView tvEstado = layout.findViewById(R.id.tvEstadoRequisicaoEnfermeiroAlert);
                        TextView tvDataHora = layout.findViewById(R.id.tvDataHoraRequisicaoEnfermeiroAlert);
                        TextView tvEndereco = layout.findViewById(R.id.tvEnderecoRequisicaoEnfermeiroAlert);
                        ImageView ivEndereco = layout.findViewById(R.id.ivEnderecoRequisicaoEnfermeiroAlert);
                        final TextView tvNomePaciente = layout.findViewById(R.id.tvNomePacienteRequisicaoEnfermeiroAlert);
                        final TextView tvIdadePaciente = layout.findViewById(R.id.tvIdadePacienteRequisicaoEnfermeiroAlert);
                        final TextView tvNomeCooperativa = layout.findViewById(R.id.tvNomeCooperativaRequisicaoEnfermeiroAlert);
                        final TextView tvMunicipioCooperativa = layout.findViewById(R.id.tvMunicipioCooperativaRequisicaoEnfermeiroAlert);
                        TextView tvServicos = layout.findViewById(R.id.tvServicosRequisicaoEnfermeiroAlert);

                        if (!TextUtils.isEmpty(requisicao.getCooperativa())) {
                            servicosFirebase.carregarCooperativa(requisicao.getCooperativa(), new ServicosFirebase.ResultadoListener<Cooperativa>() {
                                @Override
                                public void onSucesso(Cooperativa cooperativa) {
                                    tvNomeCooperativa.setText(cooperativa.getNome());
                                    tvMunicipioCooperativa.setText(cooperativa.getMunicipio() + " - " + cooperativa.getUf());
                                }

                                @Override
                                public void onErro(String mensagem) {
                                    Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                }
                            });
                            clCooperativa.setVisibility(View.VISIBLE);
                        } else {
                            clCooperativa.setVisibility(View.GONE);
                        }

                        servicosFirebase.downloadFoto(requisicao.getPaciente(), new ServicosFirebase.ResultadoListener<Bitmap>() {
                            @Override
                            public void onSucesso(Bitmap foto) {
                                civFotoPaciente.setImageBitmap(foto);
                            }

                            @Override
                            public void onErro(String mensagem) {
                                Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                            }
                        });

                        servicosFirebase.carregarPaciente(requisicao.getPaciente(), new ServicosFirebase.ResultadoListener<Paciente>() {
                            @Override
                            public void onSucesso(Paciente paciente) {
                                tvNomePaciente.setText(paciente.getNome() + " " + paciente.getSobrenome());
                                Calendar calendar = Calendar.getInstance();
                                long nascimento = -1;
                                try {
                                    nascimento = new SimpleDateFormat("dd/MM/yyyy", locale).parse(paciente.getNascimento()).getTime();
                                } catch (ParseException e) {
                                }
                                if (nascimento > 0) {
                                    calendar.setTimeInMillis(calendar.getTimeInMillis() - nascimento);
                                    tvIdadePaciente.setText(calendar.get(Calendar.YEAR) - 1970 + " anos");
                                }
                            }

                            @Override
                            public void onErro(String mensagem) {
                                Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                            }
                        });

                        tvEstado.setText("Estado: " + (datahora < requisicao.getDatahora() ? "Agendado" : "Atendido"));
                        tvDataHora.setText("Data/Hora: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", locale).format(requisicao.getDatahora()));
                        tvEndereco.setText("Endereço: " + requisicao.getEndereco());
                        tvServicos.setText("\u2022 " + TextUtils.join("\n\u2022 ", requisicao.getServico()));

                        ivEndereco.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MapaActivity.class);
                                intent.putExtra("Requisicao", requisicao);
                                startActivity(intent);
                            }
                        });

                        TextView titulo = new TextView(getActivity());
                        titulo.setText("Informações do atendimento");
                        titulo.setPadding(10, 10, 10, 10);
                        titulo.setGravity(Gravity.CENTER);
                        titulo.setTextColor(Color.BLACK);
                        titulo.setTextSize(24);

                        alertDialogInformacoes.setView(layout).setCustomTitle(titulo).create().show();
                    }
                })
                .setCancelable(false)
                .setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create().show();
            }
        });
        return view;
    }
}
