package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.github.tommyettinger.ds.ObjectObjectMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GdxJsonTest {
    @Test
    public void testArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        Array<String> words = Array.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words, Array.class);
        System.out.println(data);
        Array<?> words2 = json.fromJson(Array.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        Array<GridPoint2> points = Array.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, Array.class);
        System.out.println(data);
        Array<?> points2 = json.fromJson(Array.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
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
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
    }

    @Test
    public void testLongArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        LongArray numbers = LongArray.with(42, 23, 666666666666666L, 420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongArray numbers2 = json.fromJson(LongArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
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
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
    }

    @Test
    public void testCharArray() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        CharArray numbers = CharArray.with('l', 'i', 'b', 'G', 'D', 'X', '☀');
        String data = json.toJson(numbers);
        System.out.println(data);
        CharArray numbers2 = json.fromJson(CharArray.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
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
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
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
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
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
        Assert.assertEquals(words, words2);
        System.out.println();
        ObjectSet<GridPoint2> points = ObjectSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, ObjectSet.class, GridPoint2.class);
        System.out.println(data);
        ObjectSet<?> points2 = json.fromJson(ObjectSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
//        Assert.assertEquals(points, points2); // Surprisingly, this fails due to non-String items
        System.out.println();
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
        Assert.assertEquals(words, words2);
        System.out.println();
        OrderedSet<GridPoint2> points = OrderedSet.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points, OrderedSet.class, GridPoint2.class);
        System.out.println(data);
        OrderedSet<?> points2 = json.fromJson(OrderedSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
//        Assert.assertEquals(points, points2); // Surprisingly, this fails due to non-String items
        System.out.println();
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
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
    }

    @Test
    public void testObjectMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ObjectMap<String, GridPoint2> words = new ObjectMap<>(3);
        words.put("foo", new GridPoint2(42, 42));
        words.put("bar", new GridPoint2(23, 23));
        words.put("baz", new GridPoint2(666, 666));
        String data = json.toJson(words, ObjectMap.class);
        System.out.println(data);
        ObjectMap<?, ?> words2 = json.fromJson(ObjectMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ObjectMap<GridPoint2, String> points = new ObjectMap<>(3);
        points.put(new GridPoint2(42, 42), "foo");
        points.put(new GridPoint2(23, 23), "bar");
        points.put(new GridPoint2(666, 666), "baz");
        data = json.toJson(points, ObjectMap.class);
        System.out.println(data);
        ObjectMap<?, ?> points2 = json.fromJson(ObjectMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("("+pair.key.getClass()+")");
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("("+pair.value.getClass()+")");
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // fails because ObjectMap only permits String keys (undocumented).
        System.out.println();
    }

    @Test
    public void testOrderedMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        OrderedMap<String, GridPoint2> words = new OrderedMap<>(3);
        words.put("foo", new GridPoint2(42, 42));
        words.put("bar", new GridPoint2(23, 23));
        words.put("baz", new GridPoint2(666, 666));
        String data = json.toJson(words, OrderedMap.class);
        System.out.println(data);
        OrderedMap<?, ?> words2 = json.fromJson(OrderedMap.class, data);
        for(OrderedMap.Entry<?, ?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        OrderedMap<GridPoint2, String> points = new OrderedMap<>(3);
        points.put(new GridPoint2(42, 42), "foo");
        points.put(new GridPoint2(23, 23), "bar");
        points.put(new GridPoint2(666, 666), "baz");
        data = json.toJson(points, OrderedMap.class);
        System.out.println(data);
        OrderedMap<?, ?> points2 = json.fromJson(OrderedMap.class, data);
        for(OrderedMap.Entry<?, ?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // fails because OrderedMap only permits String keys (undocumented).
        System.out.println();
    }

    @Test
    public void testObjectLongMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ObjectLongMap<String> words = new ObjectLongMap<>(3);
        words.put("foo", 42L);
        words.put("bar", 23L);
        words.put("baz", 666666666666666L);
        String data = json.toJson(words);
        System.out.println(data);
        ObjectLongMap<?> words2 = json.fromJson(ObjectLongMap.class, data);
        for(ObjectLongMap.Entry<?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ObjectLongMap<GridPoint2> points = new ObjectLongMap<>(3);
        points.put(new GridPoint2(42, 42), 42);
        points.put(new GridPoint2(23, 23), 23);
        points.put(new GridPoint2(666, 666), 666666666666666L);
        data = json.toJson(points);
        System.out.println(data);
        ObjectLongMap<?> points2 = json.fromJson(ObjectLongMap.class, data);
        for(ObjectLongMap.Entry<?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(points, points2); // this one passes because it doesn't have any special serializer.
        System.out.println();
    }

    @Test
    public void testObjectFloatMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ObjectFloatMap<String> words = new ObjectFloatMap<>(3);
        words.put("foo", 42.42f);
        words.put("bar", 23.23f);
        words.put("baz", 666.666f);
        String data = json.toJson(words, ObjectFloatMap.class);
        System.out.println(data);
        ObjectFloatMap<?> words2 = json.fromJson(ObjectFloatMap.class, data);
        for(ObjectFloatMap.Entry<?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ObjectFloatMap<GridPoint2> points = new ObjectFloatMap<>(3);
        points.put(new GridPoint2(42, 42), 42.42f);
        points.put(new GridPoint2(23, 23), 23.23f);
        points.put(new GridPoint2(666, 666), 666.666f);
        data = json.toJson(points, ObjectFloatMap.class);
        System.out.println(data);
        ObjectFloatMap<?> points2 = json.fromJson(ObjectFloatMap.class, data);
        for(ObjectFloatMap.Entry<?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // fails because of non-String keys
        System.out.println();
    }

    @Test
    public void testObjectIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ObjectIntMap<String> words = new ObjectIntMap<>(3);
        words.put("foo", 42);
        words.put("bar", 23);
        words.put("baz", 666);
        String data = json.toJson(words, ObjectIntMap.class);
        System.out.println(data);
        ObjectIntMap<?> words2 = json.fromJson(ObjectIntMap.class, data);
        for(ObjectIntMap.Entry<?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ObjectIntMap<GridPoint2> points = new ObjectIntMap<>(3);
        points.put(new GridPoint2(42, 42), 42);
        points.put(new GridPoint2(23, 23), 23);
        points.put(new GridPoint2(666, 666), 666);
        data = json.toJson(points, ObjectIntMap.class);
        System.out.println(data);
        ObjectIntMap<?> points2 = json.fromJson(ObjectIntMap.class, data);
        for(ObjectIntMap.Entry<?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // Fails due to non-String keys
        System.out.println();
    }
    
    @Test
    public void testArrayMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        ArrayMap<String, GridPoint2> words = new ArrayMap<>(3);
        words.put("foo", new GridPoint2(42, 42));
        words.put("bar", new GridPoint2(23, 23));
        words.put("baz", new GridPoint2(666, 666));
        String data = json.toJson(words, ArrayMap.class);
        System.out.println(data);
        ArrayMap<?, ?> words2 = json.fromJson(ArrayMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ArrayMap<GridPoint2, String> points = new ArrayMap<>(3);
        points.put(new GridPoint2(42, 42), "foo");
        points.put(new GridPoint2(23, 23), "bar");
        points.put(new GridPoint2(666, 666), "baz");
        data = json.toJson(points, ArrayMap.class);
        System.out.println(data);
        ArrayMap<?, ?> points2 = json.fromJson(ArrayMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // There's some issue here with the non-String keys...
        System.out.println();
    }

    @Test
    public void testIdentityMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IdentityMap<String, GridPoint2> words = new IdentityMap<>(3);
        words.put("foo", new GridPoint2(42, 42));
        words.put("bar", new GridPoint2(23, 23));
        words.put("baz", new GridPoint2(666, 666));
        String data = json.toJson(words, IdentityMap.class);
        System.out.println(data);
        IdentityMap<?, ?> words2 = json.fromJson(IdentityMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(words, words2); // Fails because of, well, identity comparison.
        System.out.println();
        IdentityMap<GridPoint2, String> points = new IdentityMap<>(3);
        points.put(new GridPoint2(42, 42), "foo");
        points.put(new GridPoint2(23, 23), "bar");
        points.put(new GridPoint2(666, 666), "baz");
        data = json.toJson(points, IdentityMap.class);
        System.out.println(data);
        IdentityMap<?, ?> points2 = json.fromJson(IdentityMap.class, data);
        for(ObjectMap.Entry<?, ?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
//        Assert.assertEquals(points, points2); // Also fails because of, well, identity comparison.
        System.out.println();
    }

    @Test
    public void testIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IntMap<String> words = new IntMap<>(3);
        words.put(42 , "foo");
        words.put(23 , "bar");
        words.put(666, "baz");

        String data = json.toJson(words, IntMap.class);
        System.out.println(data);
        IntMap<?> words2 = json.fromJson(IntMap.class, data);
        for(IntMap.Entry<?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        IntMap<GridPoint2> points = new IntMap<>(3);
        points.put(42 , new GridPoint2(42, 42));
        points.put(23 , new GridPoint2(23, 23));
        points.put(666, new GridPoint2(666, 666));
        data = json.toJson(points, IntMap.class);
        System.out.println(data);
        IntMap<?> points2 = json.fromJson(IntMap.class, data);
        for(IntMap.Entry<?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
    }

    @Test
    public void testIntIntMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IntIntMap words = new IntIntMap(3);
        words.put(42 , 4242);
        words.put(23 , 2323);
        words.put(666, 666666);

        String data = json.toJson(words);
        System.out.println(data);
        IntIntMap words2 = json.fromJson(IntIntMap.class, data);
        for(IntIntMap.Entry pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
    }

    @Test
    public void testIntFloatMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        IntFloatMap words = new IntFloatMap(3);
        words.put(42 , 42.42f);
        words.put(23 , 23.23f);
        words.put(666, 666.666f);

        String data = json.toJson(words);
        System.out.println(data);
        IntFloatMap words2 = json.fromJson(IntFloatMap.class, data);
        for(IntFloatMap.Entry pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
    }

    @Test
    public void testLongMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        LongMap<String> words = new LongMap<>(3);
        words.put(42L, "foo");
        words.put(23L, "bar");
        words.put(66666666666666L, "baz");

        String data = json.toJson(words, LongMap.class);
        System.out.println(data);
        LongMap<?> words2 = json.fromJson(LongMap.class, data);
        for(LongMap.Entry<?> pair : words2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        LongMap<GridPoint2> points = new LongMap<>(3);
        points.put(42 , new GridPoint2(42, 42));
        points.put(23 , new GridPoint2(23, 23));
        points.put(666, new GridPoint2(666, 666));
        data = json.toJson(points, LongMap.class);
        System.out.println(data);
        Assert.assertEquals(words, words2);
        LongMap<?> points2 = json.fromJson(LongMap.class, data);
        for(LongMap.Entry<?> pair : points2) {
            System.out.print(pair.key);
            System.out.print("=");
            System.out.print(pair.value);
            System.out.print("; ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
    }

    @Test
    public void testQueue() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        Queue<String> words = new Queue<>(4);
        words.addLast("Peanut");
        words.addLast("Butter");
        words.addLast("Jelly");
        words.addLast("Time");
        String data = json.toJson(words, Queue.class, String.class);
        System.out.println(data);
        Queue<?> words2 = json.fromJson(Queue.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        Queue<GridPoint2> points = new Queue<>(3);
        points.addLast(new GridPoint2(42, 42));
        points.addLast(new GridPoint2(23, 23));
        points.addLast(new GridPoint2(666, 666));
        data = json.toJson(points, Queue.class); // don't specify an elementType!
        System.out.println(data);
        Queue<?> points2 = json.fromJson(Queue.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
    }

    @Test
    public void testLongQueue() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        LongQueue numbers = new LongQueue(4);
        numbers.addLast(42);
        numbers.addLast(23);
        numbers.addLast(666);
        numbers.addLast(420);
        String data = json.toJson(numbers);
        System.out.println(data);
        LongQueue numbers2 = json.fromJson(LongQueue.class, data);
        System.out.print(numbers2.get(0));
        for (int i = 1; i < numbers2.size; i++) {
            System.out.print(", ");
            System.out.print(numbers2.get(i));
        }
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
    }

    @Test
    public void testDeep() {
        ArrayList<ArrayList<HashMap<Vector2, String>>> deep = new ArrayList<>(8), after;
        HashMap<Vector2, String> hm0 = new HashMap<>(1);
        HashMap<Vector2, String> hm1 = new HashMap<>(ObjectObjectMap.with(new Vector2(1, 2), "1 2"));
        HashMap<Vector2, String> hm2 = new HashMap<>(ObjectObjectMap.with(new Vector2(3, 4), "3 4", new Vector2(5, 6), "5 6"));
        HashMap<Vector2, String> hm3 = new HashMap<>(ObjectObjectMap.with(new Vector2(7, 8), "7 8", new Vector2(9, 0), "9 0"));
        deep.add(new ArrayList<>(Arrays.asList(hm0, hm1)));
        deep.add(new ArrayList<>(Arrays.asList(hm2, hm3)));
        deep.add(new ArrayList<>(Arrays.asList(hm0, hm1, hm2, hm3)));

        Json json = new Json(JsonWriter.OutputType.javascript);
        json.setTypeName(null);
        String data = json.toJson(deep, ArrayList.class);
        System.out.println(data);
        after = json.fromJson(ArrayList.class, data);
        System.out.println(after);
//        Assert.assertEquals(deep, after); // fails because after contains JsonValue rather than HashMap items
    }

}
