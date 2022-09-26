package com.p2psim;

import java.util.ArrayList;

import com.p2psim.socket.P2PSocket;
import com.p2psim.socket.Utils;

import lombok.Getter;

@Getter
public abstract class Node implements Runnable{
    
    protected long id;
    protected int listen_on_port;
    private P2PSocket listen_socket;

    public Node(int listen_on_port) {
        this.id = Utils.hashNode(this);
        this.listen_on_port = listen_on_port;
    }

    public abstract void run();

    public void sleep(long milis){
    }
}
