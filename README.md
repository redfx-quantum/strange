# strange [![Build Status](https://travis-ci.org/gluonhq/strange.svg?branch=master)](https://travis-ci.org/gluonhq/strange)
Quantum Computing API for Java

This project defines a Java API that can be used to create Quantum Programs.
A Quantum Program, defined by <code>com.gluonhq.strange.Program</code> can be executed on an implementation of the 
<code>com.gluonhq.strange.QuantumExecutionEnvironment</code>.


# Getting Started

Strange is distributed via the traditional Java distribution channels (e.g. maven central and jcenter) and can thus easily be used leveraging maven or gradle build software.

Using gradle

A typical build.gradle file looks as follows:
```gradle
apply plugin: 'java'
apply plugin: 'application'

repositories {
    mavenCentral()
}

dependencies {
    compile 'com.gluonhq:strange:0.0.1'
}

mainClassName = 'SimpleStrangeDemo'

```

The sample application contains a single Java file:
```java
import com.gluonhq.strange.*;
import com.gluonhq.strange.gate.*;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import java.util.Arrays;

public class SimpleStrangeDemo {

    public static void main(String[] args) {
        Program p = new Program(2);
        Step s = new Step();
        s.addGate(new X(0));
        p.addStep(s);
        Step t = new Step();
        t.addGate(new Hadamard(0));
        t.addGate(new X(1));
        p.addStep(t);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        Arrays.asList(qubits).forEach(q -> System.out.println("qubit with probability on 1 = "+q.getProbability()+", measured it gives "+ q.measure()));
    }

}
```

This sample create a <code>Program</code> that requires 2 qubits. It will create 2 steps (<code>s</code> and <code>t</code>).
The first step adds a Paul-X (NOT) Gate to the first qubit. 
The second steps adds a Hadamard Gate to the first qubit, and a NOT gate to the second qubit.
Both steps are added to the <code>Program</code>.

In order to "run" this program, we need a <code>QuantumExecutionEnvironment</code>. Strange comes with a 
<code>SimpleQuantumExecutionEnvironment</code> which contains a very simple, non-optimized quantum computer simulator.

After running the program on this simulator, we inspect the state of the Qubits. As expected, there is a 50% chance the first qubit (which had an X and an H gate) will be in the <code>0</code> state, and a 50% chance it will be in the <code>1</code> state. The second qubit will always be in the <code>1</code> state.


Running this application a number of times will consistently give the same probabilities, and different measurements.

# Visualisation

The Strange API's allow to create and simulate quantum programs. A companion project, [StrangeFX](https://github.com/gluonhq/strangefx) , allows to visualise programs, and create them with a simple drag and drop interface. The sample program above rendered via StrangeFX looks as follows:
![StrangeFX rendering](https://github.com/gluonhq/strangefx/blob/master/docs/images/simpleview.png)

