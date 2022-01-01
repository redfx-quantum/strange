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
package org.redfx.strange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * A single Step in a quantum <code>Program</code>. In a step, a number
 * of Gates can be added, involving a number of qubits. However, in a single
 * step a qubit can be involved in at most one <code>Gate</code>. It is illegal
 * to declare 2 gates in a single step that operate on the same qubit.
 *
 * @author johan
 * @version $Id: $Id
 */
public class Step {
    
    /**
     * The type of the step. Typically, a step contains instructions that alter 
     * the quantum circuit. Those are steps with type <code>NORMAL</code>.
     * <br>
     * Some steps do not alter the quantum circuit at all and can be ignored in
     * computations. Those are steps with type <code>PSEUDO</code> and they 
     * are typically used for visualization.
     */
    public enum Type {
        NORMAL,
        PSEUDO,
        PROBABILITY
    }
    
    private final Type type;
    private final ArrayList<Gate> gates = new ArrayList<>();
    private int index;
    private final String name;

    private Program program;
    
    private int complexStep = -1; // if a complex step needs to broken into
    // simple steps, only one simple step can have this value to be the index of the complex step

    private boolean informal = false;

    /**
     * <p>Constructor for Step.</p>
     *
     * @param moreGates a {@link org.redfx.strange.Gate} object
     */
    public Step(Gate... moreGates ) {
        this("unknown", moreGates);
    }

    /**
     * <p>Constructor for Step.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param moreGates a {@link org.redfx.strange.Gate} object
     */
    public Step(String name, Gate... moreGates ) {
        this.name = name;
        addGates(moreGates);
        this.type = Type.NORMAL;
    }
    
    /**
     * <p>Constructor for Step.</p>
     *
     * @param type a {@link org.redfx.strange.Step.Type} object
     */
    public Step(Type type) {
        this.type = type;
        this.name = "pseudo";
    }

    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return a {@link org.redfx.strange.Step.Type} object
     */
    public Type getType() {
        return this.type;
    }
    /**
     * Return the name of this step. This is for descriptive information only, it has no impact on the
     * computations
     *
     * @return the name of the step, if supplied by the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add gate to the list of gates for this step
     *
     * @param gate gate to add
     * @throws java.lang.IllegalArgumentException in case the supplied Gate operates on a qubit that is already
     * been operated on in this step
     */
    public void addGate(Gate gate) throws IllegalArgumentException {
        verifyUnique(Objects.requireNonNull(gate));
        gates.add(gate);
    }

    /**
     * Adds the multiple Gates to the list of gates for this step
     *
     * @param moreGates more gates
     * @throws java.lang.IllegalArgumentException in case the supplied Gate operates on a qubit that is already
     * been operated on in this step
     */
    public void addGates(Gate... moreGates) throws IllegalArgumentException {
        for( Gate g: moreGates ){
            addGate(g);
        }
    }

    /**
     * <p>Getter for the field <code>gates</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<Gate> getGates() {
        return Collections.unmodifiableList(gates);
    }
    
    /**
     * Remove a Gate from this step
     *
     * @param g the Gate that should be removed
     */
    public void removeGate(Gate g) {
        gates.remove(g);
    }
    
    /**
     * <p>Setter for the field <code>complexStep</code>.</p>
     *
     * @param idx a int
     */
    public void setComplexStep(int idx) {
        this.complexStep = idx;
    }
    
    /**
     * <p>Getter for the field <code>complexStep</code>.</p>
     *
     * @return a int
     */
    public int getComplexStep() {
        return this.complexStep;
    }

    /**
     * <p>setInformalStep.</p>
     *
     * @param b a boolean
     */
    public void setInformalStep(boolean b) {
        this.informal = b;
    }

    /**
     * <p>isInformal.</p>
     *
     * @return a boolean
     */
    public boolean isInformal() {
        return informal;
    }

    /**
     * <p>Setter for the field <code>index</code>.</p>
     *
     * @param s a int
     */
    public void setIndex(int s) {
        this.index = s;
        this.complexStep = s;
    }
    
    /**
     * <p>Getter for the field <code>index</code>.</p>
     *
     * @return a int
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * <p>Setter for the field <code>program</code>.</p>
     *
     * @param p a {@link org.redfx.strange.Program} object
     */
    public void setProgram(Program p) {
        this.program = p;
    }

    /**
     * <p>Getter for the field <code>program</code>.</p>
     *
     * @return a {@link org.redfx.strange.Program} object
     */
    public Program getProgram() {
        return this.program;
    }

    /**
     * <p>setInverse.</p>
     *
     * @param val a boolean
     */
    public void setInverse(boolean val) {
        // TODO: https://github.com/redfx-quantum/strange/issues/93
        for (Gate g: gates) {
            if (g instanceof BlockGate) {
                ((BlockGate)g).inverse();
            } else {
                g.setInverse(val);
            }
        }
    }
    
    private void verifyUnique (Gate gate) {
        for (Gate g: gates) {
            long overlap = g.getAffectedQubitIndexes().stream().filter(gate.getAffectedQubitIndexes()::contains).count();
            if (overlap > 0) throw new IllegalArgumentException("Adding gate that affects a qubit already involved in this step");
        }
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        if (this.getType() == Step.Type.PSEUDO) {
            return "Pseudo-step";
        } else {
            return "Step with gates "+this.gates;
        }
    }

}
