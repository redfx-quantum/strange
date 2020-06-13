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

import com.gluonhq.strange.local.Computations;
import java.util.ArrayList;
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
        assert(idx == control+1);
    }

    @Override
    public int getMainQubitIndex() {
        return control;
    }

    @Override
    public List<Integer> getAffectedQubitIndexes() {
        ArrayList answer = new ArrayList(super.getAffectedQubitIndexes());
        answer.add(control);
        return answer;
    }

    @Override
    public int getHighestAffectedQubitIndex() {
        return super.getHighestAffectedQubitIndex();
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
        Complex[][] part = super.getMatrix();
        int dim = part.length;
        Complex[][] answer = Computations.createIdentity(2 * dim);
       // Complex[][] answer = new Complex[dim+2][dim+2];
        for (int i = 0; i < dim; i++) {
//            answer[i][0] = Complex.ZERO;
//            answer[i][1] = Complex.ZERO;
//            answer[0][i] = Complex.ZERO;
//            answer[1][i] = Complex.ZERO;
            for (int j = 0; j < dim; j++) {
                answer[i+dim][j+dim] = part[i][j];
            }
        }
//        answer[0][dim] = Complex.ZERO;
//        answer[0][dim+1] = Complex.ZERO;
//        answer[1][dim] = Complex.ZERO;
//        answer[1][dim+1] = Complex.ZERO;
//        answer[dim][0] = Complex.ZERO;
//        answer[dim+1][0] = Complex.ZERO;
//        answer[dim][1] = Complex.ZERO;
//        answer[dim+1][1] = Complex.ZERO;
//        answer[0][0] = Complex.ONE;
//        answer[1][1] = Complex.ONE;
System.err.println("CBG matrix: ");
        Complex.printMatrix(answer);
        return answer;
    }
    
    public int getSize() {
        return super.getSize()+1;
    }
    
    @Override public String toString() {
        return "CGate for "+super.toString();
    }

    
}
