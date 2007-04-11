/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.av;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;

/**
 * Beschreibung einer Datenanmeldung mit (potentiell)
 * mehreren Systemobjekten und einer Datenbeschreibung.
 * Die Systemobjekte k�nnen hier sowohl Typen wie auch
 * Instanzen sein.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class DAVDatenAnmeldung{

	/**
	 * Menge aller einzelnen Objektanmeldungen
	 */
	protected Collection<DAVObjektAnmeldung> objektAnmeldungen = 
									new TreeSet<DAVObjektAnmeldung>();
	
	/**
	 * Die �bergebenen Konfigurationsbereiche
	 */
	protected Collection<ConfigurationArea> konfigurationsBereiche = null;
	

	/**
	 * Standardkonstruktor (alle �bergebenen Typen werden
	 * in ihre Instanzen aufgel�st). Sollten innerhalb der
	 * Datenbeschreibung sog. Wildcards (in Form von <code>
	 * null</code>) vorkommen, so werden diese
	 * hier aufgel�st.
	 * 
	 * @param objekte Objekte, f�r die die Anmeldung gilt
	 * @param datenBeschreibung Datenbeschreibung, f�r die
	 * die Objekte angemeldet sind
	 * @param Verbindung zum Datenverteiler
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung,
							 final ClientDavInterface dav){
		if(objekte == null || objekte.length == 0){
			this.objektAnmeldungen.addAll(DUAHilfe.
					getAlleObjektAnmeldungen(null, datenBeschreibung, dav));
		}else{
			for(SystemObject obj:objekte){
				this.objektAnmeldungen.addAll(DUAHilfe.
						getAlleObjektAnmeldungen(obj, datenBeschreibung, dav));
			}
		}
	}
	
	/**
	 * Erweiterter Konstruktor mit Konfigurationsbereichen.
	 * Es werden hier nur Datenanmeldungen erzeugt, deren
	 * Objekte innerhalb der �bergebenen Konfigurationsbereiche
	 * definiert sind. (alle �bergebenen Typen werden in ihre
	 * Instanzen aufgel�st)
	 * 
	 * @param objekte Objekte, f�r die die Anmeldung gilt
	 * @param desc Datenbeschreibung, f�r die die Objekte angemeldet sind
	 * @param kb Liste mit Konfigurationsbereichen, durch die die Objektliste
	 * noch gefiltert werden soll
	 * @param dav Datenverteiler-Verbindung
	 */
	public DAVDatenAnmeldung(final SystemObject[] objekte,
							 final DataDescription datenBeschreibung,
							 final Collection<ConfigurationArea> kBereiche,
							 final ClientDavInterface dav)
	throws NullPointerException{
		this.konfigurationsBereiche = kBereiche;
		
		if(objekte == null || objekte.length == 0){
			for(SystemObject finObj:DUAHilfe.getFinaleObjekte(null, dav, kBereiche)){
				this.objektAnmeldungen.addAll(DUAHilfe.
					getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
			}
		}else{
			for(SystemObject obj:objekte){
				for(SystemObject finObj:DUAHilfe.getFinaleObjekte(obj, dav, kBereiche)){
					this.objektAnmeldungen.addAll(DUAHilfe.
						getAlleObjektAnmeldungen(finObj, datenBeschreibung, dav));
				}
			}
		}
	}
	
	/**
	 * Erfragt die Datenbeschreibung, f�r die die
	 * Objekte angemeldet sind
	 * 
	 * @return die Datenbeschreibung, f�r die die
	 * Objekte angemeldet sind
	 */
	public final DataDescription getDatenBeschreibung(){
		DataDescription datenBeschreibung = null;
		for(DAVObjektAnmeldung objektAnmeldung:this.objektAnmeldungen){
			datenBeschreibung = objektAnmeldung.getDatenBeschreibung();
			break;
		}
		return datenBeschreibung;
	}	

	/**
	 * Erfragt die (finalen) Objekte, f�r die die Anmeldung gilt
	 * 
	 * @return (finale) Objekte, f�r die die Anmeldung gilt
	 */
	public final Collection<SystemObject> getObjekte() {
		Collection<SystemObject> objekte = 
							new HashSet<SystemObject>();
		
		for(DAVObjektAnmeldung objektAnmeldung:this.objektAnmeldungen){
			objekte.add(objektAnmeldung.getObjekt());
		}
		
		return objekte;
	}

	/**
	 * Erfragt die Objekte, f�r die die Anmeldung gilt als Array
	 * 
	 * @return die Objekte, f�r die die Anmeldung gilt als Array
	 */
	public final SystemObject[] getObjekteAlsArray() {
		return getObjekte().toArray(new SystemObject[0]);
	}
	
	/**
	 * Erfragt alle einzelnen Objekt-Anmeldungen
	 * die innerhalb von diesem Objekt definiert sind
	 * 
	 * @return eine (ggf. leere) Menge von Objekt-Anmeldungen
	 */
	public final Collection<DAVObjektAnmeldung> getObjektAnmeldungen(){
		return this.objektAnmeldungen;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "Datenanmeldungsbeschreibung\n"; //$NON-NLS-1$
		String bereiche = "keine\n"; //$NON-NLS-1$
		
		if(this.konfigurationsBereiche != null){
			bereiche = DUAKonstanten.EMPTY_STR;
			for(ConfigurationArea kb:this.konfigurationsBereiche){
				bereiche += kb.toString() + "\n"; //$NON-NLS-1$
			}
		}
		s += "Konfigurationsbereiche:\n" + bereiche;  //$NON-NLS-1$
		s += "Datenbeschreibung:\n" + getDatenBeschreibung() + "\nObjekte:\n"; //$NON-NLS-1$ //$NON-NLS-2$
		for(SystemObject obj:getObjekte()){
			s += obj + "\n"; //$NON-NLS-1$
		}
		
		return s;
	}
	
}
