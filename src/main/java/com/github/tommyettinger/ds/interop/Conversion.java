package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.*;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.FloatIterator;

import java.util.Collection;
import java.util.Map;
import java.util.PrimitiveIterator;

public class Conversion {

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
     * Can be used to convert from any Collection to a new Array of the same element type.
     * This can take an {@link ObjectList}, {@link com.github.tommyettinger.ds.ObjectSet},
     * {@link com.github.tommyettinger.ds.NumberedSet}, or any JDK Collection, to name a few.
     * @param from a Collection from the JDK, jdkgdxds, or some other library
     * @param <T> the element type for {@code from} and the result
     * @return a new Array of type T holding the items of {@code from} (this does not give a Class to the Array constructor)
     */
    public static <T> Array<T> toArray(Collection<T> from) {
        Array<T> array = new Array<>(from.size());
        for(T t : from)
            array.add(t);
        return array;
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
     * Can be used to convert from any jdkgdxds int-based collection (such as {@link IntList},
     * {@link com.github.tommyettinger.ds.IntSet}, or {@link IntObjectMap.Keys}) to a libGDX IntArray.
     * @param from a primitive-int-backed data structure from jdkgdxds
     * @return a new IntArray holding the items of {@code from}
     */
    public static IntArray toIntArray(PrimitiveCollection.OfInt from) {
        IntArray array = new IntArray(from.size());
        PrimitiveIterator.OfInt it = from.iterator();
        while (it.hasNext()){
            array.add(it.nextInt());
        }
        return array;
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
     * Can be used to convert from any jdkgdxds long-based collection (such as {@link LongList},
     * {@link com.github.tommyettinger.ds.LongSet}, or {@link LongObjectMap.Keys}) to a libGDX LongArray.
     * @param from a primitive-long-backed data structure from jdkgdxds
     * @return a new LongArray holding the items of {@code from}
     */
    public static LongArray toLongArray(PrimitiveCollection.OfLong from) {
        LongArray array = new LongArray(from.size());
        PrimitiveIterator.OfLong it = from.iterator();
        while (it.hasNext()){
            array.add(it.nextLong());
        }
        return array;
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
     * Can be used to convert from any jdkgdxds float-based collection (such as {@link FloatList}
     * or {@link com.github.tommyettinger.ds.ObjectFloatMap.Values}) to a libGDX FloatArray.
     * @param from a primitive-float-backed data structure from jdkgdxds
     * @return a new FloatArray holding the items of {@code from}
     */
    public static FloatArray toFloatArray(PrimitiveCollection.OfFloat from) {
        FloatArray array = new FloatArray(from.size());
        FloatIterator it = from.iterator();
        while (it.hasNext()){
            array.add(it.nextFloat());
        }
        return array;
    }

    /**
     * Can be used to convert from any JDK Map to a new libGDX ArrayMap with the same key and value types.
     * Note that jdkgdxds does not have a direct equivalent to libGDX's ArrayMap, because ArrayMap is only the "right
     * choice" in some extremely narrow situations, and is only barely used inside libGDX (and only in its 3D code).
     * @param from a Map from the JDK, jdkgdxds, or some other library
     * @param <K> the key type
     * @param <V> the value type
     * @return a new ArrayMap holding the keys and values of {@code from} (this does not give a Class to the ArrayMap constructor)
     */
    public static <K, V> ArrayMap<K, V> toArrayMap(Map<K, V> from) {
        ArrayMap<K, V> arrayMap = new ArrayMap<>(true, from.size());
        for(Map.Entry<K, V> e : from.entrySet())
            arrayMap.put(e.getKey(), e.getValue());
        return arrayMap;
    }
}
