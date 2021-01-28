package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.github.tommyettinger.ds.ObjectList;

import java.util.Collection;
import java.util.Map;

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
