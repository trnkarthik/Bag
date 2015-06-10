package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

public class BagBaseDialogFragment extends DialogFragment {

    private Boolean isBeingShown = false;

    @Inject
    public BagBaseDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((BagApplication) getActivity().getApplication()).inject(this);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isBeingShown) {
            return;
        }

        super.show(manager, tag);
        isBeingShown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isBeingShown = false;
        super.onDismiss(dialog);
    }
}
