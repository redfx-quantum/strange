package com.gluonhq.strange.cqc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CQCSession {

    private Socket socket;
    private OutputStream os;
    private InputStream is;
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
        sendCqcHeader(Protocol.CQC_TP_HELLO, 0);
    }

    public void createQubit() throws IOException {
        sendSimpleCommand(Protocol.CQC_CMD_NEW, (short)0);
    }

    private void sendComman() {

    }
    public void createEPR(String name) throws IOException {
        sendCommand(Protocol.CQC_CMD_EPR,(short)0,true,false, true, 12);
   //     sendSimpleCommand(Protocol.CQC_CMD_EPR, (short)0);
    }

    public ResponseMessage readMessage() throws IOException {
        if (is == null) {
            is = socket.getInputStream();
        }
        byte[] msg = new byte[256];
        int len = is.read(msg);
        if (len < 8) {
            throw new IOException ("Can't read message if it has less than 8 bytes (got "+len+")");
        }
        ResponseMessage answer = new ResponseMessage(msg, len);
        return answer;
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
    }

    private void sendCommand(byte command, short qubit_id, boolean notify, boolean action, boolean block, int length) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, length);
        if (command == Protocol.CQC_CMD_EPR) {
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeShort(0); // qubit_id
            dos.writeByte(command);
            dos.writeByte(0);
dos.writeShort(1);
dos.writeShort(2);
dos.writeInt(4);
dos.flush();
        }

    }

    private void sendSimpleCommand(byte command, short qid) throws IOException {
        sendCommand(command, qid, false, false, false,0);
        System.err.println("Sending simple command "+command);
    }

}
