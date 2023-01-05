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

package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.BooleanArray;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.CharArray;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongArray;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.LongQueue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ShortArray;
import com.github.tommyettinger.ds.BooleanList;
import com.github.tommyettinger.ds.ByteDeque;
import com.github.tommyettinger.ds.ByteList;
import com.github.tommyettinger.ds.CaseInsensitiveMap;
import com.github.tommyettinger.ds.CaseInsensitiveOrderedMap;
import com.github.tommyettinger.ds.CaseInsensitiveOrderedSet;
import com.github.tommyettinger.ds.CaseInsensitiveSet;
import com.github.tommyettinger.ds.CharDeque;
import com.github.tommyettinger.ds.CharList;
import com.github.tommyettinger.ds.FloatDeque;
import com.github.tommyettinger.ds.FloatList;
import com.github.tommyettinger.ds.IdentityObjectMap;
import com.github.tommyettinger.ds.IdentityObjectOrderedMap;
import com.github.tommyettinger.ds.IntDeque;
import com.github.tommyettinger.ds.IntFloatMap;
import com.github.tommyettinger.ds.IntFloatOrderedMap;
import com.github.tommyettinger.ds.IntIntMap;
import com.github.tommyettinger.ds.IntIntOrderedMap;
import com.github.tommyettinger.ds.IntList;
import com.github.tommyettinger.ds.IntObjectMap;
import com.github.tommyettinger.ds.IntObjectOrderedMap;
import com.github.tommyettinger.ds.IntOrderedSet;
import com.github.tommyettinger.ds.IntSet;
import com.github.tommyettinger.ds.LongDeque;
import com.github.tommyettinger.ds.LongList;
import com.github.tommyettinger.ds.LongObjectMap;
import com.github.tommyettinger.ds.LongObjectOrderedMap;
import com.github.tommyettinger.ds.NumberedSet;
import com.github.tommyettinger.ds.ObjectDeque;
import com.github.tommyettinger.ds.ObjectFloatMap;
import com.github.tommyettinger.ds.ObjectFloatOrderedMap;
import com.github.tommyettinger.ds.ObjectIntMap;
import com.github.tommyettinger.ds.ObjectIntOrderedMap;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.ds.ObjectLongMap;
import com.github.tommyettinger.ds.ObjectLongOrderedMap;
import com.github.tommyettinger.ds.ObjectObjectMap;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import com.github.tommyettinger.ds.ObjectOrderedSet;
import com.github.tommyettinger.ds.ObjectSet;
import com.github.tommyettinger.ds.ShortDeque;
import com.github.tommyettinger.ds.ShortList;
import com.github.tommyettinger.ds.support.sort.FloatComparator;
import com.github.tommyettinger.ds.support.sort.IntComparator;
import com.github.tommyettinger.ds.support.sort.LongComparator;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Converts libGDX data structures to the JDK-interface-compatible data structures in jdkgdxds. This is arguably
 * misleadingly-named, because not all of the interfaces used by jdkgdxds are present in the JDK, and this isn't always
 * a perfect fit. If you primarily use jdkgdxds data structures, then most of the APIs should be very similar to JDK
 * data structures, when they aren't identical. There are all sorts of extensions jdkgdxds does, such as for the ordered
 * data structures.
 */
public class ConversionToJDK {

    /**
     * Can be used to convert from a libGDX Array to a jdkgdxds ObjectList of the same element type.
     * @param from an Array from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectList of type T holding the items of {@code from}
     */
    public static <T> ObjectList<T> toObjectList(Array<T> from) {
        ObjectList<T> list = new ObjectList<>(from.size);
        for(T t : from)
            list.add(t);
        return list;
    }

    /**
     * Can be used to convert from a libGDX Queue to a jdkgdxds ObjectList of the same element type.
     * @param from a Queue from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectList of type T holding the items of {@code from}
     */
    public static <T> ObjectList<T> toObjectList(Queue<T> from) {
        ObjectList<T> list = new ObjectList<>(from.size);
        for(T t : from)
            list.add(t);
        return list;
    }

