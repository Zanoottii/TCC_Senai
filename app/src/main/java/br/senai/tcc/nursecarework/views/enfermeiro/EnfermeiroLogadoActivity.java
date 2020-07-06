package br.senai.tcc.nursecarework.views.enfermeiro;

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

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.R;

public class EnfermeiroLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String barra;
    private Toolbar toolbar;
    private ServicosFirebase servicosFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermeiro_logado);
        toolbar = findViewById(R.id.tbEnfermeiro);
        setSupportActionBar(toolbar);

        servicosFirebase = new ServicosFirebase(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_enfermeiro);
        NavigationView navigationView = findViewById(R.id.nav_view_enfermeiro);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Usuario usuario = Usuario.getInstance();
        View headerView = navigationView.getHeaderView(0);
        CircularImageView civFoto = headerView.findViewById(R.id.civFotoEnfermeiroLogado);
        TextView tvEmailEnfermeiro = headerView.findViewById(R.id.tvEmailEnfermeiroLogado);
        civFoto.setImageBitmap(usuario.getFoto());
        tvEmailEnfermeiro.setText(usuario.getEmail());

        displaySelectedScreen(R.id.meusAtendimentosEnfermeiro);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_enfermeiro);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.verPerfilEnfermeiro:
                barra = "Meu perfil";
                fragment = new PerfilEnfermeiroFragment();
                break;
            case R.id.meusAtendimentosEnfermeiro:
                barra = "Meus atendimentos";
                fragment = new MeusAtendimentosFragment();
                break;
            case R.id.requisicoesDisponiveisEnfermeiro:
                barra = "Requisições disponíveis";
                fragment = new RequisicoesDisponiveisFragment();
                break;
            case R.id.sairEnfermeiro:
                servicosFirebase.deslogar();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flLista, fragment);
            ft.commit();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_enfermeiro);
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
