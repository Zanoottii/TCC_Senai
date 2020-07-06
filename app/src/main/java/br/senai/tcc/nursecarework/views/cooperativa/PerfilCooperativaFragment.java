package br.senai.tcc.nursecarework.views.cooperativa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.senai.tcc.nursecarework.models.Cooperativa;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.R;

public class PerfilCooperativaFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_cooperativa, container, false);

        Usuario usuario = Usuario.getInstance();
        Cooperativa cooperativa = usuario.getCooperativa();

        TextView tvEmail = view.findViewById(R.id.tvEmailPerfilCooperativa);
        TextView tvNome = view.findViewById(R.id.tvNomePerfilCooperativa);
        TextView tvCnpj = view.findViewById(R.id.tvCnpjPerfilCooperativa);
        TextView tvMunicipio = view.findViewById(R.id.tvMunicipioPerfilCooperativa);

        tvEmail.setText(usuario.getEmail());
        tvNome.setText("Nome: " + cooperativa.getNome());
        tvCnpj.setText("CNPJ: " + cooperativa.getCnpj());
        tvMunicipio.setText("Município: " + cooperativa.getMunicipio() + " - " + cooperativa.getUf());

        view.findViewById(R.id.bEditarPerfilCooperativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarCooperativaActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}
