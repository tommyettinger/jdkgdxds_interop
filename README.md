# jdkgdxds_interop
Inter-operate between libGDX and jdkgdxds

## We have libGDX already.
There's several collection types in [libGDX](https://libgdx.com/) that have no convenient way to interoperate with the
JDK interfaces. `ObjectMap` doesn't implement `Map`, `Array` doesn't implement `List`, and in general nothing more than
`Iterable` is available. But, libGDX also provides its `Json` class and related code, which is a nice way of serializing
data so that all platforms can both read and write it.

## And then there's jdkgdxds.
The new [jdkgdxds](https://github.com/tommyettinger/jdkgdxds) library takes the JDK interfaces like `Map` and `List`,
applies them to data structures closely derived from the libGDX ones, and then keeps going to extend the data structure
coverage to things like insertion-ordered long-to-float maps and case-insensitive sets of CharSequence items. It does
not have a way to serialize to JSON out-of-the-box, though things that can serialize the JDK interfaces can at least
write and read the all-Object types like `ObjectList`, `ObjectObjectMap` and `ObjectOrderedSet`.

## So what do we have here?
Here's jdkgdxds_interop, for conversions both ways between jdkgdxds and libGDX data structures, and also to write the
jdkgdxds data structures using libGDX's Json. This consists of just three classes, all in the
`com.github.tommyettinger.ds.interop` package. `ConversionToGDX` has methods that take any class that implements one of
the JDK or one of jdkgdxds' interfaces, like `Collection` or `PrimitiveCollection.OfLong`, and converts it to a narrower
libGDX data structure. `ConversionToJDK` has methods that take a specific libGDX class, typically, and convert it to a
similar jdkgdxds data structure. `JsonSupport` is probably the star of the show, and allows registering serializers on a
`Json` object, so it can read and write jdkgdxds types. This registration could be all at once, using `registerAll()`,
or one at a time using any of its other methods. The Json serialization also uses an especially-concise format to store
each of the `EnhancedRandom` implementations in jdkgdxds. These four classes (`DistinctRandom`, `LaserRandom`,
`TricycleRandom`, and `FourWheelRandom`) are sometimes serializable without jdkgdxds-interop, but work regardless of JDK
version if you do use this library. Better still, you can register `EnhancedRandom` for serialization, so places that
have an `EnhancedRandom` but don't specify an implementation can still store one (which includes its implementing class)
and read an `EnhancedRandom` back. If you have your own class that extends `java.util.Random`, then you probably want
to register `AtomicLong` (which JsonSupport can do) or to write your own serializer.

## How do I get it?
The Gradle dependency, with the usual caveats about optionally replacing `implementation` with `api`, is: 
```groovy
implementation "com.github.tommyettinger:jdkgdxds_interop:0.1.4.2"
```
It's not unlikely that you might need `api` instead of `implementation`, especially if you are writing a library, or a
module that needs to be used from another section.

If you use GWT (libGDX's HTML target), then you also need this in your `html/build.gradle` file:
```groovy
implementation "com.github.tommyettinger:jdkgdxds_interop:0.1.4.2:sources"
```
You also need the GWT `inherits` in your `GdxDefinition.gwt.xml` file:
```xml
     <inherits name="jdkgdxds_interop" />
```

I hope that's all you need! It's a small-ish simple library!
