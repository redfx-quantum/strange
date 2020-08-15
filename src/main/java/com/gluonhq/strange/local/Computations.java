/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Gluon Software
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
import static com.gluonhq.strange.Complex.tensor;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.Oracle;
import com.gluonhq.strange.gate.PermutationGate;
import com.gluonhq.strange.gate.ProbabilitiesGate;
import com.gluonhq.strange.gate.SingleQubitGate;
import com.gluonhq.strange.gate.ThreeQubitGate;
import com.gluonhq.strange.gate.TwoQubitGate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author johan
 */
public class Computations {
        
    static void dbg (String s) {
        System.err.println("[DBG] " + System.currentTimeMillis()%100000+": "+s);
    }
    
    public static Complex[][] calculateStepMatrix(List<Gate> gates, int nQubits) {
        long l0 = System.currentTimeMillis();
        Complex[][] a = new Complex[1][1];
        a[0][0] = Complex.ONE;
        int idx = nQubits-1;
        System.err.println("Calculate stepMatrix for "+nQubits+" qubits, gates = "+gates);
        printMemory();
        while (idx >= 0) {
            final int cnt = idx;
            Gate myGate = gates.stream()
                    .filter(
                   // gate -> gate.getAffectedQubitIndex().contains(cnt)
                        gate -> gate.getHighestAffectedQubitIndex() == cnt )
                    .findFirst()
                    .orElse(new Identity(idx));
            dbg("stepmatrix, cnt = "+cnt+", idx = "+idx+", myGate = "+myGate);
            if (myGate instanceof BlockGate) {
                dbg("calculateStepMatrix for blockgate "+myGate+" of class "+myGate.getClass());
                BlockGate sqg = (BlockGate)myGate;
                a = tensor(a, sqg.getMatrix());
                dbg("calculateStepMatrix for blockgate calculated "+myGate);

                idx = idx - sqg.getSize()+1;
                System.err.println("now, idx = "+idx);
            }
            if (myGate instanceof SingleQubitGate) {
                dbg("sqg");
                SingleQubitGate sqg = (SingleQubitGate)myGate;
                a = tensor(a, sqg.getMatrix());
                dbg("sqgdone");
            }
            if (myGate instanceof TwoQubitGate) {
                TwoQubitGate tqg = (TwoQubitGate)myGate;
                a = tensor(a, tqg.getMatrix());
                idx--;
            }
            if (myGate instanceof ThreeQubitGate) {
                ThreeQubitGate tqg = (ThreeQubitGate)myGate;
                a = tensor(a, tqg.getMatrix());
                idx = idx-2;
            }
            if (myGate instanceof PermutationGate) {
                throw new RuntimeException("No perm allowed ");
//                a = tensor(a, myGate.getMatrix());
  //              idx = 0;
            }
            if (myGate instanceof Oracle) {
                a = myGate.getMatrix();
                idx = 0;
            }
            idx--;
        }
      //  printMatrix(a);
        long l1 = System.currentTimeMillis();
        System.err.println("COMPCOUT calculateStepMatrix for "+gates+" and "+nQubits+" took "+ (l1 -l0) +" ms");
        return a;
    }
    
