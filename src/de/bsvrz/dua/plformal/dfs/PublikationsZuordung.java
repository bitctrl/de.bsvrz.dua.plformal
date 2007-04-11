/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
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
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAUtensilien;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;
import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;

/**
 * In dieser Klasse sind alle Informationen zusammengefasst, die das
 * Publikationsverhalten bezüglich <b>einer</b> bestimmten SWE, <b>einem</b>
 * bestimmten Modul-Typ und <b>einem</b> Publikationsaspekt beschreiben
 * innerhalb der Datenflusssteuerung beschreiben.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class PublikationsZuordung {

	/**
	 * Der Modul-Typ
	 */
	private ModulTyp modulTyp = null;

	/**
	 * Der Publikationsaspekt
	 */
	private Aspect aspekt = null;

	/**
	 * die (finalen) Objekte, für die ein Publikationsverhalten beschrieben ist
	 */
	private Collection<SystemObject> objekte = new HashSet<SystemObject>();

	/**
	 * die Attributgruppen, für die ein Publikationsverhalten vorgesehen ist
	 */
	private Collection<AttributeGroup> atgs = new HashSet<AttributeGroup>();

	/**
	 * soll publiziert werden
	 */
	private boolean publizieren = false;
	
	/**
	 * Die Objektanmeldungen, die innerhalb dieser Publikationszuordnung
	 * vorgesehen sind (bzw. bei <code>publizieren ==  false</code> explizit
	 * nicht vorgesehen)
	 */
	private Collection<DAVObjektAnmeldung> anmeldungen = 
									new TreeSet<DAVObjektAnmeldung>();

	
	/**
	 * Standardkonstruktor<br>
	 * <b>Achtung:</b> Sollte die Menge der übergebenen
	 * Objekte bzw. Attributgruppen leer sein, so werden
	 * <b>alle</b> Objekte bzw. Attributgruppen in den
	 * übergebenen Konfigurationskereichen (bzw. im 
	 * Standardkonfigurationsbereich) angenommen.
	 * 
	 * @param data ein Datenverteiler-Datum mit den
	 * mit einer Publikationszuordnung assoziierten Daten
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @throws Exception wenn einer der Parameter nicht
	 * ausgelesen werden konnte
	 */
	protected PublikationsZuordung(final Data data, final IVerwaltung verwaltung)
	throws Exception{
		this.aspekt = (Aspect)data.getReferenceValue(
				DFSKonstanten.ATT_ASP).getSystemObject();
		this.modulTyp = ModulTyp.getZustand((int)data.getUnscaledValue(
				DFSKonstanten.ATT_MODUL_TYP).getState().getValue());
		this.publizieren = data.getTextValue(
				DFSKonstanten.ATT_PUBLIZIEREN).getText()
				.toLowerCase().equals("ja"); //$NON-NLS-1$

		if(data.getArray(DFSKonstanten.ATT_OBJ).getLength() == 0){
			this.objekte.addAll(DUAUtensilien.getFinaleObjekte(null,
					verwaltung.getVerbindung(),
					verwaltung.getKonfigurationsBereiche()));			
		}else{
			for (int iObj = 0; iObj < data.getArray(
					DFSKonstanten.ATT_OBJ).getLength(); iObj++) {
				SystemObject dummy = data.getArray(DFSKonstanten.ATT_OBJ)
										.getReferenceValue(iObj).getSystemObject();
				
				this.objekte.addAll(DUAUtensilien.getFinaleObjekte(dummy,
												verwaltung.getVerbindung(),
												verwaltung.getKonfigurationsBereiche()));
			}
		}
		
		if(data.getArray(DFSKonstanten.ATT_ATG).getLength() == 0){
			this.atgs.add(null);
		}else{
			for (int iAtg = 0; iAtg < data.getArray(
					DFSKonstanten.ATT_ATG).getLength(); iAtg++) {
				this.atgs.add((AttributeGroup) data.getArray(
						DFSKonstanten.ATT_ATG)
						.getReferenceValue(iAtg)
						.getSystemObject());
			}
		}
		
		for(AttributeGroup atg:this.atgs){
			DataDescription datenBeschreibung = new DataDescription(atg, this.aspekt, (short)0);
						
			for(SystemObject finObj:this.objekte){
				this.anmeldungen.addAll(DUAUtensilien.
						getAlleObjektAnmeldungen(finObj, datenBeschreibung, verwaltung.getVerbindung()));
			}
		}
	}
	
	/**
	 * Erfragt die Objektanmeldungen, die innerhalb dieser Publikationszuordnung
	 * vorgesehen sind (bzw. bei <code>publizieren ==  false</code> explizit
	 * nicht vorgesehen sind)
	 * 
	 * @return eine Menge von Objektanmeldungen
	 */
	public Collection<DAVObjektAnmeldung> getObjektAnmeldungen(){
		return this.anmeldungen;
	}
	
	/**
	 * Erfragt den Aspekt
	 * 
	 * @return den Aspekt
	 */
	public Aspect getAspekt() {
		return aspekt;
	}

	/**
	 * Erfragt den Modul-Typ, für den diese Piblikationszuordnung gilt
	 * 
	 * @return der Modul-Typs
	 */
	public final ModulTyp getModulTyp() {
		return modulTyp;
	}

	/**
	 * Erfragt das Publikations-FLAG
	 * 
	 * @return das Publikations-FLAG
	 */
	public final boolean isPublizieren() {
		return publizieren;
	}

	/**
	 * Erfragt alle hier definierten Attributgruppen
	 * 
	 * @return alle hier definierten Attributgruppen
	 */
	public final Collection<AttributeGroup> getAtgs() {
		return atgs;
	}

	/**
	 * Erfragt die Menge aller hier definierten (finalen) Objekte
	 * 
	 * @return die Menge aller hier definierten (finalen) Objekte
	 */
	public final Collection<SystemObject> getObjekte() {
		return this.objekte;
	}

	/**
	 * Fragt, ob eine bestimmte Publikationszuordnung mit dieser hier kompatibel
	 * ist. Ob sie sich also widersprechen. Ein Widerspruch liegt vor, wenn:<br>
	 * 1. der Modul-Typ identisch ist UND<br>
	 * 2. für <b>beide</b> Objekte die Publikation eingeschalten ist UND<br>
	 * 3. der Publikationsaspekt der beiden Objekte <code>this</code> und
	 * <code>vergleichsObj</code> nicht identisch ist UND<br>
	 * 4. eine Objekt-Überschneidung innerhalb der Member-SystemObjekte von
	 * <code>this</code> und <code>vergleichsObj</code> besteht UND<br>
	 * 5. die Schnittmenge der Member-Attributgruppen nicht leer ist.<br>
	 * 
	 * @param that
	 *            das Objekt, mit dem dieses verglichen werden soll
	 * @return <code>null</code> wenn kein Widerspruch vorliegt und eine den
	 *         Widerspruch illustrierende Fehlermeldung sonst.
	 */
	public final String isKompatibelMit(final PublikationsZuordung that) {
		try{
			if (this.modulTyp.equals(that.getModulTyp()) && // 1.
				this.isPublizieren() && that.isPublizieren() && // 2.
				!this.getAspekt().equals(that.getAspekt())) { // 3.
	
				for(DAVObjektAnmeldung thisAnmeldung:this.getObjektAnmeldungen()){	// 4. & 5.
					for(DAVObjektAnmeldung thatAnmeldung:that.getObjektAnmeldungen()){
						if(thisAnmeldung.getObjekt().equals(thatAnmeldung.getObjekt()) && 
						   thisAnmeldung.getDatenBeschreibung().getAttributeGroup().equals(
								   thatAnmeldung.getDatenBeschreibung().getAttributeGroup())){
							return "Die beiden Objektanmeldungen sind für" + //$NON-NLS-1$
									" die Datenflusssteuerung widersprüchlich:\n" + //$NON-NLS-1$ 
									thisAnmeldung + "\n" + thatAnmeldung; //$NON-NLS-1$							
						}
					}				
				}
			}
		}catch(Exception ex){
			return "Die Kompatibilitätsprüfung konnte nicht" + //$NON-NLS-1$
					" vollständig durchgeführt werden: " + ex.getMessage(); //$NON-NLS-1$
		}
			
		return null; // keine Widersprüche
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "Modul-Typ: " + modulTyp + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Aspekt: " + aspekt + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		s += "Publizieren: " + (publizieren ? "ja" : "nein") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		for (SystemObject obj : objekte) {
			s += "Objekt: " + obj + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		for (AttributeGroup atg : atgs) {
			s += "Atg: " + atg + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
}
