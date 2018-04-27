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
package com.gluonhq.strange.gate;

import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import java.util.Collections;
import java.util.List;

/**
 *
 * This class describe a Gate that operates on a single qubit only.
 * @author johan
 */
public abstract class SingleQubitGate implements Gate {
    
    private int idx;
    
    public SingleQubitGate() {}
    
    public SingleQubitGate (int idx) {
        this.idx = idx;
    }

    @Override
    public int getMainQubitIndex() {
        return this.idx;
    }
    
    @Override
    public void setMainQubit(int idx) {
        this.idx = idx;
    }
    
    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        throw new RuntimeException("A SingleQubitGate can not have additional qubits");
    }

    @Override
    public List<Integer> getAffectedQubitIndex() {
        return Collections.singletonList(idx);
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    @Override
    public String getCaption() {
        return getName();    
    }
    
    @Override
    public String getGroup() {
        return "SingleQubit";
    }
    
    public abstract Complex[][] getMatrix();
    
    @Override public String toString() {
        return "Gate with index "+idx+" and caption "+getCaption();
    }
    
}
