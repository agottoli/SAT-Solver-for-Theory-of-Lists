package listSAT;

/**
 * Classe che imprementa un hashtable ad indirizzamento aperto 
 * con doppio hashing
 * 
 * NOTA: questa hashtable è favorevole per inserimento e ricerca,
 *       ma non va troppo bene per la rimozione di un nodo
 * 
 * @author Alessandro Gottoli vr060886
 */
public class HashOpenStandardDoppioH implements HashOpenInterface {
    
    /**
     * Tabella hash (dizionario) che contiene gli oggetti key-value.
     */
    private Entry[] table;
    
    /**
     * Numero di oggetti inseriti nella tabella hash.
     */
    private int count;
    
    /**
     * Indice di carico della tabella.
     * La tabella verrà reindirizzata quando la dimensione eccede il carico.
     * 
     * (The value of this field is ( int )( capacity * loadFactor )).
     * 
     */
    private int threshold;
    
    /**
     * Fattore di carico (α) della tabella. (Quello che non voglio superare).
     */
    private float loadFactor;
    
	/**
	 * Elementi salvati nella tabella hash 
	 */
    public static class Entry {
        
        /**
         * Valore dell'oggetto.
         */
        Object value;
        
        /**
         * Chiave associata all'oggetto.
         */
        String key; // equivale a value.toString()
        
        /**
         * Valore che indica se la cella è libera.
         */
        boolean free; // usato solo se si cancella
        
        /**
         * Costruttore dell'oggetto che associa key - value. 
         * (free = false) indica che la cella contiene un oggetto.
         * 
         * @param key chiave identificativa.
         * @param value valore dell'oggetto.
         */
        Entry(String key, Object value) {
            this.key = key;
            this.value = value;
            this.free = false;
        }
        
        /**
         * Restituisce il valore della chiave di un oggetto 
         * presente in dizionario.
         * 
         * @return stringa col valore della chiave.
         */
        public String getKey() {
            return key;
        }
        
        /**
         * Restituisce il valore dell'oggetto presente nel dizionario.
         * 
         * @return valore dell'oggetto.
         */
        public Object getValue() {
            return value;
        }
        
        /**
         * Imposta il valore di un oggetto nel dizionario.
         * 
         * @param o oggetto da inserire.
         */
        public void setValue(Object o) {
            value = o;
        }
        
        /**
         * Indica se un elemento è vuoto.
         * 
         * @return true se è vuoto, false altrimenti.
         */
        public boolean isEmpty() {
            return (free);
        }
        
        /**
         * Restituisce le rappresentazione grafica della coppia
         * chiave e oggetto associato.
         * 
         * @return stringa (key, value) di un elemento in dizionario.
         */
        @Override
        public String toString(){
            if (isEmpty()) return ("deleted");
            return ("(" + getKey() + ", " + getValue() + ")");
        }
    }
    
    /**
     * Costruttore della table hash.
     * 
     * @param m dimensione della tabella.
     */
    public HashOpenStandardDoppioH(int m){
        this.table = new Entry[m];
        this.count = 0;
        this.loadFactor = 0.75f;
        this.threshold = (int) (m * loadFactor);
    }
    
    /**
     * Indica se un dizionario è vuoto.
     * 
     * @return true se la tabella è vuota (no kay-value), false altrimenti.
     */
    public boolean isEmpty(){
        return (count == 0);
    }
    
    /**
     * Restituisce il valore dell'oggetto associato ad una determinata key
     * nel dizionario.
     * 
     * @param key stringa di una chiave da ricercare nel dizionario.
     * @return valore dell'oggetto associato alla key presente nel dizionario, 
     *         null altrimenti.
     * @exception NullPointerException se la chiave inserita è null.
     */
    public Object get(String key) throws NullPointerException {
        
         if (key == null)
		throw new NullPointerException("La chiave o il valore non possono essere nulli)");

		int i = 1;
		int hash = hash(key, 0);
		Entry e = table[hash];
		while (e != null) {
			if (key.equals(e.key))
				return e.value;
			e = table[hash(key, i++)];
		}
		return null;
        
        /*
        // controllo eccezioni
        if (key == null) 
            throw new NullPointerException("la chiave non può essere nulla");
        
        int index = hash(key);
        for (Entry e = table[index]; e != null; 
                    index = (index - probe(key)) % table.length, e = table[index]){
            if (e.key.equals(key) && !e.isEmpty()) return e.value;
        }
        return null;
        */

    }
    
