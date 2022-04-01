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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import dev.antimoxs.hyplus.HyPlus;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

public class UnixPipe extends Pipe {
    private final AFUNIXSocket socket = AFUNIXSocket.newInstance();

    UnixPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) throws IOException {
        super(ipcClient, callbacks);
        this.socket.connect(new AFUNIXSocketAddress(new File(location)));
    }

    public Packet read() throws IOException {
        InputStream is = this.socket.getInputStream();

        while(is.available() == 0 && this.status == PipeStatus.CONNECTED) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException var6) {
            }
        }

        if (this.status == PipeStatus.DISCONNECTED) {
            throw new IOException("Disconnected!");
        } else if (this.status == PipeStatus.CLOSED) {
            return new Packet(OpCode.CLOSE, (JsonObject)null);
        } else {
            byte[] d = new byte[8];
            is.read(d);
            ByteBuffer bb = ByteBuffer.wrap(d);
            Packet.OpCode op = OpCode.values()[Integer.reverseBytes(bb.getInt())];
            d = new byte[Integer.reverseBytes(bb.getInt())];
            is.read(d);

            Packet p = new Packet(op, new JsonParser().parse(new String(d)).getAsJsonObject());
            HyPlus.debugLog(String.format("Received packet: %s", p.toString()));
            if (this.listener != null) {
                this.listener.onPacketReceived(this.ipcClient, p);
            }

            return p;
        }
    }

    public void write(byte[] b) throws IOException {
        this.socket.getOutputStream().write(b);
    }

    public void close() throws IOException {
        HyPlus.debugLog("Closing IPC pipe...");
        this.send(OpCode.CLOSE, new JsonObject(), (Callback)null);
        this.status = PipeStatus.CLOSED;
        this.socket.close();
    }
}
