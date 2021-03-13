package ex1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashTableTest {



/*
      1.count()
      -Error: El mètode count no funcionava, per tant la variable count no s'incrementava en cap costat
      Observant l'error, si es sobreescriu un element no ha d'augmentar la mida de size.
      2.size()
      -Retorna correctament la mida fixa de l'array
      3.put()
      -En aquestes proves unitàries s'ha provoca col·lisions amb el mateix hash
      per veure que realment els col·loca en l'ordre adequat i on toca
      Segons comprovo les col·lisions estan bé, en el seu ordre
      Error: Fallava a l'escriure un element amb la mateixa key, el repetia,també al intentar sobrescriure no al substituïa
      4.get()
      -Tot bé, no detecto errors
      5.drop()
      -Error: Si es borraba el primer element, el esborrava tot, col·lisions incloses
      -Error: Si l'element s'ha esborra tenia una key més gran que col·lisions anteriors hi donava NULL POINTER EXCEPTIONS
      Sembla que hi esta solucionat

      Errors trobats total: 5
      Error extra: Abans no podies afegir elements negatius ara si.


*/

    HashTable hs = new HashTable();

    @ParameterizedTest
    @CsvSource({"3","7","17"})
    void count(int n) {
        for (int i = 0; i <n ; i++) {
            hs.put(String.valueOf(i), "value "+i);
        }
        Assertions.assertEquals(n, hs.count());
        System.out.println(hs.toString());

        // Aquí sobreescriurà y comprovara si count es correcto
        hs.put("0", "value sobrescrit");
        Assertions.assertEquals(n, hs.count());
        System.out.println(hs.toString());

    // Hem repetit dues vegades sobreescriurà si no donava cap error
    //        for (int i = 0; i <n ; i++) {
    //            hs.put(String.valueOf(i), "nou value "+i);
    //        }
    //        Assertions.assertEquals(n, hs.count());
    //        System.out.println(hs.toString());

    // Aquí introdueix un key ja existent per verificar si count segueix sent correcte
    //        hs.put("0", "nou value sobrescrit");
    //        System.out.println(hs.toString());
    //        Assertions.assertEquals(n, hs.count());
    }


    @org.junit.jupiter.api.Test
    void size() {
         Assertions.assertEquals(16, hs.size());
         System.out.println(hs.toString());


    }


    @Test
    void put() {

        hs.put("0", "valor 0");
        hs.put("1", "valor 1");

        // Modifico el valor con el key "1"
        hs.put("1", "valor 1");
        assertEquals("valor 1", hs.get("1"));

        // Afegeixo un valor vacio
        hs.put("2", "");
        assertEquals("", hs.get("2"));

        // Afegeixo dos nous valors en el bucket[0] para comprovar las col·lisions
        hs.put("77", "valor 7");
        hs.put("11", "valor 11");
        assertEquals("valor 7", hs.get("77"));

        //Modifico el valor que es troba en mig
        hs.put("77", "valor 7");

        //Comprovar que puc afegir diferentes tipos de caracteres como key
        hs.put("valor 7", "77");

        //Comprovar que puc afegir numero negatiu com a key
        hs.put("-5", "valor -5");


        //Comprovar que tenim 16 taules de hash
        assertEquals(16, hs.size());

        //Comprovar que tots els valors es troba en els buckets corresponents

        assertEquals("\n" +
                " bucket[0] = [0, valor 0] -> [77, valor 7] -> [11, valor 11]\n" +
                " bucket[1] = [1, valor 1]\n" +
                " bucket[2] = [2, ]\n" +
                " bucket[8] = [-5, valor -5]\n" +
                " bucket[11] = [valor 7, 77]", hs.toString());

        System.out.println(hs.toString());

    }



    @Test
    void get() {

       //Aqui introdueixo una dada i tot seguit ho esborro i verifico que el mètode get torna el valor correcte

        hs.put("0","value 0");
        hs.put("-3","value 3");

        //Comprovar que puc cerca la keys especifica
        assertEquals("value 0", hs.get("0"));
        //Comprovar que puc cerca keys amb numeros negatius
        assertEquals("value 3", hs.get("-3"));

        // esborro una key per comprovar si es existeix o no a continuació
        hs.drop("0");
        // Aqui fem que la posició zero no hi ha element i retorna correctament null
        assertEquals(null, hs.get("0"));

        //Aqui li demano un element que no existeix i retorna null correctament
        assertEquals(null, hs.get("2"));

        System.out.println(hs.toString());


    }



    @Test
    void drop() {
        hs.put("1", "valor 1");
        hs.put("2", "valor 2");
        hs.put("1234", "valor 3");
        hs.put("-5", "valor -5");

        //Comprovo que puc esborrar un valor
        hs.drop("1");

        //Afegeixo 3 valors al bucket [0]
        hs.put("0", "valor 1");
        hs.put("77", "valor 7");
        hs.put("11", "valor 11");

        //Comprovo si puc esborrar el valor del mig
        hs.drop("77");

        //Comprovo si puc esborrar l'últim valor de l'bucket [0] enmig
        hs.drop("11");

        //Comprovo si puc esborrar un valor amb un key de 4 dígits
        hs.drop("1234");

        //Comprovar que tots els valors es troba en els buckets corresponents
        assertEquals("\n" +
                " bucket[0] = [0, valor 1]\n" +
                " bucket[2] = [2, valor 2]\n" +
                " bucket[8] = [-5, valor -5]", hs.toString());
        System.out.println(hs.toString());


    }
}