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

//import com.gluonhq.connect.converter.InputStreamInputConverter;
//
//import javax.json.Json;
//import javax.json.JsonNumber;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import javax.json.JsonValue;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;

/**
 * <p>ResultConverter class.</p>
 *
 * @author alain
 * @version $Id: $Id
 */
public class ResultConverter {
//        extends InputStreamInputConverter<Result> {
//
//    @Override
//    public Result read() {
//        List<Qubit> qubits = new ArrayList<>();
//        InputStreamReader reader = null;
//        try {
//            reader = new InputStreamReader(getInputStream());
//            StringBuilder b = new StringBuilder();
//            char[] buffer = new char[4096];
//            int read;
//            while ((read = reader.read(buffer, 0, 4096)) != -1) {
//                b.append(buffer, 0, read);
//            }
//
//            System.out.println("result = " + b.toString());
//
//            JsonReader jsonReader = Json.createReader(new StringReader(b.toString()));
//            JsonObject jsonResult = jsonReader.readObject();
//            JsonValue jsonQubits = jsonResult.get("qubits");
//            if (jsonQubits.getValueType() == JsonValue.ValueType.ARRAY) {
//                jsonResult.getJsonArray("qubits").getValuesAs(JsonNumber.class)
//                        .stream()
//                        .map(probability -> {
//                            Qubit qubit = new Qubit();
//                            qubit.setProbability(probability.doubleValue());
//                            return qubit;
//                        }).forEach(qubits::add);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return new Result(qubits.toArray(new Qubit[0]), new Complex[0]);
//    }
}
