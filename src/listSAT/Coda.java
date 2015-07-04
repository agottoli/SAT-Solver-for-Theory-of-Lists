package listSAT;

/**
 * Classe di una coda FIFO rappresentata con una lista
 * 
 * @author Alessandro Gottoli vr352595
 */
public class Coda {

	/**
	 * Elementi della coda
	 */
	public class ElemCoda {

		/**
		 * Valore del primo elemento (della coppia) 
		 */
		Object node1;
		
		/**
		 * Valore del secondo elemento (della coppia) 
		 */
		Object node2;
		
		/**
		 * Puntatore all'elemento successivo della coda
		 * null se non ce ne sono più
		 */
		ElemCoda next;

		/**
		 * Costruttore degli elementi della coda
		 * @param n1 primo valore della coppia da salvare
		 * @param n2 secondo valore della coppia da salvare
		 * @param c puntatore all'elemento successivo in coda
		 */
		public ElemCoda(Object n1, Object n2, ElemCoda c) {
			node1 = n1;
			node2 = n2;
			next = c;
		}
	}
	
	/**
	 * Puntatore al primo elemento della coda (testa)
	 */
	ElemCoda head;
	
	/**
	 * Puntatore all'ultimo elemento della coda
	 */
	ElemCoda last;
	
	/**
	 * Numenro di elementi presenti in coda
	 */
	int nOggetti;

	/**
	 * Costruttore di default di una coda vuota
	 */
	public Coda() {
		head = last = null;
		nOggetti = 0;
	}

	/**
	 * Costruisce l'elemento della coda e lo inserisce in coda
	 * 
	 * @param n1 primo valore della coppia da salvare in coda
	 * @param n2 secondo valore della coppia da salvare in coda
	 */
	public void insert(Object n1, Object n2) {
		// inserimento in coda
		//if (head == null)
		if (nOggetti == 0)
			head = last = new ElemCoda(n1, n2, null);
		else {
			last.next = new ElemCoda(n1, n2, null);
			last = last.next;
		}
		nOggetti++;
	}

	/**
	 * Costruisce l'elemento della coda e lo inserisce in coda
	 * 
	 * @param n array che contiene i due valori da salvare in coda
	 */
	public void insert(Object[] n) {
		// inserimento in coda
		insert(n[0], n[1]);
	}

	/**
	 * Rimuove e restituisce il primo elemento della coda (testa)
	 * 
	 * @return array che contiene i valori salvati nel primo elemento della coda
	 * @throws NullPointerException se la coda è vuota
	 */
	public Object[] remove() throws NullPointerException {
		//if (head == null)
		if (nOggetti == 0)
			throw new NullPointerException("Coda.remove(): la coda è vuota.");
		//return null;
		Object[] n = {head.node1, head.node2};
		head = head.next;
		return n;
	}

	/**
	 * Dimensione della coda
	 * 
	 * @return numero degli elementi salvati in coda
	 */
	public int size() {
		return nOggetti;
	}

	/**
	 * Indica se la coda è vuota
	 * 
	 * @return true se la coda è vuota, false altrimenti
	 */
	public boolean isEmpty() {
		return (nOggetti == 0);
	}

	/**
	 * Restituisce una stringa che rappresenta tutte le coppie di valori salvati
	 * nella coda
	 * 
	 * @return rappresentazione delle coppie di valori salvati in coda
	 */
	@Override
	public String toString() {
		ElemCoda o = head;
		StringBuilder s = new StringBuilder();
		while (o != null) {
			s.append(o.node1.toString()).append(" , ").append(o.node2.toString()).append("\n");
			o = o.next;
		}
		return s.toString();
	}
}
