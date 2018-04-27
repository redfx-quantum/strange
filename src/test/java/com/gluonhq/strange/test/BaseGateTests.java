package com.gluonhq.strange.test;

import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.cloud.CloudlinkQuantumExecutionEnvironment;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.SwingUtilities;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BaseGateTests {

    /**
     * Flag for setting up the JavaFX, we only need to do this once for all tests.
     */
    private static boolean jfxIsSetup;

    public Result runProgram(Program program) throws RuntimeException {
        if(!jfxIsSetup) {
            setupJavaFX();
            jfxIsSetup = true;
        }

        CompletableFuture<Result> futureResult = new CompletableFuture<>();
        Platform.runLater(() -> {
            QuantumExecutionEnvironment qee = new CloudlinkQuantumExecutionEnvironment();
            GluonObservableObject<Result> result = qee.runProgram(program);
            if (result.get() != null) {
                futureResult.complete(result.get());
            } else {
                result.stateProperty().addListener((obs, ov, nv) -> {
                    if (nv == ConnectState.SUCCEEDED) {
                        futureResult.complete(result.get());
                    } else if (nv == ConnectState.FAILED) {
                        futureResult.completeExceptionally(result.getException());
                    } else if (nv == ConnectState.CANCELLED) {
                        futureResult.completeExceptionally(new InterruptedException("Remote function call was cancelled."));
                    }
                });
            }
        });

        try {
            return futureResult.get(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                e.printStackTrace();
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
