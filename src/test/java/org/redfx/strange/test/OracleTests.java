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
package org.redfx.strange.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Oracle;


public class OracleTests extends BaseGateTests {

    @Test
    public void createOracleMatrix() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.ONE;
        matrix[1][1] = Complex.ONE;
        matrix[0][1] = Complex.ZERO;
        matrix[1][0] = Complex.ZERO;
        Oracle oracle = new Oracle(matrix);
        assertNotNull(oracle);
    }

    // Oracle should replace null elements in matrix with Complex.ZERO
    @Test
    public void createDefaultOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.ONE;
        Oracle oracle = new Oracle(matrix);
        Complex[][] omatrix = oracle.getMatrix();
        assertNotNull(omatrix[1][1]);
    }

    @Test
    public void identityOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.ONE;
        matrix[1][1] = Complex.ONE;
        matrix[0][1] = Complex.ZERO;
        matrix[1][0] = Complex.ZERO;
        Oracle oracle = new Oracle(matrix);
        Program p = new Program(1, new Step(oracle));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
    }


    @Test
    public void notOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][1] = Complex.ONE;
        matrix[1][0] = Complex.ONE;
        Oracle oracle = new Oracle(matrix);
        Program p = new Program(1, new Step(oracle));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }
}
