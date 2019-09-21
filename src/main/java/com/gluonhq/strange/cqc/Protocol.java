package com.gluonhq.strange.cqc;

public class Protocol {

    public static final byte VERSION = 0x2;

    public static final byte CQC_TP_HELLO = 0x0;
    public static final byte CQC_TP_COMMAND = 0x1;
    public static final byte CQC_TP_EPR_OK = 0x6;
    public static final byte CQC_TP_NEW_OK = 0xA;

    public static final byte CQC_CMD_NEW = 0x1;
    public static final byte CQC_CMD_EPR = 0x7;
    public static final byte CQC_CMD_EPR_RECV = 0x8;
    public static final byte CQC_CMD_X = 0xA;
    public static final byte CQC_CMD_H = 0x11;
    public static final byte CQC_CMD_CNOT = 0x13;

}