    /**
     * Can be used to convert from a libGDX ObjectSet or OrderedSet to a jdkgdxds ObjectList of the same element type.
     * @param from an ObjectSet or OrderedSet from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectList of type T holding the unique items of {@code from}
     */
    public static <T> ObjectList<T> toObjectList(com.badlogic.gdx.utils.ObjectSet<T> from) {
        ObjectList<T> list = new ObjectList<>(from.size);
        for(T t : from)
            list.add(t);
        return list;
    }

    /**
     * Can be used to convert from a libGDX IntArray to a jdkgdxds IntList.
     * @param from a libGDX IntArray
     * @return a new IntList holding the items of {@code from}
     */
    public static IntList toIntList(IntArray from){
        return new IntList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntSet}
     * to a new jdkgdxds {@link IntList}.
     * @param from a libGDX IntSet
     * @return a new jdkgdxds IntList holding the items in {@code from}
     */
    public static IntList toIntList(com.badlogic.gdx.utils.IntSet from) {
        IntList set = new IntList(from.size);
        com.badlogic.gdx.utils.IntSet.IntSetIterator it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX LongArray to a jdkgdxds LongList.
     * @param from a libGDX LongArray
     * @return a new LongList holding the items of {@code from}
     */
    public static LongList toLongList(LongArray from){
        return new LongList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX LongQueue to a jdkgdxds LongList.
     * @param from a libGDX LongQueue
     * @return a new LongList holding the items of {@code from}
     */
    public static LongList toLongList(LongQueue from){
        LongList list = new LongList(from.size);
        for (int i = 0; i < from.size; i++) {
            list.add(from.get(i));
        }
        return list;
    }

    /**
     * Can be used to convert from a libGDX FloatArray to a jdkgdxds FloatList.
     * @param from a libGDX FloatArray
     * @return a new FloatList holding the items of {@code from}
     */
    public static FloatList toFloatList(FloatArray from){
        return new FloatList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX ByteArray to a jdkgdxds ByteList.
     * @param from a libGDX ByteArray
     * @return a new ByteList holding the items of {@code from}
     */
    public static ByteList toByteList(ByteArray from){
        return new ByteList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX CharArray to a jdkgdxds CharList.
     * @param from a libGDX CharArray
     * @return a new CharList holding the items of {@code from}
     */
    public static CharList toCharList(CharArray from){
        return new CharList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX ShortArray to a jdkgdxds ShortList.
     * @param from a libGDX ShortArray
     * @return a new ShortList holding the items of {@code from}
     */
    public static ShortList toShortList(ShortArray from){
        return new ShortList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX BooleanArray to a jdkgdxds BooleanList.
     * @param from a libGDX BooleanArray
     * @return a new BooleanList holding the items of {@code from}
     */
    public static BooleanList toBooleanList(BooleanArray from){
        return new BooleanList(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.Array}
     * to a new jdkgdxds {@link ObjectSet}. This will not necessarily maintain the order of the
     * items in the Array.
     * @param from a libGDX Array
     * @return a new jdkgdxds ObjectSet holding the unique items in {@code from}
     */
    public static <T> ObjectSet<T> toObjectSet(com.badlogic.gdx.utils.Array<T> from) {
        ObjectSet<T> set = new ObjectSet<>(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link Queue}
     * to a new jdkgdxds {@link ObjectSet}. This will not necessarily maintain the order of the
     * items in the Queue.
     * @param from a libGDX Queue
     * @return a new jdkgdxds ObjectSet holding the unique items in {@code from}
     */
    public static <T> ObjectSet<T> toObjectSet(Queue<T> from) {
        ObjectSet<T> set = new ObjectSet<>(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet}
     * to a new jdkgdxds {@link ObjectSet}.
     * @param from a libGDX ObjectSet or OrderedSet
     * @return a new jdkgdxds ObjectSet holding the unique items in {@code from}
     */
    public static <T> ObjectSet<T> toObjectSet(com.badlogic.gdx.utils.ObjectSet<T> from) {
        ObjectSet<T> set = new ObjectSet<>(from.size);
        com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator<T> it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.Array}
     * to a new jdkgdxds {@link ObjectOrderedSet}. This will maintain the order of the
     * items in the Array.
     * @param from a libGDX Array
     * @return a new jdkgdxds ObjectOrderedSet holding the unique items in {@code from}
     */
    public static <T> ObjectOrderedSet<T> toObjectOrderedSet(com.badlogic.gdx.utils.Array<T> from) {
        ObjectOrderedSet<T> set = new ObjectOrderedSet<>(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link Queue}
     * to a new jdkgdxds {@link ObjectOrderedSet}. This will maintain the order of the
     * items in the Queue.
     * @param from a libGDX Queue
     * @return a new jdkgdxds ObjectOrderedSet holding the unique items in {@code from}
     */
    public static <T> ObjectOrderedSet<T> toObjectOrderedSet(Queue<T> from) {
        ObjectOrderedSet<T> set = new ObjectOrderedSet<>(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet} or
     * {@link com.badlogic.gdx.utils.OrderedSet} to a new jdkgdxds {@link ObjectOrderedSet}.
     * @param from a libGDX ObjectSet or OrderedSet
     * @return a new jdkgdxds ObjectOrderedSet holding the unique items in {@code from}
     */
    public static <T> ObjectOrderedSet<T> toObjectOrderedSet(com.badlogic.gdx.utils.ObjectSet<T> from) {
        ObjectOrderedSet<T> set = new ObjectOrderedSet<>(from.size);
        com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator<T> it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.Array}
     * to a new jdkgdxds {@link CaseInsensitiveSet}. This will not necessarily maintain the order of the
     * items in the Array.
     * @param from a libGDX Array
     * @return a new jdkgdxds CaseInsensitiveSet holding the unique items (case-insensitive) in {@code from}
     */
    public static <T extends CharSequence> CaseInsensitiveSet toCaseInsensitiveSet(com.badlogic.gdx.utils.Array<T> from) {
        CaseInsensitiveSet set = new CaseInsensitiveSet(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet} or
     * {@link com.badlogic.gdx.utils.OrderedSet} to a new jdkgdxds {@link CaseInsensitiveSet}.
     * @param from a libGDX ObjectSet or OrderedSet
     * @return a new jdkgdxds CaseInsensitiveSet holding the unique items (case-insensitive) in {@code from}
     */
    public static <T extends CharSequence> CaseInsensitiveSet toCaseInsensitiveSet(com.badlogic.gdx.utils.ObjectSet<T> from) {
        CaseInsensitiveSet set = new CaseInsensitiveSet(from.size);
        com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator<T> it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.Array}
     * to a new jdkgdxds {@link CaseInsensitiveSet}. This will maintain the order of the
     * items in the Array.
     * @param from a libGDX Array
     * @return a new jdkgdxds CaseInsensitiveSet holding the unique items (case-insensitive) in {@code from}
     */
    public static <T extends CharSequence> CaseInsensitiveOrderedSet toCaseInsensitiveOrderedSet(com.badlogic.gdx.utils.Array<T> from) {
        CaseInsensitiveOrderedSet set = new CaseInsensitiveOrderedSet(from.size);
        for (T t : from) {
            set.add(t);
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet} or
     * {@link com.badlogic.gdx.utils.OrderedSet} to a new jdkgdxds {@link ObjectOrderedSet}.
     * If from is an OrderedSet, this will maintain its order.
     * @param from a libGDX ObjectSet or OrderedSet
     * @return a new jdkgdxds ObjectOrderedSet holding the unique items (case-insensitive) in {@code from}
     */
    public static <T extends CharSequence> CaseInsensitiveOrderedSet toCaseInsensitiveOrderedSet(com.badlogic.gdx.utils.ObjectSet<T> from) {
        CaseInsensitiveOrderedSet set = new CaseInsensitiveOrderedSet(from.size);
        com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator<T> it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntSet}
     * to a new jdkgdxds {@link IntSet}.
     * @param from a libGDX IntSet
     * @return a new jdkgdxds IntSet holding the unique items in {@code from}
     */
    public static IntSet toIntSet(com.badlogic.gdx.utils.IntSet from) {
        IntSet set = new IntSet(from.size);
        com.badlogic.gdx.utils.IntSet.IntSetIterator it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntArray}
     * to a new jdkgdxds {@link IntSet}.
     * @param from a libGDX IntArray
     * @return a new jdkgdxds IntSet holding the unique items in {@code from}
     */
    public static IntSet toIntSet(com.badlogic.gdx.utils.IntArray from) {
        return new IntSet(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntSet}
     * to a new jdkgdxds {@link IntOrderedSet}. Because a libGDX IntSet is not
     * meaningfully ordered, the initial order of the IntOrderedSet this returns is
     * probably not going to match the insertion order for the IntSet. You can sort
     * an {@link IntOrderedSet} with {@link IntOrderedSet#sort()}.
     * @param from a libGDX IntSet
     * @return a new jdkgdxds IntSet holding the unique items in {@code from}
     */
    public static IntOrderedSet toIntOrderedSet(com.badlogic.gdx.utils.IntSet from) {
        IntOrderedSet set = new IntOrderedSet(from.size);
        com.badlogic.gdx.utils.IntSet.IntSetIterator it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntArray}
     * to a new jdkgdxds {@link IntOrderedSet}. This will maintain the order of the
     * items in the IntArray.
     * @param from a libGDX IntArray
     * @return a new jdkgdxds IntOrderedSet holding the unique items in {@code from}
     */
    public static IntOrderedSet toIntOrderedSet(com.badlogic.gdx.utils.IntArray from) {
        return new IntOrderedSet(from.items, 0, from.size);
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet}
     * (or more typically, a libGDX {@link OrderedSet}) to a new jdkgdxds {@link NumberedSet}.
     * This will maintain the order in an OrderedSet argument, and because it returns a
     * NumberedSet, the order can be looked up bidirectionally with
     * {@link NumberedSet#indexOf(Object)}. If given an ordinary ObjectSet, it will not maintain
     * any kind of order with the given set.
     * @param from a libGDX ObjectSet or OrderedSet
     * @return a new jdkgdxds NumberedSet holding the unique items in {@code from}
     */
    public static <T> NumberedSet<T> toNumberedSet(com.badlogic.gdx.utils.ObjectSet<T> from) {
        NumberedSet<T> set = new NumberedSet<>(from.size);
        com.badlogic.gdx.utils.ObjectSet.ObjectSetIterator<T> it = from.iterator();
        while (it.hasNext) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.Array} to a new
     * jdkgdxds {@link NumberedSet}, keeping only unique items in the Array. This will
     * maintain the order in the Array argument, and because it returns a NumberedSet, the order
     * can be looked up bidirectionally with {@link NumberedSet#indexOf(Object)}.
     * @param from a libGDX Array
     * @return a new jdkgdxds NumberedSet holding the unique items in {@code from}
     */
    public static <T> NumberedSet<T> toNumberedSet(Array<T> from) {
        NumberedSet<T> set = new NumberedSet<>(from.size);
        Array.ArrayIterator<T> it = from.iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX {@link Queue} to a new
     * jdkgdxds {@link NumberedSet}, keeping only unique items in the Queue. This will
     * maintain the order in the Queue argument, and because it returns a NumberedSet, the order
     * can be looked up bidirectionally with {@link NumberedSet#indexOf(Object)}.
     * @param from a libGDX Queue
     * @return a new jdkgdxds NumberedSet holding the unique items in {@code from}
     */
    public static <T> NumberedSet<T> toNumberedSet(Queue<T> from) {
        NumberedSet<T> set = new NumberedSet<>(from.size);
        Iterator<T> it = from.iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from a libGDX ObjectMap (or OrderedMap) to a new jdkgdxds
     * {@link ObjectObjectMap}.
     * @param from a libGDX ObjectMap (or OrderedMap)
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectObjectMap
     * @param <V> the type of values; the same in {@code from} and the returned ObjectObjectMap
     * @return a new ObjectObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> ObjectObjectMap<K, V> toObjectObjectMap(ObjectMap<K, V> from){
        ObjectObjectMap<K, V> map = new ObjectObjectMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX ObjectMap (or OrderedMap) to a new jdkgdxds
     * {@link ObjectObjectOrderedMap}. If {@code from} is an OrderedMap, then this will maintain
     * its order in the returned ObjectObjectOrderedMap.
     * @param from a libGDX ObjectMap (or OrderedMap)
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectObjectOrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned ObjectObjectOrderedMap
     * @return a new ObjectObjectOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> ObjectObjectOrderedMap<K, V> toObjectObjectOrderedMap(ObjectMap<K, V> from){
        ObjectObjectOrderedMap<K, V> map = new ObjectObjectOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX ArrayMap to a new jdkgdxds {@link ObjectObjectMap}.
     * @param from a libGDX ArrayMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectObjectMap
     * @param <V> the type of values; the same in {@code from} and the returned ObjectObjectMap
     * @return a new ObjectObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> ObjectObjectMap<K, V> toObjectObjectMap(ArrayMap<K, V> from){
        ObjectObjectMap<K, V> map = new ObjectObjectMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX ArrayMap to a new jdkgdxds
     * {@link ObjectObjectOrderedMap}. This will maintain the ArrayMap's order in the
     * returned ObjectObjectOrderedMap.
     * @param from a libGDX ArrayMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectObjectOrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned ObjectObjectOrderedMap
     * @return a new ObjectObjectOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> ObjectObjectOrderedMap<K, V> toObjectObjectOrderedMap(ArrayMap<K, V> from){
        ObjectObjectOrderedMap<K, V> map = new ObjectObjectOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX ObjectMap (or OrderedMap) to a new jdkgdxds
     * {@link CaseInsensitiveMap}.
     * @param from a libGDX ObjectMap (or OrderedMap)
     * @param <K> the type of keys; must extend CharSequence, so this could be String, StringBuilder, etc.
     * @param <V> the type of values; the same in {@code from} and the returned CaseInsensitiveMap
     * @return a new CaseInsensitiveMap holding all of the key-value pairs in {@code from}
     */
    public static <K extends CharSequence, V> CaseInsensitiveMap<V> toCaseInsensitiveMap(ObjectMap<K, V> from){
        CaseInsensitiveMap<V> map = new CaseInsensitiveMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX ObjectMap (or OrderedMap) to a new jdkgdxds
     * {@link CaseInsensitiveOrderedMap}. If {@code from} is an OrderedMap, then this will maintain
     * its order in the returned CaseInsensitiveOrderedMap.
     * @param from a libGDX ObjectMap (or OrderedMap)
     * @param <K> the type of keys; must extend CharSequence, so this could be String, StringBuilder, etc.
     * @param <V> the type of values; the same in {@code from} and the returned CaseInsensitiveOrderedMap
     * @return a new CaseInsensitiveOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K extends CharSequence, V> CaseInsensitiveOrderedMap<V> toCaseInsensitiveOrderedMap(ObjectMap<K, V> from){
        CaseInsensitiveOrderedMap<V> map = new CaseInsensitiveOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectIntMap} to a
     * jdkgdxds ObjectIntMap.
     * @param from a libGDX ObjectIntMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectIntMap
     * @return a new jdkgdxds ObjectIntMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectIntMap<K> toObjectIntMap(com.badlogic.gdx.utils.ObjectIntMap<K> from){
        ObjectIntMap<K> map = new ObjectIntMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectIntMap} to a
     * jdkgdxds ObjectIntOrderedMap. Because the given libGDX ObjectIntMap does not maintain order,
     * the initial ordering of the returned ObjectIntOrderedMap is undefined, but it can be sorted
     * with {@link ObjectIntOrderedMap#sort()} or {@link ObjectIntOrderedMap#sortByValue(IntComparator)}.
     * @param from a libGDX ObjectIntMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectIntOrderedMap
     * @return a new jdkgdxds ObjectIntOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectIntOrderedMap<K> toObjectIntOrderedMap(com.badlogic.gdx.utils.ObjectIntMap<K> from){
        ObjectIntOrderedMap<K> map = new ObjectIntOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectLongMap} to a
     * jdkgdxds ObjectLongMap.
     * @param from a libGDX ObjectLongMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectLongMap
     * @return a new jdkgdxds ObjectLongMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectLongMap<K> toObjectLongMap(com.badlogic.gdx.utils.ObjectLongMap<K> from){
        ObjectLongMap<K> map = new ObjectLongMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectLongMap} to a
     * jdkgdxds ObjectLongOrderedMap. Because the given libGDX ObjectLongMap does not maintain order,
     * the initial ordering of the returned ObjectLongOrderedMap is undefined, but it can be sorted
     * with {@link ObjectLongOrderedMap#sort()} or {@link ObjectLongOrderedMap#sortByValue(LongComparator)}.
     * @param from a libGDX ObjectLongMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectLongOrderedMap
     * @return a new jdkgdxds ObjectLongOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectLongOrderedMap<K> toObjectLongOrderedMap(com.badlogic.gdx.utils.ObjectLongMap<K> from){
        ObjectLongOrderedMap<K> map = new ObjectLongOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectFloatMap} to a
     * jdkgdxds ObjectFloatMap.
     * @param from a libGDX ObjectFloatMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectFloatMap
     * @return a new jdkgdxds ObjectFloatMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectFloatMap<K> toObjectFloatMap(com.badlogic.gdx.utils.ObjectFloatMap<K> from){
        ObjectFloatMap<K> map = new ObjectFloatMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectFloatMap} to a
     * jdkgdxds ObjectFloatOrderedMap. Because the given libGDX ObjectFloatMap does not maintain order,
     * the initial ordering of the returned ObjectFloatOrderedMap is undefined, but it can be sorted
     * with {@link ObjectFloatOrderedMap#sort()} or {@link ObjectFloatOrderedMap#sortByValue(FloatComparator)}.
     * @param from a libGDX ObjectFloatMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectFloatOrderedMap
     * @return a new jdkgdxds ObjectFloatOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectFloatOrderedMap<K> toObjectFloatOrderedMap(com.badlogic.gdx.utils.ObjectFloatMap<K> from){
        ObjectFloatOrderedMap<K> map = new ObjectFloatOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntMap} to a
     * jdkgdxds IntObjectMap.
     * @param from a libGDX IntMap
     * @param <V> the type of values; the same in {@code from} and the returned IntObjectMap
     * @return a new jdkgdxds IntObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <V> IntObjectMap<V> toIntObjectMap(com.badlogic.gdx.utils.IntMap<V> from){
        IntObjectMap<V> map = new IntObjectMap<>(from.size);
        IntMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntMap} to a
     * jdkgdxds IntObjectOrderedMap. Because the given libGDX IntMap does not maintain order,
     * the initial ordering of the returned IntObjectOrderedMap is undefined, but it can be sorted
     * with {@link IntObjectOrderedMap#sort()} or {@link IntObjectOrderedMap#sortByValue(Comparator)}.
     * @param from a libGDX IntMap
     * @param <V> the type of values; the same in {@code from} and the returned IntObjectOrderedMap
     * @return a new jdkgdxds IntObjectOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <V> IntObjectOrderedMap<V> toIntObjectOrderedMap(com.badlogic.gdx.utils.IntMap<V> from){
        IntObjectOrderedMap<V> map = new IntObjectOrderedMap<>(from.size);
        IntMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntIntMap} to a
     * jdkgdxds IntIntMap.
     * @param from a libGDX IntIntMap
     * @return a new jdkgdxds IntIntMap holding all of the key-value pairs in {@code from}
     */
    public static IntIntMap toIntIntMap(com.badlogic.gdx.utils.IntIntMap from){
        IntIntMap map = new IntIntMap(from.size);
        com.badlogic.gdx.utils.IntIntMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntIntMap} to a
     * jdkgdxds IntIntOrderedMap. Because the given libGDX IntIntMap does not maintain order,
     * the initial ordering of the returned IntIntOrderedMap is undefined, but it can be sorted
     * with {@link IntIntOrderedMap#sort()} or {@link IntIntOrderedMap#sortByValue(IntComparator)}.
     * @param from a libGDX IntIntMap
     * @return a new jdkgdxds IntIntOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static IntIntOrderedMap toIntIntOrderedMap(com.badlogic.gdx.utils.IntIntMap from){
        IntIntOrderedMap map = new IntIntOrderedMap(from.size);
        com.badlogic.gdx.utils.IntIntMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntFloatMap} to a
     * jdkgdxds IntFloatMap.
     * @param from a libGDX IntFloatMap
     * @return a new jdkgdxds IntFloatMap holding all of the key-value pairs in {@code from}
     */
    public static IntFloatMap toIntFloatMap(com.badlogic.gdx.utils.IntFloatMap from){
        IntFloatMap map = new IntFloatMap(from.size);
        com.badlogic.gdx.utils.IntFloatMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.IntFloatMap} to a
     * jdkgdxds IntFloatOrderedMap. Because the given libGDX IntFloatMap does not maintain order,
     * the initial ordering of the returned IntFloatOrderedMap is undefined, but it can be sorted
     * with {@link IntFloatOrderedMap#sort()} or {@link IntFloatOrderedMap#sortByValue(FloatComparator)}.
     * @param from a libGDX IntFloatMap
     * @return a new jdkgdxds IntFloatOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static IntFloatOrderedMap toIntFloatOrderedMap(com.badlogic.gdx.utils.IntFloatMap from){
        IntFloatOrderedMap map = new IntFloatOrderedMap(from.size);
        com.badlogic.gdx.utils.IntFloatMap.Keys it = from.keys();
        while (it.hasNext){
            int k = it.next();
            map.put(k, from.get(k, 0));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.LongMap} to a
     * jdkgdxds LongObjectMap.
     * @param from a libGDX LongMap
     * @param <V> the type of values; the same in {@code from} and the returned LongObjectMap
     * @return a new jdkgdxds LongObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <V> LongObjectMap<V> toLongObjectMap(com.badlogic.gdx.utils.LongMap<V> from){
        LongObjectMap<V> map = new LongObjectMap<>(from.size);
        LongMap.Keys it = from.keys();
        while (it.hasNext){
            long k = it.next();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.LongMap} to a
     * jdkgdxds LongObjectOrderedMap. Because the given libGDX LongMap does not maintain order,
     * the initial ordering of the returned LongObjectOrderedMap is undefined, but it can be sorted
     * with {@link LongObjectOrderedMap#sort()} or {@link LongObjectOrderedMap#sortByValue(Comparator)}.
     * @param from a libGDX LongMap
     * @param <V> the type of values; the same in {@code from} and the returned LongObjectOrderedMap
     * @return a new jdkgdxds LongObjectOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <V> LongObjectOrderedMap<V> toLongObjectOrderedMap(com.badlogic.gdx.utils.LongMap<V> from){
        LongObjectOrderedMap<V> map = new LongObjectOrderedMap<>(from.size);
        LongMap.Keys it = from.keys();
        while (it.hasNext){
            long k = it.next();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX IdentityMap, ObjectMap, or OrderedMap to a new
     * jdkgdxds {@link IdentityObjectMap}.
     * @param from a libGDX IdentityMap, ObjectMap, or OrderedMap
     * @param <K> the type of keys; the same in {@code from} and the returned IdentityObjectMap
     * @param <V> the type of values; the same in {@code from} and the returned IdentityObjectMap
     * @return a new IdentityObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> IdentityObjectMap<K, V> toIdentityObjectMap(ObjectMap<K, V> from){
        IdentityObjectMap<K, V> map = new IdentityObjectMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX IdentityMap, ObjectMap, or OrderedMap to a new jdkgdxds
     * {@link IdentityObjectOrderedMap}. If {@code from} is an OrderedMap, then this will maintain
     * its order in the returned IdentityObjectOrderedMap.
     * @param from a libGDX IdentityMap, ObjectMap, or OrderedMap
     * @param <K> the type of keys; the same in {@code from} and the returned IdentityObjectOrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned IdentityObjectOrderedMap
     * @return a new IdentityObjectOrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> IdentityObjectOrderedMap<K, V> toIdentityObjectOrderedMap(ObjectMap<K, V> from){
        IdentityObjectOrderedMap<K, V> map = new IdentityObjectOrderedMap<>(from.size);
        for(K k : from.keys()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a libGDX Array to a jdkgdxds ObjectDeque of the same element type.
     * @param from an Array from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectDeque of type T holding the items of {@code from}
     */
    public static <T> ObjectDeque<T> toObjectDeque(Array<T> from) {
        ObjectDeque<T> deque = new ObjectDeque<>(from.size);
        for(T t : from)
            deque.add(t);
        return deque;
    }

    /**
     * Can be used to convert from a libGDX Queue to a jdkgdxds ObjectDeque of the same element type.
     * @param from a Queue from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectDeque of type T holding the items of {@code from}
     */
    public static <T> ObjectDeque<T> toObjectDeque(Queue<T> from) {
        ObjectDeque<T> deque = new ObjectDeque<>(from.size);
        for(T t : from)
            deque.add(t);
        return deque;
    }

    /**
     * Can be used to convert from a libGDX ObjectSet or OrderedSet to a jdkgdxds ObjectDeque of the same element type.
     * @param from an ObjectSet or OrderedSet from libGDX
     * @param <T> the element type for {@code from} and the result
     * @return a new ObjectDeque of type T holding the unique items of {@code from}
     */
    public static <T> ObjectDeque<T> toObjectDeque(com.badlogic.gdx.utils.ObjectSet<T> from) {
        ObjectDeque<T> deque = new ObjectDeque<>(from.size);
        for(T t : from)
            deque.add(t);
        return deque;
    }

    /**
     * Can be used to convert from a libGDX LongQueue to a jdkgdxds LongDeque.
     * @param from a libGDX LongQueue
     * @return a new LongDeque holding the items of {@code from}
     */
    public static LongDeque toLongDeque(LongQueue from){
        LongDeque deque = new LongDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX LongArray to a jdkgdxds LongDeque.
     * @param from a libGDX LongArray
     * @return a new LongDeque holding the items of {@code from}
     */
    public static LongDeque toLongDeque(LongArray from){
        LongDeque deque = new LongDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX IntArray to a jdkgdxds IntDeque.
     * @param from a libGDX IntArray
     * @return a new IntDeque holding the items of {@code from}
     */
    public static IntDeque toIntDeque(IntArray from){
        IntDeque deque = new IntDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX IntSet to a jdkgdxds IntDeque.
     * @param from a libGDX IntSet
     * @return a new IntDeque holding the items of {@code from}
     */
    public static IntDeque toIntDeque(com.badlogic.gdx.utils.IntSet from){
        IntDeque deque = new IntDeque(from.size);
        com.badlogic.gdx.utils.IntSet.IntSetIterator it = from.iterator();
        while (it.hasNext) {
            deque.add(it.next());
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX ByteArray to a jdkgdxds ByteDeque.
     * @param from a libGDX ByteArray
     * @return a new ByteDeque holding the items of {@code from}
     */
    public static ByteDeque toByteDeque(ByteArray from){
        ByteDeque deque = new ByteDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }
    /**
     * Can be used to convert from a libGDX ShortArray to a jdkgdxds ShortDeque.
     * @param from a libGDX ShortArray
     * @return a new ShortDeque holding the items of {@code from}
     */
    public static ShortDeque toShortDeque(ShortArray from){
        ShortDeque deque = new ShortDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX CharArray to a jdkgdxds CharDeque.
     * @param from a libGDX CharArray
     * @return a new CharDeque holding the items of {@code from}
     */
    public static CharDeque toCharDeque(CharArray from){
        CharDeque deque = new CharDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }

    /**
     * Can be used to convert from a libGDX FloatArray to a jdkgdxds FloatDeque.
     * @param from a libGDX FloatArray
     * @return a new FloatDeque holding the items of {@code from}
     */
    public static FloatDeque toFloatDeque(FloatArray from){
        FloatDeque deque = new FloatDeque(from.size);
        for (int i = 0; i < from.size; i++) {
            deque.add(from.get(i));
        }
        return deque;
    }
}
