package com.p2psim;

import com.p2psim.socket.P2PSocket;

import lombok.Getter;

@Getter
public abstract class Node implements Runnable{
    protected int port;
    private P2PSocket listen_socket;

    public Node(int port) {
        this.port = port;
    }

    public abstract void run();

    public void sleep(long milis){
    }
}
