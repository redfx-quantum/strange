/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, 2019, Gluon Software
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
package com.gluonhq.strange.local;

import com.gluonhq.strange.BlockGate;
import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.Oracle;
import com.gluonhq.strange.gate.PermutationGate;
import com.gluonhq.strange.gate.ProbabilitiesGate;
import com.gluonhq.strange.gate.SingleQubitGate;
import com.gluonhq.strange.gate.Swap;
import com.gluonhq.strange.gate.ThreeQubitGate;
import com.gluonhq.strange.gate.TwoQubitGate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author johan
 */
public class SimpleQuantumExecutionEnvironment implements QuantumExecutionEnvironment {
    
    @Override
    public Result runProgram(Program p) {
        int nQubits = p.getNumberQubits();
        Qubit[] qubit = new Qubit[nQubits];
        for (int i = 0; i < nQubits; i++) {
            qubit[i] = new Qubit();
        }
        int dim = 1 << nQubits;
        double[] initalpha = p.getInitialAlphas();
        Complex[] probs = new Complex[dim];
        for (int i = 0; i < dim; i++) {
            probs[i] = Complex.ONE;
            for (int j = 0; j < nQubits; j++) {
                int pw = nQubits - j -1 ;
                int pt = 1 << pw;
                int div = i/pt;
                int md = div % 2;
                if (md == 0) {
                    probs[i] = probs[i].mul(initalpha[j]);
                } else {
                    probs[i] = probs[i].mul(Math.sqrt(1-initalpha[j]*initalpha[j]));
                }
            }
        }
        List<Step> steps = p.getSteps();
        List<Step> simpleSteps = p.getDecomposedSteps();
        if (simpleSteps == null) {
            simpleSteps = new ArrayList<>();
            for (Step step : steps) {
                simpleSteps.addAll(Computations.decomposeStep(step, nQubits));
            }
            p.setDecomposedSteps(simpleSteps);
        }
        Result result = new Result(nQubits, steps.size());
        int cnt = 0;
        if (simpleSteps.isEmpty()) {
            result.setIntermediateProbability(0, probs);
        }
        for (Step step: simpleSteps) {
            if (!step.getGates().isEmpty()) {
                cnt++;
                probs = applyStep(step, probs, qubit);
                // printProbs(probs);
                int idx = step.getComplexStep();
                if (idx > -1) {
                    result.setIntermediateProbability(idx, probs);
                }
            }
        }
        double[] qp = calculateQubitStatesFromVector(probs);
        for (int i = 0; i < nQubits; i++) {
            qubit[i].setProbability(qp[i]);
        }
        result.measureSystem();
        p.setResult(result);
        return result;
    }
    
    @Override
    public void runProgram(Program p, Consumer<Result> result) {
        Thread t = new Thread(() -> result.accept(runProgram(p)));
        t.start();
    }

    private void printProbs(Complex[] p) {
        for (int i = 0; i < p.length; i++) {
            System.err.println("Probabiliy["+i+"]: "+p[i]);
        }
    }


    private List<Step> decomposeSteps(List<Step> steps) {
        return steps;
    }
    
    private Complex[]  applyStep (Step step, Complex[] vector, Qubit[] qubits) {
        List<Gate> gates = step.getGates();
        if (!gates.isEmpty() && gates.get(0) instanceof ProbabilitiesGate ) {
            return vector;
        }
        Complex[][] a = calculateStepMatrix(gates, qubits.length);
        Complex[] result = new Complex[vector.length];
        if (a.length != result.length) {
            throw new RuntimeException ("Wrong length of matrix or probability vector: expected "+result.length+" but got "+a.length);
        }
        for (int i = 0; i < vector.length; i++) {
            result[i] = Complex.ZERO;
            for (int j = 0; j < vector.length; j++) {
                result[i] = result[i].add(a[i][j].mul(vector[j]));
            }
        }
        return result;
    }
    
    private Complex[][] calculateStepMatrix(List<Gate> gates, int nQubits) {
        return Computations.calculateStepMatrix(gates, nQubits);

    }

    // replaced by the similar function on Complex
    @Deprecated
    public Complex[][] tensor(Complex[][] a, Complex[][] b) {
        int d1 = a.length;
        int d2 = b.length;
        Complex[][] result = new Complex[d1 * d2][d1 * d2];
        for (int rowa = 0; rowa < d1; rowa++) {
            for (int cola = 0; cola < d1; cola++) {
                for (int rowb = 0; rowb < d2; rowb++) {
                    for (int colb = 0; colb < d2; colb++) {
                        result[d2 * rowa + rowb][d2 * cola + colb] = a[rowa][cola].mul( b[rowb][colb]);
                    }
                }
            }
        }
        return result;
    }

    private double[] calculateQubitStatesFromVector(Complex[] vectorresult) {
        int nq = (int) Math.round(Math.log(vectorresult.length) / Math.log(2));
        double[] answer = new double[nq];
        int ressize = 1 << nq;
        for (int i = 0; i < nq; i++) {
            int pw = i;//nq - i - 1;
            int div = 1 << pw;
            for (int j = 0; j < ressize; j++) {
                int p1 = j / div;
                if (p1 % 2 == 1) {
                    answer[i] = answer[i] + vectorresult[j].abssqr();
                }
            }
        }
        return answer;
    }
    
    public Complex[][] createPermutationMatrix(int first, int second, int n) {
        Complex[][] swapMatrix = new Swap().getMatrix();
        Complex[][] iMatrix = new Identity().getMatrix();
        Complex[][] answer = iMatrix;
        int i = 1;
        if (first == 0) {
            answer = swapMatrix;
            i++;
        }
        while (i < n) {
            if (i == first) {
                i++;
                answer = tensor(answer, swapMatrix);
            } else {
                answer = tensor(answer, iMatrix);
            }
            i++;
        }
        return answer;
    }

    private void printMatrix(Complex[][] a) {
        for (int i = 0; i < a.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < a[i].length; j++) {
                sb.append(a[i][j]).append("    ");
            }
            System.err.println("m["+i+"]: "+sb);
        }
    }
    
}
