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
package com.gluonhq.strange;

import java.util.Arrays;

/**
 *
 * @author johan
 */
public class Result {
    
    private int nqubits;
    private int nsteps;
    
    private Qubit[] qubits;
    private Complex[] probability;
    private Complex[][] intermediates;
 
    public Result(int nqubits, int steps) {
        assert(steps > 0);
        this.nqubits = nqubits;
        this.nsteps = steps;
        intermediates = new Complex[steps][];
    }

    public Result(Qubit[] q, Complex[] p) {
        this.qubits = q;
        this.probability = p;
    }
    
    public Qubit[] getQubits() {
        if (this.qubits != null) {
            return this.qubits;
        }
        Qubit[] answer = new Qubit[nqubits];
        double[] d = calculateQubitStatesFromVector(intermediates[nsteps-1]);
        for (int i = 0; i < answer.length; i++) {
            answer[i] = new Qubit();
            answer[i].setProbability(d[i]);
        }
        return answer;
    }
    
    public Complex[] getProbability() {
        return this.probability;
    }
    
    public void setIntermediateProbability(int step, Complex[] p) {
        System.out.println("set ip for step "+step+" to "+Arrays.asList(p));
        this.intermediates[step] = p;
    }
    
    public Complex[] getIntermediateProbability(int step) {
        return intermediates[step];
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
}
