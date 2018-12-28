package com.gluonhq.strange.test;

import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.cloud.CloudlinkQuantumExecutionEnvironment;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
