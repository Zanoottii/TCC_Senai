package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.Models.Usuario;
import br.senai.tcc.nursecarework.R;

public class EnfermeiroLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String barra;
    private Toolbar toolbar;
    private ServicosFirebase servicosFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermeiro_logado);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        servicosFirebase = new ServicosFirebase(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Usuario usuario = Usuario.getInstance();
        View headerView = navigationView.getHeaderView(0);
        CircularImageView imgUserEnfermeiro = headerView.findViewById(R.id.imgUserEnfermeiro);
        TextView emailEnfermeiro = headerView.findViewById(R.id.emailEnfermeiro);
        imgUserEnfermeiro.setImageBitmap(usuario.getFoto());
        emailEnfermeiro.setText(usuario.getEmail());

        displaySelectedScreen(R.id.pacientesDisponiveis);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {
            case R.id.verPerfil:
                barra = "Meu foto";
                fragment = new PerfilFragment();
                break;

            case R.id.meusPacientes:
                barra = "Meus Paciente";
                fragment = new MeusPacientesFragment();
                break;

            case R.id.pacientesDisponiveis:
                barra = "Paciente disponiveis";
                fragment = new PacientesDispFragment();
                break;

            case R.id.sairLogin:
                servicosFirebase.deslogar();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fraPacientesDisp, fragment);
            ft.commit();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;

    }

    public ServicosFirebase getServicosFirebase() {
        return servicosFirebase;
    }
}
