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
import org.redfx.strange.Complex;
import org.redfx.strange.Step;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.redfx.strange.Gate;
import org.redfx.strange.local.Computations;

/**
 * <p>AddInteger class.</p>
 *
 * @author johan
 * @version $Id: $Id
 */
public class AddInteger extends BlockGate<AddInteger> {

    Block block;
    static HashMap<Integer, Block> cache = new HashMap<>();

    static Logger LOG = Logger.getLogger(AddInteger.class.getName());
    
    /**
     * Add the qubit in the x register and the y register, result is in x
     *
     * @param x0 start idx x register
     * @param x1 end idx x register
     * @param num the integer to be added, (y_m.. y_0)
     * x_0 ----- y_0 + x_0
     * x_1 ----- y+1 + x_1
     */
    public AddInteger(int x0, int x1, int num) {
        super();
        setIndex(x0);
        x1 = x1 - x0;
        x0 = 0;
        int hash = 1000000 * x0 + 10000*x1+ num;
        this.block = cache.get(hash);
        if (this.block == null) {
            this.block = createBlock(x0, x1, num);
   //         cache.put(hash, block);
        }
        setBlock(block);
       
    }
    
    /**
     * <p>createBlock.</p>
     *
     * @param x0 a int
     * @param x1 a int
     * @param num a int
     * @return a {@link org.redfx.strange.Block} object
     */
    public Block createBlock(int x0, int x1, int num) {
        boolean old = false;
        int m = x1-x0+1;
        Block answer = new Block("AddInteger ", m);
        answer.addStep(new Step(new Fourier(m, 0)));
        Step pstep = new Step();
        for (int i = 0; i < m; i++) {
            Complex[][] mat = Complex.identityMatrix(2);
            for (int j = 0; j < m-i ; j++) {
                int cr0 = m-j-i-1;
                if ((num >> cr0 & 1) == 1) {
                    System.err.println("NEED to apply R with i = "+i+" and j = "+j+" and inv = "+inverse);
                    Gate gate = new R(2, 1 + j, i);
                    if (inverse) gate.setInverse(true);
                    mat = Complex.mmul(mat,gate.getMatrix());
                    System.err.println("now matrix = ");
                    Complex.printMatrix(mat, System.err);
                    if (old) {
                        Step s = new Step(new R(2, 1 + j, i));
                        answer.addStep(s);
                    }
                }
            }
            if (!old) {
                System.err.println("Create sqmg for i = "+i+": ");
                Complex.printMatrix(mat, System.err);
                pstep.addGate(new SingleQubitMatrixGate(i, mat));
            }
        }
        if (!old) {
            answer.addStep(pstep);
        }
        answer.addStep(new Step(new Fourier(m, 0).inverse()));
        System.err.println("AI, steps = "+answer.getSteps());
        return answer;
    }

    @Override
    public void setInverse(boolean inv) {
        super.setInverse(inv);
        if (this.block != null) {
            List<Step> steps = this.block.getSteps();
            for (int i = 1; i < steps.size() - 1; i++) {
                steps.get(i).setInverse(inv);
            }
        } else {
            LOG.finest("Inverse required for " + this + ", but we don't have a block yet. createBlock will take care of this.");
        }
    }
    /** {@inheritDoc} */
    @Override
    public boolean hasOptimization() {
        return true;
    }
    @Override
    public Complex[] applyOptimize(Complex[] v) {
        LOG.info("Apply optimize for AddInteger with steps " + block.getSteps());
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
        return "A\nD\nD\nI";
    }

}
