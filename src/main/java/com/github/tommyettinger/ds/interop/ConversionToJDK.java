package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.*;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.IntSet;
import com.github.tommyettinger.ds.ObjectIntMap;
import com.github.tommyettinger.ds.ObjectSet;

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
     * Can be used to convert from a libGDX FloatArray to a jdkgdxds FloatList.
     * @param from a libGDX FloatArray
     * @return a new FloatList holding the items of {@code from}
     */
    public static FloatList toFloatList(FloatArray from){
        return new FloatList(from.items, 0, from.size);
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
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.ObjectSet}
     * to a new jdkgdxds {@link ObjectSet}.
     * @param from a libGDX ObjectSet
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
     * Can be used to convert from a libGDX {@link com.badlogic.gdx.utils.OrderedSet}
     * to a new jdkgdxds {@link ObjectOrderedSet}.
     * @param from a libGDX OrderedSet
     * @return a new jdkgdxds ObjectOrderedSet holding the unique items in {@code from}
     */
    public static <T> ObjectOrderedSet<T> toObjectOrderedSet(com.badlogic.gdx.utils.OrderedSet<T> from) {
        ObjectOrderedSet<T> set = new ObjectOrderedSet<>(from.size);
        OrderedSet.OrderedSetIterator<T> it = from.iterator();
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
     * Can be used to convert from a libGDX ObjectMap to a new jdkgdxds {@link ObjectObjectMap}.
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

}
