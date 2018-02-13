package fix.android.opengl;

public class GLES20 {
    static {
        System.loadLibrary("glfix");
    }

    private GLES20() {
    }

    public static native void glDrawElements(int i, int i2, int i3, int i4);

    public static native void glVertexAttribPointer(int i, int i2, int i3, boolean z, int i4, int i5);
}
