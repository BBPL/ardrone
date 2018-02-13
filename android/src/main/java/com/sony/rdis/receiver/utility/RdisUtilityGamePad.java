package com.sony.rdis.receiver.utility;

import com.sony.rdis.common.Dbg;
import com.sony.rdis.receiver.RdisManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpVersions;

public class RdisUtilityGamePad {
    private static final String logTag = "RDIS_UTIL";
    private int mDeviceId;
    private RdisUtilityGamePadInfo mRdisUtilityGamePadInfo = null;
    private boolean mRemoconGamePad;
    private int[] mSensorType;

    public RdisUtilityGamePad(boolean z, int i, int[] iArr, RdisUtilityGamePadInfo rdisUtilityGamePadInfo) {
        this.mRemoconGamePad = z;
        this.mDeviceId = i;
        this.mSensorType = iArr;
        this.mRdisUtilityGamePadInfo = rdisUtilityGamePadInfo;
    }

    public int getDeviceId() {
        return this.mDeviceId;
    }

    public String getMacAddress() {
        return this.mRdisUtilityGamePadInfo == null ? null : this.mRdisUtilityGamePadInfo.mMacAddress;
    }

    public String getNickname() {
        return this.mRdisUtilityGamePadInfo == null ? null : this.mRdisUtilityGamePadInfo.mNickname;
    }

    public int[] getSensorType() {
        return this.mSensorType;
    }

    public boolean isDefaultGamePad() {
        return this.mRemoconGamePad;
    }

    public void setKeyConfig(RdisUtilityKeyConfiguration rdisUtilityKeyConfiguration) {
        if (this.mRdisUtilityGamePadInfo != null && !this.mRdisUtilityGamePadInfo.mRegisteredFlag) {
            return;
        }
        if (rdisUtilityKeyConfiguration == null) {
            RdisManager.getRdisManager().sendGeneralCommunication(this.mDeviceId, "UNSET_KEY_CONFIG", HttpVersions.HTTP_0_9);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (rdisUtilityKeyConfiguration.useAnalogStick) {
                jSONObject.put("DIRECTIONAL", "ANALOG");
            } else {
                jSONObject.put("DIRECTIONAL", "DIGITAL");
            }
            jSONObject.put("DIRECTIONAL_KEY_UP", rdisUtilityKeyConfiguration.keyUp);
            jSONObject.put("DIRECTIONAL_KEY_DOWN", rdisUtilityKeyConfiguration.keyDown);
            jSONObject.put("DIRECTIONAL_KEY_LEFT", rdisUtilityKeyConfiguration.keyLeft);
            jSONObject.put("DIRECTIONAL_KEY_RIGHT", rdisUtilityKeyConfiguration.keyRight);
            jSONObject.put("KEY_A", rdisUtilityKeyConfiguration.keyA);
            jSONObject.put("KEY_B", rdisUtilityKeyConfiguration.keyB);
            jSONObject.put("KEY_C", rdisUtilityKeyConfiguration.keyC);
            jSONObject.put("KEY_D", rdisUtilityKeyConfiguration.keyD);
            RdisManager.getRdisManager().sendGeneralCommunication(this.mDeviceId, "SET_KEY_CONFIG", jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    public void setUserColor(int i) {
        if (this.mRdisUtilityGamePadInfo != null && !this.mRdisUtilityGamePadInfo.mRegisteredFlag) {
            return;
        }
        if (i == 0) {
            RdisManager.getRdisManager().sendGeneralCommunication(this.mDeviceId, "UNSET_USER_COLOR", HttpVersions.HTTP_0_9);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("USER_COLOR", "0x" + Integer.toHexString(i));
            RdisManager.getRdisManager().sendGeneralCommunication(this.mDeviceId, "SET_USER_COLOR", jSONObject.toString());
        } catch (JSONException e) {
            Dbg.m1742e(logTag, "ERROR :" + e);
        }
    }

    public void vibrate(long j) {
        RdisManager.getRdisManager().vibrate(this.mDeviceId, j);
    }
}
