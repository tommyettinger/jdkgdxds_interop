package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.tommyettinger.digital.AlternateRandom;
import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.BooleanIterator;
import com.github.tommyettinger.ds.support.util.ByteIterator;
import com.github.tommyettinger.ds.support.util.CharIterator;
import com.github.tommyettinger.ds.support.util.FloatIterator;
import com.github.tommyettinger.ds.support.util.ShortIterator;
import com.github.tommyettinger.random.*;
import com.github.tommyettinger.random.distribution.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("rawtypes")
public final class JsonSupport {
    private JsonSupport() {
    }

    @Nonnull
    private static Base BASE = Base.BASE16;

    /**
     * Registers JDKGDXDS' classes with the given Json object, allowing it to read and write JDKGDXDS types.
     *
     * @param json a libGDX Json object that will have serializers registered for all JDKGDXDS types.
     */
    public static void registerAll(@Nonnull Json json) {
        registerObjectList(json);
        registerIntList(json);
        registerLongList(json);
        registerFloatList(json);
        registerByteList(json);
        registerShortList(json);
        registerCharList(json);
        registerDoubleList(json);
        registerBooleanList(json);

        registerObjectDeque(json);
        registerLongDeque(json);
        registerIntDeque(json);
        registerShortDeque(json);
        registerByteDeque(json);
        registerDoubleDeque(json);
        registerFloatDeque(json);
        registerCharDeque(json);
        registerBooleanDeque(json);

        registerObjectSet(json);
        registerObjectOrderedSet(json);
        registerIntSet(json);
        registerIntOrderedSet(json);
        registerLongSet(json);
        registerLongOrderedSet(json);

        registerObjectObjectMap(json);
        registerObjectObjectOrderedMap(json);
        registerObjectIntMap(json);
        registerObjectIntOrderedMap(json);
        registerObjectLongMap(json);
        registerObjectLongOrderedMap(json);
        registerObjectFloatMap(json);
        registerObjectFloatOrderedMap(json);

        registerIntObjectMap(json);
        registerIntObjectOrderedMap(json);
        registerIntIntMap(json);
        registerIntIntOrderedMap(json);
        registerIntLongMap(json);
        registerIntLongOrderedMap(json);
        registerIntFloatMap(json);
        registerIntFloatOrderedMap(json);

        registerLongObjectMap(json);
        registerLongObjectOrderedMap(json);
        registerLongIntMap(json);
        registerLongIntOrderedMap(json);
        registerLongLongMap(json);
        registerLongLongOrderedMap(json);
        registerLongFloatMap(json);
        registerLongFloatOrderedMap(json);

        registerCaseInsensitiveSet(json);
        registerCaseInsensitiveOrderedSet(json);
        registerCaseInsensitiveMap(json);
        registerCaseInsensitiveOrderedMap(json);

        registerNumberedSet(json);

        registerBinaryHeap(json);

        registerBase(json);
        registerHasher(json);

        // registers many others
        registerDistributedRandom(json);

        registerRandomXS128(json);

        registerClass(json);
    }


    /**
     * Gets the numeral system, also called radix or base, used by some methods here to encode numbers.
     * If it hasn't been changed, the default this uses is {@link Base#BASE36}, because it is fairly compact.
     * @return the Base system this uses, which is always non-null.
     */
    public static Base getNumeralBase() {
        return BASE;
    }
    /**
     * Sets the numeral system, also called radix or base, used by some methods here to encode numbers.
     * This is most likely to be used with {@link Base#scrambledBase(Random)} to obfuscate specific numbers,
     * typically random seeds, put into readable JSON files. The methods this affects are all related to registering
     * {@link EnhancedRandom} and its implementations, {@link RandomXS128}, and {@link AtomicLong}. If this hasn't been
     * called, the default this uses is {@link Base#BASE36}, because it is fairly compact.
     * @param base a non-null Base system
     */
    public static void setNumeralBase(Base base){
        if(base != null)
            BASE = base;
    }

