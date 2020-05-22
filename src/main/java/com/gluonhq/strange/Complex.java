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
package com.gluonhq.strange;

public final class Complex {
    
    public static final Complex ZERO = new Complex(0.d, 0.d);
    public static final Complex ONE = new Complex(1.d, 0.d);
    public static final Complex I = new Complex(0.d, 1.d);
    
    private static final double HV = 1./Math.sqrt(2.);

    public static final Complex HC = new Complex(HV, 0.d);
    public static final Complex HCN = new Complex(-HV, 0.d);
    
    public final double r;
    public final double i;
    
    /**
     * Create a complex number with a real component only
     * @param r the real component
     */
    public Complex(double r) {
        this(r, 0.d);
    }
    
    /**
     * Create a complex number with a real and imaginary component
     * @param r the real component
     * @param i the imaginary component
     */
    public Complex(double r, double i) {
        this.r = r;
        this.i = i;
    }
    
    public Complex add(Complex b) {
        double nr = this.r + b.r;
        double ni = this.i + b.i;
        return new Complex(nr, ni);
    }

    public Complex min(Complex b) {
        double nr = this.r - b.r;
        double ni = this.i - b.i;
        return new Complex(nr, ni);
    }

    public Complex mul(Complex b) {
        double nr = (this.r * b.r) - (this.i * b.i);
        double ni = (this.r * b.i) + (this.i * b.r);
        return new Complex(nr, ni);
    }
    
    public Complex mul(double b) {
        return new Complex(this.r * b, this.i * b);
    }
    
    public double abssqr() {
        return (this.r*this.r + this.i*this.i);
    }
    
    /**
     * Calculate the tensor product of matrix a and matrix b
     * @param a
     * @param b
     * @return 
     */
    public static Complex[][] tensor(Complex[][] a, Complex[][] b) {
        int d1 = a.length;
        int d2 = b.length;
        Complex[][] result = new Complex[d1 * d2][d1 * d2];
        for (int rowa = 0; rowa < d1; rowa++) {
            for (int cola = 0; cola < d1; cola++) {
                for (int rowb = 0; rowb < d2; rowb++) {
                    for (int colb = 0; colb < d2; colb++) {
                        result[d2 * rowa + rowb][d2 * cola + colb] = a[rowa][cola].mul( b[rowb][colb]);
                    }
                }
            }
        }
        return result;
    }

    public static Complex[][] mmul(Complex[][] a, Complex[][]b) {
        int arow = a.length;
        int acol = a[0].length;
        int brow = b.length;
        int bcol = b[0].length;
        if (acol != brow) throw new RuntimeException("#cols a "+acol+" != #rows b "+brow);
        Complex[][] answer = new Complex[arow][bcol];
        for (int i = 0; i < arow; i++) {
            for (int j = 0; j < bcol; j++) {
                Complex el = Complex.ZERO;
                for (int k = 0; k < acol;k++) {
                    el = el.add(a[i][k].mul(b[k][j]));
                }
                answer[i][j] = el;
            }
        }
        return answer;
    }

    public static void printArray(Complex[] ca) {
        System.err.println("complex["+ca.length+"]: ");
        for (Complex c: ca){
            System.err.println("-> "+c);
        }
    }
    @Override 
    public String toString() {
        return "("+this.r+", "+this.i+")";
    }
}
