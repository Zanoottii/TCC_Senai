package br.senai.tcc.nursecarework.views.enfermeiro;

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
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.helpers.ServicosFirebase;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro6Activity extends AppCompatActivity {
    private CircularImageView civFoto;
    private ImageButton ibSelecionaFoto;
    private EditText etCoren;
    private ServicosFirebase servicosFirebase;
    private Enfermeiro enfermeiro;
    private String email, senha;
    private Bitmap bitmap;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte6);

        servicosFirebase = new ServicosFirebase(this);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        civFoto = findViewById(R.id.civFotoEnfermeiro);
        ibSelecionaFoto = findViewById(R.id.ibSelecionaFotoEnfermeiro);
        etCoren = findViewById(R.id.etCorenEnfermeiro);

        etCoren.addTextChangedListener(new SimpleMaskTextWatcher(etCoren, new SimpleMaskFormatter("NNN-NNN")));

        ibSelecionaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CadastroEnfermeiro6Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CadastroEnfermeiro6Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                Intent intent = new Intent(CadastroEnfermeiro6Activity.this, CadastroEnfermeiro5Activity.class);
                intent.putExtra("Enfermeiro", enfermeiro);
                intent.putExtra("email", email);
                intent.putExtra("senha", senha);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.bCadastrarEnfermeiro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCoren.getText().toString().length() < 7) {
                    etCoren.setError("Preencha o número do Coren");
                    etCoren.requestFocus();
                } else {
                    if (bitmap == null)
                        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);

                    enfermeiro.setCoren(etCoren.getText().toString());

                    progress.show();
                    servicosFirebase.cadastrarEnfermeiro(email, senha, enfermeiro, bitmap, new ServicosFirebase.ResultadoListener() {
                        @Override
                        public void onSucesso(Object objeto) {
                            progress.dismiss();
                            startActivity(new Intent(CadastroEnfermeiro6Activity.this, EnfermeiroLogadoActivity.class));
                            finish();
                        }

                        @Override
                        public void onErro(String mensagem) {
                            progress.dismiss();
                            Toast.makeText(CadastroEnfermeiro6Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
