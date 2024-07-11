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
or one at a time using any of its other methods.

The Json serialization also uses an especially-concise format to store each of the `EnhancedRandom` implementations in
[juniper](https://github.com/tommyettinger/juniper). Even though juniper is not a direct dependency of jdkgdxds, it used
to be part of that library, and its use is recommended with any of the randomized methods in jdkgdxds, so it still makes
sense to have here. These classes (`DistinctRandom`, `LaserRandom`, `TricycleRandom`, uhh... there's a lot in the
`com.github.tommyettinger.random` package) are sometimes serializable
without jdkgdxds-interop, but work regardless of JDK version if
you do use this library. Better still, you can register `EnhancedRandom` for serialization, so places that have an
`EnhancedRandom` but don't specify an implementation can still store one (which includes its implementing class) and
read an `EnhancedRandom` back. If you have your own class that extends `java.util.Random`, which is admittedly unlikely,
you should probably write your own serializer modeled after the serializer for the `RandomXS128` class in libGDX here.
Java 17 and higher block libGDX's `Json` class from accessing the state of `java.util.Random`, which also prevents any
serialization of subclasses unless they use custom serialization. This also means that `java.util.Random` can't be
serialized or deserialized by libGDX `Json` on JDK 17 or higher, even with a custom serializer, unless some special
additional work happens (and even that might not work on future JDKs). `digital`'s `AlternateRandom` class is also now
serializable here; its algorithm is the same as `PasarRandom` from juniper, but it doesn't have as many features, so
simply using `PasarRandom` is preferable if you depend on juniper anyway. 

Starting with juniper 0.1.0, it now contains quite a few statistical distributions, each of which stores some parameter
or parameters and an EnhancedRandom to generate numbers. These can all be registered individually or as a group by
`JsonSupport.registerDistribution()`. Once registered, you can store a `Distribution` much like an `EnhancedRandom`, and
it uses a similar compact storage system. You can also store a `Distribution` and deserialize it as a `Distribution`,
allowing any implementation to be fit into that variable.

The [digital](https://github.com/tommyettinger/digital) library is a direct dependency of jdkgdxds, and it has the
`Hasher`, `Interpolator`, and `AlternateRandom` classes that can be registered, as well as a `Base` class. `Base` is
especially important here because you can
configure the numeral base that numbers are printed in by specifying one to `JsonSupport.setNumeralBase(Base)`; this can
be handy to weakly obfuscate numbers if you pass a scrambled base (as `Base` can generate).

The Json flavor in libGDX allows specifying class tags as a kind of shorter alias for a package-qualified class name. As
of jdkgdxds-interop 1.1.1.1, when registering any class, this will add a very short class tag unless you had previously
called `JsonSupport.setAddClassTags(false);`. The class tags can be hard to read if you want human-readable JSON files,
but long packages add to file size and can also be strenuous to read repeatedly. Class tags are added by default.

## How do I get it?
The Gradle dependency, with the usual caveats about optionally replacing `implementation` with `api`, is: 
```groovy
implementation "com.github.tommyettinger:jdkgdxds_interop:1.6.2.0"
```
It's not unlikely that you might need `api` instead of `implementation`, especially if you are writing a library, or a
module that needs to be used from another section.

If you use GWT (libGDX's HTML target), then you also need this in your `html/build.gradle` file:
```groovy
implementation "com.github.tommyettinger:funderby:0.1.2:sources"
implementation "com.github.tommyettinger:digital:0.4.8:sources"
implementation "com.github.tommyettinger:juniper:0.6.1:sources"
implementation "com.github.tommyettinger:jdkgdxds:1.6.2:sources"
implementation "com.github.tommyettinger:jdkgdxds_interop:1.6.2.0:sources"
```
You also need the GWT `inherits` in your `GdxDefinition.gwt.xml` file:
```xml
    <inherits name="com.badlogic.gdx.backends.gdx_backends_gwt" />
    <inherits name="com.github.tommyettinger.funderby" />
    <inherits name="com.github.tommyettinger.digital" />
    <inherits name="com.github.tommyettinger.juniper" />
    <inherits name="com.github.tommyettinger.jdkgdxds" />
    <inherits name="com.github.tommyettinger.jdkgdxds_interop" />
```

I hope that's all you need! It's a small-ish simple library!
