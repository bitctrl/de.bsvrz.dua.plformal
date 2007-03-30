package de.bsvrz.dua.plformal.adapter;

import java.util.ArrayList;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.ArgumentList;
import sys.funclib.debug.Debug;
import sys.funclib.operatingMessage.MessageGrade;
import sys.funclib.operatingMessage.MessageSender;
import sys.funclib.operatingMessage.MessageState;
import sys.funclib.operatingMessage.MessageType;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.schnittstellen.IVerwaltung;

/**
 * Adapterklasse für Verwaltungsmodule.
 * 
 * @author Thierfelder
 *
 */
public abstract class AbstraktVerwaltungsAdapter
implements IVerwaltung {
	
	/**
	 * der Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
			
	/**
	 * Die Objekte, die bearbeitet werden sollen.
	 */
	protected SystemObject[] objekte = null;
	
	/**
	 * Verbindung zum Datenverteiler 
	 */
	protected ClientDavInterface verbindung = null;
	
	/**
	 * Diese Klasse versendet Betriebsmeldungen
	 */
	protected MessageSender nachrichtenSender = null;
	
	/**
	 * die Argumente der Kommandozeile
	 */
	protected ArrayList<String> komArgumente = new ArrayList<String>();

	/**
	 * {@inheritDoc}
	 */
	public final SystemObject[] getSystemObjekte() {
		return this.objekte;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final ClientDavInterface getVerbindung() {
		return this.verbindung;
	}
	
	/**
	 * Erfragt eine Verbindung zum Versenden von Betriebsmeldungen.
	 * 
	 * @return Betriebsmeldungs-Sender.
	 */
	public final MessageSender getBetriebsmeldungsSender(){
		return this.nachrichtenSender;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void sendeBetriebsMeldung(String id,
									 MessageType typ,
									 String nachrichtenTypErweiterung,
									 MessageGrade klasse,
									 MessageState status,
									 String nachricht) {
		this.nachrichtenSender.sendMessage(id, typ, nachrichtenTypErweiterung,
															klasse, status, nachricht);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface dieVerbindung)
	throws Exception {
		try{
			this.verbindung = dieVerbindung;
			DAVKonstanten.initialisiere(dieVerbindung);
			this.nachrichtenSender = MessageSender.getInstance();
			this.nachrichtenSender.setApplicationLabel(this.getApplikationsName());
			this.initialisiere();
		}catch(DUAInitialisierungsException ex){
			String fehler = "Initialisierung der Applikation " + this.getApplikationsName() + //$NON-NLS-1$
							" fehlgeschlagen."; //$NON-NLS-1$
			LOGGER.error(fehler, ex);
			if(this.nachrichtenSender != null){
				this.nachrichtenSender.sendMessage(MessageType.APPLICATION_DOMAIN, MessageGrade.ERROR,
						fehler);
			}
			Runtime.getRuntime().exit(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void parseArguments(ArgumentList argumente)
	throws Exception {
		
		Debug.init(this.getApplikationsName(), argumente);
		
		for(String s:argumente.getArgumentStrings()){
			if(s != null)this.komArgumente.add(s);
		}
		
		argumente.fetchUnusedArguments();
	}
	
	/**
	 * Diese Methode wird zur Initialisierung aufgerufen, <b>nachdem</b> sowohl die
	 * Argumente der Kommandozeile, als auch die Datenverteilerverbindung übergeben
	 * wurden (also nach dem Aufruf der Methoden <code>parseArguments(..)</code> und 
	 * <code>initialize(..)</code>).
	 * 
	 * @throws DUAInitialisierungsException falls es Probleme bei der
	 * Initialisierung geben sollte
	 */
	protected abstract void initialisiere() throws DUAInitialisierungsException;
	
}
