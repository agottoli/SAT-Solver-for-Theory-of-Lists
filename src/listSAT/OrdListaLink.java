package listSAT;

/**
 * Classe che rappresenta un lista ordinata per chiavi
 * 
 * @author Alessandro Gottoli vr352595
 */
public class OrdListaLink {
	
	/**
	 * Classe degli elementi salvati nella lista
	 */
	private class ElemLista {
    	
    	/** Elemento contenuto nel nodo. */ 
        Comparable key;
        
        /** Riferimento al successivo elemento. */
        ElemLista next;
        
        /** Costruttore di default del nodo. */
        ElemLista(Comparable o) {
            key = o;
            next = null;
        }

		/**
		 * Compara l'elemento della lista corrente con quello passato come 
		 * parametro in odinamento lessicografico
		 * 
		 * @param n2 elemento con cui confronare
		 * @return 0 se sono uguali
		 *         -1 se l'elemento corrente è minore del parametro
		 *         1 se l'elemento corrente è maggiore del parametro
		 */
		private int compareTo(ElemLista n2) {
			return key.compareTo((Comparable) n2.key);
		}
		
		/**
		 * Restituisce una rappresentazione della key dell'elemento della lista
		 * 
		 * @return stringa che rappresenta la key dell'elemento della lista
		 */
		@Override
		public String toString() {
			return key.toString();
		}
    }

	/**
     * Testa della lista.
     */
    private ElemLista head;
	
	private ElemLista last;
	
	/**
	 * Indice di supporto della procedura scan che tiene traccia del nodo a cui
	 * siamo arrivati ogni volta che si chiama la procedura
	 */
	private ElemLista scanIndex;
    
    /**
     * Numero di oggetti presenti.
     */
    private int nOggetti=0;

    /**
     * Costruttore di default.
     */
    public OrdListaLink() {
        scanIndex = last = head = null;
        nOggetti = 0;
    }
	
	/**
	 * Costruttore della lista con un elemento passato per parametro
	 * 
	 * @param c elemento di partenza (unico presente nella lista da creare) 
	 */
	public OrdListaLink(Comparable c) {
		ElemLista n = new ElemLista(c);
		scanIndex = last = head = n;
		nOggetti = 1;
	}
	
