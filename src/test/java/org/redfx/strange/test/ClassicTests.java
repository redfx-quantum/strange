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
package org.redfx.strange.test;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.redfx.strange.algorithm.Classic;

/**
 *
 * @author johan
 */
public class ClassicTests {
    
    @Test
    public void random() {
        int z = 0;
        int o = 0;
        for (int i = 0; i < 100; i++) {
            int b = Classic.randomBit();
            if (b == 0) z++;
            if (b == 1) o++;
        }
        assertTrue (z > 10);
        assertTrue (o > 10);
    }
    
    @Test
    public void s00() {
        int sum = Classic.qsum(0, 0);
        assertEquals(0, sum);
    }

    @Test
    public void s01() {
        int sum = Classic.qsum(0,1);
        assertEquals(1, sum);
    }
    
       
    @Test
    public void s10() {
        int sum = Classic.qsum(1,0);
        assertEquals(1, sum);
    }
    
    @Test
    public void s12() {
        int sum = Classic.qsum(1,2);
        assertEquals(3, sum);
    }
    
    @Test
    public void s22() {
        int sum = Classic.qsum(2,2);
        assertEquals(0, sum);
    }
        
    @Test
    public void s413() {
        int sum = Classic.qsum(4,13);
        assertEquals(17, sum);
    }
  
    @Test
    public void quantumSearch() {
        Function<Person, Integer> f29Mexico
                = (Person p) -> ((p.getAge() == 29) && (p.getCountry().equals("Mexico"))) ? 1 : 0;
        for (int i = 0; i < 10; i++) {
            List<Person> persons = prepareDatabase();
            Collections.shuffle(persons);
            Person target = Classic.search(persons, f29Mexico);
            assertEquals(target.getAge(), 29);
            assertEquals(target.getCountry(), "Mexico");
        }
    }


    List<Person> prepareDatabase() {
        List<Person> persons = new LinkedList<>();
        persons.add(new Person("Alice", 42, "Nigeria"));
        persons.add(new Person("Bob", 36, "Australia"));
        persons.add(new Person("Eve", 85, "USA"));
        persons.add(new Person("Niels", 18, "Greece"));
        persons.add(new Person("Albert", 29, "Mexico"));
        persons.add(new Person("Roger", 29, "Belgium"));
        persons.add(new Person("Marie", 15, "Russia"));
        persons.add(new Person("Janice", 52, "China"));
        return persons;
    }

}
