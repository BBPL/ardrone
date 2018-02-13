package com.sony.rdis.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.sony.rdis.common.Dbg;

public class ServiceCommunicator extends BroadcastReceiver {
    private static final String logTag = "RDIS_LIB";
    private Context mContext = null;
    private ServiceCommunicatorListener mListener = null;
    private String mProtocol = null;

    public ServiceCommunicator(Context context, ServiceCommunicatorListener serviceCommunicatorListener, String str) {
        Dbg.m1744i(logTag, "ServiceCommunicator: ");
        this.mContext = context;
        this.mListener = serviceCommunicatorListener;
        this.mProtocol = str;
        this.mContext.registerReceiver(this, new IntentFilter(ServiceComuncationProtocol.ACTION_RECV_GENERAL_PURPOSE_COMMUNICATION));
    }

    public void destroy() {
        this.mContext.unregisterReceiver(this);
    }

    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(ServiceComuncationProtocol.PROTOCOL);
        int intExtra = intent.getIntExtra(ServiceComuncationProtocol.CLIENT_ID, -1);
        String stringExtra2 = intent.getStringExtra(ServiceComuncationProtocol.DATA_NAME);
        String stringExtra3 = intent.getStringExtra(ServiceComuncationProtocol.DATA);
        if (stringExtra.equals(this.mProtocol) && this.mListener != null) {
            this.mListener.recvData(intExtra, stringExtra2, stringExtra3);
        }
    }

    public void sendData(int i, String str, String str2) {
        if (str == null || str2 == null) {
            Dbg.m1748w(logTag, "sendData() name or value is illegal.");
            return;
        }
        Intent intent = new Intent(ServiceComuncationProtocol.ACTION_SEND_GENERAL_PURPOSE_COMMUNICATION);
        intent.putExtra(ServiceComuncationProtocol.PROTOCOL, this.mProtocol);
        intent.putExtra(ServiceComuncationProtocol.CLIENT_ID, i);
        intent.putExtra(ServiceComuncationProtocol.DATA_NAME, str);
        intent.putExtra(ServiceComuncationProtocol.DATA, str2);
        this.mContext.sendBroadcast(intent);
    }
}
