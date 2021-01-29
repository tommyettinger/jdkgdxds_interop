package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.IntSet;
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
    public static <T> Array<T> toArray(Collection<T> from) {
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

    /**
     * Can be used to convert from any JDK Collection, such as a jdkgdxds ObjectSet or ObjectList, or
     * a JDK ArrayList, to a new libGDX {@link ObjectSet}, using only the unique items in {@code from}.
     * @param from anything that implements the JDK Collection interface
     * @return a new libGDX ObjectSet holding the unique items in {@code from}
     */
    public static <T> ObjectSet<T> toObjectSet(Collection<T> from) {
        ObjectSet<T> set = new ObjectSet<>(from.size());
        Iterator<T> it = from.iterator();
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

}
