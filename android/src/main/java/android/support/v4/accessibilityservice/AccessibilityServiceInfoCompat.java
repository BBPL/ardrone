package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;

public class AccessibilityServiceInfoCompat {
    public static final int FEEDBACK_ALL_MASK = -1;
    private static final AccessibilityServiceInfoVersionImpl IMPL;

    interface AccessibilityServiceInfoVersionImpl {
        boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo);

        String getDescription(AccessibilityServiceInfo accessibilityServiceInfo);

        String getId(AccessibilityServiceInfo accessibilityServiceInfo);

        ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo);

        String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo);
    }

    static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoVersionImpl {
        AccessibilityServiceInfoStubImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo) {
            return false;
        }

        public String getDescription(AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }

        public String getId(AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }

        public String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo) {
            return null;
        }
    }

    static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoStubImpl {
        AccessibilityServiceInfoIcsImpl() {
        }

        public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(accessibilityServiceInfo);
        }

        public String getDescription(AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getDescription(accessibilityServiceInfo);
        }

        public String getId(AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getId(accessibilityServiceInfo);
        }

        public ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getResolveInfo(accessibilityServiceInfo);
        }

        public String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo) {
            return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(accessibilityServiceInfo);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityServiceInfoIcsImpl();
        } else {
            IMPL = new AccessibilityServiceInfoStubImpl();
        }
    }

    private AccessibilityServiceInfoCompat() {
    }

    public static String feedbackTypeToString(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        while (i > 0) {
            int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(i);
            i &= numberOfTrailingZeros ^ -1;
            if (stringBuilder.length() > 1) {
                stringBuilder.append(", ");
            }
            switch (numberOfTrailingZeros) {
                case 1:
                    stringBuilder.append("FEEDBACK_SPOKEN");
                    break;
                case 2:
                    stringBuilder.append("FEEDBACK_HAPTIC");
                    break;
                case 4:
                    stringBuilder.append("FEEDBACK_AUDIBLE");
                    break;
                case 8:
                    stringBuilder.append("FEEDBACK_VISUAL");
                    break;
                case 16:
                    stringBuilder.append("FEEDBACK_GENERIC");
                    break;
                default:
                    break;
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String flagToString(int i) {
        switch (i) {
            case 1:
                return "DEFAULT";
            default:
                return null;
        }
    }

    public static boolean getCanRetrieveWindowContent(AccessibilityServiceInfo accessibilityServiceInfo) {
        return IMPL.getCanRetrieveWindowContent(accessibilityServiceInfo);
    }

    public static String getDescription(AccessibilityServiceInfo accessibilityServiceInfo) {
        return IMPL.getDescription(accessibilityServiceInfo);
    }

    public static String getId(AccessibilityServiceInfo accessibilityServiceInfo) {
        return IMPL.getId(accessibilityServiceInfo);
    }

    public static ResolveInfo getResolveInfo(AccessibilityServiceInfo accessibilityServiceInfo) {
        return IMPL.getResolveInfo(accessibilityServiceInfo);
    }

    public static String getSettingsActivityName(AccessibilityServiceInfo accessibilityServiceInfo) {
        return IMPL.getSettingsActivityName(accessibilityServiceInfo);
    }
}
