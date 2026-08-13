package org.greenbytes.http.sfv;

/**
 * Types of Structured Data.
 */
public enum SfDataType {
    /**
     * RFC 9651, Section 3.1
     */
    LIST,
    /**
     * RFC 9651, Section 3.1.1
     */
    INNERLIST,
    /**
     * RFC 9651, Section 3.2
     */
    DICTIONARY,
    /**
     * RFC 9651, Section 3.3.6
     */
    BOOLEAN,
    /**
     * RFC 9651, Section 3.3.5
     */
    BYTESEQUENCE,
     /**
     * RFC 9651, Section 3.3.7
     */
    DATE,
    /**
     * RFC 9651, Section 3.3.2
     */
    DECIMAL,
    /**
     * RFC 9651, Section 3.3.8
     */
    DISPLAYSTRING,
    /**
     * RFC 9651, Section 3.3.1
     */
    INTEGER,
    /**
     * RFC 9651, Section 3.3.3
     */
    STRING,
    /**
     * RFC 9651, Section 3.3.4
     */
    TOKEN
}
