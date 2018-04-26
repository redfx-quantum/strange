package com.gluonhq.strange.cloudlink;

import java.util.ArrayList;

public class PermutationGate extends Gate {

    private int a;
    private int b;
    private int n;

    private Complex[][] matrix;

    public PermutationGate(int a, int b, int n) {
        super("P", "permutation", new ArrayList<>());

        assert(a < n);
        assert(b < n);
        this.a = a;
        this.b = b;
        this.n = n;

        int dim = 1 << n;
        this.matrix = new Complex[dim][dim];
        int amask = 1 << a;
        int bmask = 1 << b;
        for (int i = 0; i < dim; i++) {
            int x = (amask & i) /amask;
            int y = (bmask & i) /bmask;
            if (x == y) {
                for (int j = 0; j < dim; j++) {
                    matrix[i][j] = (i ==j ? Complex.ONE : Complex.ZERO);
                }
            } else {
                int flipped = ((i ^ amask) ^ bmask);
                for (int j = 0; j < dim; j++) {
                    matrix[i][j] = (j == flipped ? Complex.ONE : Complex.ZERO );
                }
            }
        }

        for (int i =0 ; i < n; i++) {
            getAffectedQubitIndex().add(i);
        }
    }

    @Override
    public Complex[][] getMatrix() {
        return matrix;
    }
}
