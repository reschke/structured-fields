package org.greenbytes.http.sfv;

import java.util.function.Supplier;

public interface Item<T> extends Supplier<T> {

    public StringBuilder appendTo(StringBuilder sb);

    public String serialize();
}
