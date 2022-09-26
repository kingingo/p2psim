package com.p2psim.socket.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class StringPacket extends Packet{
    @Getter
    @Setter
    private String msg;

    public StringPacket(){}

    @Override
    public void parseFromInput(DataInputStream in) throws IOException {
        this.msg = in.readUTF();
    }

    @Override
    public void writeToOutput(DataOutputStream out) throws IOException {
        out.writeUTF(this.msg);
    }
    
}
