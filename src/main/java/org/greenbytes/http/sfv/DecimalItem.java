package org.greenbytes.http.sfv;

import java.math.BigDecimal;

public class DecimalItem implements Item {

    private final BigDecimal value;

    public DecimalItem(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String serialize() {
        return value.toPlainString();
    }
}
