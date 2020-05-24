/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, 2019, Gluon Software
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

public class InvFourier extends Fourier {
    
    /**
     * Create a Fourier gate with the given size (dimensions), starting at idx
     * @param size number of qubits affected by this gate
     * @param idx the index of the first qubit in the circuit affected by this gate
     */
    public InvFourier(int size, int idx) {
        super(size, idx);
      //  super(new Block("Fourier", size), idx);
    }
    
    
    @Override
    public Complex[][] getMatrix() {
        if (matrix == null) {
            double omega = Math.PI*2/size;
            double den = Math.sqrt(size);
            matrix = new Complex[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = i; j < size; j++) {
                    double alpha = omega *i *j;
                    matrix[i][j] = new Complex(Math.cos(alpha)/den, -1*Math.sin(alpha)/den);
                }
                for (int k = 0; k < i; k++) {
                    matrix[i][k] = matrix[k][i];
                }
            }
        }
        return matrix;
    }
}
