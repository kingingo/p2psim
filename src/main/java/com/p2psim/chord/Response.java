package com.p2psim.chord;

import com.p2psim.socket.TCPSocket;
import com.p2psim.socket.packets.Packet;

import lombok.Getter;

public class Response<P extends Packet> {
    @Getter
    private TCPSocket socket;
    private Class<P> clazz;
    private Packet packet;

    public Response(TCPSocket socket, Class<P> clazz, Packet packet) {
        this.socket = socket;
        this.packet = packet;
        this.clazz = clazz;
    }

    public P getPacket() {
        return clazz.cast(this.packet);
    }
}
