package com.example.nabi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginBtn, googleBtn, facebooktestBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth =  FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        googleBtn = findViewById(R.id.googleLogin);

        // 이미 로그인되어있으면 로그인창 생략하는 코드
        if (firebaseAuth.getCurrentUser() != null)
            updateUI(firebaseAuth.getCurrentUser());


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String pwd = passwordText.getText().toString().trim();
                try{
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        updateUI(firebaseAuth.getCurrentUser());
                                    }else{
                                        Toast.makeText(LoginActivity.this,"다시 시도해주세요",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (IllegalArgumentException e)
                {
                    Toast.makeText(LoginActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 구글로 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        facebooktestBtn = findViewById(R.id.facebooktestBtn);
        // 페이스북으로 로그인
        facebooktestBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });

        // 비밀번호 재설정
        TextView pwdReset = findViewById(R.id.pwdReset);
        pwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText et = new EditText(LoginActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("비밀번호 재설정")
                        .setMessage("이메일을 입력해주세요")
                        .setView(et); //제목
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.sendPasswordResetEmail(et.getText().toString()).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "이메일로 링크를 발송했습니다", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        });
                builder.show();
            }
        });

        TextView goJoin = findViewById(R.id.goJoin);
        goJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    // 구글 로그인시 필요한 코드들
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
            }
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            String email = user.getEmail();
                            String name = user.getDisplayName();
                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("name", name);

                            db.collection("users").document(uid).set(hashMap);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // ....구글 로그인시 필요한 코드들 끝


    // 페이스북 로그인 필요한 코드들
    private void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("login", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("login", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("login", "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("login", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            String email = user.getEmail();
                            String name = user.getDisplayName();
                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("name", name);
                            db.collection("users").document(uid).set(hashMap);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    //....페이스북 로그인 필요한 코드들 끝


    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }




}
