package com.parrot.freeflight.academy.activities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.ReadTermsActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
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

public class AcademyRegisterActivity extends ParrotActivity implements OnClickListener, OnCheckedChangeListener {
    private OnTouchListener OnTouchOutOfFocusListener = new C10342();
    private ActionBar actionBar;
    private AlertDialog alertDialog;
    private boolean conditions_ok = false;
    private CheckBox conditions_ok_box;
    private String email;
    private EditText email_edit;
    private Point firstPointTouch = new Point();
    private String login;
    private EditText login_edit;
    private boolean mailing_ok = false;
    private CheckBox mailing_ok_box;
    private boolean movedEnough = false;
    private int movementPrecision = 10;
    private String password;
    private String password_confirm;
    private EditText password_confirm_edit;
    private EditText password_edit;
    private Button signup;
    private ImageButton terms;

    class C10342 implements OnTouchListener {
        C10342() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    AcademyRegisterActivity.this.firstPointTouch.x = (int) motionEvent.getX(0);
                    AcademyRegisterActivity.this.firstPointTouch.y = (int) motionEvent.getY(0);
                    break;
                case 1:
                    if (!(AcademyRegisterActivity.this.getCurrentFocus() == null || AcademyRegisterActivity.this.getCurrentFocus() == view || AcademyRegisterActivity.this.movedEnough)) {
                        ((InputMethodManager) AcademyRegisterActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(AcademyRegisterActivity.this.getCurrentFocus().getWindowToken(), 2);
                    }
                    AcademyRegisterActivity.this.movedEnough = false;
                    break;
                case 2:
                    if (Math.pow((double) (motionEvent.getX(0) - ((float) AcademyRegisterActivity.this.firstPointTouch.x)), 2.0d) + Math.pow((double) (motionEvent.getY(0) - ((float) AcademyRegisterActivity.this.firstPointTouch.y)), 2.0d) > Math.pow((double) AcademyRegisterActivity.this.movementPrecision, 2.0d)) {
                        AcademyRegisterActivity.this.movedEnough = true;
                        break;
                    }
                    break;
            }
            return false;
        }
    }

    protected class RegisterAsyncTask extends AsyncTask<Void, Void, List<Object>> {

        class C10351 implements Runnable {
            C10351() {
            }

            public void run() {
                AcademyRegisterActivity.this.finish();
            }
        }

        protected RegisterAsyncTask() {
        }

        protected List<Object> doInBackground(Void... voidArr) {
            List<Object> arrayList;
            UnsupportedEncodingException e;
            ClientProtocolException e2;
            IOException e3;
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPost = new HttpPost(AcademyRegisterActivity.this.getString(C0984R.string.http).concat(AcademyRegisterActivity.this.getString(C0984R.string.url_server)).concat(AcademyRegisterActivity.this.getString(C0984R.string.url_profile)));
                httpPost.setHeader("Accept-Language", Locale.getDefault().getLanguage());
                List arrayList2 = new ArrayList();
                arrayList2.add(new BasicNameValuePair("tos", "on"));
                arrayList2.add(new BasicNameValuePair("username", AcademyRegisterActivity.this.login));
                arrayList2.add(new BasicNameValuePair("email", AcademyRegisterActivity.this.email));
                arrayList2.add(new BasicNameValuePair("password1", AcademyRegisterActivity.this.password));
                arrayList2.add(new BasicNameValuePair("password2", AcademyRegisterActivity.this.password_confirm));
                arrayList2.add(new BasicNameValuePair("email_academy", String.valueOf(AcademyRegisterActivity.this.mailing_ok)));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList2, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPost);
                HttpEntity entity = execute.getEntity();
                int statusCode = execute.getStatusLine().getStatusCode();
                String entityUtils = EntityUtils.toString(entity, "UTF-8");
                arrayList = new ArrayList(2);
                try {
                    arrayList.add(0, Integer.valueOf(statusCode));
                    arrayList.add(1, entityUtils);
                } catch (UnsupportedEncodingException e4) {
                    e = e4;
                    e.printStackTrace();
                    return arrayList;
                } catch (ClientProtocolException e5) {
                    e2 = e5;
                    e2.printStackTrace();
                    return arrayList;
                } catch (IOException e6) {
                    e3 = e6;
                    e3.printStackTrace();
                    return arrayList;
                }
            } catch (UnsupportedEncodingException e7) {
                e = e7;
                arrayList = null;
                e.printStackTrace();
                return arrayList;
            } catch (ClientProtocolException e8) {
                e2 = e8;
                arrayList = null;
                e2.printStackTrace();
                return arrayList;
            } catch (IOException e9) {
                e3 = e9;
                arrayList = null;
                e3.printStackTrace();
                return arrayList;
            }
            return arrayList;
        }

        protected void onPostExecute(List<Object> list) {
            if (list != null) {
                switch (((Integer) list.get(0)).intValue()) {
                    case 200:
                        AcademyRegisterActivity.this.showAlertDialog(AcademyRegisterActivity.this.getString(C0984R.string.aa_ID000129), AcademyRegisterActivity.this.getString(C0984R.string.aa_ID000130), new C10351());
                        return;
                    default:
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            Iterator keys = new JSONObject((String) list.get(1)).keys();
                            while (keys.hasNext()) {
                                String str = (String) keys.next();
                                stringBuilder.append(String.format("- %s: %s", new Object[]{str, r2.getJSONArray(str).get(0).toString()}));
                                if (keys.hasNext()) {
                                    stringBuilder.append("\n");
                                }
                            }
                            AcademyRegisterActivity.this.showAlertDialog(AcademyRegisterActivity.this.getString(C0984R.string.aa_ID000128), stringBuilder.toString(), null);
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                }
            }
            AcademyRegisterActivity.this.showAlertDialog(AcademyRegisterActivity.this.getString(C0984R.string.aa_ID000128), AcademyRegisterActivity.this.getString(C0984R.string.ff_ID000206), null);
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
                    AcademyRegisterActivity.this.login = AcademyRegisterActivity.this.login_edit.getText().toString();
                    return;
                case C0984R.id.password_edit /*2131361819*/:
                    AcademyRegisterActivity.this.password = AcademyRegisterActivity.this.password_edit.getText().toString();
                    return;
                case C0984R.id.password_confirm_edit /*2131361937*/:
                    AcademyRegisterActivity.this.password_confirm = AcademyRegisterActivity.this.password_confirm_edit.getText().toString();
                    return;
                case C0984R.id.email_edit /*2131361939*/:
                    AcademyRegisterActivity.this.email = editable.toString();
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
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    private void initActionBar() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.aa_ID000122));
        this.actionBar.initBackButton();
    }

    private void initListeners() {
        findViewById(C0984R.id.login_register_scrollView_layout).setOnTouchListener(this.OnTouchOutOfFocusListener);
        this.signup.setOnClickListener(this);
        this.terms.setOnClickListener(this);
        this.login_edit.addTextChangedListener(new myTextWatcher(this.login_edit.getId()));
        this.password_edit.addTextChangedListener(new myTextWatcher(this.password_edit.getId()));
        this.password_confirm_edit.addTextChangedListener(new myTextWatcher(this.password_confirm_edit.getId()));
        this.email_edit.addTextChangedListener(new myTextWatcher(this.email_edit.getId()));
        this.conditions_ok_box.setOnCheckedChangeListener(this);
        this.mailing_ok_box.setOnCheckedChangeListener(this);
    }

    private void initUI() {
        this.signup = (Button) findViewById(C0984R.id.sign_up);
        this.terms = (ImageButton) findViewById(C0984R.id.read_terms);
        this.login_edit = (EditText) findViewById(C0984R.id.login_edit);
        this.password_edit = (EditText) findViewById(C0984R.id.password_edit);
        this.password_confirm_edit = (EditText) findViewById(C0984R.id.password_confirm_edit);
        this.email_edit = (EditText) findViewById(C0984R.id.email_edit);
        this.conditions_ok_box = (CheckBox) findViewById(C0984R.id.terms_of_use_agreement);
        this.mailing_ok_box = (CheckBox) findViewById(C0984R.id.mail_agreement);
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case C0984R.id.terms_of_use_agreement /*2131361940*/:
                this.conditions_ok = z;
                return;
            case C0984R.id.mail_agreement /*2131361943*/:
                this.mailing_ok = z;
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.read_terms /*2131361942*/:
                startActivity(new Intent(this, ReadTermsActivity.class));
                return;
            case C0984R.id.sign_up /*2131361945*/:
                if (this.conditions_ok) {
                    new RegisterAsyncTask().execute(new Void[0]);
                    hideKeyboard();
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_register);
        initActionBar();
        initUI();
        initListeners();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
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
        ((TextView) this.alertDialog.findViewById(16908299)).setGravity(3);
    }
}
