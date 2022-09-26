package com.p2psim.socket;

import java.util.HashMap;
import java.util.LinkedList;

public class TCPServerSocket extends P2PSocket{
    private static HashMap<Integer, TCPServerSocket> listen_sockets = new HashMap<Integer, TCPServerSocket>();

    protected static boolean listen_socket_port(int port){
        return TCPServerSocket.listen_sockets.containsKey(port);
    }

    protected static TCPServerSocket get_listen_socket(int port){
        return TCPServerSocket.listen_sockets.get(port);
    }
    
    private LinkedList<TCPSocket> accepted = new LinkedList<TCPSocket>();

    public TCPServerSocket(int listen_port) {
        super(SOCKET_TYPE.TCP, listen_port);
        this.init_listing();
    }

    protected void init_listing(){
        if(TCPServerSocket.listen_socket_port(port)){
            throw new RuntimeException("socket port already in use");
        }else{
            TCPServerSocket.listen_sockets.put(port, this);
        }
    }
    
    public TCPSocket handshake(TCPSocket socket){
        if(socket.getPort() == this.port){
            this.accepted.push(socket);
            TCPSocket connected = new TCPSocket(this.port);
            connected.setConnected(socket);
            this.accepted.push(connected);
            return connected;
        }
        return null;
    }

    public TCPSocket accept(){
        if(this.accepted.size() > 0){
            return this.accepted.pop();
        }
        return null;
    }
}
