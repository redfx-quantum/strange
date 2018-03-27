# strange [![Build Status](https://travis-ci.org/gluonhq/strange.svg?branch=master)](https://travis-ci.org/gluonhq/strange)
Quantum Computing API for Java

This project defines a Java API that can be used to create Quantum Programs.
A Quantum Program, defined by <code>com.gluonhq.strange.Program</code> can be executed on an implementation of the 
<code>com.gluonhq.strange.QuantumExecutionEnvironment</code>.


# Getting Started

Strange is distributed via the traditional Java distribution channels (e.g. maven central and jcenter) and can thus easily be used leveraging maven or gradle build software.

Using gradle

A typical build.gradle file looks as follows:
<code>
apply plugin: 'java'
apply plugin: 'application'

repositories {
    mavenCentral()
}

dependencies {
    compile 'com.gluonhq:strange:0.0.1'
}

mainClassName = 'com.gluonhq.strange.demo.Demo'

</code>

