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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author johan
 */
public class Block {

    List<Step> steps = new ArrayList<>();
    private final int nqubits;
    private Complex[][] matrix = null;
    private final String name;

    public Block(int size) {
        this ("anonymous", size);
    }
    public Block(String name, int size) {
        this.nqubits = size;
        this.name = name;
    }
    
    public void addStep(Step step) {
        this.steps.add(step);
        matrix = null;
    }
    
    public List<Step> getSteps() {
        return this.steps;
    }
    
    public int getNQubits() {
        return this.nqubits;
    }
    
    private void validateGate(Gate gate) {
        gate.getAffectedQubitIndexes().stream().filter((idx) -> (idx > nqubits -1)).
                forEachOrdered(item -> {
            throw new IllegalArgumentException("Can't add a gate with qubit index larger than block size");
        });
    }

    Complex[][] getMatrix() {
        if (matrix == null) {
            List<Step> simpleSteps = new ArrayList<>();
            for (Step step : steps) {
                simpleSteps.addAll(Computations.decomposeStep(step, nqubits));
            }
            Collections.reverse(simpleSteps);
            for (Step step: simpleSteps) {
                Complex[][] m = Computations.calculateStepMatrix(step.getGates(), nqubits);
                if (matrix == null) {
                    matrix = m;
                } else {
                    matrix = Complex.mmul(matrix, m);
                }
            }
        }
        return matrix;
    }
    
    @Override public String toString() {
        return "Block named "+name+" at "+super.toString();
    }
}
