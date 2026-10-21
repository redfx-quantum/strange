package org.redfx.strange.render;

import com.sun.net.httpserver.HttpServer;
import org.redfx.strange.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class WebRenderer implements Renderer {

    private static final Logger LOG = Logger.getLogger(WebRenderer.class.getName());

    private final int port;
    private HttpServer server;
    private final AtomicReference<byte[]> currentPage = new AtomicReference<>(new byte[0]);

    public WebRenderer() {
        this(8080);
    }

    public WebRenderer(int port) {
        this.port = port;
    }

    @Override
    public void render(Program program, Result result) {
        String html = buildHtml(program, result);
        currentPage.set(html.getBytes(StandardCharsets.UTF_8));
        ensureServerRunning();
        LOG.info(() -> "Circuit visualization: http://localhost:" + port);
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    private void ensureServerRunning() {
        if (server != null) return;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                byte[] body = currentPage.get();
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
            });
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start HTTP server on port " + port, e);
        }
    }

    private String buildHtml(Program program, Result result) {
        int nQubits = program.getNumberQubits();
        List<Step> steps = program.getSteps();
        int nSteps = steps.size();

        String[][] grid = TextRenderer.buildGrid(nQubits, steps);

        StringBuilder jsGrid = new StringBuilder("[");
        for (int q = 0; q < nQubits; q++) {
            jsGrid.append("[");
            for (int s = 0; s < nSteps; s++) {
                jsGrid.append("\"").append(escapeJs(grid[q][s])).append("\"");
                if (s < nSteps - 1) jsGrid.append(",");
            }
            jsGrid.append("]");
            if (q < nQubits - 1) jsGrid.append(",");
        }
        jsGrid.append("]");

        String jsResult = "null";
        if (result != null) {
            Qubit[] qubits = result.getQubits();
            Complex[] probVec = result.getProbability();
            StringBuilder rb = new StringBuilder("{probs:[");
            for (int i = 0; i < nQubits; i++) {
                if (i > 0) rb.append(",");
                rb.append(String.format(Locale.ROOT, "%.4f", qubits[i].getProbability()));
            }
            rb.append("],stateVec:[");
            if (probVec != null) {
                int limit = Math.min(probVec.length, 64);
                for (int i = 0; i < limit; i++) {
                    if (i > 0) rb.append(",");
                    rb.append(String.format(Locale.ROOT, "%.4f", probVec[i].abssqr()));
                }
            }
            rb.append("]}");
            jsResult = rb.toString();
        }

        return HTML_TEMPLATE
                .replace("__NQUBITS__", String.valueOf(nQubits))
                .replace("__NSTEPS__", String.valueOf(nSteps))
                .replace("__GRID__", jsGrid.toString())
                .replace("__RESULT__", jsResult);
    }

    private static String escapeJs(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\' -> sb.append("\\\\");
                case '"'  -> sb.append("\\\"");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case 0x2028 -> sb.append("\\u2028");
                case 0x2029 -> sb.append("\\u2029");
                case '<' -> sb.append("\\u003c");
                case '>' -> sb.append("\\u003e");
                case '&' -> sb.append("\\u0026");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
            <meta charset="utf-8">
            <title>Quantum Circuit</title>
            <style>
              body { font-family: monospace; background: #1e1e2e; color: #cdd6f4; margin: 20px; }
              h2, h3 { color: #89b4fa; margin: 12px 0; }
              svg { display: block; }
            </style>
            </head>
            <body>
            <h2>Quantum Circuit</h2>
            <div id="circuit"></div>
            <div id="result"></div>
            <script>
            const nQubits = __NQUBITS__;
            const nSteps  = __NSTEPS__;
            const grid    = __GRID__;
            const result  = __RESULT__;

            const LEFT  = 65, TOP = 30, ROW = 60, COL = 90, GW = 50, GH = 32;
            const svgW  = LEFT + nSteps * COL + 50;
            const svgH  = TOP + nQubits * ROW + 30;
            const qy    = q => TOP + q * ROW + ROW / 2;
            const sx    = s => LEFT + s * COL + COL / 2;

            const ns = "http://www.w3.org/2000/svg";
            function el(tag, attrs, text) {
              const e = document.createElementNS(ns, tag);
              for (const [k, v] of Object.entries(attrs)) e.setAttribute(k, v);
              if (text !== undefined) e.textContent = text;
              return e;
            }

            const svg = el("svg", { width: svgW, height: svgH });

            // qubit wires + labels
            for (let q = 0; q < nQubits; q++) {
              const y = qy(q);
              svg.append(el("line", { x1: 0, y1: y, x2: svgW, y2: y,
                stroke: "#45475a", "stroke-width": 2 }));
              svg.append(el("text", { x: 8, y: y, fill: "#a6e3a1", "font-size": 13,
                "font-family": "monospace", "dominant-baseline": "middle" }, "q[" + q + "]"));
            }

            // gates
            for (let s = 0; s < nSteps; s++) {
              const x = sx(s);

              // vertical connector span
              let minQ = nQubits, maxQ = -1;
              for (let q = 0; q < nQubits; q++) {
                if (grid[q][s] !== "─") { minQ = Math.min(minQ, q); maxQ = Math.max(maxQ, q); }
              }
              if (maxQ > minQ) {
                svg.append(el("line", { x1: x, y1: qy(minQ), x2: x, y2: qy(maxQ),
                  stroke: "#89b4fa", "stroke-width": 2 }));
              }

              // per-qubit symbols
              for (let q = 0; q < nQubits; q++) {
                const cell = grid[q][s];
                const y = qy(q);
                if (cell === "─" || cell === "│") continue;
                if (cell === "●") {
                  svg.append(el("circle", { cx: x, cy: y, r: 7, fill: "#89b4fa" }));
                } else {
                  svg.append(el("rect", { x: x - GW/2, y: y - GH/2, width: GW, height: GH,
                    rx: 4, fill: "#313244", stroke: "#89b4fa", "stroke-width": 1.5 }));
                  svg.append(el("text", { x, y, fill: "#cdd6f4", "font-size": 14,
                    "font-family": "monospace", "dominant-baseline": "middle",
                    "text-anchor": "middle" }, cell));
                }
              }
            }

            document.getElementById("circuit").append(svg);

            // result panel
            if (result) {
              const BAR = 180, div = document.getElementById("result");
              div.innerHTML = "<h2>Result</h2>";
              const probSvgH = nQubits * 30 + 10;
              const probSvg = el("svg", { width: BAR + 160, height: probSvgH });

              for (let q = 0; q < nQubits; q++) {
                const y = q * 30 + 5, prob = result.probs[q], fill = Math.round(prob * BAR);
                probSvg.append(el("text", { x: 0, y: y + 12, fill: "#a6e3a1", "font-size": 13,
                  "font-family": "monospace", "dominant-baseline": "middle" }, "q[" + q + "]"));
                probSvg.append(el("rect", { x: 40, y, width: BAR, height: 20,
                  rx: 3, fill: "#313244" }));
                probSvg.append(el("rect", { x: 40, y, width: fill, height: 20,
                  rx: 3, fill: "#89b4fa" }));
                probSvg.append(el("text", { x: 40 + BAR + 8, y: y + 12, fill: "#cdd6f4",
                  "font-size": 12, "font-family": "monospace",
                  "dominant-baseline": "middle" }, (prob * 100).toFixed(1) + "%"));
              }
              div.append(probSvg);

              // state vector
              const nonZero = result.stateVec.map((v, i) => [i, v]).filter(([,v]) => v > 1e-6);
              if (nonZero.length > 0) {
                const h3 = document.createElement("h3");
                h3.textContent = "State Vector";
                div.append(h3);
                const stSvgH = nonZero.length * 22 + 10;
                const stSvg = el("svg", { width: 320, height: stSvgH });
                nonZero.forEach(([i, amp], row) => {
                  const bin = i.toString(2).padStart(nQubits, "0");
                  const y = row * 22 + 2, barLen = Math.round(amp * 150);
                  stSvg.append(el("text", { x: 0, y: y + 12, fill: "#cdd6f4", "font-size": 12,
                    "font-family": "monospace", "dominant-baseline": "middle" }, "|" + bin + "⟩"));
                  stSvg.append(el("rect", { x: 55, y, width: 150, height: 18, rx: 3, fill: "#313244" }));
                  stSvg.append(el("rect", { x: 55, y, width: barLen, height: 18, rx: 3, fill: "#89b4fa" }));
                  stSvg.append(el("text", { x: 215, y: y + 12, fill: "#cdd6f4", "font-size": 12,
                    "font-family": "monospace", "dominant-baseline": "middle" }, amp.toFixed(4)));
                });
                div.append(stSvg);
              }
            }
            </script>
            </body>
            </html>
            """;
}
