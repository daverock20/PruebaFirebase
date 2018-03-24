package com.example.daale.pruebafirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnIniciado,btnCerrado;
    TextView tvEmail,tvPassword;
    ProgressBar pbProgreso;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciado = findViewById(R.id.btningresar);
        btnCerrado = findViewById(R.id.btncerrar);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);

        pbProgreso = (ProgressBar) findViewById(R.id.pbProgress);
        pbProgreso.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null){
                    //no esta logeado
                    btnCerrado.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Correcto y No logueado", Toast.LENGTH_LONG).show();
                }else{
                    //esta logeado
                    btnCerrado.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Correcto y Logueado", Toast.LENGTH_LONG).show();
                }
            }
        };

       // Toast.makeText(this, "Hola mundo" , Toast.LENGTH_LONG).show();
        btnIniciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Ingresando", Toast.LENGTH_LONG).show();

                Ingresar();

            }
        });
        btnCerrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Cerrando Sesión", Toast.LENGTH_LONG).show();

                mAuth.signOut();

            }
        });
    }

    private void Ingresar() {
        String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            pbProgreso.setVisibility(View.VISIBLE);
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       //Toast.makeText(getApplicationContext(), "Correcto", Toast.LENGTH_LONG).show();
                   }else {
                       Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_LONG).show();
                   }
                   pbProgreso.setVisibility(View.INVISIBLE);
               }
           });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            mAuth.removeAuthStateListener(listener);
        }
    }
}
