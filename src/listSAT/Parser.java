package listSAT;

/**
 * Classe che si occupa sia dell'analisi sintattica, sia di quella semantica
 * di una stringa contenente delle formule nella teoria delle liste.
 * Allo stesso tempo costruisce un grafo diretto aciclico utlile per eseguire
 * l'algoritmo di chiusura di congruenza per decidere la soddifacibilità.
 * 
 * 
 * @author Alessandro Gottoli vr352595
 */
public class Parser {
    
    public static Boolean usoOrdList;

	/**
	 * Pila che contiene i parametri di una funzione man mano che vengono
	 * riconosciuti come elementi validi
	 */
	public static Pila param;
	
	/**
	 * Tabella hash che contiene tutti i nodi che rappresentano gli elementi
	 * riconosciuti come validi
	 */
	public static HashOpenStandardDoppioH hashtable;
	
	/**
	 * tabella hash che contiene come key il nome della funzione e 
	 * come elemento l'arietà della funzione
	 */
	//public static HashOpen controlloArity;
	
	/**
	 * Coda che contiene tutta la sequenza di uguaglianze trovate nell'analisi 
	 * della stringa
	 */
	public static Coda uguaglianze;
	
	/**
	 * Coda che contiene tutta la sequenza di disuguaglianze trovate nell'analisi
	 * della stringa
	 */
	public static Coda disuguaglianze;
	
	/**
	 * Indice del "cursore" durante l'analisi della stringa
	 */
	public static int i;
	
	/**
	 * Stringa da analizzare contenente le formule e le uguaglianze
	 */
	public static String F;
	
	public static int avanzamento;

	/**
	 * Scansiona la stringa passata ed effettua su di essa:
	 *    - analisi sintattica
	 *    - analisi semantica
	 * Inoltre costruisce il grafo utile per eseguire il controllo di
	 * soddisfacibilità
	 * 
	 * @param formula la stringa da analizzare contenente le uguaglianze e le
	 *					disuguaglianze delle formule
	 * @return coda delle uguaglianze che deve rispettare la soddisfacibilità
	 */
	public static Object[] analisiAndCostrGrafo(String formula, Boolean usoOL) {

		param = new Pila();
                usoOrdList = usoOL;
                //if (usoOrdList = usoOL)
                    hashtable = new HashOpenStandardDoppioH(32);
                //else
                //    hashtable = new HashOpenPlusList(32);
		//controlloArity = new HashOpen(32);
		//controlloArity.put("car", 1);
		//controlloArity.put("cdr", 1);
		//controlloArity.put("cons", 2);
		uguaglianze = new Coda();
		disuguaglianze = new Coda();
		F = formula;

		avanzamento = 0;




		if (F == null)
			throw new NullPointerException("Analisi Sintattica: la stringa non può essere nulla.");
		//return null;

		i = 0;

		try {
			// per essere valida deve esserci almeno una clausola
			analisiClausola(); // previene stringa vuota o tutti spazi

			// o siamo in fondo alla stringa, 
			// o c'è un qualche simbolo che non è uno spazio
			while (i < F.length() && (F.charAt(i) == ',' || F.charAt(i) == ';')) {
				// c'è un separatore, allora deve esserci un altra clausola
				i++;
				analisiClausola();
			}

			if (i < F.length())
				throw new IllegalArgumentException("Errore Sintattico (clausola scritta in modo errato): i = " + i);

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Analisi Sintattica: fine della stringa in modo inatteso.");
			return null;
		} catch (IllegalArgumentException e) {
			System.out.println(e.toString());
			return null;
		}
                
               ///////////////////////////////////////////////////////////////////////////ERRORE 
                if (!usoOrdList) {
                    // se uso HLNODE devo inserire effettivamente i diversi nella HashTable
                    //for (int i = 0; i < htl.getEntryListSize(); i++) {
                    //   ((HLNode) htl.get()).getDiversi().store();
                    //}
                    for (int i = 0, trovati = 0; i < hashtable.tableSize() && trovati < hashtable.size(); i++) {
                        HLNode n;
                        try {
                            n = (HLNode) hashtable.getElemNumero(i);
                            
                        }catch (NullPointerException e) {
                            n = null;
                        }
                        if (n != null) {
                                trovati++;
                                HashOpenPlusList gD = n.getDiversi();
                                if (gD != null)
                                    gD.store();
                            }
                    }
                    /*try {
                        System.in.read();
                    } catch (Exception e) {}*/
                }
                
                
		// quando usciamo è per solo 2 casi:
		//if (i < 0)
		// analisiClausola ha restituito -1 --> errore
		//	return null;
		// oppure ho finito di analizzare tutta la stringa
		Object[] rv = {uguaglianze, disuguaglianze, hashtable};
		return rv;
	}

