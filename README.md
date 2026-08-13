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
- https://reschke.github.io/structured-fields/apidocs/
- https://reschke.github.io/structured-fields/coverage/

## Examples (as shown in RFC9651)

See https://github.com/reschke/structured-fields/blob/main/src/test/java/org/greenbytes/http/sfv/RFC9651ExamplesTest.java
for a lot of tests.

Parsing (https://www.rfc-editor.org/info/rfc9651/#example):

````java
private static Foo parseAndValidateExample(String serialization, URI baseUri) {
    Item<?> item = Parser.parseItem(serialization);

    long amountOfFoo = item.longValue();
    if (amountOfFoo < 0 || amountOfFoo > 10) {
        throw new IllegalArgumentException("invalid amountOfFoo (was " + amountOfFoo + ")");
    }

    Item<?> fooURLParam = item.params().get("foourl");

    URI url = null;
    if (fooURLParam != null) {
        url = URI.create(fooURLParam.stringValue());
        if (! url.isAbsolute()) {
            url = baseUri.resolve(url);
        }
    }

    Foo foo = new Foo();
    foo.amount = (int) amountOfFoo;
    foo.url = url;
    return foo;
}
````
Serializing (https://www.rfc-editor.org/info/rfc9651/#section-3.2-14):
~~~
Example-Dict: a=(1 2), b=3, c=4;aa=bb, d=(5 6);valid
~~~~
````java
private static Dictionary createDictionaryMix2() {
    return Dictionary.valueOf("a", InnerList.valueOf(1, 2),
            "b", 3,
            "c", IntegerItem.of(4).
                    withParamValuesOf("aa", TokenItem.of("bb")),
            "d", InnerList.valueOf(5, 6).
                    withParamValuesOf("valid", true));
}
````


## Testing Client

Here's a command line tool which will feed all arguments into the parser (as if obtained
from multiple field lines), parsed as Item, List, or Dictionary, and return diagnostic
information.

```
$ java -jar target/structured-fields-0.6-SNAPSHOT.jar 'date;v=@1' 'number;v=123' '( token );bool'

Item: >>date;v=@1,number;v=123,( token );bool<<
        ---------^ (0x2c) Extra characters in string parsed as Item

List: date;v=@1, number;v=123, (token);bool (OuterList)
  date;v=@1, number;v=123, (token);bool (OuterList)
    date (TokenItem)
      ;v=@1 (Parameters)
        v -> @1 (DateItem)
    number (TokenItem)
      ;v=123 (Parameters)
        v -> 123 (IntegerItem)
    (token) (InnerList)
      token (TokenItem)
      ;bool (Parameters)
        bool -> ?1 (BooleanItem)


Dict: >>date;v=@1,number;v=123,( token );bool<<
        -----------------------^ (0x28) Key must start with LCALPHA or '*': '(' (\u0028)
```


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

