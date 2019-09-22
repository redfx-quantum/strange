package com.gluonhq.strange.cqc;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResponseMessage {

    private InputStream is;
    byte type;
    private short qubitId;
    private byte measurement;

    private short eprOtherPort;

    public ResponseMessage(InputStream is) {
        this.is = is;
        parseInputStream();
    }

    public ResponseMessage(byte[] msg, int length) {
        type = msg[1];
    }

    public byte getType() {
        return type;
    }

    public boolean getMeasurement() {
        return (measurement != 0x0);
    }

    public short getQubitId() {
        return this.qubitId;
    }

    public short getEprOtherPort() {
        return eprOtherPort;
    }

    private void parseInputStream() {
        try {
            DataInputStream dis = new DataInputStream(is);
            byte protocol = dis.readByte();
            if (protocol != Protocol.VERSION) {
                System.err.println("Wrong protocol version: "+protocol);
                throw new IOException("wrong protocol from server response");
            }
            byte cmd = dis.readByte();
            System.err.println("CMD = "+cmd);
            this.type = cmd;
            short appId = dis.readShort();
            int len = dis.readInt();
            System.err.println("len = "+len);
            if (cmd == Protocol.CQC_TP_NEW_OK) {
                short qid = dis.readShort();
                System.err.println("qid = "+qid);
                this.qubitId = qid;
            } else if (cmd == Protocol.CQC_TP_EPR_OK) {
                short qid = dis.readShort();
                System.err.println("qid = "+qid);
                this.qubitId = qid;
                parseEntangledHeader(dis, len -2);
            } else if (cmd == Protocol.CQC_TP_MEASOUT) {
                this.measurement = dis.readByte();
                System.err.println("got measurement: "+this.measurement);
            }
            else {
                System.err.println("ignore payload len = "+len);
                byte[] payload = new byte[len];
                dis.read(payload);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void parseEntangledHeader(DataInputStream dis,int expected) throws IOException {
        int myIp = dis.readInt();
        short myPort = dis.readShort();
        short myAppId = dis.readShort();

        int otherId = dis.readInt();
        this.eprOtherPort = dis.readShort();
        short otherAppId = dis.readShort();

        int eaid = dis.readInt();
        long timestamp = dis.readLong();
        long tog = dis.readLong();
        short goodness = dis.readShort();
        byte df = dis.readByte();
        byte unused = dis.readByte();
//        byte[] payload = new byte[expected];
//        dis.read(payload);
    }
}
