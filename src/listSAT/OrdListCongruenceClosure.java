package listSAT;

/**
 * Classe che implementa l'algoritmo di chiusura di congruenza per 
 * individuare la soddisfacibilità o la insoddisfacibilità della teoria delle
 * liste
 * 
 * @author Alessandro Gottoli vr352595
 */
public class OrdListCongruenceClosure {

	/**
	 * Metodo principale che indica se le uguaglianze e disuquaglianze passate
	 * come parametri sono soddisfacibili
	 * 
	 * @param clausole array di 2 code, la prima rappresenta le uguaglianze
	 *					la seconda indica le disuguaglianze 
	 * @return true se è soddisfacibile, false altrimenti
	 */
	public static Boolean soddisfacibilita(Object[] clausole) {
		Coda uguagl = (Coda) clausole[0];
		Coda disuguagl = (Coda) clausole[1];
                HashOpenStandardDoppioH nodi = (HashOpenStandardDoppioH) clausole[2];
		/*
		Node[] dis =  (Node[]) disuguaglianze.remove();
		while (dis != null) {
		dis[0].addDiversi(dis[1]);
		dis[1].addDiversi(dis[0]);
		}
		 */

		Object[] ug = null;
		Boolean flag = true;
		try {
			ug = uguagl.remove();
		} catch (NullPointerException e) {
			flag = false;
			System.out.println(e.getLocalizedMessage());
		}
                OrdListNode p1;
                OrdListNode p2;
		while (flag) {
                    if ((p1 = find((OrdListNode) ug[0])) != 
                            (p2 = find((OrdListNode) ug[1]))) {
                        // faccio il merge solo se i due nodi non sono già nella
                        // stessa classe
                        // questo serve perché è stato rimosso il controllo dentro
                        // al metodo merge per efficienza
			try {
				merge(p1, p2);
			} catch (IllegalStateException e) {
				System.out.println(e.getMessage());
				return false;
			}
                    }
			try {
				ug = uguagl.remove();
			} catch (NullPointerException e) {
				flag = false;
			}

		}

		//System.out.println("Controllo tutte le disuguaglianze per essere sicuro di averle rispettate tutte.");
		Object[] dis;
                flag = true;
		do {
			try {
				dis = disuguagl.remove();
			} catch (NullPointerException e) {
                            //return true;
                            
                            // RISOLVO atom(cons(x,y)) = INSODD
                            
                            // CONTROLLARE NODO PER NODO SE SONO SODDISFATTI isAtom vs isCons
                            for (int i = 0; i < nodi.tableSize(); i++) {
                                try {
                                    OrdListNode n = (OrdListNode)nodi.getElemNumero(i);
                                    if (!n.isAtomConsSAT())
                                        return false;
                                    
                                    // controlla che cdr(cons(x,y)) = cons(x,y) non sia permesso
                                    if (n.getFn().equals("cdr") 
                                            && find(n.getArgs()[0]) == find(n) 
                                            //&& n.getArgs()[0].isCons())
                                            && find(n).isCons())
                                        return false;
                                    
                                        
                                } catch (IndexOutOfBoundsException ex) {
                                } catch (NullPointerException ex) {
                                }
                            }
                            return true;
			}
                        
			if (find((OrdListNode) dis[0]) == find((OrdListNode) dis[1])) {
				System.out.println(((OrdListNode) dis[0]).toString() + " != "
						+ ((OrdListNode) dis[1]).toString() + " non è rispettato.\nINSODDISFACIBILE");
				return false;
			}
		} while (true);

                
                

	}

	/**
	 * Trova il rappresentante della classe di equivalenza del nodo passato
	 * come parametro
	 * 
	 * @param node nodo di cui vogliamo trovare il rappresentante
	 * @return puntatore al nodo rappresentante della classe del nodo passato
	 */
	private static OrdListNode find(OrdListNode node) {

		//DEBUG
		//System.out.println("find " + node.toString() + " =====> " + node.getFind().toString());

		if (node.getFind().equals(node))
			return node;
		OrdListNode rappr = find(node.getFind());
		node.setFind(rappr); // Compressione dei cammini
		return rappr;
	}

	/**
	 * Trova tutti i padri della classe di equivalenza del nodo passato 
	 * come parametro
	 * 
	 * @param node nodo appartenente alla classe di cui vogliamo calcolare i padri
	 * @return i padri della classe di equivalenza del nodo passato
	 */
	private static OrdListaLink ccPar(OrdListNode node) {
		return find(node).getCCPar();
	}

