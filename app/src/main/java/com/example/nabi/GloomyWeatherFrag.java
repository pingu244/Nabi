package com.example.nabi;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class GloomyWeatherFrag extends DialogFragment {

    ImageButton cloudy, halfCloudy, sunny, rainy, snowy;
    Button start;
    int select = -1;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gloomyweatherfrag);

        cloudy = dialog.findViewById(R.id.gloomy_cloudy);
        halfCloudy = dialog.findViewById(R.id.gloomy_halfCloudy);
        sunny = dialog.findViewById(R.id.gloomy_sunny);
        rainy = dialog.findViewById(R.id.gloomy_rainy);
        snowy = dialog.findViewById(R.id.gloomy_snowy);
        start = dialog.findViewById(R.id.gloomy_start);
        start.setEnabled(false);

        sunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 0;
                start.setEnabled(true);
            }
        });

        halfCloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 1;
                start.setEnabled(true);
            }
        });

        cloudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 2;
                start.setEnabled(true);
            }
        });

        rainy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 3;
                start.setEnabled(true);
            }
        });

        snowy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 4;
                start.setEnabled(true);
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("gloomyWeather", select);
                docRef.set(hashMap, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "설정 되었습니다.", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                        });
            }
        });



        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
