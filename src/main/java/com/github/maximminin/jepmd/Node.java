/*
* %CopyrightBegin%
*
* Copyright Maxim Minin. All Rights Reserved.
*
* The contents of this file are subject to the Erlang Public License,
* Version 1.1, (the "License"); you may not use this file except in
* compliance with the License. You should have received a copy of the
* Erlang Public License along with this software. If not, it can be
* retrieved online at http://www.erlang.org/.
*
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
* the License for the specific language governing rights and limitations
* under the License.
*
* %CopyrightEnd%
*/
package com.github.maximminin.jepmd;

import java.net.Socket;

public class Node {
    
    private String name;
    private int port;
    private int ntype;
    private int proto;
    private int distHigh;
    private int distLow;
    private int creation;
    private Socket socket;
    

    public Node(String name, int port, int ntype, int proto, int distHigh, int distLow, int creation, Socket socket) {
        super();
        this.name = name;
        this.port = port;
        this.ntype = ntype;
        this.proto = proto;
        this.distHigh = distHigh;
        this.distLow = distLow;
        this.creation = creation;
        this.socket = socket;
    }

    
    public Node(String name, int port) {
        super();
        this.name = name;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public int getNtype() {
        return ntype;
    }

    public int getProto() {
        return proto;
    }

    public int getDistHigh() {
        return distHigh;
    }

    public int getDistLow() {
        return distLow;
    }

    public int getCreation() {
        return creation;
    }

    public Socket getSocket() {
        return socket;
    }

}
