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
package com.gluonhq.strange.test;
import com.gluonhq.strange.algorithm.Classic;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author johan
 */
public class ClassicTests {
    
    @Test
    public void random() {
        int z = 0;
        int o = 0;
        for (int i = 0; i < 100; i++) {
            int b = Classic.randomBit();
            System.out.println("b = "+b);
            if (b == 0) z++;
            if (b == 1) o++;
        }
        assertTrue (z > 10);
        assertTrue (o > 10);
    }
    
    @Test
    public void s00() {
        int sum = Classic.qsum(0, 0);
        assertEquals(0, sum);
    }

    @Test
    public void s01() {
        int sum = Classic.qsum(0,1);
        assertEquals(1, sum);
    }
    
       
    @Test
    public void s10() {
        int sum = Classic.qsum(1,0);
        assertEquals(1, sum);
    }
    
    @Test
    public void s12() {
        int sum = Classic.qsum(1,2);
        assertEquals(3, sum);
    }
    
    @Test
    public void s22() {
        int sum = Classic.qsum(2,2);
        assertEquals(0, sum);
    }
        
    @Test
    public void s413() {
        int sum = Classic.qsum(4,13);
        assertEquals(17, sum);
    }
}
