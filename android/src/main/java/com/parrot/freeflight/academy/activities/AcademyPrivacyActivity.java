package com.parrot.freeflight.academy.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Privacy;
import com.parrot.freeflight.academy.model.PrivacyItem;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.FontUtils.TYPEFACE;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AcademyPrivacyActivity extends ParrotActivity implements OnCheckedChangeListener {
    public static boolean isChanged = false;
    private ActionBar actionBar;
    private ArrayList<PrivacyItem> privacy_items;
    private ListView privacy_list;

    private class ConnectAsyncTask extends AsyncTask<Void, Void, Void> {
        private ConnectAsyncTask() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Void doInBackground(java.lang.Void... r7) {
            /*
            r6 = this;
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009e }
            r0.<init>();	 Catch:{ Exception -> 0x009e }
            r1 = "Basic ";
            r0 = r0.append(r1);	 Catch:{ Exception -> 0x009e }
            r1 = new java.lang.String;	 Catch:{ Exception -> 0x009e }
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x009e }
            r2.<init>();	 Catch:{ Exception -> 0x009e }
            r3 = com.parrot.freeflight.academy.utils.AcademyUtils.login;	 Catch:{ Exception -> 0x009e }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x009e }
            r3 = ":";
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x009e }
            r3 = com.parrot.freeflight.academy.utils.AcademyUtils.password;	 Catch:{ Exception -> 0x009e }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x009e }
            r2 = r2.toString();	 Catch:{ Exception -> 0x009e }
            r2 = r2.getBytes();	 Catch:{ Exception -> 0x009e }
            r3 = 2;
            r2 = android.util.Base64.encode(r2, r3);	 Catch:{ Exception -> 0x009e }
            r1.<init>(r2);	 Catch:{ Exception -> 0x009e }
            r0 = r0.append(r1);	 Catch:{ Exception -> 0x009e }
            r1 = r0.toString();	 Catch:{ Exception -> 0x009e }
            r0 = new java.net.URL;	 Catch:{ Exception -> 0x009e }
            r2 = com.parrot.freeflight.academy.activities.AcademyPrivacyActivity.this;	 Catch:{ Exception -> 0x009e }
            r3 = 2131165488; // 0x7f070130 float:1.7945195E38 double:1.052935653E-314;
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x009e }
            r3 = com.parrot.freeflight.academy.activities.AcademyPrivacyActivity.this;	 Catch:{ Exception -> 0x009e }
            r4 = 2131165489; // 0x7f070131 float:1.7945197E38 double:1.0529356537E-314;
            r3 = r3.getString(r4);	 Catch:{ Exception -> 0x009e }
            r4 = com.parrot.freeflight.academy.activities.AcademyPrivacyActivity.this;	 Catch:{ Exception -> 0x009e }
            r5 = 2131165491; // 0x7f070133 float:1.79452E38 double:1.0529356547E-314;
            r4 = r4.getString(r5);	 Catch:{ Exception -> 0x009e }
            r3 = r3.concat(r4);	 Catch:{ Exception -> 0x009e }
            r2 = r2.concat(r3);	 Catch:{ Exception -> 0x009e }
            r0.<init>(r2);	 Catch:{ Exception -> 0x009e }
            r0 = r0.openConnection();	 Catch:{ Exception -> 0x009e }
            r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x009e }
            r2 = "Authorization";
            r0.setRequestProperty(r2, r1);	 Catch:{ Exception -> 0x009e }
            r0.connect();	 Catch:{ Exception -> 0x009e }
            r1 = r0.getResponseCode();	 Catch:{ Exception -> 0x009e }
            com.parrot.freeflight.academy.utils.AcademyUtils.responseCode = r1;	 Catch:{ Exception -> 0x009e }
            r1 = com.parrot.freeflight.academy.utils.AcademyUtils.responseCode;	 Catch:{ Exception -> 0x009e }
            switch(r1) {
                case 200: goto L_0x007f;
                case 401: goto L_0x00aa;
                default: goto L_0x007d;
            };	 Catch:{ Exception -> 0x009e }
        L_0x007d:
            r0 = 0;
            return r0;
        L_0x007f:
            r1 = new org.json.JSONObject;	 Catch:{ Exception -> 0x009e }
            r0 = r0.getInputStream();	 Catch:{ Exception -> 0x009e }
            r0 = com.parrot.freeflight.academy.utils.JSONParser.readStream(r0);	 Catch:{ Exception -> 0x009e }
            r1.<init>(r0);	 Catch:{ Exception -> 0x009e }
            r0 = com.parrot.freeflight.academy.utils.AcademyUtils.profile;	 Catch:{ Exception -> 0x009e }
            r2 = new com.parrot.freeflight.academy.model.Privacy;	 Catch:{ Exception -> 0x009e }
            r2.<init>(r1);	 Catch:{ Exception -> 0x009e }
            r0.setPrivacy(r2);	 Catch:{ Exception -> 0x009e }
            r0 = "responseCode";
            r1 = "200";
            android.util.Log.w(r0, r1);	 Catch:{ Exception -> 0x009e }
            goto L_0x007d;
        L_0x009e:
            r0 = move-exception;
            r1 = "connection";
            r2 = "fail";
            android.util.Log.e(r1, r2);
            r0.printStackTrace();
            goto L_0x007d;
        L_0x00aa:
            r0 = "responseCode";
            r1 = "401";
            android.util.Log.w(r0, r1);	 Catch:{ Exception -> 0x009e }
            goto L_0x007d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.academy.activities.AcademyPrivacyActivity.ConnectAsyncTask.doInBackground(java.lang.Void[]):java.lang.Void");
        }

        protected void onPostExecute(Void voidR) {
            AcademyPrivacyActivity.this.switchToLayout(C0984R.layout.academy_privacy_list);
            AcademyPrivacyActivity.this.initPrivacyItems();
        }

        protected void onPreExecute() {
            AcademyPrivacyActivity.this.switchToLayout(C0984R.layout.academy_authentication_progress);
            ((TextView) AcademyPrivacyActivity.this.findViewById(C0984R.id.loading_text)).setText(AcademyPrivacyActivity.this.getString(C0984R.string.aa_ID000000));
        }
    }

    private final class PrivacyAdapter extends ArrayAdapter<PrivacyItem> implements OnCheckedChangeListener {
        Context context;
        ArrayList<PrivacyItem> data = new ArrayList();
        int layoutResourceId;

        public PrivacyAdapter(Context context, int i, ArrayList<PrivacyItem> arrayList) {
            super(context, i, arrayList);
            this.layoutResourceId = i;
            this.context = context;
            this.data = arrayList;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            PrivacyItemHolder privacyItemHolder;
            if (view == null) {
                view = ((Activity) this.context).getLayoutInflater().inflate(this.layoutResourceId, viewGroup, false);
                privacyItemHolder = new PrivacyItemHolder();
                privacyItemHolder.name = (TextView) view.findViewById(C0984R.id.privacy_item_text);
                privacyItemHolder.name.setTypeface(TYPEFACE.Helvetica(this.context));
                privacyItemHolder.value = (RadioGroup) view.findViewById(C0984R.id.privacy_item_toggle);
                privacyItemHolder.value.setId(i);
                view.setTag(privacyItemHolder);
            } else {
                privacyItemHolder = (PrivacyItemHolder) view.getTag();
            }
            privacyItemHolder.name.setText(((PrivacyItem) this.data.get(i)).getName());
            ((RadioButton) privacyItemHolder.value.getChildAt(((PrivacyItem) this.data.get(i)).getState())).toggle();
            privacyItemHolder.value.setOnCheckedChangeListener(this);
            return view;
        }

        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case C0984R.id.privacy_item_public /*2131361882*/:
                    ((PrivacyItem) this.data.get(radioGroup.getId())).setState(0);
                    break;
                case C0984R.id.privacy_item_private /*2131361883*/:
                    ((PrivacyItem) this.data.get(radioGroup.getId())).setState(1);
                    break;
            }
            AcademyPrivacyActivity.isChanged = true;
        }
    }

    private static final class PrivacyItemHolder {
        TextView name;
        RadioGroup value;

        private PrivacyItemHolder() {
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        private UpdateAsyncTask() {
        }

        protected Void doInBackground(Void... voidArr) {
            try {
                HttpClient defaultHttpClient = new DefaultHttpClient();
                HttpUriRequest httpPut = new HttpPut(AcademyPrivacyActivity.this.getString(C0984R.string.http).concat(AcademyPrivacyActivity.this.getString(C0984R.string.url_server).concat(AcademyPrivacyActivity.this.getString(C0984R.string.url_profile_privacy))));
                String str = "Basic " + new String(Base64.encode((AcademyUtils.login + ":" + AcademyUtils.password).getBytes(), 2));
                List arrayList = new ArrayList();
                Privacy privacy = AcademyUtils.profile.getPrivacy();
                System.out.println(privacy);
                arrayList.add(new BasicNameValuePair("city", Integer.toString(privacy.getCity())));
                arrayList.add(new BasicNameValuePair("civility", Integer.toString(privacy.getCivility())));
                arrayList.add(new BasicNameValuePair("contact", Integer.toString(privacy.getContact())));
                arrayList.add(new BasicNameValuePair("address", Integer.toString(privacy.getAddress())));
                arrayList.add(new BasicNameValuePair("email", Integer.toString(privacy.getEmail())));
                httpPut.setHeader("Authorization", str);
                httpPut.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                HttpResponse execute = defaultHttpClient.execute(httpPut);
                HttpEntity entity = execute.getEntity();
                System.out.println(execute.getStatusLine().getStatusCode());
                System.out.println(EntityUtils.toString(entity));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {
            AcademyPrivacyActivity.this.switchToLayout(C0984R.layout.academy_authentication_progress);
            ((TextView) AcademyPrivacyActivity.this.findViewById(C0984R.id.loading_text)).setText(AcademyPrivacyActivity.this.getString(C0984R.string.aa_ID000000));
        }
    }

    private void initActionBar() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.aa_ID000163).toUpperCase());
        this.actionBar.initBackButton();
    }

    private void initPrivacyItems() {
        Privacy privacy = AcademyUtils.profile.getPrivacy();
        PrivacyItem privacyItem = new PrivacyItem(getString(C0984R.string.aa_ID000117), privacy.getCity());
        PrivacyItem privacyItem2 = new PrivacyItem(getString(C0984R.string.aa_ID000168), privacy.getCivility());
        PrivacyItem privacyItem3 = new PrivacyItem(getString(C0984R.string.aa_ID000169), privacy.getContact());
        PrivacyItem privacyItem4 = new PrivacyItem(getString(C0984R.string.aa_ID000170), privacy.getAddress());
        PrivacyItem privacyItem5 = new PrivacyItem(getString(C0984R.string.aa_ID000017), privacy.getEmail());
        this.privacy_items = new ArrayList();
        this.privacy_items.add(privacyItem);
        this.privacy_items.add(privacyItem2);
        this.privacy_items.add(privacyItem3);
        this.privacy_items.add(privacyItem4);
        this.privacy_items.add(privacyItem5);
        this.privacy_list = (ListView) findViewById(C0984R.id.privacy_list);
        ListAdapter privacyAdapter = new PrivacyAdapter(this, C0984R.layout.academy_privacy_item, this.privacy_items);
        this.privacy_list.setEnabled(false);
        this.privacy_list.setAdapter(privacyAdapter);
    }

    private void switchToLayout(int i) {
        View inflateView = inflateView(i, null, false);
        ViewGroup viewGroup = (ViewGroup) findViewById(C0984R.id.content);
        viewGroup.removeAllViewsInLayout();
        viewGroup.addView(inflateView, new LayoutParams(-1, -1));
    }

    private void updatePrivacy() {
        AcademyUtils.profile.getPrivacy().setCity(((PrivacyItem) this.privacy_items.get(0)).getState());
        AcademyUtils.profile.getPrivacy().setCivility(((PrivacyItem) this.privacy_items.get(1)).getState());
        AcademyUtils.profile.getPrivacy().setContact(((PrivacyItem) this.privacy_items.get(2)).getState());
        AcademyUtils.profile.getPrivacy().setAddress(((PrivacyItem) this.privacy_items.get(3)).getState());
        AcademyUtils.profile.getPrivacy().setEmail(((PrivacyItem) this.privacy_items.get(4)).getState());
        new UpdateAsyncTask().execute(new Void[0]);
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_privacy);
        initActionBar();
        ((TextView) findViewById(C0984R.id.loading_text)).setText(getString(C0984R.string.aa_ID000000));
        new ConnectAsyncTask().execute(new Void[0]);
    }

    protected void onDestroy() {
        if (isChanged) {
            updatePrivacy();
        }
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
