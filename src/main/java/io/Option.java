package io;

import java.net.SocketOption;

public class Option implements SocketOption {

    public Option(String n, Class t){
        this.n = n;
        this.t = t;
    }

    String n;
    Class t;

    @Override
    public String name() {
        return this.n;
    }

    @Override
    public Class type() {
        return this.t;
    }

}
