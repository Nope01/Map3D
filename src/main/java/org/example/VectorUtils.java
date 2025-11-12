package org.example;

import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VectorUtils {

    public static float[] vector3fToFloat (List<Vector3f> vecList) {
        float[] result = new float[vecList.size() * 3];
        int count = 0;
        for (Vector3f vec : vecList) {
            result[count++] = vec.x;
            result[count++] = vec.y;
            result[count++] = 0;
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

    public static Map<Vector3f, Integer> verticesToMap(List<Vector3f> vertices) {
        Map<Vector3f, Integer> map = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            map.put(vertices.get(i), i);
        }
        return map;
    }
}
