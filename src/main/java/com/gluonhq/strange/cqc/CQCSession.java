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
        byte option = Protocol.CQC_OPT_NOTIFY | Protocol.CQC_OPT_BLOCK;
        sendCommandHeader((short) 0, Protocol.CQC_CMD_NEW, option);
        System.err.println("readmessage");
        ResponseMessage msg = readMessage();
        System.err.println("readmessage again, expect TP_DONE");
        ResponseMessage done = readMessage();
        System.err.println("that was a message of type "+done.getType());
        return msg.getQubitId();
    }

    // TODO return an EPR (create that class first)
    public ResponseMessage createEPR(String name, short port) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, 12);
        byte option = Protocol.CQC_OPT_NOTIFY | Protocol.CQC_OPT_BLOCK;
        sendCommandHeader((short) 0, Protocol.CQC_CMD_EPR, option);
        sendCommunicationHeader((short)0, port, 127*256*256*256+1);
        System.err.println("readmessage");
        ResponseMessage msg = readMessage();
        System.err.println("readmessage again, expect TP_DONE");
        ResponseMessage done = readMessage();
        System.err.println("that was a message of type "+done.getType());
        return msg;
    }

    public void applyGate(Gate gate) throws IOException {
        int qid = gate.getMainQubitIndex();
        byte cmdByte = 0;
        int len = 4;
        if (gate instanceof X) {
            cmdByte = Protocol.CQC_CMD_X;
        } else if (gate instanceof Hadamard) {
            cmdByte = Protocol.CQC_CMD_H;
        } else if (gate instanceof Cnot) {
            cmdByte = Protocol.CQC_CMD_CNOT;
            len = 6;
        }
        sendCqcHeader(Protocol.CQC_TP_COMMAND, len);

        byte option = Protocol.CQC_OPT_NOTIFY | Protocol.CQC_OPT_BLOCK;
        System.err.println("Send command to apply gate");
        sendCommandHeader((short) qid, cmdByte, option);
        if (gate instanceof Cnot) {
            sendExtraQubitHeader((short) ((Cnot) gate).getSecondQubit());
        }
        System.err.println("wait for TP_DONE");
        ResponseMessage done = readMessage();
        System.err.println("that was a message of type "+done.getType());
        if (done.getType() != Protocol.CQC_TP_DONE) {
            System.err.println("That wasn't TP_DONE!");
            throw new IOException("Got message of type "+done.getType()+" instead of TP_DONE");
        }
    }

    public boolean measure(int qid) throws IOException {
        sendCqcHeader(Protocol.CQC_TP_COMMAND, 4);
        byte option = Protocol.CQC_OPT_BLOCK;
        sendCommandHeader((short) qid, Protocol.CQC_CMD_MEASURE, option);
        System.err.println("send measure command, read response");
        ResponseMessage done = readMessage();
        System.err.println("got measure response");
        return done.getMeasurement();
    }

    public ResponseMessage readMessage() throws IOException {
        if (is == null) {
            System.err.println("IS NULL!!!\n");
            is = socket.getInputStream();
        }
        ResponseMessage answer = new ResponseMessage(is);
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

    private void sendExtraQubitHeader(short extraQubit) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeShort(extraQubit);
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
