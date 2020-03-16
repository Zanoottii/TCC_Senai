package br.senai.tcc.nursecarework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.mikhaellopez.circularimageview.CircularImageView;

public class CadastroPaciente4Activity extends AppCompatActivity {

    private ImageView voltar;
    private CircularImageView foto;
    private Button concluido, galeria;
    private static final int codigo_camera = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte4);

        voltar = findViewById(R.id.pacienteVoltar4);
        foto = findViewById(R.id.fotoPaciente);
        concluido = findViewById(R.id.btnCadPaciente4);
        galeria = findViewById(R.id.btnGaleria2);

        //Botão para voltar para a tele de cadastro parte 3
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente4Activity.this, CadastroPaciente3Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para concluir o cadastro do enfermeiro
        concluido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente4Activity.this, CooperativaLogadoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //botão paara abrir a galeria
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aqui ele verifica se o usuário já deu a permissão....
                if (ActivityCompat.checkSelfPermission(CadastroPaciente4Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //e caso ainda não tenha dado, ele solicita...
                    ActivityCompat.requestPermissions(CadastroPaciente4Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    //ao executar novamente ele irá verificar que já foi dado a permissão e irá executar a funcionalidade normalmente
                    registerForContextMenu(galeria);
                    openContextMenu(galeria);
                }
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem carregarImg = menu.add("Galeria");
        carregarImg.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selecionarGaleria();
                return false;
            }
        });
    }

    public void selecionarGaleria() {
        Intent abrirGaleria = new Intent(Intent.ACTION_GET_CONTENT);
        abrirGaleria.setType("image/*");
        abrirGaleria.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(abrirGaleria, codigo_camera);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == codigo_camera && resultCode == RESULT_OK) {
            foto.setImageURI(data.getData());
        }
    }
}
