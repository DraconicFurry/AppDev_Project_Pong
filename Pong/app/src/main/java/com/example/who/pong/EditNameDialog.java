package com.example.who.pong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditNameDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Name:").setView(R.layout.dialog_edit_name);

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText mEditText = ((EditText) getDialog().findViewById(R.id.edit_text_name));
                HighscorePreferences.setName(getActivity(), mEditText.getText().toString());
                MenuActivity act = (MenuActivity) getActivity();
                act.refreshText();
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
