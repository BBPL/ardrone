package com.parrot.freeflight.academy.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.parrot.freeflight.C0984R;
import java.util.ArrayList;

public class AutoCompleteTextViewDialog<T> extends AlertDialog {
    private int current = -1;
    private MyAutoCompleteTextView mAutoCompleteTextView;
    private Context mContext;
    private ArrayList<T> mList;
    private DialogListener mReadyListener;

    class C10371 implements OnItemClickListener {
        C10371() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            AutoCompleteTextViewDialog.this.current = i;
        }
    }

    class C10382 implements OnClickListener {
        C10382() {
        }

        public void onClick(View view) {
            if (AutoCompleteTextViewDialog.this.current != -1) {
                AutoCompleteTextViewDialog.this.mReadyListener.ready(AutoCompleteTextViewDialog.this.current);
            }
            AutoCompleteTextViewDialog.this.dismiss();
        }
    }

    public interface DialogListener {
        void ready(int i);
    }

    public AutoCompleteTextViewDialog(Context context, ArrayList<T> arrayList, DialogListener dialogListener) {
        super(context);
        this.mReadyListener = dialogListener;
        this.mContext = context;
        this.mList = new ArrayList();
        this.mList = arrayList;
    }

    public MyAutoCompleteTextView getmAutoCompleteTextView() {
        return this.mAutoCompleteTextView;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_spinner_dialog);
        this.mAutoCompleteTextView = (MyAutoCompleteTextView) findViewById(C0984R.id.dialog_spinner);
        this.mAutoCompleteTextView.setAdapter(new ArrayAdapter(this.mContext, 17367050, this.mList));
        this.mAutoCompleteTextView.setThreshold(1);
        this.mAutoCompleteTextView.setOnItemClickListener(new C10371());
        ((Button) findViewById(C0984R.id.dialogOK)).setOnClickListener(new C10382());
    }

    public void setOnShowListener(OnShowListener onShowListener) {
        super.setOnShowListener(onShowListener);
        getWindow().setSoftInputMode(5);
    }

    public void setmSpinner(MyAutoCompleteTextView myAutoCompleteTextView) {
        this.mAutoCompleteTextView = myAutoCompleteTextView;
    }
}
