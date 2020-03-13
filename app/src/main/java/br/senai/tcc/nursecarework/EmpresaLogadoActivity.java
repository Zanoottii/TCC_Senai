package br.senai.tcc.nursecarework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import java.util.ArrayList;

public class EmpresaLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String barra;
    private Toolbar toolbar;
    private FloatingActionButton fab;

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

        displaySelectedScreen(R.id.meusPacientes);

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

    private void displaySelectedScreen(int itemId){

        Fragment fragment = null;

        switch (itemId){
            case R.id.verPerfilEmp:
                fab.setVisibility(View.INVISIBLE); //deixar o floating button ivisivel
                barra = "Meu perfil";
                fragment = new PerfilEmpFragment();
                break;

            case R.id.meusPacientes:
                fab.setVisibility(View.VISIBLE);//deixar o floating button vísivel
                barra = "Meus pacientes";
                fragment = new MeusPacientesFragment();
                break;

            case R.id.pacientesAceitos:
                fab.setVisibility(View.INVISIBLE);//deixar o floating button ivisivel
                barra = "Pacientes aceitos";
                fragment = new PacientesAceitosFragment();
                break;

            case R.id.pacientesPendentes:
                fab.setVisibility(View.INVISIBLE);//deixar o floating button ivisivel
                barra = "Pacientes pendentes";
                fragment = new PacientesPendentesFragment();
                break;

            case R.id.sairLogin2:
                Intent intent = new Intent(EmpresaLogadoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragMeusPacientes, fragment);
            ft.commit();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
}