    /**
     * Registers ObjectList with the given Json object, so ObjectList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectList(@Nonnull Json json) {
        json.addClassTag("oL", ObjectList.class);
        json.setSerializer(ObjectList.class, new Json.Serializer<ObjectList>() {
            @Override
            public void write(Json json, ObjectList object, Class knownType) {
                json.writeObjectStart(ObjectList.class, null);
                json.writeArrayStart("items");
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ObjectList<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
//                System.out.println(jsonData.size);
                ObjectList<Object> data = new ObjectList<Object>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntList with the given Json object, so IntList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntList(@Nonnull Json json) {
        json.setSerializer(IntList.class, new Json.Serializer<IntList>() {
            @Override
            public void write(Json json, IntList object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfInt it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
            }

            @Override
            public IntList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return IntList.with(jsonData.asIntArray());
            }
        });

    }

    /**
     * Registers LongList with the given Json object, so LongList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongList(@Nonnull Json json) {
        json.setSerializer(LongList.class, new Json.Serializer<LongList>() {
            @Override
            public void write(Json json, LongList object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfLong it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
            }

            @Override
            public LongList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return LongList.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers FloatList with the given Json object, so FloatList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFloatList(@Nonnull Json json) {
        json.setSerializer(FloatList.class, new Json.Serializer<FloatList>() {
            @Override
            public void write(Json json, FloatList object, Class knownType) {
                json.writeArrayStart();
                FloatIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextFloat());
                }
                json.writeArrayEnd();
            }

            @Override
            public FloatList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return FloatList.with(jsonData.asFloatArray());
            }
        });
    }

    /**
     * Registers ByteList with the given Json object, so ByteList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerByteList(@Nonnull Json json) {
        json.setSerializer(ByteList.class, new Json.Serializer<ByteList>() {
            @Override
            public void write(Json json, ByteList object, Class knownType) {
                json.writeArrayStart();
                ByteIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextByte());
                }
                json.writeArrayEnd();
            }

            @Override
            public ByteList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return ByteList.with(jsonData.asByteArray());
            }
        });
    }

    /**
     * Registers ShortList with the given Json object, so ShortList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerShortList(@Nonnull Json json) {
        json.setSerializer(ShortList.class, new Json.Serializer<ShortList>() {
            @Override
            public void write(Json json, ShortList object, Class knownType) {
                json.writeArrayStart();
                ShortIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextShort());
                }
                json.writeArrayEnd();
            }

            @Override
            public ShortList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return ShortList.with(jsonData.asShortArray());
            }
        });
    }

    /**
     * Registers CharList with the given Json object, so CharList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCharList(@Nonnull Json json) {
        json.setSerializer(CharList.class, new Json.Serializer<CharList>() {
            @Override
            public void write(Json json, CharList object, Class knownType) {
                json.writeArrayStart();
                CharIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextChar());
                }
                json.writeArrayEnd();
            }

            @Override
            public CharList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return CharList.with(jsonData.asCharArray());
            }
        });
    }

    /**
     * Registers DoubleList with the given Json object, so DoubleList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDoubleList(@Nonnull Json json) {
        json.setSerializer(DoubleList.class, new Json.Serializer<DoubleList>() {
            @Override
            public void write(Json json, DoubleList object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfDouble it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextDouble());
                }
                json.writeArrayEnd();
            }

            @Override
            public DoubleList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return DoubleList.with(jsonData.asDoubleArray());
            }
        });
    }

    /**
     * Registers BooleanList with the given Json object, so BooleanList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBooleanList(@Nonnull Json json) {
        json.setSerializer(BooleanList.class, new Json.Serializer<BooleanList>() {
            @Override
            public void write(Json json, BooleanList object, Class knownType) {
                json.writeArrayStart();
                BooleanIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextBoolean());
                }
                json.writeArrayEnd();
            }

            @Override
            public BooleanList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return BooleanList.with(jsonData.asBooleanArray());
            }
        });
    }

    /**
     * Registers ObjectSet with the given Json object, so ObjectSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectSet(@Nonnull Json json) {
        json.setSerializer(ObjectSet.class, new Json.Serializer<ObjectSet>() {
            @Override
            public void write(Json json, ObjectSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectSet<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectSet<?> data = new ObjectSet<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers ObjectOrderedSet with the given Json object, so ObjectOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectOrderedSet(@Nonnull Json json) {
        json.setSerializer(ObjectOrderedSet.class, new Json.Serializer<ObjectOrderedSet>() {
            @Override
            public void write(Json json, ObjectOrderedSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectOrderedSet<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectOrderedSet<?> data = new ObjectOrderedSet<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntSet with the given Json object, so IntSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntSet(@Nonnull Json json) {
        json.setSerializer(IntSet.class, new Json.Serializer<IntSet>() {
            @Override
            public void write(Json json, IntSet object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfInt it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
            }

            @Override
            public IntSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return IntSet.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers IntOrderedSet with the given Json object, so IntOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntOrderedSet(@Nonnull Json json) {
        json.setSerializer(IntOrderedSet.class, new Json.Serializer<IntOrderedSet>() {
            @Override
            public void write(Json json, IntOrderedSet object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfInt it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
            }

            @Override
            public IntOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return IntOrderedSet.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers LongSet with the given Json object, so LongSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongSet(@Nonnull Json json) {
        json.setSerializer(LongSet.class, new Json.Serializer<LongSet>() {
            @Override
            public void write(Json json, LongSet object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfLong it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
            }

            @Override
            public LongSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return LongSet.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers LongOrderedSet with the given Json object, so LongOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongOrderedSet(@Nonnull Json json) {
        json.setSerializer(LongOrderedSet.class, new Json.Serializer<LongOrderedSet>() {
            @Override
            public void write(Json json, LongOrderedSet object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfLong it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
            }

            @Override
            public LongOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return LongOrderedSet.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers ObjectObjectMap with the given Json object, so ObjectObjectMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectObjectMap(@Nonnull Json json) {
        json.addClassTag("ooM", ObjectObjectMap.class);
        json.setSerializer(ObjectObjectMap.class, new Json.Serializer<ObjectObjectMap>() {
            @Override
            public void write(Json json, ObjectObjectMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    json.writeObjectStart(ObjectObjectMap.class, null);
                } catch (SerializationException ignored) {
                }
                Iterator<Map.Entry<Object, Object>> es = new ObjectObjectMap.Entries<Object, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue(), null);
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectObjectMap<?, ?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectObjectMap<?, ?> data = new ObjectObjectMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers ObjectObjectOrderedMap with the given Json object, so ObjectObjectOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectObjectOrderedMap(@Nonnull Json json) {
        json.setSerializer(ObjectObjectOrderedMap.class, new Json.Serializer<ObjectObjectOrderedMap>() {
            @Override
            public void write(Json json, ObjectObjectOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<Map.Entry<Object, Object>> es = new ObjectObjectOrderedMap.OrderedMapEntries<Object, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue(), knownType);
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectObjectOrderedMap<?, ?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectObjectOrderedMap<?, ?> data = new ObjectObjectOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(null, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectLongMap with the given Json object, so ObjectLongMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectLongMap(@Nonnull Json json) {
        json.setSerializer(ObjectLongMap.class, new Json.Serializer<ObjectLongMap>() {
            @Override
            public void write(Json json, ObjectLongMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectLongMap.Entry<Object>> es = new ObjectLongMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectLongMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectLongMap<?> data = new ObjectLongMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectLongOrderedMap with the given Json object, so ObjectLongOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectLongOrderedMap(@Nonnull Json json) {
        json.setSerializer(ObjectLongOrderedMap.class, new Json.Serializer<ObjectLongOrderedMap>() {
            @Override
            public void write(Json json, ObjectLongOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectLongMap.Entry<Object>> es = new ObjectLongOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectLongOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectLongOrderedMap<?> data = new ObjectLongOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectIntMap with the given Json object, so ObjectIntMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectIntMap(@Nonnull Json json) {
        json.setSerializer(ObjectIntMap.class, new Json.Serializer<ObjectIntMap>() {
            @Override
            public void write(Json json, ObjectIntMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectIntMap.Entry<Object>> es = new ObjectIntMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectIntMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectIntMap<?> data = new ObjectIntMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectIntOrderedMap with the given Json object, so ObjectIntOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectIntOrderedMap(@Nonnull Json json) {
        json.setSerializer(ObjectIntOrderedMap.class, new Json.Serializer<ObjectIntOrderedMap>() {
            @Override
            public void write(Json json, ObjectIntOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectIntMap.Entry<Object>> es = new ObjectIntOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectIntOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectIntOrderedMap<?> data = new ObjectIntOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectFloatMap with the given Json object, so ObjectFloatMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectFloatMap(@Nonnull Json json) {
        json.setSerializer(ObjectFloatMap.class, new Json.Serializer<ObjectFloatMap>() {
            @Override
            public void write(Json json, ObjectFloatMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectFloatMap.Entry<Object>> es = new ObjectFloatMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectFloatMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectFloatMap<?> data = new ObjectFloatMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });

    }

    /**
     * Registers ObjectFloatOrderedMap with the given Json object, so ObjectFloatOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectFloatOrderedMap(@Nonnull Json json) {
        json.setSerializer(ObjectFloatOrderedMap.class, new Json.Serializer<ObjectFloatOrderedMap>() {
            @Override
            public void write(Json json, ObjectFloatOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<ObjectFloatMap.Entry<Object>> es = new ObjectFloatOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        writer.name(k);
                        json.writeValue(e.getValue());
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.pop();
                } catch (IOException ignored) {
                }
            }

            @Override
            public ObjectFloatOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectFloatOrderedMap<?> data = new ObjectFloatOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntObjectMap with the given Json object, so IntObjectMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntObjectMap(@Nonnull Json json) {
        json.setSerializer(IntObjectMap.class, new Json.Serializer<IntObjectMap>() {
            @Override
            public void write(Json json, IntObjectMap object, Class knownType) {
                json.writeObjectStart();
                for (IntObjectMap.Entry<Object> e : new IntObjectMap.Entries<Object>(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntObjectMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntObjectMap<?> data = new IntObjectMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntObjectOrderedMap with the given Json object, so IntObjectOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntObjectOrderedMap(@Nonnull Json json) {
        json.setSerializer(IntObjectOrderedMap.class, new Json.Serializer<IntObjectOrderedMap>() {
            @Override
            public void write(Json json, IntObjectOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (IntObjectOrderedMap.Entry<Object> e : new IntObjectOrderedMap.OrderedMapEntries<Object>(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntObjectOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntObjectOrderedMap<?> data = new IntObjectOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntIntMap with the given Json object, so IntIntMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntIntMap(@Nonnull Json json) {
        json.setSerializer(IntIntMap.class, new Json.Serializer<IntIntMap>() {
            @Override
            public void write(Json json, IntIntMap object, Class knownType) {
                json.writeObjectStart();
                for (IntIntMap.Entry e : new IntIntMap.Entries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntIntMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntIntMap data = new IntIntMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntIntOrderedMap with the given Json object, so IntIntOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntIntOrderedMap(@Nonnull Json json) {
        json.setSerializer(IntIntOrderedMap.class, new Json.Serializer<IntIntOrderedMap>() {
            @Override
            public void write(Json json, IntIntOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (IntIntOrderedMap.Entry e : new IntIntOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntIntOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntIntOrderedMap data = new IntIntOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntLongMap with the given Json object, so IntLongMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntLongMap(@Nonnull Json json) {
        json.setSerializer(IntLongMap.class, new Json.Serializer<IntLongMap>() {
            @Override
            public void write(Json json, IntLongMap object, Class knownType) {
                json.writeObjectStart();
                for (IntLongMap.Entry e : new IntLongMap.Entries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntLongMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntLongMap data = new IntLongMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntLongOrderedMap with the given Json object, so IntLongOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntLongOrderedMap(@Nonnull Json json) {
        json.setSerializer(IntLongOrderedMap.class, new Json.Serializer<IntLongOrderedMap>() {
            @Override
            public void write(Json json, IntLongOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (IntLongOrderedMap.Entry e : new IntLongOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntLongOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntLongOrderedMap data = new IntLongOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntFloatMap with the given Json object, so IntFloatMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntFloatMap(@Nonnull Json json) {
        json.setSerializer(IntFloatMap.class, new Json.Serializer<IntFloatMap>() {
            @Override
            public void write(Json json, IntFloatMap object, Class knownType) {
                json.writeObjectStart();
                for (IntFloatMap.Entry e : new IntFloatMap.Entries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntFloatMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntFloatMap data = new IntFloatMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntFloatOrderedMap with the given Json object, so IntFloatOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntFloatOrderedMap(@Nonnull Json json) {
        json.setSerializer(IntFloatOrderedMap.class, new Json.Serializer<IntFloatOrderedMap>() {
            @Override
            public void write(Json json, IntFloatOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (IntFloatOrderedMap.Entry e : new IntFloatOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Integer.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntFloatOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                IntFloatOrderedMap data = new IntFloatOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Integer.parseInt(value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongObjectMap with the given Json object, so LongObjectMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongObjectMap(@Nonnull Json json) {
        json.setSerializer(LongObjectMap.class, new Json.Serializer<LongObjectMap>() {
            @Override
            public void write(Json json, LongObjectMap object, Class knownType) {
                json.writeObjectStart();
                for (LongObjectMap.Entry<?> e : new LongObjectMap.Entries<Object>(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongObjectMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongObjectMap<?> data = new LongObjectMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongObjectOrderedMap with the given Json object, so LongObjectOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongObjectOrderedMap(@Nonnull Json json) {
        json.setSerializer(LongObjectOrderedMap.class, new Json.Serializer<LongObjectOrderedMap>() {
            @Override
            public void write(Json json, LongObjectOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (LongObjectOrderedMap.Entry<Object> e : new LongObjectOrderedMap.OrderedMapEntries<Object>(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongObjectOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongObjectOrderedMap<?> data = new LongObjectOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongIntMap with the given Json object, so LongIntMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongIntMap(@Nonnull Json json) {
        json.setSerializer(LongIntMap.class, new Json.Serializer<LongIntMap>() {
            @Override
            public void write(Json json, LongIntMap object, Class knownType) {
                json.writeObjectStart();
                for (LongIntMap.Entry e : new LongIntMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongIntMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongIntMap data = new LongIntMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongIntOrderedMap with the given Json object, so LongIntOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongIntOrderedMap(@Nonnull Json json) {
        json.setSerializer(LongIntOrderedMap.class, new Json.Serializer<LongIntOrderedMap>() {
            @Override
            public void write(Json json, LongIntOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (LongIntOrderedMap.Entry e : new LongIntOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongIntOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongIntOrderedMap data = new LongIntOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongLongMap with the given Json object, so LongLongMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongLongMap(@Nonnull Json json) {
        json.setSerializer(LongLongMap.class, new Json.Serializer<LongLongMap>() {
            @Override
            public void write(Json json, LongLongMap object, Class knownType) {
                json.writeObjectStart();
                for (LongLongMap.Entry e : new LongLongMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongLongMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongLongMap data = new LongLongMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongLongOrderedMap with the given Json object, so LongLongOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongLongOrderedMap(@Nonnull Json json) {
        json.setSerializer(LongLongOrderedMap.class, new Json.Serializer<LongLongOrderedMap>() {
            @Override
            public void write(Json json, LongLongOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (LongLongOrderedMap.Entry e : new LongLongOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongLongOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongLongOrderedMap data = new LongLongOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongFloatMap with the given Json object, so LongFloatMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongFloatMap(@Nonnull Json json) {
        json.setSerializer(LongFloatMap.class, new Json.Serializer<LongFloatMap>() {
            @Override
            public void write(Json json, LongFloatMap object, Class knownType) {
                json.writeObjectStart();
                for (LongFloatMap.Entry e : new LongFloatMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongFloatMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongFloatMap data = new LongFloatMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongFloatOrderedMap with the given Json object, so LongFloatOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongFloatOrderedMap(@Nonnull Json json) {
        json.setSerializer(LongFloatOrderedMap.class, new Json.Serializer<LongFloatOrderedMap>() {
            @Override
            public void write(Json json, LongFloatOrderedMap object, Class knownType) {
                json.writeObjectStart();
                for (LongFloatOrderedMap.Entry e : new LongFloatOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongFloatOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                LongFloatOrderedMap data = new LongFloatOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), json.readValue(float.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers BinaryHeap with the given Json object, so BinaryHeap can be written to and read from JSON.
     * It may be problematic to register a custom serializer for the Node items of the BinaryHeap, so it's
     * recommended that you don't; the output is more verbose with the default serialization, but it works.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBinaryHeap(@Nonnull Json json) {
        json.setSerializer(BinaryHeap.class, new Json.Serializer<BinaryHeap>() {
            @Override
            public void write(Json json, BinaryHeap object, Class knownType) {
                json.writeObjectStart();
                json.writeValue("max", object.isMaxHeap());
                json.writeArrayStart("items");
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public BinaryHeap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                BinaryHeap<?> data = new BinaryHeap<>(jsonData.size, jsonData.parent.getBoolean("max"));
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers NumberedSet with the given Json object, so NumberedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    
    public static void registerNumberedSet(@Nonnull Json json) {
        json.setSerializer(NumberedSet.class, new Json.Serializer<NumberedSet>() {
            @Override
            public void write(Json json, NumberedSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public NumberedSet<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                NumberedSet<?> data = new NumberedSet<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers CaseInsensitiveSet with the given Json object, so CaseInsensitiveSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCaseInsensitiveSet(@Nonnull Json json) {
        json.setSerializer(CaseInsensitiveSet.class, new Json.Serializer<CaseInsensitiveSet>() {
            @Override
            public void write(Json json, CaseInsensitiveSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public CaseInsensitiveSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveSet data = new CaseInsensitiveSet(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers CaseInsensitiveOrderedSet with the given Json object, so CaseInsensitiveOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCaseInsensitiveOrderedSet(@Nonnull Json json) {
        json.setSerializer(CaseInsensitiveOrderedSet.class, new Json.Serializer<CaseInsensitiveOrderedSet>() {
            @Override
            public void write(Json json, CaseInsensitiveOrderedSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public CaseInsensitiveOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveOrderedSet data = new CaseInsensitiveOrderedSet(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }
    
    /**
     * Registers CaseInsensitiveMap with the given Json object, so CaseInsensitiveMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCaseInsensitiveMap(@Nonnull Json json) {
        json.setSerializer(CaseInsensitiveMap.class, new Json.Serializer<CaseInsensitiveMap>() {
            @Override
            public void write(Json json, CaseInsensitiveMap object, Class knownType) {
                json.writeObjectStart();
                Iterator<Map.Entry<CharSequence, Object>> es = new CaseInsensitiveMap.Entries<CharSequence, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<CharSequence, ?> e = es.next();
                    json.writeValue(e.getKey().toString(), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveMap<?> data = new CaseInsensitiveMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(value.name, json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers CaseInsensitiveOrderedMap with the given Json object, so CaseInsensitiveOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCaseInsensitiveOrderedMap(@Nonnull Json json) {
        json.setSerializer(CaseInsensitiveOrderedMap.class, new Json.Serializer<CaseInsensitiveOrderedMap>() {
            @Override
            public void write(Json json, CaseInsensitiveOrderedMap object, Class knownType) {
                json.writeObjectStart();
                Iterator<Map.Entry<CharSequence, Object>> es = new ObjectObjectOrderedMap.OrderedMapEntries<CharSequence, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<CharSequence, ?> e = es.next();
                    json.writeValue(e.getKey().toString(), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveOrderedMap<?> data = new CaseInsensitiveOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(value.name, json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers AtomicLong with the given Json object, so AtomicLong can be written to and read from JSON.
     * This primarily matters if you intend to read and/or write {@link java.util.Random} objects or their subclasses
     * without registering a custom serializer for them. Although the EnhancedRandom implementations in jdkgdxds have
     * custom serializers here already, and don't need this method to be called, other classes that extend Random are
     * likely to need this if you ever run on Java 16 or higher.
     * <br>
     * Surprisingly, this method is compatible with GWT, even though most concurrent code doesn't work there.
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerAtomicLong(@Nonnull Json json) {
        json.setSerializer(AtomicLong.class, new Json.Serializer<AtomicLong>() {
            @Override
            public void write(Json json, AtomicLong object, Class knownType) {
                json.writeValue("`" + BASE.signed(object.get()) + "`");
            }

            @Override
            public AtomicLong read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 3) return null;
                final int tick = s.indexOf('`', 1);
                final long state = BASE.readLong(s, 1, tick);
                return new AtomicLong(state);
            }
        });
    }

    /**
     * Registers AlternateRandom with the given Json object, so AlternateRandom can be written to and read from JSON.
     * Note that AlternateRandom is not a juniper EnhancedRandom, and so cannot be deserialized to an EnhancedRandom
     * field. It is also almost exactly the same as {@link PasarRandom}, so you might want to just prefer PasarRandom
     * if you depend on juniper anyway because of this library.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerAlternateRandom(@Nonnull Json json) {
        json.addClassTag("AltR", AlternateRandom.class);
        json.setSerializer(AlternateRandom.class, new Json.Serializer<AlternateRandom>() {
            @Override
            public void write(Json json, AlternateRandom object, Class knownType) {
                json.writeValue(object.serializeToString());
            }

            @Override
            public AlternateRandom read(Json json, JsonValue jsonData, Class type) {
                AlternateRandom r = new AlternateRandom(1L, 2L, 3L, 4L, 5L);
                r.deserializeFromString(jsonData.asString());
                return r;
            }
        });
    }

    /**
     * Registers FourWheelRandom with the given Json object, so FourWheelRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFourWheelRandom(@Nonnull Json json) {
        json.addClassTag("FoWR", FourWheelRandom.class);
        json.setSerializer(FourWheelRandom.class, new Json.Serializer<FourWheelRandom>() {
            @Override
            public void write(Json json, FourWheelRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public FourWheelRandom read(Json json, JsonValue jsonData, Class type) {
                FourWheelRandom r = new FourWheelRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers TrimRandom with the given Json object, so TrimRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTrimRandom(@Nonnull Json json) {
        json.addClassTag("TrmR", TrimRandom.class);
        json.setSerializer(TrimRandom.class, new Json.Serializer<TrimRandom>() {
            @Override
            public void write(Json json, TrimRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public TrimRandom read(Json json, JsonValue jsonData, Class type) {
                TrimRandom r = new TrimRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers WhiskerRandom with the given Json object, so WhiskerRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerWhiskerRandom(@Nonnull Json json) {
        json.addClassTag("WhiR", WhiskerRandom.class);
        json.setSerializer(WhiskerRandom.class, new Json.Serializer<WhiskerRandom>() {
            @Override
            public void write(Json json, WhiskerRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public WhiskerRandom read(Json json, JsonValue jsonData, Class type) {
                WhiskerRandom r = new WhiskerRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers PasarRandom with the given Json object, so PasarRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerPasarRandom(@Nonnull Json json) {
        json.addClassTag("PasR", PasarRandom.class);
        json.setSerializer(PasarRandom.class, new Json.Serializer<PasarRandom>() {
            @Override
            public void write(Json json, PasarRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public PasarRandom read(Json json, JsonValue jsonData, Class type) {
                PasarRandom r = new PasarRandom(1L, 1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ChopRandom with the given Json object, so ChopRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChopRandom(@Nonnull Json json) {
        json.addClassTag("ChpR", ChopRandom.class);
        json.setSerializer(ChopRandom.class, new Json.Serializer<ChopRandom>() {
            @Override
            public void write(Json json, ChopRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ChopRandom read(Json json, JsonValue jsonData, Class type) {
                ChopRandom r = new ChopRandom(1, 1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Xoshiro128PlusPlusRandom with the given Json object, so Xoshiro128PlusPlusRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerXoshiro128PlusPlusRandom(@Nonnull Json json) {
        json.addClassTag("XPPR", Xoshiro128PlusPlusRandom.class);
        json.setSerializer(Xoshiro128PlusPlusRandom.class, new Json.Serializer<Xoshiro128PlusPlusRandom>() {
            @Override
            public void write(Json json, Xoshiro128PlusPlusRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Xoshiro128PlusPlusRandom read(Json json, JsonValue jsonData, Class type) {
                Xoshiro128PlusPlusRandom r = new Xoshiro128PlusPlusRandom(1, 1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers StrangerRandom with the given Json object, so StrangerRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerStrangerRandom(@Nonnull Json json) {
        json.addClassTag("StrR", StrangerRandom.class);
        json.setSerializer(StrangerRandom.class, new Json.Serializer<StrangerRandom>() {
            @Override
            public void write(Json json, StrangerRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public StrangerRandom read(Json json, JsonValue jsonData, Class type) {
                StrangerRandom r = new StrangerRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Xoshiro256StarStarRandom with the given Json object, so Xoshiro256StarStarRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerXoshiro256StarStarRandom(@Nonnull Json json) {
        json.addClassTag("XSSR", Xoshiro256StarStarRandom.class);
        json.setSerializer(Xoshiro256StarStarRandom.class, new Json.Serializer<Xoshiro256StarStarRandom>() {
            @Override
            public void write(Json json, Xoshiro256StarStarRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Xoshiro256StarStarRandom read(Json json, JsonValue jsonData, Class type) {
                Xoshiro256StarStarRandom r = new Xoshiro256StarStarRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers TricycleRandom with the given Json object, so TricycleRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTricycleRandom(@Nonnull Json json) {
        json.addClassTag("TriR", TricycleRandom.class);
        json.setSerializer(TricycleRandom.class, new Json.Serializer<TricycleRandom>() {
            @Override
            public void write(Json json, TricycleRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public TricycleRandom read(Json json, JsonValue jsonData, Class type) {
                TricycleRandom r = new TricycleRandom(1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers RomuTrioRandom with the given Json object, so RomuTrioRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerRomuTrioRandom(@Nonnull Json json) {
        json.addClassTag("RTrR", RomuTrioRandom.class);
        json.setSerializer(RomuTrioRandom.class, new Json.Serializer<RomuTrioRandom>() {
            @Override
            public void write(Json json, RomuTrioRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public RomuTrioRandom read(Json json, JsonValue jsonData, Class type) {
                RomuTrioRandom r = new RomuTrioRandom(1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LaserRandom with the given Json object, so LaserRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLaserRandom(@Nonnull Json json) {
        json.addClassTag("LasR", LaserRandom.class);
        json.setSerializer(LaserRandom.class, new Json.Serializer<LaserRandom>() {
            @Override
            public void write(Json json, LaserRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LaserRandom read(Json json, JsonValue jsonData, Class type) {
                LaserRandom r = new LaserRandom(1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers MizuchiRandom with the given Json object, so MizuchiRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerMizuchiRandom(@Nonnull Json json) {
        json.addClassTag("MizR", MizuchiRandom.class);
        json.setSerializer(MizuchiRandom.class, new Json.Serializer<MizuchiRandom>() {
            @Override
            public void write(Json json, MizuchiRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public MizuchiRandom read(Json json, JsonValue jsonData, Class type) {
                MizuchiRandom r = new MizuchiRandom(1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers DistinctRandom with the given Json object, so DistinctRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDistinctRandom(@Nonnull Json json) {
        json.addClassTag("DisR", DistinctRandom.class);
        json.setSerializer(DistinctRandom.class, new Json.Serializer<DistinctRandom>() {
            @Override
            public void write(Json json, DistinctRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public DistinctRandom read(Json json, JsonValue jsonData, Class type) {
                DistinctRandom r = new DistinctRandom(1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers GoldenQuasiRandom with the given Json object, so GoldenQuasiRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerGoldenQuasiRandom(@Nonnull Json json) {
        json.addClassTag("GoQR", GoldenQuasiRandom.class);
        json.setSerializer(GoldenQuasiRandom.class, new Json.Serializer<GoldenQuasiRandom>() {
            @Override
            public void write(Json json, GoldenQuasiRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public GoldenQuasiRandom read(Json json, JsonValue jsonData, Class type) {
                GoldenQuasiRandom r = new GoldenQuasiRandom(1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers VanDerCorputQuasiRandom with the given Json object, so VanDerCorputQuasiRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerVanDerCorputQuasiRandom(@Nonnull Json json) {
        json.addClassTag("VCQR", VanDerCorputQuasiRandom.class);
        json.setSerializer(VanDerCorputQuasiRandom.class, new Json.Serializer<VanDerCorputQuasiRandom>() {
            @Override
            public void write(Json json, VanDerCorputQuasiRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public VanDerCorputQuasiRandom read(Json json, JsonValue jsonData, Class type) {
                VanDerCorputQuasiRandom r = new VanDerCorputQuasiRandom(1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers DistributedRandom with the given Json object, so DistributedRandom can be written to and read from JSON.
     * This also registers all other EnhancedRandom types and all Distribution types.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDistributedRandom(@Nonnull Json json) {
        JsonSupport.registerEnhancedRandom(json);
        JsonSupport.registerDistribution(json);
        json.setSerializer(DistributedRandom.class, new Json.Serializer<DistributedRandom>() {
            @Override
            public void write(Json json, DistributedRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public DistributedRandom read(Json json, JsonValue jsonData, Class type) {
                return new DistributedRandom().stringDeserialize(jsonData.asString(), BASE);
            }
        });
    }

    /**
     * Registers EnhancedRandom with the given Json object, so EnhancedRandom can be written to and read from JSON.
     * This also registers {@link DistinctRandom}, {@link LaserRandom}, {@link TricycleRandom}, {@link FourWheelRandom},
     * {@link Xoshiro256StarStarRandom}, {@link StrangerRandom}, {@link TrimRandom}, {@link WhiskerRandom},
     * {@link RomuTrioRandom}, {@link ChopRandom}, {@link Xoshiro128PlusPlusRandom}, and {@link MizuchiRandom}, plus
     * {@link AtomicLong} because some subclasses of {@link java.util.Random} need it. This does not register
     * {@link DistributedRandom}, but {@link #registerDistributedRandom(Json)} calls this method instead (and also calls
     * {@link #registerDistribution(Json)}).
     * <br>
     * Abstract classes aren't usually serializable like this, but because each of the EnhancedRandom serializers uses a
     * specific format shared with what this uses, and that format identifies which class is used, it works here.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnhancedRandom(@Nonnull Json json) {
        registerAtomicLong(json);
        registerDistinctRandom(json);
        registerLaserRandom(json);
        registerTricycleRandom(json);
        registerFourWheelRandom(json);
        registerXoshiro256StarStarRandom(json);
        registerStrangerRandom(json);
        registerTrimRandom(json);
        registerWhiskerRandom(json);
        registerMizuchiRandom(json);
        registerRomuTrioRandom(json);
        registerChopRandom(json);
        registerXoshiro128PlusPlusRandom(json);
        registerPasarRandom(json);
        registerGoldenQuasiRandom(json);
        registerVanDerCorputQuasiRandom(json);
        json.setSerializer(EnhancedRandom.class, new Json.Serializer<EnhancedRandom>() {
            @Override
            public void write(Json json, EnhancedRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public EnhancedRandom read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                try {
                    return Deserializer.deserialize(jsonData.asString(), BASE);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /**
     * Registers RandomXS128 with the given Json object, so RandomXS128 can be written to and read from JSON.
     * Note that RandomXS128 is not a jdkgdxds EnhancedRandom, and so registering this won't allow you to read
     * RandomXS128 objects from EnhancedRandom fields. This serializer is here to reduce the hassle when you do want to
     * serialize a RandomXS128, because on Java 16 and higher, RandomXS128 would need some extra steps for Json to be
     * able to read some fields from it.
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerRandomXS128(@Nonnull Json json) {
        json.setSerializer(RandomXS128.class, new Json.Serializer<RandomXS128>() {
            @Override
            public void write(Json json, RandomXS128 object, Class knownType) {
                json.writeValue("`" + BASE.signed(object.getState(0)) + "~" + BASE.signed(object.getState(1)) + "`");
            }

            @Override
            public RandomXS128 read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 5) return null;
                final int split = s.indexOf('~', 1);
                final long stateA = BASE.readLong(s, 1, split);
                final long stateB = BASE.readLong(s, split + 1, s.indexOf('`', split));
                return new RandomXS128(stateA, stateB);
            }
        });
    }

    //// This can be used with no dependencies other than libGDX. It does allocate more than the above version.
//    public static void registerRandomXS128(@Nonnull Json json) {
//        json.setSerializer(RandomXS128.class, new Json.Serializer<RandomXS128>() {
//            @Override
//            public void write(Json json, RandomXS128 object, Class knownType) {
//                json.writeValue("`" + object.getState(0) + "~" + object.getState(1) + "`");
//            }
//
//            @Override
//            public RandomXS128 read(Json json, JsonValue jsonData, Class type) {
//                String s;
//                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 5) return null;
//                final int split = s.indexOf('~', 1);
//                final long stateA = Long.parseLong(s.substring(1, split));
//                final long stateB = Long.parseLong(s.substring(split + 1, s.indexOf('`', split)));
//                return new RandomXS128(stateA, stateB);
//            }
//        });
//    }


    /**
     * Registers ArcsineDistribution with the given Json object, so ArcsineDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerArcsineDistribution(@Nonnull Json json) {
        json.addClassTag("Arcsine", ArcsineDistribution.class);
        json.setSerializer(ArcsineDistribution.class, new Json.Serializer<ArcsineDistribution>() {
            @Override
            public void write(Json json, ArcsineDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ArcsineDistribution read(Json json, JsonValue jsonData, Class type) {
                ArcsineDistribution r = new ArcsineDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers BernoulliDistribution with the given Json object, so BernoulliDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBernoulliDistribution(@Nonnull Json json) {
        json.addClassTag("Bernoulli", BernoulliDistribution.class);
        json.setSerializer(BernoulliDistribution.class, new Json.Serializer<BernoulliDistribution>() {
            @Override
            public void write(Json json, BernoulliDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public BernoulliDistribution read(Json json, JsonValue jsonData, Class type) {
                BernoulliDistribution r = new BernoulliDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers BetaDistribution with the given Json object, so BetaDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBetaDistribution(@Nonnull Json json) {
        json.addClassTag("Beta", BetaDistribution.class);
        json.setSerializer(BetaDistribution.class, new Json.Serializer<BetaDistribution>() {
            @Override
            public void write(Json json, BetaDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public BetaDistribution read(Json json, JsonValue jsonData, Class type) {
                BetaDistribution r = new BetaDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers BetaPrimeDistribution with the given Json object, so BetaPrimeDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBetaPrimeDistribution(@Nonnull Json json) {
        json.addClassTag("BetaPrime", BetaPrimeDistribution.class);
        json.setSerializer(BetaPrimeDistribution.class, new Json.Serializer<BetaPrimeDistribution>() {
            @Override
            public void write(Json json, BetaPrimeDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public BetaPrimeDistribution read(Json json, JsonValue jsonData, Class type) {
                BetaPrimeDistribution r = new BetaPrimeDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers BinomialDistribution with the given Json object, so BinomialDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBinomialDistribution(@Nonnull Json json) {
        json.addClassTag("Binomial", BinomialDistribution.class);
        json.setSerializer(BinomialDistribution.class, new Json.Serializer<BinomialDistribution>() {
            @Override
            public void write(Json json, BinomialDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public BinomialDistribution read(Json json, JsonValue jsonData, Class type) {
                BinomialDistribution r = new BinomialDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers CauchyDistribution with the given Json object, so CauchyDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCauchyDistribution(@Nonnull Json json) {
        json.addClassTag("Cauchy", CauchyDistribution.class);
        json.setSerializer(CauchyDistribution.class, new Json.Serializer<CauchyDistribution>() {
            @Override
            public void write(Json json, CauchyDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public CauchyDistribution read(Json json, JsonValue jsonData, Class type) {
                CauchyDistribution r = new CauchyDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ChiDistribution with the given Json object, so ChiDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChiDistribution(@Nonnull Json json) {
        json.addClassTag("Chi", ChiDistribution.class);
        json.setSerializer(ChiDistribution.class, new Json.Serializer<ChiDistribution>() {
            @Override
            public void write(Json json, ChiDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ChiDistribution read(Json json, JsonValue jsonData, Class type) {
                ChiDistribution r = new ChiDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ChiSquareDistribution with the given Json object, so ChiSquareDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChiSquareDistribution(@Nonnull Json json) {
        json.addClassTag("ChiSquare", ChiSquareDistribution.class);
        json.setSerializer(ChiSquareDistribution.class, new Json.Serializer<ChiSquareDistribution>() {
            @Override
            public void write(Json json, ChiSquareDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ChiSquareDistribution read(Json json, JsonValue jsonData, Class type) {
                ChiSquareDistribution r = new ChiSquareDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ContinuousUniformDistribution with the given Json object, so ContinuousUniformDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerContinuousUniformDistribution(@Nonnull Json json) {
        json.addClassTag("ContinuousUniform", ContinuousUniformDistribution.class);
        json.setSerializer(ContinuousUniformDistribution.class, new Json.Serializer<ContinuousUniformDistribution>() {
            @Override
            public void write(Json json, ContinuousUniformDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ContinuousUniformDistribution read(Json json, JsonValue jsonData, Class type) {
                ContinuousUniformDistribution r = new ContinuousUniformDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers DiscreteUniformDistribution with the given Json object, so DiscreteUniformDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDiscreteUniformDistribution(@Nonnull Json json) {
        json.addClassTag("DiscreteUniform", DiscreteUniformDistribution.class);
        json.setSerializer(DiscreteUniformDistribution.class, new Json.Serializer<DiscreteUniformDistribution>() {
            @Override
            public void write(Json json, DiscreteUniformDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public DiscreteUniformDistribution read(Json json, JsonValue jsonData, Class type) {
                DiscreteUniformDistribution r = new DiscreteUniformDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ErlangDistribution with the given Json object, so ErlangDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerErlangDistribution(@Nonnull Json json) {
        json.addClassTag("Erlang", ErlangDistribution.class);
        json.setSerializer(ErlangDistribution.class, new Json.Serializer<ErlangDistribution>() {
            @Override
            public void write(Json json, ErlangDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ErlangDistribution read(Json json, JsonValue jsonData, Class type) {
                ErlangDistribution r = new ErlangDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ExponentialDistribution with the given Json object, so ExponentialDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerExponentialDistribution(@Nonnull Json json) {
        json.addClassTag("Exponential", ExponentialDistribution.class);
        json.setSerializer(ExponentialDistribution.class, new Json.Serializer<ExponentialDistribution>() {
            @Override
            public void write(Json json, ExponentialDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ExponentialDistribution read(Json json, JsonValue jsonData, Class type) {
                ExponentialDistribution r = new ExponentialDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers FisherSnedecorDistribution with the given Json object, so FisherSnedecorDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFisherSnedecorDistribution(@Nonnull Json json) {
        json.addClassTag("FisherSnedecor", FisherSnedecorDistribution.class);
        json.setSerializer(FisherSnedecorDistribution.class, new Json.Serializer<FisherSnedecorDistribution>() {
            @Override
            public void write(Json json, FisherSnedecorDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public FisherSnedecorDistribution read(Json json, JsonValue jsonData, Class type) {
                FisherSnedecorDistribution r = new FisherSnedecorDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers FisherTippettDistribution with the given Json object, so FisherTippettDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFisherTippettDistribution(@Nonnull Json json) {
        json.addClassTag("FisherTippett", FisherTippettDistribution.class);
        json.setSerializer(FisherTippettDistribution.class, new Json.Serializer<FisherTippettDistribution>() {
            @Override
            public void write(Json json, FisherTippettDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public FisherTippettDistribution read(Json json, JsonValue jsonData, Class type) {
                FisherTippettDistribution r = new FisherTippettDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers GammaDistribution with the given Json object, so GammaDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerGammaDistribution(@Nonnull Json json) {
        json.addClassTag("Gamma", GammaDistribution.class);
        json.setSerializer(GammaDistribution.class, new Json.Serializer<GammaDistribution>() {
            @Override
            public void write(Json json, GammaDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public GammaDistribution read(Json json, JsonValue jsonData, Class type) {
                GammaDistribution r = new GammaDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers GeometricDistribution with the given Json object, so GeometricDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerGeometricDistribution(@Nonnull Json json) {
        json.addClassTag("Geometric", GeometricDistribution.class);
        json.setSerializer(GeometricDistribution.class, new Json.Serializer<GeometricDistribution>() {
            @Override
            public void write(Json json, GeometricDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public GeometricDistribution read(Json json, JsonValue jsonData, Class type) {
                GeometricDistribution r = new GeometricDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers KumaraswamyDistribution with the given Json object, so KumaraswamyDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerKumaraswamyDistribution(@Nonnull Json json) {
        json.addClassTag("Kumaraswamy", KumaraswamyDistribution.class);
        json.setSerializer(KumaraswamyDistribution.class, new Json.Serializer<KumaraswamyDistribution>() {
            @Override
            public void write(Json json, KumaraswamyDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public KumaraswamyDistribution read(Json json, JsonValue jsonData, Class type) {
                KumaraswamyDistribution r = new KumaraswamyDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LaplaceDistribution with the given Json object, so LaplaceDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLaplaceDistribution(@Nonnull Json json) {
        json.addClassTag("Laplace", LaplaceDistribution.class);
        json.setSerializer(LaplaceDistribution.class, new Json.Serializer<LaplaceDistribution>() {
            @Override
            public void write(Json json, LaplaceDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LaplaceDistribution read(Json json, JsonValue jsonData, Class type) {
                LaplaceDistribution r = new LaplaceDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LogCauchyDistribution with the given Json object, so LogCauchyDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLogCauchyDistribution(@Nonnull Json json) {
        json.addClassTag("LogCauchy", LogCauchyDistribution.class);
        json.setSerializer(LogCauchyDistribution.class, new Json.Serializer<LogCauchyDistribution>() {
            @Override
            public void write(Json json, LogCauchyDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LogCauchyDistribution read(Json json, JsonValue jsonData, Class type) {
                LogCauchyDistribution r = new LogCauchyDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LogisticDistribution with the given Json object, so LogisticDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLogisticDistribution(@Nonnull Json json) {
        json.addClassTag("Logistic", LogisticDistribution.class);
        json.setSerializer(LogisticDistribution.class, new Json.Serializer<LogisticDistribution>() {
            @Override
            public void write(Json json, LogisticDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LogisticDistribution read(Json json, JsonValue jsonData, Class type) {
                LogisticDistribution r = new LogisticDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LogNormalDistribution with the given Json object, so LogNormalDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLogNormalDistribution(@Nonnull Json json) {
        json.addClassTag("LogNormal", LogNormalDistribution.class);
        json.setSerializer(LogNormalDistribution.class, new Json.Serializer<LogNormalDistribution>() {
            @Override
            public void write(Json json, LogNormalDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LogNormalDistribution read(Json json, JsonValue jsonData, Class type) {
                LogNormalDistribution r = new LogNormalDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LumpDistribution with the given Json object, so LumpDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLumpDistribution(@Nonnull Json json) {
        json.addClassTag("Lump", LumpDistribution.class);
        json.setSerializer(LumpDistribution.class, new Json.Serializer<LumpDistribution>() {
            @Override
            public void write(Json json, LumpDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LumpDistribution read(Json json, JsonValue jsonData, Class type) {
                LumpDistribution r = new LumpDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers NormalDistribution with the given Json object, so NormalDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerNormalDistribution(@Nonnull Json json) {
        json.addClassTag("Normal", NormalDistribution.class);
        json.setSerializer(NormalDistribution.class, new Json.Serializer<NormalDistribution>() {
            @Override
            public void write(Json json, NormalDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public NormalDistribution read(Json json, JsonValue jsonData, Class type) {
                NormalDistribution r = new NormalDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ParetoDistribution with the given Json object, so ParetoDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerParetoDistribution(@Nonnull Json json) {
        json.addClassTag("Pareto", ParetoDistribution.class);
        json.setSerializer(ParetoDistribution.class, new Json.Serializer<ParetoDistribution>() {
            @Override
            public void write(Json json, ParetoDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ParetoDistribution read(Json json, JsonValue jsonData, Class type) {
                ParetoDistribution r = new ParetoDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers PoissonDistribution with the given Json object, so PoissonDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerPoissonDistribution(@Nonnull Json json) {
        json.addClassTag("Poisson", PoissonDistribution.class);
        json.setSerializer(PoissonDistribution.class, new Json.Serializer<PoissonDistribution>() {
            @Override
            public void write(Json json, PoissonDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public PoissonDistribution read(Json json, JsonValue jsonData, Class type) {
                PoissonDistribution r = new PoissonDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers PowerDistribution with the given Json object, so PowerDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerPowerDistribution(@Nonnull Json json) {
        json.addClassTag("Power", PowerDistribution.class);
        json.setSerializer(PowerDistribution.class, new Json.Serializer<PowerDistribution>() {
            @Override
            public void write(Json json, PowerDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public PowerDistribution read(Json json, JsonValue jsonData, Class type) {
                PowerDistribution r = new PowerDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers RayleighDistribution with the given Json object, so RayleighDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerRayleighDistribution(@Nonnull Json json) {
        json.addClassTag("Rayleigh", RayleighDistribution.class);
        json.setSerializer(RayleighDistribution.class, new Json.Serializer<RayleighDistribution>() {
            @Override
            public void write(Json json, RayleighDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public RayleighDistribution read(Json json, JsonValue jsonData, Class type) {
                RayleighDistribution r = new RayleighDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers StudentsTDistribution with the given Json object, so StudentsTDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerStudentsTDistribution(@Nonnull Json json) {
        json.addClassTag("StudentsT", StudentsTDistribution.class);
        json.setSerializer(StudentsTDistribution.class, new Json.Serializer<StudentsTDistribution>() {
            @Override
            public void write(Json json, StudentsTDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public StudentsTDistribution read(Json json, JsonValue jsonData, Class type) {
                StudentsTDistribution r = new StudentsTDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers TriangularDistribution with the given Json object, so TriangularDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTriangularDistribution(@Nonnull Json json) {
        json.addClassTag("Triangular", TriangularDistribution.class);
        json.setSerializer(TriangularDistribution.class, new Json.Serializer<TriangularDistribution>() {
            @Override
            public void write(Json json, TriangularDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public TriangularDistribution read(Json json, JsonValue jsonData, Class type) {
                TriangularDistribution r = new TriangularDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers WeibullDistribution with the given Json object, so WeibullDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerWeibullDistribution(@Nonnull Json json) {
        json.addClassTag("Weibull", WeibullDistribution.class);
        json.setSerializer(WeibullDistribution.class, new Json.Serializer<WeibullDistribution>() {
            @Override
            public void write(Json json, WeibullDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public WeibullDistribution read(Json json, JsonValue jsonData, Class type) {
                WeibullDistribution r = new WeibullDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers ZipfianDistribution with the given Json object, so ZipfianDistribution can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerZipfianDistribution(@Nonnull Json json) {
        json.addClassTag("Zipfian", ZipfianDistribution.class);
        json.setSerializer(ZipfianDistribution.class, new Json.Serializer<ZipfianDistribution>() {
            @Override
            public void write(Json json, ZipfianDistribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ZipfianDistribution read(Json json, JsonValue jsonData, Class type) {
                ZipfianDistribution r = new ZipfianDistribution();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Distribution with the given Json object, so Distribution can be written to and read from JSON.
     * This also registers all currently-known Distribution subclasses.
     * <br>
     * Abstract classes aren't usually serializable like this, but because each of the Distribution serializers uses a
     * specific format shared with what this uses, and that format identifies which class is used, it works here.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDistribution(@Nonnull Json json) {
        registerArcsineDistribution(json);
        registerBernoulliDistribution(json);
        registerBetaDistribution(json);
        registerBetaPrimeDistribution(json);
        registerBinomialDistribution(json);
        registerCauchyDistribution(json);
        registerChiDistribution(json);
        registerChiSquareDistribution(json);
        registerContinuousUniformDistribution(json);
        registerDiscreteUniformDistribution(json);
        registerErlangDistribution(json);
        registerExponentialDistribution(json);
        registerExponentialDistribution(json);
        registerFisherSnedecorDistribution(json);
        registerFisherTippettDistribution(json);
        registerGammaDistribution(json);
        registerGeometricDistribution(json);
        registerKumaraswamyDistribution(json);
        registerLaplaceDistribution(json);
        registerLogCauchyDistribution(json);
        registerLogisticDistribution(json);
        registerLogNormalDistribution(json);
        registerLumpDistribution(json);
        registerNormalDistribution(json);
        registerParetoDistribution(json);
        registerPoissonDistribution(json);
        registerPowerDistribution(json);
        registerRayleighDistribution(json);
        registerStudentsTDistribution(json);
        registerTriangularDistribution(json);
        registerWeibullDistribution(json);
        registerZipfianDistribution(json);

        json.setSerializer(Distribution.class, new Json.Serializer<Distribution>() {
            @Override
            public void write(Json json, Distribution object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Distribution read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                try {
                    return Deserializer.deserializeDistribution(jsonData.asString(), BASE);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /**
     * Registers ObjectDeque with the given Json object, so ObjectDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectDeque(@Nonnull Json json) {
        json.setSerializer(ObjectDeque.class, new Json.Serializer<ObjectDeque>() {
            @Override
            public void write(Json json, ObjectDeque object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectDeque<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectDeque<?> data = new ObjectDeque<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers LongDeque with the given Json object, so LongDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongDeque(@Nonnull Json json) {
        json.setSerializer(LongDeque.class, new Json.Serializer<LongDeque>() {
            @Override
            public void write(Json json, LongDeque object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfLong it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
            }

            @Override
            public LongDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return LongDeque.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers IntDeque with the given Json object, so IntDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntDeque(@Nonnull Json json) {
        json.setSerializer(IntDeque.class, new Json.Serializer<IntDeque>() {
            @Override
            public void write(Json json, IntDeque object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfInt it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
            }

            @Override
            public IntDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return IntDeque.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers CharDeque with the given Json object, so CharDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCharDeque(@Nonnull Json json) {
        json.setSerializer(CharDeque.class, new Json.Serializer<CharDeque>() {
            @Override
            public void write(Json json, CharDeque object, Class knownType) {
                json.writeArrayStart();
                CharIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextChar());
                }
                json.writeArrayEnd();
            }

            @Override
            public CharDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return CharDeque.with(jsonData.asCharArray());
            }
        });
    }

    /**
     * Registers ShortDeque with the given Json object, so ShortDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerShortDeque(@Nonnull Json json) {
        json.setSerializer(ShortDeque.class, new Json.Serializer<ShortDeque>() {
            @Override
            public void write(Json json, ShortDeque object, Class knownType) {
                json.writeArrayStart();
                ShortIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextShort());
                }
                json.writeArrayEnd();
            }

            @Override
            public ShortDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return ShortDeque.with(jsonData.asShortArray());
            }
        });
    }

    /**
     * Registers ByteDeque with the given Json object, so ByteDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerByteDeque(@Nonnull Json json) {
        json.setSerializer(ByteDeque.class, new Json.Serializer<ByteDeque>() {
            @Override
            public void write(Json json, ByteDeque object, Class knownType) {
                json.writeArrayStart();
                ByteIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextByte());
                }
                json.writeArrayEnd();
            }

            @Override
            public ByteDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return ByteDeque.with(jsonData.asByteArray());
            }
        });
    }

    /**
     * Registers FloatDeque with the given Json object, so FloatDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFloatDeque(@Nonnull Json json) {
        json.setSerializer(FloatDeque.class, new Json.Serializer<FloatDeque>() {
            @Override
            public void write(Json json, FloatDeque object, Class knownType) {
                json.writeArrayStart();
                FloatIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextFloat());
                }
                json.writeArrayEnd();
            }

            @Override
            public FloatDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return FloatDeque.with(jsonData.asFloatArray());
            }
        });
    }

    /**
     * Registers DoubleDeque with the given Json object, so DoubleDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDoubleDeque(@Nonnull Json json) {
        json.setSerializer(DoubleDeque.class, new Json.Serializer<DoubleDeque>() {
            @Override
            public void write(Json json, DoubleDeque object, Class knownType) {
                json.writeArrayStart();
                PrimitiveIterator.OfDouble it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextDouble());
                }
                json.writeArrayEnd();
            }

            @Override
            public DoubleDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return DoubleDeque.with(jsonData.asDoubleArray());
            }
        });
    }

    /**
     * Registers BooleanDeque with the given Json object, so BooleanDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBooleanDeque(@Nonnull Json json) {
        json.setSerializer(BooleanDeque.class, new Json.Serializer<BooleanDeque>() {
            @Override
            public void write(Json json, BooleanDeque object, Class knownType) {
                json.writeArrayStart();
                BooleanIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextBoolean());
                }
                json.writeArrayEnd();
            }

            @Override
            public BooleanDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return BooleanDeque.with(jsonData.asBooleanArray());
            }
        });
    }

    /**
     * Registers Base with the given Json object, so Base can be written to and read from JSON.
     * This is a simple wrapper around Base's built-in {@link Base#serializeToString()} and
     * {@link Base#deserializeFromString(String)} methods.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBase(@Nonnull Json json) {
        json.setSerializer(Base.class, new Json.Serializer<Base>() {
            @Override
            public void write(Json json, Base object, Class knownType) {
                json.writeValue(object.serializeToString());
            }

            @Override
            public Base read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return Base.deserializeFromString(jsonData.asString());
            }
        });
    }
    /**
     * Registers Hasher with the given Json object, so Hasher can be written to and read from JSON.
     * This just stores the seed (which is a single {@code long}) as a String in the current Base used by JsonSupport.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerHasher(@Nonnull Json json) {
        json.setSerializer(Hasher.class, new Json.Serializer<Hasher>() {
            @Override
            public void write(Json json, Hasher object, Class knownType) {
                json.writeValue(BASE.signed(object.seed));
            }

            @Override
            public Hasher read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return new Hasher(BASE.readLong(jsonData.asString()));
            }
        });
    }

    /**
     * Registers the JDK Class type with the given Json object, so Class values can be written to and read from JSON.
     * This can be useful for code that needs to use Class values as keys or as values. Class keys can be used with an
     * {@link IdentityObjectMap} slightly more efficiently than an {@link ObjectObjectMap}.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerClass(@Nonnull Json json) {
        json.setSerializer(Class.class, new Json.Serializer<Class>() {
            @Override
            public void write(Json json, Class object, Class knownType) {
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
                json.writeValue(object.getName());
            }

            @Override
            public Class<?> read(Json json, JsonValue jsonData, Class type) {
                if(jsonData != null && !jsonData.isNull())
                {
                    try {
                        return ClassReflection.forName(jsonData.asString());
                    } catch (ReflectionException ignored) {
                    }
                }
                return null;
            }
        });

    }

}
