package br.senai.tcc.nursecarework.views.cooperativa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.senai.tcc.nursecarework.helpers.ListaRequisicoesCooperativaAdapter;
import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;

public class MinhasRequisicoesFragment extends Fragment {
    private ListView lvLista;
    private List<Requisicao> requisicoes;
    private ListaRequisicoesCooperativaAdapter adapter;
    private ServicosFirebase servicosFirebase;
    private long datahora;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        servicosFirebase = ((CooperativaLogadoActivity) getActivity()).getServicosFirebase();

        lvLista = view.findViewById(R.id.lvLista);

        servicosFirebase.listarRequisicaoCooperativa(new ServicosFirebase.ResultadoListener<List<Requisicao>>() {
            @Override
            public void onSucesso(List<Requisicao> listaRequisicoes) {
                requisicoes = listaRequisicoes;
                datahora = Calendar.getInstance().getTimeInMillis();
                adapter = new ListaRequisicoesCooperativaAdapter(getActivity(), requisicoes, datahora);
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
                    alertDialogOpcao.setPositiveButton("Cancelar requisição", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            servicosFirebase.cancelarRequisicao(requisicao, new ServicosFirebase.ResultadoListener() {
                                @Override
                                public void onSucesso(Object objeto) {
                                    requisicoes.remove(i);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Requisição cancelada", Toast.LENGTH_SHORT).show();
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
                        View layout = getLayoutInflater().inflate(R.layout.alert_informacoes_requisicao_cooperativa, null);
                        AlertDialog.Builder alertDialogInformacoes = new AlertDialog.Builder(getActivity());

                        ConstraintLayout clEnfermeiro = layout.findViewById(R.id.clEnfermeiroRequisicaoCooperativaAlert);
                        final CircularImageView civFotoPaciente = layout.findViewById(R.id.civFotoPacienteRequisicaoCooperativaAlert);
                        final CircularImageView civFotoEnfermeiro = layout.findViewById(R.id.civFotoEnfermeiroRequisicaoCooperativaAlert);
                        TextView tvEstado = layout.findViewById(R.id.tvEstadoRequisicaoCooperativaAlert);
                        TextView tvDataHora = layout.findViewById(R.id.tvDataHoraRequisicaoCooperativaAlert);
                        final TextView tvNomePaciente = layout.findViewById(R.id.tvNomePacienteRequisicaoCooperativaAlert);
                        final TextView tvCpfPaciente = layout.findViewById(R.id.tvCpfPacienteRequisicaoCooperativaAlert);
                        final TextView tvNomeEnfermeiro = layout.findViewById(R.id.tvNomeEnfermeiroRequisicaoCooperativaAlert);
                        final TextView tvCorenEnfermeiro = layout.findViewById(R.id.tvCorenEnfermeiroRequisicaoCooperativaAlert);
                        TextView tvServicos = layout.findViewById(R.id.tvServicosRequisicaoCooperativaAlert);

                        if (!TextUtils.isEmpty(requisicao.getEnfermeiro())) {
                            servicosFirebase.downloadFoto(requisicao.getEnfermeiro(), new ServicosFirebase.ResultadoListener<Bitmap>() {
                                @Override
                                public void onSucesso(Bitmap foto) {
                                    civFotoEnfermeiro.setImageBitmap(foto);
                                }

                                @Override
                                public void onErro(String mensagem) {
                                    Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                }
                            });

                            servicosFirebase.carregarEnfermeiro(requisicao.getEnfermeiro(), new ServicosFirebase.ResultadoListener<Enfermeiro>() {
                                @Override
                                public void onSucesso(Enfermeiro enfermeiro) {
                                    tvNomeEnfermeiro.setText(enfermeiro.getNome());
                                    tvCorenEnfermeiro.setText("COREN: " + enfermeiro.getCoren());
                                }

                                @Override
                                public void onErro(String mensagem) {
                                    Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                                }
                            });

                            tvEstado.setText("Estado: " + (datahora < requisicao.getDatahora() ? "Agendado" : "Atendido"));
                            clEnfermeiro.setVisibility(View.VISIBLE);
                        } else {
                            tvEstado.setText("Estado: " + (datahora < requisicao.getDatahora() ? "Aguardando aceitação" : "Não atendido"));
                            clEnfermeiro.setVisibility(View.GONE);
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
                                tvCpfPaciente.setText("CPF: " + paciente.getCpf());
                            }

                            @Override
                            public void onErro(String mensagem) {
                                Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
                            }
                        });

                        tvDataHora.setText("Data/Hora: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "BR")).format(requisicao.getDatahora()));
                        tvServicos.setText("\u2022 " + TextUtils.join("\n\u2022 ", requisicao.getServico()));

                        TextView titulo = new TextView(getActivity());
                        titulo.setText("Informações da requisição");
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
