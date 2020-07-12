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
public class BlockGate implements Gate {

    private final Block block;
    protected final int idx;
    
    /**
     * Create a block 
     * @param block
     * @param idx
     */
    public BlockGate (Block block, int idx) {
        this.block = block;
        this.idx = idx;
    }
    
    @Override
    public void setMainQubitIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMainQubitIndex() {
        return idx;
    }

    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> getAffectedQubitIndexes() {
        return IntStream.range(idx, idx+block.getNQubits()).boxed().collect(Collectors.toList());
    }

    @Override
    public int getHighestAffectedQubitIndex() {
        return block.getNQubits()+idx-1;
    }

    @Override
    public String getCaption() {
        return "B";
    }

    @Override
    public String getName() {
        return "BlockGate";
    }

    @Override
    public String getGroup() {
        return "BlockGroup";
    }

    @Override
    public Complex[][] getMatrix() {
        Complex[][] answer = block.getMatrix();
        return answer;
    }
    
    public int getSize() {
        return block.getNQubits();
    }
    
    @Override public String toString() {
        return "Gate for block "+block;
    }

    
}
