package com.github.romanqed.nem.gui;

import org.xml.sax.Attributes;

import java.util.Map;

final class MapAttributes implements Attributes {
    private final Map<String, String> body;

    MapAttributes(Map<String, String> body) {
        this.body = body;
    }

    @Override
    public int getLength() {
        return body.size();
    }

    @Override
    public String getURI(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLocalName(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQName(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIndex(String uri, String localName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIndex(String qName) {
        return body.containsKey(qName) ? 0 : -1;
    }

    @Override
    public String getType(String uri, String localName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(String qName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(String uri, String localName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(String qName) {
        return body.get(qName);
    }
}
