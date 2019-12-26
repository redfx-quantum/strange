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

import com.gluonhq.strange.Gate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * This class describe a Gate that operates on three qubits. In a single
 * <code>Step</code>, there should not be two Gates that act on the same qubit.
 * @author johan
 */
public abstract class ThreeQubitGate implements Gate {
    
    private int first;
    private int second;
    private int third;
    
    public ThreeQubitGate() {}
    
    public ThreeQubitGate (int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public void setMainQubitIndex(int idx) {
        this.first = idx;
    }
    
    @Override
    public int getMainQubitIndex() {
        return this.first;
    }
    
    @Override
    public void setAdditionalQubit(int idx, int cnt) {
        if ((cnt < 1) || (cnt > 2)) { 
            System.err.println("Can't set qubit "+cnt+" as additional on a three qubit gate");
        }
        if (cnt == 1) this.second = idx;
        if (cnt == 2) this.third = idx;
    }

    public int getMainQubit() {
        return this.first;
    }
    
    public int getSecondQubit() {
        return this.second;
    }
    
    public int getThirdQubit() {
        return this.third;
    }
        
    @Override
    public List<Integer> getAffectedQubitIndexes() {
        return Arrays.asList(first, second, third);
    }

    @Override
    public int getHighestAffectedQubitIndex() {
        return Collections.max(getAffectedQubitIndexes());
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
        return "ThreeQubit";
    }
    
    
    @Override public String toString() {
        return "Gate acting on qubits "+first+", "+second+" and "+third+" and caption "+getCaption();
    }
    
}
