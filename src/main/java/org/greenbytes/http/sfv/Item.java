package org.greenbytes.http.sfv;

public interface Item {

    public StringBuilder appendTo(StringBuilder sb);

    public String serialize();
}
