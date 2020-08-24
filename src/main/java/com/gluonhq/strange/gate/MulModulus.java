/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Johan Vos
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
import com.gluonhq.strange.local.Computations;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author johan
 */
public class MulModulus extends BlockGate<MulModulus> {

    Block block;
    static HashMap<Integer, Block> cache = new HashMap<>();

    /**
     * Multiply the qubit in the x register with an integer mul
     * @param x0 start idx x register
     * @param x1 end idx x register
     * x_0 ----- y_0 + x_0
     * x_1 ----- y+1 + x_1
     * y_0 ----- 0
     * y_1 ----- 0
     * ANC ----- ANC (0) 
     * x_1 and y_1 are overflow bits (initially 0, final 0)
     */
    public MulModulus(int x0, int x1, int mul, int mod) {
        super();
        this.setIndex(x0);
        x1 = x1-x0;
        x0 = 0;
        this.block = createBlock(x0, x1,mul, mod);
        setBlock(block);
       
    }
    
    public Block createBlock(int y0, int y1, int mul, int mod) {
        int hash = 1000000 * y0 + 10000*y1+ 100*mul + mod;
        System.err.println("CreateBlock MulModus hash for "+y0+", " + y1+", mul = "+mul+", mod = "+mod+", hash = "+hash);
        this.block = cache.get(hash);
        if (block != null) {
            System.err.println("cached, block = "+block);
            return block;
        }
        System.err.println("not cached, let's create the block");

        int x0 = y0;
        int x1 = y1-y0;
        int size = 1 + x1-x0;
     //   int dim = 1 << size;
        Block answer = new Block("MulModulus", 2 * size+1);
    //    System.err.println("first blocks, add with "+x0+", "+x1+", size = "+size);

        for (int i = 0; i < mul; i++) {
     //       System.err.println("CREATE ADD with "+x0+", "+x1+", "+(x1+1)+", "+(x1+size));
            AddModulus add = new AddModulus(x0, x1, x1+1, x1 + size, mod);
            answer.addStep(new Step(add));
        }

        for (int i = x0; i < x1+1; i++) {
      //      System.err.println("swap "+i+" with "+(i+size));
            answer.addStep(new Step (new Swap(i, i + size)));
        }

        int invsteps = Computations.getInverseModulus(mul,mod);
    //    System.err.println("invsteps = "+invsteps);
        for (int i = 0; i < invsteps; i++) {
            AddModulus add = new AddModulus(x0, x1, x1+1, x1 + size, mod).inverse();
      //      System.err.println("CREATE ADDmod with "+x0+", "+x1+", "+(x1+1)+", "+(x1+size));

            answer.addStep(new Step(add));
        }
cache.put(hash, answer);
        System.err.println("CREATED MulModus hash for "+y0+", " + y1+", mul = "+mul+", mod = "+mod+", block = "+block);

        return answer;
    }
  
    
}
