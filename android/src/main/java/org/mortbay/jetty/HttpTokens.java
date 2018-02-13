package org.mortbay.jetty;

public interface HttpTokens {
    public static final byte CARRIAGE_RETURN = (byte) 13;
    public static final int CHUNKED_CONTENT = -2;
    public static final byte COLON = (byte) 58;
    public static final byte[] CRLF = new byte[]{(byte) 13, (byte) 10};
    public static final int EOF_CONTENT = -1;
    public static final byte LINE_FEED = (byte) 10;
    public static final int NO_CONTENT = 0;
    public static final int SELF_DEFINING_CONTENT = -4;
    public static final byte SEMI_COLON = (byte) 59;
    public static final byte SPACE = (byte) 32;
    public static final byte TAB = (byte) 9;
    public static final int UNKNOWN_CONTENT = -3;
}
