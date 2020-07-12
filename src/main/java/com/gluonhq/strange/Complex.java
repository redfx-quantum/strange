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

import com.gluonhq.strange.gate.PermutationGate;
import java.io.PrintStream;

public final class Complex {
    
    public static final Complex ZERO = new Complex(0.d, 0.d);
    public static final Complex ONE = new Complex(1.d, 0.d);
    public static final Complex I = new Complex(0.d, 1.d);
    
    private static final double HV = 1./Math.sqrt(2.);

    public static final Complex HC = new Complex(HV, 0.d);
    public static final Complex HCN = new Complex(-HV, 0.d);


    public double r;
    public double i;
    
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
    
    /**
     * Add and replace
     * @param b
     * @return 
     */
    public Complex addr(Complex b) {
        this.r = this.r + b.r;
        this.i = this.i + b.i;
        return this;
    }

    /**
     * AddMul and replace
     * @param b
     * @return 
     */
    public Complex addmulr(Complex a, Complex b) {
        double nr = (a.r * b.r) - (a.i * b.i);
        double ni = (a.r * b.i) + (a.i * b.r);
        this.r = this.r + nr;
        this.i = this.i + ni;
        return this;
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
                        if ((a[rowa][cola] == Complex.ZERO) || (b[rowb][colb] == Complex.ZERO)) {
                            result[d2 * rowa + rowb][d2 * cola + colb] = Complex.ZERO;
                        } else {
                            result[d2 * rowa + rowb][d2 * cola + colb] = a[rowa][cola].mul( b[rowb][colb]);
                        }
                    }
                }
            }
        }
        return result;
    }
static int zCount = 0;
static int nzCount = 0;

    public static Complex[][] mmul(Complex[][] a, Complex[][]b) {
        long l0 = System.currentTimeMillis();
        int arow = a.length;
        int acol = a[0].length;
        int brow = b.length;
        int bcol = b[0].length;
        int am = 0;
        if (acol != brow) throw new RuntimeException("#cols a "+acol+" != #rows b "+brow);
        Complex[][] answer = new Complex[arow][bcol];
        for (int i = 0; i < arow; i++) {
            for (int j = 0; j < bcol; j++) {
                Complex el = new Complex(0.,0.);
                boolean zero = true;
                for (int k = 0; k < acol;k++) {
                    if ((a[i][k] != Complex.ZERO) &&(b[k][j] != Complex.ZERO) ) {
                        if ((a[i][k].r < .0001) && (b[k][j].r < 0.0001) ){
                            //dooh 
                            am++;
                        }
                        el.addmulr(a[i][k], b[k][j]);
                        zero = false;
                    }
                }
                if (zero) {
                    answer[i][j] = Complex.ZERO;
                    zCount++;
                } else {
                    answer[i][j] = el;
                    nzCount ++;
                }
            }
        }
        long l1 = System.currentTimeMillis();
        System.err.println("mulitply matrix "+arow+", "+acol+", "+bcol+" took "+(l1 -l0)+" zc = "+zCount+" and nzc = "+nzCount);
        return answer;
    }
    
    static Complex[][] conjugateTranspose(Complex[][] src) {
        int d0 = src.length;
        int d1 = src[0].length;
        Complex[][] answer = new Complex[d1][d0];
        for (int i = 0; i < d0; i++) {
            for (int j = 0; j < d1; j++) {
                Complex c = src[i][j];
                answer[j][i] = new Complex(c.r, -1*c.i);
            }
        }
        return answer;
    }
    

    public static Complex[][] permutate0(Complex[][] matrix, PermutationGate pg) {
        Complex[][] p = pg.getMatrix();
        int dim = p.length;
        Complex[][] answer = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            int idx = 0;
            while (p[i][idx].equals(Complex.ZERO)) idx++;
            for (int j = 0; j < dim; j ++) {
                answer[i][j] = matrix[idx][j];
            }
        }
        return answer;
    }
    
    public static Complex[][] permutate(PermutationGate pg, Complex[][] matrix) {
        Complex[][] p = pg.getMatrix();
        int dim = p.length;
        Complex[][] answer = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            int idx = 0;
            while (p[i][idx].equals(Complex.ZERO)) idx++;
            for (int j = 0; j < dim; j ++) {
                answer[i][j] = matrix[idx][j];
            }
        }
       return answer;
        }
    
    public static Complex[][] permutate(Complex[][] matrix, PermutationGate pg) {
        Complex[][] p = pg.getMatrix();
        int dim = p.length;
        Complex[][] answer = new Complex[dim][dim];
        for (int i = 0; i < dim; i++) {
            int idx = 0;
            while (p[idx][i].equals(Complex.ZERO)) idx++;
            for (int j = 0; j < dim; j ++) {
                answer[j][i] = matrix[j][idx];
            }
        }
       return answer;
   }

    public static void printArray(Complex[] ca) {
        printArray(ca, System.err);
    }
    
    public static void printArray(Complex[] ca, PrintStream ps) {
        ps.println("complex["+ca.length+"]: ");
        for (Complex c: ca){
            ps.println("-> "+c);
        }
    }
    
    public static void printMatrix(Complex[][] cm) {
        printMatrix(cm, System.err);
    }
    
    static final boolean debug = false;
    public static void printMatrix(Complex[][] cm, PrintStream ps) {
        if (!debug) return;
        ps.println("complex["+cm.length+"]: ");
        for (int idx = 0; idx < cm.length; idx++){
            String row = "row "+idx;
            for (int jdx = 0; jdx < cm.length; jdx++) {
                Complex c = cm[idx][jdx];
                row = row + ":" + c.toString();
            }
            ps.println("-> "+row);
         //   idx++;
        }
    }
    
    @Override 
    public String toString() {
        return "("+this.r+", "+this.i+")";
    }
}
