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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AppSession {

    private Socket socket;
    private OutputStream os;
    PipedOutputStream pos = new PipedOutputStream();
    PipedInputStream pis;


    public AppSession() {
    }

    public AppSession(int port) throws IOException {
        openServerSocket(port);
    }

    public OutputStream connect(String host, int port) throws IOException {
        System.err.println("Connecting to    " + host + ":" + port);
        Socket socket = new Socket(host, port);
        System.err.println("socket created: " + socket);
        this.socket = socket;
        this.os = socket.getOutputStream();
        return this.os;
    }

    public void close() throws IOException {
        this.os.close();
        this.socket.close();
    }


    public byte readByte() throws IOException {
        return (byte) pis.read();
    }

    private void openServerSocket(int port) throws IOException {
        pis = new PipedInputStream(pos);

        ServerSocket serverSocket = new ServerSocket(port);
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                try {
                    boolean go = true;
                    while (go) {
                        Socket child = serverSocket.accept();
                        System.err.println("Got a child socket");
                        processChildSocket(child);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        serverThread.start();
    }

    private void processChildSocket(Socket socket) {

        try {
            InputStream is = socket.getInputStream();
            int l = is.read();
            while (l > -1) {
                pos.write(l);
                l = is.read();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

}
