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
package org.redfx.strange;

import java.util.function.Consumer;

/**
 * <p>QuantumExecutionEnvironment interface.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public interface QuantumExecutionEnvironment {

    /**
     * Execute the Program <code>p</code>.
     * As a result of this method, a <code>Result</code> object is created
     * containing the probability vector for the total system. This probability
     * vector is well defined: every invocation of this method with the same
     * <code>Program p</code> will return a <code>Result</code> instance that has
     * the same probability vector.
     * The <code>Result</code> also contains a list of Qubits, each with a measuredValue.
     * Every invocation of this method, or of the <code>Result.measureSystem</code> method
     * may result in different values for <code>measuredValue</code> for the qubits.
     *
     * @param p the provided <code>Program</code> to be executed.
     * @return the <code>Result</code> instance containing the probability vector for
     * the system, and a measurement for every qubit.
     */
    Result runProgram(Program p);

    /**
     * <p>runProgram.</p>
     *
     * @param p a {@link org.redfx.strange.Program} object
     * @param result a {@link java.util.function.Consumer} object
     */
    void runProgram (Program p, Consumer<Result> result);
    
    /**
     * <p>mmul.</p>
     *
     * @param a an array of {@link org.redfx.strange.Complex} objects
     * @param b an array of {@link org.redfx.strange.Complex} objects
     * @return an array of {@link org.redfx.strange.Complex} objects
     */
    default public Complex[][] mmul(Complex[][] a, Complex[][] b) {
        return Complex.mmul(a, b);
    }
    
   // Future<Result> runProgram(Program p);

}
