package com.example.line_app.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.line_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class LoginFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        login_email = getView().findViewById(R.id.login_email);
        login_password = getView().findViewById(R.id.login_password);
        LoginBtn = getView().findViewById(R.id.LoginBtn);
        login_progress_bar = getView().findViewById(R.id.login_progress_bar);
        LoginBtn.setOnClickListener(v -> submit());
        mAuth = FirebaseAuth.getInstance();
    }
    private void startLoading(){
        login_progress_bar.setVisibility(View.VISIBLE);
        LoginBtn.setVisibility(View.GONE);
    }
    private void endLoading(){
        login_progress_bar.setVisibility(View.GONE);
        LoginBtn.setVisibility(View.VISIBLE);
    }
    private void submit(){
        if(login_email.getText().toString().trim().equals("") || login_password.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Above can't be null", Toast.LENGTH_SHORT).show();
            return;
        }
        startLoading();
        mAuth.signInWithEmailAndPassword(login_email.getText().toString(), login_password.getText().toString())
                .addOnCompleteListener((Activity) getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in is successful
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("tag", "User profile failed");
                        endLoading();
                    }
                });
    }
    private FirebaseAuth mAuth;
    private TextInputEditText login_email, login_password;
    private Button LoginBtn;
    private ProgressBar login_progress_bar;
}