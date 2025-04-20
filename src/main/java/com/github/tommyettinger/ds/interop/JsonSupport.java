/*
 * Copyright (c) 2023-2025 See AUTHORS file.
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

package com.github.tommyettinger.ds.interop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.tommyettinger.digital.*;
import com.github.tommyettinger.digital.Interpolations.Interpolator;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.*;
import com.github.tommyettinger.function.ObjToObjFunction;
import com.github.tommyettinger.random.*;
import com.github.tommyettinger.random.distribution.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("rawtypes")
public final class JsonSupport {
    private JsonSupport() {
    }

    @NonNull
    private static Base BASE = Base.BASE10;

    private static boolean LEGIBLE_FLOATS = true;

    private static boolean ADD_CLASS_TAGS = true;

    /**
     * Registers JDKGDXDS' classes with the given Json object, allowing it to read and write JDKGDXDS types.
     *
     * @param json a libGDX Json object that will have serializers registered for all JDKGDXDS types.
     */
    public static void registerAll(@NonNull Json json) {
        registerObjectList(json);
        registerIntList(json);
        registerLongList(json);
        registerFloatList(json);
        registerByteList(json);
        registerShortList(json);
        registerCharList(json);
        registerDoubleList(json);
        registerBooleanList(json);

        registerObjectBag(json);
        registerIntBag(json);
        registerLongBag(json);
        registerFloatBag(json);
        registerByteBag(json);
        registerShortBag(json);
        registerCharBag(json);
        registerDoubleBag(json);
        registerBooleanBag(json);

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
        registerOffsetBitSet(json);
        registerEnumSet(json);

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

        registerEnumMap(json);

        registerCaseInsensitiveSet(json);
        registerCaseInsensitiveOrderedSet(json);
        registerCaseInsensitiveMap(json);
        registerCaseInsensitiveOrderedMap(json);

        registerFilteredStringSet(json);
        registerFilteredStringOrderedSet(json);
        registerFilteredStringMap(json);
        registerFilteredStringOrderedMap(json);

        registerNumberedSet(json);

        registerBinaryHeap(json);

        registerJunction(json);

        // from digital.
        registerBase(json);
        registerHasher(json);
        registerAlternateRandom(json);
        registerInterpolator(json);

        // from juniper. these register many others.
        registerDistributedRandom(json);
        registerInterpolatedRandom(json);

        // from libGDX.
        registerRandomXS128(json);

        // from the JDK.
        registerClass(json);
    }


    /**
     * Gets the numeral system, also called radix or base, used by some methods here to encode numbers.
     * If it hasn't been changed, the default this uses is {@link Base#BASE10}, because it is the most human-readable.
     * @return the Base system this uses, which is always non-null.
     */
    public static Base getNumeralBase() {
        return BASE;
    }
    /**
     * Sets the numeral system, also called radix or base, used by some methods here to encode numbers.
     * This is most likely to be used with {@link Base#scrambledBase(Random)} to obfuscate specific numbers,
     * such as random seeds, put into readable JSON files. The methods this affects are mostly related to registering
     * {@link EnhancedRandom} and its implementations, {@link RandomXS128}, and {@link AtomicLong}. If this hasn't been
     * called, the default this uses is {@link Base#BASE10}, because it is the most human-readable.
     * @param base a non-null Base system
     */
    public static void setNumeralBase(Base base){
        if(base != null)
            BASE = base;
    }

    /**
     * Gets the status of whether this will write float and double items using {@link Base#general(float)} (when true)
     * or {@link Base#signed(int)} (when false). Sometimes one option will produce smaller output, and sometimes the
     * other will. If this is false, then there is a maximum length a float or double will use (in chars). If this is
     * true, then floats and doubles will always print in base-10, and only use scientific notation for very large or
     * very small numbers (determined by distance from 0.0), but the maximum length is larger.
     * @return true if this is writing floats in a human-readable way, or false if writing floats in a compact way
     */
    public static boolean areFloatsLegible() {
        return LEGIBLE_FLOATS;
    }

    /**
     * Sets the status of whether this will write float and double items using {@link Base#general(float)} (when true)
     * or {@link Base#signed(int)} (when false). Sometimes one option will produce smaller output, and sometimes the
     * other will. If this is false, then there is a maximum length a float or double will use (in chars). If this is
     * true, then floats and doubles will always print in base-10, and only use scientific notation for very large or
     * very small numbers (determined by distance from 0.0), but the maximum length is larger.
     * @param legibleFloats true to write floats in a human-readable way, or false to write floats in a compact way
     */
    public static void setFloatsLegible(boolean legibleFloats) {
        LEGIBLE_FLOATS = legibleFloats;
    }

    /**
     * Gets the status of whether this will add short class tags when registering classes. If true (the default), this
     * will use very short class tags. If false, this will use the normal Json behavior of package-qualified class
     * names, which can be quite long.
     * @return true if this is currently set to add class tags when registering classes, or false otherwise
     */
    public static boolean isAddClassTags() {
        return ADD_CLASS_TAGS;
    }

    /**
     * If true, this will call {@link Json#addClassTag(String, Class)} to register a very short name when
     * registering most classes. If false, registering classes will not register class tags. Using short class tags can
     * impede readability, but if users are expected to either not see raw JSON files or not care about {@code class}
     * entries in them, then using short tags saves a lot of file size. This is especially useful for games that
     * transmit JSON over a network. The default for this, if not yet called, is true (it will use short tags).
     * @param addClassTags if true, this will use short class tags instead of long names with packages for classes
     */
    public static void setAddClassTags(boolean addClassTags) {
        ADD_CLASS_TAGS = addClassTags;
    }

    private static String str(float data) {
        return LEGIBLE_FLOATS ? BASE.general(data) : BASE.signed(data);
    }

    private static String str(double data) {
        return LEGIBLE_FLOATS ? BASE.general(data) : BASE.signed(data);
    }

    private static StringBuilder append(StringBuilder sb, float data) {
        return LEGIBLE_FLOATS ? BASE.appendGeneral(sb, data) : BASE.appendSigned(sb, data);
    }

    private static StringBuilder append(StringBuilder sb, double data) {
        return LEGIBLE_FLOATS ? BASE.appendGeneral(sb, data) : BASE.appendSigned(sb, data);
    }

    private static String join(float[] data) {
        return LEGIBLE_FLOATS ? BASE.join(" ", data) : BASE.joinExact(" ", data);
    }

    private static String join(double[] data) {
        return LEGIBLE_FLOATS ? BASE.join(" ", data) : BASE.joinExact(" ", data);
    }

    private static StringBuilder appendJoined(StringBuilder sb, float[] data) {
        return LEGIBLE_FLOATS ? BASE.appendJoined(sb, " ", data) : BASE.appendJoinedExact(sb, " ", data);
    }

    private static StringBuilder appendJoined(StringBuilder sb, double[] data) {
        return LEGIBLE_FLOATS ? BASE.appendJoined(sb, " ", data) : BASE.appendJoinedExact(sb, " ", data);
    }

    private static String join(float[] data, int start, int length) {
        return LEGIBLE_FLOATS ? BASE.join(" ", data, start, length) : BASE.joinExact(" ", data, start, length);
    }

    private static String join(double[] data, int start, int length) {
        return LEGIBLE_FLOATS ? BASE.join(" ", data, start, length) : BASE.joinExact(" ", data, start, length);
    }

    private static StringBuilder appendJoined(StringBuilder sb, float[] data, int start, int length) {
        return LEGIBLE_FLOATS ? BASE.appendJoined(sb, " ", data, start, length) : BASE.appendJoinedExact(sb, " ", data, start, length);
    }

    private static StringBuilder appendJoined(StringBuilder sb, double[] data, int start, int length) {
        return LEGIBLE_FLOATS ? BASE.appendJoined(sb, " ", data, start, length) : BASE.appendJoinedExact(sb, " ", data, start, length);
    }

    private static float floatRead(String data) {
        return LEGIBLE_FLOATS ? BASE.readFloat(data) : BASE.readFloatExact(data);
    }

    private static double doubleRead(String data) {
        return LEGIBLE_FLOATS ? BASE.readDouble(data) : BASE.readDoubleExact(data);
    }

    private static float[] floatSplit(String data) {
        return LEGIBLE_FLOATS ? BASE.floatSplit(data, " ") : BASE.floatSplitExact(data, " ");
    }

    private static double[] doubleSplit(String data) {
        return LEGIBLE_FLOATS ? BASE.doubleSplit(data, " ") : BASE.doubleSplitExact(data, " ");
    }

    /**
     * Registers ObjectList with the given Json object, so ObjectList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oL", ObjectList.class);
        json.setSerializer(ObjectList.class, new Json.Serializer<ObjectList>() {
            @Override
            public void write(Json json, ObjectList object, Class knownType) {
                json.writeObjectStart(ObjectList.class, knownType);
                json.writeArrayStart("items");
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
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
    public static void registerIntList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iL", IntList.class);
        json.setSerializer(IntList.class, new Json.Serializer<IntList>() {
            @Override
            public void write(Json json, IntList object, Class knownType) {
                json.writeObjectStart(IntList.class, knownType);
                json.writeValue("items", BASE.join(" ", object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public IntList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return IntList.with(BASE.intSplit(jsonData.asString(), " "));
            }
        });

    }

    /**
     * Registers LongList with the given Json object, so LongList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lL", LongList.class);
        json.setSerializer(LongList.class, new Json.Serializer<LongList>() {
            @Override
            public void write(Json json, LongList object, Class knownType) {
                json.writeObjectStart(LongList.class, knownType);
                json.writeValue("items", BASE.join(" ", object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public LongList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return LongList.with(BASE.longSplit(jsonData.asString(), " "));
            }
        });
    }

    /**
     * Registers FloatList with the given Json object, so FloatList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFloatList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("fL", FloatList.class);
        json.setSerializer(FloatList.class, new Json.Serializer<FloatList>() {
            @Override
            public void write(Json json, FloatList object, Class knownType) {
                json.writeObjectStart(FloatList.class, knownType);
                json.writeValue("items", join(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public FloatList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return FloatList.with(floatSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers ByteList with the given Json object, so ByteList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerByteList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("bL", ByteList.class);
        json.setSerializer(ByteList.class, new Json.Serializer<ByteList>() {
            @Override
            public void write(Json json, ByteList object, Class knownType) {
                json.writeObjectStart(ByteList.class, knownType);
                json.writeValue("items", BASE.join(" ", object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public ByteList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ByteList.with(BASE.byteSplit(jsonData.asString(), " "));
            }
        });
    }

    /**
     * Registers ShortList with the given Json object, so ShortList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerShortList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("sL", ShortList.class);
        json.setSerializer(ShortList.class, new Json.Serializer<ShortList>() {
            @Override
            public void write(Json json, ShortList object, Class knownType) {
                json.writeObjectStart(ShortList.class, knownType);
                json.writeValue("items", BASE.join(" ", object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public ShortList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ShortList.with(BASE.shortSplit(jsonData.asString(), " "));
            }
        });
    }

    /**
     * Registers CharList with the given Json object, so CharList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCharList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("cL", CharList.class);
        json.setSerializer(CharList.class, new Json.Serializer<CharList>() {
            @Override
            public void write(Json json, CharList object, Class knownType) {
                json.writeObjectStart(CharList.class, knownType);
                json.writeValue("data", object.toDenseString(), String.class);
                json.writeObjectEnd();
            }

            @Override
            public CharList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("data")) == null) return null;
                return CharList.with(jsonData.asString().toCharArray());
            }
        });
    }

    /**
     * Registers DoubleList with the given Json object, so DoubleList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDoubleList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("dL", DoubleList.class);
        json.setSerializer(DoubleList.class, new Json.Serializer<DoubleList>() {
            @Override
            public void write(Json json, DoubleList object, Class knownType) {
                json.writeObjectStart(DoubleList.class, knownType);
                json.writeValue("items", join(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public DoubleList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return DoubleList.with(doubleSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers BooleanList with the given Json object, so BooleanList can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBooleanList(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("tL", BooleanList.class); // t for truth; represents boolean
        json.setSerializer(BooleanList.class, new Json.Serializer<BooleanList>() {
            @Override
            public void write(Json json, BooleanList object, Class knownType) {
                json.writeObjectStart(BooleanList.class, knownType);
                json.writeValue("items", TextTools.joinDense(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public BooleanList read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return BooleanList.with(TextTools.booleanSplitDense(jsonData.asString()));
            }
        });
    }

    /**
     * Registers ObjectBag with the given Json object, so ObjectBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oB", ObjectBag.class);
        json.setSerializer(ObjectBag.class, new Json.Serializer<ObjectBag>() {
            @Override
            public void write(Json json, ObjectBag object, Class knownType) {
                json.writeObjectStart(ObjectBag.class, knownType);
                json.writeArrayStart("items");
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ObjectBag<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                ObjectBag<?> data = new ObjectBag<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers IntBag with the given Json object, so IntBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iB", IntBag.class);
        json.setSerializer(IntBag.class, new Json.Serializer<IntBag>() {
            @Override
            public void write(Json json, IntBag object, Class knownType) {
                json.writeObjectStart(IntBag.class, knownType);
                json.writeArrayStart("items");
                IntIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public IntBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return IntBag.with(jsonData.asIntArray());
            }
        });

    }

    /**
     * Registers LongBag with the given Json object, so LongBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lB", LongBag.class);
        json.setSerializer(LongBag.class, new Json.Serializer<LongBag>() {
            @Override
            public void write(Json json, LongBag object, Class knownType) {
                json.writeObjectStart(LongBag.class, knownType);
                json.writeArrayStart("items");
                LongIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public LongBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return LongBag.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers FloatBag with the given Json object, so FloatBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFloatBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("fB", FloatBag.class);
        json.setSerializer(FloatBag.class, new Json.Serializer<FloatBag>() {
            @Override
            public void write(Json json, FloatBag object, Class knownType) {
                json.writeObjectStart(FloatBag.class, knownType);
                json.writeValue("items", join(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public FloatBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return FloatBag.with(floatSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers ByteBag with the given Json object, so ByteBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerByteBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("bB", ByteBag.class);
        json.setSerializer(ByteBag.class, new Json.Serializer<ByteBag>() {
            @Override
            public void write(Json json, ByteBag object, Class knownType) {
                json.writeObjectStart(ByteBag.class, knownType);
                json.writeArrayStart("items");
                ByteIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextByte());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ByteBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ByteBag.with(jsonData.asByteArray());
            }
        });
    }

    /**
     * Registers ShortBag with the given Json object, so ShortBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerShortBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("sB", ShortBag.class);
        json.setSerializer(ShortBag.class, new Json.Serializer<ShortBag>() {
            @Override
            public void write(Json json, ShortBag object, Class knownType) {
                json.writeObjectStart(ShortBag.class, knownType);
                json.writeArrayStart("items");
                ShortIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextShort());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ShortBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ShortBag.with(jsonData.asShortArray());
            }
        });
    }

    /**
     * Registers CharBag with the given Json object, so CharBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCharBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("cB", CharBag.class);
        json.setSerializer(CharBag.class, new Json.Serializer<CharBag>() {
            @Override
            public void write(Json json, CharBag object, Class knownType) {
                json.writeObjectStart(CharBag.class, knownType);
                json.writeArrayStart("items");
                CharIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextChar());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public CharBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return CharBag.with(jsonData.asCharArray());
            }
        });
    }

    /**
     * Registers DoubleBag with the given Json object, so DoubleBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDoubleBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("dB", DoubleBag.class);
        json.setSerializer(DoubleBag.class, new Json.Serializer<DoubleBag>() {
            @Override
            public void write(Json json, DoubleBag object, Class knownType) {
                json.writeObjectStart(DoubleBag.class, knownType);
                json.writeValue("items", join(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public DoubleBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return DoubleBag.with(doubleSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers BooleanBag with the given Json object, so BooleanBag can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBooleanBag(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("tB", BooleanBag.class); // t for truth; represents boolean
        json.setSerializer(BooleanBag.class, new Json.Serializer<BooleanBag>() {
            @Override
            public void write(Json json, BooleanBag object, Class knownType) {
                json.writeObjectStart(BooleanBag.class, knownType);
                json.writeValue("items", TextTools.joinDense(object.items, 0, object.size()));
                json.writeObjectEnd();
            }

            @Override
            public BooleanBag read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return BooleanBag.with(TextTools.booleanSplitDense(jsonData.asString()));
            }
        });
    }

    /**
     * Registers ObjectDeque with the given Json object, so ObjectDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oQ", ObjectDeque.class);
        json.setSerializer(ObjectDeque.class, new Json.Serializer<ObjectDeque>() {
            @Override
            public void write(Json json, ObjectDeque object, Class knownType) {
                json.writeObjectStart(ObjectDeque.class, knownType);
                json.writeArrayStart("items");
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
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
    public static void registerLongDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lQ", LongDeque.class);
        json.setSerializer(LongDeque.class, new Json.Serializer<LongDeque>() {
            @Override
            public void write(Json json, LongDeque object, Class knownType) {
                json.writeObjectStart(LongDeque.class, knownType);
                json.writeArrayStart("items");
                LongIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public LongDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return LongDeque.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers IntDeque with the given Json object, so IntDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iQ", IntDeque.class);
        json.setSerializer(IntDeque.class, new Json.Serializer<IntDeque>() {
            @Override
            public void write(Json json, IntDeque object, Class knownType) {
                json.writeObjectStart(IntDeque.class, knownType);
                json.writeArrayStart("items");
                IntIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public IntDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return IntDeque.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers CharDeque with the given Json object, so CharDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCharDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("cQ", CharDeque.class);
        json.setSerializer(CharDeque.class, new Json.Serializer<CharDeque>() {
            @Override
            public void write(Json json, CharDeque object, Class knownType) {
                json.writeObjectStart(CharDeque.class, knownType);
                json.writeArrayStart("items");
                CharIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextChar());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public CharDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return CharDeque.with(jsonData.asCharArray());
            }
        });
    }

    /**
     * Registers ShortDeque with the given Json object, so ShortDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerShortDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("sQ", ShortDeque.class);
        json.setSerializer(ShortDeque.class, new Json.Serializer<ShortDeque>() {
            @Override
            public void write(Json json, ShortDeque object, Class knownType) {
                json.writeObjectStart(ShortDeque.class, knownType);
                json.writeArrayStart("items");
                ShortIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextShort());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ShortDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ShortDeque.with(jsonData.asShortArray());
            }
        });
    }

    /**
     * Registers ByteDeque with the given Json object, so ByteDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerByteDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("bQ", ByteDeque.class);
        json.setSerializer(ByteDeque.class, new Json.Serializer<ByteDeque>() {
            @Override
            public void write(Json json, ByteDeque object, Class knownType) {
                json.writeObjectStart(ByteDeque.class, knownType);
                json.writeArrayStart("items");
                ByteIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextByte());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public ByteDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return ByteDeque.with(jsonData.asByteArray());
            }
        });
    }

    /**
     * Registers FloatDeque with the given Json object, so FloatDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFloatDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("fQ", FloatDeque.class);
        json.setSerializer(FloatDeque.class, new Json.Serializer<FloatDeque>() {
            @Override
            public void write(Json json, FloatDeque object, Class knownType) {
                json.writeObjectStart(FloatDeque.class, knownType);
                StringBuilder sb = new StringBuilder(object.size());
                FloatIterator it = object.iterator();
                while (it.hasNext()) {
                    sb.append(' ');
                    append(sb, it.nextFloat());
                }
                json.writeValue("items", sb.substring(1));
                json.writeObjectEnd();
            }

            @Override
            public FloatDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return FloatDeque.with(floatSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers DoubleDeque with the given Json object, so DoubleDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDoubleDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("dQ", DoubleDeque.class);
        json.setSerializer(DoubleDeque.class, new Json.Serializer<DoubleDeque>() {
            @Override
            public void write(Json json, DoubleDeque object, Class knownType) {
                json.writeObjectStart(DoubleDeque.class, knownType);
                StringBuilder sb = new StringBuilder(object.size());
                DoubleIterator it = object.iterator();
                while (it.hasNext()) {
                    sb.append(' ');
                    append(sb, it.nextDouble());
                }
                json.writeValue("items", sb.substring(1));
                json.writeObjectEnd();
            }

            @Override
            public DoubleDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return DoubleDeque.with(doubleSplit(jsonData.asString()));
            }
        });
    }

    /**
     * Registers BooleanDeque with the given Json object, so BooleanDeque can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBooleanDeque(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("tQ", ObjectDeque.class); // t for truth (boolean)
        json.setSerializer(BooleanDeque.class, new Json.Serializer<BooleanDeque>() {
            @Override
            public void write(Json json, BooleanDeque object, Class knownType) {
                json.writeObjectStart(BooleanDeque.class, knownType);
                StringBuilder sb = new StringBuilder(object.size());
                BooleanIterator it = object.iterator();
                while (it.hasNext()) {
                    sb.append(it.nextBoolean() ? '1' : '0');
                }
                json.writeValue("items", sb.toString());
                json.writeObjectEnd();
            }

            @Override
            public BooleanDeque read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return BooleanDeque.with(TextTools.booleanSplitDense(jsonData.asString()));
            }
        });
    }

    /**
     * Registers EnumSet with the given Json object, so EnumSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("eS", EnumSet.class);
        json.setSerializer(EnumSet.class, new Json.Serializer<EnumSet>() {
            @Override
            public void write(Json json, EnumSet object, Class knownType) {
                json.writeObjectStart(EnumSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Enum<?> o : object) {
                    json.writeValue(o, Enum.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                EnumSet data = new EnumSet();
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(Enum.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumOrderedSet with the given Json object, so EnumOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("eOS", EnumOrderedSet.class);
        json.setSerializer(EnumOrderedSet.class, new Json.Serializer<EnumOrderedSet>() {
            @Override
            public void write(Json json, EnumOrderedSet object, Class knownType) {
                json.writeObjectStart(EnumOrderedSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Enum<?> o : object) {
                    json.writeValue(o, Enum.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                EnumOrderedSet data = new EnumOrderedSet();
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(json.readValue(Enum.class, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers ObjectSet with the given Json object, so ObjectSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oS", ObjectSet.class);
        json.setSerializer(ObjectSet.class, new Json.Serializer<ObjectSet>() {
            @Override
            public void write(Json json, ObjectSet object, Class knownType) {
                json.writeObjectStart(ObjectSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
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
    public static void registerObjectOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oOS", ObjectOrderedSet.class);
        json.setSerializer(ObjectOrderedSet.class, new Json.Serializer<ObjectOrderedSet>() {
            @Override
            public void write(Json json, ObjectOrderedSet object, Class knownType) {
                json.writeObjectStart(ObjectOrderedSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
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
    public static void registerIntSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iS", IntSet.class);
        json.setSerializer(IntSet.class, new Json.Serializer<IntSet>() {
            @Override
            public void write(Json json, IntSet object, Class knownType) {
                json.writeObjectStart(IntSet.class, knownType);
                json.writeArrayStart("items");
                IntIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public IntSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return IntSet.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers IntOrderedSet with the given Json object, so IntOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerIntOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iOS", IntOrderedSet.class);
        json.setSerializer(IntOrderedSet.class, new Json.Serializer<IntOrderedSet>() {
            @Override
            public void write(Json json, IntOrderedSet object, Class knownType) {
                json.writeObjectStart(IntOrderedSet.class, knownType);
                json.writeArrayStart("items");
                IntIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public IntOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return IntOrderedSet.with(jsonData.asIntArray());
            }
        });
    }

    /**
     * Registers LongSet with the given Json object, so LongSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lS", LongSet.class);
        json.setSerializer(LongSet.class, new Json.Serializer<LongSet>() {
            @Override
            public void write(Json json, LongSet object, Class knownType) {
                json.writeObjectStart(LongSet.class, knownType);
                json.writeArrayStart("items");
                LongIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public LongSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return LongSet.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers LongOrderedSet with the given Json object, so LongOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lOS", LongOrderedSet.class);
        json.setSerializer(LongOrderedSet.class, new Json.Serializer<LongOrderedSet>() {
            @Override
            public void write(Json json, LongOrderedSet object, Class knownType) {
                json.writeObjectStart(LongOrderedSet.class, knownType);
                json.writeArrayStart("items");
                LongIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextLong());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public LongOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull() || (jsonData = jsonData.get("items")) == null) return null;
                return LongOrderedSet.with(jsonData.asLongArray());
            }
        });
    }

    /**
     * Registers EnumMap with the given Json object, so EnumMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("eoM", EnumMap.class);
        json.setSerializer(EnumMap.class, new Json.Serializer<EnumMap>() {
            @Override
            public void write(Json json, EnumMap object, Class knownType) {
                json.writeObjectStart(EnumMap.class, knownType);
                Iterator<Map.Entry<Enum<?>, Object>> es = new EnumMap.Entries<Object>(object).iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    Map.Entry<Enum<?>, ?> e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumMap<?> data = new EnumMap<>();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), json.readValue(null, value = value.next));
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumOrderedMap with the given Json object, so EnumOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("eoOM", EnumOrderedMap.class);
        json.setSerializer(EnumOrderedMap.class, new Json.Serializer<EnumOrderedMap>() {
            @Override
            public void write(Json json, EnumOrderedMap object, Class knownType) {
                json.writeObjectStart(EnumOrderedMap.class, knownType);
                Iterator<Map.Entry<Enum<?>, Object>> es = object.entrySet().iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    Map.Entry<Enum<?>, ?> e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumOrderedMap<?> data = new EnumOrderedMap<>();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), json.readValue(null, value = value.next));
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumIntMap with the given Json object, so EnumIntMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumIntMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elM", EnumIntMap.class);
        json.setSerializer(EnumIntMap.class, new Json.Serializer<EnumIntMap>() {
            @Override
            public void write(Json json, EnumIntMap object, Class knownType) {
                json.writeObjectStart(EnumIntMap.class, knownType);
                Iterator<EnumIntMap.Entry> es = object.entrySet().iterator().iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumIntMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), int.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumIntMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumIntMap data = new EnumIntMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asInt());
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumIntOrderedMap with the given Json object, so EnumIntOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumIntOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elOM", EnumIntOrderedMap.class);
        json.setSerializer(EnumIntOrderedMap.class, new Json.Serializer<EnumIntOrderedMap>() {
            @Override
            public void write(Json json, EnumIntOrderedMap object, Class knownType) {
                json.writeObjectStart(EnumIntOrderedMap.class, knownType);
                Iterator<EnumIntMap.Entry> es = new EnumIntOrderedMap.OrderedMapEntries(object).iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumIntMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), int.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumIntOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumIntOrderedMap data = new EnumIntOrderedMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asInt());
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumLongMap with the given Json object, so EnumLongMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumLongMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elM", EnumLongMap.class);
        json.setSerializer(EnumLongMap.class, new Json.Serializer<EnumLongMap>() {
            @Override
            public void write(Json json, EnumLongMap object, Class knownType) {
                json.writeObjectStart(EnumLongMap.class, knownType);
                Iterator<EnumLongMap.Entry> es = object.entrySet().iterator().iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumLongMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), long.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumLongMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumLongMap data = new EnumLongMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asLong());
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumLongOrderedMap with the given Json object, so EnumLongOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumLongOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elOM", EnumLongOrderedMap.class);
        json.setSerializer(EnumLongOrderedMap.class, new Json.Serializer<EnumLongOrderedMap>() {
            @Override
            public void write(Json json, EnumLongOrderedMap object, Class knownType) {
                json.writeObjectStart(EnumLongOrderedMap.class, knownType);
                Iterator<EnumLongMap.Entry> es = new EnumLongOrderedMap.OrderedMapEntries(object).iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumLongMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), long.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumLongOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumLongOrderedMap data = new EnumLongOrderedMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asLong());
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumFloatMap with the given Json object, so EnumFloatMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumFloatMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elM", EnumFloatMap.class);
        json.setSerializer(EnumFloatMap.class, new Json.Serializer<EnumFloatMap>() {
            @Override
            public void write(Json json, EnumFloatMap object, Class knownType) {
                json.writeObjectStart(EnumFloatMap.class, knownType);
                Iterator<EnumFloatMap.Entry> es = object.entrySet().iterator().iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumFloatMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), float.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumFloatMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumFloatMap data = new EnumFloatMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asFloat());
                }
                return data;
            }
        });
    }

    /**
     * Registers EnumFloatOrderedMap with the given Json object, so EnumFloatOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnumFloatOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("elOM", EnumFloatOrderedMap.class);
        json.setSerializer(EnumFloatOrderedMap.class, new Json.Serializer<EnumFloatOrderedMap>() {
            @Override
            public void write(Json json, EnumFloatOrderedMap object, Class knownType) {
                json.writeObjectStart(EnumFloatOrderedMap.class, knownType);
                Iterator<EnumFloatMap.Entry> es = new EnumFloatOrderedMap.OrderedMapEntries(object).iterator();
                json.writeArrayStart("parts");
                while (es.hasNext()) {
                    EnumFloatMap.Entry e = es.next();
                    json.writeValue(e.getKey(), Enum.class);
                    json.writeValue(e.getValue(), float.class);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public EnumFloatOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                EnumFloatOrderedMap data = new EnumFloatOrderedMap();
                for (JsonValue value = jsonData.getChild("parts"); value != null; value = value.next) {
                    data.put(json.readValue(Enum.class, value), (value = value.next).asFloat());
                }
                return data;
            }
        });
    }

    /**
     * Registers ObjectObjectMap with the given Json object, so ObjectObjectMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerObjectObjectMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooM", ObjectObjectMap.class);
        json.setSerializer(ObjectObjectMap.class, new Json.Serializer<ObjectObjectMap>() {
            @Override
            public void write(Json json, ObjectObjectMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectObjectMap.class, knownType);
                Iterator<Map.Entry<Object, Object>> es = new ObjectObjectMap.Entries<Object, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectObjectMap<?, ?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
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
    public static void registerObjectObjectOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooOM", ObjectObjectOrderedMap.class);
        json.setSerializer(ObjectObjectOrderedMap.class, new Json.Serializer<ObjectObjectOrderedMap>() {
            @Override
            public void write(Json json, ObjectObjectOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectObjectOrderedMap.class, knownType);
                Iterator<Map.Entry<Object, Object>> es = new ObjectObjectOrderedMap.OrderedMapEntries<Object, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<?, ?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectObjectOrderedMap<?, ?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
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
    public static void registerObjectLongMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("olM", ObjectLongMap.class);
        json.setSerializer(ObjectLongMap.class, new Json.Serializer<ObjectLongMap>() {
            @Override
            public void write(Json json, ObjectLongMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectLongMap.class, knownType);
                Iterator<ObjectLongMap.Entry<Object>> es = new ObjectLongMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, BASE.signed(e.getValue()), String.class);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectLongMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectLongMap<?> data = new ObjectLongMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), BASE.readLong(value.asString()));
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
    public static void registerObjectLongOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("olOM", ObjectLongOrderedMap.class);
        json.setSerializer(ObjectLongOrderedMap.class, new Json.Serializer<ObjectLongOrderedMap>() {
            @Override
            public void write(Json json, ObjectLongOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectLongOrderedMap.class, knownType);
                Iterator<ObjectLongMap.Entry<Object>> es = new ObjectLongOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectLongMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, BASE.signed(e.getValue()), String.class);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectLongOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectLongOrderedMap<?> data = new ObjectLongOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), BASE.readLong(value.asString()));
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
    public static void registerObjectIntMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oiM", ObjectIntMap.class);
        json.setSerializer(ObjectIntMap.class, new Json.Serializer<ObjectIntMap>() {
            @Override
            public void write(Json json, ObjectIntMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectIntMap.class, knownType);
                Iterator<ObjectIntMap.Entry<Object>> es = new ObjectIntMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, BASE.signed(e.getValue()), String.class);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectIntMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectIntMap<?> data = new ObjectIntMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), BASE.readInt(value.asString()));
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
    public static void registerObjectIntOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oiOM", ObjectIntOrderedMap.class);
        json.setSerializer(ObjectIntOrderedMap.class, new Json.Serializer<ObjectIntOrderedMap>() {
            @Override
            public void write(Json json, ObjectIntOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectIntOrderedMap.class, knownType);
                Iterator<ObjectIntMap.Entry<Object>> es = new ObjectIntOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectIntMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, BASE.signed(e.getValue()), String.class);
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectIntOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectIntOrderedMap<?> data = new ObjectIntOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), BASE.readInt(value.asString()));
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
    public static void registerObjectFloatMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ofM", ObjectFloatMap.class);
        json.setSerializer(ObjectFloatMap.class, new Json.Serializer<ObjectFloatMap>() {
            @Override
            public void write(Json json, ObjectFloatMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectFloatMap.class, knownType);
                Iterator<ObjectFloatMap.Entry<Object>> es = new ObjectFloatMap.Entries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectFloatMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectFloatMap<?> data = new ObjectFloatMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), value.asFloat());
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
    public static void registerObjectFloatOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ofOM", ObjectFloatOrderedMap.class);
        json.setSerializer(ObjectFloatOrderedMap.class, new Json.Serializer<ObjectFloatOrderedMap>() {
            @Override
            public void write(Json json, ObjectFloatOrderedMap object, Class knownType) {
                JsonWriter writer = json.getWriter();
                json.writeObjectStart(ObjectFloatOrderedMap.class, knownType);
                Iterator<ObjectFloatMap.Entry<Object>> es = new ObjectFloatOrderedMap.OrderedMapEntries<Object>(object).iterator();
                while (es.hasNext()) {
                    ObjectFloatMap.Entry<?> e = es.next();
                    String k = e.getKey() instanceof CharSequence ? e.getKey().toString() : json.toJson(e.getKey(), (Class) null);
                    json.setWriter(writer);
                    json.writeValue(k, e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public ObjectFloatOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                ObjectFloatOrderedMap<?> data = new ObjectFloatOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(json.fromJson(null, value.name), value.asFloat());
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
    public static void registerIntObjectMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ioM", IntObjectMap.class);
        json.setSerializer(IntObjectMap.class, new Json.Serializer<IntObjectMap>() {
            @Override
            public void write(Json json, IntObjectMap object, Class knownType) {
                json.writeObjectStart(IntObjectMap.class, knownType);
                for (IntObjectMap.Entry<Object> e : new IntObjectMap.Entries<Object>(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public IntObjectMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntObjectMap<?> data = new IntObjectMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), json.readValue(null, value));
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
    public static void registerIntObjectOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ioOM", IntObjectOrderedMap.class);
        json.setSerializer(IntObjectOrderedMap.class, new Json.Serializer<IntObjectOrderedMap>() {
            @Override
            public void write(Json json, IntObjectOrderedMap object, Class knownType) {
                json.writeObjectStart(IntObjectOrderedMap.class, knownType);
                for (IntObjectOrderedMap.Entry<Object> e : new IntObjectOrderedMap.OrderedMapEntries<Object>(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public IntObjectOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntObjectOrderedMap<?> data = new IntObjectOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), json.readValue(null, value));
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
    public static void registerIntIntMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iiM", IntIntMap.class);
        json.setSerializer(IntIntMap.class, new Json.Serializer<IntIntMap>() {
            @Override
            public void write(Json json, IntIntMap object, Class knownType) {
                json.writeObjectStart(IntIntMap.class, knownType);
                for (IntIntMap.Entry e : new IntIntMap.Entries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntIntMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntIntMap data = new IntIntMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asInt());
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
    public static void registerIntIntOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iiOM", IntIntOrderedMap.class);
        json.setSerializer(IntIntOrderedMap.class, new Json.Serializer<IntIntOrderedMap>() {
            @Override
            public void write(Json json, IntIntOrderedMap object, Class knownType) {
                json.writeObjectStart(IntIntOrderedMap.class, knownType);
                for (IntIntOrderedMap.Entry e : new IntIntOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntIntOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntIntOrderedMap data = new IntIntOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asInt());
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
    public static void registerIntLongMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ilM", IntLongMap.class);
        json.setSerializer(IntLongMap.class, new Json.Serializer<IntLongMap>() {
            @Override
            public void write(Json json, IntLongMap object, Class knownType) {
                json.writeObjectStart(IntLongMap.class, knownType);
                for (IntLongMap.Entry e : new IntLongMap.Entries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntLongMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntLongMap data = new IntLongMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asLong());
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
    public static void registerIntLongOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ilOM", IntLongOrderedMap.class);
        json.setSerializer(IntLongOrderedMap.class, new Json.Serializer<IntLongOrderedMap>() {
            @Override
            public void write(Json json, IntLongOrderedMap object, Class knownType) {
                json.writeObjectStart(IntLongOrderedMap.class, knownType);
                for (IntLongOrderedMap.Entry e : new IntLongOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntLongOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntLongOrderedMap data = new IntLongOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asLong());
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
    public static void registerIntFloatMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ifM", IntFloatMap.class);
        json.setSerializer(IntFloatMap.class, new Json.Serializer<IntFloatMap>() {
            @Override
            public void write(Json json, IntFloatMap object, Class knownType) {
                json.writeObjectStart(IntFloatMap.class, knownType);
                for (IntFloatMap.Entry e : new IntFloatMap.Entries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntFloatMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntFloatMap data = new IntFloatMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asFloat());
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
    public static void registerIntFloatOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ifOM", IntFloatOrderedMap.class);
        json.setSerializer(IntFloatOrderedMap.class, new Json.Serializer<IntFloatOrderedMap>() {
            @Override
            public void write(Json json, IntFloatOrderedMap object, Class knownType) {
                json.writeObjectStart(IntFloatOrderedMap.class, knownType);
                for (IntFloatOrderedMap.Entry e : new IntFloatOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(BASE.signed(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public IntFloatOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                IntFloatOrderedMap data = new IntFloatOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(BASE.readInt(value.name), value.asFloat());
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
    public static void registerLongObjectMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("loM", LongObjectMap.class);
        json.setSerializer(LongObjectMap.class, new Json.Serializer<LongObjectMap>() {
            @Override
            public void write(Json json, LongObjectMap object, Class knownType) {
                json.writeObjectStart(LongObjectMap.class, knownType);
                for (LongObjectMap.Entry<?> e : new LongObjectMap.Entries<Object>(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public LongObjectMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
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
    public static void registerLongObjectOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("loOM", LongObjectOrderedMap.class);
        json.setSerializer(LongObjectOrderedMap.class, new Json.Serializer<LongObjectOrderedMap>() {
            @Override
            public void write(Json json, LongObjectOrderedMap object, Class knownType) {
                json.writeObjectStart(LongObjectOrderedMap.class, knownType);
                for (LongObjectOrderedMap.Entry<Object> e : new LongObjectOrderedMap.OrderedMapEntries<Object>(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public LongObjectOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
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
    public static void registerLongIntMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("liM", LongIntMap.class);
        json.setSerializer(LongIntMap.class, new Json.Serializer<LongIntMap>() {
            @Override
            public void write(Json json, LongIntMap object, Class knownType) {
                json.writeObjectStart(LongIntMap.class, knownType);
                for (LongIntMap.Entry e : new LongIntMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongIntMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongIntMap data = new LongIntMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asInt());
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
    public static void registerLongIntOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("liOM", LongIntOrderedMap.class);
        json.setSerializer(LongIntOrderedMap.class, new Json.Serializer<LongIntOrderedMap>() {
            @Override
            public void write(Json json, LongIntOrderedMap object, Class knownType) {
                json.writeObjectStart(LongIntOrderedMap.class, knownType);
                for (LongIntOrderedMap.Entry e : new LongIntOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongIntOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongIntOrderedMap data = new LongIntOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asInt());
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
    public static void registerLongLongMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("llM", LongLongMap.class);
        json.setSerializer(LongLongMap.class, new Json.Serializer<LongLongMap>() {
            @Override
            public void write(Json json, LongLongMap object, Class knownType) {
                json.writeObjectStart(LongLongMap.class, knownType);
                for (LongLongMap.Entry e : new LongLongMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongLongMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongLongMap data = new LongLongMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asLong());
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
    public static void registerLongLongOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("llOM", LongLongOrderedMap.class);
        json.setSerializer(LongLongOrderedMap.class, new Json.Serializer<LongLongOrderedMap>() {
            @Override
            public void write(Json json, LongLongOrderedMap object, Class knownType) {
                json.writeObjectStart(LongLongOrderedMap.class, knownType);
                for (LongLongOrderedMap.Entry e : new LongLongOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongLongOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongLongOrderedMap data = new LongLongOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asLong());
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
    public static void registerLongFloatMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lfM", LongFloatMap.class);
        json.setSerializer(LongFloatMap.class, new Json.Serializer<LongFloatMap>() {
            @Override
            public void write(Json json, LongFloatMap object, Class knownType) {
                json.writeObjectStart(LongFloatMap.class, knownType);
                for (LongFloatMap.Entry e : new LongFloatMap.Entries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongFloatMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongFloatMap data = new LongFloatMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asFloat());
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
    public static void registerLongFloatOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("lfOM", LongFloatOrderedMap.class);
        json.setSerializer(LongFloatOrderedMap.class, new Json.Serializer<LongFloatOrderedMap>() {
            @Override
            public void write(Json json, LongFloatOrderedMap object, Class knownType) {
                json.writeObjectStart(LongFloatOrderedMap.class, knownType);
                for (LongFloatOrderedMap.Entry e : new LongFloatOrderedMap.OrderedMapEntries(object)) {
                    json.writeValue(Long.toString(e.key), e.getValue());
                }
                json.writeObjectEnd();
            }

            @Override
            public LongFloatOrderedMap read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                LongFloatOrderedMap data = new LongFloatOrderedMap(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(Long.parseLong(value.name), value.asFloat());
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
    public static void registerBinaryHeap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oBQ", BinaryHeap.class); // object items, Bit kind, Queue type
        json.setSerializer(BinaryHeap.class, new Json.Serializer<BinaryHeap>() {
            @Override
            public void write(Json json, BinaryHeap object, Class knownType) {
                json.writeObjectStart(BinaryHeap.class, knownType);
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
    
    public static void registerNumberedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oNS", NumberedSet.class); // object items, Numbered kind, Set type
        json.setSerializer(NumberedSet.class, new Json.Serializer<NumberedSet>() {
            @Override
            public void write(Json json, NumberedSet object, Class knownType) {
                json.writeObjectStart(NumberedSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o, null);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
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
    public static void registerCaseInsensitiveSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oCS", CaseInsensitiveSet.class); // object items, Case-insensitive kind, Set type
        json.setSerializer(CaseInsensitiveSet.class, new Json.Serializer<CaseInsensitiveSet>() {
            @Override
            public void write(Json json, CaseInsensitiveSet object, Class knownType) {
                json.writeObjectStart(CaseInsensitiveSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveSet data = new CaseInsensitiveSet(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(value.asString());
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
    public static void registerCaseInsensitiveOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oCOS", CaseInsensitiveOrderedSet.class); // object items, Case-insensitive+Ordered kind, Set type
        json.setSerializer(CaseInsensitiveOrderedSet.class, new Json.Serializer<CaseInsensitiveOrderedSet>() {
            @Override
            public void write(Json json, CaseInsensitiveOrderedSet object, Class knownType) {
                json.writeObjectStart(CaseInsensitiveOrderedSet.class, knownType);
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CaseInsensitiveOrderedSet data = new CaseInsensitiveOrderedSet(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(value.asString());
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
    public static void registerCaseInsensitiveMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooCM", CaseInsensitiveSet.class); // object keys, object values, Case-insensitive kind, Map type
        json.setSerializer(CaseInsensitiveMap.class, new Json.Serializer<CaseInsensitiveMap>() {
            @Override
            public void write(Json json, CaseInsensitiveMap object, Class knownType) {
                json.writeObjectStart(CaseInsensitiveMap.class, knownType);
                Iterator<Map.Entry<CharSequence, Object>> es = new CaseInsensitiveMap.Entries<CharSequence, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<CharSequence, ?> e = es.next();
                    json.writeValue(e.getKey().toString(), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
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
    public static void registerCaseInsensitiveOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooCOM", CaseInsensitiveSet.class); // object keys, object values, Case-insensitive+Ordered kind, Map type
        json.setSerializer(CaseInsensitiveOrderedMap.class, new Json.Serializer<CaseInsensitiveOrderedMap>() {
            @Override
            public void write(Json json, CaseInsensitiveOrderedMap object, Class knownType) {
                json.writeObjectStart(CaseInsensitiveOrderedMap.class, knownType);
                Iterator<Map.Entry<CharSequence, Object>> es = new ObjectObjectOrderedMap.OrderedMapEntries<CharSequence, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<CharSequence, ?> e = es.next();
                    json.writeValue(e.getKey().toString(), e.getValue(), null);
                }
                json.writeObjectEnd();
            }

            @Override
            public CaseInsensitiveOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                CaseInsensitiveOrderedMap<?> data = new CaseInsensitiveOrderedMap<>(jsonData.size);
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(value.name, json.readValue(null, value));
                }
                return data;
            }
        });
    }

    /**
     * Registers FilteredStringSet with the given Json object, so FilteredStringSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFilteredStringSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oFSS", FilteredStringSet.class); // object items, Filtered String kind, Set type
        json.setSerializer(FilteredStringSet.class, new Json.Serializer<FilteredStringSet>() {
            @Override
            public void write(Json json, FilteredStringSet object, Class knownType) {
                json.writeObjectStart(FilteredStringSet.class, knownType);
                json.writeValue("filtering", object.getFilter().getName());
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public FilteredStringSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CharFilter filter = CharFilter.get(jsonData.parent.getString("filtering"));
                FilteredStringSet data = new FilteredStringSet(filter, jsonData.size, Utilities.getDefaultLoadFactor());
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(value.asString());
                }
                return data;
            }
        });
    }
    

    /**
     * Registers FilteredStringOrderedSet with the given Json object, so FilteredStringOrderedSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFilteredStringOrderedSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("oFSOS", FilteredStringOrderedSet.class); // object items, Filtered String kind, Ordered kind, Set type
        json.setSerializer(FilteredStringOrderedSet.class, new Json.Serializer<FilteredStringOrderedSet>() {
            @Override
            public void write(Json json, FilteredStringOrderedSet object, Class knownType) {
                json.writeObjectStart(FilteredStringOrderedSet.class, knownType);
                json.writeValue("filtering", object.getFilter().getName());
                json.writeArrayStart("items"); // This name is special.
                for (Object o : object) {
                    json.writeValue(o);
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public FilteredStringOrderedSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                CharFilter filter = CharFilter.get(jsonData.parent.getString("filtering"));
                FilteredStringOrderedSet data = new FilteredStringOrderedSet(filter, jsonData.size, Utilities.getDefaultLoadFactor());
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.add(value.asString());
                }
                return data;
            }
        });
    }

    /**
     * Registers FilteredStringMap with the given Json object, so FilteredStringMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFilteredStringMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooFSM", FilteredStringSet.class); // object keys, object values, Filtered String kind, Map type
        json.setSerializer(FilteredStringMap.class, new Json.Serializer<FilteredStringMap>() {
            @Override
            public void write(Json json, FilteredStringMap object, Class knownType) {
                json.writeObjectStart(FilteredStringMap.class, knownType);
                json.writeValue("filtering", object.getFilter().getName());
                json.writeObjectStart("data");
                Iterator<Map.Entry<String, Object>> es = new FilteredStringMap.Entries<String, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<String, ?> e = es.next();
                    json.writeValue(e.getKey(), e.getValue(), null);
                }
                json.writeObjectEnd();
                json.writeObjectEnd();
            }

            @Override
            public FilteredStringMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                CharFilter filter = CharFilter.get(jsonData.getString("filtering"));
                jsonData = jsonData.get("data");
                FilteredStringMap<?> data = new FilteredStringMap<>(filter, jsonData.size, Utilities.getDefaultLoadFactor());
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(value.name, json.readValue(null, value));
                }
                return data;
            }
        });
    }
    

    /**
     * Registers FilteredStringOrderedMap with the given Json object, so FilteredStringOrderedMap can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFilteredStringOrderedMap(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ooFSOM", FilteredStringSet.class); // object keys, object values, Filtered String kind, Ordered kind, Map type
        json.setSerializer(FilteredStringOrderedMap.class, new Json.Serializer<FilteredStringOrderedMap>() {
            @Override
            public void write(Json json, FilteredStringOrderedMap object, Class knownType) {
                json.writeObjectStart(FilteredStringOrderedMap.class, knownType);
                json.writeValue("filtering", object.getFilter().getName());
                json.writeObjectStart("data");
                Iterator<Map.Entry<String, Object>> es = new FilteredStringOrderedMap.Entries<String, Object>(object).iterator();
                while (es.hasNext()) {
                    Map.Entry<String, ?> e = es.next();
                    json.writeValue(e.getKey(), e.getValue(), null);
                }
                json.writeObjectEnd();
                json.writeObjectEnd();
            }

            @Override
            public FilteredStringOrderedMap<?> read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                JsonValue tag = jsonData.get("class");
                if(tag != null) tag.remove();
                CharFilter filter = CharFilter.get(jsonData.getString("filtering"));
                jsonData = jsonData.get("data");
                FilteredStringOrderedMap<?> data = new FilteredStringOrderedMap<>(filter, jsonData.size, Utilities.getDefaultLoadFactor());
                for (JsonValue value = jsonData.child; value != null; value = value.next) {
                    data.put(value.name, json.readValue(null, value));
                }
                return data;
            }
        });
    }
    
    /**
     * Registers OffsetBitSet with the given Json object, so OffsetBitSet can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerOffsetBitSet(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("iBS", OffsetBitSet.class); // int items, Bit kind, Set type
        json.setSerializer(OffsetBitSet.class, new Json.Serializer<OffsetBitSet>() {
            @Override
            public void write(Json json, OffsetBitSet object, Class knownType) {
                int off = object.getOffset();
                json.writeObjectStart(OffsetBitSet.class, knownType);
                json.writeValue("offset", off);
                json.writeArrayStart("values");
                IntIterator it = object.iterator();
                while (it.hasNext()) {
                    json.writeValue(it.nextInt());
                }
                json.writeArrayEnd();
                json.writeObjectEnd();
            }

            @Override
            public OffsetBitSet read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                OffsetBitSet obs = new OffsetBitSet();
                obs.setOffset(jsonData.get("offset").asInt());
                obs.addAll(jsonData.get("values").asIntArray());
                return obs;
            }
        });
    }

    /**
     * Only adds class tags for {@link Junction}, {@link Junction.Any}, {@link Junction.All}, {@link Junction.One},
     * {@link Junction.Not}, adds {@link Junction.Leaf}, and only if {@link #ADD_CLASS_TAGS} is true. You may also want
     * to add a class tag for the item type of the Junction, such as {@code json.addClassTag("Str", String.class);} .
     * <br>
     * Note: If you have a Junction of String, then {@link Junction#toString()} can produce a valid serialized String
     * that can be read back in with {@link Junction#parse(String)} . More complicated parsing is possible via
     * {@link Junction#parse(ObjToObjFunction, String)}, but it has to operate on the results of toString() on items, at
     * least for now.
     *
     * @param json a libGDX Json object that will have class tags added, if allowed
     */
    public static void registerJunction(@NonNull Json json) {
        if(ADD_CLASS_TAGS) {
            json.addClassTag("Junc", Junction.class);
            json.addClassTag("JAny", Junction.Any.class);
            json.addClassTag("JAll", Junction.All.class);
            json.addClassTag("JOne", Junction.One.class);
            json.addClassTag("JNot", Junction.Not.class);
            json.addClassTag("JLea", Junction.Leaf.class);
        }
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
    public static void registerAtomicLong(@NonNull Json json) {
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
    public static void registerAlternateRandom(@NonNull Json json) {
        if(json.getSerializer(AlternateRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("AltR", AlternateRandom.class);
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
    public static void registerFourWheelRandom(@NonNull Json json) {
        if(json.getSerializer(FourWheelRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("FoWR", FourWheelRandom.class);
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
    public static void registerTrimRandom(@NonNull Json json) {
        if(json.getSerializer(TrimRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("TrmR", TrimRandom.class);
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
    public static void registerWhiskerRandom(@NonNull Json json) {
        if(json.getSerializer(WhiskerRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("WhiR", WhiskerRandom.class);
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
    public static void registerPasarRandom(@NonNull Json json) {
        if(json.getSerializer(PasarRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("PasR", PasarRandom.class);
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
     * Registers AceRandom with the given Json object, so AceRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerAceRandom(@NonNull Json json) {
        if(json.getSerializer(AceRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("AceR", AceRandom.class);
        json.setSerializer(AceRandom.class, new Json.Serializer<AceRandom>() {
            @Override
            public void write(Json json, AceRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public AceRandom read(Json json, JsonValue jsonData, Class type) {
                AceRandom r = new AceRandom(1L, 1L, 1L, 1L, 1L);
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
    public static void registerChopRandom(@NonNull Json json) {
        if(json.getSerializer(ChopRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("ChpR", ChopRandom.class);
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
     * Registers Jsf32Random with the given Json object, so Jsf32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerJsf32Random(@NonNull Json json) {
        if(json.getSerializer(Jsf32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("JS3R", Jsf32Random.class);
        json.setSerializer(Jsf32Random.class, new Json.Serializer<Jsf32Random>() {
            @Override
            public void write(Json json, Jsf32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Jsf32Random read(Json json, JsonValue jsonData, Class type) {
                Jsf32Random r = new Jsf32Random(1, 1, 1, 1);
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
    public static void registerXoshiro128PlusPlusRandom(@NonNull Json json) {
        if(json.getSerializer(Xoshiro128PlusPlusRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XPPR", Xoshiro128PlusPlusRandom.class);
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
     * Registers Chip32Random with the given Json object, so Chip32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChip32Random(@NonNull Json json) {
        if(json.getSerializer(Chip32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XPPR", Chip32Random.class);
        json.setSerializer(Chip32Random.class, new Json.Serializer<Chip32Random>() {
            @Override
            public void write(Json json, Chip32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Chip32Random read(Json json, JsonValue jsonData, Class type) {
                Chip32Random r = new Chip32Random(1, 1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Xoshiro160RoadroxoRandom with the given Json object, so Xoshiro160RoadroxoRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerXoshiro160RoadroxoRandom(@NonNull Json json) {
        if(json.getSerializer(Xoshiro160RoadroxoRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XPPR", Xoshiro160RoadroxoRandom.class);
        json.setSerializer(Xoshiro160RoadroxoRandom.class, new Json.Serializer<Xoshiro160RoadroxoRandom>() {
            @Override
            public void write(Json json, Xoshiro160RoadroxoRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Xoshiro160RoadroxoRandom read(Json json, JsonValue jsonData, Class type) {
                Xoshiro160RoadroxoRandom r = new Xoshiro160RoadroxoRandom(1, 1, 1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Respite32Random with the given Json object, so Respite32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerRespite32Random(@NonNull Json json) {
        if(json.getSerializer(Respite32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("Re3R", Respite32Random.class);
        json.setSerializer(Respite32Random.class, new Json.Serializer<Respite32Random>() {
            @Override
            public void write(Json json, Respite32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Respite32Random read(Json json, JsonValue jsonData, Class type) {
                Respite32Random r = new Respite32Random(1, 1, 1);
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
    public static void registerStrangerRandom(@NonNull Json json) {
        if(json.getSerializer(StrangerRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("StrR", StrangerRandom.class);
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
    public static void registerXoshiro256StarStarRandom(@NonNull Json json) {
        if(json.getSerializer(Xoshiro256StarStarRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XSSR", Xoshiro256StarStarRandom.class);
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
     * Registers Xoroshiro128StarStarRandom with the given Json object, so Xoroshiro128StarStarRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerXoroshiro128StarStarRandom(@NonNull Json json) {
        if(json.getSerializer(Xoroshiro128StarStarRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XSSR", Xoroshiro128StarStarRandom.class);
        json.setSerializer(Xoroshiro128StarStarRandom.class, new Json.Serializer<Xoroshiro128StarStarRandom>() {
            @Override
            public void write(Json json, Xoroshiro128StarStarRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Xoroshiro128StarStarRandom read(Json json, JsonValue jsonData, Class type) {
                Xoroshiro128StarStarRandom r = new Xoroshiro128StarStarRandom(1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Xoshiro256MX3Random with the given Json object, so Xoshiro256MX3Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerXoshiro256MX3Random(@NonNull Json json) {
        if(json.getSerializer(Xoshiro256MX3Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("XMXR", Xoshiro256MX3Random.class);
        json.setSerializer(Xoshiro256MX3Random.class, new Json.Serializer<Xoshiro256MX3Random>() {
            @Override
            public void write(Json json, Xoshiro256MX3Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Xoshiro256MX3Random read(Json json, JsonValue jsonData, Class type) {
                Xoshiro256MX3Random r = new Xoshiro256MX3Random(1L, 1L, 1L, 1L);
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
    public static void registerTricycleRandom(@NonNull Json json) {
        if(json.getSerializer(TricycleRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("TriR", TricycleRandom.class);
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
    public static void registerRomuTrioRandom(@NonNull Json json) {
        if(json.getSerializer(RomuTrioRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("RTrR", RomuTrioRandom.class);
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
     * Registers SoloRandom with the given Json object, so SoloRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerSoloRandom(@NonNull Json json) {
        if(json.getSerializer(SoloRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("SolR", SoloRandom.class);
        json.setSerializer(SoloRandom.class, new Json.Serializer<SoloRandom>() {
            @Override
            public void write(Json json, SoloRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public SoloRandom read(Json json, JsonValue jsonData, Class type) {
                SoloRandom r = new SoloRandom(1L, 1L, 1L);
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
    public static void registerLaserRandom(@NonNull Json json) {
        if(json.getSerializer(LaserRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("LasR", LaserRandom.class);
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
    public static void registerMizuchiRandom(@NonNull Json json) {
        if(json.getSerializer(MizuchiRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("MizR", MizuchiRandom.class);
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
     * Registers PcgRXSMXSRandom with the given Json object, so PcgRXSMXSRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerPcgRXSMXSRandom(@NonNull Json json) {
        if(json.getSerializer(PcgRXSMXSRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("PRXR", PcgRXSMXSRandom.class);
        json.setSerializer(PcgRXSMXSRandom.class, new Json.Serializer<PcgRXSMXSRandom>() {
            @Override
            public void write(Json json, PcgRXSMXSRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public PcgRXSMXSRandom read(Json json, JsonValue jsonData, Class type) {
                PcgRXSMXSRandom r = new PcgRXSMXSRandom(1L, 1L);
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
    public static void registerDistinctRandom(@NonNull Json json) {
        if(json.getSerializer(DistinctRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("DisR", DistinctRandom.class);
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
     * Registers ScruffRandom with the given Json object, so ScruffRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerScruffRandom(@NonNull Json json) {
        if(json.getSerializer(ScruffRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("ScrR", ScruffRandom.class);
        json.setSerializer(ScruffRandom.class, new Json.Serializer<ScruffRandom>() {
            @Override
            public void write(Json json, ScruffRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ScruffRandom read(Json json, JsonValue jsonData, Class type) {
                ScruffRandom r = new ScruffRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers PouchRandom with the given Json object, so PouchRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerPouchRandom(@NonNull Json json) {
        if(json.getSerializer(PouchRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("PouR", PouchRandom.class);
        json.setSerializer(PouchRandom.class, new Json.Serializer<PouchRandom>() {
            @Override
            public void write(Json json, PouchRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public PouchRandom read(Json json, JsonValue jsonData, Class type) {
                PouchRandom r = new PouchRandom(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Sfc64Random with the given Json object, so Sfc64Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerSfc64Random(@NonNull Json json) {
        if(json.getSerializer(Sfc64Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("SfcR", Sfc64Random.class);
        json.setSerializer(Sfc64Random.class, new Json.Serializer<Sfc64Random>() {
            @Override
            public void write(Json json, Sfc64Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Sfc64Random read(Json json, JsonValue jsonData, Class type) {
                Sfc64Random r = new Sfc64Random(1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Crand64Random with the given Json object, so Crand64Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerCrand64Random(@NonNull Json json) {
        if(json.getSerializer(Crand64Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("CraR", Crand64Random.class);
        json.setSerializer(Crand64Random.class, new Json.Serializer<Crand64Random>() {
            @Override
            public void write(Json json, Crand64Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Crand64Random read(Json json, JsonValue jsonData, Class type) {
                Crand64Random r = new Crand64Random(1L, 1L, 1L, 1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers FlowRandom with the given Json object, so FlowRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerFlowRandom(@NonNull Json json) {
        if(json.getSerializer(FlowRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("FloR", FlowRandom.class);
        json.setSerializer(FlowRandom.class, new Json.Serializer<FlowRandom>() {
            @Override
            public void write(Json json, FlowRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public FlowRandom read(Json json, JsonValue jsonData, Class type) {
                FlowRandom r = new FlowRandom(1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers OrbitalRandom with the given Json object, so OrbitalRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerOrbitalRandom(@NonNull Json json) {
        if(json.getSerializer(OrbitalRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("OrbR", OrbitalRandom.class);
        json.setSerializer(OrbitalRandom.class, new Json.Serializer<OrbitalRandom>() {
            @Override
            public void write(Json json, OrbitalRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public OrbitalRandom read(Json json, JsonValue jsonData, Class type) {
                OrbitalRandom r = new OrbitalRandom(1L, 1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Taxon32Random with the given Json object, so Taxon32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTaxon32Random(@NonNull Json json) {
        if(json.getSerializer(Taxon32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("TxnR", Taxon32Random.class);
        json.setSerializer(Taxon32Random.class, new Json.Serializer<Taxon32Random>() {
            @Override
            public void write(Json json, Taxon32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Taxon32Random read(Json json, JsonValue jsonData, Class type) {
                Taxon32Random r = new Taxon32Random(1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Choo32Random with the given Json object, so Choo32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChoo32Random(@NonNull Json json) {
        if(json.getSerializer(Choo32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("ChoR", Choo32Random.class);
        json.setSerializer(Choo32Random.class, new Json.Serializer<Choo32Random>() {
            @Override
            public void write(Json json, Choo32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Choo32Random read(Json json, JsonValue jsonData, Class type) {
                Choo32Random r = new Choo32Random(1, 1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Chill32Random with the given Json object, so Chill32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerChill32Random(@NonNull Json json) {
        if(json.getSerializer(Chill32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("Ch3R", Chill32Random.class);
        json.setSerializer(Chill32Random.class, new Json.Serializer<Chill32Random>() {
            @Override
            public void write(Json json, Chill32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Chill32Random read(Json json, JsonValue jsonData, Class type) {
                Chill32Random r = new Chill32Random(1, 1, 1);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers Bear32Random with the given Json object, so Bear32Random can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerBear32Random(@NonNull Json json) {
        if(json.getSerializer(Bear32Random.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("BeaR", Bear32Random.class);
        json.setSerializer(Bear32Random.class, new Json.Serializer<Bear32Random>() {
            @Override
            public void write(Json json, Bear32Random object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public Bear32Random read(Json json, JsonValue jsonData, Class type) {
                Bear32Random r = new Bear32Random(1, 1, 1, 1);
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
    public static void registerGoldenQuasiRandom(@NonNull Json json) {
        if(json.getSerializer(GoldenQuasiRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("GoQR", GoldenQuasiRandom.class);
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
    public static void registerVanDerCorputQuasiRandom(@NonNull Json json) {
        if(json.getSerializer(VanDerCorputQuasiRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("VCQR", VanDerCorputQuasiRandom.class);
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
     * Registers LowChangeQuasiRandom with the given Json object, so LowChangeQuasiRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLowChangeQuasiRandom(@NonNull Json json) {
        if(json.getSerializer(LowChangeQuasiRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("VCQR", LowChangeQuasiRandom.class);
        json.setSerializer(LowChangeQuasiRandom.class, new Json.Serializer<LowChangeQuasiRandom>() {
            @Override
            public void write(Json json, LowChangeQuasiRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LowChangeQuasiRandom read(Json json, JsonValue jsonData, Class type) {
                LowChangeQuasiRandom r = new LowChangeQuasiRandom(1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers TupleQuasiRandom with the given Json object, so TupleQuasiRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerTupleQuasiRandom(@NonNull Json json) {
        if(json.getSerializer(TupleQuasiRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("TuQR", TupleQuasiRandom.class);
        json.setSerializer(TupleQuasiRandom.class, new Json.Serializer<TupleQuasiRandom>() {
            @Override
            public void write(Json json, TupleQuasiRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public TupleQuasiRandom read(Json json, JsonValue jsonData, Class type) {
                TupleQuasiRandom r = new TupleQuasiRandom(1L);
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers KnownSequenceRandom with the given Json object, so KnownSequenceRandom can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerKnownSequenceRandom(@NonNull Json json) {
        if(json.getSerializer(KnownSequenceRandom.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("KnSR", KnownSequenceRandom.class);
        json.setSerializer(KnownSequenceRandom.class, new Json.Serializer<KnownSequenceRandom>() {
            @Override
            public void write(Json json, KnownSequenceRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public KnownSequenceRandom read(Json json, JsonValue jsonData, Class type) {
                KnownSequenceRandom r = new KnownSequenceRandom();
                r.stringDeserialize(jsonData.asString(), BASE);
                return r;
            }
        });
    }

    /**
     * Registers LongSequence with the given Json object, so LongSequence can be written to and read from JSON.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerLongSequence(@NonNull Json json) {
        if(json.getSerializer(LongSequence.class) != null) return;
        if(ADD_CLASS_TAGS) json.addClassTag("LSeq", LongSequence.class);
        json.setSerializer(LongSequence.class, new Json.Serializer<LongSequence>() {
            @Override
            public void write(Json json, LongSequence object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public LongSequence read(Json json, JsonValue jsonData, Class type) {
                LongSequence s = new LongSequence();
                s.stringDeserialize(jsonData.asString(), BASE);
                return s;
            }
        });
    }

    /**
     * Registers ReverseWrapper with the given Json object, so ReverseWrapper can be written to and read from JSON.
     * This also registers all other EnhancedRandom types.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerReverseWrapper(@NonNull Json json) {
        if(json.getSerializer(ReverseWrapper.class) != null) return;
        JsonSupport.registerEnhancedRandom(json);
        if(ADD_CLASS_TAGS) json.addClassTag("RevW", ReverseWrapper.class);
        json.setSerializer(ReverseWrapper.class, new Json.Serializer<ReverseWrapper>() {
            @Override
            public void write(Json json, ReverseWrapper object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ReverseWrapper read(Json json, JsonValue jsonData, Class type) {
                ReverseWrapper w = new ReverseWrapper();
                w.stringDeserialize(jsonData.asString(), BASE);
                return w;
            }
        });
    }

    /**
     * Registers ArchivalWrapper with the given Json object, so ArchivalWrapper can be written to and read from JSON.
     * This also registers all other EnhancedRandom types, and {@link LongSequence}.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerArchivalWrapper(@NonNull Json json) {
        if(json.getSerializer(ArchivalWrapper.class) != null) return;
        JsonSupport.registerEnhancedRandom(json);
        JsonSupport.registerLongSequence(json);
        if(ADD_CLASS_TAGS) json.addClassTag("ArcW", ArchivalWrapper.class);
        json.setSerializer(ArchivalWrapper.class, new Json.Serializer<ArchivalWrapper>() {
            @Override
            public void write(Json json, ArchivalWrapper object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public ArchivalWrapper read(Json json, JsonValue jsonData, Class type) {
                ArchivalWrapper w = new ArchivalWrapper();
                w.stringDeserialize(jsonData.asString(), BASE);
                return w;
            }
        });
    }

    /**
     * Registers DistributedRandom with the given Json object, so DistributedRandom can be written to and read from JSON.
     * This also registers all other EnhancedRandom types and all Distribution types.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerDistributedRandom(@NonNull Json json) {
        if(json.getSerializer(DistributedRandom.class) != null) return;
        JsonSupport.registerEnhancedRandom(json);
        JsonSupport.registerDistribution(json);
        if(ADD_CLASS_TAGS) json.addClassTag("DsrR", DistributedRandom.class);
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
     * Registers InterpolatedRandom with the given Json object, so InterpolatedRandom can be written to and read from JSON.
     * This also registers all other EnhancedRandom types and all Interpolator types.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerInterpolatedRandom(@NonNull Json json) {
        if(json.getSerializer(InterpolatedRandom.class) != null) return;
        JsonSupport.registerEnhancedRandom(json);
        JsonSupport.registerInterpolator(json);
        if(ADD_CLASS_TAGS) json.addClassTag("InrR", InterpolatedRandom.class);
        json.setSerializer(InterpolatedRandom.class, new Json.Serializer<InterpolatedRandom>() {
            @Override
            public void write(Json json, InterpolatedRandom object, Class knownType) {
                json.writeValue(object.stringSerialize(BASE));
            }

            @Override
            public InterpolatedRandom read(Json json, JsonValue jsonData, Class type) {
                return new InterpolatedRandom().stringDeserialize(jsonData.asString(), BASE);
            }
        });
    }

    /**
     * Registers EnhancedRandom with the given Json object, so EnhancedRandom can be written to and read from JSON.
     * This also registers {@link DistinctRandom}, {@link LaserRandom}, {@link TricycleRandom}, {@link FourWheelRandom},
     * {@link Xoshiro256StarStarRandom}, {@link Xoshiro128PlusPlusRandom}, {@link Xoshiro256MX3Random},
     * {@link Xoroshiro128StarStarRandom}, {@link StrangerRandom}, {@link TrimRandom}, {@link WhiskerRandom},
     * {@link RomuTrioRandom}, {@link ChopRandom}, {@link Xoshiro128PlusPlusRandom}, {@link MizuchiRandom},
     * {@link ScruffRandom}, {@link AceRandom}, {@link FlowRandom}, {@link Taxon32Random}, {@link Choo32Random},
     * {@link GoldenQuasiRandom}, {@link VanDerCorputQuasiRandom},
     * {@link LowChangeQuasiRandom}, {@link TupleQuasiRandom}, and {@link KnownSequenceRandom}, plus
     * {@link AtomicLong} because some subclasses of {@link java.util.Random} need it. This does not register
     * {@link DistributedRandom} or the wrappers {@link ReverseWrapper} and {@link ArchivalWrapper}, but
     * {@link #registerDistributedRandom(Json)}, {@link #registerReverseWrapper(Json)}, and
     * {@link #registerArchivalWrapper(Json)} call this method instead (the first of those also calls
     * {@link #registerDistribution(Json)}).
     * <br>
     * Abstract classes aren't usually serializable like this, but because each of the EnhancedRandom serializers uses a
     * specific format shared with what this uses, and that format identifies which class is used, it works here.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerEnhancedRandom(@NonNull Json json) {
        if(json.getSerializer(EnhancedRandom.class) != null) return;
        registerAtomicLong(json);
        registerAceRandom(json);
        registerBear32Random(json);
        registerChill32Random(json);
        registerChoo32Random(json);
        registerChopRandom(json);
        registerCrand64Random(json);
        registerDistinctRandom(json);
        registerFlowRandom(json);
        registerFourWheelRandom(json);
        registerGoldenQuasiRandom(json);
        registerJsf32Random(json);
        registerKnownSequenceRandom(json);
        registerLaserRandom(json);
        registerLowChangeQuasiRandom(json);
        registerMizuchiRandom(json);
        registerPasarRandom(json);
        registerPcgRXSMXSRandom(json);
        registerPouchRandom(json);
        registerRespite32Random(json);
        registerRomuTrioRandom(json);
        registerScruffRandom(json);
        registerSfc64Random(json);
        registerSoloRandom(json);
        registerStrangerRandom(json);
        registerTaxon32Random(json);
        registerTricycleRandom(json);
        registerTrimRandom(json);
        registerTupleQuasiRandom(json);
        registerVanDerCorputQuasiRandom(json);
        registerWhiskerRandom(json);
        registerXoroshiro128StarStarRandom(json);
        registerXoshiro128PlusPlusRandom(json);
        registerXoshiro256MX3Random(json);
        registerXoshiro256StarStarRandom(json);
        if(ADD_CLASS_TAGS) json.addClassTag("EnhR", EnhancedRandom.class);
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
                    Gdx.app.error("Json Read Exception (EnhancedRandom)", e.toString());
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
    public static void registerRandomXS128(@NonNull Json json) {
        if(json.getSerializer(RandomXS128.class) != null) return;
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
//    public static void registerRandomXS128(@NonNull Json json) {
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
    public static void registerArcsineDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Arcsine", ArcsineDistribution.class);
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
    public static void registerBernoulliDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Bernoulli", BernoulliDistribution.class);
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
    public static void registerBetaDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Beta", BetaDistribution.class);
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
    public static void registerBetaPrimeDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("BetaPrime", BetaPrimeDistribution.class);
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
    public static void registerBinomialDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Binomial", BinomialDistribution.class);
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
    public static void registerCauchyDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Cauchy", CauchyDistribution.class);
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
    public static void registerChiDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Chi", ChiDistribution.class);
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
    public static void registerChiSquareDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ChiSquare", ChiSquareDistribution.class);
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
    public static void registerContinuousUniformDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("ContinuousUniform", ContinuousUniformDistribution.class);
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
    public static void registerDiscreteUniformDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("DiscreteUniform", DiscreteUniformDistribution.class);
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
    public static void registerErlangDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Erlang", ErlangDistribution.class);
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
    public static void registerExponentialDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Exponential", ExponentialDistribution.class);
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
    public static void registerFisherSnedecorDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("FisherSnedecor", FisherSnedecorDistribution.class);
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
    public static void registerFisherTippettDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("FisherTippett", FisherTippettDistribution.class);
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
    public static void registerGammaDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Gamma", GammaDistribution.class);
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
    public static void registerGeometricDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Geometric", GeometricDistribution.class);
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
    public static void registerKumaraswamyDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Kumaraswamy", KumaraswamyDistribution.class);
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
    public static void registerLaplaceDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Laplace", LaplaceDistribution.class);
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
    public static void registerLogCauchyDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("LogCauchy", LogCauchyDistribution.class);
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
    public static void registerLogisticDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Logistic", LogisticDistribution.class);
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
    public static void registerLogNormalDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("LogNormal", LogNormalDistribution.class);
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
    public static void registerLumpDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Lump", LumpDistribution.class);
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
    public static void registerNormalDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Normal", NormalDistribution.class);
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
    public static void registerParetoDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Pareto", ParetoDistribution.class);
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
    public static void registerPoissonDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Poisson", PoissonDistribution.class);
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
    public static void registerPowerDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Power", PowerDistribution.class);
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
    public static void registerRayleighDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Rayleigh", RayleighDistribution.class);
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
    public static void registerStudentsTDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("StudentsT", StudentsTDistribution.class);
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
    public static void registerTriangularDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Triangular", TriangularDistribution.class);
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
    public static void registerWeibullDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Weibull", WeibullDistribution.class);
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
    public static void registerZipfianDistribution(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Zipfian", ZipfianDistribution.class);
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
    public static void registerDistribution(@NonNull Json json) {
        if(json.getSerializer(Distribution.class) != null) return;
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

        if(ADD_CLASS_TAGS) json.addClassTag("Dist", Distribution.class);

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
                    Gdx.app.error("Json Read Exception (Distribution)", e.toString());
                    return null;
                }
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
    public static void registerBase(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Base", Base.class);
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
    public static void registerHasher(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("Hshr", Hasher.class);

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
     * Registers Interpolator with the given Json object, so Interpolator can be written to and read from JSON.
     * This is a simple wrapper around each Interpolator's unique {@link Interpolator#getTag()} and
     * {@link Interpolations#get(String)} to read it back.
     *
     * @param json a libGDX Json object that will have a serializer registered
     */
    public static void registerInterpolator(@NonNull Json json) {
        json.addClassTag("Inlr", Interpolator.class);
        json.setSerializer(Interpolator.class, new Json.Serializer<Interpolator>() {
            @Override
            public void write(Json json, Interpolator object, Class knownType) {
                json.writeValue(object.getTag());
            }

            @Override
            public Interpolator read(Json json, JsonValue jsonData, Class type) {
                if (jsonData == null || jsonData.isNull()) return null;
                return Interpolations.get(jsonData.asString());
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
    public static void registerClass(@NonNull Json json) {
        if(ADD_CLASS_TAGS) json.addClassTag("C", Class.class); // just Class type
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
                    } catch (ReflectionException e) {
                        Gdx.app.error("Json Read Exception (Class)", e.toString());
                        return null;
                    }
                }
                return null;
            }
        });

    }

}
