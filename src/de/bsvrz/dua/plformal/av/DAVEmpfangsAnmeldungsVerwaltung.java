package de.bsvrz.dua.plformal.av;

import java.util.Collection;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import sys.funclib.debug.Debug;

/**
 * Verwaltungsklasse für Datenanmeldungen zum Empfangen von Daten.
 * Über die Methode <code>modifiziereDatenAnmeldung(..)</code> lassen
 * sich Daten anmelden bzw. abmelden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DAVEmpfangsAnmeldungsVerwaltung 
extends DAVAnmeldungsVerwaltung{
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Rolle des Empfängers
	 */
	private ReceiverRole rolle = null;
	
	/**
	 * Optionen
	 */
	private ReceiveOptions optionen = null;
	
	/**
	 * der Empfänger der Daten
	 */
	private ClientReceiverInterface empfaenger = null;
	

	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteilerverbindung
	 * @param rolle Rolle
	 * @param optionen Optionen
	 * @param empfaenger die Empfänger-Klasse der Datenverteiler-
	 * Daten, für die diese Anmeldungs-Verwaltung arbeiten soll
	 */
	public DAVEmpfangsAnmeldungsVerwaltung(final ClientDavInterface dav,
										   final ReceiverRole rolle,
										   final ReceiveOptions optionen,
										   final ClientReceiverInterface empfaenger){
		super(dav);
		this.rolle = rolle;
		this.optionen = optionen;
		this.empfaenger = empfaenger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String abmelden(final Collection
						<DAVObjektAnmeldung> abmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(abmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung abmeldung:abmeldungen){
			try{
				this.dav.unsubscribeReceiver(this.empfaenger,
						abmeldung.getObjekt(), abmeldung.getDatenBeschreibung());
				this.aktuelleObjektAnmeldungen.remove(abmeldung);
				info += abmeldung;
			}catch(Exception ex){
				LOGGER.error("Probleme beim Abmelden ale Empfänger/Senke", ex); //$NON-NLS-1$
			}
		}
		return info + "von [" + empfaenger + "]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String anmelden(final Collection
						<DAVObjektAnmeldung> anmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(anmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung anmeldung:anmeldungen){
			try{
				this.dav.subscribeReceiver(this.empfaenger,
					anmeldung.getObjekt(), anmeldung.getDatenBeschreibung(),
					this.optionen, this.rolle);
				this.aktuelleObjektAnmeldungen.add(anmeldung);
				info += anmeldung;
			}catch(Exception ex){
				LOGGER.error("Probleme beim Anmelden ale Empfänger/Senke", ex); //$NON-NLS-1$
			}
		}
		return info + "für [" + empfaenger + "]\n";  //$NON-NLS-1$//$NON-NLS-2$		
	}

}
