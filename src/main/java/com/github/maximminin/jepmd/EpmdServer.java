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

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/** 
 * java version of epmd (erlang portmapper daemon)
 * 
 * @author Maxim Minin
 *
 */
public class EpmdServer extends Thread {

    public static int ALIVE_REQUEST_ID = 120;
    public static int ALIVE_RESPONSE_ID = 121;
    public static int PORT_REQUEST_ID = 122;
    public static int PORT_RESPONSE_ID = 119;
    public static int NAMES_REQUEST_ID = 110;
    public static int DUMP_REQUEST_ID = 100;
    public static int KILL_REQUEST_ID = 107;
    public static int STOP_REQUEST_ID = 115;

    public static int EPMD_PORT = 4369;

    private static EpmdServer epmdServer;

    /**
     *  start epmd listener on default port (4369).
     *  
     */
    public static void startEpmd() {
        startEpmd(EPMD_PORT);
    }

    /**
     *  start epmd listener on port
     * @param port - only needed when the default port is not desired
     */
    public static void startEpmd(int port) {
        if (epmdServer == null) {
            epmdServer = new EpmdServer(port);
            epmdServer.start();
        }
    }

    /**
     * stop epmd listener
     */
    public static void stopEpmd() {
        if (epmdServer != null) {
            try {
                for(Node node: epmdServer.nodes.getNodes()){
                    node.getSocket().close();
                }
                epmdServer.interrupt();
                epmdServer.echod.close();
                epmdServer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Nodes nodes = new Nodes();
    private int cnt = 0;
    private int port = EPMD_PORT;
    private ServerSocket echod;

    private EpmdServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            echod = new ServerSocket(port);
            while (!isInterrupted()) {
                Socket socket = echod.accept();
                ClientThread ct = new ClientThread(++cnt, socket, nodes, port);
                ct.start();
            }
        }catch(BindException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            e.printStackTrace();
        }

    }
    
}
