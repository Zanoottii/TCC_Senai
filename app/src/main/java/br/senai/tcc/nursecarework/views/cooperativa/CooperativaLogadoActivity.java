package br.senai.tcc.nursecarework.views.cooperativa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.views.paciente.CadastroPaciente1Activity;
import br.senai.tcc.nursecarework.R;

public class CooperativaLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String barra;
    private Toolbar toolbar;
    private FloatingActionButton fabAdicionar;
    private ServicosFirebase servicosFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperativa_logado);
        toolbar = findViewById(R.id.tbCooperativa);
        setSupportActionBar(toolbar);

        servicosFirebase = new ServicosFirebase(this);

        fabAdicionar = findViewById(R.id.fabAdicionar);
        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CooperativaLogadoActivity.this, CadastroPaciente1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout_cooperativa);
        NavigationView navigationView = findViewById(R.id.nav_view_cooperativa);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Usuario usuario = Usuario.getInstance();
        View headerView = navigationView.getHeaderView(0);
        TextView tvEmailCooperativa = headerView.findViewById(R.id.tvEmailCooperativaLogado);
        tvEmailCooperativa.setText(usuario.getEmail());

        displaySelectedScreen(R.id.minhasRequisicoesCooperativa);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_cooperativa);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.verPerfilCooperativa:
                fabAdicionar.setVisibility(View.INVISIBLE);
                barra = "Meu perfil";
                fragment = new PerfilCooperativaFragment();
                break;
            case R.id.listaPacientesCooperativa:
                fabAdicionar.setVisibility(View.VISIBLE);
                barra = "Lista de pacientes";
                fragment = new ListaPacientesFragment();
                break;
            case R.id.minhasRequisicoesCooperativa:
                fabAdicionar.setVisibility(View.INVISIBLE);
                barra = "Minhas requisições";
                fragment = new MinhasRequisicoesFragment();
                break;
            case R.id.sairCooperativa:
                servicosFirebase.deslogar();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flLista, fragment);
            ft.commit();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_cooperativa);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public ServicosFirebase getServicosFirebase() {
        return servicosFirebase;
    }
}
