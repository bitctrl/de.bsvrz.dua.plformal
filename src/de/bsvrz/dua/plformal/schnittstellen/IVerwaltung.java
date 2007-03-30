package de.bsvrz.dua.plformal.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.application.StandardApplication;
import sys.funclib.operatingMessage.MessageGrade;
import sys.funclib.operatingMessage.MessageState;
import sys.funclib.operatingMessage.MessageType;

/**
 * Abstrakte Implementation einer Schnittstelle zu einem Verwaltungsmodul. Ein
 * Verwaltungsmodul stellt immer den Eintrittspunkt in eine SWE4.x dar und 
 * implementiert daher das Interface <code>StandardApplication</code>.
 * 
 * @author Thierfelder
 *
 */
public interface IVerwaltung
extends StandardApplication,
		ClientReceiverInterface{

	/**
	 * Erfragt die Verbindung zum Datenverteiler.
	 * 
	 * @return die Verbindung zum Datenverteiler
	 */
	public ClientDavInterface getVerbindung();
	
	/**
	 * Sendet eine Betriebsmeldung an die Betriebsmeldungsverwaltung
	 * 
	 * @param id ID der Meldung. Dieses Attribut kann von der Applikation gesetzt werden,
	 * um einen Bezug zu einer vorherigen Meldung herzustellen.
	 * @param typ Typ der Betriebsmeldung (Diese Klasse stellt die beiden Zustände
	 * "System" und "Fach" für Meldungen, die sich auf systemtechnische oder
	 * fachliche Zustände beziehen, bereit)
	 * @param nachrichtenTypErweiterung Erweiterung
	 * @param klasse Klasse der Betriebsmeldung
	 * @param status Gibt den Zustand einer Meldung an
	 * @param nachricht Nachrichtentext der Betriebsmeldung
	 */
	public void sendeBetriebsMeldung(final String id,
									 final MessageType typ,
									 final String nachrichtenTypErweiterung,
									 final MessageGrade klasse,
									 final MessageState status,
									 final String nachricht);

	/**
	 * Über diese Methode soll ein Modul Verwaltung anderen Modulen
	 * die Menge aller zu bearbeitenden Objekte zur Verfügung stellen.
	 * Sollte an dieser Stelle <code>null</code> übergeben werden, so
	 * sollten vom fragenden Modul alle inhaltlich passenden Systemobjekte
	 * des Standardkonfigurationsbereichs zur Bearbeitung angenommen werden.
	 * 
	 * @return alle zu bearbeitenden Objekte
	 */
	public SystemObject[] getSystemObjekte();
	
	/**
	 * Erfragt die dem Verwaltungsmodul übergebenen Konfigurationsbereiche
	 * 
	 * @return alle Konfigurationsbereiche, die diesem Verwaltungsmodul übergeben wurden.
	 */
	public Collection<ConfigurationArea> getKonfigurationsBereiche();
	
	/**
	 * Diese Methode gibt den Namen der Applikation (SWE) zurück,
	 * die dieses Interface implementiert. Dieser Name entspricht
	 * der innerhalb der Datenflusssteuerung angelegten ID 
	 * für diese SWE.
	 * 
	 * @return Name dieser Applikation
	 */
	public String getApplikationsName();
	
}
