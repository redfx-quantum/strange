package com.gluonhq.strange.cqc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class AppSession {

    private Socket socket;
    private OutputStream os;

    public AppSession() {

    }

    public OutputStream connect(String host, int port) throws IOException {
        System.err.println("Connecting to    " + host + ":" + port);
        Socket socket = new Socket(host, port);
        System.err.println("socket created: " + socket);
        this.socket = socket;
        this.os = socket.getOutputStream();
        return this.os;
    }

}
