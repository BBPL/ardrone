package com.parrot.ardronetool.tracking;

import java.util.HashMap;

public enum DEVICE_TYPE_ENUM {
    DEVICE_TYPE__IPHONE(0),
    DEVICE_TYPE__IPAD(1),
    DEVICE_TYPE__ITOUCH(2),
    DEVICE_TYPE__ANDROID2(3),
    DEVICE_TYPE__ANDROID3(4),
    DEVICE_TYPE__ANDROID4(5);
    
    static HashMap<Integer, DEVICE_TYPE_ENUM> valuesList;
    private final int value;

    private DEVICE_TYPE_ENUM(int i) {
        this.value = i;
    }

    public static DEVICE_TYPE_ENUM getFromValue(int i) {
        if (valuesList == null) {
            DEVICE_TYPE_ENUM[] values = values();
            valuesList = new HashMap(values.length);
            for (DEVICE_TYPE_ENUM device_type_enum : values) {
                valuesList.put(Integer.valueOf(device_type_enum.getValue()), device_type_enum);
            }
        }
        return (DEVICE_TYPE_ENUM) valuesList.get(Integer.valueOf(i));
    }

    public int getValue() {
        return this.value;
    }
}
