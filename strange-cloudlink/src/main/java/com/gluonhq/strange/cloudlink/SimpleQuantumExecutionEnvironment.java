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
package com.gluonhq.strange.cloudlink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleQuantumExecutionEnvironment {
    
    public Result runProgram(Program p) {
        int nQubits = p.getNumberQubits();
        int dim = 1 << nQubits;
        List<Complex> probs = new ArrayList<>(dim);
        probs.add(Complex.ONE);
        for (int i = 1; i < dim; i++) {
            probs.add(Complex.ZERO);
        }
        List<Step> steps = p.getSteps();
        List<Step> simpleSteps = new ArrayList<>();
        for (Step step: steps) {
            simpleSteps.addAll(decomposeStep(step, nQubits));
        }
       System.out.println("stepsize "+steps.size()+" and simplestepsize = "+simpleSteps.size());
       printProbs(probs);
        for (Step step: simpleSteps) {
            probs = applyStep(step, probs, nQubits);
//            printProbs(probs);
        }
        double[] qp = calculateQubitStatesFromVector(probs);
        List<Double> qubits = new ArrayList<>(nQubits);
        for (int i = 0; i < nQubits; i++) {
            qubits.add(qp[i]);
        }
        return new Result(qubits, probs);
    }

    private void printProbs(List<Complex> p) {
        System.out.println("\n");
        for (int i = 0; i < p.size(); i++) {
            System.out.println("P["+i+"]: "+p.get(i));
        }
    }
    /**
     * decompose a Step into steps that can be processed without permutations
     * @param s
     * @return 
     */
    private List<Step> decomposeStep(Step s, int nqubit) {
        ArrayList<Step> answer = new ArrayList<>();
        answer.add(s);
        List<Gate> gates = s.getGates();
        if (gates.isEmpty()) return answer;
        boolean simple = gates.stream().allMatch(g -> "SingleQubit".equals(g.getGroup()));
        if (simple) return answer;
        // at least one non-singlequbitgate
       // System.out.println("Complex gates!");
        List<Gate> firstGates = new ArrayList<>();
        for (Gate gate : gates) {
            if ("SingleQubit".equals(gate.getGroup())) {
                firstGates.add(gate);
            } else if ("TwoQubit".equals(gate.getGroup())) {
                int first = gate.getAffectedQubitIndex().get(0);
                int second = gate.getAffectedQubitIndex().get(1);
                if (first == second + 1) {
                    firstGates.add(gate);
                } else {
                    if (first == second) throw new RuntimeException ("Wrong gate, first == second for "+gate);
                    if (first > second) {
                        Step prePermutation = new Step();

                        PermutationGate pg = new PermutationGate(first - 1, second, nqubit);
                        prePermutation.addGate(pg);
                        answer.add(0, prePermutation);
                        answer.add(prePermutation);
                        gate.setAdditionalQubit(first - 1, second);
                    } else {
                        Step prePermutation = new Step();

                        PermutationGate pg = new PermutationGate(first, second, nqubit );
                        prePermutation.addGate(pg);

                        answer.add(0, prePermutation);
                        answer.add(prePermutation);
                        Step postPermutation = new Step();
                        if (first != second -1) {
                            PermutationGate pg2 = new PermutationGate(second-1, first, nqubit );
                            postPermutation.addGate(pg2);
                            answer.add(1, postPermutation);
                            answer.add(3,postPermutation);
                        }
                        gate.setMainQubit(second);
                        gate.setAdditionalQubit(second-1,0);
                    }
                }
            } else {
                throw new RuntimeException("Gate must be SingleQubit or TwoQubit");
            }
        }
        return answer;
    }

    private List<Complex> applyStep(Step step, List<Complex> vector, int nQubits) {
        List<Gate> gates = step.getGates();
        Complex[][] a = calculateStepMatrix(gates, nQubits);
        System.out.println("applystep, gates = "+gates);
        printMatrix(a);
        List<Complex> result = new ArrayList<>(vector.size());
        for (int i = 0; i < vector.size(); i++) {
            result.add(Complex.ZERO);
            for (int j = 0; j < vector.size(); j++) {
                result.set(i, result.get(i).add(a[i][j].mul(vector.get(j))));
            }
        }
        return result;
    }
    
    private Complex[][] calculateStepMatrix(List<Gate> gates, int nQubits) {
        Complex[][] a = new Complex[1][1];
        a[0][0] = Complex.ONE;
        int idx = nQubits-1;
        while (idx >= 0) {
            Gate myGate = new Identity(idx);
            final int cnt = idx;
            Optional<Gate> myGateOpt = gates.stream().filter(gate -> gate.getAffectedQubitIndex().contains(cnt))
                    .findFirst();
            if (myGateOpt.isPresent()) {
                myGate = myGateOpt.get();
            }
            System.out.println("idx = "+idx+", gate = "+myGate);
            a = tensor(a, myGate.getMatrix());
            if ("TwoQubit".equals(myGate.getGroup())) {
                idx--;
            }
            if ("P".equals(myGate.getCaption())) {
                idx = 0;
            }
            idx--;
        }
       // System.out.println("done calcstepmatrix, nq = "+nQubits+", dim = "+a.length);
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

    private double[] calculateQubitStatesFromVector(List<Complex> vectorresult) {
        int nq = (int) Math.round(Math.log(vectorresult.size()) / Math.log(2));
        double[] answer = new double[nq];
        int ressize = 1 << nq;
        for (int i = 0; i < nq; i++) {
            int pw = i;//nq - i - 1;
            int div = 1 << pw;
            for (int j = 0; j < ressize; j++) {
                int p1 = j / div;
                if (p1 % 2 == 1) {
                    answer[i] = answer[i] + vectorresult.get(j).abssqr();
                }
            }
        }
        return answer;
    }

    private void printMatrix(Complex[][] a) {
        for (int i = 0; i < a.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < a[i].length; j++) {
                sb.append(a[i][j]).append("    ");
            }
            System.out.println("m["+i+"]: "+sb);
        }
    }
    
}
