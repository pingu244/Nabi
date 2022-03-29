package com.example.nabi.fragment.Home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.nabi.LoginActivity;
import com.example.nabi.R;
import com.google.firebase.auth.FirebaseAuth;

public class Mypage_logoutFragment extends DialogFragment implements View.OnClickListener {

    TextView logout, cancel;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.mypage_logoutfrag);

        logout = dialog.findViewById(R.id.mypage_logoutBtn);
        cancel = dialog.findViewById(R.id.mypage_logout_cancel);
        logout.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.mypage_logoutBtn:
                firebaseAuth =  FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                dismiss();  // fragment창 사라짐
                ActivityCompat.finishAffinity(getActivity());   // 모든 activity 끄기
                startActivity(new Intent(getActivity(), LoginActivity.class));  // 로그인 activity 켜기
                break;

            case R.id.mypage_logout_cancel:
                dismiss();
                break;

        }

    }
}
