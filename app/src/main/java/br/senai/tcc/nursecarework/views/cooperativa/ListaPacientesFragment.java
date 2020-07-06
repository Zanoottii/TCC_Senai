package br.senai.tcc.nursecarework.views.cooperativa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.ListaPacientesAdapter;
import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.views.requisicao.CadastroRequisicao1Activity;

public class PacientesFragment extends Fragment {
    private ListView lvLista;
    private List<Paciente> pacientes;
    private ServicosFirebase servicosFirebase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        servicosFirebase = ((CooperativaLogadoActivity) getActivity()).getServicosFirebase();

        lvLista = view.findViewById(R.id.lvLista);

        servicosFirebase.listarPacientes(new ServicosFirebase.ResultadoListener<List<Paciente>>() {
            @Override
            public void onSucesso(List<Paciente> listaPacientes) {
                pacientes = listaPacientes;
                lvLista.setAdapter(new ListaPacientesAdapter(getActivity(), pacientes));
            }

            @Override
            public void onErro(String mensagem) {
                Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
            }
        });

        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Paciente paciente = pacientes.get(i);
                new AlertDialog.Builder(getContext())
                        .setMessage("Selecione uma opção")
                        .setPositiveButton("Requisitar atendimento", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                Intent intent = new Intent(getContext(), CadastroRequisicao1Activity.class);
                                intent.putExtra("Paciente", paciente);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Ver mais informações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder alertDialogInformacoes = new AlertDialog.Builder(getContext());

                                View layout = getLayoutInflater().inflate(R.layout.alert_informacoes_paciente, null);
                                alertDialogInformacoes.setView(layout);

                                final CircularImageView civFoto = layout.findViewById(R.id.civFotoPacienteAlert);
                                TextView tvNome = layout.findViewById(R.id.tvNomePacienteAlert);
                                TextView tvCpf = layout.findViewById(R.id.tvCpfPacienteAlert);
                                TextView tvDataNascimento = layout.findViewById(R.id.tvDataNascimentoPacienteAlert);
                                TextView tvTelefone = layout.findViewById(R.id.tvTelefonePacienteAlert);
                                TextView tvEndereco = layout.findViewById(R.id.tvEnderecoPacienteAlert);

                                servicosFirebase.downloadFoto(paciente.getId(), new ServicosFirebase.ResultadoListener<Bitmap>() {
                                    @Override
                                    public void onSucesso(Bitmap foto) {
                                        civFoto.setImageBitmap(foto);
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                String endereco = paciente.getLogradouro() + ", " + paciente.getNumero() +
                                        (paciente.getComplemento().length() > 0 ? " " + paciente.getComplemento() : "") + " - " +
                                        paciente.getBairro() + " - " + paciente.getMunicipio() + " - " + paciente.getUf();

                                tvNome.setText("Nome: " + paciente.getNome() + " " + paciente.getSobrenome());
                                tvCpf.setText("CPF: " + paciente.getCpf());
                                tvDataNascimento.setText("Data de nascimento: " + paciente.getNascimento());
                                tvTelefone.setText("Telefone: " + paciente.getCelular());
                                tvEndereco.setText("Endereço: " + endereco);

                                TextView titulo = new TextView(getContext());
                                titulo.setText("Informações do paciente");
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
