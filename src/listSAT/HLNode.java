package listSAT;

/**
 *
 * @author ale
 */
public class HLNode implements NodeInterface {
// int id; // inutile

	/**
	 * Etichetta del dopo, cioè la funzione del nodo oppure il nome 
	 * dell'elemento
	 */
	private String fn;
        
        
        private String stampaCompleta = null; //  fnComplete
	
	/**
	 * Array contenente i puntatori ai codi argomento della funzione del 
	 * nodo corrente.
	 * null se il nodo non è funzione.
	 */
	private HLNode[] args; // arity = args.length
	
	/**
	 * Puntatore an nodo rappresentate la classe di equivalenza del nodo.
	 * Inizialmente punterà a se stesso perché ogni elemeno è per conto suo.
	 */
	private HLNode find; // uso del puntatore invece dell'id (int find);
	
	/**
	 * Lista contenente i puntatori a tutti i nodi padre del nodo.
	 * Poi se il nodo è rappresentante di una classe, conterrà anche i padri
	 * degli altri nodi appartenenti alla stessa classe.
	 */
	private DoppiaList ccpar;// = null;
	
	/**
	 * Lista contenente i puntatori a tutti i nodi che non devono appartenere
	 * alla stessa classe del nodo corrente.
	 * Poi se il nodo è rappresentante di una classe, conterrà anche i nodi
	 * presenti nello stesso campo dei nodi della propria classe. 
	 */
	private HashOpenPlusList diversi;// = null; // neq
	
	/**
	 * Indica se un nodo deve essere di tipo atomo
	 * true = questo nodo e tutti quelli della classe devono essere atomi
	 * false = è di tipo non atomo (cons) oppure non si sa.
	 */
	private Boolean atom = false;
	
	/**
	 * Indica se un nodo deve essere di tipo non atomo (cons)
	 * true = questo nodo e tutti quelli della classe devono essere non atomi
	 * false = è di tipo atomo oppure non si sa.
	 */
	private Boolean cons = false;
	
	/**
	 * Rango del nodo
	 * rappresenta una stima dei nodi appartenenti alla classe del nodo
	 * e serve per unire sempre le classi più piccole alle classi più grandi.
	 */
	private int rank;
        
        /** Rappresenta la classe di equivalenza del nodo */
	private DoppiaList equivalenceClass;

	/**
	 * Costruttore del nodo
	 * 
	 * @param fn stringa contenente la funzione del nodo 
	 *				oppure il nome dell'elemento
	 * @param args array di puntatori ai nodi che sono argomenti della funzione
	 *				del nodo oppure null se il nodo non è una funzione
	 */
	public HLNode(String fn, HLNode[] args) {
		this.fn = fn;
		this.args = args;
		rank = 0;
		find = this;
		// find e ccpar hanno sempre valore di default alla crezione del nodo
//		ccpar = new OrdListaLink();
//		diversi = new OrdListaLink();

		// QUANDO SARA' PRONTA LA LISTA
		//System.out.println("arity: " + arity());
		for (int i = 0; i < arity(); i++)
			args[i].addCCPar(this);
		
		//DEBUG
		//System.out.println("Costruzione nuovo nodo: " + this.toString());
	} // OK

    HLNode(String fn, NodeInterface[] ar) {
        HLNode[] args = null;
        if (ar != null) {
            args = new HLNode[ar.length];
            for (int i = 0; i < args.length; i++)
                args[i] = (HLNode) ar[i];
        }
        
        this.fn = fn;
		this.args = args;
		rank = 0;
		find = this;
		// find e ccpar hanno sempre valore di default alla crezione del nodo
//		ccpar = new OrdListaLink();
//		diversi = new OrdListaLink();

		// QUANDO SARA' PRONTA LA LISTA
		//System.out.println("arity: " + arity());
		for (int i = 0; i < arity(); i++)
			args[i].addCCPar(this);
		
		//DEBUG
		//System.out.println("Costruzione nuovo nodo: " + this.toString());
    
    }

	/**
	 * Imposta il rappresentante della classe del nodo
	 * 
	 * @param find puntatore al nodo rappresentante della classe
	 */
	public void setFind(NodeInterface find) {
		//DEBUG 
		//System.out.println("utilizzato un setFind su: " + this.toString() + " in " + find.toString());
		
		this.find = (HLNode) find;
	} // OK

	/**
	 * Restituisce il puntatore al nodo contenuto nel campo find del nodo
	 * NOTA: non è detto che sia il rappresentante della classe
	 * 
	 * @return puntatore al nodo contenuto nel campo find
	 */
	public HLNode getFind() {
		return find;
	} // OK

	/**
	 * Unisce un nodo alla lista dei padri del nodo
	 * 
	 * @param ccpar nodo da unire alla lista dei padri del nodo
	 */
	public void addCCPar(HLNode par) {
//		OrdListaLink oll = new OrdListaLink(ccpar);
		//oll.insert(0, ccpar);
		//System.out.println("oll: " + oll.toString());
//		addCCPar(oll);
            if (ccpar == null)
		ccpar = new DoppiaList();
            ccpar = ccpar.insert(par);
	} // OK

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
	public void resetCCPar() {
		ccpar = null; // new OrdListaLink();
	}

	/**
	 * Restituisce la lista dei padri del nodo
	 * 
	 * @return la lista dei padri del nodo (e se è rappresentante anche quelli
	 *			dei nodi appartenenti alla sua classe
         *              null se non ci sono padri
	 */
	public DoppiaList getCCPar() {
		return ccpar;
	} // OK

