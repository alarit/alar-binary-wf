package com.github.alarit.model;

public abstract class InnerVertex implements Vertex {

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void perform() {
        return;
    }

}
