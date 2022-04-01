//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jagrosh.discordipc.entities.pipe;

import com.google.gson.*;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.Packet.OpCode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import dev.antimoxs.hyplus.HyPlus;

public class WindowsPipe extends Pipe {
    private final RandomAccessFile file;

    WindowsPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) {
        super(ipcClient, callbacks);

        try {
            this.file = new RandomAccessFile(location, "rw");
        } catch (FileNotFoundException var5) {
            throw new RuntimeException(var5);
        }
    }

    public void write(byte[] b) throws IOException {
        this.file.write(b);
    }

    public Packet read() throws IOException {
        while(this.file.length() == 0L && this.status == PipeStatus.CONNECTED) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException var5) {
            }
        }

        if (this.status == PipeStatus.DISCONNECTED) {
            throw new IOException("Disconnected!");
        } else if (this.status == PipeStatus.CLOSED) {
            return new Packet(OpCode.CLOSE, (JsonObject)null);
        } else {
            Packet.OpCode op = OpCode.values()[Integer.reverseBytes(this.file.readInt())];
            int len = Integer.reverseBytes(this.file.readInt());
            byte[] d = new byte[len];
            this.file.readFully(d);

            String bytes = new String(d);

            Packet p = new Packet(op, new JsonParser().parse(bytes).getAsJsonObject());
            HyPlus.debugLog(String.format("Received packet: %s", p.toString()));
            if (this.listener != null) {
                this.listener.onPacketReceived(this.ipcClient, p);
            }

            return p;
        }
    }

    public void close() throws IOException {
        HyPlus.debugLog("Closing IPC pipe...");
        this.send(OpCode.CLOSE, new JsonObject(), (Callback)null);
        this.status = PipeStatus.CLOSED;
        this.file.close();
    }



}
