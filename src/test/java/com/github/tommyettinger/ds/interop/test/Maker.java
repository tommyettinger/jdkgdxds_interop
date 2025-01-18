/*
 * Copyright (c) 2022-2025 See AUTHORS file.
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

import java.util.*;

/**
 * Utility methods for more easily constructing data structures, particularly those in Java's standard library.
 * All static methods and inner classes; meant to be imported with {@code import static squidpony.Maker.*}.
 * Created by Tommy Ettinger on 5/19/2016.
 */
public final class Maker {
    private Maker() {}
    public static class LHM {
        private LHM() {}
        /**
         * Makes a LinkedHashMap (LHM) with key and value types inferred from the types of k0 and v0, and considers all
         * parameters key-value pairs, casting the Objects at positions 0, 2, 4... etc. to K and the objects at positions
         * 1, 3, 5... etc. to V. If rest has an odd-number length, then it discards the last item. If any pair of items in
         * rest cannot be cast to the correct type of K or V, then this inserts nothing for that pair and logs information
         * on the problematic pair to the static Maker.issueLog field.
         * @param k0 the first key; used to infer the types of other keys if generic parameters aren't specified.
         * @param v0 the first value; used to infer the types of other values if generic parameters aren't specified.
         * @param rest an array or vararg of keys and values in pairs; should contain alternating K, V, K, V... elements
         * @param <K> the type of keys in the returned LinkedHashMap; if not specified, will be inferred from k0
         * @param <V> the type of values in the returned LinkedHashMap; if not specified, will be inferred from v0
         * @return a freshly-made LinkedHashMap with K keys and V values, using k0, v0, and the contents of rest to fill it
         */
        @SuppressWarnings("unchecked")
        public static <K, V> LinkedHashMap<K, V> with(K k0, V v0, Object... rest)
        {
            if(rest == null || rest.length == 0)
            {
                LinkedHashMap<K, V> lhm = new LinkedHashMap<>(2);
                lhm.put(k0, v0);
                return lhm;
            }
            LinkedHashMap<K, V> lhm = new LinkedHashMap<>(1 + (rest.length / 2));
            lhm.put(k0, v0);

            for (int i = 0; i < rest.length - 1; i+=2) {
                try {
                    lhm.put((K) rest[i], (V) rest[i + 1]);
                }catch (ClassCastException cce) {
                }
            }
            return lhm;
        }

        /**
         * Makes an empty LinkedHashMap (LHM); needs key and value types to be specified in order to work. For an empty
         * LinkedHashMap with String keys and Coord values, you could use {@code Maker.<String, Coord>with();}. Using
         * the new keyword is probably just as easy in this case; this method is provided for completeness relative to
         * with() with 2 or more parameters.
         * @param <K> the type of keys in the returned LinkedHashMap; cannot be inferred and must be specified
         * @param <V> the type of values in the returned LinkedHashMap; cannot be inferred and must be specified
         * @return an empty LinkedHashMap with the given key and value types.
         */
        public static <K, V> LinkedHashMap<K, V> with()
        {
            return new LinkedHashMap<>();
        }

    }
    public static class HM {
        private HM() {}

        /**
         * Makes a HashMap (HM) with key and value types inferred from the types of k0 and v0, and considers all
         * parameters key-value pairs, casting the Objects at positions 0, 2, 4... etc. to K and the objects at positions
         * 1, 3, 5... etc. to V. If rest has an odd-number length, then it discards the last item. If any pair of items in
         * rest cannot be cast to the correct type of K or V, then this inserts nothing for that pair and logs information
         * on the problematic pair to the static Maker.issueLog field.
         * @param k0 the first key; used to infer the types of other keys if generic parameters aren't specified.
         * @param v0 the first value; used to infer the types of other values if generic parameters aren't specified.
         * @param rest an array or vararg of keys and values in pairs; should contain alternating K, V, K, V... elements
         * @param <K> the type of keys in the returned HashMap; if not specified, will be inferred from k0
         * @param <V> the type of values in the returned HashMap; if not specified, will be inferred from v0
         * @return a freshly-made HashMap with K keys and V values, using k0, v0, and the contents of rest to fill it
         */
        @SuppressWarnings("unchecked")
        public static <K, V> HashMap<K, V> with(K k0, V v0, Object... rest)
        {
            if(rest == null || rest.length == 0)
            {
                HashMap<K, V> hm = new HashMap<>(2);
                hm.put(k0, v0);
                return hm;
            }
            HashMap<K, V> hm = new HashMap<>(1 + (rest.length / 2));
            hm.put(k0, v0);

            for (int i = 0; i < rest.length - 1; i+=2) {
                try {
                    hm.put((K) rest[i], (V) rest[i + 1]);
                }catch (ClassCastException cce) {
                }
            }
            return hm;
        }

