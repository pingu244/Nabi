package com.example.nabi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class EditPasswordFrag extends DialogFragment {

    EditText email;
    TextView transmit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.editpasswordfrag);

        email = dialog.findViewById(R.id.passwordEdit_email);
        transmit = dialog.findViewById(R.id.passwordEditBtn);

        transmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "이메일로 링크를 발송했습니다", Toast.LENGTH_LONG).show();

                                    firebaseAuth.signOut();
                                    dismiss();  // fragment창 사라짐
                                    ActivityCompat.finishAffinity(getActivity());   // 모든 activity 끄기
                                    startActivity(new Intent(getActivity(), LoginActivity.class));  // 로그인 activity 켜기
                                }
                            }
                        });
            }
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
