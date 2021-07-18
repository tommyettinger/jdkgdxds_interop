package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.*;
import com.github.tommyettinger.ds.support.util.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("rawtypes")
public final class JsonSupport {
    private JsonSupport() {
    }

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

        // registers several others
        registerEnhancedRandom(json);

        registerRandomXS128(json);
    }

    /**
     * Registers ObjectList with the given Json object, so ObjectList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectList(@Nonnull Json json) {
        json.setSerializer(ObjectList.class, new Json.Serializer<ObjectList>() {
            @Override
            public void write(Json json, ObjectList object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectList<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectList<?> data = new ObjectList<>(jsonData.size);
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
        json.setSerializer(ObjectObjectMap.class, new Json.Serializer<ObjectObjectMap>() {
            @Override
            public void write(Json json, ObjectObjectMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                try {
                    writer.object();
                } catch (IOException ignored) {
                }
                Iterator<Map.Entry<?, ?>> es = new ObjectObjectMap.Entries<>(object).iterator();
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
                Iterator<Map.Entry<?, ?>> es = new ObjectObjectOrderedMap.OrderedMapEntries<>(object).iterator();
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
                Iterator<ObjectLongMap.Entry<?>> es = new ObjectLongMap.Entries<>(object).iterator();
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
                Iterator<ObjectLongMap.Entry<?>> es = new ObjectLongOrderedMap.OrderedMapEntries<>(object).iterator();
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
                Iterator<ObjectIntMap.Entry<?>> es = new ObjectIntMap.Entries<>(object).iterator();
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
                Iterator<ObjectIntMap.Entry<?>> es = new ObjectIntOrderedMap.OrderedMapEntries<>(object).iterator();
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
                Iterator<ObjectFloatMap.Entry<?>> es = new ObjectFloatMap.Entries<>(object).iterator();
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
                Iterator<ObjectFloatMap.Entry<?>> es = new ObjectFloatOrderedMap.OrderedMapEntries<>(object).iterator();
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
                for (IntObjectMap.Entry<?> e : (Iterable<IntObjectMap.Entry<?>>) new IntObjectMap.Entries<>(object)) {
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
                for (IntObjectOrderedMap.Entry<?> e : (Iterable<IntObjectOrderedMap.Entry<?>>) new IntObjectOrderedMap.OrderedMapEntries<>(object)) {
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
                    data.put(Integer.parseInt(value.name), json.readValue(long.class, value));
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
                for (LongObjectMap.Entry<?> e : (Iterable<LongObjectMap.Entry<?>>) new LongObjectMap.Entries<>(object)) {
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
                for (LongObjectOrderedMap.Entry<?> e : (Iterable<LongObjectOrderedMap.Entry<?>>) new LongObjectOrderedMap.OrderedMapEntries<>(object)) {
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
                Iterator<Map.Entry<CharSequence, ?>> es = new CaseInsensitiveMap.Entries<>(object).iterator();
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
                Iterator<Map.Entry<CharSequence, ?>> es = new ObjectObjectOrderedMap.OrderedMapEntries<>(object).iterator();
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
                json.writeValue("`" + Long.toString(object.get(), 36) + "`");
            }

            @Override
            public AtomicLong read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 3) return null;
                final int tick = s.indexOf('`', 1);
                final long state = Long.parseLong(s.substring(1, tick), 36);
                return new AtomicLong(state);
            }
        });
    }


    /**
     * Registers FourWheelRandom with the given Json object, so FourWheelRandom can be written to and read from JSON.
     * This is (currently) different from the registration for this class in jdkgdxds-interop, because this needs to be
     * in a format that can be read into an EnhancedRandom value by using a type tag stored in the serialized JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFourWheelRandom(@Nonnull Json json) {
        json.addClassTag("#FoWR", FourWheelRandom.class);
        json.setSerializer(FourWheelRandom.class, new Json.Serializer<FourWheelRandom>() {
            @Override
            public void write(Json json, FourWheelRandom object, Class knownType) {
                json.writeValue("#FoWR`" + Long.toString(object.getStateA(), 36) + "~" + Long.toString(object.getStateB(), 36) + "~" + Long.toString(object.getStateC(), 36) + "~" + Long.toString(object.getStateD(), 36) + "`");
            }

            @Override
            public FourWheelRandom read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 14) return null;
                int tilde = s.indexOf('~', 6);
                final long stateA = Long.parseLong(s.substring(6, tilde), 36);
                final long stateB = Long.parseLong(s.substring(tilde + 1, tilde = s.indexOf('~', tilde + 1)), 36);
                final long stateC = Long.parseLong(s.substring(tilde + 1, tilde = s.indexOf('~', tilde + 1)), 36);
                final long stateD = Long.parseLong(s.substring(tilde + 1, s.indexOf('`', tilde)), 36);
                return new FourWheelRandom(stateA, stateB, stateC, stateD);
            }
        });
    }

    /**
     * Registers TricycleRandom with the given Json object, so TricycleRandom can be written to and read from JSON.
     * This is (currently) different from the registration for this class in jdkgdxds-interop, because this needs to be
     * in a format that can be read into an EnhancedRandom value by using a type tag stored in the serialized JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTricycleRandom(@Nonnull Json json) {
        json.addClassTag("#TriR", TricycleRandom.class);
        json.setSerializer(TricycleRandom.class, new Json.Serializer<TricycleRandom>() {
            @Override
            public void write(Json json, TricycleRandom object, Class knownType) {
                json.writeValue("#TriR`" + Long.toString(object.getStateA(), 36) + "~" + Long.toString(object.getStateB(), 36) + "~" + Long.toString(object.getStateC(), 36) + "`");
            }

            @Override
            public TricycleRandom read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 12) return null;
                int tilde = s.indexOf('~', 6);
                final long stateA = Long.parseLong(s.substring(6, tilde), 36);
                final long stateB = Long.parseLong(s.substring(tilde + 1, tilde = s.indexOf('~', tilde + 1)), 36);
                final long stateC = Long.parseLong(s.substring(tilde + 1, s.indexOf('`', tilde)), 36);
                return new TricycleRandom(stateA, stateB, stateC);
            }
        });
    }

    /**
     * Registers LaserRandom with the given Json object, so LaserRandom can be written to and read from JSON.
     * This is (currently) different from the registration for this class in jdkgdxds-interop, because this needs to be
     * in a format that can be read into an EnhancedRandom value by using a type tag stored in the serialized JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLaserRandom(@Nonnull Json json) {
        json.addClassTag("#LasR", LaserRandom.class);
        json.setSerializer(LaserRandom.class, new Json.Serializer<LaserRandom>() {
            @Override
            public void write(Json json, LaserRandom object, Class knownType) {
                json.writeValue("#LasR`" + Long.toString(object.getStateA(), 36) + "~" + Long.toString(object.getStateB() >>> 1, 36) + "`");
            }

            @Override
            public LaserRandom read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 10) return null;
                final int tilde = s.indexOf('~', 6);
                final long stateA = Long.parseLong(s.substring(6, tilde), 36);
                final long stateB = Long.parseLong(s.substring(tilde + 1, s.indexOf('`', tilde)), 36) << 1;
                return new LaserRandom(stateA, stateB);
            }
        });
    }

    /**
     * Registers DistinctRandom with the given Json object, so DistinctRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDistinctRandom(@Nonnull Json json) {
        json.addClassTag("#DisR", DistinctRandom.class);
        json.setSerializer(DistinctRandom.class, new Json.Serializer<DistinctRandom>() {
            @Override
            public void write(Json json, DistinctRandom object, Class knownType) {
                json.writeValue("#DisR`" + Long.toString(object.getSelectedState(0), 36) + "`");
            }

            @Override
            public DistinctRandom read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 8) return null;
                final int tick = s.indexOf('`', 6);
                final long state = Long.parseLong(s.substring(6, tick), 36);
                return new DistinctRandom(state);
            }
        });
    }

    /**
     * Registers EnhancedRandom with the given Json object, so EnhancedRandom can be written to and read from JSON.
     * This also registers {@link DistinctRandom}, {@link LaserRandom}, {@link TricycleRandom}, and
     * {@link FourWheelRandom}, plus {@link AtomicLong} because some subclasses of {@link java.util.Random} need it.
     * Interfaces aren't usually serializable like this, but because each of the EnhancedRandom serializers uses a
     * specific format shared with what this uses, and that format identifies which class is used, it works here.
     * Well, it works as long as the EnhancedRandom implementation you are serializing or deserializing was itself
     * registered with this. You don't have to worry about that for any of the jdkgdxds EnhancedRandom types.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnhancedRandom(@Nonnull Json json) {
        JsonSupport.registerAtomicLong(json);
        registerDistinctRandom(json);
        registerLaserRandom(json);
        registerTricycleRandom(json);
        registerFourWheelRandom(json);
        json.setSerializer(EnhancedRandom.class, new Json.Serializer<EnhancedRandom>() {
            @Override
            public void write(Json json, EnhancedRandom object, Class knownType) {
                json.writeArrayStart();
                Class impl = object.getClass();
                String tag = json.getTag(impl);
                if(tag == null) tag = impl.getName();
                json.writeValue(tag);
                json.writeValue(object);
                json.writeArrayEnd();
            }

            @Override
            public EnhancedRandom read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                try {
                    String tag = jsonData.asString().substring(0, 5);
                    Class<?> impl = json.getClass(tag);
                    if(impl == null) impl = ClassReflection.forName(tag);
                    return (EnhancedRandom) json.readValue(impl, jsonData);
                } catch (ReflectionException | ClassCastException e) {
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
                json.writeValue("`" + Long.toString(object.getState(0), 36) + "~" + Long.toString(object.getState(1), 36) + "`");
            }

            @Override
            public RandomXS128 read(Json json, JsonValue jsonData, Class type) {
                String s;
                if (jsonData == null || jsonData.isNull() || (s = jsonData.asString()) == null || s.length() < 5) return null;
                final int tilde = s.indexOf('~', 1);
                final long stateA = Long.parseLong(s.substring(1, tilde), 36);
                final long stateB = Long.parseLong(s.substring(tilde + 1, s.indexOf('`', tilde)), 36);
                return new RandomXS128(stateA, stateB);
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
}
