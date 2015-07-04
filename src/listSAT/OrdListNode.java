package listSAT;

import java.io.IOException;

/**
 * Classe rappresentante un nodo del grafo diretto aciclico
 * per calcolare la soddisfacibilità di una formula nella teoria delle liste
 * 
 * @author Alessandro Gottoli vr352595
 */
public class OrdListNode implements Comparable, NodeInterface {
	// int id; // inutile

	/**
	 * Etichetta del dopo, cioè la funzione del nodo oppure il nome 
	 * dell'elemento
	 */
	private String fn;
        
        private String stampaCompleta = null;
	
	/**
	 * Array contenente i puntatori ai codi argomento della funzione del 
	 * nodo corrente.
	 * null se il nodo non è funzione.
	 */
	private OrdListNode[] args; // arity = args.length
	
	/**
	 * Puntatore an nodo rappresentate la classe di equivalenza del nodo.
	 * Inizialmente punterà a se stesso perché ogni elemeno è per conto suo.
	 */
	private OrdListNode find; // uso del puntatore invece dell'id (int find);
	
	/**
	 * Lista contenente i puntatori a tutti i nodi padre del nodo.
	 * Poi se il nodo è rappresentante di una classe, conterrà anche i padri
	 * degli altri nodi appartenenti alla stessa classe.
	 */
	private OrdListaLink ccpar;// = null;
	
	/**
	 * Lista contenente i puntatori a tutti i nodi che non devono appartenere
	 * alla stessa classe del nodo corrente.
	 * Poi se il nodo è rappresentante di una classe, conterrà anche i nodi
	 * presenti nello stesso campo dei nodi della propria classe. 
	 */
	private OrdListaLink diversi;// = null;
	
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

