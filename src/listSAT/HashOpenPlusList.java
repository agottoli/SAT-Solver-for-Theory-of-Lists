/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listSAT;

/**
 *
 * @author ale
 */
public class HashOpenPlusList implements HashOpenInterface {
        /** Tabella Hash */
	private Entry[] table;
	/** Lista delle locazioni occupate della tabella hash */
	private DoppiaList entryList;
	/** Numero totale degli assegnamenti nella tabella hash */
	private int count;
	/** Lista temporanea */
	private DoppiaList objList;
        /**
	 * Sulla tabella viene rieseguita la funzione hash
	 * quando la sua grandezza supera la soglia.
	 * Il valore di questo campo: ( int ) ( capacità * loadFactor )
	 */
	private int threshold;
	/** Fattore di carico della tabella hash */
	private static final float loadFactor = 0.75f;

    int getEntryListSize() {
        return entryList.size();
    }
        
        /**
	 * Implementa una entry dell'hash table
	 */
	private static class Entry {

		/** Valore associato a una chiave */
		private Object value;
		/** posizione reale della entry (hash % table.length)*/
		private int realPos;
                
                private String key;

		/**
		 * Costruttore predefinito di Entry.
		 *
		 * @param obj oggetto associato alla entry
		 * @param originalPos posizione predefinita della entry
		 */
		public Entry(Object obj, String k, int originalPos) {
			value = obj;
                        key = k;
			realPos = originalPos;
		}

		/**
		 * Restituise la stringa che rappresenta una voce.
		 *
		 * @return stringa che rappresenta una voce
		 */
		@Override
		public String toString() {
			return "[ " + value.toString() + " ]";
		}
	}
      
   /**
	 * Costruttore predefinito della tabella hash
	 * con indirizzamento a doppio hashing.
	 *
	 * @param size dimensione della tabella
	 */
	public HashOpenPlusList(int size) {
		table = new Entry[size];
		entryList = new DoppiaList();
		objList = new DoppiaList();
		count = 0;
		threshold = (int) (table.length * loadFactor);
	}

	/**
	 * Costruttore predefinito della tabella hash
	 * con indirizzamento a doppio hashing.
	 */
	public HashOpenPlusList() {
		//table = new Entry[8];
		//entryList = new DoppiaList();
		objList = new DoppiaList();
		//count = 0;
		//threshold = (int) (table.length * loadFactor);
	}

        	/**
	 * Esegue la funzione hash con il metodo della divisione.
	 *
	 * @param key chiave su cui eseguire la funzione hash.
	 * @return intero compreso tra 0 e grandezza tabella.
	 */
	private int hash(String key) { //(Object key) {
		return key.hashCode() % table.length;
	}

	/**
	 * Funzione di supporto alla funzione hash che attraverso il metodo
	 * della moltiplicazione ricava un numero compreso tra 0 e gradezzatabella.
	 *
	 * @param key oggettp da codificare.
	 * @return intero compreso tra 0 e grandezza tabella.
	 */
	private int probe(String key) { //(Object key) {
		double p = key.hashCode() * 0.6180;
		int result = (int) ((p - Math.floor(p)) * table.length);
		if (result == 0)
			return 1;
		return result;
	}
        
        /**
	 * Esegue la funzione hash che sfrutta 2 differenti algoritmi di hashing
	 * per codificare una Stringa in un intero. Per i=0 la funzione
	 * si comporta come la normale funzione hash. Per i>0 calcola la
	 * posizione con della voce supponendo ci siano i-1 chiavi con lo
	 * stesso codice hash.
	 *
	 * @param key chiave su cui eseguire la funzione hash.
	 * @param i salto su cui calcolare la funzione.
	 * @return intero compreso tra 0 e grandezza tabella.
	 */
	private int hash(String key, int i) { //(Object key, int i) {
		return Math.abs((hash(key) - i * probe(key)) % table.length);
	}

	/**
	 * Restituisce in numero di elementi salvati nella tabella.
	 *
	 * @return numero elementi salvati nella tabella
	 */
	public int size() {
		return count;
	}

	/**
	 * Controlle se un oggetto e' presente nella table hash.
	 *
	 * @param obj oggetto da controllare
	 * @return true se presente, false altrimenti
	 * @throws NullPointerException se l'oggetto e' null
	 */
	public boolean check(String key) throws NullPointerException { //(Object obj) throws NullPointerException {
		if (key == null)
			throw new NullPointerException("La chiave o il valore non possono essere nulli)");

		int i = 1;
		int hash = hash(key, 0);
		Entry e = table[hash];
		while (e != null) {
			if (key.equals(e.key))
				return true;
			e = table[hash(key, i++)];
		}
		return false;
	}


