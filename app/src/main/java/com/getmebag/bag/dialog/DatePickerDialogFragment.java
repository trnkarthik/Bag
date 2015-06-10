package com.getmebag.bag.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.app.BagApplication;
import com.getmebag.bag.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.text.TextUtils.isEmpty;
import static com.getmebag.bag.utils.DateUtils.MM_DD_YYYY;
import static com.getmebag.bag.utils.DateUtils.calendarToDate;
import static com.getmebag.bag.utils.DateUtils.dateToCalendar;
import static com.getmebag.bag.utils.DateUtils.dateToString;
import static com.getmebag.bag.utils.DateUtils.stringToDate;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by karthiktangirala on 6/5/15.
 */
public class DatePickerDialogFragment extends BagBaseDialogFragment {

    private static final String FIELD_SELECTED_DATE =
            "PhoneNumberDialogFragment.SelectedPhoneNumber";

    private String selectedDate;

    public DialogActionsListener dialogActionsListener;

    @InjectView(R.id.datepicker_dialog_calendar)
    DatePicker datePicker;

    @InjectView(R.id.datepicker_dialog_confirm_button)
    Button datePickerButton;

    @InjectView(R.id.datepicker_dialog_skip_for_now)
    TextView datePickerSkipButton;

    @Inject
    public DatePickerDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ((BagApplication) getActivity().getApplication()).inject(this);

        final Bundle bundle = getArguments();
        selectedDate = bundle.getString(FIELD_SELECTED_DATE);

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.datepicker_dialog_layout);

        ButterKnife.inject(this, dialog);

        final Calendar calendar = dateToCalendar(stringToDate(selectedDate, DateUtils.MM_DD_YYYY));
        if (calendar != null) {
            datePicker.updateDate(calendar.get(YEAR),
                    calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        }
        datePicker.setMaxDate(new Date().getTime());

        datePickerSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogActionsListener.setOnDialogNeutralButtonListener("", getDialog());
                showAlertDialog(getActivity(), getActivity()
                        .getString(R.string.user_birthday_add_later ));
            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                dialogActionsListener
                        .setOnDialogNeutralButtonListener(
                                dateToString(calendarToDate(currentCalendar), MM_DD_YYYY), dialog);
            }
        });
        return dialog;

    }

    private void showAlertDialog(final Context context, final String message) {
        CommonAlertDialogFragment commonAlertDialogFragment = new CommonAlertDialogFragment.Builder()
                .setMessage(message)
                .setNeutralButtonText(context.getString(R.string.dialog_ok))
                .setDialogDismissOnNeutralButtonClick(true)
                .setCanceledOnTouchOutside(false)
                .build();

        commonAlertDialogFragment.show(getActivity().getSupportFragmentManager(),
                getString(R.string.dialog_common_alert));
    }


    public static class Builder {

        private String selectedDate;

        public Builder setSelectedDate(String selectedDate) {
            this.selectedDate = selectedDate;
            return this;
        }

        public DatePickerDialogFragment build() {
            DatePickerDialogFragment fragment = new DatePickerDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(FIELD_SELECTED_DATE, isEmpty(selectedDate)
                    ? "" : selectedDate);
            fragment.setArguments(bundle);
            return fragment;
        }

    }

}
