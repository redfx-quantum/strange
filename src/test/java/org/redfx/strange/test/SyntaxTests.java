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

import static org.junit.jupiter.api.Assertions.*;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.Identity;
import org.redfx.strange.gate.PermutationGate;

public class SyntaxTests {

    @Test
    public void simpleIGate() {
        Program p = new Program(1);
        Step s = new Step(new Identity(0));
        boolean gotException = false;
        try {
            s.addGate(new Hadamard(0));
        } catch (IllegalArgumentException ex) {
            gotException = true;
        }
        assertTrue(gotException);
    }
    
    //@Test
    public void addDoubleGates() {
        Program p = new Program(1);
        Step s = new Step(new Identity(0));
        s.addGate(new Identity(0));
    }

    @Test
    public void testNamedStep() {
        Step s0 = new Step("Hello!");
        Step s1 = new Step("foo", new Identity(0));
        Step s2 = new Step(new Identity(0), new Identity(1));
    }
    
    @Test
    public void permutation() {
        int dim = 4;
        Complex[][] a = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                a[i][j] = new Complex(i*dim+j,0);
            }
        }
        assertEquals(a[0][0].r , 0 );
        assertEquals(a[0][1].r , 1 );
        assertEquals(a[0][2].r , 2 );
        assertEquals(a[1][0].r , 4 );
        assertEquals(a[1][1].r , 5 );
        assertEquals(a[1][2].r , 6 );
        PermutationGate pg = new PermutationGate(0,1,2);
     //   Complex.printMatrix(pg.getMatrix());
        Complex[][] res = Complex.permutate(pg, a);
      //  Complex.printMatrix(res);
        assertEquals(res[0][0].r , 0 );
        assertEquals(res[0][1].r , 2 );
        assertEquals(res[0][2].r , 1 );
        assertEquals(res[1][0].r , 4 );
        assertEquals(res[1][1].r , 6 );
        assertEquals(res[1][2].r , 5 );
    }
}
