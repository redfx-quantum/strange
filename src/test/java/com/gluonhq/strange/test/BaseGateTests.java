package com.gluonhq.strange.test;

import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;

public class BaseGateTests {

    /**
     * Flag for setting up the JavaFX, we only need to do this once for all tests.
     */
    private static boolean jfxIsSetup;

    public Result runProgram(Program program) throws RuntimeException {
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        return qee.runProgram(program);
    }


}