	/**
	 * Ignora gli spazi continui incrementando l'indice del cursore fino 
	 * ad un carattere diverso dallo spazio oppure fino ad arrivare fuori dalla
	 * stringa
	 */
	private static void saltaSpazi() {
		//Boolean flag;
		int length = F.length();
		/*
		do {
		flag = false;
		if (i < length && F.charAt(i) == ' ') {
		i++;
		flag = true;
		}
		} while (flag);
		 */
		while (i < length && F.charAt(i) == ' ')
			i++;
		//return i;
	}

	/**
	 * Analizza una singola clausola (uguaglianza/disuglaglianza) presente
	 * nella stringa da analizzare 
	 * e aggiorna l'indice del cursore fino alla prossima clausola oppure
	 * fino in fondo alla stringa
	 * 
	 * @throws IllegalArgumentException in caso di errore di parsing della stringa
	 */
	private static void analisiClausola()
			throws IllegalArgumentException {

		Boolean atom = analisiElemento();

		if (atom)
			return;


		Boolean uguaglianza = false;
		if (F.charAt(i) == '=') {
			i++;
			uguaglianza = true;
		} else if (!(F.charAt(i++) == '!' && F.charAt(i++) == '='))
			// carattere non riconosciuto
			throw new IllegalArgumentException("Errore Sintattico (clausola scritta in modo errato): i = " + --i); //return -1;

		int inizioE = i;
		atom = analisiElemento();
		if (atom)
			// la i qua non è giusta!!! edit: sistemato
			throw new IllegalArgumentException("Errore Sintattico (la funzione atom deve essere da sola in una clausola): i = " + inizioE);

		// aggiungo la clausola nella lista giusta ( = oppure != )
		NodeInterface[] n;
                if (usoOrdList)
                    n = new OrdListNode[2];
                else
                    n = new HLNode[2];
                
		for (int l = 1; l >= 0; l--)
			n[l] = (NodeInterface) param.pop();
		if (uguaglianza)
			uguaglianze.insert(n);
		else {
			disuguaglianze.insert(n);
                        n[0].addDiversi(n[1]);
                        n[1].addDiversi(n[0]);
                        
			
			//DEBUG
			//System.out.println("i diversi sono:");
			//System.out.println("\t"+ n[0].toString() + ": " + n[0].getDiversi().toString());
			//System.out.println("\t"+ n[1].toString() + ": " + n[1].getDiversi().toString());
			//System.out.println("PTkkkkkkpppppppppppppppppppppppppppppppppppppppppp");
			

		}

		//return i;

	}

	/**
	 * Analizza il nome di una funzione o di un elemento
	 * che deve contenere sono lettere (fatta eccezione per il caso !atom)
	 * e aggiorna l'indice del cursore
	 * 
	 * @return true se la funzione risulta essere !atom, false altrimenti
	 * @throws StringIndexOutOfBoundsException se c'è un errore nel parsing del nome
	 */
	private static Boolean analisiFN() {
		// se il primo carattere diverso da spazio non è un carattere alfanumerico
		// da errore sintattico
		if (i >= F.length())
			throw new StringIndexOutOfBoundsException("Errore Sintattico (la stringa è finita, manca un elemento)");

		Boolean not = false;
		if (!Character.isLetterOrDigit(F.charAt(i++))) ////////////////////////////////// < --> >= e && --> ||
			if (F.charAt(i - 1) == '!') {
				// può essere un !atom oppure !  atom
				saltaSpazi();
				not = true;
			} else
				throw new IllegalArgumentException("Errore Sintattico (nome elemento errato): i = " + i); //return -1;

		//Boolean flag;
		// continua la scansione dell'fn dell'elemento (se ha più lettere)
		/*
		do {
		flag = false;
		if (i < F.length() && Character.isLetterOrDigit(F.charAt(i))) {
		i++;
		flag = true;
		}
		} while (flag);
		 */
		int inizio = i;
		while (i < F.length() && Character.isLetterOrDigit(F.charAt(i)))
			i++;
		int fine = i;
		// restituisce il primo carattere non alfanumerico dopo l'fn 
		// (può essere anche spazio)
		//return i;

		if (not && F.substring(inizio, fine).equals("atom"))
			return true;

		return false;
	}

