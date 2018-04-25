package com.gluonhq.strange.cloud;

import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.util.ArrayList;
import java.util.List;

public class ResultConverter extends InputStreamInputConverter<Result> {

    @Override
    public Result read() {
        List<Qubit> qubits = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(getInputStream())) {
            JsonObject jsonResult = jsonReader.readObject();
            jsonResult.getJsonArray("qubits").getValuesAs(JsonNumber.class)
                    .stream()
                    .map(probability -> {
                        Qubit qubit = new Qubit();
                        qubit.setProbability(probability.doubleValue());
                        return qubit;
                    }).forEach(qubits::add);
        }

        return new Result(qubits.toArray(new Qubit[qubits.size()]), new Complex[0]);
    }
}
