package br.senai.tcc.nursecarework.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.senai.tcc.nursecarework.models.Cooperativa;
import br.senai.tcc.nursecarework.models.Enfermeiro;
import br.senai.tcc.nursecarework.models.Paciente;
import br.senai.tcc.nursecarework.models.Requisicao;
import br.senai.tcc.nursecarework.views.cooperativa.CadastroCooperativa2Activity;
import br.senai.tcc.nursecarework.views.enfermeiro.CadastroEnfermeiro2Activity;
import br.senai.tcc.nursecarework.views.enfermeiro.CadastroEnfermeiro6Activity;
import br.senai.tcc.nursecarework.views.MainActivity;

@SuppressWarnings("unchecked")
public class ServicosFirebase implements FirebaseAuth.AuthStateListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private Activity activity;
    private Usuario usuario;

    public ServicosFirebase(Activity activity) {
        usuario = Usuario.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (activity != null) {
            this.activity = activity;
            mAuth.addAuthStateListener(this);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (!isLogado() &&
                !(activity instanceof MainActivity) &&
                !(activity instanceof CadastroEnfermeiro2Activity) &&
                !(activity instanceof CadastroEnfermeiro6Activity) &&
                !(activity instanceof CadastroCooperativa2Activity)) {
            Usuario.clearInstance();
            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public interface ResultadoListener<T> {
        void onSucesso(T objeto);

        void onErro(String mensagem);
    }

    public boolean isLogado() {
        user = mAuth.getCurrentUser();
        return user != null;
    }

    private void iniciarBanco() {
        if (db == null) db = FirebaseFirestore.getInstance();
        if (mStorageRef == null) mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void logar(String email, String senha, final ResultadoListener resultado) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isLogado()) {
                                carregarUsuario(new ResultadoListener() {
                                    @Override
                                    public void onSucesso(Object objeto) {
                                        resultado.onSucesso(null);
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        resultado.onErro(mensagem);
                                    }
                                });
                            }
                        } else {
                            FirebaseAuthException firebaseAuthException = (FirebaseAuthException) task.getException();
                            if (firebaseAuthException != null) {
                                resultado.onErro(firebaseAuthException.getErrorCode());
                            } else {
                                resultado.onErro("Erro desconhecido");
                            }
                        }
                    }
                });
    }

    public void deslogar() {
        mAuth.signOut();
    }

    public void downloadFoto(String id, final ResultadoListener resultado) {
        iniciarBanco();
        StorageReference fileRef = mStorageRef.child(id + ".png");
        fileRef.getBytes(1048576).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                resultado.onSucesso(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                resultado.onErro("Erro ao descarregar a foto");
            }
        });
    }

    public void uploadFoto(Bitmap foto, String id, final ResultadoListener resultado) {
        iniciarBanco();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 90, stream);
        StorageReference fileRef = mStorageRef.child(id + ".png");
        UploadTask uploadTask = fileRef.putBytes(stream.toByteArray());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                resultado.onSucesso(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                resultado.onErro("Erro ao enviar a foto");
            }
        });
    }

    public void carregarUsuario(final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("usuarios")
                .whereEqualTo("id_firebase", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                usuario.setUid(documentSnapshot.getId());
                                usuario.setEmail(user.getEmail());
                                String tipo = documentSnapshot.getString("tipo");
                                if ("enfermeiros".equals(tipo)) {
                                    carregarEnfermeiro(usuario.getUid(), new ResultadoListener<Enfermeiro>() {
                                        @Override
                                        public void onSucesso(Enfermeiro enfermeiro) {
                                            usuario.setEnfermeiro(enfermeiro);
                                            downloadFoto(usuario.getUid(), new ResultadoListener<Bitmap>() {
                                                @Override
                                                public void onSucesso(Bitmap foto) {
                                                    usuario.setFoto(foto);
                                                    resultado.onSucesso(null);
                                                }

                                                @Override
                                                public void onErro(String mensagem) {
                                                    resultado.onErro(mensagem);
                                                }
                                            });

                                        }

                                        @Override
                                        public void onErro(String mensagem) {
                                            resultado.onErro(mensagem);
                                        }
                                    });
                                } else if ("cooperativas".equals(tipo)) {
                                    carregarCooperativa(usuario.getUid(), new ResultadoListener<Cooperativa>() {
                                        @Override
                                        public void onSucesso(Cooperativa cooperativa) {
                                            usuario.setCooperativa(cooperativa);
                                            resultado.onSucesso(null);
                                        }

                                        @Override
                                        public void onErro(String mensagem) {
                                            resultado.onErro(mensagem);
                                        }
                                    });
                                } else {
                                    resultado.onErro("Este usuário não é um enfermeiro ou cooperativa");
                                }
                            } else {
                                resultado.onErro("Usuário logado não é mais válido");
                            }
                        } else {
                            resultado.onErro("Não foi possível carregar as informações do usuário");
                        }
                    }
                });
    }

    public void carregarPaciente(String id, final ResultadoListener resultado) {
        db.collection("pacientes")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                resultado.onSucesso(documentSnapshot.toObject(Paciente.class));
                            } else {
                                resultado.onErro("Não existe informações deste paciente");
                            }
                        } else {
                            resultado.onErro("Não foi possível carregar as informações do paciente");
                        }
                    }
                });
    }

    public void carregarEnfermeiro(String id, final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("enfermeiros")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                resultado.onSucesso(documentSnapshot.toObject(Enfermeiro.class));
                            } else {
                                resultado.onErro("Não existe informações deste enfermeiro");
                            }
                        } else {
                            resultado.onErro("Não foi possível carregar as informações do enfermeiro");
                        }
                    }
                });
    }

    public void carregarCooperativa(String id, final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("cooperativas")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                resultado.onSucesso(documentSnapshot.toObject(Cooperativa.class));
                            } else {
                                resultado.onErro("Não existe informações desta cooperativa");
                            }
                        } else {
                            resultado.onErro("Não foi possível carregar as informações da cooperativa");
                        }
                    }
                });
    }

    public void cadastrarPaciente(final Paciente paciente, final Bitmap foto, final ResultadoListener resultado) {
        gravarUsuario("pacientes", "", new ResultadoListener<String>() {
            @Override
            public void onSucesso(final String id) {
                paciente.setId(id);
                gravarPaciente(paciente, new ResultadoListener() {
                    @Override
                    public void onSucesso(Object objeto) {
                        uploadFoto(foto, id, new ResultadoListener() {
                            @Override
                            public void onSucesso(Object objeto) {
                                resultado.onSucesso(null);
                            }

                            @Override
                            public void onErro(String mensagem) {
                                db.collection("usuarios").document(id).delete();
                                resultado.onErro(mensagem);
                            }
                        });
                    }

                    @Override
                    public void onErro(String mensagem) {
                        db.collection("usuarios").document(id).delete();
                        resultado.onErro(mensagem);
                    }
                });
            }

            @Override
            public void onErro(String mensagem) {
                resultado.onErro(mensagem);
            }
        });
    }

    public void cadastrarEnfermeiro(final String email, final String senha, final Enfermeiro enfermeiro, final Bitmap foto, final ResultadoListener resultado) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isLogado()) {
                                gravarUsuario("enfermeiros", user.getUid(), new ResultadoListener<String>() {
                                    @Override
                                    public void onSucesso(final String id) {
                                        usuario.setUid(id);
                                        gravarEnfermeiro(enfermeiro, new ResultadoListener() {
                                            @Override
                                            public void onSucesso(Object objeto) {
                                                uploadFoto(foto, id, new ResultadoListener() {
                                                    @Override
                                                    public void onSucesso(Object objeto) {
                                                        usuario.setEmail(email);
                                                        usuario.setEnfermeiro(enfermeiro);
                                                        usuario.setFoto(foto);
                                                        resultado.onSucesso(null);
                                                    }

                                                    @Override
                                                    public void onErro(String mensagem) {
                                                        db.collection("usuarios").document(id).delete();
                                                        usuario.setUid(null);
                                                        user.delete();
                                                        resultado.onErro(mensagem);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onErro(String mensagem) {
                                                db.collection("usuarios").document(id).delete();
                                                usuario.setUid(null);
                                                user.delete();
                                                resultado.onErro(mensagem);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        user.delete();
                                        resultado.onErro(mensagem);
                                    }
                                });
                            } else {
                                user.delete();
                                resultado.onErro("Erro ao logar como usuário");
                            }
                        } else {
                            resultado.onErro("Erro ao criar o usuário");
                        }
                    }
                });
    }

    public void cadastrarCooperativa(final String email, final String senha, final Cooperativa cooperativa, final ResultadoListener resultado) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isLogado()) {
                                gravarUsuario("cooperativas", user.getUid(), new ResultadoListener<String>() {
                                    @Override
                                    public void onSucesso(final String id) {
                                        usuario.setUid(id);
                                        gravarCooperativa(cooperativa, new ResultadoListener() {
                                            @Override
                                            public void onSucesso(Object objeto) {
                                                usuario.setEmail(email);
                                                usuario.setCooperativa(cooperativa);
                                                resultado.onSucesso(null);
                                            }

                                            @Override
                                            public void onErro(String mensagem) {
                                                db.collection("usuarios").document(id).delete();
                                                usuario.setUid(null);
                                                user.delete();
                                                resultado.onErro(mensagem);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onErro(String mensagem) {
                                        user.delete();
                                        resultado.onErro(mensagem);
                                    }
                                });
                            } else {
                                user.delete();
                                resultado.onErro("Erro ao logar como usuário");
                            }
                        } else {
                            resultado.onErro("Erro ao criar o usuário");
                        }
                    }
                });
    }

    public void cadastrarRequisicao(final Requisicao requisicao, final ResultadoListener resultado) {
        iniciarBanco();
        requisicao.setCooperativa(usuario.getUid());
        db.collection("requisicoes")
                .add(requisicao)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        resultado.onSucesso(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultado.onErro("Erro ao agendar o atendimento");
                    }
                });
    }

    private void gravarUsuario(String tipo, String id, final ResultadoListener resultado) {
        iniciarBanco();
        Map<String, Object> mUsuario = new HashMap<>();
        mUsuario.put("tipo", tipo);
        mUsuario.put("id_firebase", id);
        db.collection("usuarios")
                .add(mUsuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        resultado.onSucesso(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultado.onErro("Erro ao gravar o usuário");
                    }
                });
    }

    private void gravarPaciente(Paciente paciente, final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("pacientes")
                .document(paciente.getId())
                .set(paciente)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resultado.onSucesso("paciente");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultado.onErro("Erro ao gravar o paciente");
                    }
                });
    }

    public void gravarEnfermeiro(Enfermeiro enfermeiro, final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("enfermeiros")
                .document(usuario.getUid())
                .set(enfermeiro)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resultado.onSucesso("enfermeiro");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultado.onErro("Erro ao gravar o enfermeiro");
                    }
                });
    }

    public void gravarCooperativa(Cooperativa cooperativa, final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("cooperativas")
                .document(usuario.getUid())
                .set(cooperativa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resultado.onSucesso("cooperativa");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resultado.onErro("Erro ao gravar a cooperativa");
                    }
                });
    }

    public void obterId(Object tipo, final ResultadoListener resultado) {
        iniciarBanco();
        Query query;
        if (tipo instanceof Paciente) {
            query = db.collection("pacientes").whereEqualTo("cpf", ((Paciente) tipo).getCpf());
        } else if (tipo instanceof Enfermeiro) {
            query = db.collection("enfermeiros").whereEqualTo("cpf", ((Enfermeiro) tipo).getCpf());
        } else if (tipo instanceof Cooperativa) {
            query = db.collection("cooperativas").whereEqualTo("cnpj", ((Cooperativa) tipo).getCnpj());
        } else {
            resultado.onErro("Tipo desconhecido");
            return;
        }
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && querySnapshot.isEmpty()) {
                                resultado.onSucesso("");
                            } else {
                                if (querySnapshot != null)
                                    resultado.onSucesso(querySnapshot.getDocuments().get(0).getId());
                            }
                        } else {
                            resultado.onErro("Não foi possível verificar se a informação do usuário já existe");
                        }
                    }
                });
    }

    public void listarRequisicaoEnfermeiro(String id, final ResultadoListener resultado) {
        iniciarBanco();
        Enfermeiro enfermeiro = usuario.getEnfermeiro();
        final int distanciaMaxima = enfermeiro.getDistancia() * 1000;
        final Location localEnfermeiro = new Location("");
        localEnfermeiro.setLatitude(enfermeiro.getLatitude());
        localEnfermeiro.setLongitude(enfermeiro.getLongitude());
        long datahora = Calendar.getInstance().getTimeInMillis();
        Query query = id.isEmpty() ?
                db.collection("requisicoes").whereEqualTo("enfermeiro", "").whereGreaterThan("datahora", datahora).orderBy("datahora") :
                db.collection("requisicoes").whereEqualTo("enfermeiro", id).orderBy("datahora", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Requisicao> requisicoes = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Requisicao requisicao = document.toObject(Requisicao.class);
                            Location localPaciente = new Location("");
                            localPaciente.setLatitude(requisicao.getLatitude());
                            localPaciente.setLongitude(requisicao.getLongitude());
                            int distancia = (int) localEnfermeiro.distanceTo(localPaciente);
                            if (distancia <= distanciaMaxima) {
                                requisicao.setId(document.getId());
                                requisicao.setDistancia(distancia);
                                requisicoes.add(requisicao);
                            }
                        }
                    }
                    resultado.onSucesso(requisicoes);
                } else {
                    resultado.onErro("Erro ao consultar");
                }
            }
        });
    }

    public void listarRequisicaoCooperativa(final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("requisicoes")
                .whereEqualTo("cooperativa", usuario.getUid())
                .orderBy("datahora", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Requisicao> requisicoes = new ArrayList<>();
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    Requisicao requisicao = document.toObject(Requisicao.class);
                                    if (!"0".equals(requisicao.getEnfermeiro())) {
                                        requisicao.setId(document.getId());
                                        requisicoes.add(requisicao);
                                    }
                                }
                            }
                            resultado.onSucesso(requisicoes);
                        } else {
                            resultado.onErro("Erro ao consultar");
                        }
                    }
                });
    }

    public void listarPacientes(final ResultadoListener resultado) {
        iniciarBanco();
        db.collection("pacientes")
                .orderBy("nome")
                .orderBy("sobrenome")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Paciente> pacientes = new ArrayList<>();
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    Paciente paciente = document.toObject(Paciente.class);
                                    paciente.setId(document.getId());
                                    pacientes.add(paciente);
                                }
                            }
                            resultado.onSucesso(pacientes);
                        } else {
                            resultado.onErro("Erro ao consultar");
                        }
                    }
                });
    }

    public void aceitarRequisicao(Requisicao requisicao, final ResultadoListener resultado) {
        iniciarBanco();
        if (Calendar.getInstance().getTimeInMillis() < requisicao.getDatahora()) {
            db.collection("requisicoes")
                    .document(requisicao.getId())
                    .update("enfermeiro", usuario.getUid())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            resultado.onSucesso(null);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resultado.onErro("Erro ao aceitar");
                        }
                    });
        } else {
            resultado.onErro("Não é possivel aceitar");
        }
    }

    public void cancelarRequisicao(Requisicao requisicao, final ResultadoListener resultado) {
        iniciarBanco();
        if (Calendar.getInstance().getTimeInMillis() < requisicao.getDatahora()) {
            db.collection("requisicoes")
                    .document(requisicao.getId())
                    .update("enfermeiro", usuario.getEnfermeiro() == null ? "0" : "")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            resultado.onSucesso(null);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resultado.onErro("Erro ao cancelar");
                        }
                    });
        } else {
            resultado.onErro("Não é possivel cancelar");
        }
    }
}
