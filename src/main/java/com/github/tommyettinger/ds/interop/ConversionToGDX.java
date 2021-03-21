package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.IntFloatMap;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectLongMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.FloatIterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * Converts from arbitrary data structures that implement JDK interfaces to libGDX data structures (which typically
 * implement {@link Iterable} at most). You can send JDK classes like {@link java.util.ArrayList} to methods in this to
 * convert to {@link Array} or {@link ObjectSet}, just as you can send jdkgdxds classes like {@link ObjectList}.
 */
public class ConversionToGDX {

    /**
     * Can be used to convert from any Collection to a new Array of the same element type.
     * This can take an {@link ObjectList}, {@link com.github.tommyettinger.ds.ObjectSet},
     * {@link NumberedSet}, or any JDK Collection, to name a few.
     * @param from a Collection from the JDK, jdkgdxds, or some other library
     * @param <T> the element type for {@code from} and the result
     * @return a new Array of type T holding the items of {@code from} (this does not give a Class to the Array constructor)
     */
    public static <T> Array<T> toArray(Collection<? extends T> from) {
        Array<T> array = new Array<>(from.size());
        for(T t : from)
            array.add(t);
        return array;
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
     * Can be used to convert from any jdkgdxds long-based collection (such as {@link LongList},
     * {@link LongSet}, or {@link LongObjectMap.Keys}) to a libGDX LongArray.
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
     * Can be used to convert from any jdkgdxds long-based collection (such as {@link LongList},
     * {@link LongSet}, or {@link LongObjectMap.Keys}) to a libGDX LongQueue.
     * @param from a primitive-long-backed data structure from jdkgdxds
     * @return a new LongQueue holding the items of {@code from}
     */
    public static LongQueue toLongQueue(PrimitiveCollection.OfLong from) {
        LongQueue queue = new LongQueue(from.size());
        PrimitiveIterator.OfLong it = from.iterator();
        while (it.hasNext()){
            queue.addLast(it.nextLong());
        }
        return queue;
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
    public static <K, V> ArrayMap<K, V> toArrayMap(Map<? extends K, ? extends V> from) {
        ArrayMap<K, V> arrayMap = new ArrayMap<>(true, from.size());
        for(Map.Entry<? extends K, ? extends V> e : from.entrySet())
            arrayMap.put(e.getKey(), e.getValue());
        return arrayMap;
    }

    /**
     * Can be used to convert from any JDK Collection, such as a jdkgdxds ObjectSet or ObjectList, or
     * a JDK ArrayList, to a new libGDX {@link ObjectSet}, using only the unique items in {@code from}.
     * @param from anything that implements the JDK Collection interface
     * @return a new libGDX ObjectSet holding the unique items in {@code from}
     */
    public static <T> ObjectSet<T> toObjectSet(Collection<? extends T> from) {
        ObjectSet<T> set = new ObjectSet<>(from.size());
        Iterator<? extends T> it = from.iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from any JDK Collection, such as a jdkgdxds ObjectOrderedSet or ObjectList, or
     * a JDK ArrayList, to a new libGDX {@link OrderedSet}, using only the unique items in {@code from}.
     * @param from anything that implements the JDK Collection interface
     * @return a new libGDX OrderedSet holding the unique items in {@code from}
     */
    public static <T> OrderedSet<T> toOrderedSet(Collection<? extends T> from) {
        OrderedSet<T> set = new OrderedSet<>(from.size());
        Iterator<? extends T> it = from.iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /**
     * Can be used to convert from any int-based PrimitiveCollection, such as an {@link IntList},
     * {@link com.github.tommyettinger.ds.IntSet}, or {@link com.github.tommyettinger.ds.IntIntMap.Keys},
     * to a new libGDX IntSet, using only the unique items in {@code from}.
     * @param from a primitive-int-backed data structure from jdkgdxds
     * @return a new IntSet holding the unique items in {@code from}
     */
    public static IntSet toIntSet(PrimitiveCollection.OfInt from) {
        IntSet set = new IntSet(from.size());
        PrimitiveIterator.OfInt it = from.iterator();
        while (it.hasNext()) {
            set.add(it.nextInt());
        }
        return set;
    }

    /**
     * Can be used to convert from any JDK Map, such as a {@link java.util.HashMap}, {@link ObjectObjectMap},
     * or {@link CaseInsensitiveOrderedMap}, to a libGDX ObjectMap.
     * @param from any Map that has a usable {@link Map#keySet()} method
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectMap
     * @param <V> the type of values; the same in {@code from} and the returned ObjectMap
     * @return a new ObjectMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> ObjectMap<K, V> toObjectMap(Map<? extends K, ? extends V> from){
        ObjectMap<K, V> map = new ObjectMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from any JDK Map, such as a {@link java.util.LinkedHashMap},
     * {@link ObjectObjectOrderedMap}, or {@link CaseInsensitiveMap}, to a libGDX OrderedMap.
     * If {@code from} maintains the order of its keys, then the returned OrderedMap will have
     * the same order.
     * @param from any Map that has a usable {@link Map#keySet()} method
     * @param <K> the type of keys; the same in {@code from} and the returned OrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned OrderedMap
     * @return a new OrderedMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> OrderedMap<K, V> toOrderedMap(Map<? extends K, ? extends V> from){
        OrderedMap<K, V> map = new OrderedMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.ObjectIntMap} or
     * {@link ObjectIntOrderedMap} to a libGDX ObjectIntMap.
     * @param from a jdkgdxds ObjectIntMap or ObjectIntOrderedMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectIntMap
     * @return a new libGDX ObjectIntMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectIntMap<K> toObjectIntMap(com.github.tommyettinger.ds.ObjectIntMap<? extends K> from){
        ObjectIntMap<K> map = new ObjectIntMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.ObjectLongMap} or
     * {@link ObjectLongOrderedMap} to a libGDX ObjectLongMap.
     * @param from a jdkgdxds ObjectLongMap or ObjectLongOrderedMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectLongMap
     * @return a new libGDX ObjectLongMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectLongMap<K> toObjectLongMap(com.github.tommyettinger.ds.ObjectLongMap<? extends K> from){
        ObjectLongMap<K> map = new ObjectLongMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.ObjectFloatMap} or
     * {@link ObjectFloatOrderedMap} to a libGDX ObjectFloatMap.
     * @param from a jdkgdxds ObjectFloatMap or ObjectFloatOrderedMap
     * @param <K> the type of keys; the same in {@code from} and the returned ObjectFloatMap
     * @return a new libGDX ObjectFloatMap holding all of the key-value pairs in {@code from}
     */
    public static <K> ObjectFloatMap<K> toObjectFloatMap(com.github.tommyettinger.ds.ObjectFloatMap<? extends K> from){
        ObjectFloatMap<K> map = new ObjectFloatMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.IntObjectMap} or
     * {@link IntObjectOrderedMap} to a libGDX IntMap.
     * @param from a jdkgdxds IntObjectMap or IntObjectOrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned IntMap
     * @return a new libGDX IntMap holding all of the key-value pairs in {@code from}
     */
    public static <V> IntMap<V> toIntMap(com.github.tommyettinger.ds.IntObjectMap<? extends V> from){
        IntMap<V> map = new IntMap<>(from.size());
        PrimitiveIterator.OfInt it = from.keySet().iterator();
        while (it.hasNext()) {
            int k = it.nextInt();
            map.put(k, from.get(k));
        }
        return map;
    }
    
    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.IntIntMap} or
     * {@link IntIntOrderedMap} to a libGDX IntIntMap.
     * @param from a jdkgdxds IntIntMap or IntIntOrderedMap
     * @return a new libGDX IntIntMap holding all of the key-value pairs in {@code from}
     */
    public static IntIntMap toIntIntMap(com.github.tommyettinger.ds.IntIntMap from){
        IntIntMap map = new IntIntMap(from.size());
        PrimitiveIterator.OfInt it = from.keySet().iterator();
        while (it.hasNext()) {
            int k = it.nextInt();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.IntFloatMap} or
     * {@link IntFloatOrderedMap} to a libGDX IntFloatMap.
     * @param from a jdkgdxds IntFloatMap or IntFloatOrderedMap
     * @return a new libGDX IntFloatMap holding all of the key-value pairs in {@code from}
     */
    public static IntFloatMap toIntFloatMap(com.github.tommyettinger.ds.IntFloatMap from){
        IntFloatMap map = new IntFloatMap(from.size());
        PrimitiveIterator.OfInt it = from.keySet().iterator();
        while (it.hasNext()) {
            int k = it.nextInt();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from a jdkgdxds {@link com.github.tommyettinger.ds.LongObjectMap} or
     * {@link LongObjectOrderedMap} to a libGDX IntMap.
     * @param from a jdkgdxds LongObjectMap or LongObjectOrderedMap
     * @param <V> the type of values; the same in {@code from} and the returned IntMap
     * @return a new libGDX IntMap holding all of the key-value pairs in {@code from}
     */
    public static <V> LongMap<V> toLongMap(com.github.tommyettinger.ds.LongObjectMap<? extends V> from){
        LongMap<V> map = new LongMap<>(from.size());
        PrimitiveIterator.OfLong it = from.keySet().iterator();
        while (it.hasNext()) {
            long k = it.nextLong();
            map.put(k, from.get(k));
        }
        return map;
    }

    /**
     * Can be used to convert from any JDK Map, such as a {@link java.util.HashMap}, {@link IdentityObjectMap},
     * or {@link CaseInsensitiveOrderedMap}, to a libGDX IdentityMap. In almost all cases, IdentityMap is more
     * permissive about allowing duplicate keys, so converting to an IdentityMap should be lossless.
     * @param from any Map that has a usable {@link Map#keySet()} method
     * @param <K> the type of keys; the same in {@code from} and the returned IdentityMap
     * @param <V> the type of values; the same in {@code from} and the returned IdentityMap
     * @return a new IdentityMap holding all of the key-value pairs in {@code from}
     */
    public static <K, V> IdentityMap<K, V> toIdentityMap(Map<? extends K, ? extends V> from){
        IdentityMap<K, V> map = new IdentityMap<>(from.size());
        for(K k : from.keySet()) {
            map.put(k, from.get(k));
        }
        return map;
    }

}