	/**
	 * Costruttore del nodo
	 * 
	 * @param fn stringa contenente la funzione del nodo 
	 *				oppure il nome dell'elemento
	 * @param args array di puntatori ai nodi che sono argomenti della funzione
	 *				del nodo oppure null se il nodo non è una funzione
	 */
	public OrdListNode(String fn, OrdListNode[] args) {
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

    OrdListNode(String fn, NodeInterface[] ar) {
        OrdListNode[] args = null;
        if (ar != null) {
            args = new OrdListNode[ar.length];
            for (int i = 0; i < args.length; i++)
                args[i] = (OrdListNode) ar[i];
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
	public void setFind(OrdListNode find) {
		//DEBUG 
		//System.out.println("utilizzato un setFind su: " + this.toString() + " in " + find.toString());
		
		this.find = find;
	}

	/**
	 * Restituisce il puntatore al nodo contenuto nel campo find del nodo
	 * NOTA: non è detto che sia il rappresentante della classe
	 * 
	 * @return puntatore al nodo contenuto nel campo find
	 */
	public OrdListNode getFind() {
		return find;
	}

	/**
	 * Unisce un nodo alla lista dei padri del nodo
	 * 
	 * @param ccpar nodo da unire alla lista dei padri del nodo
	 */
	public void addCCPar(OrdListNode par) {
//		OrdListaLink oll = new OrdListaLink(ccpar);
		//oll.insert(0, ccpar);
		//System.out.println("oll: " + oll.toString());
//		addCCPar(oll);
            
            //DEBUG
            /*if (fn.equals("o")) {
                System.out.print("\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\no.ccpar: " + ccpar);
                if (ccpar != null) {
                    System.out.println("\n con nOggetti: " + ccpar.size());
                    String s = ccpar.toString();
                    int numeroOccorrenze = 1;
                    for(int j = 0;  j < s.length(); ++j) {
                            if (s.charAt(j) == ' ') {
                                ++numeroOccorrenze;
                            }
                    }
                    System.out.println(" effettivi:   " + numeroOccorrenze);
                }
                System.out.println("\n\n aggiungere: " + par + "\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n");
                try {
                    System.in.read();
                } catch (IOException e) {}
            }*/
                
            if (ccpar == null)
		ccpar = new OrdListaLink(par);
            else
                ccpar.insert(par);
            
            //DEBUG
            /*if (fn.equals("o")) {
                System.out.print("POSTINSERT\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\no.ccpar: " + ccpar);
                System.out.println("\n con nOggetti: " + ccpar.size());
                String s = ccpar.toString();
                int numeroOccorrenze = 1;
                for(int j = 0;  j < s.length(); ++j) {
                            if (s.charAt(j) == ' ') {
                                ++numeroOccorrenze;
                            }
                }
                System.out.println(" effettivi:   " + numeroOccorrenze);
                System.out.println("\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n");
                try {
                    System.in.read();
                } catch (IOException e) {}
            }*/
            
	}

	/**
	 * Unisce una lista di nodi alla lista dei padri del nodo
	 * 
	 * @param ccpar lista di nodi da unire alla lista dei padri del nodo
	 */
	public void addCCPar(OrdListaLink ccpar) {
		if (this.ccpar == null)
			this.ccpar = ccpar;
                else if (ccpar != null) {
                    
                    //DEBUG
                    /*if (fn.equals("o")) {
                        System.out.print("\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\no.ccpar: " + this.ccpar);
                        System.out.println("\n con nOggetti: " + this.ccpar.size());
                            String s = this.ccpar.toString();
                            int numeroOccorrenze = 1;
                            for(int j = 0;  j < s.length(); ++j) {
                                    if (s.charAt(j) == ' ') {
                                        ++numeroOccorrenze;
                                    }
                            }
                            System.out.println(" effettivi:    " + numeroOccorrenze);
    
                        System.out.println("\n\n aggiungere: " + ccpar + "\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n");
                        System.out.println("\n con nOggetti: " + ccpar.size());
                        s = ccpar.toString();
                        numeroOccorrenze = 1;
                            for(int j = 0;  j < s.length(); ++j) {
                                    if (s.charAt(j) == ' ') {
                                        ++numeroOccorrenze;
                                    }
                            }
                            System.out.println(" effettivi:    " + numeroOccorrenze);
                        try {
                            System.in.read();
                        } catch (IOException e) {}
                    }*/
                    
                    
                    this.ccpar.union(ccpar);
                    
                    //DEBUG
                    /*if (fn.equals("o")) {
                        System.out.print("POST UNION\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\no.ccpar: " + this.ccpar);
                        System.out.println("\n con nOggetti: " + this.ccpar.size());
                        String s = this.ccpar.toString();
                        int numeroOccorrenze = 1;
                        for(int j = 0;  j < s.length(); ++j) {
                                    if (s.charAt(j) == ' ') {
                                        ++numeroOccorrenze;
                                    }
                        }
                        System.out.println(" effettivi:    " + numeroOccorrenze);
                        System.out.println("\n\nOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\n\n");
                        try {
                            System.in.read();
                        } catch (IOException e) {}
                    }*/
                    
                }
	}

	/**
	 * Resetta la lista dei padri del nodo creando una lista vuota
	 */
	public void resetCCPar() {
		ccpar = null; // = new OrdListaLink();;
	}

	/**
	 * Restituisce la lista dei padri del nodo
	 * 
	 * @return la lista dei padri del nodo (e se è rappresentante anche quelli
	 *			dei nodi appartenenti alla sua classe
         *              null se non ci sono padri
	 */
	public OrdListaLink getCCPar() {
		return ccpar;
	}

	/**
	 * Restituisce il nome della funzione del nodo oppure il nome se non 
	 * è funzione
	 * 
	 * @return nome della funzione o nome dell'elemento
	 */
	public String getFn() {
		return fn;
	}

	/**
	 * Restituisce un array di puntatori ai nodi che sono argomenti del
	 * nodo corrente
	 * null se il nodo corrente non è funzione
	 * 
	 * @return array dei nodi argomenti
	 */
	public OrdListNode[] getArgs() {
		return args;
	}

	// ESPERIMENTO
	/**
	 * Aggiunge un nodo alla lista dei nodi che non devono appartenere alla
	 * stessa classe del nodo corrente
	 * 
	 * @param div nodo da aggiungere 
	 */
	public void addDiversi(OrdListNode div) {
		if (diversi == null)
                    diversi = new OrdListaLink(div);
                else
                    diversi.insert(div);
	}

	
	/**
	 * Aggiunge una lista di nodi alla lista dei nodi che non devono 
	 * appartenere alla stessa classe del nodo corrente
	 * 
	 * @param div lista dei nodi da aggiungere
	 */
	public void addDiversi(OrdListaLink div) {
		if (diversi == null)
			diversi = div;
                else if (div != null)
                    diversi.union(div);
	}

	/**
	 * Restituisce la lista dei nodi che non devono appartenere alla stessa
	 * classe del nodo corrente
	 * 
	 * @return lista dei nodi 
	 */
	public OrdListaLink getDiversi() {
		return diversi;
	}
        
        public void resetDiversi() {
            diversi = null;
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

	/**
	 * Restituisce una stringa rappresentante il nodo corrente con i suoi 
	 * argomenti
	 * indica anche i padri del nodo
	 * 
	 * @return stringa rappresentante il nodo compresi i padri
	 */
	public String toStringConCCPAR() {
		StringBuilder s = new StringBuilder(fn);
		if (arity() != 0) {
			s.append('(');
			int i = 0;
			for (; i < arity() - 1; i++)
				s.append(args[i].toString()).append(',');

			s.append(args[i].toString()).append(')');
		}
		s.append("\nccpar: ");

		if (ccpar != null && ccpar.size() > 0)
			s.append(ccpar.toString());
		else
			s.append("-vuoto-");

		return s.toString();
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
    @Override
	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}

	/**
	 * Imposta che il nodo è un atomo
	 */
	public void setAtom() {
		atom = true;
	}
	
	/**
	 * Indica se il nodo è atomo
	 * 
	 * @return true se il nodo è atomo
	 *			false se non lo è oppure non si sa
	 */
	public Boolean isAtom() {
		return atom;
	}
	
	/**
	 * Imposta che il nodo è di tipo composto
	 */
	public void setCons() {
		cons = true;
	}
	
	/**
	 * Indica se il nodo è di tipo composto
	 * 
	 * @return true se il nodo è di tipo composto
	 *			false se non è composto oppure non è noto
	 */
	public Boolean isCons() {
		return cons;
	}
	
	/**
	 * Restituisce il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 * 
	 * @return il rango della classe di equivalenza di cui il nodo è 
	 *			rappresentante
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Incrementa il rango della classe di equivalenza 
	 * di cui il nodo è rappresentante
	 */
	public void increaseRank() {
		rank++;
	}
        
        public Boolean isAtomConsSAT() {
            return !(atom && cons);
        }

    @Override
    public void setFind(NodeInterface find) {
        setFind((OrdListNode) find);
    }

    @Override
    public void addDiversi(NodeInterface div) {
        addDiversi((OrdListNode) div);
    }
}
