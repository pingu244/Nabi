package com.example.nabi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class JoinActivity extends AppCompatActivity {

    EditText emailText, passwordText, passwordcheckText, name;
    Button joinBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.joinEmail);
        passwordText = findViewById(R.id.joinPassword);
        passwordcheckText = findViewById(R.id.joinPasswordCheck);
        name = findViewById(R.id.joinName);
        joinBtn = findViewById(R.id.joinBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 가입정보 가져오기
                String email = emailText.getText().toString().trim();
                String pwd = passwordText.getText().toString().trim();
                String pwdcheck = passwordcheckText.getText().toString().trim();

                createAccount(email, pwd, pwdcheck);



            }
        });




        // 로그인하기 텍스트
        TextView backtoLogin = findViewById(R.id.backtoLogin);
        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void createAccount(String email, String pwd, String pwdcheck){
        final ProgressDialog mDialog = new ProgressDialog(this);
        mDialog.setMessage("가입중입니다...");
        mDialog.show();
        try {
            if(pwd.equals(pwdcheck)){
                firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener
                        (this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // 가입 성공시
                                if(task.isSuccessful()) {
                                    mDialog.dismiss();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String nname = name.getText().toString().trim();

                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                    HashMap<Object, String> hashMap = new HashMap<>();

                                    hashMap.put("uid", uid);
                                    hashMap.put("email", email);
                                    hashMap.put("name", nname);
                                    hashMap.put("gloomyWeather", "-1");

                                    db.collection("users").document(uid)
                                            .set(hashMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("join", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("join", "Error writing document", e);
                                                }
                                            });

                                    //가입이 이루어져을시 가입 화면을 빠져나감.
                                    Intent intent = new Intent(JoinActivity.this, OnBoardingActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(JoinActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                } else{ // 가입 실패시
                                    mDialog.dismiss();
                                    Toast.makeText(JoinActivity.this, "다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                    return;  //해당 메소드 진행을 멈추고 빠져나감.
                                }
                            }
                        });
            }// 비밀번호확인이 다를때
            else{
                mDialog.dismiss();
                Toast.makeText(JoinActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IllegalArgumentException e)
        {
            mDialog.dismiss();
            Toast.makeText(JoinActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

}
