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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Nodes {
    
    Map<String, Node> nodes =new HashMap<String, Node>();
    
    public void addNode(String name, Node node){
        nodes.put(name, node);
    }
    public Node getNode(String name){
        return nodes.get(name);
    }
    public void deleteNode(String name){
        nodes.remove(name);
    }
    public Collection<Node> getNodes(){
        return nodes.values();
    }

}
