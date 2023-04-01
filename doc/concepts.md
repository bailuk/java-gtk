# Types

## Pointer

`Pointer` is the base class for all Generated Types (GObject Classes, GObject Records and GObject Interfaces) and
the base class for Wrapper Types (Int, Byte, Str ...)

`Pointer` holds a `PointerContainer` and `PointerContainer` holds a `long` that contains a c/native pointer.
Any java-gtk type is just a c-pointer with an API.

`Pointer` and `PointerContainer` implement `PointerInterface`. `PointerInterface` provides casting functions
to cast between different Pointer types: `cast()`, `asPointer()`, `asCPointer()`, `asJnaPointer()`

`Pointer` is also derived from `Type`, which offers static variants of this type casting functions.

See 'Interfaces' for an example

## Casting

`cast()` returns a `PointerContainer`, any sub class of `Pointer` offers a constructor taking a `PointerContainer` as
parameter.

This allows for casting between java-gtk types.

Of course type casting should only be applied if both types are compatible, like for accessing the implementing class
of an interface. The static `cast()` function (of `Type`) can be used to get a `PointerContainer` from other pointer representation (c-pointer, JnaPointer).

See 'Interfaces' for an example

## Interfaces

GObject interfaces are represented by separate classes. Generated GObject Classes offer `asInterfaceName()` functions to 
access their interface APIs.

In this Example widget, buildable und button reference the same object:

```java
// access Buildable interface of Button (Widget)
Widget widget = new Button();

Buildable buildable = widget.asBuildable();
int id = buildable.getBuildableId() // access interface API

// cast back to button
Button button = new Button(buildable.cast());
button.setLabel("ID: " + id);
```
