package de.bsvrz.dua.plformal.allgemein.schnittstellen;

import stauma.dav.clientside.ResultData;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.dfs.IDatenFlussSteuerungsListener;
import de.bsvrz.dua.plformal.dfs.ModulTyp;

/**
 * Allgemeine Beschreibung der Schnittstelle Berarbeitungsknoten.
 * Diese Schnittstelle wird zur Initialisierung und Verkettung
 * von verschiedenen Modulen der DUA verwendet.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IBearbeitungsKnoten
extends IDatenFlussSteuerungsListener{
	
	/**
	 * Setzt die Verbindung zum Verwaltungsmodul und
	 * initialisiert diesen Bearbeitungsknoten. Nach
	 * dem Aufruf dieser Methode wird davon ausgegangen,
	 * dass der Knoten voll funktionsf�hig ist. Also
	 * zum Beispiel alle Sendeanmeldungen durchgef�hrt
	 * wurden. 
	 * 
	 * @param verwaltung eine Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException wird ausgel�st,
	 * wenn als Parameter <code>null</code> �bergeben wurde.
	 */
	public void initialisiere(IVerwaltung verwaltung)
	throws DUAInitialisierungsException;

	/**
	 * Teilt diesem Knoten mit, an welchen Knoten die Daten nach der
	 * vollst�ndigen Bearbeitung durch diesen Knoten weitergeleitet
	 * werden sollen.
	 * 
	 * @param knoten der chronologisch nachgeordnete Bearbeitungsknoten
	 * oder <code>null</code>, wenn dieser Knoten der Letzte ist.
	 */
	public void setNaechstenBearbeitungsKnoten(IBearbeitungsKnoten knoten);

	/**
	 * Legt fest, ob eine Publikation der in diesem
	 * Bearbeitungsknoten aufbereiteten Daten in den
	 * Datenverteiler stattfinden soll. 
	 * 
	 * @param publizieren <code>true</code>, wenn 
	 * publiziert werden soll
	 */
	public void setPublikation(boolean publizieren);
	
	/**
	 * Aktualisierungsmethode. �ber diese Methode sollten
	 * dem Objekt, das dieses Interface implementiert alle
	 * zu bearbeitenden Daten zur Verf�gung gestellt werden.  
	 * 
	 * @param arg0 aktuelle Daten vom Vorg�ngerknoten.
	 */
	public void aktualisiereDaten(ResultData[] arg0);
	
	/**
	 * Erfragt den Typen des Moduls, das dieses
	 * Interface implementiert
	 * 
	 * @return der Name dieses Moduls
	 */
	public ModulTyp getModulTyp();

}
