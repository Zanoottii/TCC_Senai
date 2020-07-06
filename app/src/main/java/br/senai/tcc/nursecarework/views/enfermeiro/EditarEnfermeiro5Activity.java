package br.senai.tcc.nursecarework.views.enfermeiro;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import br.senai.tcc.nursecarework.R;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.helpers.Usuario;
import br.senai.tcc.nursecarework.models.Enfermeiro;

public class EditarEnfermeiro5Activity extends AppCompatActivity {
    private CircularImageView civFoto;
    private ImageButton ibSelecionaFoto;
    private EditText etCoren;
    private ServicosFirebase servicosFirebase;
    private Enfermeiro enfermeiro;
    private Bitmap bitmap;
    private Usuario usuario;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte6);

        servicosFirebase = new ServicosFirebase(this);
        usuario = Usuario.getInstance();

        enfermeiro = (Enfermeiro) getIntent().getSerializableExtra("Enfermeiro");

        civFoto = findViewById(R.id.civFotoEnfermeiro);
        ibSelecionaFoto = findViewById(R.id.ibSelecionaFotoEnfermeiro);
        etCoren = findViewById(R.id.etCorenEnfermeiro);
        Button bSalvar = findViewById(R.id.bCadastrarEnfermeiro);

        etCoren.addTextChangedListener(new SimpleMaskTextWatcher(etCoren, new SimpleMaskFormatter("NNN-NNN")));

        civFoto.setImageBitmap(usuario.getFoto());
        etCoren.setText(enfermeiro.getCoren());
        bSalvar.setText("Salvar");

        ibSelecionaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditarEnfermeiro5Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditarEnfermeiro5Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    registerForContextMenu(ibSelecionaFoto);
                    openContextMenu(ibSelecionaFoto);
                }
            }
        });

        progress = new ProgressDialog(this);
        progress.setTitle("Carregando");
        progress.setMessage("Aguarde...");
        progress.setCancelable(false);

        findViewById(R.id.ivVoltarEnfermeiro6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarEnfermeiro5Activity.this, EditarEnfermeiro4Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                startActivity(intent);
                finish();
            }
        });

        bSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCoren.getText().toString().length() < 7) {
                    etCoren.setError("Preencha o número do Coren");
                    etCoren.requestFocus();
                } else {
                    enfermeiro.setCoren(etCoren.getText().toString());

                    progress.show();
                    servicosFirebase.gravarEnfermeiro(enfermeiro, new ServicosFirebase.ResultadoListener() {
                        @Override
                        public void onSucesso(Object objeto) {
                            if (bitmap != null) {
                                servicosFirebase.uploadFoto(bitmap, usuario.getUid(), new ServicosFirebase.ResultadoListener() {
                                    @Override
                                    public void onSucesso(Object objeto) {
                                        usuario.setFoto(bitmap);
                                        concluir();
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        progress.dismiss();
                                        Toast.makeText(EditarEnfermeiro5Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                concluir();
                            }
                        }

                        @Override
                        public void onErro(String mensagem) {
                            progress.dismiss();
                            Toast.makeText(EditarEnfermeiro5Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void concluir() {
        usuario.setEnfermeiro(enfermeiro);
        progress.dismiss();
        Toast.makeText(EditarEnfermeiro5Activity.this, "Salvo", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditarEnfermeiro5Activity.this, EnfermeiroLogadoActivity.class));
        finish();
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
