package org.greenbytes.http.sfv;

import java.util.function.Supplier;

public interface Item<T> extends Supplier<T> {

    public Item<T> withParams(Parameters params);

    public StringBuilder serializeTo(StringBuilder sb);

    public String serialize();
}