	/**
	 * Unisce le due classi di equivalenza dei rappresentanti passati come 
	 * parametri e controlla se risultano inconsistenze
	 * 
	 * @param rappr1 rappresentante della classe che devo unire alla seconda
	 * @param rappr2 rappresentante della classe alla quale viene unita la prima
	 */
	private static void union(OrdListNode rappr1, OrdListNode rappr2) {

		//DEBUG
		//System.out.print("UNION:\n\tr1: " + rappr1.toString());
		//System.out.println("\tr2: " + rappr2.toString());
		//Node rappr1 = find(n1); ottimizzazione dei find
		//Node rappr2 = find(n2);
		//find(n1).setFind(find(n2));

		//DEBUG
		//System.out.println("\tvediamo i campi da cambiare nella union...");
		//System.out.println("\t   " + rappr1.toString() + ".ccpar = " + rappr1.getCCPar());
		//System.out.println("\t   " + rappr2.toString() + ".ccpar = " + rappr2.getCCPar());

		
		if (rappr1.isAtom())
			rappr2.setAtom();
		
		if (rappr1.isCons())
			rappr2.setCons();
		
		if (!rappr2.isAtomConsSAT())
			throw new IllegalStateException("INSODDISFACIBILE!!!!");
		
		// in rappr2 sono più numerosi
		rappr1.setFind(rappr2);
		// sappiamo che sono già rappresentanti delle classi di congruenza
		// possiamo usare i metodi del nodo per aggiornare i ccpar
		rappr2.addCCPar(rappr1.getCCPar());
		rappr1.resetCCPar();
		if (rappr1.getRank() == rappr2.getRank())
			rappr2.increaseRank();

		//DEBUG
		//System.out.println("\tvediamo nuovi campi cambiati nella union...");
		//System.out.println("\t   find(" + rappr1.toString() + ") = " + rappr1.getFind());
		//System.out.println("\t   " + rappr1.toString() + ".ccpar = " + rappr1.getCCPar());
		//System.out.println("\t   " + rappr2.toString() + ".ccpar = " + rappr2.getCCPar());

		// bisogna fare la stessa cosa dei CCPAR anche con le liste dei
		// nodi delle disuguaglianze
		confrontaDisuguaglianze(rappr1, rappr2);

		rappr2.addDiversi(rappr1.getDiversi());

		//DEBUG
		//System.out.println("i diversi devono essere uniti");
		//System.out.println("\t" + rappr1.toString() + ".diversi = " + rappr1.getDiversi());
		//System.out.println("\t" + rappr2.toString() + ".diversi = " + rappr2.getDiversi());
		//System.out.println("Union finita");
	}

	/**
	 * Controlla se due nodi sono congruenti
	 * 
	 * @param n1 primo nodo da confrontare
	 * @param n2 secondo nodo da confrontare
	 * @return true se i due nodi sono congruenti, false altrimenti
	 */
	private static Boolean areCongruent(OrdListNode n1, OrdListNode n2) {
		int arity;

		//DEBUG
		//System.out.print("are Congruent " + n1.toString() + " and " + n2.toString() + " ?... ");

		if (n1.getFn().equals(n2.getFn()) && (arity = n1.arity()) == n2.arity()) {
			for (int i = 0; i < arity; i++)
				if (!find(n1.getArgs()[i]).equals(find(n2.getArgs()[i]))) {

					//DEBUG
					//System.out.println("FALSE");

					return false;
				}

			//DEBUG
			//System.out.println("TRUE");

			return true;
		}

		//DEBUG
		//System.out.println("FALSE");

		return false;

	}

	/**
	 * Vengono passati i rappresentanti delle classi dei due nodi che devono 
	 * essere uniti dall'uguaglianza e se sono diversi unisce le due classi
	 * Non importa l'ordine in cui vengono inserite le classi, perché il metodo
	 * unirà sempre la più piccola alla più grande
	 * NOTA: deve essere già il rappresentante almeno risparmio le chiamate a 
	 *       find ad ogni merge (si guadagna nei merge fatti sull'espandiCongruenze...)
	 * 
	 * @param rappr1 rappresentante della prima classe da unire
	 * @param rappr2 rappresentante della seconda classe da unire
	 */
	private static void merge(OrdListNode rappr1, OrdListNode rappr2) {

		//DEBUG
		//System.out.println("Il rango di " + rappr1.toString() + " è " + rappr1.getRank()
		//		+ "\nmentre quello di " + rappr2.toString() + " è " + rappr2.getRank());

		// metto in n2 il nodo con rango maggiore
		if (rappr1.getRank() > rappr2.getRank()) {
			OrdListNode temp = rappr1;
			rappr1 = rappr2;
			rappr2 = temp;
		}

		//DEBUG
		//System.out.print("MERGE\n\tnodo1: " + rappr1.toString());
		//System.out.println(",\tnodo2: " + rappr2.toString());

		//Node rappr1 = find(n1);
		//Node rappr2 = find(n2);

		if (!rappr1.equals(rappr2)) {
			// già rappresentante allora uso metodi del nodo
			// OrdListaLink ccpar1 = rappr1.getCCPar();
			// OrdListaLink ccpar2 = rappr2.getCCPar();
			// PROBLEMA: ccparX vengono uniti nella union...
			//			 allora come fare per avere le due liste?
			//			 costruisco array
                    Comparable[] ccpar1Arr = null, ccpar2Arr = null;
                    if (rappr1.getCCPar() != null) {
                        //DEBUG
                        //System.err.println("CCPAR1: " + rappr1.getCCPar());
                        
			ccpar1Arr = rappr1.getCCPar().toArray();
                    }
                    if (rappr2.getCCPar() != null) {
                        //DEBUG
                        //System.err.println("CCPAR2: " + rappr2.getCCPar());
                        
                        ccpar2Arr = rappr2.getCCPar().toArray();
                    }
                    
                    union(rappr1, rappr2);

			// dipende dalla lista (ultimo nodo della lista?)
			//espandiCongruenze(ccpar1, ccpar2);
			espandiCongruenze(ccpar1Arr, ccpar2Arr);
		}
	}

