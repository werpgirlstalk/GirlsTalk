package com.example.sai.girlstalk.dialogs;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sai.GirlsTalk.R;
import com.example.sai.girlstalk.viewModels.UserViewModel;

public class ForgotPasswordDialog extends DialogFragment
{
    public ForgotPasswordDialog() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View finalView = inflater.inflate(R.layout.fragment_forgot_password_dialog,container,false);

        setStyle(STYLE_NO_TITLE,R.style.ThemeOverlay_AppCompat_Dialog);

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        Button sendEmailBtn = finalView.findViewById(R.id.sendPasswordRecoveryBtn);
        Button cancelBtn = finalView.findViewById(R.id.cancelRecoveryDialogBtn);
        EditText recoveryEmail = finalView.findViewById(R.id.forgotPasswordEmail);

        sendEmailBtn.setOnClickListener( v ->
        {
            sendEmailBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            recoveryEmail.setEnabled(false);

            String email = recoveryEmail.getText().toString().trim();

            if (!email.isEmpty()) userViewModel.resetPassword(email).observe(this,isSuccessful ->
            {
                if (isSuccessful != null) if (isSuccessful)
                {
                    Toast.makeText(getContext(), "An Email Has Been Sent To You , Open It To Reset Your Password", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else {
                    sendEmailBtn.setEnabled(true);
                    cancelBtn.setEnabled(true);
                    recoveryEmail.setEnabled(true);
                }
            });
            else {
                sendEmailBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
                recoveryEmail.setEnabled(true);
                Toast.makeText(getContext(), "Please Enter An Email", Toast.LENGTH_LONG).show();
            }
        });

        cancelBtn.setOnClickListener(v -> dismiss());

        return finalView;
    }
}