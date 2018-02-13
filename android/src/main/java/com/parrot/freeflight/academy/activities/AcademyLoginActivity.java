package com.parrot.freeflight.academy.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.api.Academy;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.model.Profile;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.GuestSpaceActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.service.FlightUploadService;
import com.parrot.freeflight.service.FlightUploadService.LocalBinder;
import com.parrot.freeflight.ui.ActionBar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpVersions;

public class AcademyLoginActivity extends ParrotActivity implements OnClickListener, OnCheckedChangeListener {
    private static final int SHOW_ADACEMY_DELAY = 2000;
    private OnTouchListener OnTouchOutOfFocusListener = new C10096();
    private ActionBar actionBar;
    private AlertDialog alertDialog;
    private Button authenticate;
    private ConnectAsyncTask connectTask;
    private ARDroneEngine droneEngine;
    private String email = null;
    private Point firstPointTouch = new Point();
    private FlightUploadService flightUploadService;
    private FlightUploadServiceConnection flightUploadServiceConnection;
    private Button forgotpassword;
    private Handler handler;
    private EditText login_edit;
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private EditText password_edit;
    private RadioGroup password_save;
    private String responseString;
    private Runnable showAcademy = new C10074();
    private long showAcademyTime;
    private Button signup;
    private ImageButton why_sign_up;

    class C10074 implements Runnable {
        C10074() {
        }

        public void run() {
            AcademyLoginActivity.this.showAcademyTime = 0;
            AcademyLoginActivity.this.startActivity(new Intent(AcademyLoginActivity.this, AcademyActivity.class));
            AcademyLoginActivity.this.finish();
        }
    }

