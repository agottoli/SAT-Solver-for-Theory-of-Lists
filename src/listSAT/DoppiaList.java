/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listSAT;

/**
 *
 * @author Alessandro Gottoli vr352595
 */
public class DoppiaList {
    
    /**
	 * Implementa un nodo della lista
	 */
	private class ListNode {

		/** Puntatore al prossimo nodo */
		private ListNode next;
		/** Puntatore al nodo precedente */
		private ListNode prev;
		/** Oggetto del nodo */
		private Object obj;

		/**
		 * Costruttore predefinito
		 *
		 * @param obj1 oggetto del nodo
		 */
		public ListNode(Object obj1) {
			obj = obj1;
		}

		/**
		 * Rappresentazione del nodo. Restituisce la rappresentazione
		 * dell'oggetto contenuto del nodo.
		 *
		 * @return rappresetazione del nodo
		 */
		@Override
		public String toString() {
			return obj.toString();
		}
	}
        
        
        /** Testa della lista */
	public ListNode head;
	/** Numero elementei della lista */
	public int nElementi;
	/** Puntatore per l'accesso efficiente */
	public ListNode index;
        
        public DoppiaList l1 = null;
        public DoppiaList l2 = null;
        public int indexContatore;

	/**
	 * Costruttore per una lista vuota.
	 */
	public DoppiaList() {
		nElementi = 0;
                indexContatore = 0;
	}
        
        /**
	 * Costruttore per una lista con un solo elemento
	 *
	 * @param obj oggetto del primo nodo della lista
	 */
	public DoppiaList(Object obj) {
		head = new ListNode(obj);
		head.prev = head;
		head.next = head;
		index = head;
		nElementi = 1;
                indexContatore = 0;
	}
        
            /**
	 * Restituisce in sequenza gli elementi della lista. Per mantenere la
	 * consistenza e' necessario invocare questo metodo un numero di volte
	 * pari alla lunghezza della lista
	 *
	 * @return oggetto della lista
	 * @throws IllegalStateException se la lista e' vuota
	 */
	public Object iterator() throws IllegalStateException {
		if (nElementi == 0)
			throw new IllegalStateException("List.iterator(): Lista vuota");
           // NON SI PUO' FARE COSI     
           /*     if ((l1 == null || l1.nElementi == 0) 
                        && (l2 == null || l2.nElementi == 0)) {
                    // siamo nella lista di base
                    if ((indexContatore = indexContatore % nElementi) == 0)
                        index = head;
                    indexContatore++;
                    ListNode t = index;
                    index = index.next;
                    return t.obj;
                }
                
                if (indexContatore < l1.nElementi)
                    return l1.iterator();
                return l2.iterator();
            */
                
                if ((indexContatore = indexContatore % nElementi) == 0)
                        index = head;
                    indexContatore++;
                    ListNode t = index;
                    index = index.next;
                    return t.obj;
                

	}

    /**
	 * Unisce 2 liste in tempo costante. La lista risultante e' la concatenazione
	 * della prima con la seconda.
	 *
	 * @param List1 prima lista da unire
	 * @param List2 seconda lista da unire
	 * @return concatenazione di List1,List2
	 */
	public static DoppiaList union(DoppiaList List1, DoppiaList List2) {
		if (List1 == null && List2 == null)
			return null;
		if (List1 == null || List1.nElementi == 0)
			return List2;
		if (List2 == null || List2.nElementi == 0)
			return List1;
		DoppiaList result = new DoppiaList();
		result.head = List1.head;

		ListNode last1 = List1.head.prev;
		ListNode last2 = List2.head.prev;
		List1.head.prev.next = List2.head;
		List2.head.prev.next = List1.head;
		List1.head.prev = last2;
		List2.head.prev = last1;

		result.nElementi = List1.nElementi + List2.nElementi;
		result.index = result.head;
                
                result.l1 = List1;
                result.l2 = List2;
		return result;
	}

