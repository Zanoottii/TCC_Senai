package br.senai.tcc.nursecarework.views.enfermeiro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.R;

public class PerfilEnfermeiroFragment extends Fragment {
    private CircularImageView civFoto;
    private TextView tvEmail, tvNome, tvCpf, tvCelular, tvCoren, tvBanco, tvAgencia, tvConta, tvDistancia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_enfermeiro, container, false);

        Usuario usuario = Usuario.getInstance();
        Enfermeiro enfermeiro = usuario.getEnfermeiro();

        civFoto = view.findViewById(R.id.civFotoPerfilEnfermeiro);
        tvEmail = view.findViewById(R.id.tvEmailPerfilEnfermeiro);
        tvNome = view.findViewById(R.id.tvNomePerfilEnfermeiro);
        tvCpf = view.findViewById(R.id.tvCpfPerfilEnfermeiro);
        tvCelular = view.findViewById(R.id.tvCelularPerfilEnfermeiro);
        tvCoren = view.findViewById(R.id.tvCorenPerfilEnfermeiro);
        tvBanco = view.findViewById(R.id.tvBancoPerfilEnfermeiro);
        tvAgencia = view.findViewById(R.id.tvAgenciaPerfilEnfermeiro);
        tvConta = view.findViewById(R.id.tvContaPerfilEnfermeiro);
        tvDistancia = view.findViewById(R.id.tvDistanciaPerfilEnfermeiro);

        civFoto.setImageBitmap(usuario.getFoto());
        tvEmail.setText(usuario.getEmail());
        tvNome.setText("Nome: " + enfermeiro.getNome() + " " + enfermeiro.getSobrenome());
        tvCpf.setText("CPF: " + enfermeiro.getCpf());
        tvCelular.setText("Celular: " + enfermeiro.getCelular());
        tvCoren.setText("Coren: " + enfermeiro.getCoren());
        tvBanco.setText("Banco: " + enfermeiro.getBanco());
        tvAgencia.setText("Agência: " + enfermeiro.getAgencia());
        tvConta.setText("Conta: " + enfermeiro.getConta());
        tvDistancia.setText("Raio de atendimento: " + (int) enfermeiro.getDistancia() + " Km");

        view.findViewById(R.id.bEditarPerfilEnfermeiro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarEnfermeiro1Activity.class));
                getActivity().finish();
            }
        });

        return view;
    }

}
