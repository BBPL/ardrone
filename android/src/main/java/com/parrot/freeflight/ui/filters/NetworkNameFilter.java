package com.parrot.freeflight.ui.filters;

import android.text.InputFilter;
import android.text.Spanned;
import java.util.regex.Pattern;
import org.mortbay.jetty.HttpVersions;

public class NetworkNameFilter implements InputFilter {
    private Pattern lettersAndDigits = Pattern.compile("[a-zA-Z0-9_]*");

    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        return !this.lettersAndDigits.matcher(charSequence.subSequence(i, i2)).matches() ? HttpVersions.HTTP_0_9 : spanned.length() > 32 ? HttpVersions.HTTP_0_9 : null;
    }
}
