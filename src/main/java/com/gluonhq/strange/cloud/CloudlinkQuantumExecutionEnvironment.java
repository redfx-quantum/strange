package com.gluonhq.strange.cloud;

import com.gluonhq.cloudlink.client.data.RemoteFunctionBuilder;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class CloudlinkQuantumExecutionEnvironment implements QuantumExecutionEnvironment {

    private final String functionName;

    public CloudlinkQuantumExecutionEnvironment(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public GluonObservableObject<Result> runProgram(Program p) {
        String serializedProgram = serializeProgram(p).toString();

        System.out.println("serializedProgram = " + serializedProgram);

        return RemoteFunctionBuilder.create(functionName)
                .param("program", serializedProgram)
                .cachingEnabled(false)
                .object()
                .call(new ResultConverter());
    }

    private JsonObject serializeProgram(Program program) {
        JsonArrayBuilder jsonSteps = Json.createArrayBuilder();
        program.getSteps().stream()
                .map(this::serializeStep)
                .forEach(jsonSteps::add);

        return Json.createObjectBuilder()
                .add("numberQubits", program.getNumberQubits())
                .add("steps", jsonSteps.build())
                .build();
    }

    private JsonObject serializeStep(Step step) {
        JsonArrayBuilder jsonGates = Json.createArrayBuilder();
        step.getGates().stream()
                .map(this::serializeGate)
                .forEach(jsonGates::add);

        return Json.createObjectBuilder()
                .add("gates", jsonGates.build())
                .build();
    }

    private JsonObject serializeGate(Gate gate) {
        JsonArrayBuilder jsonAffectedQubitIndex = Json.createArrayBuilder();
        gate.getAffectedQubitIndex().forEach(jsonAffectedQubitIndex::add);

        return Json.createObjectBuilder()
                .add("caption", gate.getCaption())
                .add("group", gate.getGroup())
                .add("affectedQubitIndex", jsonAffectedQubitIndex.build())
                .build();
    }
}