	/**
	 * Analizza il singolo elemento presente nella stringa compresa arietà
	 * se l'elemento risulta valido allora costruisce il nodo corrispondente
	 * aggiornando eventualmente anche i campi di altri elementi
	 * Aggiorna anche l'indice del cursore al prossimo elemento
	 * 
	 * @return true se il nome della funzione è atom (sia positivo che negativo "!")
	 *			false altrimenti
	 * @throws IllegalArgumentException se c'è un errore di parsing nella stringa
	 */
	private static Boolean analisiElemento() throws IllegalArgumentException,
                                                        IllegalStateException {

		saltaSpazi();

		int inizioFN = i;
		Boolean notAtom = analisiFN();
		int fineFN = i;

		//int k = 0;
		//while (k < elementi.length && elementi[k] != null && !elementi[k].equals(F.substring(i, j)))
		//	k++;
		//elementi[k] = F.substring(i, j);

		saltaSpazi();

		int inizioParametri = i;
		int nArgomenti = 0;
		// finito l'fn ci può essere:
		// '(' caso interessante di una funzione
		// ',' o ';' 
		// ')'
		// '=' o '!=' se è il primo elemento della coppia
		if (i < F.length() && F.charAt(i) == '(') {
			// siamo nel caso di una funzione (arity > 0)
			// quindi passo alla valutazione degli argomenti e ce ne deve essere
			// almeno uno
			//Boolean flag;

			do {
				//flag = false;
				i++;
				analisiElemento();
				//System.out.println("la i vale: " + i);
				
				avanzamento = i;
				
				nArgomenti++;
				// metti nella lista degli argomenti 
				saltaSpazi();
				/*
				if (F.charAt(i) == ',' || F.charAt(i) == ';')
				// deve esserci un altro elemento
				flag = true;
				//else if (F.charAt(i) == ')')
				//	i++;
				 */
			} while (F.charAt(i) == ',' || F.charAt(i) == ';');

			// gestione chiusura parentesi per ogni funzione
			// la stringa deve finire con ')' se ha almeno un elemento
			if (F.charAt(i++) != ')')
				throw new IllegalArgumentException("Errore Sintattico (manca parentesi di chiusura funzione): i = " + i);
			//return -1;

		}

		int fineParametri = i;

		Boolean fnAtom = false;

		String funzione = F.substring(inizioFN, fineFN);

		if (notAtom) {
                    
                    if (nArgomenti != 1)
				throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i);

			// modifica stringa per sostituire 
			// !atom(A) con A = cons(A1, A2)
			// se A = fun(x,y) --> scrivo fun(x,y) = cons(fun1,fun2)

			fnAtom = true;

			// il parametro che c'è in stack è A
			// io devo creare un nuovo oggetto chiamato cons(fn1, fn2)
			NodeInterface a = (NodeInterface) param.pop();
                        
                        //////////////////////////////
                        // OTTIMIZZAZIONE
                        //if (a.isAtom())
                        //    throw new IllegalStateException("INSODDISFACIBILE perché !atom isAtom");
                        a.setCons();
                        /////////////////////////////
//*                        
                        // COSTRUZIONE DEI 5 NODI NECESSARI PER IL NOT ATOM 
                        // (ma sono davvero necessari? SI' per forza)
			String fnA = a.toString(); // va bene solo FN oppure ci vuole tutta la KEY???
			// caso !atom(fn(a,b)) e !atom(fn(c,d)) ???????????????????????????????????????
			NodeInterface node = (NodeInterface) hashtable.get("cons([" + fnA + "1],[" + fnA + "2])");
			if (node != null)
				// la clausola !atom c'era anche in precedenza
				return fnAtom;

			NodeInterface n1, n2;
                        
                        if (usoOrdList) {
                            n1 = new OrdListNode("[" + fnA + "1]", null);
                            n2 = new OrdListNode("[" + fnA + "2]", null);
                            NodeInterface[] argA = {n1, n2};
                            node = new OrdListNode("cons", argA);
                        } else {
                            n1 = new HLNode("[" + fnA + "1]", null);
                            n2 = new HLNode("[" + fnA + "2]", null);
                            NodeInterface[] argA = {n1, n2};
                            node = new HLNode("cons", argA);
                        }
                        
                        // NO OTTIMIZZAZIONE PERCHE' IL NODO è appena stato creato
                        // OTTIMIZZAZIONE controlla nella costruzione se il non atom è settato ad atom
                        // if (node.isAtom()) // UNSAT
                        //    throw new IllegalStateException("INSODDISFACIBILE per cons && atom");
			
                        node.setCons();
                        n1.setAtom();

			NodeInterface[] argom = {a, node};

			//System.out.println("!atom --> " + a.toString() + " = " + node.toString());
			uguaglianze.insert(argom);
			
			
			// COSTRUISCO I NODI CAR E CDR del nuovo nodo CONS
			
			//String par = F.substring(inizioParametri, fineParametri).replaceAll(";", ",").replaceAll(" ", "");
			String key = node.toString();
			NodeInterface car = (NodeInterface) hashtable.get("car(" + key + ")");
			NodeInterface cdr = (NodeInterface) hashtable.get("cdr(" + key + ")");
					
			//DEBUG
			//System.out.println("\n\n\ncar + par = car(" + key + ")\n\n\n");
					
					
			if (car == null) {
				//DEBUG
				//System.out.println("CAR nuovo");
                                
                                NodeInterface[] consArg = {node};
                                    
                                if (usoOrdList)
                                    car = new OrdListNode("car", consArg);
                                else
                                    car = new HLNode("car", consArg);
				
				// ???
				car.setAtom(); // ???
                                // consArg.setCons(); // ??? guardare regole NO!!! GIA' FATTO
				
				hashtable.put("car(" + key + ")", car);
				// DEBUG 
				//System.out.println(car.toString());
			}
			car.setFind(n1);
					
			//DEBUG
			//System.out.println(car.toString() + " CAR find = " + CongruenceClosure.find(car));
					
			if (cdr == null) {
				//DEBUG
				//System.out.println("CDR nuovo");
                                
				NodeInterface[] consArg = {node};
                                
                                if (usoOrdList)
                                    cdr = new OrdListNode("cdr", consArg);
                                else
                                    cdr = new HLNode("cdr", consArg);
				//cdr = new OrdListNode("cdr", consArg);
				hashtable.put("cdr(" + key + ")", cdr);
                                
                                // consArg.setCons(); // ??? guardare regole NO!!! GIA' FATTO
                                
                                ///////////////////////////////////////////////////////////////////
                                // CASO Th. Liste ACICLICA
                                //disuguaglianze.insert(node, cdr); // così cdr(a) = a è INSODD ////////////////////////////////////////////////////////////NOTA PER ALE
                                //node.addDiversi(cdr);
                                //cdr.addDiversi(node);
                                ///////////////////////////////////////////////////////////////////
                        }
			cdr.setFind(n2);
//*/

		} else if (funzione.equals("atom")) {
			fnAtom = true;
			
			if (nArgomenti != 1)
				throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i);

