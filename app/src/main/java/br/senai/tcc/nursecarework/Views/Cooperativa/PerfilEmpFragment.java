package br.senai.tcc.nursecarework.Views.Cooperativa;

import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import br.senai.tcc.nursecarework.Models.Cooperativa;
import br.senai.tcc.nursecarework.Models.Usuario;
import br.senai.tcc.nursecarework.R;

public class PerfilEmpFragment extends Fragment {

    private Button btEditar;
    private CircularImageView fotoPerfilEmp;
    private TextView perfilEmailEmp, nomePerfilEmp, cnpjPerfilEmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil_emp, container, false);

        Usuario usuario = Usuario.getInstance();
        Cooperativa cooperativa = usuario.getCooperativa();

        btEditar = view.findViewById(R.id.btnEditarPerfilEmp);
        fotoPerfilEmp = view.findViewById(R.id.fotoPerfilEmp);
        perfilEmailEmp = view.findViewById(R.id.perfilEmailEmp);
        nomePerfilEmp = view.findViewById(R.id.nomePerfilEmp);
        cnpjPerfilEmp = view.findViewById(R.id.cnpjPerfilEmp);


        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //fotoPerfilEmp.setImageBitmap(usuario.getFoto());
        perfilEmailEmp.setText(usuario.getEmail());
        nomePerfilEmp.setText("Nome: " + cooperativa.getNome());
        cnpjPerfilEmp.setText("Cnpj: " + cooperativa.getCnpj());

        return view;
    }
}
