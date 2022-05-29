package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button btnLogar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView cadastrarText = findViewById(R.id.cadastrar_text_view);
        campoEmail = findViewById(R.id.login_email);
        campoSenha = findViewById(R.id.login_senha);
        btnLogar = findViewById(R.id.button_logar);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {

                        Usuario usuario = new Usuario();

                        usuario.setEmail(email);
                        usuario.setSenha(senha);

                        logarUsuario(usuario);
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cadastrarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    public void logarUsuario(Usuario usuario) {
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaPrincipal();
                } else {
                    String excecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email e senha não correspodem a um usuário cadastrado!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar o usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = auth.getCurrentUser();

        if (usuarioAtual != null) {
            abrirTelaPrincipal();
        }
    }
}