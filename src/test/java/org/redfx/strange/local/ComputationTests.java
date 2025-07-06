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
package org.redfx.strange.local;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.redfx.strange.Gate;
import static org.junit.jupiter.api.Assertions.*;
import org.redfx.strange.Complex;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.Identity;
import org.redfx.strange.gate.ImmediateMeasurement;


/**
 *
 * @author johan
 */
public class ComputationTests {
    
    @Test
    public void testImmediateMeasurements() {
        List<Gate> gates = new ArrayList<>();
        assertFalse(Computations.containsImmediateMeasurementOnly(gates));
        gates.add(new Identity());
        assertFalse(Computations.containsImmediateMeasurementOnly(gates));
        gates.add(new ImmediateMeasurement(0, null));
        assertTrue(Computations.containsImmediateMeasurementOnly(gates));
        gates.add(new Hadamard(1));
        assertFalse(Computations.containsImmediateMeasurementOnly(gates));
    }

    @Test
    public void testImmediateValues() {
        int choice = 2;
        int size = 4;
        Complex[] prob = new Complex[size];
        for (int i = 0; i < size; i++) {
            prob[i] = (i == choice ? Complex.ONE : Complex.ZERO);
        }
        List<Gate> gates = new ArrayList<>();
        gates.add(new ImmediateMeasurement(0, null));
        Complex[] results = Computations.doImmediateMeasurement(gates, prob, 2);
        for (int i = 0; i < size; i++) {
            Complex c = results[i];
            if (i == choice) {
                assertEquals(1,c.abssqr());
            } else {
                assertEquals(0, c.abssqr());
            }
        }
    }
    
    
    @Test
    public void testCallbackScenarios() throws InterruptedException {
        testImmediateValuesCallback(0, false);
        testImmediateValuesCallback(1, false);
        testImmediateValuesCallback(2, true);
        testImmediateValuesCallback(3, true);
    }

    @Test
    public void swapBits() {
        int a = 27;
        int v = Computations.swapBits(a, 0, 1);
        assertEquals(27, v);
        v = Computations.swapBits(a, 1, 0);
        assertEquals(27, v);
        a = 26;
        v = Computations.swapBits(a, 0, 1);
        assertEquals(25, v);
        v = Computations.swapBits(a, 1, 0);
        assertEquals(25, v);
        a = 27;
        v = Computations.swapBits(a, 2, 3);
        assertEquals(23, v);
        v = Computations.swapBits(a, 3, 2);
        assertEquals(23, v);
    }

    public void testImmediateValuesCallback(int choice, boolean exp) throws InterruptedException {
        int size = 4;
        CountDownLatch cdl = new CountDownLatch(1);
        Complex[] prob = new Complex[size];
        for (int i = 0; i < size; i++) {
            prob[i] = (i == choice ? Complex.ONE : Complex.ZERO);
        }
        List<Gate> gates = new ArrayList<>();
        gates.add(new ImmediateMeasurement(1, v -> {
            assertEquals(exp, v);
            cdl.countDown();
            
        }));
        Complex[] results = Computations.doImmediateMeasurement(gates, prob, 2);
        for (int i = 0; i < size; i++) {
            Complex c = results[i];
            if (i == choice) {
                assertEquals(1,c.abssqr());
            } else {
                assertEquals(0, c.abssqr());
            }
        }
        cdl.await(1, TimeUnit.SECONDS);
    }

}
