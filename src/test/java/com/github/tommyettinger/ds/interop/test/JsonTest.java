package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.ds.ObjectSet;
import com.github.tommyettinger.ds.interop.JsonSupport;
import org.junit.Test;

public class JsonTest {
    @Test
    public void testObjectSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerWith(json);
        ObjectSet<String> words = ObjectSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectSet words2 = json.fromJson(ObjectSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
    }
}
