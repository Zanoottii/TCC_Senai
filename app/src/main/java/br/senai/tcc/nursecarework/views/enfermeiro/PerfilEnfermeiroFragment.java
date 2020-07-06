package br.senai.tcc.nursecarework.views.enfermeiro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.models.Usuario;
import br.senai.tcc.nursecarework.R;

public class PerfilFragment extends Fragment {

    private CircularImageView imgUserEnfer;
    private TextView perfilEmailEnfer, perfilNomeEnfer, perfilSobrenomeEnfer, perfilCpfEnfer, perfilFoneEnfer, perfilBancoEnfer, perfilNcontaEnfer, perfilAgenciaEnfer, perfilNcoremEnfer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_enfermeiro, container, false);

        Usuario usuario = Usuario.getInstance();
        Enfermeiro enfermeiro = usuario.getEnfermeiro();

        imgUserEnfer = view.findViewById(R.id.imgUserEnfer);
        perfilEmailEnfer = view.findViewById(R.id.perfilEmailEnfer);
        perfilNomeEnfer = view.findViewById(R.id.perfilNomeEnfer);
        perfilSobrenomeEnfer = view.findViewById(R.id.perfilSobrenomeEnfer);
        perfilCpfEnfer = view.findViewById(R.id.perfilCpfEnfer);
        perfilFoneEnfer = view.findViewById(R.id.perfilFoneEnfer);
        perfilBancoEnfer = view.findViewById(R.id.perfilBancoEnfer);
        perfilNcontaEnfer = view.findViewById(R.id.perfilNcontaEnfer);
        perfilAgenciaEnfer = view.findViewById(R.id.perfilAgenciaEnfer);
        perfilNcoremEnfer = view.findViewById(R.id.perfilNcoremEnfer);

        imgUserEnfer.setImageBitmap(usuario.getFoto());
        perfilEmailEnfer.setText("Email: " + usuario.getEmail());
        perfilNomeEnfer.setText("Nome: " + enfermeiro.getNome());
        perfilSobrenomeEnfer.setText("Sobrenome: " + enfermeiro.getSobrenome());
        perfilCpfEnfer.setText("CPF: " + enfermeiro.getCpf());
        perfilFoneEnfer.setText("Telefone: " + enfermeiro.getCelular());
        perfilBancoEnfer.setText("Banco: " + enfermeiro.getBanco());
        perfilNcontaEnfer.setText("Conta: " + enfermeiro.getConta());
        perfilAgenciaEnfer.setText("Agencia: " + enfermeiro.getAgencia());
        perfilNcoremEnfer.setText("Coren: " + enfermeiro.getCoren());
        return view;
    }

}
