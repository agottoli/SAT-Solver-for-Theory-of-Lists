/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listSAT;

/**
 *
 * @author ale
 */
public interface NodeInterface {
    
    	/**
	 * Imposta il rappresentante della classe del nodo
	 * 
	 * @param find puntatore al nodo rappresentante della classe
	 */
	public void setFind(NodeInterface find);

	/**
	 * Restituisce il puntatore al nodo contenuto nel campo find del nodo
	 * NOTA: non è detto che sia il rappresentante della classe
	 * 
	 * @return puntatore al nodo contenuto nel campo find
	 */
	//public NodeInterface getFind();

	/**
	 * Unisce un nodo alla lista dei padri del nodo
	 * 
	 * @param ccpar nodo da unire alla lista dei padri del nodo
	 */
	//public void addCCPar(NodeInterface par);

	/**
	 * Unisce una lista di nodi alla lista dei padri del nodo
	 * 
	 * @param ccpar lista di nodi da unire alla lista dei padri del nodo
	 */
	/*public void addCCPar(DoppiaList ccpar) {
		//if (this.ccpar == null)
		//	this.ccpar = ccpar;
		//else
		this.ccpar.union(ccpar);
	}*/

	/**
	 * Resetta la lista dei padri del nodo creando una lista vuota
	 */
	//public void resetCCPar();

	/**
	 * Restituisce la lista dei padri del nodo
	 * 
	 * @return la lista dei padri del nodo (e se è rappresentante anche quelli
	 *			dei nodi appartenenti alla sua classe
         *              null se non ci sono padri
	 */
	//public DoppiaList getCCPar();

	/**
	 * Restituisce il nome della funzione del nodo oppure il nome se non 
	 * è funzione
	 * 
	 * @return nome della funzione o nome dell'elemento
	 */
	//public String getFn();

	/**
	 * Restituisce un array di puntatori ai nodi che sono argomenti del
	 * nodo corrente
	 * null se il nodo corrente non è funzione
	 * 
	 * @return array dei nodi argomenti
	 */
	//public NodeInterface[] getArgs();

        
        
                                                                    /**
                                                                     * Inserisce gli elementi precedentemente aggiunti nella lista delle disugualianze
                                                                     * relative al nodo nella hash table appropriata. Grazie a questo meccanismo la dimensione
                                                                     * della hash table e' ottimale.
                                                                     */
                                                                    //public void store();
        
	// ESPERIMENTO
	/**
	 * Aggiunge un nodo alla lista dei nodi che non devono appartenere alla
	 * stessa classe del nodo corrente
	 * 
	 * @param div nodo da aggiungere 
	 */
	public void addDiversi(NodeInterface div);

	
	/**
	 * Aggiunge una lista di nodi alla lista dei nodi che non devono 
	 * appartenere alla stessa classe del nodo corrente
	 * 
	 * @param div lista dei nodi da aggiungere
	 */
	/*public void addDiversi(HashOpenPlusList div) {
		if (diversi == null)
                    diversi = div;
		else
                    diversi.union(div);
	}*/

	/**
	 * Restituisce la lista dei nodi che non devono appartenere alla stessa
	 * classe del nodo corrente
	 * 
	 * @return lista dei nodi 
	 */
	//public HashOpenInterface getDiversi();

	/**
	 * Restituisce l'arietà della funzione del nodo corrente
	 * 0 se non è una funzione
	 * 
	 * @return arietà del nodo corrente, 0 se non è una funzione
	 */
	//public int arity();

	/**
	 * Restituisce una stringa rappresentante il nodo corrente con i suoi 
	 * argomenti
	 * 
	 * @return stringa rappresentante il nodo
	 */
	@Override
	public String toString();


	//public int compareTo(Node node) {
	//	return this.toString().compareTo(node.toString());
	//}
	/**
	 * Compara due nodi confrontando la loro rappresentazione e ordinandoli in
	 * modo lessicografico
	 * 
	 * @param o nodo da comparare
	 * @return 0 se sono uguali
	 *			-1 se this è minore di o
	 *			1 se this è maggiore di o
	 */
	//public int compareTo(Object o);

	/**
	 * Imposta che il nodo è un atomo
	 */
	public void setAtom();
	
	/**
	 * Indica se il nodo è atomo
	 * 
	 * @return true se il nodo è atomo
	 *			false se non lo è oppure non si sa
	 */
	public Boolean isAtom();
	
	/**
	 * Imposta che il nodo è di tipo composto
	 */
	public void setCons();
	
	/**
	 * Indica se il nodo è di tipo composto
	 * 
	 * @return true se il nodo è di tipo composto
	 *			false se non è composto oppure non è noto
	 */
	public Boolean isCons();
	
	/**
	 * Restituisce il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 * 
	 * @return il rango della classe di equivalenza di cui il nodo è 
	 *			rappresentante
	 */
	//public int getRank();
	
	/**
	 * Incrementa il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 */
	//public void increaseRank();
        
        
                                                                            /**
                                                                             * Restituisce hashCode del nodo, calcolato sul campo relativo al nome completo del termine
                                                                             * Per efficenza dopo essere calcolato una prima volta e' salvato in locale per essere
                                                                             * disponibile in seguito senza essere nuovamente computato.
                                                                             */
                                                                            //@Override
                                                                            //public int hashCode();
                                                                            
        //public DoppiaList getEqClass();

    //public void setEqClass(DoppiaList eq);
    
    	/**
	 * Verifica che 2 nodi siano semanticamente equivalente, cioe' con lo stesso identificativo
	 * e con la stessa arieta'.
	 *
	 * @param n nodo da verificare con this
	 * @return true se n e' uguale a this, false altrimenti
	 */
	//public boolean nodeEquals(NodeInterface n);

    //public void resetDiversi();

    //public void resetEqClass();

    //public void setCCPar(DoppiaList dl);
    
    //public void setDiversi(HashOpenPlusList div);
    
    
    
}

