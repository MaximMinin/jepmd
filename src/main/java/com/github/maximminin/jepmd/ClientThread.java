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
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ericsson.otp.erlang.OtpInputStream;
import com.ericsson.otp.erlang.OtpOutputStream;


public class ClientThread extends Thread{
	private int id;
	private Socket socket;
	private Nodes nodes;
	private int epmdPort;
	
	public ClientThread(int id, Socket socket, Nodes nodes, int epmdPort) {
		super();
		this.id = id;
		this.socket = socket;
		this.nodes = nodes;
		this.epmdPort=epmdPort;
	}
	
	public void run(){
		try {
		    byte[] tmpbuf = new byte[272];
		    int n = socket.getInputStream().read(tmpbuf);
		    if (n < 0) {
		        socket.close();
		    }
		    OtpInputStream ibuf = new OtpInputStream(tmpbuf, 0);
		    int length = ibuf.read2BE();
		    int response = ibuf.read1();
		    if (response == EpmdServer.ALIVE_REQUEST_ID) {
		            int port = ibuf.read2BE();
		            int ntype = ibuf.read1();
		            int proto = ibuf.read1();
		            int distHigh = ibuf.read2BE();
		            int distLow = ibuf.read2BE();
		            int nameLength = ibuf.read2BE();
		            int count = nameLength;
		            String name ="";
		            while (count > 0){
		                count--;
		                name=name+((char)ibuf.read1());
		            }
		            int extraLength = ibuf.read2BE();
		            int[] extras = new int[extraLength];
		            for(int i = 0; i<extraLength;i++){
		                extras[i]=ibuf.read1();
		            }
		            if(nodes.getNode(name)==null){
		                log("New Node: "+name+" on "+port);
		                nodes.addNode(name, new Node(name, port, ntype, proto, distHigh, distLow, id, socket));
		                sendAlifeResponse(0, id);
		                while(socket.getInputStream().read()!=-1){
		                    //TODO: ???
		                }
		                nodes.deleteNode(name);
		                log("Node: "+name+ " disconnected");
		                socket.close();
		            }else{
                        sendAlifeResponse(1, id);
                        socket.close();
		            }
		    }else if(response==EpmdServer.PORT_REQUEST_ID){
		        String name="";
		        int count= length-1;
		        while(count>0){
		            name=name+(char)ibuf.read1();
		            count--;
		        }
                sendNodeInfo( nodes.getNode(name));
                socket.close();
            }else if(response == EpmdServer.NAMES_REQUEST_ID){
                sendNodesInfo();
                socket.close();
            }else if (response == EpmdServer.KILL_REQUEST_ID || 
                      response == EpmdServer.STOP_REQUEST_ID){
                int legthObBody = ibuf.read4BE();
                int count=legthObBody;
                while (count>0){
                    ibuf.read1();
                }
                EpmdServer.stopEpmd();
            }else{
                log("Unknown request is ignored");
                socket.close();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    	
	private void sendAlifeResponse(int result, int creation)throws IOException{
        OtpOutputStream obuf = new OtpOutputStream();
        obuf.write1(EpmdServer.ALIVE_RESPONSE_ID);
        obuf.write1(result);
        obuf.write2BE(1);
        obuf.writeTo(socket.getOutputStream());
	}
	
	private void sendNodeInfo(Node node)throws IOException{
	    OtpOutputStream obuf = new OtpOutputStream();
	    if(node != null){
	        obuf.write1(EpmdServer.PORT_RESPONSE_ID);
	        obuf.write1(0L);
	        obuf.write2BE(node.getPort());
	        obuf.write1(node.getNtype());
	        obuf.write1(node.getProto());
	        obuf.write2BE(node.getDistHigh());
	        obuf.write2BE(node.getDistLow());
	        obuf.write2BE(node.getName().length());
	        obuf.writeN(node.getName().getBytes());
	        obuf.write1(0L);
	        obuf.write1(0L);
	    }else{
            obuf.write1(EpmdServer.PORT_RESPONSE_ID);
            obuf.write1(1L);
	    }
	    obuf.writeTo(socket.getOutputStream());

	}
	
	private void sendNodesInfo()throws IOException{
        OtpOutputStream obuf = new OtpOutputStream();
        obuf.write4BE(epmdPort);
        for(Node node: nodes.getNodes()){
            obuf.write("name ".getBytes());
            obuf.write(node.getName().getBytes());
            obuf.write(" at port ".getBytes());
            obuf.write((node.getPort()+"").getBytes());
            obuf.write1(10);
        }
        obuf.writeTo(socket.getOutputStream());
	    
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
	
	private void log(String message){
	    System.out.println(sdf.format(new Date())+ " "+ message );
	}
	
}
