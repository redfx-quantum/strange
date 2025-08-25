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
package org.redfx.strange.gate;

import org.redfx.strange.Complex;
import org.redfx.strange.QuantumExecutionEnvironment;

/**
 * <p>InvFourier class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class InvFourier extends Fourier {
    
    /**
     * Create a Fourier gate with the given size (dimensions), starting at idx
     *
     * @param size number of qubits affected by this gate
     * @param idx the index of the first qubit in the circuit affected by this gate
     */
    public InvFourier(int size, int idx) {
        super("InvFourier", size, idx);
        this.inverse();
    }
//    
//    /** {@inheritDoc} */
//    @Override
//    public Complex[][] getMatrix() {
//        return getMatrix(null);
//    }
//    
//    /** {@inheritDoc} */
//    @Override
//    public Complex[][] getMatrix(QuantumExecutionEnvironment eqq) {
//        if (matrix == null) {
//            double omega = Math.PI*2/size;
//            double den = Math.sqrt(size);
//            matrix = new Complex[size][size];
//            System.err.println("[INV] CREATED "+matrix);
//            for (int i = 0; i < size; i++) {
//                for (int j = i; j < size; j++) {
//                    double alpha = omega *i *j;
//                    int tpd = (int)(alpha/(Math.PI * 2));
//                    if (tpd > 0) {
//                        alpha = alpha - (Math.PI*2 * tpd);
//                    }
//                    double ar = Math.cos(alpha);
//                    double ai = Math.sin(alpha);
//                    if (Math.abs(alpha) < 1e-6) {
//                        ar = 1;
//                        ai = 0;
//                    }else if (Math.abs(Math.PI - alpha) < 1e-6) {
//                        ar = -1;
//                        ai = 0;
//                    } else if (Math.abs(Math.PI / 2 - alpha) < 1e-6) {
//                        ar = 0;
//                        ai = 1;
//                    } else if (Math.abs(3 * Math.PI / 2 - alpha) < 1e-6) {
//                        ar = 0;
//                        ai = -1;
//                    }
//                    matrix[i][j] = new Complex(ar/den, -1*ai/den);
//                }
//                for (int k = 0; k < i; k++) {
//                    matrix[i][k] = matrix[k][i];
//                }
//            }
//        }
//        System.err.println("INVFOURMATRIX = ");
//        Complex.printMatrix(matrix, System.err);
//        return matrix;
//    }
//    
//    /** {@inheritDoc} */
//    @Override
//    public boolean hasOptimization() {
//        return true;
//    }
}