    /**
	 * Inserisce una nuova voce rispettando la regola
	 * dell'indirizzamento a doppio hashing.
	 * <BR> Salva l'oggetto nella posizione dalla tabella corrispondente alla funzione hash
	 * originale
	 * <BR> Se tale posizione non e' libera trova una nuova posizione doveall'allocare
	 * l'oggetto.
	 * <BR> Se un oggetto salvato e' semanticamente uguale ad un'altro contenuto
	 * restituisce il precedente senza modificare l'hash table
	 *
	 * @param obj oggetto da inserire
	 * @return oggetto inserito se non ne' e' presente un'altro uguale, quello precedente altrimenti
	 * @throws NullPointerException se l'oggetto inserito e' null
	 */
    @Override
	public Object put(String key, Object obj) throws NullPointerException { //(Object obj) throws NullPointerException {
		if (key == null || obj == null)
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
		table[hash] = new Entry(obj, key, hash(key, 0)); // ci va lo 0 perché è la posizione predefinita
		entryList = entryList.insert(hash);
		count++;
		return obj;
	}

                                                /**
                                                 * Inserisce un oggetto in una lista temporanea.
                                                 *
                                                 * @param obj da inserire
                                                 */
                                                public void insert(Object obj) {
                                                        objList = objList.insert(obj);
                                                }

                                                /**
                                                 * Inserisce i nodi presenti nella lista temporanea nella hashtable. Questo meccanismo
                                                 * e' utile se si vuole costruire una table hash di dimensioni ottimali.
                                                 */
                                                public void store() {
                                                        table = new Entry[objList.size() * 2];
                                                        entryList = new DoppiaList();
                                                        count = 0;
                                                        threshold = (int) (table.length * loadFactor);
                                                        for (int i = 0; i < objList.size(); i++) {
                                                            HLNode n = (HLNode) objList.iterator();
                                                                this.put(n.toString(), n);
                                                        }
                                                }
        
        /**
	 * Restituisce in sequenza gli oggetti salvati nella hash table. Per mantenere la
	 * consistenza tale metodo deve essere invocato un numero di volte pari
	 * al numero di oggetti inseriti.
	 *
	 * @return oggetto dell'hash table.
	 */
	public Object get() {
		return table[(Integer) entryList.iterator()].value;
	}
        

    @Override
        public Object get(String key) throws NullPointerException { //(Object obj) throws NullPointerException {
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
            
        }

	/**
	 *  Rimappa tutte le voci della tabella attuale su
	 *  una nuova di dimensioni doppie+1.
	 *  Considera le chiavi cancellate come nulle.
	 */
	private void rehash() {
		int oldCapacity = table.length;
		Entry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		Entry newMap[] = new Entry[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;

		// Rehash di tutte le voci.
		for (int i = 0; i < oldCapacity; i++)
			// Se e.value = null allora oggetto già cancellato
			// non viene considerato.
                        if (oldMap[i] != null && oldMap[i].key != null) {
			//if (oldMap[i] != null && oldMap[i].value != null) {
				int index = hash(oldMap[i].key, 0);
				for (int j = 1; table[index] != null; j++)
					index = hash(oldMap[i].key, j);

				table[index] = oldMap[i];
			}
	}

	/**
	 * Unisce 2 hashTable solo se gli oggetti presenti in esse solo semanicamente
	 * diversi. Se hashTable1 e hashTable2 sono null restituisce null, se hashTable1 e' null
	 * restitusce hashTable2, se hashTable2 e' null restituisce hashTable1.
	 * La tabella hash risultante e' ottenuta in tempo linere rispetto agli elementi contenuti
	 * nelle tabelle hash di ingresso.
	 *
	 * @param hashTable1 prima hash table da unire
	 * @param hashTable2 seconda hash table da inserire
	 * @return hash table unione delle precedenti
	 */
	public static HashOpenPlusList disjoinUnion(HashOpenPlusList hashTable1, HashOpenPlusList hashTable2) {
		if (hashTable1 == null && hashTable2 == null)
			return null;
		if (hashTable1 == null)
			return hashTable2;
		if (hashTable2 == null)
			return hashTable1;

		HashOpenPlusList result = new HashOpenPlusList(((hashTable1.count + hashTable2.count) * 2));
		copy(hashTable1, result);
		copy(hashTable2, result);

		return result;
	}
        
        /**
	 * Copia gli oggetti di una hash table in un'altra.
	 *
	 * @param orig hash table di input
	 * @param result hash table risultante
	 */
	private static void copy(HashOpenPlusList orig, HashOpenPlusList result) {
		for (int i = 0; i < orig.entryList.size(); i++) {
			Entry obj = (Entry) orig.table[(Integer) orig.entryList.iterator()];
			result.put(obj.key, obj.value);
		}
	}

	/**
	 * Restituisce la rappresentazione di tutte le
	 * voci di una tabella con relativo hash e posizione originale
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(" Entry: \tPosizione \t  Hash \t  RealPos\n");
		for (int i = 0; i < table.length; i++)
			if (table[i] != null)
				s.append(table[i].toString()).append("\t\t ").append(i).append("\t\t ").append(table[i].key.hashCode()).append("\t\t ").append(table[i].realPos).append("\n");
			else
				s.append(" null \n");
		return s.toString();
	}

    
}