	/*
	public static void espandiCongruenze(OrdListaLink ccpar1, OrdListaLink ccpar2) {
	
	// PRESTAZIONI ORRIDE
	Node n1 = (Node) ccpar1.get(0);
	for (int i = 0; n1 != null; n1 = (Node) ccpar1.get(++i)) {
	Node n2 = (Node) ccpar2.get(0);
	for (int j = 0; n2 != null; n2 = (Node) ccpar2.get(++j)) {
	if (find(n1) == find(n2) && areCongruent(n1, n2))
	merge(n1, n2);
	}
	}
	
	}
	 */
	
	/**
	 * Controlla se dai padri che passiamo come parametri si possono unire altre 
	 * classi sfruttando le congruenze
	 * 
	 * @param ccpar1 padri della prima classe
	 * @param ccpar2 padri della seconda classe
	 */
	private static void espandiCongruenze(Comparable[] ccpar1, Comparable[] ccpar2) {
            if (ccpar1 == null || ccpar2 == null)
                return;

		//DEBUG
		//System.out.println("ESPANDI CONGRUENZE");

		// prestazioni OK
		//if (ccpar1 != null && ccpar2 != null)
		for (int i = 0; i < ccpar1.length; i++)
			for (int j = 0; j < ccpar2.length; ++j) {

				//DEBUG
				//System.out.println("\ti = " + i + ", j = " + j);

				OrdListNode n1 = (OrdListNode) ccpar1[i];
				OrdListNode n2 = (OrdListNode) ccpar2[j];

				//DEBUG
				//System.out.println("\t: " + n1 + " e " + n2);

				OrdListNode r1, r2; // ottimizzo chiamando solo una volta il find
				if ((r1 = find(n1)) != (r2 = find(n2)) && areCongruent(n1, n2)) {
					//System.out.println("TRUE (condizione per fare il merge)");
					merge(r1, r2);
				}
			}

	}

	/**
	 * Metodo che controlla che i rappresentanti delle due classi da unire 
	 * soddisfino tutte le disuguaglianze
	 * 
	 * @param n1 rappresentante della prima classe
	 * @param n2 rappresentante della seconda classe
	 */
	private static void confrontaDisuguaglianze(OrdListNode n1, OrdListNode n2) {

		//DEBUG
		//System.out.println("confrontaDisuguaglianze");

		OrdListaLink div = n1.getDiversi();

                if (div != null) {
                    //DEBUG
                    //System.out.println("\tdiversi di " + n1.toString() + ": " + div.toString());

                    for (int i = 0; i < div.size(); i++) {
                            // if () find(diversi) = n2 INSODDISFACIBILE
                            // e viceversa
                            // System.out.println(find((NodeWithOrdList) div.scan()).toString() + " vs " + find(n2).toString());
                            if (find((OrdListNode) div.scan()) == find(n2))
                                    throw new IllegalStateException("INSODDISFACIBILE!!!!");
                    }
                }

		div = n2.getDiversi();
                
                if (div != null) {
                    //DEBUG
                    //System.out.println("\tdiversi di " + n2.toString() + ": " + div.toString());

                    for (int i = 0; i < div.size(); i++)
                            // if () find(diversi) = n2 INSODDISFACIBILE
                            // e viceversa
                            if (find(n1) == find((OrdListNode) div.scan()))
                                    throw new IllegalStateException("INSODDISFACIBILE!!!!");
                }

		//DEBUG
		//System.out.println("le disuguaglianze sono rispettate.");
	}
}
