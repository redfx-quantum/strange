package org.redfx.strange.test;

public class Person {
    
    private final String name;
    private final int age;
    private final String country;
    
    public Person(String name, int age, String country) {
        this.name = name;
        this.age = age;
        this.country = country;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public String getCountry() {
        return this.country;
    }
}
