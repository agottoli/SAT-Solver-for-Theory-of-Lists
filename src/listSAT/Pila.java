package listSAT;

/**
 * Classe che rappresenta una pila (o Stack) 
 * cioè una coda di tipo LIFO
 * 
 * @author Alessandro Gottoli vr352595
 */
public class Pila {
	
	/**
	 * Elementi della pila
	 */
	public class ElemPila {
		
		/** Oggetto salvato nella pila */
		Object node;
		/** Puntatore all'oggetto successivo salvato nella pila */
		ElemPila next;
		
		/**
		 * Costruttore dell'elemento da salvare nella pila
		 * 
		 * @param n oggetto da salvare
		 * @param c puntatore all'elemento successivo
		 */
		public ElemPila(Object n, ElemPila c) {
			node = n;
			next = c;
		}
	}
	
	/**
	 * Puntatore all'elemento in testa alla pila
	 */
	ElemPila head;
	
	/**
	 * Costruttore di una pila vuota
	 */
	public Pila() {
		head = null;
	}
	
	/**
	 * Inserisce l'elemento passato come parametro nella pila
	 * 
	 * @param n elemento da inserire nella pila
	 */
	public void push(Object n) {
		// inserimento in testa
		head = new ElemPila(n, head);
	}
	
	/**
	 * Rimuove e restituisce l'elemento in cima alla pila
	 * 
	 * @return elemento in cima alla pila
	 */
	public Object pop() {
		if (head == null)
			throw new NullPointerException("Pila.pop(): la pila è vuota");
		Object n = head.node;
		head = head.next;
		return n;
	}
	
	/**
	 * Controlla se la pila è vuota
	 * 
	 * @return true se la pila è vuota, false altrimenti
	 */
	public Boolean isEmpty() {
		return head == null;
	}
	
}
