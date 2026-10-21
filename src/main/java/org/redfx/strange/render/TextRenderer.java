package org.redfx.strange.render;

import org.redfx.strange.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;

public class TextRenderer implements Renderer {

    private final PrintStream out;

    public TextRenderer() {
        this(System.out);
    }

    public TextRenderer(PrintStream out) {
        this.out = out;
    }

    @Override
    public void render(Program program, Result result) {
        int nQubits = program.getNumberQubits();
        List<Step> steps = program.getSteps();
        int nSteps = steps.size();

        String[][] grid = buildGrid(nQubits, steps);
        int[] widths = columnWidths(grid, nQubits, nSteps);

        out.println("Circuit (" + nQubits + " qubits, " + nSteps + " steps):");
        for (int q = 0; q < nQubits; q++) {
            StringBuilder row = new StringBuilder(String.format("q[%d]: ", q));
            for (int s = 0; s < nSteps; s++) {
                appendCell(row, grid[q][s], widths[s]);
            }
            out.println(row);
        }

        if (result != null) {
            renderResult(result, nQubits);
        }
    }

    static String[][] buildGrid(int nQubits, List<Step> steps) {
        int nSteps = steps.size();
        String[][] grid = new String[nQubits][nSteps];
        for (int q = 0; q < nQubits; q++)
            for (int s = 0; s < nSteps; s++)
                grid[q][s] = "─";

        for (int s = 0; s < nSteps; s++) {
            Step step = steps.get(s);
            if (step.getType() == Step.Type.PSEUDO) continue;

            for (Gate gate : step.getGates()) {
                int main = gate.getMainQubitIndex();
                int minQ, maxQ;

                if (gate instanceof ControlledGate cg) {
                    for (int ctrl : cg.getControlIndexes()) grid[ctrl][s] = "●";
                    grid[cg.getRootGateIndex()][s] = cg.getRootGate().getCaption();
                    minQ = cg.getControlIndexes().stream().mapToInt(Integer::intValue).min().getAsInt();
                    maxQ = cg.getControlIndexes().stream().mapToInt(Integer::intValue).max().getAsInt();
                    minQ = Math.min(minQ, cg.getRootGateIndex());
                    maxQ = Math.max(maxQ, cg.getRootGateIndex());
                } else {
                    List<Integer> affected = gate.getAffectedQubitIndexes();
                    if (gate instanceof ControlledBlockGate cbg) {
                        grid[cbg.getControlQubit()][s] = "●";
                    }
                    grid[main][s] = gate.getCaption();
                    minQ = affected.stream().mapToInt(Integer::intValue).min().orElse(main);
                    maxQ = affected.stream().mapToInt(Integer::intValue).max().orElse(main);
                }

                for (int q = minQ; q <= maxQ; q++) {
                    if ("─".equals(grid[q][s])) grid[q][s] = "│";
                }
            }
        }
        return grid;
    }

    private static int[] columnWidths(String[][] grid, int nQubits, int nSteps) {
        int[] widths = new int[nSteps];
        for (int s = 0; s < nSteps; s++) {
            int max = 1;
            for (int q = 0; q < nQubits; q++) {
                String cell = grid[q][s];
                if (!"─".equals(cell) && !"│".equals(cell) && !"●".equals(cell)) {
                    max = Math.max(max, cell.length());
                }
            }
            widths[s] = max;
        }
        return widths;
    }

    private static void appendCell(StringBuilder sb, String cell, int width) {
        int padding = width - cell.length();
        int left = padding / 2;
        int right = padding - left;
        sb.append("─").append("─".repeat(left)).append(cell).append("─".repeat(right)).append("─");
    }

    private void renderResult(Result result, int nQubits) {
        out.println();
        out.println("Result:");
        Qubit[] qubits = result.getQubits();
        for (int i = 0; i < nQubits; i++) {
            out.printf(Locale.ROOT, "  q[%d]: prob(|1⟩) = %.4f%n", i, qubits[i].getProbability());
        }

        Complex[] probVec = result.getProbability();
        if (probVec != null) {
            out.println();
            out.println("State vector (top amplitudes):");
            int shown = 0;
            for (int i = 0; i < probVec.length && shown < 8; i++) {
                double amp = probVec[i].abssqr();
                if (amp > 1e-6) {
                    out.printf(Locale.ROOT, "  |%s⟩: %.4f%n", toBinaryKet(i, nQubits), amp);
                    shown++;
                }
            }
        }
    }

    private static String toBinaryKet(int index, int nQubits) {
        String bin = Integer.toBinaryString(index);
        return "0".repeat(Math.max(0, nQubits - bin.length())) + bin;
    }
}
