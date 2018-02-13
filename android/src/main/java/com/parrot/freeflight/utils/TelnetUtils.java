package com.parrot.freeflight.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TelnetUtils {
    public static final boolean executeRemotely(String str, int i, String str2) {
        Socket socket;
        boolean z;
        UnknownHostException e;
        IOException e2;
        Throwable th;
        OutputStream outputStream;
        OutputStream outputStream2;
        OutputStream outputStream3 = null;
        try {
            socket = new Socket(str, i);
            try {
                outputStream3 = socket.getOutputStream();
                outputStream3.write(str2.getBytes());
                outputStream3.flush();
                z = true;
                if (outputStream3 != null) {
                    try {
                        outputStream3.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                if (!(socket == null || socket.isClosed())) {
                    try {
                        socket.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
            } catch (UnknownHostException e4) {
                e = e4;
                try {
                    e.printStackTrace();
                    if (outputStream3 != null) {
                        try {
                            outputStream3.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (!(socket == null || socket.isClosed())) {
                        try {
                            socket.close();
                            return false;
                        } catch (IOException e5) {
                            e22 = e5;
                            e22.printStackTrace();
                            return false;
                        }
                    }
                    z = false;
                    return z;
                } catch (Throwable th2) {
                    th = th2;
                    Socket socket2 = socket;
                    outputStream = outputStream3;
                    Object obj = socket2;
                    outputStream2 = outputStream3;
                    outputStream3 = outputStream;
                    socket = outputStream2;
                    if (outputStream3 != null) {
                        try {
                            outputStream3.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                    }
                    if (!(socket == null || socket.isClosed())) {
                        try {
                            socket.close();
                        } catch (IOException e3222) {
                            e3222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e22 = e6;
                e22.printStackTrace();
                if (outputStream3 != null) {
                    try {
                        outputStream3.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                }
                if (!(socket == null || socket.isClosed())) {
                    try {
                        socket.close();
                        return false;
                    } catch (IOException e7) {
                        e222 = e7;
                        e222.printStackTrace();
                        return false;
                    }
                }
                z = false;
                return z;
            } catch (Throwable th3) {
                th = th3;
                if (outputStream3 != null) {
                    outputStream3.close();
                }
                socket.close();
                throw th;
            }
        } catch (UnknownHostException e8) {
            e = e8;
            socket = null;
            e.printStackTrace();
            if (outputStream3 != null) {
                outputStream3.close();
            }
            socket.close();
            return false;
        } catch (IOException e9) {
            e222 = e9;
            socket = null;
            e222.printStackTrace();
            if (outputStream3 != null) {
                outputStream3.close();
            }
            socket.close();
            return false;
        } catch (Throwable th4) {
            th = th4;
            outputStream = null;
            outputStream2 = outputStream3;
            outputStream3 = outputStream;
            socket = outputStream2;
            if (outputStream3 != null) {
                outputStream3.close();
            }
            socket.close();
            throw th;
        }
        return z;
    }
}
