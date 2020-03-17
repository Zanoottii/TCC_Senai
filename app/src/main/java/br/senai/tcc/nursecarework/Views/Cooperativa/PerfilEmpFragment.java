package br.senai.tcc.nursecarework.Views.Cooperativa;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.senai.tcc.nursecarework.R;

public class PerfilEmpFragment extends Fragment {

    private Button btEditar;

    public PerfilEmpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_emp, container, false);
        btEditar = view.findViewById(R.id.btnEditarPerfilEmp);
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return view;
    }
}
