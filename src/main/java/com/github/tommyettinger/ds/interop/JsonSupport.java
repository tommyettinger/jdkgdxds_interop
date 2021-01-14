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
    private JsonSupport(){
    }

    /**
     * Registers JDKGDXDS' classes with the given Json object, allowing it to read and write JDKGDXDS types.
     * @param json a libGDX Json object that will have serializers registered for all JDKGDXDS types.
     */
    public static void registerWith(@Nonnull Json json) {
        json.setSerializer(ObjectList.class, new Json.Serializer<ObjectList>() {
            @Override
            public void write(Json json, ObjectList object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object){
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectList read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectList data = new ObjectList<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                return IntList.with(jsonData.asIntArray());
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                return LongList.with(jsonData.asLongArray());
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                return FloatList.with(jsonData.asFloatArray());
            }
        });

        json.setSerializer(ObjectSet.class, new Json.Serializer<ObjectSet>() {
            @Override
            public void write(Json json, ObjectSet object, Class knownType) {
                json.writeArrayStart();
                for (Object o : object){
                    json.writeValue(o);
                }
                json.writeArrayEnd();
            }

            @Override
            public ObjectSet read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectSet data = new ObjectSet<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectOrderedSet data = new ObjectOrderedSet<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                return IntSet.with(jsonData.asIntArray());
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                return IntOrderedSet.with(jsonData.asIntArray());
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                return LongSet.with(jsonData.asLongArray());
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                return LongOrderedSet.with(jsonData.asLongArray());
            }
        });
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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectObjectMap<?,?> data = new ObjectObjectMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(null, value));
                }
                return data;
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectObjectOrderedMap<?,?> data = new ObjectObjectOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(null, value));
                }
                return data;
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectLongMap<?> data = new ObjectLongMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectLongOrderedMap<?> data = new ObjectLongOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(long.class, value));
                }
                return data;
            }
        });

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
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectIntMap<?> data = new ObjectIntMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), json.readValue(int.class, value));
                }
                return data;
            }
        });

        json.setSerializer(ObjectIntOrderedMap.class, new Json.Serializer<ObjectIntOrderedMap>() {
            @Override
            public void write(Json json, ObjectIntOrderedMap object, Class knownType) {
                json.writeObjectStart();
                json.writeValue("k", object.order(), null, null);
                json.writeValue("v", new IntList(object.values()), null, null);
                json.writeObjectEnd();
            }

            @Override
            public ObjectIntOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectList<?> k = json.readValue("k", ObjectList.class, jsonData);
                IntList v = json.readValue("v", IntList.class, jsonData);
                return new ObjectIntOrderedMap<>(k, v);
            }
        });

        json.setSerializer(ObjectFloatMap.class, new Json.Serializer<ObjectFloatMap>() {
            @Override
            public void write(Json json, ObjectFloatMap object, Class knownType) {
                json.writeObjectStart();
                json.writeValue("k", new ObjectList(object.keySet()), null, null);
                json.writeValue("v", new FloatList(object.values()), null, null);
                json.writeObjectEnd();
            }

            @Override
            public ObjectFloatMap<?> read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectList<?> k = json.readValue("k", ObjectList.class, jsonData);
                FloatList v = json.readValue("v", FloatList.class, jsonData);
                return new ObjectFloatMap<>(k, v);
            }
        });

        json.setSerializer(ObjectFloatOrderedMap.class, new Json.Serializer<ObjectFloatOrderedMap>() {
            @Override
            public void write(Json json, ObjectFloatOrderedMap object, Class knownType) {
                json.writeObjectStart();
                json.writeValue("k", object.order(), null, null);
                json.writeValue("v", new FloatList(object.values()), null, null);
                json.writeObjectEnd();
            }

            @Override
            public ObjectFloatOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectList<?> k = json.readValue("k", ObjectList.class, jsonData);
                FloatList v = json.readValue("v", FloatList.class, jsonData);
                return new ObjectFloatOrderedMap<>(k, v);
            }
        });

    }
}
