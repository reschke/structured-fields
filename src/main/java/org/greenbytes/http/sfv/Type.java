package org.greenbytes.http.sfv;

import java.util.function.Supplier;

public interface Type<T> extends Supplier<T> {

    public StringBuilder serializeTo(StringBuilder sb);

    public String serialize();
}
