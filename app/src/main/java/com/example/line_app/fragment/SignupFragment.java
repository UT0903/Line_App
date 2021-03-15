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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;


public class SignupFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
    private void startLoading(){
        signup_progress_bar.setVisibility(View.VISIBLE);
        SignupBtn.setVisibility(View.GONE);
    }
    private void endLoading(){
        signup_progress_bar.setVisibility(View.GONE);
        SignupBtn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        signup_username = getView().findViewById(R.id.signup_username);
        signup_email = getView().findViewById(R.id.signup_email);
        signup_password = getView().findViewById(R.id.signup_password);
        SignupBtn = getView().findViewById(R.id.SignupBtn);
        signup_progress_bar = getView().findViewById(R.id.signup_progress_bar);
        SignupBtn.setOnClickListener(v -> submit());
        mAuth = FirebaseAuth.getInstance();
    }
    private FirebaseAuth mAuth;
    private TextInputEditText signup_username, signup_email, signup_password;
    private Button SignupBtn;
    private ProgressBar signup_progress_bar;
    public void submit(){

        if(signup_username.getText().toString().trim().equals("") || signup_email.getText().toString().trim().equals("") || signup_password.getText().toString().trim().equals("")){
            Toast.makeText(getContext(), "Above can't be null", Toast.LENGTH_SHORT).show();
            return;
        }
        startLoading();
        mAuth.createUserWithEmailAndPassword(signup_email.getText().toString(), signup_password.getText().toString())
                .addOnCompleteListener((Activity) getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in is successful
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(signup_username.getText().toString()).build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("tag", "User profile updated.");
                                            getActivity().finish();
                                            //endLoading();
                                        }
                                        else{
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            endLoading();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("tag", "User profile failed");
                        endLoading();
                    }
                });
    }
}

