package listSAT;

/**
 *
 * @author ale
 */
public class HLCongruenceClosure {
    
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
                HLNode p1;
                HLNode p2;
		while (flag) {
                    if ((p1 = find((HLNode) ug[0])) != 
                            (p2 = find((HLNode) ug[1]))) {
                        // faccio il merge solo se i due nodi non sono già nella
                        // stessa classe
                        // questo serve perché è stato rimosso il controllo dentro
                        // al metodo merge per efficienza
			try {
                            
                            //DEBUG
                            //System.out.println("UGUAGLIANZA: " + p1 + " e " + p2);
                            
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
                                    HLNode n = (HLNode)nodi.getElemNumero(i);
                                    
                                    if (n.isAtom() && n.isCons())
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
                        
			if (find((HLNode) dis[0]) == find((HLNode) dis[1])) {
				System.out.println(((HLNode) dis[0]).toString() + " != "
						+ ((HLNode) dis[1]).toString() + " non è rispettato.\nINSODDISFACIBILE");
				return false;
			}
		} while (true);

                
                

	}
    
	/**
	 * Restituisce il rappresentante del nodo. Questo metodo implementa l'euristica
	 * della compressione dei cammini.
	 *
	 * @return rappresentante del nodo
	 */
	private static HLNode find(HLNode node) {
		if (node.getFind() == node)
			return node;
                HLNode f = find(node.getFind());
		node.setFind(f);
		return f;
	}

	/**
	 * Esegue l'unione di 2 nodi. Nel nodo scelto come rappresentante sia la lista
	 * dei padri, sia la lista delle disugualianze, e' data dall'unione delle
	 * strtture dati di entrambi i nodi. Questo metodo implementa l'euristica
	 * di unione per rango. Se x.rank < y.rank il nodo y e' il nuovo rappresentante
	 * altrimenti lo e' il nodo x. Prima di invocare trueUnion per unire effettivamente
	 * i nodi controlla che la nuova classe di equilavenza sia ammissibile.
	 *
	 * @param x primo nodo da unire
	 * @param y secondo nodo da unire
	 */
	private static boolean union(HLNode x, HLNode y) throws IllegalStateException {
		if ((x.getDiversi() != null && x.getDiversi().check(y.toString())) 
                        || (y.getDiversi() != null && y.getDiversi().check(x.toString())))
                    // controlla che Y non sia presente nelle disuguaglianze della X 
                    // e poi fa il contrario
                    // controlla solo i rappresentanti
                    throw new IllegalStateException("\n INSODDISFACIBILE");
                // adesso controllo anche tutti gli altri
		check(y.getEqClass(), x.getDiversi());
		check(x.getEqClass(), y.getDiversi());

		if (x.getRank() == y.getRank())
			x.increaseRank();
		if (x.getRank() < y.getRank()) {	// standard
			trueUnion(x, y);
			return false;
		}// if (x.rank > y.rank)
		trueUnion(y, x);
		return true;
	}

	/**
	 * Esegue l'unione effettiva di 2 nodi.
	 *
	 * @param x primo nodo da unire
	 * @param y secondo nodo da unire
	 */
	private static void trueUnion(HLNode x, HLNode y) {
		x.setFind(y);
		if (x.isAtom())
			y.setAtom();
                if (x.isCons())
                    y.setCons();
                
                if (y.isAtom() && y.isCons())
                    throw new IllegalStateException("INSODDISFACIBILE!!!!");
                
                // praticamente costante (OrdList invece n^2 nel caso pessimo)
                // OrdList cancella i doppi, qui NO
		y.setCCPar(DoppiaList.union(y.getCCPar(), x.getCCPar()));
                
                // crea sempre una hashtable nuova (n^2) vs sempre (n^2) di OrdList
		y.setDiversi(HashOpenPlusList.disjoinUnion(y.getDiversi(), x.getDiversi()));
                
                // la lista sembra migliore
		DoppiaList result = DoppiaList.union(y.getEqClass(), x.getEqClass());
                
		if (result == null) {
                    // erano da soli, allora creo una nuova tabella inserendogli i rappresentanti
			result = new DoppiaList();
			result.insert(x);
			result.insert(y);
		}
		y.setEqClass(result);
	}

	/**
	 * Controlla che la classe di equivalenza e l'insieme delle disugualianze siano disgiunti.
	 * Siccome entrambi gli insiemi sono implementati tramite hash table, per rendere il metodo
	 * piu' efficente l'insieme piu' piccolo e' mappato su quello piu' grande.
	 *
	 * @param equivalenceClass classe di equivalenza
	 * @param neq insieme delle disugualianze
	 * @throws IllegalStateException se la classe di equivalenza e l'insieme delle disugualianze non sono disgiunti
	 */
	private static void check(DoppiaList equivalenceClass, HashOpenPlusList neq) throws IllegalStateException {
		if (equivalenceClass == null || neq == null)
			return;
		for (int i = 0; i < equivalenceClass.size(); i++)
			if (neq.check(equivalenceClass.iterator().toString()))
				throw new IllegalStateException("\n INSODDISFACIBILE");
	}

	/**
	 * Verifica che 2 nodi siano congruenti.
	 *
	 * @param x primo nodo
	 * @param y secondo nodo
	 * @return true se x e' congruente a y false altrimenti
	 */
	private static boolean congruent(HLNode x, HLNode y) {
		if (x.nodeEquals(y)) {
			for (int i = 0; i < x.arity(); i++)
				if (find(x.getArgs()[i]) != find(y.getArgs()[i]))
					return false;
			return true;
		}
		return false;
	}



	/**
	 * Unisce 2 nodi x e y se congruenti. Il metodo e' chiamato in modo ricorsivo
	 * sui padri dei nodi di input. Se si presenta nelle coppie un nodo duplicato
	 * viene anche rimosso dall'unione.
	 *
	 * @param x primo nodo
	 * @param y secondo nodo
	 */
	public static void merge(HLNode xFind, HLNode yFind) {
            
            //System.out.println("NUOVA MERGE RAGGIUNTA");
 /*           try {
                System.in.read();
            } catch (Exception e) {}
  */
		//HLNode xFind = find(x);
		//HLNode yFind = find(y);
		//if (xFind != yFind) {
                    
                    // costante vs i miei n per la copia della lista nell'array
			DoppiaList ccparX = xFind.getCCPar(); 
			DoppiaList ccparY = yFind.getCCPar();
                        
			boolean xParent = union(xFind, yFind);
                        
			if (ccparX == null || ccparY == null)
				return;
                        
                        // espando le congruenze solo se entrambi contengono i nodi
                        // hanno dei parenti
                        //System.out.println("LE LISTE SONO LUNGHE: " + ccparX.size() + " e " + ccparY.size());
                        //String prima = "LE LISTE SONO LUNGHE: " + ccparX.size() + " e " + ccparY.size();
    /*                    
                        try {
                            System.in.read();
                        } catch (Exception e) {}
     */
                       
                        if (ccparX.size() < 0 && ccparY.size() > 0)
                            espandiCongruenzeConEliminazione(xFind, yFind, ccparX, ccparY, xParent);
                        else {
                            for (int i = 0; i < ccparX.size(); i++) {
				HLNode t1 = (HLNode) ccparX.iterator();
                                                                 
                                    for (int j = 0; j < ccparY.size() && i < ccparX.size(); j++) {
                                            HLNode t2 = (HLNode) ccparY.iterator();
                                    

                                                //DEBUG
                                                //System.out.println("espando congruenza tra: " + t1 + " e " + t2);
                                                // decido che il 3° è il nodo con classe con rango minore
                                                // x se la union mi da false
                                                // y viceversa
                                                // controlla se t1 e t2 sono uguali 
                                                // rimuove dal più piccolo
                                                boolean flag = !xParent ? exOp(t1, t2, xFind, ccparY) : exOp(t1, t2, yFind, ccparX);
                                                if (!flag) {
                                                    if (!xParent)
                                                        j--;
                                                    else
                                                        i--;
                                                } else {
      
     
                                                    // se sono uguali mi dice false
                                                    // allora non controlla l' "espansione delle congruenze"
                                                    HLNode fT1 = find(t1);
                                                    HLNode fT2 = find(t2);
                                                    if (fT1 != fT2 && congruent(t1, t2))
                                                            merge(fT1, fT2);
                                                }
                                            
                                    }
                                }
			
                        }
                        
        /*
        
        if (xParent)
                                espandiCongruenze(yFind,ccparY, ccparX);
                            else
                                espandiCongruenze(xFind, ccparX, ccparY);
        */
                        
                        //String dopo = "LE LISTE SONO diventate LUNGHE: " + ccparX.size() + " e " + ccparY.size();
                        
                        //System.out.println(prima + dopo);
      /*                  try {
                            System.in.read();
                        } catch (Exception e) {}
                        
        */                
                        
			garbageCollector(xFind, yFind, xParent); //garbageCollector(x, y, xParent);
                        
                        
		//}
	}
	
	
	private static void espandiCongruenze(HLNode rapprMaggiore, DoppiaList ccparMaggiore, DoppiaList ccparMinore) {
            for (int i = 0; i < ccparMaggiore.size(); i++) {
				HLNode t1 = (HLNode) ccparMaggiore.iterator();
                                                                 
                                    for (int j = 0; j < ccparMinore.size(); j++) {
                                            HLNode t2 = (HLNode) ccparMinore.iterator();
                                    

                                                //DEBUG
                                                System.out.println("espando congruenza tra: " + t1 + " e " + t2);
                                                /*String test = t1 + " e " + t2;
                                                if(test.equals("Rn1(o,w,w) e Fun(a,i,w)"))
                                                    try {
                                                        System.in.read();
                                                    } catch (Exception e) {}
                                                */
                                                // decido che il 3° è il nodo con classe con rango minore
                                                // x se la union mi da false
                                                // y viceversa
                                                // controlla se t1 e t2 sono uguali 
                                                // rimuove dal più piccolo
                                                boolean flag = exOp(t1, t2, rapprMaggiore, ccparMinore);
                                                if (!flag) {
                                                    j--;
                                                    
                                                } else {
      
     
                                                    // se sono uguali mi dice false
                                                    // allora non controlla l' "espansione delle congruenze"
                                                    HLNode fT1 = find(t1);
                                                    HLNode fT2 = find(t2);
                                                    if (fT1 != fT2 && congruent(t1, t2))
                                                            merge(fT1, fT2);
                                                }
                                            
                                    }
                                }
        }
        
        
    private static void espandiCongruenzeConEliminazione(HLNode xFind, HLNode yFind, DoppiaList ccparX, DoppiaList ccparY, Boolean xParent) {
        HashOpenStandardDoppioH par1 = new HashOpenStandardDoppioH(ccparX.size() * 2);
        HashOpenStandardDoppioH par2 = new HashOpenStandardDoppioH(ccparY.size() * 2);
                        
                        
                        for (int i = 0; i < ccparX.size(); i++) {
				HLNode t1 = (HLNode) ccparX.iterator();
                                if (par1.get(t1.toString()) != null) {
                                    i--;
                                    ccparX.remove();
                                    if (xParent)
                                        xFind.getCCPar().decrease();
                                    else 
                                        yFind.getCCPar().decrease();
                                } else {
                                    
                                    par1.put(t1.toString(), 1);
                                    Boolean primaVoltaY = true;
                                    Boolean blocca = false;
                                    for (int j = 0; j < ccparY.size() && i < ccparX.size(); j++) {
                                            HLNode t2 = (HLNode) ccparY.iterator();
                                            blocca = false;
                                            if (primaVoltaY) {
                                                if (par2.get(t2.toString()) != null) {
                                                    j--;
                                                    ccparY.remove();
                                                    if (xParent)
                                                        xFind.getCCPar().decrease();
                                                    else 
                                                        yFind.getCCPar().decrease();
                                                    blocca = true;
                                                } else {
                                                    par2.put(t2.toString(), 1);
                                                }
                                            }                                    
                                            if (!blocca) {
                                                //DEBUG
                                                //System.out.println("espando congruenza tra: " + t1 + " e " + t2);
                                                String test = t1 + " e " + t2;
         /*                                       if(test.equals("Rn1(o,w,w) e Fun(a,i,w)"))
                                                    try {
                                                        System.in.read();
                                                    } catch (Exception e) {}
          */
                                                // decido che il 3° è il nodo con classe con rango minore
                                                // x se la union mi da false
                                                // y viceversa
                                                // controlla se t1 e t2 sono uguali 
                                                // rimuove dal più piccolo
                                                boolean flag = !xParent ? exOp(t1, t2, xFind, ccparY) : exOp(t1, t2, yFind, ccparX);
                                                if (!flag) {
                                                    if (!xParent)
                                                        j--;
                                                    else
                                                        i--;
                                                } else {
                                                    // se sono uguali mi dice false
                                                    // allora non controlla l' "espansione delle congruenze"
                                                    HLNode fT1 = find(t1);
                                                    HLNode fT2 = find(t2);
                                                    if (fT1 != fT2 && congruent(t1, t2))
                                                            merge(fT1, fT2);
                                                }
                                            }
                                    }
                                }
			}
    }

        /**
	 * Rimuove il nodo duplicato
	 *
	 * @param x primo nodo
	 * @param y secondo nodo
	 * @param ccparX lista dei padri del primo nodo
	 * @param ccparY lista dei padri del secondo nodo
	 * @param xParent se x e' padre di y
	 */
       	private static boolean exOp(HLNode t1, HLNode t2, HLNode x, DoppiaList ccparY) {
		if (t1 == t2) {
			ccparY.remove();
			x.getCCPar().decrease();
                        return false;
                }
                return true;
	}

        /**
	 * Annulla i campi del nodo figlio.
	 *
	 * @param x primo nodo
	 * @param y secondo nodo
	 * @param xParent se x e' padre di y
	 */
	private static void garbageCollector(HLNode x, HLNode y, boolean xParent) {
		if (xParent) {
			y.resetCCPar();
			y.resetDiversi();
			y.resetEqClass();
		} else {
			x.resetCCPar();
			x.resetDiversi();
			x.resetEqClass();
		}
	}
    
    
}
