package com.delarosa.huffmancode;

import java.io.Serializable;

public class HuffmanDto implements Serializable {
    public String symbol;
    public String code;

    public HuffmanDto() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
