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
import com.example.nabi.fragment.PushNotification.PreferenceHelper;
import com.google.firebase.auth.FirebaseAuth;

import static com.facebook.FacebookSdk.getApplicationContext;

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
                PreferenceHelper.setStep(getApplicationContext(), 0);
                PreferenceHelper.setMeditate(getApplicationContext(), 0);

                firebaseAuth =  FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                dismiss();  // fragment??? ?????????
                ActivityCompat.finishAffinity(getActivity());   // ?????? activity ??????
                startActivity(new Intent(getActivity(), LoginActivity.class));  // ????????? activity ??????
                break;

            case R.id.mypage_logout_cancel:
                dismiss();
                break;

        }

    }
}
