/*
 * Copyright (c) 2023 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tommyettinger.ds.interop.test;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.ds.interop.JsonSupport;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class JdkJsonTest {
    @Test(expected = AssertionError.class)
    public void testRandom() {
        JsonSupport.setNumeralBase(Base.BASE10);
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerAlternateRandom(json);
        Random random = new Random(123456789);
        random.nextLong();
        String data = json.toJson(random);
        System.out.println(data);
        Random random2 = json.fromJson(Random.class, data);
        Assert.assertEquals(random.nextLong(), random2.nextLong());
    }

    @Test
    public void testArrayList() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerArrayList(json);
        ArrayList<String> words = Maker.AL.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ArrayList<?> words2 = json.fromJson(ArrayList.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        ArrayList<GridPoint2> points = Maker.AL.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ArrayList<?> points2 = json.fromJson(ArrayList.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();

    }

    @Test
    public void testHashSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerHashSet(json);
        HashSet<String> words = Maker.HS.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        HashSet<?> words2 = json.fromJson(HashSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        HashSet<GridPoint2> points = Maker.HS.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        HashSet<?> points2 = json.fromJson(HashSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
    }

    @Test
    public void testLinkedHashSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerLinkedHashSet(json);
        LinkedHashSet<String> words = Maker.LHS.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        LinkedHashSet<?> words2 = json.fromJson(LinkedHashSet.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        Assert.assertEquals(words, words2);
        System.out.println();
        LinkedHashSet<GridPoint2> points = Maker.LHS.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        LinkedHashSet<?> points2 = json.fromJson(LinkedHashSet.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        Assert.assertEquals(points, points2);
        System.out.println();
    }
    @Test
    public void testHashMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerHashMap(json);
        HashMap<String, GridPoint2> words = Maker.HM.with("foo", new GridPoint2(42, 42), "bar", new GridPoint2(23, 23), "baz", new GridPoint2(666, 666));
        String data = json.toJson(words, HashMap.class);
        System.out.println(data);
        HashMap<?, ?> words2 = json.fromJson(HashMap.class, data);
        for(Map.Entry<?, ?> pair : words2.entrySet()) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
//        System.out.println();
//        HashMap<GridPoint2, String> points = Maker.HM.with(new GridPoint2(42, 42), "foo", new GridPoint2(23, 23), "bar", new GridPoint2(666, 666), "baz");
//        data = json.toJson(points);
//        System.out.println(data);
//        HashMap<?, ?> points2 = json.fromJson(HashMap.class, data);
//        for(Map.Entry<?, ?> pair : points2.entrySet()) {
//            System.out.print(pair.getKey());
//            System.out.print("=");
//            System.out.print(pair.getValue());
//            System.out.print("; ");
//        }
//        Assert.assertEquals(points, points2);
    }

    @Test
    public void testLinkedHashMap() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerLinkedHashMap(json);
        LinkedHashMap<String, GridPoint2> words = Maker.LHM.with("foo", new GridPoint2(42, 42), "bar", new GridPoint2(23, 23), "baz", new GridPoint2(666, 666));
        String data = json.toJson(words);
        System.out.println(data);
        LinkedHashMap<?, ?> words2 = json.fromJson(LinkedHashMap.class, data);
        for(Map.Entry<?, ?> pair : words2.entrySet()) {
            System.out.print(pair.getKey());
            System.out.print("=");
            System.out.print(pair.getValue());
            System.out.print("; ");
        }
        Assert.assertEquals(words, words2);
//        System.out.println();
//        LinkedHashMap<GridPoint2, String> points = Maker.LHM.with(new GridPoint2(42, 42), "foo", new GridPoint2(23, 23), "bar", new GridPoint2(666, 666), "baz");
//        data = json.toJson(points);
//        System.out.println(data);
//        LinkedHashMap<?, ?> points2 = json.fromJson(LinkedHashMap.class, data);
//        for(Map.Entry<?, ?> pair : points2.entrySet()) {
//            System.out.print(pair.getKey());
//            System.out.print("=");
//            System.out.print(pair.getValue());
//            System.out.print("; ");
//        }
//        Assert.assertEquals(points, points2);
    }

    @Test(expected = AssertionError.class)
    public void testOffsetBitSet() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerOffsetBitSet(json);
        BitSet numbers = new BitSet(9);
        numbers.set(42);
        numbers.set(23);
        numbers.set(666);
        numbers.set(420);
        String data = json.toJson(numbers);
        System.out.println(data);
        BitSet numbers2 = json.fromJson(BitSet.class, data);
        PrimitiveIterator.OfInt it = numbers2.stream().iterator();
        while (it.hasNext()){
            System.out.print(it.nextInt());
            if(it.hasNext())
                System.out.print(", ");
        }
        Assert.assertEquals(numbers, numbers2);
        System.out.println();
    }

    @Test
    public void testAtomicLong() {
        JsonSupport.setNumeralBase(Base.BASE10);
        //JsonSupport.setNumeralBase(Base.BASE16);
        Json json = new Json(JsonWriter.OutputType.minimal);
        // Serializing subclasses of java.util.Random really needs the classes registered.
//        FourWheelRandom random = new FourWheelRandom(123456789, 0xFA7BAB1E5L, 0xB0BAFE77L, 0x1234123412341234L);
        AtomicLong al = new AtomicLong(Base.BASE36.readLong("json"));
        //JsonSupport.registerAtomicLong(json);
//        System.out.println(json.toJson(random));
        System.out.println(json.toJson(al));
    }

    @Test
    public void testObjectDeque() {
        Json json = new Json(JsonWriter.OutputType.minimal);
        //JsonSupport.registerObjectDeque(json);
        ArrayDeque<String> words = Maker.AD.with("Peanut", "Butter", "Jelly", "Time");
        String data = json.toJson(words);
        System.out.println(data);
        ArrayDeque<?> words2 = json.fromJson(ArrayDeque.class, data);
        for(Object word : words2) {
            System.out.print(word);
            System.out.print(", ");
        }
        System.out.println();
        ArrayDeque<GridPoint2> points = Maker.AD.with(new GridPoint2(42, 42), new GridPoint2(23, 23), new GridPoint2(666, 666));
        data = json.toJson(points);
        System.out.println(data);
        ArrayDeque<?> points2 = json.fromJson(ArrayDeque.class, data);
        for(Object point : points2) {
            System.out.print(point);
            System.out.print(", ");
        }
        System.out.println();
    }
    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    @Test(expected = ClassCastException.class)
    public void testDeep() {
//        json.addClassTag("str", String.class);
//        json.setTypeName("=");
        {
            ArrayList<ArrayList<HashMap<Vector2, String>>> deep = new ArrayList<>(8), after;
            HashMap<Vector2, String> hm0 = new HashMap<>(1);
            HashMap<Vector2, String> hm1 = Maker.HM.with(new Vector2(1, 2), "1 2");
            HashMap<Vector2, String> hm2 = Maker.HM.with(new Vector2(3, 4), "3 4", new Vector2(5, 6), "5 6");
            HashMap<Vector2, String> hm3 = Maker.HM.with(new Vector2(7, 8), "7 8", new Vector2(9, 0), "9 0");
            deep.add(Maker.AL.with(hm1, hm0));
            deep.add(Maker.AL.with(hm2, hm3));
            deep.add(Maker.AL.with(hm0, hm1, hm2, hm3));

            Json json = new Json(JsonWriter.OutputType.minimal);
            //JsonSupport.registerArrayList(json);
            //JsonSupport.registerHashMap(json);
            String data = json.toJson(deep);
            System.out.println(data);
            after = json.fromJson(ArrayList.class, data);
            System.out.println(after);
            System.out.println(after.getClass());
            System.out.println(after.get(0).getClass());
            System.out.println(after.get(0).get(0).getClass());
            System.out.println(after.get(0).get(0).values().iterator().next().getClass());
        }
        {
            ArrayList<ArrayList<LinkedHashMap<Vector2, String>>> deep = new ArrayList<>(8), after;
            LinkedHashMap<Vector2, String> hm0 = new LinkedHashMap<>(1);
            LinkedHashMap<Vector2, String> hm1 = Maker.LHM.with(new Vector2(1, 2), "1 2");
            LinkedHashMap<Vector2, String> hm2 = Maker.LHM.with(new Vector2(3, 4), "3 4", new Vector2(5, 6), "5 6");
            LinkedHashMap<Vector2, String> hm3 = Maker.LHM.with(new Vector2(7, 8), "7 8", new Vector2(9, 0), "9 0");
            deep.add(Maker.AL.with(hm1, hm0));
            deep.add(Maker.AL.with(hm2, hm3));
            deep.add(Maker.AL.with(hm0, hm1, hm2, hm3));

            Json json = new Json(JsonWriter.OutputType.minimal);
            //JsonSupport.registerArrayList(json);
            //JsonSupport.registerLinkedHashMap(json);
            String data = json.toJson(deep);
            System.out.println(data);
            after = json.fromJson(ArrayList.class, data);
            System.out.println(after);
            System.out.println(after.getClass());
            System.out.println(after.get(0).getClass());
            System.out.println(after.get(0).get(0).getClass());
            System.out.println(after.get(0).get(0).values().iterator().next().getClass());
        }
        {
            ArrayList<ArrayList<HashSet<String>>> deep = new ArrayList<>(8), after;
            HashSet<String> hm0 = new HashSet<>(1);
            HashSet<String> hm1 = Maker.HS.with("hm1 hm0");
            HashSet<String> hm2 = Maker.HS.with("hm2 hm3", "HAM 123");
            HashSet<String> hm3 = Maker.HS.with("hm0 hm1", "HAM 456");
            deep.add(Maker.AL.with(hm1, hm0));
            deep.add(Maker.AL.with(hm2, hm3));
            deep.add(Maker.AL.with(hm0, hm1, hm2, hm3));

            Json json = new Json(JsonWriter.OutputType.minimal);
            //JsonSupport.registerArrayList(json);
            //JsonSupport.registerHashSet(json);
            String data = json.toJson(deep);
            System.out.println(data);
            after = json.fromJson(ArrayList.class, data);
            System.out.println(after);
            System.out.println(after.getClass());
            System.out.println(after.get(0).getClass());
            System.out.println(after.get(0).get(0).getClass());
            System.out.println(after.get(0).get(0).iterator().next().getClass());
        }
        {
            ArrayList<ArrayList<LinkedHashSet<String>>> deep = new ArrayList<>(8), after;
            LinkedHashSet<String> hm0 = new LinkedHashSet<>(1);
            LinkedHashSet<String> hm1 = Maker.LHS.with("hm1 hm0");
            LinkedHashSet<String> hm2 = Maker.LHS.with("hm2 hm3", "HAM 123");
            LinkedHashSet<String> hm3 = Maker.LHS.with("hm0 hm1", "HAM 456");
            deep.add(Maker.AL.with(hm1, hm0));
            deep.add(Maker.AL.with(hm2, hm3));
            deep.add(Maker.AL.with(hm0, hm1, hm2, hm3));

            Json json = new Json(JsonWriter.OutputType.minimal);
            //JsonSupport.registerArrayList(json);
            //JsonSupport.registerLinkedHashSet(json);
            String data = json.toJson(deep);
            System.out.println(data);
            after = json.fromJson(ArrayList.class, data);
            System.out.println(after);
            System.out.println(after.getClass());
            System.out.println(after.get(0).getClass());
            System.out.println(after.get(0).get(0).getClass());
            System.out.println(after.get(0).get(0).iterator().next().getClass());
        }
    }
}
