package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;

/**
 * Created by karthiktangirala on 5/20/15.
 */
public class LocationZipCodeDialogFragment extends DialogFragment {

    private EditText locationDialogEditText;
    private Button locationDialogCancelButton;
    private Button locationDialogConfirmButton;
    public DialogActionsListener dialogActionsListener;

    @Inject
    public LocationZipCodeDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((BagApplication) getActivity().getApplication()).inject(this);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.location_zipcode_dialog_layout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
        locationDialogConfirmButton = (Button) dialog.findViewById(R.id.location_dialog_confirm_button);
        locationDialogCancelButton = (Button) dialog.findViewById(R.id.location_dialog_cancel_button);
        locationDialogEditText = (EditText) dialog.findViewById(R.id.location_dialog_edittext);

        locationDialogConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(locationDialogEditText.getText())) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dialogActionsListener.setOnDialogPositiveButtonListener(locationDialogEditText.getText().toString());
                    dismiss();
                } else {
                    locationDialogEditText.setError(v.getContext().getString(R.string.please_enter_a_valid_zipcode));
                }
            }
        });

        locationDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionsListener.setOnDialogNegativeButtonListener(getDialog());
            }
        });

        return dialog;
    }

}
