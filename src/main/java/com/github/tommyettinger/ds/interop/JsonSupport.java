package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.tommyettinger.ds.IntOrderedSet;
import com.github.tommyettinger.ds.IntSet;
import com.github.tommyettinger.ds.ObjectOrderedSet;
import com.github.tommyettinger.ds.ObjectSet;

import javax.annotation.Nonnull;
import java.util.PrimitiveIterator;

@SuppressWarnings("rawtypes")
public class JsonSupport {
    private JsonSupport(){
    }

    /**
     * Registers JDKGDXDS' classes with the given Json object, allowing it to read and write JDKGDXDS types.
     * @param json a libGDX Json object that will have serializers registered for all JDKGDXDS types.
     */
    public static void registerWith(@Nonnull Json json) {
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

    }
}
