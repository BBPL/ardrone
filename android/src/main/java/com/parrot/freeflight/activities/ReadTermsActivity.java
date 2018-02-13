package com.parrot.freeflight.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;

public class ReadTermsActivity extends ParrotActivity {
    private ActionBar actionBar;
    private TextView terms;

    private void initActionBar() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.ff_ID000163));
        this.actionBar.initBackButton();
    }

    private void initText() {
        this.terms.setText(getString(C0984R.string.ff_ID000172) + "\n" + getString(C0984R.string.ff_ID000173) + "\n" + getString(C0984R.string.ff_ID000174) + "\n" + getString(C0984R.string.ff_ID000175));
    }

    private void initUI() {
        this.terms = (TextView) findViewById(C0984R.id.content);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.academy_terms);
        initActionBar();
        initUI();
        initText();
    }
}