    /**
     * Inserisce un elemento nel dizionario associando la determinata chiave 
     * al determinato valore dell'oggetto.
     * 
     * @param key la chiave.
     * @param value il valore dell'oggetto.
     * @return il valore precedente associato alla chiave in questo dizionario,
     *         null se non l'aveva.
     * @exception NullPointerException se almeno uno tra chiave e valore è null.
     * @see Object#equals(Object).
     */
    public Object put(String key, Object value) throws NullPointerException {
        if (key == null || value == null)
			throw new NullPointerException("La chiave o il valore non possono essere nulli)");

		if (count >= threshold)
			rehash();

		int i = 0;
		String old; //Object old;
		int hash = hash(key, 0);
		Entry e = table[hash];
		while (e != null) {
			old = e.key; //e.value;
			if (key.equals(old))// andrebbe bene lo stesso (obj.equals(old))
				return old;
			hash = hash(key, ++i);
			e = table[hash];
		}
		table[hash] = new Entry(key, value); // ci va lo 0 perché è la posizione predefinita
		//entryList = entryList.insert(hash);
		count++;
		return value;
        /*
        
        
        // controllo eccezioni
        if (value == null || key == null)
            throw new NullPointerException("OpenHash.put(): la chiave " +
					"o il valore non possono essere null");
        // controllo se la chiave è già presente in dizionario
        Entry[] tab = table;
        int index = hash(key);
        for (Entry e = tab[index]; e != null; 
                        index = (index + probe(key)) % tab.length, e = tab[index]) {
            if (e.getKey().equals(key)) {
                Object old = e.value;
                e.value = value;
                if (e.isEmpty()) {
                    e.free = false;
                    return null;
                }
                return old;
            }
        }
        // controllo il carico della tabella hash
        if (count > threshold) {
            // troppo carica: effettuo il rehash
            rehash();
            tab = table;   
        }
        // inserisco nuovo valore
        // cerco la prima cella libera spettante al hash della chiave
        // in modo da non avere una serie di celle vuote dovute alle
        // cancellazioni precedenti, favorendo così le prestazioni
        index = hash(key, 0);
        for (Entry e = tab[index]; e != null && !e.isEmpty(); 
                        index = (index - probe(key)) % tab.length, e = tab[index]);
        tab[index] = new Entry(key, value);
        count++;
        return null;
         */        
    }

    
    /**
     * Rieffettua l'hash a tutte le chiavi. 
     */
    protected void rehash() {
        
        //DEBUG
        //System.out.println("Rehashing in corso di tableSize = " + table.length);
        
        int oldCapacity = table.length;
        Entry[] oldMap = table;
        int newCapacity = oldCapacity * 2 + 1;
        // creo una tabella di hash più grande
        Entry[] newMap = new Entry[newCapacity];
        threshold = (int)(newCapacity * loadFactor);
        table = newMap;
        // Rehash di tutti gli elementi
        int index;
        count = 0;
        for (int i = oldCapacity - 1; i >= 0; i--) {
            if (oldMap[i] != null && !oldMap[i].isEmpty()) {
                index = hash(oldMap[i].getKey());
                // reinserisco ciascun elemento nella nuova tabella
                put(oldMap[i].getKey(), oldMap[i].getValue());
            }
        }
    }
    
    /**
     * Codifica la chiave stringa in un numero long.
     * Lavora in base 256. s[0]*256^(n-1) + s[1]*256^(n-1) + ... + s[n]*256^[0].
     * 
     * @param key chiave da codificare.
     * @return chiave codificata in un intero.
     */    
    protected static long code(String key) {
        long cod = 0;
        for (int i = key.length() - 1; i >= 0; i--) {
            cod += ((int)(key.charAt(i)) * Math.pow(256, i));
        }
        return cod;
    }
    
    /**
     * Effettua l'hash delle chiavi.
     * 
     * @param key chiava da codificare
     * @return numero di hash
     */
                            private int hash(String key) { //(Object key) {
                                    return (key.hashCode() % table.length);
                            }
    /*protected int hash(String key) { // LA MALEDETTA CHE CI METTE UN CASINO
        int hashC = 0;
        long cod = code(key);
        while (cod > 0) {
            hashC += cod % 1000;
            cod /= 1000;
        }
        return (hashC % table.length);
    }*/
                            
                            private int hash(String key, int i) { //(Object key, int i) {
                                return Math.abs((hash(key) - i * probe(key)) % table.length);
                            }
    
    /**
     * Probe della funzione indirizzamento doppio hash.
     * 
     * @param key chiave da codificare.
     * @return intero compreso tra 0 e dimensione tabella hash.
     */
    protected int probe(String key) {
        double code = code(key) * 0.6180;
        return (int)((code - Math.floor(code)) * (this.table.length - 1)) + 1;
    }
    
    /**
     * Stampa il contenuto della tabella hash.
     */
    public void stampa(){
        for (int i = 0; i < table.length; i++){
            System.out.print(i + ":\t");
            if (table[i] == null) System.out.println("-empty-");
            else if (table[i].isEmpty()) System.out.println("\"deleted\"");
            else {
                String key = table[i].getKey();
                System.out.println("(" + key + ", " + code(key) + ", " 
                     + hash(key) + ", " + table[i].getValue().toString() + ")");
            }
        }
    }
	
	/**
	 * Restituisce le dimensioni della tabella hash.
	 * 
	 * @return dimensioni della tabella hash
	 */
	public int tableSize() {
		return table.length;
	}
        
        public int size() {
            return count;
        }
        
        
        public Object getElemNumero(int i) {
            if (i < 0 || i >= table.length)
                throw new IndexOutOfBoundsException("HashOpen.getElemNumero: indice fuori dai limiti.");
            if (table[i] == null)
                throw new NullPointerException("HashOpen.getElemNumero: cella vuota.");
            return table[i].getValue();
        }
}
