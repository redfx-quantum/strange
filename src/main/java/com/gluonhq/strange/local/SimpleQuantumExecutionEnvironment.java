/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Gluon Software
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

import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.SingleQubitGate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author johan
 */
public class SimpleQuantumExecutionEnvironment {
    
    public Result runProgram(Program p) {
        int nQubits = p.getNumberQubits();
        Qubit[] qubit = new Qubit[nQubits];
        for (int i = 0; i < nQubits; i++) {
            qubit[i] = new Qubit();
        }
        int dim = 1 << nQubits;
        Complex[] probs = new Complex[dim];
        probs[0] = Complex.ONE;
        for (int i = 1; i < dim; i++) {
            probs[i] = Complex.ZERO;
        }
        List<Step> steps = p.getSteps();
        for (Step step: steps) {
            probs = applyStep(step, probs, qubit);
        }
        double[] qp = calculateQubitStatesFromVector(probs);
        for (int i = 0; i < nQubits; i++) {
            qubit[i].setProbability(qp[i]);
        }
        Result result = new Result(qubit, probs);
        return result;
    }
    
    private Complex[]  applyStep (Step step, Complex[] vector, Qubit[] qubits) {
        List<Gate> gates = step.getGates();
        Complex[][] a = calculateStepMatrix(gates, qubits.length);
        Complex[] result = new Complex[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = Complex.ZERO;
            for (int j = 0; j < vector.length; j++) {
                result[i] = result[i].add(a[i][j].mul(vector[j]));
            }
        }
        return result;
    }
    
    private Complex[][] calculateStepMatrix(List<Gate> gates, int nQubits) {
        Complex[][] a = new Complex[1][1];
        a[0][0] = Complex.ONE;
        int idx = 0;
        while (idx < nQubits) {
            Gate myGate = new Identity(idx);
            final int cnt = idx;
            Optional<Gate> myGateOpt = gates.stream().filter(gate -> gate.getAffectedQubitIndex().contains(cnt))
                    .findFirst();
            if (myGateOpt.isPresent()) {
                myGate = myGateOpt.get();
            }
            if (myGate instanceof SingleQubitGate) {
                SingleQubitGate sqg = (SingleQubitGate)myGate;
                a = tensor(a,sqg.getMatrix());
            }
            idx++;
        }
        return a;
    }
    
        private Complex[][] tensor(Complex[][] a, Complex[][] b) {
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
            int pw = nq - i - 1;
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

    
}
