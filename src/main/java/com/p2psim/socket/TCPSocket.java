package com.p2psim.socket;

import com.p2psim.socket.packets.Packet;
import com.p2psim.socket.packets.StringPacket;

import lombok.Setter;

public class TCPSocket extends P2PSocket{
    @Setter
    private TCPSocket connected;

    public TCPSocket(int port) {
        super(SOCKET_TYPE.TCP, port);
    }

    public boolean connect(){
        if(TCPServerSocket.listen_socket_port(port)){
            this.connected = TCPServerSocket.get_listen_socket(port).handshake(this);
        }
        return this.is_connected();
    }

    public void close(){
        if(is_connected()){
            this.connected.setConnected(null);
            this.connected = null;
        }
    }

    public boolean is_connected(){
        return this.connected != null;
    }

    public void send(String msg){
        this.send(new StringPacket().setMsg(msg));
    }
    
    public void send(Packet packet){
        this.connected.add_packet(packet);
    }
    
}
