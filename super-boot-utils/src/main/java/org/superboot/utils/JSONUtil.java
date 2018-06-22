package org.superboot.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <b> Json工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
public class JSONUtil {


    /**
     * 排序时候依据的key
     */
    public static String sortKey;

    /**
     * 根据KEY对JSONARRAY进行排序
     *
     * @param array 需要排序的对象
     * @return
     */
    public static JSONArray sortJsonArray(JSONArray array) {

        //对JSONARRAY进行排序
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < array.size(); i++) {
            jsonValues.add(array.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = a.get(sortKey) + "";
                    valB = b.get(sortKey) + "";
                } catch (JSONException e) {
                }
                return valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonValues.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }

        return sortedJsonArray;

    }
}
