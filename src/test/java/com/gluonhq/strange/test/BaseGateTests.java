package com.gluonhq.strange.test;

import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.cloud.CloudlinkQuantumExecutionEnvironment;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import javafx.embed.swing.JFXPanel;

import javax.swing.SwingUtilities;
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
        /*
        if(!jfxIsSetup) {
            setupJavaFX();
            jfxIsSetup = true;
        }

//        QuantumExecutionEnvironment qee = new CloudlinkQuantumExecutionEnvironment("executeProgram");
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
//        Future<Result> futureResult = qee.runProgram(program);
//        try {
//            return futureResult.get(1, TimeUnit.MINUTES);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return qee.runProgram(program);
*/
    }

    private void setupJavaFX() {

        long timeMillis = System.currentTimeMillis();

        final CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            try {
                // initializes JavaFX environment
                new JFXPanel();

                latch.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("javafx initialising...");
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("javafx is initialised in " + (System.currentTimeMillis() - timeMillis) + "ms");
    }
}
