package com.parrot.freeflight.track_3d_viewer.ios_stuff;

public class OpenGLInterleavedVertex {
    public final float[] color = new float[4];
    public final float[] normal = new float[3];
    public final float[] position = new float[3];
    public final float[] texcoord = new float[2];

    public static float[] convert(OpenGLInterleavedVertex[] openGLInterleavedVertexArr) {
        float[] fArr = new float[(openGLInterleavedVertexArr.length * 12)];
        int i = 0;
        for (OpenGLInterleavedVertex openGLInterleavedVertex : openGLInterleavedVertexArr) {
            int i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.position[0];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.position[1];
            i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.position[2];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.color[0];
            i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.color[1];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.color[2];
            i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.color[3];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.normal[0];
            i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.normal[1];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.normal[2];
            i2 = i + 1;
            fArr[i] = openGLInterleavedVertex.texcoord[0];
            i = i2 + 1;
            fArr[i2] = openGLInterleavedVertex.texcoord[1];
        }
        return fArr;
    }
}
