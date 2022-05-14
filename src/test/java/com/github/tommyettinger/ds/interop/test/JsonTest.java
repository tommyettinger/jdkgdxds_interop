package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.interop.JsonSupport;
import com.github.tommyettinger.random.*;
import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.ds.support.util.*;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class JsonTest {
    @Test
    public void testObjectList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectList(json);
        ObjectList<String> words = ObjectList.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectList<?> words2 = json.fromJson(ObjectList.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectList<GridPoint2> points = ObjectList.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ObjectList<?> points2 = json.fromJson(ObjectList.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testIntList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntList(json);
        IntList numbers = IntList.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntList numbers2 = json.fromJson(IntList.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextInt());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testLongList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongList(json);
        LongList numbers = LongList.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongList numbers2 = json.fromJson(LongList.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextLong());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testFloatList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerFloatList(json);
        FloatList numbers = FloatList.with(42.42f, 23.23f, 666.666f, 420.42f);
        String data = json.toJson(numbers);
        System.out.println(data);
        FloatList numbers2 = json.fromJson(FloatList.class, data);
        FloatIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextFloat());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testByteList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerByteList(json);
        ByteList numbers = ByteList.with((byte) 42, (byte) 23, (byte) -66, (byte) -20);
        String data = json.toJson(numbers);
        System.out.println(data);
        ByteList numbers2 = json.fromJson(ByteList.class, data);
        ByteIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextByte());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testShortList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerShortList(json);
        ShortList numbers = ShortList.with((short) 42, (short) 23, (short) 666, (short) 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        ShortList numbers2 = json.fromJson(ShortList.class, data);
        ShortIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextShort());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testCharList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCharList(json);
        CharList numbers = CharList.with('a', 'b', 'y', 'z');
        String data = json.toJson(numbers);
        System.out.println(data);
        CharList numbers2 = json.fromJson(CharList.class, data);
        CharIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextChar());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testDoubleList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerDoubleList(json);
        DoubleList numbers = DoubleList.with(42.42, 23.23, 666.666, 420.42);
        String data = json.toJson(numbers);
        System.out.println(data);
        DoubleList numbers2 = json.fromJson(DoubleList.class, data);
        PrimitiveIterator.OfDouble it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextDouble());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testBooleanList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerBooleanList(json);
        BooleanList values = BooleanList.with(true, false, false, true);
        String data = json.toJson(values);
        System.out.println(data);
        BooleanList numbers2 = json.fromJson(BooleanList.class, data);
        BooleanIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextBoolean());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testObjectSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectSet(json);
        ObjectSet<String> words = ObjectSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectSet<?> words2 = json.fromJson(ObjectSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectSet<GridPoint2> points = ObjectSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ObjectSet<?> points2 = json.fromJson(ObjectSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testObjectOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectOrderedSet(json);
        ObjectOrderedSet<String> words = ObjectOrderedSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectOrderedSet<?> words2 = json.fromJson(ObjectOrderedSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectOrderedSet<GridPoint2> points = ObjectOrderedSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ObjectOrderedSet<?> points2 = json.fromJson(ObjectOrderedSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testIntSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntSet(json);
        IntSet numbers = IntSet.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntSet numbers2 = json.fromJson(IntSet.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextInt());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testIntOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntOrderedSet(json);
        IntOrderedSet numbers = IntOrderedSet.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntOrderedSet numbers2 = json.fromJson(IntOrderedSet.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextInt());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testLongSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongSet(json);
        LongSet numbers = LongSet.with(42L, 23L, 666666666666L, 4200000000000000L);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongSet numbers2 = json.fromJson(LongSet.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextLong());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testLongOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongOrderedSet(json);
        LongOrderedSet numbers = LongOrderedSet.with(42L, 23L, 666666666666L, 4200000000000000L);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongOrderedSet numbers2 = json.fromJson(LongOrderedSet.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextLong());
            if(it.hasNext())
                System.out.print(", ");
        }
    }

    @Test
    public void testObjectObjectMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectObjectMap(json);
        ObjectObjectMap<String, GridPoint2> words = new ObjectObjectMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
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
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
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
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectObjectOrderedMap(json);
        ObjectObjectOrderedMap<String, GridPoint2> words = new ObjectObjectOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
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
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
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
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectLongMap(json);
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
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
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

    @Test
    public void testObjectLongOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectLongOrderedMap(json);
        ObjectLongOrderedMap<String> words = new ObjectLongOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new long[]{42L, 23L, 666666666666L});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectLongOrderedMap<?> words2 = json.fromJson(ObjectLongOrderedMap.class, data);
        for(ObjectLongOrderedMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectLongOrderedMap<GridPoint2> points = new ObjectLongOrderedMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
                new long[]{42L, 23L, 666666666666L});
        data = json.toJson(points);
        System.out.println(data);
        ObjectLongOrderedMap<?> points2 = json.fromJson(ObjectLongOrderedMap.class, data);
        for(ObjectLongOrderedMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testObjectIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectIntMap(json);
        ObjectIntMap<String> words = new ObjectIntMap<>(new String[]{"foo", "bar", "baz"},
                new int[]{42, 23, 666});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectIntMap<?> words2 = json.fromJson(ObjectIntMap.class, data);
        for(ObjectIntMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectIntMap<GridPoint2> points = new ObjectIntMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
                new int[]{42, 23, 666});
        data = json.toJson(points);
        System.out.println(data);
        ObjectIntMap<?> points2 = json.fromJson(ObjectIntMap.class, data);
        for(ObjectIntMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testObjectIntOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectIntOrderedMap(json);
        ObjectIntOrderedMap<String> words = new ObjectIntOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new int[]{42, 23, 666});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectIntOrderedMap<?> words2 = json.fromJson(ObjectIntOrderedMap.class, data);
        for(ObjectIntOrderedMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectIntOrderedMap<GridPoint2> points = new ObjectIntOrderedMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
                new int[]{42, 23, 666});
        data = json.toJson(points);
        System.out.println(data);
        ObjectIntOrderedMap<?> points2 = json.fromJson(ObjectIntOrderedMap.class, data);
        for(ObjectIntOrderedMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }


    @Test
    public void testObjectFloatMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectFloatMap(json);
        ObjectFloatMap<String> words = new ObjectFloatMap<>(new String[]{"foo", "bar", "baz"},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectFloatMap<?> words2 = json.fromJson(ObjectFloatMap.class, data);
        for(ObjectFloatMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectFloatMap<GridPoint2> points = new ObjectFloatMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
                new float[]{42.42f, 23.23f, 666.666f});
        data = json.toJson(points);
        System.out.println(data);
        ObjectFloatMap<?> points2 = json.fromJson(ObjectFloatMap.class, data);
        for(ObjectFloatMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testObjectFloatOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectFloatOrderedMap(json);
        ObjectFloatOrderedMap<String> words = new ObjectFloatOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(words);
        System.out.println(data);
        ObjectFloatOrderedMap<?> words2 = json.fromJson(ObjectFloatOrderedMap.class, data);
        for(ObjectFloatOrderedMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        ObjectFloatOrderedMap<GridPoint2> points = new ObjectFloatOrderedMap<>(
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)},
                new float[]{42.42f, 23.23f, 666.666f});
        data = json.toJson(points);
        System.out.println(data);
        ObjectFloatOrderedMap<?> points2 = json.fromJson(ObjectFloatOrderedMap.class, data);
        for(ObjectFloatOrderedMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntObjectMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntObjectMap(json);
        IntObjectMap<String> words = new IntObjectMap<>(new int[]{42, 23, 666}, new String[]{"foo", "bar", "baz"});
        String data = json.toJson(words);
        System.out.println(data);
        IntObjectMap<?> words2 = json.fromJson(IntObjectMap.class, data);
        for(IntObjectMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        IntObjectMap<GridPoint2> points = new IntObjectMap<>(new int[]{42, 23, 666},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        data = json.toJson(points);
        System.out.println(data);
        IntObjectMap<?> points2 = json.fromJson(IntObjectMap.class, data);
        for(IntObjectMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntObjectOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntObjectOrderedMap(json);
        IntObjectOrderedMap<String> words = new IntObjectOrderedMap<>(new int[]{42, 23, 666}, new String[]{"foo", "bar", "baz"});
        String data = json.toJson(words);
        System.out.println(data);
        IntObjectOrderedMap<?> words2 = json.fromJson(IntObjectOrderedMap.class, data);
        for(IntObjectOrderedMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        IntObjectOrderedMap<GridPoint2> points = new IntObjectOrderedMap<>(new int[]{42, 23, 666},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        data = json.toJson(points);
        System.out.println(data);
        IntObjectOrderedMap<?> points2 = json.fromJson(IntObjectOrderedMap.class, data);
        for(IntObjectOrderedMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntIntMap(json);
        IntIntMap numbers = new IntIntMap(new int[]{42, 23, 666},
                new int[]{1, 10, 100});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntIntMap points2 = json.fromJson(IntIntMap.class, data);
        for(IntIntMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntIntOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntIntOrderedMap(json);
        IntIntOrderedMap numbers = new IntIntOrderedMap(new int[]{42, 23, 666},
                new int[]{1, 10, 100});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntIntOrderedMap points2 = json.fromJson(IntIntOrderedMap.class, data);
        for(IntIntOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntLongMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntLongMap(json);
        IntLongMap numbers = new IntLongMap(new int[]{42, 23, 666},
                new long[]{1L, 10000000000L, -1000000000000000L});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntLongMap points2 = json.fromJson(IntLongMap.class, data);
        for(IntLongMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntLongOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntLongOrderedMap(json);
        IntLongOrderedMap numbers = new IntLongOrderedMap(new int[]{42, 23, 666},
                new long[]{1L, 10000000000L, -1000000000000000L});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntLongOrderedMap points2 = json.fromJson(IntLongOrderedMap.class, data);
        for(IntLongOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testIntFloatMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntFloatMap(json);
        IntFloatMap numbers = new IntFloatMap(new int[]{42, 23, 666},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntFloatMap points2 = json.fromJson(IntFloatMap.class, data);
        for(IntFloatMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }


    @Test
    public void testIntFloatOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntFloatOrderedMap(json);
        IntFloatOrderedMap numbers = new IntFloatOrderedMap(new int[]{42, 23, 666},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(numbers);
        System.out.println(data);
        IntFloatOrderedMap points2 = json.fromJson(IntFloatOrderedMap.class, data);
        for(IntFloatOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongObjectMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongObjectMap(json);
        LongObjectMap<String> words = new LongObjectMap<>(new long[]{42L, 23L, 666666666666L},
                new String[]{"foo", "bar", "baz"});
        String data = json.toJson(words);
        System.out.println(data);
        LongObjectMap<?> words2 = json.fromJson(LongObjectMap.class, data);
        for(LongObjectMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        LongObjectMap<GridPoint2> points = new LongObjectMap<>(new long[]{42L, 23L, 666666666666L},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        data = json.toJson(points);
        System.out.println(data);
        LongObjectMap<?> points2 = json.fromJson(LongObjectMap.class, data);
        for(LongObjectMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }


    @Test
    public void testLongObjectOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongObjectOrderedMap(json);
        LongObjectOrderedMap<String> words = new LongObjectOrderedMap<>(new long[]{42L, 23L, 666666666666L},
                new String[]{"foo", "bar", "baz"});
        String data = json.toJson(words);
        System.out.println(data);
        LongObjectOrderedMap<?> words2 = json.fromJson(LongObjectOrderedMap.class, data);
        for(LongObjectOrderedMap.Entry<?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        System.out.println();
        LongObjectOrderedMap<GridPoint2> points = new LongObjectOrderedMap<>(new long[]{42L, 23L, 666666666666L},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        data = json.toJson(points);
        System.out.println(data);
        LongObjectOrderedMap<?> points2 = json.fromJson(LongObjectOrderedMap.class, data);
        for(LongObjectOrderedMap.Entry<?> pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongIntMap(json);
        LongIntMap numbers = new LongIntMap(new long[]{42L, 23L, 666666666666L},
                new int[]{1, 10, 100});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongIntMap points2 = json.fromJson(LongIntMap.class, data);
        for(LongIntMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongIntOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongIntOrderedMap(json);
        LongIntOrderedMap numbers = new LongIntOrderedMap(new long[]{42L, 23L, 666666666666L},
                new int[]{1, 10, 100});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongIntOrderedMap points2 = json.fromJson(LongIntOrderedMap.class, data);
        for(LongIntOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongLongMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongLongMap(json);
        LongLongMap numbers = new LongLongMap(new long[]{42L, 23L, 666666666666L},
                new long[]{1L, 10000000000L, -1000000000000000L});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongLongMap points2 = json.fromJson(LongLongMap.class, data);
        for(LongLongMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }


    @Test
    public void testLongLongOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongLongOrderedMap(json);
        LongLongOrderedMap numbers = new LongLongOrderedMap(new long[]{42L, 23L, 666666666666L},
                new long[]{1L, 10000000000L, -1000000000000000L});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongLongOrderedMap points2 = json.fromJson(LongLongOrderedMap.class, data);
        for(LongLongOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongFloatMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongFloatMap(json);
        LongFloatMap numbers = new LongFloatMap(new long[]{42L, 23L, 666666666666L},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongFloatMap points2 = json.fromJson(LongFloatMap.class, data);
        for(LongFloatMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testLongFloatOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongFloatOrderedMap(json);
        LongFloatOrderedMap numbers = new LongFloatOrderedMap(new long[]{42L, 23L, 666666666666L},
                new float[]{42.42f, 23.23f, 666.666f});
        String data = json.toJson(numbers);
        System.out.println(data);
        LongFloatOrderedMap points2 = json.fromJson(LongFloatOrderedMap.class, data);
        for(LongFloatOrderedMap.Entry pair : points2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testCaseInsensitiveSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCaseInsensitiveSet(json);
        CaseInsensitiveSet words = CaseInsensitiveSet.with("Peanut", "Butter", "Jelly", "Time", "peanut", "butter", "jelly", "TIME");
        String data = json.toJson(words);
        System.out.println(data);
        CaseInsensitiveSet words2 = json.fromJson(CaseInsensitiveSet.class, data);
        for (Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
    }

    @Test
    public void testCaseInsensitiveOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCaseInsensitiveOrderedSet(json);
        CaseInsensitiveOrderedSet words = CaseInsensitiveOrderedSet.with("Peanut", "Butter", "Jelly", "Time", "peanut", "butter", "jelly", "TIME");
        String data = json.toJson(words);
        System.out.println(data);
        CaseInsensitiveOrderedSet words2 = json.fromJson(CaseInsensitiveOrderedSet.class, data);
        for (Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
    }

    @Test
    public void testCaseInsensitiveMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCaseInsensitiveMap(json);
        CaseInsensitiveMap<GridPoint2> words = new CaseInsensitiveMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        String data = json.toJson(words);
        System.out.println(data);
        CaseInsensitiveMap<?> words2 = json.fromJson(CaseInsensitiveMap.class, data);
        for(Map.Entry<CharSequence, ?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testCaseInsensitiveOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCaseInsensitiveOrderedMap(json);
        CaseInsensitiveOrderedMap<GridPoint2> words = new CaseInsensitiveOrderedMap<>(new String[]{"foo", "bar", "baz"},
                new GridPoint2[]{new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666)});
        String data = json.toJson(words);
        System.out.println(data);
        CaseInsensitiveOrderedMap<?> words2 = json.fromJson(CaseInsensitiveOrderedMap.class, data);
        for(Map.Entry<CharSequence, ?> pair : words2) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
    }

    @Test
    public void testNumberedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerNumberedSet(json);
        NumberedSet<String> words = NumberedSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        NumberedSet<?> words2 = json.fromJson(NumberedSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        NumberedSet<GridPoint2> points = NumberedSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        NumberedSet<?> points2 = json.fromJson(NumberedSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    public static class TestNode<E> extends BinaryHeap.Node {

        public E element;

        TestNode(){
            super(0f);
            this.element = null;
        }
        /**
         * @param value The initial value for the node. To change the value, use {@link BinaryHeap.Node#add(BinaryHeap.Node, float)} if the node is
         *              not in the heap, or {@link BinaryHeap.Node#setValue(BinaryHeap.Node, float)} if the node is in the heap.
         */
        public TestNode(E element, float value) {
            super(value);
            this.element = element;
        }
    }

    @Test
    public void testBinaryHeap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerBinaryHeap(json);
        ObjectList<TestNode<String>> words = ObjectList.with(
                new TestNode<>("Time", -1000000000f), new TestNode<>("Butter", 0.125f),
                new TestNode<>("Jelly", -0.125f), new TestNode<>("Peanut", Float.POSITIVE_INFINITY));
        BinaryHeap<TestNode<String>> heap = new BinaryHeap<>(true, words);
        String data = json.toJson(heap);
        System.out.println(data);
        BinaryHeap<TestNode<String>> heap2 = json.fromJson(BinaryHeap.class, data);
        for(TestNode<String> word : heap2) {
            System.out.print(word.element);
            System.out.print(", ");
        }
    }

    @Test
    public void testLaserRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLaserRandom(json);
        LaserRandom random = new LaserRandom(123456789, 0xFA7BAB1E5L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        LaserRandom random2 = json.fromJson(LaserRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testMizuchiRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerMizuchiRandom(json);
        MizuchiRandom random = new MizuchiRandom(123456789, 0xFA7BAB1E5L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        MizuchiRandom random2 = json.fromJson(MizuchiRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testTricycleRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerTricycleRandom(json);
        TricycleRandom random = new TricycleRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        TricycleRandom random2 = json.fromJson(TricycleRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testRomuTrioRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerRomuTrioRandom(json);
        RomuTrioRandom random = new RomuTrioRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        RomuTrioRandom random2 = json.fromJson(RomuTrioRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testFourWheelRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerFourWheelRandom(json);
        FourWheelRandom random = new FourWheelRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        FourWheelRandom random2 = json.fromJson(FourWheelRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateD()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testStrangerRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerStrangerRandom(json);
        StrangerRandom random = new StrangerRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        StrangerRandom random2 = json.fromJson(StrangerRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateD()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testXoshiro256StarStarRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerXoshiro256StarStarRandom(json);
        Xoshiro256StarStarRandom random = new Xoshiro256StarStarRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        Xoshiro256StarStarRandom random2 = json.fromJson(Xoshiro256StarStarRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateD()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testTrimRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerTrimRandom(json);
        TrimRandom random = new TrimRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        TrimRandom random2 = json.fromJson(TrimRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateC()));
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getStateD()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testChopRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
//        JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerChopRandom(json);
        ChopRandom random = new ChopRandom(123456789, 0xBAB1E5, 0xB0BAFE77, 0x12341234);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        ChopRandom random2 = json.fromJson(ChopRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned((int) random2.getStateA()));
        System.out.println(JsonSupport.getNumeralBase().unsigned((int) random2.getStateB()));
        System.out.println(JsonSupport.getNumeralBase().unsigned((int) random2.getStateC()));
        System.out.println(JsonSupport.getNumeralBase().unsigned((int) random2.getStateD()));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testAtomicLong() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        FourWheelRandom random = new FourWheelRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        AtomicLong al = new AtomicLong(Base.BASE36.readLong("json"));
        JsonSupport.registerAtomicLong(json);
        System.out.println(json.toJson(random));
        System.out.println(json.toJson(al));
    }
    @Test
    public void testDistinctRandom() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerDistinctRandom(json);
        DistinctRandom random = new DistinctRandom(123456789);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        DistinctRandom random2 = json.fromJson(DistinctRandom.class, data);
        System.out.println(JsonSupport.getNumeralBase().unsigned(random2.getSelectedState(0)));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testRandomXS128() {
        JsonSupport.setNumeralBase(Base.scrambledBase(new DistinctRandom()));
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerRandomXS128(json);
        RandomXS128 random = new RandomXS128(123456789, 0xFA7BAB1E5L);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        RandomXS128 random2 = json.fromJson(RandomXS128.class, data);
        System.out.println(JsonSupport.getNumeralBase().signed(random2.getState(0)));
        System.out.println(JsonSupport.getNumeralBase().signed(random2.getState(1)));
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testObjectDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerObjectDeque(json);
        ObjectDeque<String> words = ObjectDeque.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ObjectDeque<?> words2 = json.fromJson(ObjectDeque.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectDeque<GridPoint2> points = ObjectDeque.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ObjectDeque<?> points2 = json.fromJson(ObjectDeque.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testLongDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerLongDeque(json);
        LongDeque numbers = LongDeque.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongDeque numbers2 = json.fromJson(LongDeque.class, data);
        PrimitiveIterator.OfLong it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextLong());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testIntDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerIntDeque(json);
        IntDeque numbers = IntDeque.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntDeque numbers2 = json.fromJson(IntDeque.class, data);
        PrimitiveIterator.OfInt it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextInt());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testCharDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerCharDeque(json);
        CharDeque numbers = CharDeque.with("foobar".toCharArray());
        String data = json.toJson(numbers);
        System.out.println(data);
        CharDeque numbers2 = json.fromJson(CharDeque.class, data);
        CharIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextChar());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testShortDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerShortDeque(json);
        ShortDeque numbers = ShortDeque.with((short) 42, (short) 23, (short) -666, (short) -420);
        String data = json.toJson(numbers);
        System.out.println(data);
        ShortDeque numbers2 = json.fromJson(ShortDeque.class, data);
        ShortIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextShort());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testByteDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerByteDeque(json);
        ByteDeque numbers = ByteDeque.with((byte)42, (byte)23, (byte)-66, (byte)-20);
        String data = json.toJson(numbers);
        System.out.println(data);
        ByteDeque numbers2 = json.fromJson(ByteDeque.class, data);
        ByteIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextByte());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testFloatDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerFloatDeque(json);
        FloatDeque numbers = FloatDeque.with(42.42f, 23.23f, 666.666f, 420.42f, 4.7683716e-7f);
        String data = json.toJson(numbers);
        System.out.println(data);
        FloatDeque numbers2 = json.fromJson(FloatDeque.class, data);
        FloatIterator it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextFloat());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    @Test
    public void testDoubleDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        JsonSupport.registerDoubleDeque(json);
        DoubleDeque numbers = DoubleDeque.with(42.42, 23.23, 666.666, 420.42);
        String data = json.toJson(numbers);
        System.out.println(data);
        DoubleDeque numbers2 = json.fromJson(DoubleDeque.class, data);
        PrimitiveIterator.OfDouble it = numbers2.iterator();
        while (it.hasNext()){
            System.out.print(it.nextDouble());
            if(it.hasNext())
                System.out.print(", ");
        }
        System.out.println();
    }

    public static class Composite {
        public EnhancedRandom random;
        public ObjectObjectMap<String, String> mapping;

        public Composite(){
            this(new LaserRandom(), new ObjectObjectMap<>());
        }
        public Composite(EnhancedRandom random, ObjectObjectMap<String, String> mapping) {
            this.random = random;
            this.mapping = mapping;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Composite composite = (Composite) o;

            if (random != null ? !random.equals(composite.random) : composite.random != null) return false;
            return mapping != null ? mapping.equals(composite.mapping) : composite.mapping == null;
        }

        @Override
        public int hashCode() {
            int result = random != null ? random.hashCode() : 0;
            result = 31 * result + (mapping != null ? mapping.hashCode() : 0);
            return result;
        }
    }

    public static void registerComposite(@Nonnull Json json) {
        JsonSupport.registerObjectObjectMap(json);
        JsonSupport.registerEnhancedRandom(json);
        json.setSerializer(Composite.class, new Json.Serializer<Composite>() {
            @Override
            public void write(Json json, Composite object, Class knownType) {
                json.writeArrayStart();
                json.writeValue(object.random, EnhancedRandom.class);
                json.writeValue(object.mapping, ObjectObjectMap.class);
                json.writeArrayEnd();
            }

            @Override
            public Composite read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue current = jsonData.child;
                EnhancedRandom random = json.readValue(EnhancedRandom.class, current);
                current = current.next;
                ObjectObjectMap<String, String> mapping = json.readValue(ObjectObjectMap.class, current);
                return new Composite(random, mapping);
            }
        });
    }
    @Test
    public void testComposite() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        registerComposite(json);
        ObjectObjectMap<String, String> mapping = ObjectObjectMap.with("peanut", "0", "butter", "1", "jelly", "2", "time", "3");
        FourWheelRandom random = new FourWheelRandom(123);
        Composite composite = new Composite(random, mapping);
        String data = json.toJson(composite);
        System.out.println(data);
        Composite composite2 = json.fromJson(Composite.class, data);
        System.out.println(composite.mapping.toString());
        System.out.println(composite.random.nextInt());
        System.out.println(composite2.mapping.toString());
        System.out.println(composite2.random.nextInt());
        System.out.println();
    }

    @Test
    public void testDeep() {
        ObjectList<ObjectList<ObjectObjectMap<Vector2, String>>> deep = new ObjectList<>(8), after;
        ObjectObjectMap<Vector2, String> hm0 = new ObjectObjectMap<>(1);
        ObjectObjectMap<Vector2, String> hm1 = ObjectObjectMap.with(new Vector2(1, 2), "1 2");
        ObjectObjectMap<Vector2, String> hm2 = ObjectObjectMap.with(new Vector2(3, 4), "3 4", new Vector2(5, 6), "5 6");
        ObjectObjectMap<Vector2, String> hm3 = ObjectObjectMap.with(new Vector2(7, 8), "7 8", new Vector2(9, 0), "9 0");
        deep.add(ObjectList.with(hm0, hm1));
        deep.add(ObjectList.with(hm2, hm3));
        deep.add(ObjectList.with(hm0, hm1, hm2, hm3));

        Json json = new Json(JsonWriter.OutputType.javascript);
        JsonSupport.registerObjectList(json);
        JsonSupport.registerObjectObjectMap(json);
        json.setTypeName(null);
        String data = json.toJson(deep);
        System.out.println(data);
        after = json.fromJson(ObjectList.class, data);
        System.out.println(after);
    }

}
