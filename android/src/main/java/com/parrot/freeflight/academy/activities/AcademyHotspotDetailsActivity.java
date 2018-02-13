package com.parrot.freeflight.academy.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Hotspot;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.FontUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mortbay.util.URIUtil;

public class AcademyHotspotDetailsActivity extends ParrotActivity implements OnClickListener {
    public static final String EXTRA_HOTSPOT_ID = "hotspot.id";
    public static final String EXTRA_HOTSPOT_OBJ = "hotspot.object";
    private TextView creation_date;
    private Button delete;
    private EditText description_edit;
    private ProgressDialog dialog;
    public Hotspot hotspot;
    private EditText name_edit;
    private RatingBar rating;
    private TextView user;

    class C10011 implements Runnable {
        C10011() {
        }

        public void run() {
            AcademyHotspotDetailsActivity.this.finish();
        }
    }

    class C10022 implements Runnable {
        C10022() {
        }

        public void run() {
            AcademyHotspotDetailsActivity.this.finish();
        }
    }

    private class ConfirmDeletionDialog extends AlertDialog implements OnClickListener {
        private Button cancel;
        private Button confirm;
        private Context context;

        protected ConfirmDeletionDialog(Context context) {
            super(context);
            this.context = context;
        }

        private void initListeners() {
            this.confirm.setOnClickListener(this);
            this.cancel.setOnClickListener(this);
        }

        private void initUi() {
            this.confirm = (Button) findViewById(C0984R.id.delete_confirm);
            this.cancel = (Button) findViewById(C0984R.id.delete_cancel);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case C0984R.id.delete_confirm /*2131361948*/:
                    new DeleteHotspotAsyncTask().execute(new Void[0]);
                    dismiss();
                    return;
                case C0984R.id.delete_cancel /*2131361949*/:
                    dismiss();
                    return;
                default:
                    return;
            }
        }

        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(C0984R.layout.academy_remove_hotspot_dialog);
            initUi();
            initListeners();
            onPostCreate(bundle);
        }

        protected void onPostCreate(Bundle bundle) {
            FontUtils.applyFont(this.context, (ViewGroup) findViewById(16908290));
        }
    }

    private class DeleteHotspotAsyncTask extends AsyncTask<Void, Void, Integer> {
        private DeleteHotspotAsyncTask() {
        }

        protected Integer doInBackground(Void... voidArr) {
            int i = 0;
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpDelete = new HttpDelete(AcademyHotspotDetailsActivity.this.getString(C0984R.string.http).concat(AcademyHotspotDetailsActivity.this.getString(C0984R.string.url_server)).concat(AcademyHotspotDetailsActivity.this.getString(C0984R.string.url_hotspots).concat(String.valueOf(AcademyHotspotDetailsActivity.this.hotspot.getId()).concat(URIUtil.SLASH))));
                httpDelete.setHeader("Authorization", "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2)));
                i = defaultHttpClient.execute(httpDelete).getStatusLine().getStatusCode();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return Integer.valueOf(i);
        }

        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            AcademyHotspotDetailsActivity.this.dialog.dismiss();
            AcademyHotspotDetailsActivity.this.showMessage(num.intValue());
        }

        protected void onPreExecute() {
            super.onPreExecute();
            AcademyHotspotDetailsActivity.this.dialog.show();
            AcademyHotspotDetailsActivity.this.dialog.setCancelable(false);
            AcademyHotspotDetailsActivity.this.dialog.setIndeterminate(true);
        }
    }

    private void enableDisableControls() {
        this.user.setVisibility(0);
        this.creation_date.setVisibility(0);
        findViewById(C0984R.id.creation_date_text).setVisibility(0);
        if (AcademyUtils.profile.getUser().getUsername().equals(this.hotspot.getUser().getUsername())) {
            this.delete.setVisibility(0);
        }
    }

    private void initActionBar() {
        ActionBar parrotActionBar = getParrotActionBar();
        parrotActionBar.initBackButton();
        parrotActionBar.setTitle(null);
    }

    private void initDialog() {
        this.dialog = new ProgressDialog(this, C0984R.style.FreeFlightDialog_Hotspot);
        this.dialog.setMessage(getString(C0984R.string.aa_ID000180).toUpperCase());
        this.dialog.setSubMessage(getString(C0984R.string.aa_ID000180));
    }

    private void initListeners() {
        this.delete.setOnClickListener(this);
    }

    private void initUi() {
        this.name_edit = (EditText) findViewById(C0984R.id.name_edit);
        this.description_edit = (EditText) findViewById(C0984R.id.description_edit);
        this.creation_date = (TextView) findViewById(C0984R.id.creation_date);
        this.user = (TextView) findViewById(C0984R.id.user);
        this.rating = (RatingBar) findViewById(C0984R.id.rating_bar);
        this.delete = (Button) findViewById(C0984R.id.delete_hotspot);
        setValues();
    }

    private void showMessage(int i) {
        switch (i) {
            case 204:
                showAlertDialog(getString(C0984R.string.aa_ID000182), getString(C0984R.string.aa_ID000178), new C10011());
                return;
            default:
                showAlertDialog(getString(C0984R.string.aa_ID000182), getString(C0984R.string.aa_ID000183), new C10022());
                return;
        }
    }

    public static void start(Context context, Hotspot hotspot) {
        Intent intent = new Intent(context, AcademyHotspotDetailsActivity.class);
        intent.putExtra(EXTRA_HOTSPOT_OBJ, hotspot);
        context.startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.delete_hotspot /*2131361996*/:
                new ConfirmDeletionDialog(this).show();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_academy_hotspot_details);
        this.hotspot = (Hotspot) getIntent().getSerializableExtra(EXTRA_HOTSPOT_OBJ);
        initActionBar();
        initUi();
        initListeners();
        initDialog();
        enableDisableControls();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    public void setValues() {
        this.name_edit.setText(this.hotspot.getName());
        this.description_edit.setText(this.hotspot.getDescription());
        this.creation_date.setText(this.hotspot.getCreation_date());
        this.user.setText(getResources().getString(C0984R.string.aa_ID000059).concat(" ").concat(this.hotspot.getUser().getUsername().toUpperCase()));
        this.rating.setProgress(this.hotspot.getGrade());
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        new Builder(this).setTitle(str).setMessage(str2).setCancelable(false).setNegativeButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create().show();
    }
}