    /**
     * decompose a Step into steps that can be processed without permutations
     * @param s
     * @return 
     */
    public static List<Step> decomposeStep(Step s, int nqubit) {
        ArrayList<Step> answer = new ArrayList<>();
        answer.add(s);
        List<Gate> gates = s.getGates();

        if (gates.isEmpty()) return answer;
        boolean simple = gates.stream().allMatch(g -> g instanceof SingleQubitGate);
        if (simple) return answer;
        // if only 1 gate, which is an oracle, return as well
        if ((gates.size() ==1) && (gates.get(0) instanceof Oracle)) return answer;
        // at least one non-singlequbitgate
        List<Gate> firstGates = new ArrayList<>();
        for (Gate gate : gates) {
            if (gate instanceof ProbabilitiesGate) {
                s.setInformalStep(true);
                return answer;
            }
            if (gate instanceof BlockGate) {
                firstGates.add(gate);
            } else if (gate instanceof SingleQubitGate) {
                firstGates.add(gate);
            } else if (gate instanceof TwoQubitGate) {
                TwoQubitGate tqg = (TwoQubitGate) gate;
                int first = tqg.getMainQubitIndex();
                int second = tqg.getSecondQubitIndex();
                if ((first >= nqubit) || (second >= nqubit)) {
                    throw new IllegalArgumentException ("Step "+s+" uses a gate with invalid index "+first+" or "+second);
                }
                System.err.println("TQG: "+tqg+", first = "+first+" and second = "+second+", nq = "+nqubit);
                if (first == second + 1) {
                    firstGates.add(gate);
                } else {
                    if (first == second) throw new RuntimeException ("Wrong gate, first == second for "+gate);
                    if (first > second) {
                        PermutationGate pg = new PermutationGate(first - 1, second, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(0, prePermutation);
                        answer.add(postPermutation);
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                    } else {
                        PermutationGate pg = new PermutationGate(first, second, nqubit );
                        Step prePermutation = new Step(pg);
                        Step prePermutationInv = new Step(pg);
                        int realStep = s.getIndex();
                        s.setComplexStep(-1);
                        answer.add(0, prePermutation);
                        answer.add(prePermutationInv);
                        Step postPermutation = new Step();
                        Step postPermutationInv = new Step();
                        if (first != second -1) {
                            PermutationGate pg2 = new PermutationGate(second-1, first, nqubit );
                            postPermutation.addGate(pg2);
                            postPermutationInv.addGate(pg2);
                            answer.add(1, postPermutation);
                            answer.add(3, postPermutationInv);
                        } 
                        prePermutationInv.setComplexStep(realStep);
                    }
                }
            } else if (gate instanceof ThreeQubitGate) {
                ThreeQubitGate tqg = (ThreeQubitGate) gate;
                int first = tqg.getMainQubit();
                int second = tqg.getSecondQubit();
                int third = tqg.getThirdQubit();
                int sFirst = first;
                int sSecond = second;
                int sThird = third;
                if ((first == second + 1) && (second == third + 1)) {
                    firstGates.add(gate);
                } else {
                    int p0idx = 0;
                    int maxs = Math.max(second, third);
                    if (first < maxs) {
                        PermutationGate pg = new PermutationGate(first, maxs, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(p0idx, prePermutation);
                        answer.add(answer.size()-p0idx, postPermutation);
                        p0idx++;
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                        sFirst = maxs;
                        if (second > third) {
                            sSecond = first;
                        } else {
                            sThird = first;
                        }
                    }
                    if (sSecond != sFirst -1) {
                        PermutationGate pg = new PermutationGate(sFirst - 1, sSecond, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(p0idx, prePermutation);
                        answer.add(answer.size()-p0idx, postPermutation);
                        p0idx++;
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                        sSecond = sFirst -1;
                    }
                    if (sThird != sFirst -2) {
                        PermutationGate pg = new PermutationGate(sFirst - 2, sThird, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(p0idx, prePermutation);
                        answer.add(answer.size()-p0idx, postPermutation);
                        p0idx++;
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                        sThird = sFirst -2;
                    }
                }
            }
            else {
                throw new RuntimeException("Gate must be SingleQubit or TwoQubit");
            }
        }
        return answer;
    }
    
    public static void dontprintMatrix(Complex[][] a) {
        for (int i = 0; i < a.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < a[i].length; j++) {
                sb.append(a[i][j]).append("    ");
            }
            System.out.println("m["+i+"]: "+sb);
        }
    }
    
    public static int getInverseModulus(int a, int b) {
        System.err.println("invmodus asked for a = "+a+" and b = "+b);
        int r0 = a;
        int r1 = b;
        int r2 = 0;
        int s0 = 1;
        int s1 = 0;
        int s2 = 0;
        while (r1 != 1) {
            int q = r0/r1;
            r2 = r0%r1;
            s2 = s0 - q*s1;
            r0 = r1;
            r1 = r2;
            s0 = s1;
            s1 = s2;
        }
        return s1 > 0 ? s1 : s1+b;
    }
    
    public static int gcd (int a, int b) {
        int x = a > b ? a : b;
        int y = x==a ? b :a ;
        int z = 0;
        while (y != 0) {
            z = x % y;
            x = y;
            y = z;
        }
        return x;
    }
    
    
    public static Complex[][] createIdentity(int dim) {
        Complex[][] matrix = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                matrix[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }
        return matrix;
    }

    public static void printMemory() {
        Runtime rt = Runtime.getRuntime();
        long fm = rt.freeMemory()/1024;
        long mm = rt.maxMemory()/1024;
        long tm = rt.totalMemory()/1024;
        long um = tm - fm;
        System.err.println("free = "+fm+", mm = "+mm+", tm = "+tm+", used = "+um);
        System.err.println("now gc...");
        System.gc();
        fm = rt.freeMemory()/1024;
        mm = rt.maxMemory()/1024;
        tm = rt.totalMemory()/1024;
        um = tm - fm;
        System.err.println("free = "+fm+", mm = "+mm+", tm = "+tm+", used = "+um);
    }

    static Complex[] permutateVector(Complex[] vector, int a, int b) {
     //   System.err.println("permutate vector, a = "+a+" and b = "+b);
        int amask = 1 << a;
        int bmask = 1 << b;
        int dim = vector.length;
   //     System.err.println("amask = "+amask+", bmask = "+bmask);
        Complex[] answer = new Complex[dim];
        for (int i = 0; i < dim; i++) {
            int j = i;
            int x = (amask & i) /amask;
            int y = (bmask & i) /bmask;
       //     System.err.println("x = "+x+", y = "+y);
            if (x != y) {
               j ^= amask;
               j ^= bmask;
            }
        //    System.err.println("i = "+i+" and j = "+j+" and vj = "+vector[j]);
            answer[i] = vector[j];
        }
        return answer;
    }

    static Complex[] calculateNewState(List<Gate> gates, Complex[] vector, int length) {
        int dim = 1 << length;
        if (dim != vector.length) {
            throw new IllegalArgumentException ("probability vector has size "+
                    vector.length+" but we have only "+ length+" qubits.");
        }
        Complex[] result = new Complex[dim];

        Complex[][] c = calculateStepMatrix(gates, length);
        for (int i = 0; i < vector.length; i++) {
            result[i] = Complex.ZERO;
            for (int j = 0; j < vector.length; j++) {
                result[i] = result[i].add(c[i][j].mul(vector[j]));
            }
        }
        return result;
    }
}
