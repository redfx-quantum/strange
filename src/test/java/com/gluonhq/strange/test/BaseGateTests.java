package com.gluonhq.strange.test;

import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.cloud.CloudlinkQuantumExecutionEnvironment;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.BeforeClass;

public class BaseGateTests extends Application {

    public Result runProgram(Program program) {
        QuantumExecutionEnvironment qee = new CloudlinkQuantumExecutionEnvironment();
        return qee.runProgram(program);
    }

    @BeforeClass
    public static void beforeClass() {
        Thread thread = new Thread(() -> Application.launch(BaseGateTests.class), "JavaFX Test Application");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // noop
    }
}
