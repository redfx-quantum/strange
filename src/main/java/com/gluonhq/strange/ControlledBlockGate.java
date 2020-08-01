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
package com.gluonhq.strange;

import com.gluonhq.strange.gate.PermutationGate;
import com.gluonhq.strange.local.Computations;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * A Gate describes an operation on one or more qubits.
 * @author johan
 */
public class ControlledBlockGate<T> extends BlockGate {

    private int control;
    private int size;
    private int high = -1;
    private Complex[][] matrix = null;
    
    protected ControlledBlockGate() {
    }
    
    /**
     * Create a controlled blockgate
     * @param bg the block gate
     * @param idx the start-index of the block gate
     * @param control the index of the control qubit
     */
    public ControlledBlockGate(BlockGate bg, int idx, int control) {
        this (bg.getBlock(), idx, control);
    }
    
    /**
     * Create a block 
     * @param block
     * @param idx
     * @param control the control qubit
     */
    public ControlledBlockGate (Block block, int idx, int control) {
        super(block, idx);
        this.control = control;
    }

    @Override
    public int getMainQubitIndex() {
        System.err.println("GET MQI asked, return "+control);
        return control;
    }

    @Override
    public List<Integer> getAffectedQubitIndexes() {
        ArrayList answer = new ArrayList(super.getAffectedQubitIndexes());
        System.err.println("AQI asked for "+this+" currently "+answer+" add ctrl at "+control);
        answer.add(control);
        return answer;
    }

    @Override
    public int getHighestAffectedQubitIndex() {
        if (high < 0) getMatrix();
        System.err.println("GetHighestAffectedQI asked, return "+this.high);
        return this.high;
    }

    @Override
    public String getCaption() {
        return "CB";
    }

    @Override
    public String getName() {
        return "CBlockGate";
    }

    @Override
    public String getGroup() {
        return "CBlockGroup";
    }

    @Override
    public Complex[][] getMatrix() {
        System.err.println("GetMatrix asked for "+this);
        if (matrix == null) {
            System.err.println("Matrix is null, calculate now");
            int low = 0;
            this.high = control;
            this.size = super.getSize() + 1;
            int gap = control - idx;
            List<PermutationGate> perm = new LinkedList<>();
            int bs = block.getNQubits();
            System.err.println("control = " + control + ", idx = " + idx + ", gap = " + gap + " and bs = " + bs);
                         printMemory();

            if (control > idx) {
                if (gap < bs) {
                    throw new IllegalArgumentException("Can't have control at " + control + " for gate with size " + bs + " starting at " + idx);
                }
                if (gap > bs) {
                    low = idx;
                    high = control;
                    size = high - low + 1;
                    System.err.println("PG, control = " + control + ", gap = " + gap + ", bs = " + bs);
                    PermutationGate pg = new PermutationGate(control-low, control-low - gap + bs, size);
                    perm.add(pg);
                }
            } else {
                low = control;
                high = idx + bs - 1;
                size = high - low + 1;
                for (int i = 0; i < size - 1; i++) {
                    System.err.println("ADD PG "+i);
                                    printMemory();

                    PermutationGate pg = new PermutationGate(i, i + 1, size);
                    perm.add(0,pg);
                                    printMemory();
                                    System.err.println("ADDED PG "+i);

                }
            }
            System.err.println("GetMatrix called for CBG, perm = "+perm);
            //Complex[][] part = super.getMatrix();
            Complex[][] part = block.getMatrix();
            System.err.println("PART matrix? ");
             Complex.printMatrix(part);
        //    System.err.println("include this now ");
            int dim = part.length;
            matrix = Computations.createIdentity(2 * dim);
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    matrix[i + dim][j + dim] = part[i][j];
                }
            }
            System.err.println("before tensor1, matrix = ");
            Complex.printMatrix(matrix);
            if (gap > bs) {
                matrix = Complex.tensor(Computations.createIdentity(1 << (gap - bs)), matrix);
            }
            if ((gap < 0) && (-1 * gap > 1)) {
                matrix = Complex.tensor(matrix,Computations.createIdentity(1 << (-1 * gap -1 )));
            }
            System.err.println("before perm, matrix = ");
            Complex.printMatrix(matrix);
            for (PermutationGate pg : perm) {
                printMemory();
                System.err.println("PRE/POST perm : "+pg);
                Complex.printMatrix(pg.getMatrix());
                

              //  matrix = Complex.mmul(pg.getMatrix(), matrix);
                
                matrix = Complex.permutate(pg, matrix);

                matrix = Complex.permutate(matrix, pg);
                                
             //   matrix = Complex.mmul(matrix, pg.getMatrix());
                System.err.println("after perm, matrix = ");
                Complex.printMatrix(pg.getMatrix());

            }

            System.err.println("CBG matrix: ");
            Complex.printMatrix(matrix);
        } else {
            System.err.println("Matrix was cached");
        }
        return matrix;
    }

    public int getSize() {
        return size;
    }
    
    @Override public String toString() {
        return "CGate for "+super.toString();
    }

    void printMemory() {
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
    
}
