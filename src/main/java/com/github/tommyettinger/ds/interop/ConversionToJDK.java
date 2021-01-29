package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.*;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.IntSet;
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
     * Can be used to convert from a libGDX IntArray to a jdkgdxds IntList.
     * @param from a libGDX IntArray
     * @return a new IntList holding the items of {@code from}
     */
    public static IntList toIntList(IntArray from){
        return new IntList(from.items, 0, from.size);
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

}