	/**
	 * Unisce la lista corrente con quella passata come parametro
	 * mantenendo i nodi ordinati
	 * 
	 * @param lista lista da unire alla corrente
	 */
	void union(OrdListaLink lista) throws NullPointerException {
            if (lista == null)
                throw new NullPointerException("OrdListaLink.union: la lista da unire non può essere null.");
            
		if (isEmpty()) {
			head = lista.head;
			last = lista.last;
			nOggetti = lista.size();
			
		} else if (!lista.isEmpty()) {
			ElemLista n1 = head;
			ElemLista n2 = lista.head;
			ElemLista preN1 = null;
			
			//DEBUG
			//System.out.println("ALMENO UNA UNION NON VUOTA");
			
			// salta se l'elemento è uguale
			if (n1.compareTo(n2) == 0)
				n2 = n2.next;
			
			if (n2 != null) {
				if (n1.compareTo(n2) > 0) {
					head = n2;
					n2 = n2.next;
					head.next = n1;
					preN1 = head;
					nOggetti++;
				} else { // deve essere < 0 perché di = ce ne può essere solo 1
					preN1 = n1;
					n1 = n1.next;
				}
			}

			while (n1 != null && n2 != null) {
				if (n1.compareTo(n2) < 0) { // n1 < n2
					preN1 = n1;
					n1 = n1.next;
				} else if (n1.compareTo(n2) == 0) { // n1 = n2
					// elemento già presente in elenco, allora lo salto
					n2 = n2.next;
                                        preN1 = n1;
					n1 = n1.next;
				} else {
					// n2 < n1
					// devo inserire n2 prima di n1
					preN1.next = n2;
                                        preN1 = n2;
					n2 = n2.next;
					preN1.next = n1;
					nOggetti++;
				}
			}
			
			if (n1 == null) {
				// appendo tutti gli elementi restanti di n2 
				preN1.next = n2;
				last = lista.last;
				while (n2 != null) {
					nOggetti++;
					n2 = n2.next;
				}
			}
		}
		
		scanIndex = head;
	}

        
    public void insert(Comparable c) {
        if (c == null)
            throw new NullPointerException("OrdListaLink.insert: l'elemento da inserire non può essere null.");
        
        //System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        if (isEmpty()) {
            last = head = new ElemLista(c);
            nOggetti++;
        } else {
            ElemLista nuovo = new ElemLista(c);
            
            if (head.compareTo(nuovo) < 0) {
                    ElemLista el = head;
                    while (el.next != null && el.next.compareTo(nuovo) < 0)
                        el = el.next;
                    if (el.next == null) {
                        el.next = nuovo;
                        last = nuovo;
                        nOggetti++;
                    } else if (el.next.compareTo(nuovo) > 0) {
                        nuovo.next = el.next;
                        el.next = nuovo;
                        nOggetti++;
                    } // else il nodo è uguale e allora non faccio niente
                } else if (head.compareTo(nuovo) > 0) {
                    nuovo.next = head;
                    head = nuovo;
                    nOggetti++;
                } // else head = nuovo (nodo doppio)
        }
        scanIndex = head;
    }
	
	//void union(ListaLink ccpar) {
	//	last.next = ccpar.head;
	//	last = ccpar.last;
	//}
    
    /**
     * @see Lista#get(int, Object)
     */
    public Object get(int k) throws IndexOutOfBoundsException{
        if(k<0 || k>=nOggetti )
            throw new IndexOutOfBoundsException("OrdListaLink.get(): indice non valido");
        if(k==0)
            return this.head.key;
        else{
            ElemLista indice = head;
            // scorro la lista fino al k-esimo elemento.
            for(int i=0 ; i<k ; i++)
                indice = indice.next;
            return indice.key; // key o non key?? ----------------------------------------------
        }
    }
    
    /**
     * @see Lista#size()
     */
    public int size(){
        return nOggetti;
    }
    
    /**
     * @see Lista#Empty()
     */
    public boolean isEmpty(){
    	return (nOggetti == 0);
    }
    
    /**
     *  @see Lista#remove(int)
     */
    public void remove(int k) throws IndexOutOfBoundsException {
        if(k<0 || k>=nOggetti)
        	throw new IndexOutOfBoundsException("OrdListaLink.remove(): indice non valido");
        ElemLista t = this.head;
        /* il ciclo for si ferma un passo prima per non perdere
           il riferimento al padre dell'oggetto k */
        for(; k > 1; k--)
        	t = t.next;
        t.next = t.next.next;
		
		scanIndex = head;
		
		if (k == --nOggetti)
			last = t;
        //nOggetti--;
    }

	@Override
	public String toString() {
		System.out.println("\n---->nOgg " + nOggetti);
		if (!isEmpty()) {
			//System.out.println("???");
			ElemLista o = head;
			StringBuilder s = new StringBuilder("(");
			s.append(o.toString());
			while (o.next != null) {
				s.append(", ").append(o.next.toString());
				o = o.next;
			}
			s.append(")");
			return s.toString();
		}
		return "";
	}
	
	/**
	 * Costruisce un array con tutti gli elementi contenuti nella lista
	 * 
	 * @return array di tutti gli elementi contenuti nella lista
	 */
	public Comparable[] toArray() {
            //DEBUG
            //System.err.println("nOggetti = " + nOggetti);
		Comparable[] array = new Comparable[nOggetti];
		ElemLista n = head;
		for (int i = 0; i < nOggetti; i++) {
                    //DEBUG
                    //System.err.println("ELEMENTO " + i + "-esimo: " + n);
			array[i] = n.key;
			n = n.next;
		}
		return array;
	}

	/**
	 * Restituisce il nodo puntato dall'indice scanIndex e aggiorna l'indice
	 * all'elemento successivo
	 * Serve per scansionare tutta la lista in ordine senza doverla ripercorrere
	 * tutta ogni volta per arrivare ad un certo indice
	 * 
	 * @return il nodo puntato dall'indice scanIndex
	 */
	public Comparable scan() {
		//if (scanIndex != null) {
			Comparable k = scanIndex.key;
			if (scanIndex.next != null)
				scanIndex = scanIndex.next;
			else
				scanIndex = head;
			return k;
		//}
		
		//throw new NullPointerException("OrdListaLink.scan(): la lista è vuota.");
	}
}
