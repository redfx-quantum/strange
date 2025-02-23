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

import java.util.function.Consumer;
import java.util.logging.Logger;
import org.redfx.strange.Complex;

/**
 * <p>Measurement class.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public class ImmediateMeasurement extends SingleQubitGate {
    
    static Logger LOG = Logger.getLogger(ImmediateMeasurement.class.getName());
    Complex[][] matrix =  new Complex[][]{{Complex.ONE,Complex.ZERO}, {Complex.ZERO,Complex.ONE}};
    private final Consumer<Boolean> consumer;
    
    /**
     * <p>Constructor for ImmediateMeasurement.</p>
     * @param consumer this callback will be invoked when the measurement is done.
     * The creator of this gate will be notified on whether 0 was measured (false) or 1 (true)
     */
    public ImmediateMeasurement(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }
    
    /**
     * <p>Constructor for ImmediateMeasurement.</p>
     *
     * @param idx index of the qubit that this gate is applied to.
     * @param consumer this callback will be invoked when the measurement is done.
     * The creator of this gate will be notified on whether 0 was measured (false) or 1 (true)
     */
    public ImmediateMeasurement (int idx, Consumer<Boolean> consumer) {
        super(idx);
        this.consumer = consumer;
    }

    /** {@inheritDoc} */
    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }
    
    /** {@inheritDoc} */
    @Override public String getCaption() {return "IM";}
    
    public Consumer<Boolean> getConsumer() {
        return this.consumer;
    }
    
}
