package com.test.time.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class InnerObject {
    @Protobuf
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
