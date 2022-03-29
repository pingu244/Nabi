package com.example.nabi.fragment.Home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.nabi.LoginActivity;
import com.example.nabi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Mypage_deleteFragment extends DialogFragment implements View.OnClickListener{

    TextView delete, cancel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.mypage_deletefrag);

        delete = dialog.findViewById(R.id.mypage_deleteBtn);
        cancel = dialog.findViewById(R.id.mypage_delete_cancel);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mypage_deleteBtn:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference docRef = db.collection("users").document(user.getUid());
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                dismiss();  // fragment창 사라짐
                                                ActivityCompat.finishAffinity(getActivity());   // 모든 activity 끄기
                                                startActivity(new Intent(getActivity(), LoginActivity.class));  // 로그인 activity 켜기
                                            }
                                        }
                                    });

                                }
                            }
                        });
                break;
            case R.id.mypage_delete_cancel:
                dismiss();
                break;
        }
    }
}
