/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Gluon Software
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

import com.gluonhq.strange.Block;
import com.gluonhq.strange.BlockGate;
import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Step;
import java.util.List;

/**
 *
 * @author johan
 */
public class Add extends BlockGate {

    final Block block;
    
    public Add(int x0, int x1, int y0, int y1) {
        super();
        this.block = createBlock(x0, x1, y0, y1);
        setBlock(block);
       
    }
    
    public Block createBlock(int x0, int x1, int y0, int y1) {
        Block answer = new Block(y1-x0+1);
        int m = x1-x0+1;
        int n = y1 - y0 + 1;
        System.err.println("create adder, m = "+m+" and n = "+n);
        answer.addStep(new Step(new Fourier(m, 0)));
        for (int i = 0; i < m; i++) {
           for (int j = 0; j < m-i ; j++) {
                int cr0 = 2 *m-j-i-1;
                System.err.println("in loop, i = "+i+" and j = "+j+" and cr0 = "+cr0);
                if (cr0 < m + n) {
                    Step s = new Step(new Cr(i, cr0,2,1+j));
                    answer.addStep(s);
                }
            }
        }
        answer.addStep(new Step(new InvFourier(m, 0)));
        return answer;
    }
    
    

    
}
