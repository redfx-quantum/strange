package org.redfx.strange.test;

import org.junit.jupiter.api.Test;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.RotationX;
import org.redfx.strange.gate.RotationY;
import org.redfx.strange.gate.RotationZ;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RotationTests extends BaseGateTests{

    @Test
    public void rotationXTest(){
        int[] results = new int[3];
        for(int i = 0; i < 3; i++){
            Program p = new Program(1, new Step(new RotationX((Math.PI/2)*i,0)));
            for(int j = 0; j < 100; j++){
                Result res = runProgram(p);
                Qubit qubit = res.getQubits()[0];
                if(qubit.measure() == 1){
                    results[i]++;
                }
            }
        }
        assertTrue(results[0] < 10);
        assertTrue(results[1] > 30 && results[1] < 70);
        assertTrue(results[2] > 90);
    }

    @Test
    public void rotationYTest(){
        int[] results = new int[3];
        for(int i = 0; i < 3; i++){
            Program p = new Program(1, new Step(new RotationY((Math.PI/2)*i,0)));
            for(int j = 0; j < 100; j++){
                Result res = runProgram(p);
                Qubit qubit = res.getQubits()[0];
                if(qubit.measure() == 1){
                    results[i]++;
                }
            }
        }
        assertTrue(results[0] < 10);
        assertTrue(results[1] > 30 && results[1] < 70);
        assertTrue(results[2] > 90);
    }

    @Test
    public void rotationZTest(){
        int[] results = new int[2];
        for(int i = 0; i < 2; i++){
            Program p = new Program(1, new Step(new RotationZ(Math.PI*i,0)));
            for(int j = 0; j < 100; j++){
                Result res = runProgram(p);
                Qubit qubit = res.getQubits()[0];
                if(qubit.measure() == 1){
                    results[i]++;
                }
            }
        }
        assertTrue(results[0] < 10);
        assertTrue(results[1] < 10);
    }

}
