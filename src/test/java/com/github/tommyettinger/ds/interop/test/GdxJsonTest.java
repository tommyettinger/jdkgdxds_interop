package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.*;
import org.junit.Test;

public class GdxJsonTest {
    @Test
    public void testArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        Array<String> words = Array.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words, Array.class, String.class);
        System.out.println(data);
        Array<?> words2 = json.fromJson(Array.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        Array<GridPoint2> points = Array.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, Array.class, GridPoint2.class);
        System.out.println(data);
        Array<?> points2 = json.fromJson(Array.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testIntArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IntArray numbers = IntArray.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        IntArray numbers2 = json.fromJson(IntArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testLongArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        LongArray numbers = LongArray.with(42, 23, 666, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongArray numbers2 = json.fromJson(LongArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testFloatArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        FloatArray numbers = FloatArray.with(42.42f, 23.23f, 666.666f, 420.420f);
        String data = json.toJson(numbers);
        System.out.println(data);
        FloatArray numbers2 = json.fromJson(FloatArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testCharArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        CharArray numbers = CharArray.with('l', 'i', 'b', 'G', 'D', 'X', '\u2600');
        String data = json.toJson(numbers);
        System.out.println(data);
        CharArray numbers2 = json.fromJson(CharArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testByteArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ByteArray numbers = ByteArray.with((byte) 42, (byte)23, (byte)666, (byte)420);
        String data = json.toJson(numbers);
        System.out.println(data);
        ByteArray numbers2 = json.fromJson(ByteArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testShortArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ShortArray numbers = ShortArray.with((short) 42, (short) 23, (short) 666, (short) 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        ShortArray numbers2 = json.fromJson(ShortArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
    }

    @Test
    public void testObjectSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ObjectSet<String> words = ObjectSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words, ObjectSet.class, String.class);
        System.out.println(data);
        ObjectSet<?> words2 = json.fromJson(ObjectSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ObjectSet<GridPoint2> points = ObjectSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, ObjectSet.class, GridPoint2.class);
        System.out.println(data);
        ObjectSet<?> points2 = json.fromJson(ObjectSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testOrderedSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        OrderedSet<String> words = OrderedSet.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words, OrderedSet.class, String.class);
        System.out.println(data);
        OrderedSet<?> words2 = json.fromJson(OrderedSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        OrderedSet<GridPoint2> points = OrderedSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, OrderedSet.class, GridPoint2.class);
        System.out.println(data);
        OrderedSet<?> points2 = json.fromJson(OrderedSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
    }

    @Test
    public void testIntSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IntSet numbers = IntSet.with(42, 23, 666, 420);
        String data = json.toJson(numbers, IntSet.class);
        System.out.println(data);
        IntSet numbers2 = json.fromJson(IntSet.class, data);
        IntSet.IntSetIterator it = numbers2.iterator();
        while (it.hasNext){
            System.out.print(it.next());
            if(it.hasNext)
                System.out.print(", ");
        }
    }

}
