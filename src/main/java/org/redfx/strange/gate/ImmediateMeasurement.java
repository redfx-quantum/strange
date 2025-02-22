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
     * <p>Constructor for Measurement.</p>
     */
    public ImmediateMeasurement(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }
    
    /**
     * <p>Constructor for Measurement.</p>
     *
     * @param idx a int
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
    @Override public String getCaption() {return "M";}

    @Override
    public Complex[] applyOptimize(Complex[] v) {
        LOG.info("ApplyOptimize for s = " + v.length+ "; "+ v[0].abssqr()+", "+v[1].abssqr());
        Complex[] answer = new Complex[v.length];
        if ((v[0].abssqr() > .01d)&& (v[1].abssqr() > 0.1d)) {
            double sq = .5* Math.sqrt(2);
            if (Math.random() > .5) {
                LOG.info("FALSE");
                answer[0] = Complex.ONE.mul(sq);
                answer[1] = Complex.ZERO;
                consumer.accept(Boolean.FALSE);
            } else {
                LOG.info("TRUE");
                answer[1] = Complex.ONE.mul(sq);
                answer[0] = Complex.ZERO;
                consumer.accept(Boolean.TRUE);
            }
            return answer;
        }
        LOG.info("ow "+ (v[1].abssqr()> .01d));
        consumer.accept(v[1].abssqr()> .01d);
        return v;
    }

    @Override
    public boolean hasOptimization() {
        return true;
    }
    
}
