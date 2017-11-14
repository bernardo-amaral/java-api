package com.bernardo.japi;

import java.util.List;

import org.apache.log4j.Logger;

import com.bernardo.japi.services.v1.Token;

public class TokensBusinessManager {

    private static final Logger log = Logger.getLogger(TokensBusinessManager.class.getName());
    private static TokensBusinessManager INSTANCE = new TokensBusinessManager();

    public static TokensBusinessManager getInstance() {
        return INSTANCE;
    }

    private TokensBusinessManager() {

    }

    public Token findToken(String tokenId) throws Exception {
        log.info("TokensBusinessManager::findToken started");

        Token token = TokensDataManager.getInstance().findTokenById(tokenId);

        if (token == null) {
            throw new Exception("Nothing found");
        }

        return token;
    }

    public List<Token> findTokens() {
        return TokensDataManager.getInstance().findAllTokens();
    }

    public Token addToken(Token token) {
        Token newtoken = TokensDataManager.getInstance().insertToken(token);
        return newtoken;
    }

    public Token updateTokenAttribute(String tokenId, String attribute, String value) {

        return TokensDataManager.getInstance().updateTokenAttribute(tokenId, attribute, value);
    }

    public boolean deleteToken(String tokenId) {
        return TokensDataManager.getInstance().delete(tokenId);
    }

}
