package com.github.alarit.model;

import com.github.alarit.enums.WfStatus;

public abstract class OuterVertex implements Vertex {

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public WfStatus test() {
        return null;
    }

}
