package com.gluonhq.strange.cqc;

import com.gluonhq.strange.Gate;
import com.gluonhq.strange.gate.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
public class CQCSession {

    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private final short appId;

    public CQCSession() {
        appId = getNextAppId();
    }

    public CQCSession(short id) {
        appId = id;
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

    public int createQubit() throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, 4);
        sendCommandHeader((short) 0, Protocol.CQC_CMD_NEW, (byte) 0);
        ResponseMessage msg = readMessage();
        return msg.getQubitId();
    }

    public int createEPR(String name, short port) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, 12);
        sendCommandHeader((short) 0, Protocol.CQC_CMD_EPR, (byte) 0);
        sendCommunicationHeader((short)0, port, 127*256*256*256+1);
        ResponseMessage msg = readMessage();
        return msg.getQubitId();
    }

    public void applyGate(Gate gate) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, 4);
        int qid = gate.getMainQubitIndex();
        byte cmdByte = 0;
        if (gate instanceof X) {
            cmdByte = Protocol.CQC_CMD_X;
        } else if (gate instanceof Hadamard) {
            cmdByte = Protocol.CQC_CMD_H;
        } else if (gate instanceof Cnot) {
            cmdByte = Protocol.CQC_CMD_CNOT;
        }
        System.err.println("Send command to apply gate");
        sendCommandHeader((short) qid, cmdByte, (byte) 0);
        System.err.println("Sent command done, read reply");
        ResponseMessage msg = readMessage();
        System.err.println("reply done");


    }

    public ResponseMessage readMessage() throws IOException {
        if (is == null) {
            is = socket.getInputStream();
        }
        ResponseMessage answer = new ResponseMessage(is);
//        byte[] msg = new byte[256];
//        DataInputStream dis = new DataInputStream(is);
//        byte protocol = dis.readByte();
//        int len = is.read(msg);
//        for (int i = 0; i < len; i ++) {
//            System.err.println("b["+i+"] = "+msg[i]);
//        }
//        System.err.println("Response from CQC has length "+len);
//        if (len < 8) {
//            throw new IOException ("Can't read message if it has less than 8 bytes (got "+len+")");
//        }
//        ResponseMessage answer = new ResponseMessage(msg, len);
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

    private void sendCommandHeader(short qubit_id, byte command, byte option) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeShort(qubit_id); // qubit_id
        dos.writeByte(command);
        dos.writeByte(option);
        dos.flush();
    }

    private void sendCommunicationHeader(short appId, short port, int host) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeShort(appId);
        dos.writeShort(port);
        dos.writeInt(host);
        dos.flush();
    }

    private void sendCommand(byte command, short qubit_id, boolean notify, boolean action, boolean block, int length) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, length);


    }

    private void sendSimpleCommand(byte command, short qid) throws IOException {
        sendCommand(command, qid, false, false, false,0);
        System.err.println("Sending simple command "+command);
    }

}