			NodeInterface node = (NodeInterface) param.pop();
                        
                        // OTTIMIZZAZIONE QUA VA BENE
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			if (node.isCons())
				throw new IllegalStateException("INSODDISFACIBILE in costr grafo per car = isCons");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			node.setAtom();


		} else {
			// costruzione del nodo che rappresenta l'elemento
			String key = F.substring(inizioFN, fineParametri).replaceAll(";", ",").replaceAll(" ", "");
			
			//DEBUG
			//System.out.println("la key è: " + key);
			
			NodeInterface[] ar = null;
			if (nArgomenti > 0) {
				ar = new NodeInterface[nArgomenti];
                                // ??? OCCIO SERVE PER FORZA DIVERSIFICARLI????
                                if (usoOrdList)
                                    for (int l = nArgomenti - 1; l >= 0; l--)
					ar[l] = (OrdListNode) param.pop();
                                else
                                    for (int l = nArgomenti - 1; l >= 0; l--)
					ar[l] = (HLNode) param.pop();
			}
			
			// Controllo che il nodo con quella key sia già stato creato
			NodeInterface node = (NodeInterface) hashtable.get(key);
			
                        if (node == null) {
				//DEBUG
				//System.out.println("il nodo " + key + " è nuovo.");
				//String funzione = F.substring(inizioFN, fineFN);

				// controllo arietà
				//Object arityElemento = controlloArity.get(funzione);
				//if (arityElemento == null)
				//	controlloArity.put(funzione, nArgomenti);
				//else if ((Integer) arityElemento != nArgomenti)
				//	throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i); //return -1;
				//controlloArity(funzione, nArgomenti);

                                if (usoOrdList)
                                    node = new OrdListNode(funzione, ar);
                                else
                                    node = new HLNode(funzione, ar); // ??? OCCIO QUA NON SO SE DA PROBLEMI
                                
				hashtable.put(key, node);
				
				// controllo caso funzione = cons
				if (funzione.equals("cons")) {
                                    
                                    if (nArgomenti != 2)
                                        throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i); //return -1;
                                    
					//String par = F.substring(inizioParametri, fineParametri).replaceAll(";", ",").replaceAll(" ", "");
					NodeInterface car = (NodeInterface) hashtable.get("car(" + key + ")");
					NodeInterface cdr = (NodeInterface) hashtable.get("cdr(" + key + ")");
					node.setCons();
                                        
                                        // OTTIMIZZAZIONE 
                                        if (ar[0].isCons())
                                            throw new IllegalStateException("INSODDISFACIBILE perche arg[0] di cons isCons.");
                                        
                                        ar[0].setAtom();
					
					//DEBUG
					//System.out.println("\n\n\ncar + par = car(" + key + ")\n\n\n");
					
					
					if (car == null) {
						//DEBUG
						//System.out.println("CAR nuovo");
                                                NodeInterface[] consArg = {node};
                                                
                                                if (usoOrdList) 
                                                    car = new OrdListNode("car", consArg);
						else
                                                    car = new HLNode("car", consArg);
                                                
                                                
						hashtable.put("car(" + key + ")", car);
						// DEBUG 
						//System.out.println(car.toString());
					}
                                        // ???
                                        
                                        // OTTIMIZZAZIONE 
                                        if (car.isCons())
                                            throw new IllegalStateException("INSODDISFACIBILE in costr grafo per car = isCons");
                                        
                                        car.setAtom(); // ???
					car.setFind(ar[0]);
					
					//DEBUG
					//System.out.println(car.toString() + " CAR find = " + CongruenceClosure.find(car));
					
					if (cdr == null) {
						//DEBUG
						//System.out.println("CDR nuovo");
						NodeInterface[] consArg = {node};
                                                
                                                if (usoOrdList)
                                                    cdr = new OrdListNode("cdr", consArg);
                                                else
                                                    cdr = new HLNode("cdr", consArg);
                                               
						hashtable.put("cdr(" + key + ")", cdr);
					}
					cdr.setFind(ar[1]);
					
				} else 
					// caso della funzione "car" deve essere atom
					if (funzione.equals("car")) {
                                            
                                            if (nArgomenti != 1)
                                                throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i); //return -1;
                                            
						// ???
                                            
                                            // OTTIMIZZAZIONE
                                            if (node.isCons())
                                                throw new IllegalStateException("INSODDISFACIBILE in costr grafo per car = isCons");
                     
						node.setAtom(); // ???
						/////////////////////////////////////////////////////////////////////////////////////
						// se la TH. delle Liste fosse ACICLICA allora la linea dopo è giusta
                                                //ar[0].setCons();
                                                /////////////////////////////////////////////////////////////////////////////////////
                                                
                                                // questa è obbligatoria
                                                if (ar[0].isCons()) {
                                                    disuguaglianze.insert(node, ar[0]); // car(a) = a INSODD se a isCons
                                                    node.addDiversi(ar[0]);
                                                    ar[0].addDiversi(node);
                                                } else if (ar[0].isAtom())
                                                    uguaglianze.insert(node, ar[0]);
                                        } else 
					/////////////////////////////////////////////////////////////////////////////////////////
					if (funzione.equals("cdr")) {
                                            
                                            if (nArgomenti != 1)
                                                throw new IllegalArgumentException("Errore Sintattico (arietà sbagliata): fn = " + funzione + " in posizione i = " + i); //return -1;
                                            
                                            
                                            //questa è obbligatoria
                                            if (ar[0].isCons()) {
                                                disuguaglianze.insert(node, ar[0]); // car(a) = a INSODD se a isCons
                                                node.addDiversi(ar[0]);
                                                ar[0].addDiversi(node);
                                            } else if (ar[0].isAtom()) {
                                                uguaglianze.insert(node, ar[0]);
                                                node.setAtom();
                                            }
                                            
                                            ///////////////////////////////////////////
                                            // se la TH. delle Liste fosse ACICLICA allora le linee dopo sono giuste
                                            // ar[0].setCons();
                                            // disuguaglianze.insert(node, ar[0]); // così cdr(a) = a è INSODD ////////////////////////////////////////////////////////////NOTA PER ALE
                                            // node.addDiversi(ar[0]);
                                            //    ar[0].addDiversi(node);
                                            ////////////////////////////////////////////////7
                                        }
			}

			param.push(node);

			//DEBUG
			//System.out.println(node.toString());

		}


		saltaSpazi();

		// in tutti gli altri casi passa l'analisi al metodo chiamante,
		// perché l'elemento è finito
		//return i;
		return fnAtom;
	}
}