        /**
         * Makes an empty HashMap (HM); needs key and value types to be specified in order to work. For an empty
         * HashMap with String keys and Coord values, you could use {@code Maker.<String, Coord>with();}. Using
         * the new keyword is probably just as easy in this case; this method is provided for completeness relative to
         * with() with 2 or more parameters.
         * @param <K> the type of keys in the returned HashMap; cannot be inferred and must be specified
         * @param <V> the type of values in the returned HashMap; cannot be inferred and must be specified
         * @return an empty HashMap with the given key and value types.
         */
        public static <K, V> HashMap<K, V> with()
        {
            return new HashMap<>();
        }
    }
    public static class LHS {
        private LHS() {}

        /**
         * Makes a LinkedHashSet (LHS) of T given an array or vararg of T elements. Duplicate items in elements will have
         * all but one item discarded, using the later item in elements.
         * @param elements an array or vararg of T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated LinkedHashSet containing all of the non-duplicate items in elements, in order
         */
        @SuppressWarnings("unchecked")
        public static <T> LinkedHashSet<T> with(T... elements) {
            if(elements == null) return null;
            LinkedHashSet<T> set = new LinkedHashSet<>(elements.length);
            Collections.addAll(set, elements);
            return set;
        }

        /**
         * Makes a LinkedHashSet (LHS) of T given a single T element.
         * @param element a single T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated LinkedHashSet containing only {@code element}
         */
        public static <T> LinkedHashSet<T> with(T element) {
            LinkedHashSet<T> set = new LinkedHashSet<T>(1);
            set.add(element);
            return set;
        }
    }
    public static class HS {
        private HS() {}
        /**
         * Makes a HashSet (HS) of T given an array or vararg of T elements. Duplicate items in elements will have
         * all but one item discarded, using the later item in elements.
         * @param elements an array or vararg of T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated HashSet containing all of the non-duplicate items in elements, in order
         */
        @SuppressWarnings("unchecked")
        public static <T> HashSet<T> with(T... elements) {
            if(elements == null) return null;
            HashSet<T> set = new HashSet<>(elements.length);
            Collections.addAll(set, elements);
            return set;
        }

        /**
         * Makes a HashSet (HS) of T given a single T element.
         * @param element a single T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated HashSet containing only {@code element}
         */
        public static <T> HashSet<T> with(T element) {
            HashSet<T> set = new HashSet<T>(1);
            set.add(element);
            return set;
        }
    }
    public static class AL {
        private AL() {}

        /**
         * Makes an ArrayList of T given an array or vararg of T elements.
         * @param elements an array or vararg of T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated ArrayList containing all of elements, in order
         */
        @SuppressWarnings("unchecked")
        public static <T> ArrayList<T> with(T... elements) {
            if(elements == null) return null;
            ArrayList<T> list = new ArrayList<>(elements.length);
            Collections.addAll(list, elements);
            return list;
        }

        /**
         * Makes an ArrayList of T given a single T element; avoids creating an array for varargs as
         * {@link #with(Object[])} would do, but only allows one item.
         * @param element an array or vararg of T
         * @param <T> just about any non-primitive, non-array type (arrays would cause confusion with the vararg method)
         * @return a newly-allocated ArrayList containing only element
         */
        public static <T> ArrayList<T> with(T element) {
            ArrayList<T> list = new ArrayList<>(1);
            list.add(element);
            return list;
        }

    }
    public static class AD {
        private AD() {}

        /**
         * Makes an ArrayDeque of T given an array or vararg of T elements.
         * @param elements an array or vararg of T
         * @param <T> just about any non-primitive type
         * @return a newly-allocated ArrayDeque containing all of elements, in order
         */
        @SuppressWarnings("unchecked")
        public static <T> ArrayDeque<T> with(T... elements) {
            if(elements == null) return null;
            ArrayDeque<T> list = new ArrayDeque<>(elements.length);
            Collections.addAll(list, elements);
            return list;
        }

        /**
         * Makes an ArrayDeque of T given a single T element; avoids creating an array for varargs as
         * {@link #with(Object[])} would do, but only allows one item.
         * @param element an array or vararg of T
         * @param <T> just about any non-primitive, non-array type (arrays would cause confusion with the vararg method)
         * @return a newly-allocated ArrayDeque containing only element
         */
        public static <T> ArrayDeque<T> with(T element) {
            ArrayDeque<T> list = new ArrayDeque<>(1);
            list.add(element);
            return list;
        }

    }
}
