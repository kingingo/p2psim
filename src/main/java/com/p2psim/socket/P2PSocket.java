package com.p2psim.socket;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.p2psim.socket.packets.Packet;
import com.p2psim.socket.packets.StringPacket;

import lombok.Getter;

/**
 * FEHLER: 
 * - Socket wird nicht gelöscht, wenn er geschlossen wird
 * - Socket unterteilen in listen sockets und connect sockets
 * 
 * 
 */

@Getter
public abstract class P2PSocket {
    public static int DELAY_MIN = 50;
    public static int DELAY_MAX = 500;

    private SOCKET_TYPE type;
    protected int port;
    protected LinkedList<Packet> packets = new LinkedList<Packet>();
    
    public P2PSocket(SOCKET_TYPE type, int port){
        this.type = type;
        this.port = port;
    }

    protected void add_packet(Packet packet){
        packet.setDelay(Utils.random_int(DELAY_MIN, DELAY_MAX));
        packet.setReceive_time(System.currentTimeMillis() + packet.getDelay());
        this.packets.push(packet);
    }

    public String receive_string(){
        StringPacket packet = this.receive(StringPacket.class);
        if(packet != null){
            return packet.getMsg();
        }
        return null;
    }

    public Packet receive(){
        long current_time = System.currentTimeMillis();
    
        if(this.packets.size() > 0 && this.packets.peek().getReceive_time() <= current_time){
            return this.packets.pop();
        }
        return null;
    }

    public <P extends Packet> P receive(Class<P> clazz){
        Packet packet = this.receive();
        if(packet != null)
            return clazz.cast(packet);
        return null;
    }

    enum SOCKET_TYPE{
        TCP, UDP
    }
}