    class C10096 implements OnTouchListener {
        C10096() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionIndex() == 0) {
                switch (motionEvent.getAction()) {
                    case 0:
                        AcademyLoginActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                        AcademyLoginActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                        break;
                    case 1:
                        if (!(AcademyLoginActivity.this.getCurrentFocus() == null || AcademyLoginActivity.this.getCurrentFocus() == view || AcademyLoginActivity.this.movedEnough)) {
                            ((InputMethodManager) AcademyLoginActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(AcademyLoginActivity.this.getCurrentFocus().getWindowToken(), 2);
                        }
                        AcademyLoginActivity.this.movedEnough = false;
                        break;
                    case 2:
                        if (Math.pow((double) (motionEvent.getX(0) - ((float) AcademyLoginActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) AcademyLoginActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) AcademyLoginActivity.this.movementPrecision, 2.0d)) {
                            AcademyLoginActivity.this.movedEnough = true;
                            break;
                        }
                        break;
                }
            }
            return true;
        }
    }

    private class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConnectAsyncTask() {
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                Profile profile = Academy.getProfile(AcademyLoginActivity.this, new AcademyCredentials(AcademyUtils.login, AcademyUtils.password));
                if (profile != null) {
                    AcademyUtils.profile = profile;
                    AcademyUtils.isConnectedAcademy = true;
                    AcademyUtils.saveCredentials(AcademyLoginActivity.this);
                } else {
                    AcademyUtils.isConnectedAcademy = false;
                }
                if (AcademyLoginActivity.this.flightUploadService != null) {
                    AcademyLoginActivity.this.flightUploadService.onAutoUploadPermissionChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void voidR) {
            AcademyLoginActivity.this.testConnection();
        }

        protected void onPreExecute() {
            AcademyUtils.isConnectedAcademy = false;
        }
    }

    private class FlightUploadServiceConnection implements ServiceConnection {
        private FlightUploadServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AcademyLoginActivity.this.flightUploadService = ((LocalBinder) iBinder).getService();
            if (AcademyLoginActivity.this.flightUploadService != null) {
                AcademyLoginActivity.this.flightUploadService.onAutoUploadPermissionChanged();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            AcademyLoginActivity.this.flightUploadService = null;
        }
    }

    private class RetrieveAsyncTask extends AsyncTask<Void, Void, Integer> {
        private RetrieveAsyncTask() {
        }

        protected Integer doInBackground(Void... voidArr) {
            int i = 0;
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPost = new HttpPost(AcademyLoginActivity.this.getString(C0984R.string.http).concat(AcademyLoginActivity.this.getString(C0984R.string.url_server).concat(AcademyLoginActivity.this.getString(C0984R.string.url_password_retrieve))));
                httpPost.setHeader("Accept-Language", Locale.getDefault().getLanguage());
                List arrayList = new ArrayList();
                arrayList.add(new BasicNameValuePair("email", AcademyLoginActivity.this.email));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPost);
                try {
                    AcademyLoginActivity.this.responseString = new JSONObject(EntityUtils.toString(execute.getEntity(), "UTF-8")).getJSONArray("email").getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i = execute.getStatusLine().getStatusCode();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            } catch (ClientProtocolException e3) {
                e3.printStackTrace();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            return Integer.valueOf(i);
        }

        protected void onPostExecute(Integer num) {
            AcademyLoginActivity.this.showAlertDialog(AcademyLoginActivity.this.getString(C0984R.string.aa_ID000143), num.intValue() == 200 ? AcademyLoginActivity.this.getString(C0984R.string.aa_ID000144) : AcademyLoginActivity.this.responseString, null);
            AcademyLoginActivity.this.email = null;
        }
    }

    protected class myTextWatcher implements TextWatcher {
        private int id;

        public myTextWatcher(int i) {
            this.id = i;
        }

        public void afterTextChanged(Editable editable) {
            switch (this.id) {
                case C0984R.id.login_edit /*2131361816*/:
                    AcademyUtils.login = AcademyLoginActivity.this.login_edit.getText().toString();
                    return;
                case C0984R.id.password_edit /*2131361819*/:
                    AcademyUtils.password = AcademyLoginActivity.this.password_edit.getText().toString();
                    return;
                case C0984R.id.input_email /*2131361825*/:
                    AcademyLoginActivity.this.email = editable.toString();
                    return;
                default:
                    return;
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    }

    private void hideKeyboard() {
        try {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    private void initActionBar() {
        this.actionBar = getParrotActionBar();
        this.actionBar.setTitle(getString(C0984R.string.aa_ID000020).toUpperCase());
        this.actionBar.initHomeButton();
    }

    private void initListeners() {
        this.authenticate.setOnClickListener(this);
        this.signup.setOnClickListener(this);
        this.forgotpassword.setOnClickListener(this);
        this.password_save.setOnCheckedChangeListener(this);
        this.why_sign_up.setOnClickListener(this);
        this.login_edit.addTextChangedListener(new myTextWatcher(this.login_edit.getId()));
        this.password_edit.addTextChangedListener(new myTextWatcher(this.password_edit.getId()));
    }

    private void initUI() {
        this.authenticate = (Button) findViewById(C0984R.id.authenticate);
        this.signup = (Button) findViewById(C0984R.id.signup);
        this.forgotpassword = (Button) findViewById(C0984R.id.forgotpassword);
        this.password_save = (RadioGroup) findViewById(C0984R.id.password_save);
        this.login_edit = (EditText) findViewById(C0984R.id.login_edit);
        this.login_edit.setText(AcademyUtils.login);
        this.password_edit = (EditText) findViewById(C0984R.id.password_edit);
        this.password_edit.setText(AcademyUtils.password);
        this.why_sign_up = (ImageButton) findViewById(C0984R.id.why_sign_up);
    }

    private void retrievePassword() {
        if (this.email != null) {
            new RetrieveAsyncTask().execute(new Void[0]);
        }
    }

    private void showDialogPasswordRetrieval() {
        View inflateView = inflateView(C0984R.layout.academy_dialog_forgot_password, null, true);
        final ViewGroup viewGroup = (ViewGroup) findViewById(C0984R.id.content);
        viewGroup.addView(inflateView, new LayoutParams(-1, -1));
        final Button button = (Button) inflateView.findViewById(C0984R.id.send_password);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AcademyLoginActivity.this.retrievePassword();
                viewGroup.getChildAt(1).performClick();
            }
        });
        EditText editText = (EditText) inflateView.findViewById(C0984R.id.input_email);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                button.performClick();
                return true;
            }
        });
        editText.addTextChangedListener(new myTextWatcher(C0984R.id.input_email));
        viewGroup.getChildAt(1).bringToFront();
        viewGroup.getChildAt(1).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AcademyLoginActivity.this.hideKeyboard();
                viewGroup.removeView(view);
            }
        });
    }

    private void showWelcome() {
        switchToLayout(C0984R.layout.academy_welcome);
        ((TextView) findViewById(C0984R.id.academy_welcome_text)).setText(String.format(getString(C0984R.string.aa_ID000141), new Object[]{AcademyUtils.profile.getUser().getUsername().toUpperCase()}));
        this.showAcademyTime = System.currentTimeMillis() + 2000;
        this.handler.postDelayed(this.showAcademy, 2000);
    }

    private void signIn() {
        if (!this.login_edit.getText().toString().equals(HttpVersions.HTTP_0_9) && !this.password_edit.getText().toString().equals(HttpVersions.HTTP_0_9)) {
            switchToLayout(C0984R.layout.academy_authentication_progress);
            AcademyUtils.password_save_ok = this.password_save.getCheckedRadioButtonId() == C0984R.id.yes;
            this.connectTask = new ConnectAsyncTask();
            this.connectTask.execute(new Void[0]);
        }
    }

    private void switchToLayout(int i) {
        View inflateView = inflateView(i, null, false);
        ViewGroup viewGroup = (ViewGroup) findViewById(C0984R.id.content);
        viewGroup.removeAllViewsInLayout();
        viewGroup.addView(inflateView, new LayoutParams(-1, -1));
    }

    private void testConnection() {
        if (AcademyUtils.isConnectedAcademy) {
            DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__ACADEMY_LOGGED_IN);
            showWelcome();
            return;
        }
        switchToLayout(C0984R.layout.academy_login);
        initUI();
        initListeners();
        switch (AcademyUtils.responseCode) {
            case 401:
                showAlertDialog(getString(C0984R.string.aa_ID000145), getString(C0984R.string.aa_ID000146), null);
                return;
            default:
                showAlertDialog(getString(C0984R.string.aa_ID000145), getString(C0984R.string.aa_ID000145), null);
                return;
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case C0984R.id.yes /*2131361822*/:
                AcademyUtils.password_save_ok = true;
                return;
            case C0984R.id.no /*2131361823*/:
                AcademyUtils.password_save_ok = false;
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.authenticate /*2131361824*/:
                hideKeyboard();
                signIn();
                return;
            case C0984R.id.signup /*2131361828*/:
                startActivity(new Intent(this, AcademyRegisterActivity.class));
                return;
            case C0984R.id.why_sign_up /*2131361829*/:
                Intent intent = new Intent(this, GuestSpaceActivity.class);
                intent.putExtra("from", 0);
                startActivity(intent);
                return;
            case C0984R.id.forgotpassword /*2131361830*/:
                showDialogPasswordRetrieval();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_screen);
        findViewById(16908290).setOnTouchListener(this.OnTouchOutOfFocusListener);
        AcademyUtils.getCredentials(this);
        this.handler = new Handler();
        initActionBar();
        initUI();
        initListeners();
        this.droneEngine = ARDroneEngine.instance(getApplicationContext());
        if (AcademyUtils.isConnectedAcademy) {
            this.authenticate.performClick();
        }
        this.flightUploadServiceConnection = new FlightUploadServiceConnection();
        bindService(new Intent(this, FlightUploadService.class), this.flightUploadServiceConnection, 1);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(this.flightUploadServiceConnection);
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
        if (this.showAcademyTime != 0) {
            long currentTimeMillis = this.showAcademyTime - System.currentTimeMillis();
            if (currentTimeMillis > 0) {
                this.handler.postDelayed(this.showAcademy, currentTimeMillis);
            } else {
                this.showAcademy.run();
            }
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.connectTask != null && this.connectTask.getStatus() == Status.RUNNING) {
            this.connectTask.cancel(true);
        }
        if (this.handler != null) {
            this.handler.removeCallbacks(this.showAcademy);
        }
    }

    protected void showAlertDialog(String str, String str2, final Runnable runnable) {
        Builder builder = new Builder(this);
        View textView = new TextView(this);
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextAppearance(this, 16974264);
        if ((super.getResources().getConfiguration().screenLayout & 15) >= 3) {
            textView.setTextSize(22.0f);
        } else {
            textView.setTextSize(18.0f);
        }
        textView.setHeight(((int) textView.getTextSize()) * 4);
        builder.setCustomTitle(textView);
        this.alertDialog = builder.setMessage(str2).setCancelable(false).setNegativeButton(getString(17039370), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }).create();
        this.alertDialog.show();
        TextView textView2 = (TextView) this.alertDialog.findViewById(16908299);
        ((TextView) this.alertDialog.findViewById(16908299)).setGravity(17);
    }
}
