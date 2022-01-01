/*-
 * #%L
 * Strange
 * %%
 * Copyright (C) 2020 Johan Vos
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Johan Vos nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.redfx.strange.cloud;

//import com.gluonhq.cloudlink.client.data.RemoteFunctionBuilder;
//import com.gluonhq.connect.ConnectState;
//import com.gluonhq.connect.GluonObservableObject;
//
//import javax.json.Json;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObject;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.function.Consumer;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * <p>CloudlinkQuantumExecutionEnvironment class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class CloudlinkQuantumExecutionEnvironment {
//        implements QuantumExecutionEnvironment {
//
//    private final String functionName;
//
//    public CloudlinkQuantumExecutionEnvironment(String functionName) {
//        this.functionName = functionName;
//    }
//
//    @Override
//    public Result runProgram(Program p) {
//        Future<Result> f = doRunProgram(p);
//        try {
//            return f.get();
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public void runProgram(Program p, Consumer<Result> resultConsumer) {
//        String serializedProgram = serializeProgram(p).toString();
//
//        CompletableFuture<Result> futureResult = new CompletableFuture<>();
//
//        GluonObservableObject<Result> result = RemoteFunctionBuilder.create(functionName)
//                .param("program", serializedProgram)
//                .cachingEnabled(false)
//                .object()
//                .call(new ResultConverter());
//         result.stateProperty().addListener((obs, ov, nv) -> {
//            if (nv == ConnectState.SUCCEEDED) {
//                resultConsumer.accept(result.get());
//            }
//         });
//    }
//
//    private Future<Result> doRunProgram(Program p) {
//        String serializedProgram = serializeProgram(p).toString();
//
//        CompletableFuture<Result> futureResult = new CompletableFuture<>();
//
//        GluonObservableObject<Result> result = RemoteFunctionBuilder.create(functionName)
//                .param("program", serializedProgram)
//                .cachingEnabled(false)
//                .object()
//                .call(new ResultConverter());
//
//        result.stateProperty().addListener((obs, ov, nv) -> {
//            if (nv == ConnectState.SUCCEEDED) {
//                futureResult.complete(result.get());
//            } else if (nv == ConnectState.FAILED) {
//                futureResult.completeExceptionally(result.getException());
//            } else if (nv == ConnectState.CANCELLED) {
//                futureResult.cancel(true);
//            }
//        });
//
//        return futureResult;
//    }
//
//    private JsonObject serializeProgram(Program program) {
//        JsonArrayBuilder jsonSteps = Json.createArrayBuilder();
//        program.getSteps().stream()
//                .map(this::serializeStep)
//                .forEach(jsonSteps::add);
//
//        return Json.createObjectBuilder()
//                .add("numberQubits", program.getNumberQubits())
//                .add("steps", jsonSteps.build())
//                .build();
//    }
//
//    private JsonObject serializeStep(Step step) {
//        JsonArrayBuilder jsonGates = Json.createArrayBuilder();
//        step.getGates().stream()
//                .map(this::serializeGate)
//                .forEach(jsonGates::add);
//
//        return Json.createObjectBuilder()
//                .add("gates", jsonGates.build())
//                .build();
//    }
//
//    private JsonObject serializeGate(Gate gate) {
//        JsonArrayBuilder jsonAffectedQubitIndex = Json.createArrayBuilder();
//        gate.getAffectedQubitIndex().forEach(jsonAffectedQubitIndex::add);
//
//        return Json.createObjectBuilder()
//                .add("caption", gate.getCaption())
//                .add("group", gate.getGroup())
//                .add("affectedQubitIndex", jsonAffectedQubitIndex.build())
//                .build();
//    }

}
