/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
            } else if (cmd == Protocol.CQC_TP_RECV) {
                short qid = dis.readShort();
                this.qubitId = qid;
                System.err.println("received qubit id "+qubitId);
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
