/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFileChooser.java
 *
 * Created on 9-giu-2011, 20.06.55
 */
package listSAT;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Alessandro Gottoli vr352595
 */
public class JFileChooserDemo extends javax.swing.JFrame {

	private final String GRAFO_TEXT_INIT = "Costruzione Grafo...";
	private final String CC_TEXT_INIT = "Algoritmo di Chiusura di Congruenza...";
	private final String SAT_TEXT_INIT = " La formula risulta";
	private final String START_BUTTON_INIT = "Start";
	private final int AUMENTO_DIM = 62;
	private Thread myT, avan;
	private Boolean stop;
        private String formula;

	/** Creates new form JFileChooser */
	public JFileChooserDemo() {
		initComponents();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Formula", "txt", "cnf", "formula");
		fc.setFileFilter(filter);
		setLocationRelativeTo(null); // mette al centro 
                //PropertyChangeSupport pcs = new PropertyChangeSupport(stop);
/*		fc = new JFileChooserDemo();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Formule", "txt", "cnf", "formula");
		fc.setFileFilter(filter);
		 */
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fc = new JFileChooser(System.getProperty("user.dir"));
        inserText = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        log = new javax.swing.JTextArea();
        startButton = new javax.swing.JButton();
        grafoText = new javax.swing.JLabel();
        ccText = new javax.swing.JLabel();
        satText = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openItem = new javax.swing.JMenuItem();
        saveItem = new javax.swing.JMenuItem();
        modeMenu = new javax.swing.JMenu();
        withOrdList = new javax.swing.JRadioButtonMenuItem();
        withHL = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SAT per Teoria delle Liste");
        setResizable(false);

        inserText.setFont(new java.awt.Font("Tahoma", 1, 12));
        inserText.setText("Inserire la formula:");

        log.setColumns(20);
        log.setLineWrap(true);
        log.setRows(5);
        log.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                logComponentAdded(evt);
            }
        });
        log.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                logPropertyChange(evt);
            }
        });
        log.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                logKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                logKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(log);

        startButton.setEnabled(false);
        startButton.setText(START_BUTTON_INIT);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        grafoText.setVisible(false);
        grafoText.setFont(new java.awt.Font("Arial", 0, 12));
        grafoText.setText(GRAFO_TEXT_INIT);

        ccText.setVisible(false);
        ccText.setFont(new java.awt.Font("Arial", 0, 12));
        ccText.setText(CC_TEXT_INIT);

        satText.setVisible(false);
        satText.setFont(new java.awt.Font("Arial", 1, 12));
        satText.setText(SAT_TEXT_INIT);

        jProgressBar1.setVisible(false);
        jProgressBar1.setFont(new java.awt.Font("Tahoma", 0, 9));
        jProgressBar1.setEnabled(false);
        jProgressBar1.setFocusable(false);

        fileMenu.setText("File");
        fileMenu.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                fileMenuAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        openItem.setText("Open");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openItemActionPerformed(evt);
            }
        });
        fileMenu.add(openItem);

        saveItem.setEnabled(false);
        saveItem.setText("Save");
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveItem);

        jMenuBar1.add(fileMenu);

        modeMenu.setText("Mode");
        modeMenu.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                modeMenuAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        withOrdList.setSelected(true);
        withOrdList.setText("versione1 (OrdListLink)");
        withOrdList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withOrdListActionPerformed(evt);
            }
        });
        modeMenu.add(withOrdList);

        withHL.setText("versione2 (DoppiaList)");
        withHL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withHLActionPerformed(evt);
            }
        });
        modeMenu.add(withHL);

        jMenuBar1.add(modeMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inserText, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(219, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grafoText)
                            .addComponent(ccText)
                            .addComponent(satText))
                        .addContainerGap(347, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inserText, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startButton)
                .addGap(13, 13, 13)
                .addComponent(grafoText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ccText, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(satText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void fileMenuAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_fileMenuAncestorAdded
		// TODO add your handling code here:
	}//GEN-LAST:event_fileMenuAncestorAdded

	private void logComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_logComponentAdded
		// TODO add your handling code here:
	}//GEN-LAST:event_logComponentAdded

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
		if (startButton.getText().equals(START_BUTTON_INIT)) {
			if (grafoText.isVisible())
				resetInterface();
			else
				// Ingrandisco la Finestra
				setSize(getWidth(), getHeight() + AUMENTO_DIM);

			log.setEditable(false);
			fileMenu.setEnabled(false);
                        modeMenu.setEnabled(false);
			grafoText.setVisible(true);
			startButton.setText("Abort");
            jProgressBar1.setVisible(true);
            //            jProgressBar1.setIndeterminate(true);
			//jProgressBar1.setMaximum(log.getText().length());
			//jProgressBar1.setMinimum(0);
			

			myT = new Thread(new MyThread());
			myT.start();
		} else {
			myT.stop();
			myT = null;
                                  
                        avan.interrupt();
                        stop = true;

			fileMenu.setEnabled(true);
                        modeMenu.setEnabled(true);
			log.setEditable(true);
			startButton.setText(START_BUTTON_INIT);
			String canc = " CANCELLATO DALL'UTENTE";
                        
                        jProgressBar1.setIndeterminate(false);
                        //jProgressBar1.setEnabled(false);
           //             jProgressBar1.setVisible(false);
			if (ccText.isVisible()) {
				ccText.setText(ccText.getText() + canc);
				jProgressBar1.setVisible(false);
			} else
				grafoText.setText(grafoText.getText() + canc);
		}

	}//GEN-LAST:event_startButtonActionPerformed

	private void openItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openItemActionPerformed
		int returnVal = fc.showOpenDialog(JFileChooserDemo.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			BufferedReader in = null;
			try {
				File file = fc.getSelectedFile();
				in = new BufferedReader(new FileReader(file));
                                formula = in.readLine();
                                if (formula.length() < 50000)
                                    log.setText(formula);
                                else
                                    log.setText("Formula caricata da file: " + file);
                                
			} catch (IOException ex) {
			} finally {
				try {
					in.close();
				} catch (IOException ex) {
				}
			}
			if (log.getText().length() > 0) {
				startButton.setEnabled(true);
				saveItem.setEnabled(true);
			}
			if (grafoText.isVisible()) {
				resetInterface();
				setSize(getWidth(), getHeight() - AUMENTO_DIM);
			}

		}


	}//GEN-LAST:event_openItemActionPerformed

	private void saveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveItemActionPerformed
		int returnVal = fc.showSaveDialog(JFileChooserDemo.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			BufferedWriter out = null;
			//try {
			File file = fc.getSelectedFile();
			Boolean savefile = true;
			if (file.exists()) {
				//System.out.println("Il file " + file + " esiste già.");

				// if file exists create a JOptionPane confirm dialog
				// if user chooses YES then set savefile flag to true,
				// otherwise leave it as false

				Object[] options = {"YES", "NO"};

				JOptionPane pane = new JOptionPane(file.toString()
						+ " already exists.\n"
						+ "Do you want to replace it?",
						JOptionPane.WARNING_MESSAGE,
						JOptionPane.DEFAULT_OPTION,
						null,
						options,
						options[1]);

				JDialog dialog = pane.createDialog(null, "Warning");
                                
				dialog.setVisible(true);
                                try {
                                    Object selectedValue = pane.getValue();
                                    if (!selectedValue.toString().equals("YES"))
					savefile = false;
                                } catch (NullPointerException e) { // se chiudo la finestra dell'alert
                                    savefile = false;
                                }				
			}

			if (savefile)
				try {
					out = new BufferedWriter(new FileWriter(file));
					out.write(log.getText());
				} catch (IOException e) {
				} finally {
					try {
						out.close();
					} catch (IOException ex) {
					}
				}
		}

	}//GEN-LAST:event_saveItemActionPerformed

	private void logKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_logKeyPressed
		if (evt != null && log.isEditable())
			if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
				evt.setKeyCode(java.awt.event.KeyEvent.CHAR_UNDEFINED);

				// NOTA: si può far premere il pulsante SAT
				if (startButton.isEnabled())
					startButton.doClick();
			} else {
                            if (log.getText().startsWith("Formula caricata da file:"))
                                log.setText("");
                            formula = log.getText(); 
                            if (grafoText.isVisible()) {
					resetInterface();
					setSize(getWidth(), getHeight() - AUMENTO_DIM);
				}
                        }
	}//GEN-LAST:event_logKeyPressed

	private void logKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_logKeyReleased
		if (evt != null && log.isEditable()) {
			if (log.getText().length() == 0) {
				startButton.setEnabled(false);
				saveItem.setEnabled(false);
			} else {
				startButton.setEnabled(true);
				saveItem.setEnabled(true);
			}
                        formula = log.getText();
                }
	}//GEN-LAST:event_logKeyReleased

	private void logPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_logPropertyChange
		if (log.getText().length() == 0) {
			startButton.setEnabled(false);
			saveItem.setEnabled(false);
		} else {
			startButton.setEnabled(true);
			saveItem.setEnabled(true);
		}
	}//GEN-LAST:event_logPropertyChange

        private void modeMenuAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_modeMenuAncestorAdded
            // TODO add your handling code here:
        }//GEN-LAST:event_modeMenuAncestorAdded

        private void withOrdListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withOrdListActionPerformed
            if (withOrdList.isSelected())
                withHL.setSelected(false);
            else
                withOrdList.setSelected(true);
        }//GEN-LAST:event_withOrdListActionPerformed

        private void withHLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withHLActionPerformed
            if (withHL.isSelected())
                withOrdList.setSelected(false);
            else
                withHL.setSelected(true);
        }//GEN-LAST:event_withHLActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
			public void run() {
				new JFileChooserDemo().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ccText;
    private javax.swing.JFileChooser fc;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel grafoText;
    private javax.swing.JLabel inserText;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea log;
    private javax.swing.JMenu modeMenu;
    private javax.swing.JMenuItem openItem;
    private javax.swing.JLabel satText;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JButton startButton;
    private javax.swing.JRadioButtonMenuItem withHL;
    private javax.swing.JRadioButtonMenuItem withOrdList;
    // End of variables declaration//GEN-END:variables

	public void resetInterface() {
		grafoText.setVisible(false);
		ccText.setVisible(false);
		satText.setVisible(false);
                jProgressBar1.setVisible(false);

		grafoText.setText(GRAFO_TEXT_INIT);
		ccText.setText(CC_TEXT_INIT);
		satText.setText(SAT_TEXT_INIT);

		startButton.setText(START_BUTTON_INIT);
		//startButton.setEnabled(false);

		saveItem.setEnabled(false);
                jProgressBar1.setEnabled(false);
				jProgressBar1.setIndeterminate(false);
				jProgressBar1.setValue(0);
	}


	/**
	 * L'oggetto Runnable MyThread gestisce la reale computazione
	 */
	private class MyThread implements Runnable, PropertyChangeListener  {
            
            //public Boolean killati = false;
            

		/**
		 * Computazione avviata
		 */
		@Override
		public void run() {
                    
                    
                    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
                    pcs.addPropertyChangeListener(this);
                    
			stop = false;
			avan = new Thread(new Avanzamento());
			avan.start();
                        
                        long inizio = System.currentTimeMillis();
                        
                        try {
                            
                            Object[] clausole = Parser.analisiAndCostrGrafo(formula, withOrdList.isSelected());

                            long fine = System.currentTimeMillis();
                            stop = true;
                            
                            /*HashOpenInterface ho = (HashOpenInterface) clausole[2];
                            System.out.println("numero di nodi creati: " + ho.size());
                            try {
                                System.in.read();
                            } catch (Exception ignore){}
                             */

                //            jProgressBar1.setIndeterminate(false);

                            if (clausole == null) {
                                    System.out.println("Costruzione del grafo interrotta.");
                                    grafoText.setText(grafoText.getText() + " INTERROTTA (errore nella stringa)");
                            } else {

                                    jProgressBar1.setVisible(false);
                                    jProgressBar1.setStringPainted(false);
                                    System.out.println("Costruzione del grafo completata.");
                                    long time = (fine - inizio);// / 1000;
                                    
                                        
                                    grafoText.setText(grafoText.getText() + " COMPLETATA (" + formatoTime(time) + ")");

                                    //System.out.println("uguaglianze: " + clausole[0].toString());

                                    //Coda[] ugDis = {(Coda) clausole[0], (Coda) clausole[1]};

                                    ccText.setVisible(true);
                                    jProgressBar1.setVisible(true);
                                    jProgressBar1.setIndeterminate(true);
                                    inizio = System.currentTimeMillis();

                                    Boolean soddisfacibile;
                                    if (withOrdList.isSelected())
                                        soddisfacibile = OrdListCongruenceClosure.soddisfacibilita(clausole);
                                    else
                                        soddisfacibile = HLCongruenceClosure.soddisfacibilita(clausole);

                                    fine = System.currentTimeMillis();

                                    System.out.println("Algoritmo di Chiusura di Congruenza completato.");
                                    time = (fine - inizio);// / 1000;
                                    ccText.setText(ccText.getText() + " COMPLETATA (" + formatoTime(time) + ")");

                                    satText.setVisible(true);

                                    if (soddisfacibile) {
                                            System.out.println("E' SODDISFACIBILE.");
                                            satText.setText(satText.getText() + " SODDISFACIBILE");
                                    } else
                                            satText.setText(satText.getText() + " NON SODDISFACIBILE");

                                    jProgressBar1.setVisible(false);
                                    jProgressBar1.setIndeterminate(false);
                            }
                        } catch (IllegalStateException e) {
                            long fine = System.currentTimeMillis();
                            stop = true;
                            long time = (fine - inizio);// / 1000;
                            System.out.println("Costruzione del grafo fermata perché trovata insoddisfacibilità atom = !atom.");
                            grafoText.setText(grafoText.getText() + " FERMATA (" + formatoTime(time) + ")");
                            ccText.setText(" perché è stata trovata atom = !atom durante la costruzione del grafo");
                            ccText.setVisible(true);
                            satText.setText(satText.getText() + " NON SODDISFACIBILE");
                            satText.setVisible(true);
                            
                        }

			fileMenu.setEnabled(true);
                        modeMenu.setEnabled(true);
			log.setEditable(true);
			startButton.setText(START_BUTTON_INIT);
                        //jProgressBar1.setEnabled(false);
                        
		}
                
                private String formatoTime(long time) {
                    if (time < 1000)
                                        return time + " millisec.";
                                    else if (time > 60000) {
                                        long minuti = time / 60000;
                                        int secondi = (int) ((time / 1000) % 60);
                                        return minuti + " min. e " + secondi + " sec.";
                                    } else {
                                        int secondi = (int) (time / 1000);
                                        int milli = (int) (time % 1000);
                                        return secondi + " sec. e " + milli + " millisec.";
                                    }
                }

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            String property = pce.getPropertyName();
            //if ("background".equals(property))
            //System.err.println("STOP CAMBIATO??? " + property);
            return;
        }
	}
	
	
	private class Avanzamento implements Runnable {

		/**
		 * Computazione avviata
		 */
		@Override
		public void run() {
            int progress = 0;
            int newProgress = 0;
            int timeSleep = 700;
            //Initialize progress property.
            jProgressBar1.setValue(0);
			//jProgressBar1.setF
			jProgressBar1.setStringPainted(true);
            while (progress < 100 && !stop) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(timeSleep);
                    
                    //DEBUG
                    //System.out.println("AVANZAMENTO ANCORA IN ESECUZIONE");
                    
                } catch (InterruptedException ignore) {
                    //System.err.println("INTERRUPTED");
                    break;
                }
               
                if ((newProgress = Parser.avanzamento * 100 / formula.length()) == progress)
                    timeSleep += 300;
                else {
                    progress = newProgress;
                    jProgressBar1.setValue(Math.min(progress, 100));
                    timeSleep = Math.max(700, timeSleep - 300);
                }
                
            }
		}
	}
}
