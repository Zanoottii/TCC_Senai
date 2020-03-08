package br.senai.tcc.nursecarework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class EmpresaLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String barra;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ListView lvListaOpcoes;
    private ArrayList<InfoPacientes> listaOpcoes;
    private ArrayAdapter<InfoPacientes> listaOpcoesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_logado);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.adicionarPaciente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmpresaLogadoActivity.this, CadastroPaciente1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = findViewById(R.id.nav_view2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        lvListaOpcoes = findViewById(R.id.listaMeusPacientes);
        listaOpcoes = getListaOpcoes();

        ListaPacientesAdapter adapter = new ListaPacientesAdapter(EmpresaLogadoActivity.this, listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoPacientes infoPacientesObj = listaOpcoes.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());

                alertConfig.setMessage("Selecione uma opção")
                        .setPositiveButton("Remover paciente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listaOpcoesAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Ver informações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());

                                View layoutInformacoes = getLayoutInflater().inflate(R.layout.alert_info_pacientes, null);
                                builder2.setView(layoutInformacoes);

                                final TextView txtNome = layoutInformacoes.findViewById(R.id.nomePacienteAlert);
                                final TextView txtSobrenome = layoutInformacoes.findViewById(R.id.sobrenomePacienteAlert);
                                final TextView txtNasc = layoutInformacoes.findViewById(R.id.dataNascPacienteALert);
                                final TextView txtServico = layoutInformacoes.findViewById(R.id.tipoServicoAlert);

                                txtNome.setText("Nome: " + infoPacientesObj.getNome());
                                txtSobrenome.setText("Sobrenome: " + infoPacientesObj.getSobrenome());
                                txtNasc.setText("Data de nascimento: " + infoPacientesObj.getDataNasc());
                                txtServico.setText("Tipo de serviço: " + infoPacientesObj.getTipoServico());

                                final AlertDialog alert2 = builder2.create();
                                alert2.setTitle("Informações do paciente");
                                alert2.show();
                            }
                        })
                        .setNeutralButton("Editar informações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
    }

    private ArrayList<InfoPacientes> getListaOpcoes() {
        ArrayList<InfoPacientes> infoPacientesArray = new ArrayList<>();
        InfoPacientes infoPacientesObj = new InfoPacientes();

        infoPacientesObj.setNome("Jonathan");
        infoPacientesObj.setSobrenome("Araujo");
        infoPacientesObj.setDataNasc("10/01/2003");
        infoPacientesObj.setTipoServico("retirada de pontos");
        infoPacientesArray.add(infoPacientesObj);

        InfoPacientes infoPacientesObj2 = new InfoPacientes();

        infoPacientesObj2.setNome("Carlos");
        infoPacientesObj2.setSobrenome("Zanotti");
        infoPacientesObj2.setDataNasc("10/01/2003");
        infoPacientesObj2.setTipoServico("retirada de pontos");
        infoPacientesArray.add(infoPacientesObj2);

        return infoPacientesArray;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.verPerfilEmp) {
            fab.setVisibility(View.INVISIBLE);
            PerfilEmpFragment frag = new PerfilEmpFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragMeusPacientes, frag);
            barra = "Meu perfil";
            fragmentTransaction.commit();
        } else if (id == R.id.meusPacientes) {
            fab.setVisibility(View.VISIBLE);
            MeusPacientesFragment frag = new MeusPacientesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragMeusPacientes, frag);
            barra = "Meus pacientes";
            fragmentTransaction.commit();
        } else if (id == R.id.pacientesAceitos) {
            fab.setVisibility(View.INVISIBLE);
            PacientesAceitosFragment frag = new PacientesAceitosFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragMeusPacientes, frag);
            barra = "Pacientes aceitos";
            fragmentTransaction.commit();
        } else if (id == R.id.pacientesPendentes) {
            fab.setVisibility(View.INVISIBLE);
            PacientesPendentesFragment frag = new PacientesPendentesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragMeusPacientes, frag);
            barra = "Pacientes pendentes";
            fragmentTransaction.commit();
        } else if (id == R.id.sairLogin2) {
            Intent intent = new Intent(EmpresaLogadoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
