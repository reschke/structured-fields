package org.greenbytes.http.sfv;

public interface Item<T> extends Type<T> {

    public Item<T> withParams(Parameters params);

    public Parameters getParams();
}
