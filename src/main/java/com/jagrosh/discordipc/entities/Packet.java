//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jagrosh.discordipc.entities;

import java.nio.ByteBuffer;

import com.google.gson.JsonObject;

public class Packet {
    private final OpCode op;
    private final JsonObject data;

    public Packet(OpCode op, JsonObject data) {
        this.op = op;
        this.data = data;
    }

    public byte[] toBytes() {
        byte[] d = this.data.toString().getBytes();
        ByteBuffer packet = ByteBuffer.allocate(d.length + 8);
        packet.putInt(Integer.reverseBytes(this.op.ordinal()));
        packet.putInt(Integer.reverseBytes(d.length));
        packet.put(d);
        return packet.array();
    }

    public OpCode getOp() {
        return this.op;
    }

    public JsonObject getJson() {
        return this.data;
    }

    public String toString() {
        return "Pkt:" + this.getOp() + this.getJson().toString();
    }

    public static enum OpCode {
        HANDSHAKE,
        FRAME,
        CLOSE,
        PING,
        PONG;

        private OpCode() {
        }
    }
}
