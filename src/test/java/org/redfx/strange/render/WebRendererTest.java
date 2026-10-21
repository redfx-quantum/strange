package org.redfx.strange.render;

import org.junit.jupiter.api.Test;
import org.redfx.strange.*;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class WebRendererTest {

    private static final int TEST_PORT = 19876;

    private static Program bellState() {
        return new Program(2,
            new Step(new Hadamard(0)),
            new Step(new Cnot(0, 1))
        );
    }

    @Test
    public void renderDoesNotThrow() {
        try (WebRenderer web = new WebRenderer(TEST_PORT + 1)) {
            assertDoesNotThrow(() -> web.render(bellState()));
        }
    }

    @Test
    public void httpServerResponds() throws IOException {
        try (WebRenderer web = new WebRenderer(TEST_PORT + 2)) {
            web.render(bellState());
            HttpURLConnection conn = (HttpURLConnection)
                URI.create("http://localhost:" + (TEST_PORT + 2) + "/").toURL().openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            assertEquals(200, conn.getResponseCode());
            try (InputStream is = conn.getInputStream()) {
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                assertTrue(body.contains("<html"), "Expected HTML body, got:\n" + body.substring(0, Math.min(200, body.length())));
            }
        }
    }

    @Test
    public void httpServerRespondsWithResult() throws IOException {
        try (WebRenderer web = new WebRenderer(TEST_PORT + 3)) {
            Program p = bellState();
            Result r = new SimpleQuantumExecutionEnvironment().runProgram(p);
            web.render(p, r);
            HttpURLConnection conn = (HttpURLConnection)
                URI.create("http://localhost:" + (TEST_PORT + 3) + "/").toURL().openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            assertEquals(200, conn.getResponseCode());
            try (InputStream is = conn.getInputStream()) {
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                assertTrue(body.contains("probs"), "Expected result data in HTML, got:\n" + body.substring(0, Math.min(300, body.length())));
            }
        }
    }

    @Test
    public void closeStopsServer() throws IOException {
        int port = TEST_PORT + 4;
        WebRenderer web = new WebRenderer(port);
        web.render(bellState());
        web.close();
        assertThrows(ConnectException.class, () -> new Socket("localhost", port).close());
    }
}
