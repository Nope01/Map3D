package org.example;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class VectorUtils {

    public static float[] vector3fToFloat (Vector3f[] vecList) {
        float[] result = new float[vecList.length * 2];
        int count = 0;
        for (Vector3f vec : vecList) {
            result[count++] = vec.x;
            result[count++] = vec.y;
        }
        return result;
    }

    public static List<Vector3f> floatToVector3f(float[] floatList) {
        List<Vector3f> result = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < floatList.length/3; i++) {
            result.add(new Vector3f(floatList[count++], floatList[count++], floatList[count++]));
        }
        return result;
    }
}
