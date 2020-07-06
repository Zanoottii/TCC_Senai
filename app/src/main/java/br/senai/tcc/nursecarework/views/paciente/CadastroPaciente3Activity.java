package br.senai.tcc.nursecarework.views.paciente;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.views.cooperativa.CooperativaLogadoActivity;
import br.senai.tcc.nursecarework.R;

public class CadastroPaciente3Activity extends AppCompatActivity {
    private CircularImageView civFoto;
    private ImageButton ibGaleria;
    private Paciente paciente;
    private ServicosFirebase servicosFirebase;
    private Bitmap bitmap;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_paciente_parte3);

        paciente = (Paciente) getIntent().getSerializableExtra("Paciente");

        servicosFirebase = new ServicosFirebase(this);

        civFoto = findViewById(R.id.civFotoPaciente);
        ibGaleria = findViewById(R.id.ibSelecionaFotoPaciente);

        ibGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CadastroPaciente3Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CadastroPaciente3Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    registerForContextMenu(ibGaleria);
                    openContextMenu(ibGaleria);
                }
            }
        });

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        findViewById(R.id.ivVoltarPaciente3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPaciente3Activity.this, CadastroPaciente2Activity.class);
                intent.putExtra("Paciente", paciente);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bCadastrarPaciente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap == null)
                    bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);

                progress.show();
                servicosFirebase.cadastrarPaciente(paciente, bitmap, new ServicosFirebase.ResultadoListener() {
                    @Override
                    public void onSucesso(Object objeto) {
                        progress.dismiss();
                        startActivity(new Intent(CadastroPaciente3Activity.this, CooperativaLogadoActivity.class));
                        finish();
                    }

                    @Override
                    public void onErro(String mensagem) {
                        progress.dismiss();
                        Toast.makeText(CadastroPaciente3Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuItem miGaleria = menu.add("Galeria");
        miGaleria.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data == null) {
                    return;
                }
                try {
                    bitmap = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData()), 300, 300, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    civFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                }
            }
        }
    }
}
