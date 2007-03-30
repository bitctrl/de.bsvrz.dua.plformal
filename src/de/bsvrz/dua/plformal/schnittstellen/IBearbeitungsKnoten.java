package de.bsvrz.dua.plformal.schnittstellen;

import stauma.dav.clientside.ResultData;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.dfs.IDatenFlussSteuerungsListener;

/**
 * Allgemeine Beschreibung der Schnittstelle Berarbeitungsknoten. Diese
 * Schnittstelle wird zur Initialisierung und Verkettung von verschiedenen
 * Modulen aus der DuA verwendet.
 * 
 * @author Thierfelder
 *
 */
public interface IBearbeitungsKnoten
extends IDatenFlussSteuerungsListener{
	
	/**
	 * Setzt die Verbindung zum Verwaltungsmodul und initialisiert diesen
	 * Bearbeitungsknoten. Nach dem Aufruf dieser Methode wird davon ausgegangen,
	 * dass der Knoten voll funktionsf�hig ist. Also zum Beispiel alle
	 * Sendeanmeldungen durchgef�hrt wurden. 
	 * 
	 * @param verwaltung eine Verbindung zum Verwaltungsmodul.
	 * @throws DUAInitialisierungsException wird ausgel�st, wenn als Parameter
	 * <code>null</code> �bergeben wurde.
	 */
	public void initialisiere(IVerwaltung verwaltung) throws DUAInitialisierungsException;

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
	 * Legt fest, ob eine Publikation in den Datenverteiler stattfinden soll.
	 * Bevor diese Publikation stattfinden kann, muss von der implementierenden 
	 * Klasse ermittelt werden, ob der Standardaspekt der Publikation eines
	 * bestimmten Knotens f�r ein hier bearbeitetes Datum existiert. Sollte dies
	 * nicht der Fall sein, so werden die Parameter aus der Datenflusssteuerung
	 * f�r die Publikation herangezogen.
	 * 
	 * @param publizieren soll publiziert werden?
	 */
	public void setPublikation(boolean publizieren);
	
	/**
	 * Aktualisierungsmethode. �ber diese Methode sollten dem Objekt, das
	 * dieses Interface implementiert alle zu bearbeitenden Daten zur Verf�gung
	 * gestellt werden.  
	 * 
	 * @param arg0 aktuelle Daten vom Vorg�ngerknoten.
	 */
	public void aktualisiereDaten(ResultData[] arg0);
	
	/**
	 * Erfragt den Namen des Moduls, das dieses Interface implementiert
	 * 
	 * @return der Name dieses Moduls
	 */
	public String getModulName();

}
