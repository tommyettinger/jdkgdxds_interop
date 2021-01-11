package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.interop.JsonSupport;
import com.github.tommyettinger.ds.support.util.FloatIterator;
import org.junit.Test;

import java.util.Map;
import java.util.PrimitiveIterator;

public class JsonTest {
    @Test
    public void testObjectList() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectList<String> words = ObjectList.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectList words2 = json.fromJson(ObjectList.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectList<GridPoint2> points = ObjectList.with(new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23));
        data = json.toJson(points);
        System.out.println(data);
        ObjectList points2 = json.fromJson(ObjectList.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testIntList() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        IntList numbers = IntList.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntList numbers2 = json.fromJson(IntList.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testLongList() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        LongList numbers = LongList.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongList numbers2 = json.fromJson(LongList.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testFloatList() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        FloatList numbers = FloatList.with(42.42f, 23.23f, 666.666f, 420.42f);
        String data = json.toJson(numbers);
        System.out.println(data);
        FloatList numbers2 = json.fromJson(FloatList.class, data);
        FloatIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

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
    @Test
    public void testLongSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        LongSet numbers = LongSet.with(42L, 23L, 666666666666L, 4200000000000000L);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongSet numbers2 = json.fromJson(LongSet.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }
    @Test
    public void testLongOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        LongOrderedSet numbers = LongOrderedSet.with(42L, 23L, 666666666666L, 4200000000000000L);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongOrderedSet numbers2 = json.fromJson(LongOrderedSet.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.next());
            if(it.hasNext())
                System.out.print(", ");
        }
    }
    @Test
    public void testObjectObjectMap() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectObjectMap<String, GridPoint2> words = new ObjectObjectMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23)});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectObjectMap<?, ?> words2 = json.fromJson(ObjectObjectMap.class, data);
        for(Map.Entry<?, ?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectObjectMap<GridPoint2, String> points = new ObjectObjectMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23)},
                new String[]{"foo", "bar", "baz"});
        data = json.toJson(points);
        System.out.println(data);
        ObjectObjectMap<?, ?> points2 = json.fromJson(ObjectObjectMap.class, data);
        for(Map.Entry<?, ?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testObjectObjectOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectObjectOrderedMap<String, GridPoint2> words = new ObjectObjectOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23)});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectObjectOrderedMap<?, ?> words2 = json.fromJson(ObjectObjectOrderedMap.class, data);
        for(Map.Entry<?, ?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectObjectOrderedMap<GridPoint2, String> points = new ObjectObjectOrderedMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23)},
                new String[]{"foo", "bar", "baz"});
        data = json.toJson(points);
        System.out.println(data);
        ObjectObjectOrderedMap<?, ?> points2 = json.fromJson(ObjectObjectOrderedMap.class, data);
        for(Map.Entry<?, ?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }
    @Test
    public void testObjectLongMap() {
        Json json = new Json(JsonWriter.OutputType.json);
        JsonSupport.registerWith(json);
        ObjectLongMap<String> words = new ObjectLongMap<>(new String[]{"foo", "bar", "baz"},
                new long[]{42L, 23L, 666666666666L});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectLongMap<?> words2 = json.fromJson(ObjectLongMap.class, data);
        for(ObjectLongMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectLongMap<GridPoint2> points = new ObjectLongMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(666, 666), new GridPoint2(23, 23)},
                new long[]{42L, 23L, 666666666666L});
        data = json.toJson(points);
        System.out.println(data);
        ObjectLongMap<?> points2 = json.fromJson(ObjectLongMap.class, data);
        for(ObjectLongMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

}
