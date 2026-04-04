# structured-fields
A parser for the HTTP Structured Fields syntax defined in IETF RFC 9651.

## Features
- Parsing and Serialization
- (Still) supports Java 8
- Zero dependencies (except for tests)
- Comprehensive parsing diagnostics

## Links

- https://greenbytes.de/tech/specs/rfc9651.html
- https://www.rfc-editor.org/rfc/rfc9651.html
- https://github.com/httpwg/structured-header-tests
- API docs: https://reschke.github.io/structured-fields/apidocs/

## Minimal Example

````java
 {
     Parser p = new Parser("a=?0, b, c; foo=bar");
     Dictionary d = p.parseDictionary();
     for (Map.Entry<String, Item<? extends Object>> e : d.get()) {
         String key = e.getKey();
         Item<? extends Object> item = e.getValue();
         Object value = item.get();
         Parameters params = item.getParams();
         System.out.println(key + " -> " + value + (params.isEmpty() ? "" : (" (" + params.serialize() + ")")));
     }
 }
````
gives:
~~~
 a -> false
 b -> true
 c -> true (;foo=bar)
 ~~~

## Status

This implementation is experimental and makes no promises yet on API stability
(feedback on what might be missing is appreciated).

In the mid-term, this code might transition to the Apache HTTP Components project.

## Maven Coordinates

    <dependency>
      <groupId>org.greenbytes.http</groupId>
      <artifactId>structured-fields</artifactId>
      <version>0.5</version>
    </dependency>


![Java CI with Maven](https://github.com/reschke/structured-fields/workflows/Java%20CI%20with%20Maven/badge.svg)
[![MvnRepository](https://badges.mvnrepository.com/badge/org.greenbytes.http/structured-fields/badge.svg?label=MvnRepository)](https://mvnrepository.com/artifact/org.greenbytes.http/structured-fields)
[![javadoc](https://javadoc.io/badge2/org.greenbytes.http/structured-fields/javadoc.svg)](https://javadoc.io/doc/org.greenbytes.http/structured-fields) 
