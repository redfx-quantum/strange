package com.gluonhq.strange.cqc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class CQCSession {

    private Socket socket;
    private OutputStream os;
    private short appId;

    public CQCSession() {
        appId = getNextAppId();
    }

    public void connect(String host, int port) throws IOException {
        System.err.println("Connecting to "+host+":"+port);
        Socket socket = new Socket(host, port);
        System.err.println("socket created: "+socket);
        this.socket = socket;
        this.os = socket.getOutputStream();
    }

    public void sendHello() throws IOException {
        sendCqcHeadercd op  -   (Protocol.CQC_TP_HELLO, 0);
    }

    public void createQubit() throws IOException {
        sendSimpleCommand(Protocol.CQC_CMD_NEW, (short)0);
    }

    static short appIdCounter = 0;

    private static synchronized short getNextAppId() {
        appIdCounter++;
        return appIdCounter;
    }
    /*
     * len = exclusive header.
     */
    private void sendCqcHeader(byte type, int len) throws IOException {
        int totalLength = len + 8;

        DataOutputStream dos = new DataOutputStream(os);
        dos.writeByte(Protocol.VERSION);
        dos.writeByte(type);
        dos.writeShort(appId);
        dos.writeInt(len);
        dos.flush();
//        byte[] data = new byte[8];
//        data[0] = Protocol.VERSION;
//        data[1] = type;
//        data[2] = (byte)(appId & 0xff);
//        data[3] = (byte)((appId >> 8) & 0xff);

    }
    private void sendCommand(byte command, short qubit_id, boolean notify, boolean action, boolean block, int length) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, length);

    }

    private void sendSimpleCommand(byte command, short qid) throws IOException {
        sendCommand(command, qid, false, false, false,0);
        System.err.println("Sending simple command "+command);
    }

}
