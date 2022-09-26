package com.p2psim.chord;

import com.p2psim.Node;
import com.p2psim.socket.TCPServerSocket;
import com.p2psim.socket.TCPSocket;
import com.p2psim.socket.packets.Packet;
import com.p2psim.socket.packets.StringPacket;

/**
 * https://courses.cs.duke.edu//cps212/spring15/15-744/S07/papers/chord.pdf
 * https://github.com/ChuanXia/Chord/blob/master/Node.java
 * 
 */
public class ChordNode extends Node{

    private TCPSocket[] finger_table = new TCPSocket[32];
	private TCPSocket predecessor = null;
    private TCPServerSocket server_socket = null;

    public ChordNode(int listen_on_port) {
        super(listen_on_port);
        this.server_socket = new TCPServerSocket(listen_on_port);
    }

    

    /**
	 * Notify successor that this node should be its predecessor
	 * @param successor
	 * @return successor's response
	 */
    public String notify(TCPSocket socket){
        if(socket != null && socket.getPort() != this.port){
            return send_request("i_am_pre_", socket.getPort()).getPacket().getMsg();
        }   
        return null;
    }

    /**
     * Join the chord network
     * @param port
     * @return
     */
    public boolean join(int port){
        if(port != this.port){
            Response<StringPacket> response = send_request("find_successor", port);

            if(response != null && response.getPacket().getMsg().startsWith("found_successor")){
                update_finder_table(0, response.getSocket());
                return true;
            }   
        }

        return true;
    }

    public void update_finder_table(int index, TCPSocket socket){
        this.finger_table[index] = socket;

        if(index == 0 && socket != null && socket.getPort() != this.port){

        }
    }

    public Response<StringPacket> send_request(String msg, TCPSocket socket){
        socket.send(msg+"_"+getPort());
        
        sleep(60);
        Packet packet = socket.receive();
        return packet != null ? new Response<StringPacket>(socket, StringPacket.class, packet) : null;
    }

    public Response<StringPacket> send_request(String msg, int port){
        return send_request(msg, new TCPSocket(port));
    }

    public void accept_connection() {
        TCPSocket socket = this.server_socket.accept();

        if(socket != null){
            //DO
        }
    }

    @Override
    public void run() {
       this.accept_connection();
    }
}
