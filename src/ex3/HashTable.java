package ex3;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

import java.util.ArrayList;

/**
 * Implementació d'una taula de hash sense col·lisions.
 * Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
 */
public class HashTable {
    private int SIZE = 16;
    private int ITEMS = 0;
    private HashEntry[] entries = new HashEntry[SIZE];

    public int count(){
        return this.ITEMS;
    }

    public int size(){
        return this.SIZE;
    }

    /**
     * Permet afegir un nou element a la taula.
     * @param key La clau de l'element a afegir.
     * @param value El propi element que es vol afegir.
     */
    public void put(String key, String value) {
        //codi arreglat
        boolean SobreEscriu = false;
        int hash = getHash(key);
        final HashEntry hashEntry = new HashEntry(key, value);

        if(entries[hash] == null) {
            entries[hash] = hashEntry;
            //sumar element per al count
            ITEMS++;
        }
        else {
            HashEntry temp = entries[hash];
            while(temp.next != null ) {
                /*
                 Aqui mentre va avançant per cerca l'últim element, vaig comprovant si la key introduïda
                 no sigui d'un element ja existent, en aquest cas, el sobreescriu.
                 Y pongo un boolean per quant arribi al último elemento no escrigui res
                */
                if (temp.key.equals(key)) {
                    temp.value = value;
                    SobreEscriu = true;
                }
                temp = temp.next;
            }
            //Aqui repeteixo el mateix bucle anterior perquè entri en el cas que no hi hagi col·lisions

            if (temp.key.equals(key)) {
                temp.value = value;
                SobreEscriu = true;
            }
            // Aqui entra si no ha sobreescrit cap element

            if (!SobreEscriu){
                // Temporal buit igual key y value
                temp.next = hashEntry;
                // Desa a previus el ultim valor que hi havia
                hashEntry.prev = temp;
                ITEMS++;
            }
        }
    }

    /**
     * Permet recuperar un element dins la taula.
     * @param key La clau de l'element a trobar.
     * @return El propi element que es busca (null si no s'ha trobat).
     */
    public String get(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {
            HashEntry temp = entries[hash];

            while( !temp.key.equals(key))
                temp = temp.next;

            return temp.value;
        }

        return null;
    }

    /**
     * Permet esborrar un element dins de la taula.
     * @param key La clau de l'element a trobar.
     */
    public void drop(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {

            HashTable.HashEntry temp = entries[hash];
            while( (!temp.key.equals(key)) && (temp.next != null)) {

                temp = temp.next;
            }
/*
  Amb el while anterior verificant el null o el if de tot seguit verificant la key
  Soluciono el tema que quan esborrava un element que ja havia estat esborrat
  Succeïa que l'element següent tenia una key superior a la que estava buscant per eliminar
  Llavors per molt next que fes, mai trobava l'element que se vol esborrar i donava Null pointer Exception

  En el cas que l'element que busco per esborrar estigués darrere d'altres elements, llavors si que en el
   While anterior va entrant i fent next buscant l'element, i si no hi ha cap problema.

 */
            if (temp.key.equals(key)) { // UFFF verifica key - si la key no existe se lo salta xtodo y no hace nada.
                if (temp.prev == null) {
                    // Aqui no actua correctament, Ara en cas de ser el primer element l'esborra deixant els següents
                    entries[hash] = entries[hash].next;  // Aquí al primer valor li dono el valor del següent
                    if (entries[hash] != null)
                        entries[hash].prev = null; // En cas de de tindra el siguiente el hash el borra sencer
                    // }
                } else {
                    if (temp.next != null)
                        //esborrem temp, per tant actualitzem l'anterior al següent
                        temp.next.prev = temp.prev;
                    //esborrem temp, per tant actualitzem el següent de l'anterior
                    temp.prev.next = temp.next;
                }
            }
        }
    }

    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return key.hashCode() % SIZE;
    }

    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    @Override
    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (HashEntry entry : entries) {
            if(entry == null) {
                bucket++;
                continue;
            }

            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            HashEntry temp = entry.next;
            while(temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @return Una clau que, de fer-se servir, provoca col·lisió amb la que s'ha donat.
     */
    //Afagida ArrayList per que doni millor la lista de filas de cada hash
    public ArrayList<String> getCollisionsForKey(String key) {
        return getCollisionsForKey(key, 1);
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     * @param key La clau que es farà servir per calcular col·lisions.
     * @param quantity La quantitat de col·lisions a calcular.
     * @return Un llistat de claus que, de fer-se servir, provoquen col·lisió.
     */
    public ArrayList<String> getCollisionsForKey(String key, int quantity){
        /*
          Main idea:
          alphabet = {0, 1, 2}

          Step 1: "000"
          Step 2: "001"
          Step 3: "002"
          Step 4: "010"
          Step 5: "011"
           ...
          Step N: "222"

          All those keys will be hashed and checking if collides with the given one.
        * */

        final char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> newKey = new ArrayList();
        ArrayList<String> foundKeys = new ArrayList();

        newKey.add(0);
        int collision = getHash(key);
        int current = newKey.size() -1;

        while (foundKeys.size() < quantity){
            //building current key
            String currentKey = "";
            for(int i = 0; i < newKey.size(); i++)
                currentKey += alphabet[newKey.get(i)];

            if(!currentKey.equals(key) && getHash(currentKey) == collision)
                foundKeys.add(currentKey);

            //increasing the current alphabet key
            newKey.set(current, newKey.get(current)+1);

            //overflow over the alphabet on current!
            if(newKey.get(current) == alphabet.length){
                int previous = current;
                do{
                    //increasing the previous to current alphabet key
                    previous--;
                    if(previous >= 0)  newKey.set(previous, newKey.get(previous) + 1);
                }
                while (previous >= 0 && newKey.get(previous) == alphabet.length);

                //cleaning
                for(int i = previous + 1; i < newKey.size(); i++)
                    newKey.set(i, 0);

                //increasing size on underflow over the key size
                if(previous < 0) newKey.add(0);

                current = newKey.size() -1;
            }
        }

        return  foundKeys;
    }

    /// Aqui hi estaba tot el main

}