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

import java.util.Arrays;
import org.redfx.strange.Block;
import org.redfx.strange.BlockGate;
import org.redfx.strange.Step;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.redfx.strange.Complex;
import org.redfx.strange.local.Computations;

/**
 * <p>Add class.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public class Add extends BlockGate<Add> {

    Block block;
    static HashMap<Integer, Block> cache = new HashMap<>();
    static Logger LOG = Logger.getLogger(Add.class.getName());

    /**
     * Add the qubit in the x register and the y register, result is in x
     *
     * @param x0 start idx x register
     * @param x1 end idx x register
     * @param y0 start idx y register
     * @param y1 end idx y register
     * x_0 ----- y_0 + x_0
     * x_1 ----- y+1 + x_1
     * y_0 ----- y_0
     * y_1 ----- y_1
     */
    public Add(int x0, int x1, int y0, int y1) {
        super();
        this.setIndex(x0);
        int hash = 1000000 * x0 + 10000*x1+ 100*y0 + y1;
        this.block = cache.get(hash);
        System.err.println("ADD, block = "+block);
        if (this.block == null) {
            this.block = createBlock(x0, x1, y0, y1);
        //    cache.put(hash, block);
        } 
        setBlock(block);
       
    }
    
    /**
     * <p>createBlock.</p>
     *
     * @param x0 a int
     * @param x1 a int
     * @param y0 a int
     * @param y1 a int
     * @return a {@link org.redfx.strange.Block} object
     */
    public Block createBlock(int x0, int x1, int y0, int y1) {
        Block answer = new Block("Add", y1-x0+1);
        int m = x1-x0+1;
        int n = y1 - y0 + 1;
        answer.addStep(new Step(new Fourier(m, 0)));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m-i ; j++) {
                int cr0 = 2 *m-j-i-1;
                if (cr0 < m + n) {
                    Step s = new Step(new Cr(i, cr0,2,1+j));
                    answer.addStep(s);
                }
            }
        }
        answer.addStep(new Step(new Fourier(m, 0).inverse()));
      //  answer.addStep(new Step(new InvFourier(m, 0)));
        return answer;
    }
    /** {@inheritDoc} */
    @Override
    public boolean hasOptimization() {
        return true;
    }
    @Override
    public void setInverse(boolean inv) {
        super.setInverse(inv);
        System.err.println("ADD, setInverse with inv = "+inv+" and block = "+block);
        if (this.block != null) {
            List<Step> steps = this.block.getSteps();
            for (int i = 1; i < steps.size() - 1; i++) {
                steps.get(i).setInverse(inv);
            }
            System.err.println("AFTER si, steps = "+this.block.getSteps());
        }
    }
    
    @Override
    public Complex[] applyOptimize(Complex[] v) {
        LOG.info("Apply optimize for Add with steps " + block.getSteps());
        List<Step> steps = block.getSteps();
        Step s0 = steps.get(0);
        Step sn = steps.getLast();
        LOG.info("Step 0 = " + s0 + " and current status = " + Arrays.toString(v));
        Complex[] answer = Computations.calculateNewState(s0.getGates(), v, block.getNQubits());
        LOG.info("After Step 0 ,  and current status = " + Arrays.toString(answer));
        for (int i = 1; i < steps.size() - 1; i++) {
            LOG.info("Apply step " + i + ": " + steps.get(i).getGates());
            answer = Computations.calculateNewState(steps.get(i).getGates(), answer, block.getNQubits());
            LOG.info("After Step " + i + " ,   current status = " + Arrays.toString(answer));
        }
        LOG.info("Last Step " + sn.getGates() + " ,   current status = " + Arrays.toString(answer));

        answer = Computations.calculateNewState(sn.getGates(), answer, block.getNQubits());
        LOG.info("After last Step  status = " + Arrays.toString(answer));

        return answer;
    }

    /** {@inheritDoc} */
    @Override
    public String getCaption() {
        return "A\nD\nD";
    }

}
