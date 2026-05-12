package org.greenbytes.http.sfv;

/**
 * Types of Structured Data
 */
public enum SfDataType {
    // RFC 9651, Section 3.1
    LIST,
    // RFC 9651, Section 3.1.1
    INNERLIST,
    // RFC 9651, Section 3.2
    DICTIONARY,
    // RFC 9651, Section 3.3
    BOOLEAN,
    BYTESEQUENCE,
    DATE,
    DECIMAL,
    DISPLAYSTRING,
    INTEGER,
    STRING,
    TOKEN
}
