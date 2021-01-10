package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.ds.IntOrderedSet;
import com.github.tommyettinger.ds.IntSet;
import com.github.tommyettinger.ds.ObjectOrderedSet;
import com.github.tommyettinger.ds.ObjectSet;
import com.github.tommyettinger.ds.interop.JsonSupport;
import org.junit.Test;

import java.util.PrimitiveIterator;

public class JsonTest {
    @Test
    public void testObjectSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectSet<String> words = ObjectSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectSet words2 = json.fromJson(ObjectSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectSet<GridPoint2> points = ObjectSet.with(new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23));
        data = json.toJson(points);
        System.out.println(data);
        ObjectSet points2 = json.fromJson(ObjectSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }
    @Test
    public void testObjectOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectOrderedSet<String> words = ObjectOrderedSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectOrderedSet words2 = json.fromJson(ObjectOrderedSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectOrderedSet<GridPoint2> points = ObjectOrderedSet.with(new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23));
        data = json.toJson(points);
        System.out.println(data);
        ObjectOrderedSet points2 = json.fromJson(ObjectOrderedSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }
    @Test
    public void testIntSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        IntSet numbers = IntSet.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntSet numbers2 = json.fromJson(IntSet.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }
    @Test
    public void testIntOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        IntOrderedSet numbers = IntOrderedSet.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntOrderedSet numbers2 = json.fromJson(IntOrderedSet.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

}
