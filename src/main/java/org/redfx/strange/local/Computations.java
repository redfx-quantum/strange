/*-
 * #%L
 * Strange
 * %%
 * Copyright (C) 2020 Johan Vos
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Johan Vos nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.redfx.strange.local;

import org.redfx.strange.*;
import org.redfx.strange.gate.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.redfx.strange.Complex.tensor;

/**
 * <p>Computations class.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public class Computations {

    private static final boolean debug = false;

    static final Logger LOG = Logger.getLogger(Computations.class.getName());
    static void dbg(String s) {
        SimpleQuantumExecutionEnvironment.LOG.finer(s);
    }

    /**
     * <p>calculateStepMatrix.</p>
     *
     * @param gates a {@link java.util.List} object
     * @param nQubits a int
     * @param qee a {@link org.redfx.strange.QuantumExecutionEnvironment} object
     * @return an array of {@link org.redfx.strange.Complex} objects
     */
    public static Complex[][] calculateStepMatrix(List<Gate> gates, int nQubits, QuantumExecutionEnvironment qee) {
        long l0 = System.currentTimeMillis();
        Complex[][] a = new Complex[1][1];
        a[0][0] = Complex.ONE;
        int idx = nQubits - 1;
        while (idx >= 0) {
            final int cnt = idx;
            Gate myGate = gates.stream()
                    .filter(
                            // gate -> gate.getAffectedQubitIndex().contains(cnt)
                            gate -> gate.getHighestAffectedQubitIndex() == cnt)
                    .findFirst()
                    .orElse(new Identity(idx));
            dbg("stepmatrix, cnt = " + cnt + ", idx = " + idx + ", myGate = " + myGate);
            if (myGate instanceof BlockGate) {
                dbg("calculateStepMatrix for blockgate " + myGate + " of class " + myGate.getClass());
                BlockGate sqg = (BlockGate) myGate;
                a = tensor(a, sqg.getMatrix(qee));
                dbg("calculateStepMatrix for blockgate calculated " + myGate);

                idx = idx - sqg.getSize() + 1;
            }
            if (myGate instanceof SingleQubitGate) {
                SingleQubitGate sqg = (SingleQubitGate) myGate;
                a = tensor(a, sqg.getMatrix());
            }
            if (myGate instanceof TwoQubitGate) {
                TwoQubitGate tqg = (TwoQubitGate) myGate;
                a = tensor(a, tqg.getMatrix());
                idx--;
            }
            if (myGate instanceof ThreeQubitGate) {
                ThreeQubitGate tqg = (ThreeQubitGate) myGate;
                a = tensor(a, tqg.getMatrix());
                idx = idx - 2;
            }
            if (myGate instanceof PermutationGate) {
                throw new RuntimeException("No perm allowed ");
            }
            if (myGate instanceof Oracle) {
                a = myGate.getMatrix();
                idx = 0;
            }
            idx--;
        }
        long l1 = System.currentTimeMillis();
        return a;
    }

    /**
     * decompose a Step into steps that can be processed without permutations
     *
     * @param s a {@link org.redfx.strange.Step} object
     * @param nqubit a int
     * @return a {@link java.util.List} object
     */
    public static List<Step> decomposeStep(Step s, int nqubit) {
        ArrayList<Step> answer = new ArrayList<>();
        answer.add(s);
        if (s.getType() == Step.Type.PSEUDO) {
            s.setComplexStep(s.getIndex());
            return answer;
        }

        List<Gate> gates = s.getGates();

        if (gates.isEmpty()) {
            return answer;
        }
        boolean simple = gates.stream().allMatch(g -> g instanceof SingleQubitGate);
        if (simple) {
            return answer;
        }
        Gate targetGate = gates.get(0);
        // if only 1 gate, which is an oracle, return as well
        if ((gates.size() == 1) && (targetGate instanceof Oracle || targetGate instanceof Swap)) {
            return answer;
        }
        // at least one non-singlequbitgate
        List<Gate> firstGates = new ArrayList<>();
        for (Gate gate : gates) {
            if (gate.getHighestAffectedQubitIndex() > nqubit) {
                throw new IllegalArgumentException("Only "+nqubit+" qubits available while Gate "+gate+" requires qubit "+gate.getHighestAffectedQubitIndex());
            }
            if (gate instanceof ProbabilitiesGate) {
                s.setInformalStep(true);
                return answer;
            }
            if (gate instanceof BlockGate) {
                if (gate instanceof ControlledBlockGate) {
                    processBlockGate((ControlledBlockGate) gate, answer);
                }
                firstGates.add(gate);
            } else if (gate instanceof SingleQubitGate) {
                firstGates.add(gate);
            } else if (gate instanceof ControlledGate cgate) {
                firstGates.add(gate);
            } else if (gate instanceof TwoQubitGate) {
                TwoQubitGate tqg = (TwoQubitGate) gate;
                int first = tqg.getMainQubitIndex();
                int second = tqg.getSecondQubitIndex();
                if ((first >= nqubit) || (second >= nqubit)) {
                    throw new IllegalArgumentException("Step " + s + " uses a gate with invalid index " + first + " or " + second);
                }
                if (first == second + 1) {
                    firstGates.add(gate);
                } else {
                    if (first == second) {
                        throw new RuntimeException("Wrong gate, first == second for " + gate);
                    }
                    if (first > second) {
                        PermutationGate pg = new PermutationGate(first - 1, second, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(0, prePermutation);
                        answer.add(postPermutation);
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                    } else {
                        PermutationGate pg = new PermutationGate(first, second, nqubit);
                        Step prePermutation = new Step(pg);
                        Step prePermutationInv = new Step(pg);
                        int realStep = s.getIndex();
                        s.setComplexStep(-1);
                        answer.add(0, prePermutation);
                        answer.add(prePermutationInv);
                        Step postPermutation = new Step();
                        Step postPermutationInv = new Step();
                        if (first != second - 1) {
                            PermutationGate pg2 = new PermutationGate(second - 1, first, nqubit);
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
                        answer.add(answer.size() - p0idx, postPermutation);
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
                    if (sSecond != sFirst - 1) {
                        PermutationGate pg = new PermutationGate(sFirst - 1, sSecond, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(p0idx, prePermutation);
                        answer.add(answer.size() - p0idx, postPermutation);
                        p0idx++;
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                        sSecond = sFirst - 1;
                    }
                    if (sThird != sFirst - 2) {
                        PermutationGate pg = new PermutationGate(sFirst - 2, sThird, nqubit);
                        Step prePermutation = new Step(pg);
                        Step postPermutation = new Step(pg);
                        answer.add(p0idx, prePermutation);
                        answer.add(answer.size() - p0idx, postPermutation);
                        p0idx++;
                        postPermutation.setComplexStep(s.getIndex());
                        s.setComplexStep(-1);
                        sThird = sFirst - 2;
                    }
                }
            } else {
                throw new RuntimeException("Gate must be SingleQubit or TwoQubit");
            }
        }
        return answer;
    }

    /**
     * <p>printMatrix.</p>
     *
     * @param a an array of {@link org.redfx.strange.Complex} objects
     */
    public static void printMatrix(Complex[][] a) {
        for (int i = 0; i < a.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < a[i].length; j++) {
                sb.append(a[i][j]).append("    ");
            }
            System.out.println("m[" + i + "]: " + sb);
        }
    }

    /**
     * <p>getInverseModulus.</p>
     *
     * @param a a int
     * @param b a int
     * @return a int
     */
    public static int getInverseModulus(int a, int b) {
        int r0 = a;
        int r1 = b;
        int r2 = 0;
        int s0 = 1;
        int s1 = 0;
        int s2 = 0;
        while (r1 != 1) {
            int q = r0 / r1;
            r2 = r0 % r1;
            s2 = s0 - q * s1;
            r0 = r1;
            r1 = r2;
            s0 = s1;
            s1 = s2;
        }
        return s1 > 0 ? s1 : s1 + b;
    }

    /**
     * <p>gcd.</p>
     *
     * @param a a int
     * @param b a int
     * @return a int
     */
    public static int gcd(int a, int b) {
        int x = a > b ? a : b;
        int y = x == a ? b : a;
        int z = 0;
        while (y != 0) {
            z = x % y;
            x = y;
            y = z;
        }
        return x;
    }

    /**
     * <p>fraction.</p>
     *
     * @param p a int
     * @param max a int
     * @return a int
     */
    public static int fraction(int p, int max) {
        int length = (int) Math.ceil(Math.log(max) / Math.log(2));
        int offset = length;
        int dim = 1 << offset;
        double r = (double) p / dim + .000001;
        int period = Computations.fraction(r, max);
        return period;
    }

    /**
     * <p>fraction.</p>
     *
     * @param d a double
     * @param max a int
     * @return a int
     */
    public static int fraction(double d, int max) {
        double EPS = 1e-15;
        int answer = -1;
        int h = 0;
        int k = -1;
        int a = (int) d;
        double r = d - a;
        int h_2 = 0;
        int h_1 = 1;
        int k_2 = 1;
        int k_1 = 0;
        while ((k < max) && (r > EPS)) {
            h = a * h_1 + h_2;
            k = a * k_1 + k_2;
            h_2 = h_1;
            h_1 = h;
            k_2 = k_1;
            k_1 = k;
            double rec = 1 / r;
            a = (int) rec;
            r = rec - a;
        }
        return k_2;
    }

    /**
     * <p>createIdentity.</p>
     *
     * @param dim a int
     * @return an array of {@link org.redfx.strange.Complex} objects
     */
    public static Complex[][] createIdentity(int dim) {
        Complex[][] matrix = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                matrix[i][j] = (i == j) ? Complex.ONE : Complex.ZERO;
            }
        }
        return matrix;
    }

    /**
     * <p>printMemory.</p>
     */
    public static void printMemory() {
        if (!debug) {
            return;
        }
        Runtime rt = Runtime.getRuntime();
        long fm = rt.freeMemory() / 1024;
        long mm = rt.maxMemory() / 1024;
        long tm = rt.totalMemory() / 1024;
        long um = tm - fm;
        System.err.println("free = " + fm + ", mm = " + mm + ", tm = " + tm + ", used = " + um);
        /*
        System.err.println("now gc...");
        System.gc();
        fm = rt.freeMemory()/1024;
        mm = rt.maxMemory()/1024;
        tm = rt.totalMemory()/1024;
        um = tm - fm;
        System.err.println("free = "+fm+", mm = "+mm+", tm = "+tm+", used = "+um);
         */
    }

    /**
     * <p>permutateVector.</p>
     *
     * @param vector an array of {@link org.redfx.strange.Complex} objects
     * @param a a int
     * @param b a int
     * @return an array of {@link org.redfx.strange.Complex} objects
     */
    public static Complex[] permutateVector(Complex[] vector, int a, int b) {
        int amask = 1 << a;
        int bmask = 1 << b;
        if ((amask >= vector.length) || (bmask >= vector.length)) {
            throw new IllegalArgumentException("Can not permutate element "+a+" and "+b+" of vector sized "+vector.length);
        }
        int dim = vector.length;
        Complex[] answer = new Complex[dim];
        for (int i = 0; i < dim; i++) {
            int j = i;
            int x = (amask & i) / amask;
            int y = (bmask & i) / bmask;
            if (x != y) {
                j ^= amask;
                j ^= bmask;
            }
            answer[i] = vector[j];
        }
        return answer;
    }

    static int nested = 0; // allows us to e.g. show only 2 nested steps

    /**
     * <p>calculateNewState.</p>
     *
     * @param gates a {@link java.util.List} object
     * @param vector an array of {@link org.redfx.strange.Complex} objects
     * @param length a int
     * @return an array of {@link org.redfx.strange.Complex} objects
     */
    public static Complex[] calculateNewState(List<Gate> gates, Complex[] vector, int length) {
        if (containsImmediateMeasurementOnly(gates)) {
            return doImmediateMeasurement(gates, vector, length);
        }
        nested++;
        System.err.println("[CNS0] nested = "+nested);
   //     System.err.println("[CNS1] start = "+Arrays.toString(vector));
        Complex[] answer = getNextProbability(getAllGates(gates, length), vector);
     //   System.err.println("[CNS1] answer = "+Arrays.toString(answer));
        nested--;
        return answer;
    }

    private static Complex[] getNextProbability(List<Gate> gates, Complex[] v) {
    //    LOG.info("GNP, gates = "+gates+", v = " + Arrays.toString(v));
        Complex[] answer = new Complex[v.length];
        boolean onlyIdentity = (gates.size() == 1 || gates.subList(1, gates.size()).stream().allMatch(g -> g instanceof Identity));
        boolean simple = gates.stream().allMatch(g -> ((g instanceof Identity) || (g instanceof SingleQubitGate) || (g instanceof ControlledGate)));
        if (onlyIdentity && gates.get(0) instanceof Swap swap) {
            return processSwapGate(swap, v);
        }
        if (simple) {
            return processSimpleGates(gates, v);
        }
        answer = getNextProbability2(gates, v);
  //      LOG.info("GNP calculated for "+ gates);
        return answer;
    }

    static Complex[] processSimpleGates(List<Gate> gates, Complex[] v) {
        Complex[] w = v;
        for (Gate gate: gates) {
            if (!(gate instanceof Identity)) {
                w = processNQubitGate(gate, w);
            }
        }
        return w;
    }

    public static Complex[] processNQubitGate(Gate gate, Complex[] v) {
        int size = v.length;
        Complex[] answer = new Complex[size];
        System.arraycopy(v, 0, answer, 0, size);
   //     System.err.println("before pnqg, v = "+Arrays.toString(v));
        int index = gate.getMainQubitIndex();
        int gateDim = 1 << gate.getSize();
        System.err.println("GATEDIM = "+gateDim+" and gate = "+gate);
        int length = (int) Math.ceil(Math.log(size) / Math.log(2));
        int ngroups = 1 << (length - index - 1);
        int qdelta = 1 << index;
        System.err.println("length = "+length+", ng = "+ngroups+", index = "+index+", qd = "+qdelta);
        Gate rootGate = gate;
        List<Integer> ctrlIdx = null;
        if (gate instanceof ControlledGate cgate) {
            rootGate = cgate.getRootGate();
            ctrlIdx = cgate.getControlIndexes();
        }
        boolean ctrl = ctrlIdx != null;
        System.err.println("ctrl = "+ctrl);
        Complex[][] matrix = rootGate.getMatrix();
        System.err.println("MATRIX = ");
   //     Complex.printMatrix(matrix, System.err);
        for (int group = 0; group < ngroups; group++) {
            for (int j = 2 * group * qdelta; j < (2 * group + 1) * qdelta; j++) {
                if (ctrl && (shouldSkip(j, ctrlIdx))) {
                    continue;
                }
                Complex[] work = new Complex[2];
                Complex[] tmp = new Complex[2];
                tmp[0] = new Complex(Complex.ZERO);
                tmp[1] = new Complex(Complex.ZERO);
                work[0] = v[j];
                work[1] = v[j + qdelta];
                for (int i = 0; i < gateDim; i++) {
                    for (int k = 0; k < gateDim; k++) {
                        tmp[i] = tmp[i].addr(matrix[i][k].mul(work[k]));
                    }
                }
                answer[j] = new Complex(tmp[0].r, tmp[0].i);
                answer[j + qdelta] = new Complex(tmp[1].r, tmp[1].i);
            }
        }
        
      //  System.err.println("after pnqg, v = "+Arrays.toString(answer));
        return answer;
    }

    static boolean shouldSkip(int target, List<Integer> ctrlIdxs) {
    //    System.err.println("shouldskip "+target+" with ctrlidx "+ctrlIdxs);
        int size = ctrlIdxs.size();
        if (size == 0) return false;
        int idx = 0;
        while(idx < size) {
            if (hasZeroBit(target, ctrlIdxs.get(idx))) {
//                System.err.println("YES");
                return true;
            }
            idx++;
        }
        return false;
    }

    /**
     * Check if bit at position b has value 1 in the integer a
     * @param a
     * @param b
     * @return
     */
    public static boolean hasZeroBit(int a, int b) {
       if (b < 0) return false;
        int res = a & (1 << b);
        return (res == 0);
    }

    public static Complex[] processSwapGate(Swap swap, Complex[] v) {
        Complex[] result = new Complex[v.length];
        int b0 = swap.getMainQubitIndex();
        int b1 = swap.getSecondQubitIndex();
        for (int i = 0 ; i < v.length; i++) {
            result[i] = v[swapBits(i, b0, b1)];
        }
        return result;
    }

    private static Complex[] getNextProbability2(List<Gate> gates, Complex[] v) {
        LOG.info("start gnp, #gates = "+gates.size());
        Gate gate = gates.get(0);
        int nqubits = gate.getSize();
        int gatedim = 1 << nqubits;
        int size = v.length;
     dbg("GETNEXTPROBABILITY asked for size = " + size + " and gates = " + gates+", gatedim = "+gatedim);
        if (gates.size() > 1) {

            int partdim = size / gatedim;
            Complex[] answer = new Complex[size];
            List<Gate> nextGates = gates.subList(1, gates.size());
            boolean id = true;
            for (Gate g : nextGates) {
                id = id && (g instanceof Identity);
            }
            if (id) {
                dbg("ONLY IDENTITY!! partdim = "+partdim);
                long s0 = System.currentTimeMillis();
                long s1 = s0;
                for (int j = 0; j < partdim; j++) {
                    dbg("do part "+j+" from "+partdim);
                    Complex[] oldv = new Complex[gatedim];
                    Complex[] newv = new Complex[gatedim];
                    for (int i = 0; i < gatedim; i++) {
                        oldv[i] = v[i * partdim + j];
                        newv[i] = Complex.ZERO;
                    }

                    if (gate.hasOptimization()) {
                        dbg("OPTPART!");
                        newv = gate.applyOptimize(oldv);
                    } else {
                        dbg("GET MATRIX for  "+gate);
                        Complex[][] matrix = gate.getMatrix();
                        s1 = System.currentTimeMillis();
                        for (int i = 0; i < gatedim; i++) {
                            for (int k = 0; k < gatedim; k++) {
                                newv[i] = newv[i].add(matrix[i][k].mul(oldv[k]));
                            }
                        }
                    }
                    for (int i = 0; i < gatedim; i++) {
                        answer[i * partdim + j] = newv[i];
                    }
                    dbg("done part");
                }
                long s2 = System.currentTimeMillis();
                LOG.info("Early return1");
                return answer;
            }
            long sm0 = System.currentTimeMillis();
            Complex[][] vsub = new Complex[gatedim][partdim];
            for (int i = 0; i < gatedim; i++) {
                Complex[] vorig = new Complex[partdim];
                for (int j = 0; j < partdim; j++) {
                    vorig[j] = v[j + i * partdim];
                }
                vsub[i] = getNextProbability(nextGates, vorig);
            }
            long s0 = System.currentTimeMillis();
            Complex[][] matrix = gate.getMatrix();
            long s1 = System.currentTimeMillis();
            for (int i = 0; i < gatedim; i++) {
                for (int j = 0; j < partdim; j++) {
                    answer[j + i * partdim] = Complex.ZERO;
                    for (int k = 0; k < gatedim; k++) {
                        answer[j + i * partdim] = answer[j + i * partdim].add(matrix[i][k].mul(vsub[k][j]));
                    }
                }
            }
            long s2 = System.currentTimeMillis();
            LOG.info("Early return2");
            return answer;
        } else {
            if (gatedim != size) {
                System.err.println("problem with matrix for gate " + gate);
                throw new IllegalArgumentException("wrong matrix size " + gatedim + " vs vector size " + v.length);
            }
            if (gate.hasOptimization()) {
                LOG.info("Apply opt for gate "+gate);
                Complex[] a = gate.applyOptimize(v);
                LOG.info("Early return3");

                return a;
            } else {
                LOG.info("No opt for gate "+gate);
                Complex[][] matrix = gate.getMatrix();
                Complex[] answer = new Complex[size];
                for (int i = 0; i < size; i++) {
                    answer[i] = Complex.ZERO;
                    for (int j = 0; j < size; j++) {
                        answer[i] = answer[i].add(matrix[i][j].mul(v[j]));
                    }
                }
                LOG.info("Early return4");
                return answer;
            }
        }
    }
    
    /**
     * Check if the gates operates on qubits that are part of this Program.
     * e.g. if a 3-sized gate is applied to qubit 2 in a 3-qubit circuit, this
     * will throw an IllegalArgumentException.
     */
    private static void validateGates(List<Gate> gates, int nQubits) {
        for (Gate gate : gates) {
            if (gate.getHighestAffectedQubitIndex() >= nQubits) {
                throw new IllegalArgumentException 
        ("Gate "+gate+" operates on qubit "+ gate.getHighestAffectedQubitIndex()+" but we have only "+nQubits+" qubits.");
            }
        }
    }

    private static List<Gate> getAllGates(List<Gate> gates, int nQubits) {
        validateGates(gates, nQubits);
        dbg("getAllGates, orig = " + gates);
        List<Gate> answer = new ArrayList<>();
        int idx = nQubits - 1;
        while (idx >= 0) {
            final int cnt = idx;
            Gate myGate = gates.stream()
                    .filter(
                            gate -> gate.getHighestAffectedQubitIndex() == cnt)
                    .findFirst()
                    .orElse(new Identity(idx));
            dbg("stepmatrix, cnt = " + cnt + ", idx = " + idx + ", myGate = " + myGate);
            answer.add(myGate);
            if (myGate instanceof BlockGate) {
                BlockGate sqg = (BlockGate) myGate;
                idx = idx - sqg.getSize() + 1;
                dbg("processed blockgate, size = " + sqg.getSize() + ", idx = " + idx);
            }
            if (myGate instanceof TwoQubitGate) {
                idx--;
            }
            if (myGate instanceof ThreeQubitGate) {
                idx = idx - 2;
            }
            if (myGate instanceof PermutationGate) {
                throw new RuntimeException("No perm allowed ");
            }
            if (myGate instanceof Oracle) {
                idx = 0;
            }
            idx--;
        }
        return answer;
    }

    private static void processBlockGate(ControlledBlockGate gate, ArrayList<Step> answer) {
        LOG.info("PBG");
        Step master = answer.get(answer.size() -1);
        gate.calculateHighLow();
        int low = gate.getLow();
        int control = gate.getControlQubit();
        int idx = gate.getMainQubitIndex();
        int high = control;
        int size = gate.getSize();
        int gap = control - idx;
        List<PermutationGate> perm = new LinkedList<>();
        Block block = gate.getBlock();
        int bs = block.getNQubits();

        if (control > idx) {
            if (gap < bs) {
                throw new IllegalArgumentException("Can't have control at " + control + " for gate with size " + bs + " starting at " + idx);
            }
            low = idx;
            if (gap > bs) {
                high = control;
                size = high - low + 1;
                PermutationGate pg = new PermutationGate(control, control - gap + bs, low + size);

                perm.add(pg);
            }
        } else {
            low = control;
            high = idx + bs - 1;
            size = high - low + 1;
            //   gate.correctHigh(low+bs);
            for (int i = low; i < low + size - 1; i++) {
                PermutationGate pg = new PermutationGate(i, i + 1, low + size);
                perm.add(0, pg);
            }
        }

        for (int i = 0; i < perm.size(); i++) {
            PermutationGate pg = perm.get(i);
            Step lpg = new Step(pg);
            if (i < perm.size()-1) {
                lpg.setComplexStep(-1);
            } else {
                // the complex step should be the last part of the step
                lpg.setComplexStep(master.getComplexStep());
                master.setComplexStep(-1);
            }
            answer.add(lpg);
            answer.add(0, new Step(pg));
        }
LOG.info("Result of block = "+answer);
    }
    
    // TODO: make this a utility method
    /**
     * <p>calculateQubitStatesFromVector.</p>
     *
     * @param vectorresult an array of {@link org.redfx.strange.Complex} objects
     * @return an array of doubles
     */
    public static double[] calculateQubitStatesFromVector(Complex[] vectorresult) {
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

    /**
     * Evaluates the first ImmediateMeasurement gate in the supplied list.
     * A measurement is done based on existing probabilities, and the resulting vector is
     * post-conditioned for this value. In case a callback is provided when creating the
     * ImmediateMeasurement gate, that will be called with the resulted measurement.
     * @param gates
     * @param vector
     * @param length
     * @return 
     */
    static Complex[] doImmediateMeasurement(List<Gate> gates, Complex[] vector, int length) {
        int size = vector.length;
        Gate gate = gates.stream().filter(g -> g instanceof ImmediateMeasurement).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Need an ImmediateMeasurement gate"));
        ImmediateMeasurement mGate = (ImmediateMeasurement)gate;
        int idx = mGate.getMainQubitIndex();
        double[] p = new double[2];
        for (int i = 0; i < size; i++) {
            p[(i/(1 <<idx))%2] += vector[i].abssqr();
        }
        double rnd = Math.random();
        int pick = rnd > p[0] ? 1 : 0;
        Complex[] answer = new Complex[size];
        for (int i = 0; i < size; i++) {
            if (pick == (i/(1 <<idx))%2) {
                answer[i] = vector[i].mul(1/Math.sqrt(p[pick]));
            } else {
                answer[i] = Complex.ZERO;
            }
        }
        Consumer<Boolean> consumer = mGate.getConsumer();
        if (consumer != null) consumer.accept(pick != 0);
        return answer;
    }

    /**
     * Checks if the list of gates contain one or more ImmediateMeasurement gates
     * and no other gates apart from the Identity gate
     * @param gates
     * @return 
     */
    static boolean containsImmediateMeasurementOnly(List<Gate> gates) {
        return gates.stream().anyMatch(g -> g instanceof ImmediateMeasurement) &&  
               gates.stream().allMatch(g -> g instanceof ImmediateMeasurement || g instanceof Identity);
    }

    /**
     * Swap the value of bits b0 and b1 in the value val.
     * @param val an integer value, containing 32 bits
     * @param b0 position of bit 0
     * @param b1 position of bit 1
     * @return a new integer value with bits 0 and 1 swapped
     */
    public static int swapBits(int val, int b0, int b1) {
        int bit0Val = (val >> b0) & 1;
        int bit1Val = (val >> b1) & 1;
        if (bit0Val != bit1Val) {
            int bitMask = (1 << b0) | (1 << b1);
            val ^= bitMask;
        }
        return val;
    }

}
