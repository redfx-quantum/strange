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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * A single Step in a quantum <code>Program</code>. In a step, a number
 * of Gates can be added, involving a number of qubits. However, in a single
 * step a qubit can be involved in at most one <code>Gate</code>. It is illegal
 * to declare 2 gates in a single step that operate on the same qubit.
 * @author johan
 */
public class Step {
    
    private final ArrayList<Gate> gates = new ArrayList<>();
    private int index;
    
    private int complexStep = -1; // if a complex step needs to broken into
    // simple steps, only one simple step can have this value to be the index of the complex step
    
    public void addGate(Gate g) {
        gates.add(g);
    }
    
    public List<Gate> getGates() {
        return gates;
    }
    
    public void setComplexStep(int idx) {
        this.complexStep = idx;
    }
    
    public int getComplexStep() {
        return this.complexStep;
    }

    public void setIndex(int s) {
        this.index = s;
        this.complexStep = s;
    }
    
    public int getIndex() {
        return this.index;
    }
    
}
