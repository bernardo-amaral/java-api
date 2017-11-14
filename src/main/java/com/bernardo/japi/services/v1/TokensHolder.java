package com.bernardo.japi.services.v1;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tokens")

public class TokensHolder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "id")
    private List<Token> tokens;

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }


}
