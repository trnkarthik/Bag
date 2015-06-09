package com.getmebag.bag.dialog;

import android.app.Dialog;

/**
* Created by karthiktangirala on 5/24/15.
*/
public interface DialogActionsListener {
    void setOnDialogNeutralButtonListener(String arg, Dialog dialog);

    void setOnDialogPositiveButtonListener(String arg);

    void setOnDialogNegativeButtonListener(Dialog dialog);
}
