package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.FloatIterator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SuppressWarnings("rawtypes")
public class JsonSupport {
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
            public ObjectList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectList data = new ObjectList<>(jsonData.size);
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
                    json.writeValue(it.next());
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
                    json.writeValue(it.next());
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
                    json.writeValue(it.next());
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
            public ObjectSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectSet data = new ObjectSet<>(jsonData.size);
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
            public ObjectOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectOrderedSet data = new ObjectOrderedSet<>(jsonData.size);
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
                    json.writeValue(it.next());
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
                    json.writeValue(it.next());
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
                    json.writeValue(it.next());
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
                    json.writeValue(it.next());
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<Map.Entry<?, ?>> es = new ObjectObjectMap.Entries<>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<Map.Entry<?, ?>> es = new ObjectObjectOrderedMap.OrderedMapEntries<>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectLongMap.Entry<?>> es = new ObjectLongMap.Entries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectLongMap.Entry<?>> es = new ObjectLongOrderedMap.OrderedMapEntries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectIntMap.Entry<?>> es = new ObjectIntMap.Entries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectIntMap.Entry<?>> es = new ObjectIntOrderedMap.OrderedMapEntries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectFloatMap.Entry<?>> es = new ObjectFloatMap.Entries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
                Writer writer = json.getWriter();
                try {
                    writer.write('{');
                } catch (IOException ignored) {
                }
                Iterator<ObjectFloatMap.Entry<?>> es = new ObjectFloatOrderedMap.OrderedMapEntries<>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    try {
                        String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey());
                        json.setWriter(writer);
                        json.writeValue(k);
                        writer.write(':');
                        json.writeValue(e.getValue());
                        if (es.hasNext())
                            writer.write(',');
                    } catch (IOException ignored) {
                    }
                }
                try {
                    writer.write('}');
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
}
