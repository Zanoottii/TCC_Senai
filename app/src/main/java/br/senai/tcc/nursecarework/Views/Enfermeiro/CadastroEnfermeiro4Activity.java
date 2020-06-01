package br.senai.tcc.nursecarework.Views.Enfermeiro;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.SimpleMaskTextWatcher;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import br.senai.tcc.nursecarework.Models.Enfermeiro;
import br.senai.tcc.nursecarework.Models.ServicosFirebase;
import br.senai.tcc.nursecarework.R;

public class CadastroEnfermeiro4Activity extends AppCompatActivity {

    private ImageView voltar;
    private CircularImageView foto;
    private Button concluido, galeria;
    private EditText edtNumCoren;
    private Enfermeiro enfermeiro;
    private String email, senha;
    private ServicosFirebase servicosFirebase;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_enfermeiro_parte4);

        servicosFirebase = new ServicosFirebase(this);

        Intent intent = getIntent();
        enfermeiro = (Enfermeiro) intent.getSerializableExtra("Enfermeiro");
        email = intent.getStringExtra("email");
        senha = intent.getStringExtra("senha");

        voltar = findViewById(R.id.voltar5);
        concluido = findViewById(R.id.btnCadConcluido);
        galeria = findViewById(R.id.btnGaleria);
        foto = findViewById(R.id.fotoCadastro);
        edtNumCoren = findViewById(R.id.numCoren);

        //mascara para o campo do NumCoren
        SimpleMaskFormatter simpleMaskCoren = new SimpleMaskFormatter("NNN-NNN");
        SimpleMaskTextWatcher maskCoren = new SimpleMaskTextWatcher(edtNumCoren, simpleMaskCoren);
        edtNumCoren.addTextChangedListener(maskCoren);

        //Botão para voltar para a tele de cadastro parte 3
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroEnfermeiro4Activity.this, CadastroEnfermeiro3Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //Botão para concluir o cadastro do enfermeiro
        concluido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNumCoren.getText().toString().isEmpty()) {
                    edtNumCoren.setError("Preencha o numero do Coren");
                    edtNumCoren.requestFocus();
                } else {
                    if (bitmap == null)
                        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.foto);
                    enfermeiro.setCoren(edtNumCoren.getText().toString());
                    servicosFirebase.cadastrarEnfermeiro(email, senha, enfermeiro, bitmap, new ServicosFirebase.ResultadoListener() {
                        @Override
                        public void onSucesso(Object objeto) {
                            startActivity(new Intent(CadastroEnfermeiro4Activity.this, EnfermeiroLogadoActivity.class));
                            finish();
                        }

                        @Override
                        public void onErro(String mensagem) {
                            Toast.makeText(CadastroEnfermeiro4Activity.this, mensagem, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //botão paara abrir a galeria
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aqui ele verifica se o usuário já deu a permissão....
                if (ActivityCompat.checkSelfPermission(CadastroEnfermeiro4Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //e caso ainda não tenha dado, ele solicita...
                    ActivityCompat.requestPermissions(CadastroEnfermeiro4Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        startActivityForResult(abrirGaleria, 1);
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
                    foto.setImageBitmap(bitmap);
                } catch (IOException e) {
                }
            }
        }
    }
}
