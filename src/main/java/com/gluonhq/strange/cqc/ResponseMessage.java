package com.gluonhq.strange.cqc;

import java.io.DataInputStream;
import java.io.InputStream;

public class ResponseMessage {

    private InputStream is;
    byte type;
    private short qubitId;

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

    public short getQubitId() {
        return this.qubitId;
    }

    private void parseInputStream() {
        try {
            DataInputStream dis = new DataInputStream(is);
            byte protocol = dis.readByte();
            byte cmd = dis.readByte();
            System.err.println("CMD = "+cmd);
            short appId = dis.readShort();
            int len = dis.readInt();
            System.err.println("len = "+len);
            if (cmd == Protocol.CQC_TP_NEW_OK) {
                short qid = dis.readShort();
                System.err.println("qid = "+qid);
                this.qubitId = qid;
            } else {
                System.err.println("payload len = "+len);
                byte[] payload = new byte[len];
                dis.read(payload);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
