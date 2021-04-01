package com.android.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    Button signUpButton;
    EditText userMail;
    EditText userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        //FirebaseAuth sınıfının referans olduğu nesneleri kullanmak için getInstance methodunu kullanıyoruz.
        auth=FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userMail.getText().toString();
                String parola = userPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Lütfen emailinizi giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(parola)){
                    Toast.makeText(getApplicationContext(),"Lütfen parolanızı giriniz",Toast.LENGTH_SHORT).show();
                }
                if (parola.length()<6){
                    Toast.makeText(getApplicationContext(),"Parola en az 6 haneli olmalıdır",Toast.LENGTH_SHORT).show();
                }

                //FirebaseAuth ile email,parola parametrelerini kullanarak yeni bir kullanıcı oluşturuyoruz.
                auth.createUserWithEmailAndPassword(email,parola)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //İşlem başarısız olursa kullanıcıya bir Toast mesajıyla bildiriyoruz.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Yetkilendirme Hatası",
                                            Toast.LENGTH_SHORT).show();
                                }

                                //İşlem başarılı olduğu takdir de giriş yapılıp MainActivity e yönlendiriyoruz.
                                else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                            }
                        });
            }
        });

    }
}