	/**
	 * Restituisce il nome della funzione del nodo oppure il nome se non 
	 * è funzione
	 * 
	 * @return nome della funzione o nome dell'elemento
	 */
	public String getFn() {
		return fn;
	} // OK

	/**
	 * Restituisce un array di puntatori ai nodi che sono argomenti del
	 * nodo corrente
	 * null se il nodo corrente non è funzione
	 * 
	 * @return array dei nodi argomenti
	 */
	public HLNode[] getArgs() {
		return args;
	} // OK

        
        
                                                                    /**
                                                                     * Inserisce gli elementi precedentemente aggiunti nella lista delle disugualianze
                                                                     * relative al nodo nella hash table appropriata. Grazie a questo meccanismo la dimensione
                                                                     * della hash table e' ottimale.
                                                                     */
                                                                    public void store() {
                                                                            if (diversi != null)
                                                                                    diversi.store();
                                                                    }
        
	// ESPERIMENTO
	/**
	 * Aggiunge un nodo alla lista dei nodi che non devono appartenere alla
	 * stessa classe del nodo corrente
	 * 
	 * @param div nodo da aggiungere 
	 */
	public void addDiversi(HLNode div) {
            //if (diversi == null)
            //    diversi = new HashOpenPlusList(); //(8);
            //diversi.put(div.toString(), div);
            insertNEQ(div);
	} // nella HashOpenPlusList non ci sarebbe la key
        
   // IL METODO SOPRA NON è QUELLO GIUSTO DELLA FILOSOFIA
                                            /**
                                             * Inserisce il nodo n di disugualianza. Se e' il primo oggetto a essere inserito
                                             * e' creata un nuova lista.
                                             *
                                             * @param n nodo di disugualianza
                                             */
                                            public void insertNEQ(HLNode n) {
                                                    if (diversi == null)
                                                            diversi = new HashOpenPlusList();
                                                    diversi.insert(n); // NON LI INSRISCE IN TABELLA MA SOLO ALLA LISTA
                                                    //System.out.println("NEQ: " + n + " in " + this + " ");
                                            }

	
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
	public HashOpenPlusList getDiversi() {
		return diversi;
	}

	/**
	 * Restituisce l'arietà della funzione del nodo corrente
	 * 0 se non è una funzione
	 * 
	 * @return arietà del nodo corrente, 0 se non è una funzione
	 */
	public int arity() {
		if (args == null)
			return 0;
		return args.length;
	}

	/**
	 * Restituisce una stringa rappresentante il nodo corrente con i suoi 
	 * argomenti
	 * 
	 * @return stringa rappresentante il nodo
	 */
	@Override
	public String toString() {
            if (stampaCompleta == null) {
		if (arity() == 0)
			return fn;
		StringBuilder s = new StringBuilder(fn);
		s.append('(');
		int i = 0;
		for (; i < arity() - 1; i++)
			s.append(args[i].toString()).append(',');

		s.append(args[i].toString()).append(')');

		stampaCompleta = s.toString();
            }
            return stampaCompleta;
	}


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
	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}

	/**
	 * Imposta che il nodo è un atomo
	 */
	public void setAtom() {
		atom = true;
	} // OK
	
	/**
	 * Indica se il nodo è atomo
	 * 
	 * @return true se il nodo è atomo
	 *			false se non lo è oppure non si sa
	 */
	public Boolean isAtom() {
		return atom;
	} // OK
	
	/**
	 * Imposta che il nodo è di tipo composto
	 */
	public void setCons() {
		cons = true;
	} // OK
	
	/**
	 * Indica se il nodo è di tipo composto
	 * 
	 * @return true se il nodo è di tipo composto
	 *			false se non è composto oppure non è noto
	 */
	public Boolean isCons() {
		return cons;
	} // OK
	
	/**
	 * Restituisce il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 * 
	 * @return il rango della classe di equivalenza di cui il nodo è 
	 *			rappresentante
	 */
	public int getRank() {
		return rank;
	} // OK
	
	/**
	 * Incrementa il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 */
	public void increaseRank() {
		rank++;
	} // OK
        
        
                                                                            /**
                                                                             * Restituisce hashCode del nodo, calcolato sul campo relativo al nome completo del termine
                                                                             * Per efficenza dopo essere calcolato una prima volta e' salvato in locale per essere
                                                                             * disponibile in seguito senza essere nuovamente computato.
                                                                             */
                                                                            @Override
                                                                            public int hashCode() {
                                                                                    return toString().hashCode();
                                                                            }
                                                                            
        public DoppiaList getEqClass() {
            return equivalenceClass;
        }

    public void setEqClass(DoppiaList eq) {
        equivalenceClass = eq;
    }
    
    	/**
	 * Verifica che 2 nodi siano semanticamente equivalente, cioe' con lo stesso identificativo
	 * e con la stessa arieta'.
	 *
	 * @param n nodo da verificare con this
	 * @return true se n e' uguale a this, false altrimenti
	 */
	public boolean nodeEquals(HLNode n) {
		if (this == n)
			return true;
                // possono avere anche argomenti diversi 
                // ( ??? ma nella costruzione ho messo che un nodo fn deve avere sempre la stessa arietà)
		return fn.equals(n.fn) && ((args == null && n.args == null) || args.length == n.args.length);
	}

    public void resetDiversi() {
        diversi = null;
    }

    public void resetEqClass() {
        equivalenceClass = null;
    }

    public void setCCPar(DoppiaList dl) {
        ccpar = dl;
    }
    
    public void setDiversi(HashOpenPlusList div) {
        diversi = div;
    }

    @Override
    public void addDiversi(NodeInterface div) {
        addDiversi((HLNode) div);
    }

}

