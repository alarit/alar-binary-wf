package com.github.alarit.model;

import com.github.alarit.enums.WfStatus;

public interface Vertex {

    boolean isLeaf();
    String getName();
    WfStatus test();
    void perform();
}
