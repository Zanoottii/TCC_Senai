package br.senai.tcc.nursecarework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class EnfermeiroLogadoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String barra;
    private Toolbar toolbar;
    private ListView lvListaOpcoes;
    private ArrayList<InfoClientes> listaOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermeiro_logado);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        lvListaOpcoes = findViewById(R.id.listaClientesDisp);
        listaOpcoes = getListaOpcoes();

        ListaClientesAdapter adapter = new ListaClientesAdapter(EnfermeiroLogadoActivity.this, listaOpcoes);
        lvListaOpcoes.setAdapter(adapter);

        lvListaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final InfoClientes infoClientessObj = listaOpcoes.get(i);
                final AlertDialog.Builder alertConfig = new AlertDialog.Builder(view.getContext());
                alertConfig.setTitle("Informações do cliente")
                        .setMessage("Nome: " + infoClientessObj.getNomeCliente() +
                                "\nSobrenome: " + infoClientessObj.getSobrenomeCliente() +
                                "\nData de nascimento: " + infoClientessObj.getDataNascCliente() +
                                "\nServiço: " + infoClientessObj.getTipoServicoCliente())
                        .setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(EnfermeiroLogadoActivity.this, "Cliente aceito", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Rejeitar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(EnfermeiroLogadoActivity.this, "Cliente rejeitado", Toast.LENGTH_SHORT).show();
                            }
                        });
                final AlertDialog alert = alertConfig.create();
                alert.show();
            }
        });
    }

    private ArrayList<InfoClientes> getListaOpcoes() {
        ArrayList<InfoClientes> infoClientesArray = new ArrayList<>();
        InfoClientes infoClientesObj = new InfoClientes();

        infoClientesObj.setNomeCliente("Carlos");
        infoClientesObj.setSobrenomeCliente("Zanotti");
        infoClientesObj.setDataNascCliente("03/03/2020");
        infoClientesObj.setTipoServicoCliente("retirada de pontos");
        infoClientesArray.add(infoClientesObj);

        return infoClientesArray;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.verPerfil) {
            PerfilFragment frag = new PerfilFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fraClientesDisp, frag);
            barra = "Meu perfil";
            fragmentTransaction.commit();
        } else if (id == R.id.meusClientes) {
            MeusClientesFragment frag = new MeusClientesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fraClientesDisp, frag);
            barra = "Meus clientes";
            fragmentTransaction.commit();
        } else if (id == R.id.clientesDisponiveis) {
            ClientesDispFragment frag = new ClientesDispFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fraClientesDisp, frag);
            barra = "Clientes disponiveis";
            fragmentTransaction.commit();
        } else if (id == R.id.sairLogin) {
            Intent intent = new Intent(EnfermeiroLogadoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar.setTitle(barra);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
