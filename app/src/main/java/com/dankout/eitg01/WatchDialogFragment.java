package com.dankout.eitg01;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Activity using this class needs to implement DialogOnClickListener
 **/
public class WatchDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Bevaka")
                .setPositiveButton(R.string.alert_dialog_watcher_ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((DialogOnlickListener)getActivity()).onPositiveClick();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_watcher_cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((DialogOnlickListener)getActivity()).onNegativeClick();
                    }
                })
                .create();
    }
}
