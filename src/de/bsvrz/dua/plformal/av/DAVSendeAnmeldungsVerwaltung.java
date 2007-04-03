package de.bsvrz.dua.plformal.av;

import java.util.Collection;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.ClientSenderInterface;
import stauma.dav.clientside.SenderRole;
import sys.funclib.debug.Debug;

/**
 * Verwaltungsklasse für Datenanmeldungen zum Senden von Daten.
 * Über die Methode <code>modifiziereDatenAnmeldung(..)</code> lassen
 * sich Daten anmelden bzw. abmelden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *  
 */
public class DAVSendeAnmeldungsVerwaltung 
extends DAVAnmeldungsVerwaltung{

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Rolle des Senders
	 */
	private SenderRole rolle = null;
	
	/**
	 * Der Sender der Daten
	 */
	private ClientSenderInterface sender = null;
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteilerverbindung
	 * @param rolle Rolle
	 * @param sender die Sender-Klasse der Datenverteiler-
	 * Daten, für die diese Anmeldungs-Verwaltung arbeiten soll
	 */
	public DAVSendeAnmeldungsVerwaltung(final ClientDavInterface dav,
									    final SenderRole rolle, 
									    final ClientSenderInterface sender){
		super(dav);
		this.rolle = rolle;
		this.sender = sender;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String abmelden(final Collection<DAVObjektAnmeldung> abmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(abmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung abmeldung:abmeldungen){
			try{
				this.dav.unsubscribeSender(this.sender, abmeldung.getObjekt(),
						abmeldung.getDatenBeschreibung());
				this.aktuelleObjektAnmeldungen.remove(abmeldung);
				info += abmeldung;
			}catch(Exception e){
				LOGGER.error("Probleme beim Abmelden als Sender/Quelle", e); //$NON-NLS-1$
			}
		}
		return info + "von [" + sender + "]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String anmelden(final Collection<DAVObjektAnmeldung> anmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(anmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung anmeldung:anmeldungen){
			try {
				this.dav.subscribeSender(this.sender, anmeldung.getObjekt(),
						anmeldung.getDatenBeschreibung(), this.rolle);
				this.aktuelleObjektAnmeldungen.add(anmeldung);
				info += anmeldung;
			} catch (Exception e) {
				LOGGER.error("Probleme beim Anmelden als Sender/Quelle", e); //$NON-NLS-1$
			}		
		}
		return info + "für [" + sender + "]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}
	
}
