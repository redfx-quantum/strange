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

public class Protocol {

    public static final byte VERSION = 0x2;

    public static final byte CQC_TP_HELLO = 0x0;
    public static final byte CQC_TP_COMMAND = 0x1;
    public static final byte CQC_TP_DONE = 0x4;
    public static final byte CQC_TP_RECV = 0x5;
    public static final byte CQC_TP_EPR_OK = 0x6;
    public static final byte CQC_TP_MEASOUT = 0x7;
    public static final byte CQC_TP_NEW_OK = 0xA;

    public static final byte CQC_CMD_NEW = 0x1;
    public static final byte CQC_CMD_MEASURE = 0x2;
    public static final byte CQC_CMD_SEND = 0x5;
    public static final byte CQC_CMD_RECV = 0x6;
    public static final byte CQC_CMD_EPR = 0x7;
    public static final byte CQC_CMD_EPR_RECV = 0x8;
    public static final byte CQC_CMD_X = 0xA;
    public static final byte CQC_CMD_H = 0x11;
    public static final byte CQC_CMD_CNOT = 0x14;
    public static final byte CQC_CMD_RELEASE = 0x17;

    // OPTIONS
    public static final byte CQC_OPT_NOTIFY = 0x1;
    public static final byte CQC_OPT_ACTION = 0x2;
    public static final byte CQC_OPT_BLOCK = 0x4;


}
