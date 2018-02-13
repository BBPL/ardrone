package com.parrot.freeflight.academy.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.FontUtils;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRangeDialog extends DialogFragment implements OnClickListener {
    private Date dateEnd = new Date();
    private Date dateStart = new Date();
    private TextView editDateFrom;
    private TextView editDateTo;
    private OnDismissListener onDismissListener;
    private OnClickListener onOkClickedListener;

    private class DateSetListenerImpl implements OnDateSetListener {
        private Date destDate;

        public DateSetListenerImpl(Date date) {
            this.destDate = date;
        }

        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            Calendar instance = Calendar.getInstance();
            instance.set(i, i2, i3);
            this.destDate.setTime(instance.getTime().getTime());
            DateRangeDialog.this.refreshUi();
        }
    }

    public Date getDateEnd() {
        return this.dateEnd;
    }

    public Date getDateStart() {
        return this.dateStart;
    }

    public void onClick(View view) {
        Calendar instance;
        switch (view.getId()) {
            case C0984R.id.dialog_dates_range_edit_datefrom /*2131362070*/:
                instance = Calendar.getInstance();
                instance.setTime(this.dateStart);
                new DatePickerDialog(getActivity(), new DateSetListenerImpl(this.dateStart), instance.get(1), instance.get(2), instance.get(5)).show();
                return;
            case C0984R.id.dialog_dates_range_edit_dateto /*2131362071*/:
                instance = Calendar.getInstance();
                instance.setTime(this.dateEnd);
                new DatePickerDialog(getActivity(), new DateSetListenerImpl(this.dateEnd), instance.get(1), instance.get(2), instance.get(5)).show();
                return;
            case C0984R.id.dialog_dates_range_button_ok /*2131362072*/:
                if (this.onOkClickedListener != null) {
                    this.onOkClickedListener.onClick(view);
                }
                dismiss();
                return;
            default:
                Toast.makeText(getActivity(), "Not implemented yet", 0).show();
                return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, C0984R.style.FreeFlightDialog_Dark);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0984R.layout.dialog_dates_range, viewGroup, false);
        FontUtils.applyFont(getActivity(), inflate);
        this.editDateFrom = (TextView) inflate.findViewById(C0984R.id.dialog_dates_range_edit_datefrom);
        this.editDateTo = (TextView) inflate.findViewById(C0984R.id.dialog_dates_range_edit_dateto);
        this.editDateFrom.setOnClickListener(this);
        this.editDateTo.setOnClickListener(this);
        inflate.findViewById(C0984R.id.dialog_dates_range_button_ok).setOnClickListener(this);
        refreshUi();
        return inflate;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.onDismissListener != null) {
            this.onDismissListener.onDismiss(dialogInterface);
        }
    }

    public void refreshUi() {
        DateFormat dateInstance = DateFormat.getDateInstance(2);
        if (this.dateStart != null) {
            this.editDateFrom.setText(dateInstance.format(this.dateStart));
        }
        if (this.dateEnd != null) {
            this.editDateTo.setText(dateInstance.format(this.dateEnd));
        }
    }

    public void setDateEnd(Date date) {
        this.dateEnd = new Date(date.getTime());
    }

    public void setDateStart(Date date) {
        this.dateStart = new Date(date.getTime());
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnOkClickedListener(OnClickListener onClickListener) {
        this.onOkClickedListener = onClickListener;
    }
}
