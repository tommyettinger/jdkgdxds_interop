package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.FloatIterator;

import javax.annotation.Nonnull;
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
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
                if(object == null)
                {
                    json.writeValue(null);
                    return;
                }
                json.writeObjectStart();
                json.writeValue("k", new ObjectList(object.keySet()), null, null);
                json.writeValue("v", new ObjectList(object.values()), null, null);
                json.writeObjectEnd();
            }

            @Override
            public ObjectObjectMap<?, ?> read(Json json, JsonValue jsonData, Class type) {
                if(jsonData == null || jsonData.isNull()) return null;
                ObjectList<?> k = json.readValue("k", ObjectList.class, jsonData);
                ObjectList<?> v = json.readValue("v", ObjectList.class, jsonData);
                return new ObjectObjectMap<>(k, v);
            }
        });

    }
}
