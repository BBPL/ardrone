package com.parrot.freeflight.receivers;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class MobileSignalStrengthListener extends PhoneStateListener {
    private static final int MAX_VALUE = 3;
    private MobileSignalStrengthListenerDelegate delegate;

    public MobileSignalStrengthListener(MobileSignalStrengthListenerDelegate mobileSignalStrengthListenerDelegate) {
        this.delegate = mobileSignalStrengthListenerDelegate;
    }

    public static int createFlags() {
        return 256;
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if (this.delegate != null) {
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
            this.delegate.onMobileSignalStrengthChanged(gsmSignalStrength <= 31 ? Math.round((((float) gsmSignalStrength) / 31.0f) * 3.0f) : 3);
        }
    }
}
