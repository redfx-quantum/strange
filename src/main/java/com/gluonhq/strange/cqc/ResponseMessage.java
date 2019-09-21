package com.gluonhq.strange.cqc;

public class ResponseMessage {

    final byte type;

    public ResponseMessage(byte[] msg, int length) {
        type = msg[1];
    }

    public byte getType() {
        return type;
    }
}
