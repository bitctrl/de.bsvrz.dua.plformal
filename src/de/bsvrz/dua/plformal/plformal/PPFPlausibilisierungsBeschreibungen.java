/**
 * Segment 4 Datenübernahme und Aufbereitung, SWE 4.1 Plausibilitätsprüfung formal
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

package de.bsvrz.dua.plformal.plformal;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.clientside.Data.ReferenceArray;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Diese Klasse stellt die Informationen der Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> für einen
 * möglichst schnellen Zugriff zur Verfügung. Mittels der
 * Methode <code>getAttributSpezifikationenFuer(..)</code>
 * kann für ein ResultData-Objekt erfragt werden, wie dieses
 * formal zu plausibilisieren ist.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PPFPlausibilisierungsBeschreibungen {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Alle Informationen, die zur Plausibilisierung einer
	 * Systemobjekt-Attributgruppen-Aspekt-Kombination
	 * notwendig sind
	 */
	private TreeMap<DAVObjektAnmeldung, BeschreibungFuerObjekt> resDataInfos =
				new TreeMap<DAVObjektAnmeldung, BeschreibungFuerObjekt>(); 
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	private IVerwaltung verwaltung = null;
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 */
	protected PPFPlausibilisierungsBeschreibungen(final 
			IVerwaltung verwaltung){
		this.verwaltung = verwaltung;
	}
	
	/**
	 * Fügt diesem Objekt einen neuen Parametersatz der Attributgruppe
	 * <code>atg.plausibilitätsPrüfungFormal</code> hinzu.
	 * 
	 * @param parameterSatz ein Parametersatz der Attributgruppe
	 * <code>atg.plausibilitätsPrüfungFormal</code>
	 * @throws Exception falls Fehler beim Auslesen des DAV-Datensatzes
	 * auftreten
	 */
	protected final void addParameterSatz(final Data parameterSatz)
	throws Exception{
		if(parameterSatz != null){
			final AttributeGroup atg = (AttributeGroup)parameterSatz.getReferenceValue(
					DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATG).getSystemObject();
			final Aspect asp = (Aspect)parameterSatz.getReferenceValue(
					DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ASP).getSystemObject();
			
			if(atg == null){
				throw new Exception("Übergebene Attributgruppe ist " //$NON-NLS-1$
						+ DUAKonstanten.NULL + ": " + parameterSatz); //$NON-NLS-1$
			}
			if(asp == null){
				throw new Exception("Übergebener Aspekt ist " //$NON-NLS-1$
						+ DUAKonstanten.NULL + ": " + parameterSatz); //$NON-NLS-1$
			}
			DataDescription dd = new DataDescription(atg, asp, (short)0);
			
			final ReferenceArray objekte = parameterSatz.getArray(DUAKonstanten.
					ATL_PL_FORMAL_PARA_SATZ_OBJ).asReferenceArray();
			
			Collection<SystemObject> finObjekte = new HashSet<SystemObject>(); 
			if(objekte == null || objekte.getLength() == 0){
				finObjekte = DUAHilfe.getFinaleObjekte(null,
						this.verwaltung.getVerbindung(),
						this.verwaltung.getKonfigurationsBereiche());
			}else{
				for(int i = 0; i< objekte.getLength(); i++){
					finObjekte.addAll(DUAHilfe.getFinaleObjekte(objekte.getSystemObject(i),
							this.verwaltung.getVerbindung(),
							this.verwaltung.getKonfigurationsBereiche()));	
				}				
			}
			
			final BeschreibungFuerObjekt objBeschr = new BeschreibungFuerObjekt(parameterSatz);
			
			for(SystemObject finObj:finObjekte){
				try{
					DAVObjektAnmeldung dummy = new DAVObjektAnmeldung(finObj, dd);
					if(this.resDataInfos.containsKey(dummy)){
						this.resDataInfos.get(dummy).addBeschreibung(parameterSatz);
					}else{
						this.resDataInfos.put(dummy, objBeschr);
					}
				}catch(Exception ex){
					LOGGER.warning(DUAKonstanten.EMPTY_STR, ex);
				}						
			}
		}else{
			throw new Exception("Übergebener Parametersatz ist " //$NON-NLS-1$
								+ DUAKonstanten.NULL);
		}
		
		LOGGER.info(this.toString());
	}
	
	/**
	 * Erfragt die Menge von Attributspezifikationen für ein übergebenes
	 * <code>ResultData</code>-Objekt
	 * 
	 * @param resultat ein <code>ResultData</code>-Objekt
	 * @return eine (ggf. leere) Menge von Attributspezifikationen
	 */
	protected final Collection<PPFAttributSpezifikation> 
						getAttributSpezifikationenFuer(ResultData resultat){
		Collection<PPFAttributSpezifikation> ergebnis = new HashSet<PPFAttributSpezifikation>();
		
		if(resultat != null){
			try{
				DAVObjektAnmeldung objektAnmeldung = new DAVObjektAnmeldung(resultat);
				BeschreibungFuerObjekt objBeschr = this.resDataInfos.get(objektAnmeldung);
				if(objBeschr != null){
					ergebnis.addAll(objBeschr.getAttributSpezifikationen());
				}else{
					LOGGER.fine("Es wurde keine Plausibilisierungsvorschrift" + //$NON-NLS-1$
							" gefunden für: " +	resultat); //$NON-NLS-1$
				}
			}catch(Exception ex){
				LOGGER.error("Attributspezifikationen konnten" + //$NON-NLS-1$
						" nicht ausgelesen werden", ex); //$NON-NLS-1$
			}
		}else{
			LOGGER.info("Übergebenes ResultData-Objekt ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}
		
		return ergebnis;
	}
	
	/**
	 * Erfragt die Objektanmeldungen, die notwendig sind,
	 * um die Daten, die für die formale Plausibilitätsprüfung
	 * vorgesehen sind empfangen zu können
	 * 
	 * @return eine (ggf. leere) Menge von Objektanmeldungen
	 */
	public final Collection<DAVObjektAnmeldung> getObjektAnmeldungen(){
		Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<DAVObjektAnmeldung>();
		anmeldungen.addAll(this.resDataInfos.keySet());
		return anmeldungen;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = DUAKonstanten.STR_UNDEFINIERT;

		if(resDataInfos.keySet().size() > 0){
			s = DUAKonstanten.EMPTY_STR;
			for(DAVObjektAnmeldung objAnm:resDataInfos.keySet()){
				s += "Datenbeschreibung: " + objAnm + "\n";  //$NON-NLS-1$//$NON-NLS-2$
				s += this.resDataInfos.get(objAnm);
			}
		}
		
		return s;
	}			
	
	
	/**
	 * Speichert alle Informationen, die für die Plausibilisierung
	 * <b>eines</b> Systemobjektes unter <b>einer</b> Datenbeschreibung
	 * notwendig sind (bzw. alle Informationen, die innerhalb eines Datensatzes
	 * der Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code> 
	 * in der Attributliste <code>AttributSpezifikation</code> stehen).
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class BeschreibungFuerObjekt{

		/**
		 * Menge von Plausibilisierungsspezifikationen (Attributspezifikationen)
		 * für ein Objekt unter einer Datenbeschreibung
		 */
		private Collection<PPFAttributSpezifikation> attSpez =
					new HashSet<PPFAttributSpezifikation>();
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param attSpez eine neue <code>AttributSpezifikation</code>
		 * @throws Exception wenn das Auslesen der Daten nicht vollständig
		 * erfolgreich war
		 */
		protected BeschreibungFuerObjekt(final Data attSpezDatum)
		throws Exception{
			addBeschreibung(attSpezDatum);
		}
		
		/**
		 * Fügt diesem Objekt eine neue <code>AttributSpezifikation</code>
		 * hinzu
		 * 
		 * @param attSpez eine neue <code>AttributSpezifikation</code>
		 * @throws Exception wenn das Auslesen der Daten nicht vollständig
		 * erfolgreich war
		 */
		protected void addBeschreibung(final Data attSpezDatum)
		throws Exception{
			final Data.Array attribut = attSpezDatum.getArray(
					DUAKonstanten.ATL_PL_FORMAL_PARA_SATZ_ATT_SPEZ);
			
			for(int i = 0; i<attribut.getLength(); i++){
				final Data attributSpezifikation = attribut.getItem(i);
				PPFAttributSpezifikation dummy = 
							new PPFAttributSpezifikation(attributSpezifikation);
				attSpez.add(dummy);
			}
		}		

		/**
		 * Erfragt die für dieses Objekt gespeicherten
		 * Attributspezifikationen
		 * 
		 * @return die Attributspezifikationen (ggf. leere Menge)
		 */
		protected final Collection<PPFAttributSpezifikation> 
											getAttributSpezifikationen(){
			return this.attSpez;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = DUAKonstanten.STR_UNDEFINIERT;
			
			if(attSpez.size() > 0){
				s = DUAKonstanten.EMPTY_STR;
				for(PPFAttributSpezifikation attBeschreibung:attSpez){
					s += attBeschreibung;
				}
			}
			
			return s;
		}
	}
}