    /**
	 * Inserisce un oggetto in coda alla lista
	 *
	 * @param obj oggetto da inseritre
	 * @return lista modificata
	 */
	public DoppiaList insert(Object obj) {
		if (nElementi == 0)
			return new DoppiaList(obj);

		ListNode t = new ListNode(obj);
		t.prev = head.prev;
		t.next = head;
		t.prev.next = t;
		head.prev = t;
		nElementi++;
		return this;
	}

	/**
	 * Restituisce il numero di elementi della lista
	 *
	 * @return numero di elementi
	 */
	public int size() {
		return nElementi;
	}
        
        private void notificaRimozioneElemento(int i) {
            if (nElementi == 0)
			throw new IllegalStateException("notificaRimozioneElemento: lista vuota");
            
            nElementi--;
    //            System.out.println("NOTIFICA: nElementi = " + nElementi + "rimuovendo il " + i);
   /*             try {
                    System.in.read();
                } catch (Exception e) {}
    */
            if (l1 != null 
                 && i < l1.size() && i > 0) {
                        l1.notificaRimozioneElemento(i);
            } else if (l2 != null && l2.size() > 0) {
                        l2.notificaRimozioneElemento(i - l1.size());
            }
            // else non c'è niente da notificare
    
        }

    /**
	 * Rimuove l'ultimo oggetto restituito dal metodo iterator();
         * (il precedente di index)
	 *
	 * @throws IllegalStateException se la lsita e' vuota
	 */
	public void remove() {
		if (nElementi == 0)
			throw new IllegalStateException("Remove: lista vuota");
                
                //if (l1 == null && l2 == null) {
                    // siamo alla lista base
                    if (index.prev == head) {
			//head.prev.next = head.next;
			head = index;//.next;
                    }
                    index.prev = index.prev.prev;//index.prev.prev = index;
                    index.prev.next = index;
                    nElementi--;
    //            System.out.println("RIMOZIONE ELEMENTO: nElementi = " + nElementi);
  /*              try {
                    System.in.read();
                } catch (Exception e) {}
    */            
                    // devo dire alle liste sotto 
                    // che il numero di elementi è cambiato
                    if (l1 != null 
                            && indexContatore <= l1.size() && indexContatore > 0) {
        //                System.out.println("l1: nElementi = " + l1.nElementi);
                        l1.notificaRimozioneElemento(indexContatore - 1);
                    } else if (l2 != null && l2.size() > 0) {
        //                System.out.println("l2: nElementi = " + l2.nElementi);
                        l2.notificaRimozioneElemento(indexContatore - l1.size() - 1);
                    }
                    // else non c'è niente da notificare
                    
                //}
               /* 
               if (indexContatore < l1.size() && indexContatore > 0) {
                   // siamo nella l1 che dobbiamo eliminare
                   l1.remove();
               } else {
                   // siamo nella l2
                   l2.remove();
               }
               nElementi--;
                     * 
                     */
                    
                    
                    
 /*                   
                if (indexContatore)
		if (index.prev == head) {
			//head.prev.next = head.next;
			head = index;//.next;
		}
		index.prev = index.prev.prev;//index.prev.prev = index;
                index.prev.next = index;
		nElementi--;
  */
	}

    /**
	 * Diminuisce di un'unità il contatore degli oggetti presenti nella lista.
	 * E' utile nel caso si sia rimosso un oggetto da una lista e si vuole segnalare
	 * la diminuizione del numero degli oggetti a una lista più grande che contiene quella
	 * iniziale.
	 */
	public void decrease() {
		nElementi--;
	}

     /**
	 * Rappresentazione della lista.
	 *
	 * @return rappresentazione della lista
	 */
	@Override
	public String toString() {
		if (nElementi == 0)
			return "vuota";
		StringBuilder s = new StringBuilder();
		for (int i=0; i < nElementi-1; i++) {
			s.append(index.obj).append(",");
			index = index.next;
		}
		ListNode t = index;
		index = index.next;
		return s.append(t.obj).toString();
	}   
        
   
}
