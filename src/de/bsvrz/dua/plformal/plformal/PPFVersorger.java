/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.1 Plausibilit�tspr�fung formal
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

package de.bsvrz.dua.plformal.plformal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAUtensilien;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorgerListener;
import de.bsvrz.dua.plformal.plformal.typen.PlausibilisierungsMethode;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Diese Klasse meldet sich auf die Attributgruppe
 * <code>atg.plausibilit�tsPr�fungFormal</code> eines
 * Objekts vom Typ <code>typ.plausibilit�tsPr�fungFormal</code>
 * an. Es stellt die darin enthaltenen Informationen �ber
 * die Schnittstelle <code>IPPFVersorger</code> allen angemeldeten
 * Instanzen von <code>IPPFVersorgerListener</code> zur Verf�gung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PPFVersorger
implements IPPFVersorger, ClientReceiverInterface{
		
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * die statische Instanz dieser Klasse
	 */
	private static PPFVersorger INSTANZ = null;
	
	/**
	 * Systemobjekt einer Ganzen Zahl
	 */
	private static SystemObjectType TYP_GANZ_ZAHL = null;
	
	/**
	 * Systemobjekt einer Gleitkommazahl
	 */
	private static SystemObjectType TYP_KOMMA_ZAHL = null;

	/**
	 * die aktuellen Informationen der
	 * Attributgruppe <code>atg.plausibilit�tsPr�fungFormal</code> 
	 */
	private PPFPlausibilisierungsBeschreibungen plBeschreibungen = null;
	
	/**
	 * Alle Listener dieses Objektes
	 */
	private Collection<IPPFVersorgerListener> listenerListe =
									new HashSet<IPPFVersorgerListener>();
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	private IVerwaltung verwaltung = null;
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @param plausbibilisierungsObjekt das Objekt vom Typ
	 * <code>typ.plausibilit�tsPr�fungFormal</code>, auf dessen 
	 * Daten sich angemeldet werden soll
	 * @throws DUAInitialisierungsException falls die
	 * Datenanmeldung fehl schl�gt 
	 */
	private PPFVersorger(final IVerwaltung verwaltung,
					 	 final SystemObject plausbibilisierungsObjekt)
	throws DUAInitialisierungsException{
		if(verwaltung == null){
			throw new DUAInitialisierungsException("Keine Verbindung" + //$NON-NLS-1$
					" zum Verwaltungsmodul"); //$NON-NLS-1$
		}
		if(verwaltung.getVerbindung() == null){
			throw new DUAInitialisierungsException("Keine Verbindung" + //$NON-NLS-1$
					" zum Datenverteiler"); //$NON-NLS-1$
		}
		if(plausbibilisierungsObjekt == null){
			throw new DUAInitialisierungsException("Ung�ltiges" + //$NON-NLS-1$
					" Plausibilisierungsobjekt"); //$NON-NLS-1$
		}
		this.verwaltung = verwaltung;

		TYP_GANZ_ZAHL = verwaltung.getVerbindung().getDataModel()
							.getType("typ.ganzzahlAttributTyp"); //$NON-NLS-1$
		TYP_KOMMA_ZAHL = verwaltung.getVerbindung().getDataModel()
							.getType("typ.kommazahlAttributTyp"); //$NON-NLS-1$

		try{
			final AttributeGroup atg = verwaltung.getVerbindung().getDataModel().
										getAttributeGroup(PPFKonstanten.ATG);
			final Aspect asp = verwaltung.getVerbindung().getDataModel().
								getAspect(Konstante.DAV_ASP_PARAMETER_SOLL);
			final DataDescription dd = new DataDescription(atg, asp, (short)0);
			verwaltung.getVerbindung().subscribeReceiver(this, plausbibilisierungsObjekt, dd,
						ReceiveOptions.normal(), ReceiverRole.receiver());
		}catch(Throwable t){
			throw new DUAInitialisierungsException("Unerwarteter" + //$NON-NLS-1$
					" Fehler beim Initialisieren der" + //$NON-NLS-1$
					" formalen Plausibilisierung", t); //$NON-NLS-1$
		}
		
		LOGGER.info("Initialisierung erfolgreich.\n" + //$NON-NLS-1$
				"F�r die formale Plausibilisierung" + //$NON-NLS-1$
				" wird das Objekt " + plausbibilisierungsObjekt +//$NON-NLS-1$ 
				" verwendet."); //$NON-NLS-1$
	}
	
	/**
	 * Erfragt die statische Instanz dieser Klasse.<br>
	 * <b>Achtung:</b> Es wird erst innerhalb der dem Verwaltungsmodul
	 * �bergebenen Konfigurationsbereiche und dann im Standard-
	 * Konfigurationsbereich nach einem Objekt vom Typ
	 * <code>typ.plausibilit�tsPr�fungFormal</code> gesucht.
	 * 
	 * @param verwaltung Verbindung zum Verwaltungsmodul
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException falls die
	 * Datenanmeldung fehl schl�gt 
	 */
	public static final PPFVersorger getInstanz(final IVerwaltung verwaltung)
	throws DUAInitialisierungsException{
		if(INSTANZ == null){
			/**
			 * Ermittlung des Objektes, das die formale Plausibilisierung beschreibt
			 */
			final SystemObjectType typPPF = (SystemObjectType)verwaltung.
						getVerbindung().getDataModel().getObject(PPFKonstanten.TYP);
			
			SystemObject[] plausibilisierungsObjekte = new SystemObject[0];
			
			if(typPPF != null){
				plausibilisierungsObjekte = DUAUtensilien
						.getFinaleObjekte(typPPF, verwaltung.getVerbindung(),
								verwaltung.getKonfigurationsBereiche()).toArray(new SystemObject[0]);
			}
				
 			if(plausibilisierungsObjekte.length == 0){
 				/**
 				 * nochmal suchen, ob im Standardkonfigurationsbereich ein
 				 * Objekt vom Typ <code>typ.plausibilit�tsPr�fungFormal</code>
 				 * vorhanden ist
 				 */
				plausibilisierungsObjekte = DUAUtensilien
					.getFinaleObjekte(typPPF, verwaltung.getVerbindung(), null)
						.toArray(new SystemObject[0]);
 			}

			if(plausibilisierungsObjekte.length > 0){
				if(plausibilisierungsObjekte.length > 1){
					LOGGER.warning("Es liegen mehrere Objekte vom Typ " + //$NON-NLS-1$
							PPFKonstanten.TYP + " vor"); //$NON-NLS-1$
				}
				INSTANZ = new PPFVersorger(verwaltung, plausibilisierungsObjekte[0]);
			}else{
				throw new DUAInitialisierungsException("Es liegt kein Objekt vom Typ " + //$NON-NLS-1$
						PPFKonstanten.TYP + " vor");  //$NON-NLS-1$
			}
		}

		return INSTANZ;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		PPFPlausibilisierungsBeschreibungen neuePlBeschreibungen = 
					new PPFPlausibilisierungsBeschreibungen(verwaltung);
		boolean fehler = false;
		LOGGER.info("Neue Parameter empfangen"); //$NON-NLS-1$
		
		if(resultate != null && resultate.length > 0){
			/**
			 * nur ein Objekt wird hier behandelt, d.h. dass 
			 * nur ein Datensatz (der letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];
			
			if(resultat != null && resultat.isSourceAvailable() &&
					!resultat.isNoDataAvailable() && resultat.hasData() &&
					 resultat.getData() != null){
				
				try {
					final Data.Array parameterSaetze = resultat.getData().
									getArray(PPFKonstanten.ATL_PARA_SATZ);

					for(int i = 0; i<parameterSaetze.getLength(); i++){
						neuePlBeschreibungen.addParameterSatz(
								parameterSaetze.getItem(i));
						LOGGER.fine(parameterSaetze.getItem(i).toString());
					}
				} catch (Exception e) {
					LOGGER.error("Parameterdatensatz f�r die formale" + //$NON-NLS-1$
							" Plausibilisierung konnte nicht" + //$NON-NLS-1$
							" ausgelesen werden", e); //$NON-NLS-1$
					fehler = true;
				}
			}
		}
		
		/**
		 * alle informieren
		 */
		if(!fehler){
			synchronized (this) {
				plBeschreibungen = neuePlBeschreibungen;
				for(IPPFVersorgerListener listener:listenerListe){
					listener.aktualisiereParameter(this);
				}			
			}
		}			
		
		LOGGER.info(this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public SystemObject[] getBetrachteteObjekte() {
		Collection<SystemObject> objekte = new HashSet<SystemObject>();
		
		if(this.plBeschreibungen != null){
			for(DAVObjektAnmeldung anmeldung:
						this.plBeschreibungen.getObjektAnmeldungen()){
				objekte.add(anmeldung.getObjekt());
			}
		}
		
		return objekte.toArray(new SystemObject[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<DAVObjektAnmeldung> getObjektAnmeldungen() {
		return this.plBeschreibungen == null?new ArrayList<DAVObjektAnmeldung>()
				:this.plBeschreibungen.getObjektAnmeldungen();
	}

	/**
	 * {@inheritDoc}
	 */
	public Data plausibilisiere(final ResultData resultat) {
		Data ergebnis = null;
		
		if(plBeschreibungen == null){
			LOGGER.warning("Es wurden noch keine Parameter" + //$NON-NLS-1$
					" f�r die formale Plausibilisierung empfangen"); //$NON-NLS-1$
		}else
		if(resultat != null && resultat.hasData() && resultat.getData() != null){
			Collection<PPFAttributSpezifikation> attSpezifikationen = 
					this.plBeschreibungen.getAttributSpezifikationenFuer(resultat);

			if(attSpezifikationen.size() > 0){
				ergebnis = resultat.getData();
								
				for(PPFAttributSpezifikation attSpez:attSpezifikationen){
					Data dummy = plausibilisiereAttribut(ergebnis, attSpez);
					if(dummy != null){
						ergebnis = dummy;
					}
				}				
			}else{
				LOGGER.info("ResultData " + resultat + //$NON-NLS-1$
				" ist nicht zur formalen Plausibilisierung vorgesehen."); //$NON-NLS-1$
			}
		}else{
			LOGGER.info("Das formal zu pr�fende Datum" + //$NON-NLS-1$
					" enth�lt keine sinnvollen Daten: " +  //$NON-NLS-1$
					(resultat==null?DUAKonstanten.NULL:resultat));
		}
		
		return ergebnis;
	}
	
	/**
	 * F�hrt die formale Plausibilisierung f�r ein bestimmtes Attribut
	 * innerhalb eines Datensatzes durch und gibt f�r dieses Attribut
	 * plausibilisierten Datensatz zur�ck.
	 * 
	 * @param datum der Datensatz
	 * @param attSpez die Spezifikation des Attributs und die Parameter
	 * der formalen Plausibilisierung desselben
	 * @return f�r dieses Attribut plausibilisierten Datensatz (<b>so
	 * dieser ver�ndert werden musste</b>), oder <code>null</code> sonst
	 * (damit nicht unn�tigerweise Kopien von Datens�tzen angelegt werden 
	 * m�ssen)
	 */
	private final Data plausibilisiereAttribut(final Data datum,
									  		   final PPFAttributSpezifikation attSpez){
		Data ergebnis = null;
		
		if(attSpez != null){
			final String attPfad = attSpez.getAttributPfad();
			final double min = attSpez.getMin();
			final double max = attSpez.getMax();
			if(max < min){
				LOGGER.warning("Max-Wert (" + max + ") ist kleiner" +//$NON-NLS-1$ //$NON-NLS-2$
						" als Min-Wert (" + min + ")");   //$NON-NLS-1$//$NON-NLS-2$
			}
			
			final PlausibilisierungsMethode methode = attSpez.getMethode();
			if(attPfad != null){
				try{
					Data dummy = datum.createModifiableCopy();
					Data plausDatum = DUAUtensilien.getAttributDatum(attPfad, dummy);
					if(plausDatum != null && 
						(plausDatum.getAttributeType().isOfType(TYP_GANZ_ZAHL) || 
						 plausDatum.getAttributeType().isOfType(TYP_KOMMA_ZAHL))){
						
						/**
						 * Eine Plausibilisierung wird nur durchgef�hrt, wenn das Attribut sich nicht in einem
						 * vordefinierten Zustand befindet. Sollte ein vordefinierter Zustand vorliegen, so wird
						 * das Datum zwar nicht plausibilisiert, diese Methode gibt jedoch trotzdem einen
						 * Wert zur�ck (damit nachgelagerte Funktionen z.B. dieses Datum publizieren k�nnen)
						 */
						if(!plausDatum.asUnscaledValue().isState()){
							double alterWert = plausDatum.asUnscaledValue().doubleValue();
							double neuerWert = plausDatum.asUnscaledValue().doubleValue();
							boolean implausibelMin = alterWert < min;
							boolean implausibelMax = alterWert > max;

							/**
							 * Plausibilisierung
							 */
							if(methode.equals(PlausibilisierungsMethode.NUR_PRUEFUNG)){
								if(implausibelMin || implausibelMax){
									setStatusPlFormalWert(dummy, true, attPfad, PPFKonstanten.ATT_STATUS_IMPLAUSIBEL);
								}else{
									setStatusPlFormalWert(dummy, false, attPfad, PPFKonstanten.ATT_STATUS_IMPLAUSIBEL);
								}
							}else
							if(methode.equals(PlausibilisierungsMethode.SETZE_MIN_MAX)){
								if(implausibelMax){
									neuerWert = max;
									setStatusPlFormalWert(dummy, false, attPfad,
											PPFKonstanten.ATT_STATUS_MIN);
									setStatusPlFormalWert(dummy, true, attPfad,
											PPFKonstanten.ATT_STATUS_MAX);
								}else if(implausibelMin){
									neuerWert = min;
									setStatusPlFormalWert(dummy, true, attPfad,
											PPFKonstanten.ATT_STATUS_MIN);
									setStatusPlFormalWert(dummy, false, attPfad,
											PPFKonstanten.ATT_STATUS_MAX);
								}
							}else
							if(methode.equals(PlausibilisierungsMethode.SETZE_MIN)){
								if(implausibelMin){
									neuerWert = min;
									setStatusPlFormalWert(dummy, true, attPfad,
											PPFKonstanten.ATT_STATUS_MIN);
									setStatusPlFormalWert(dummy, false, attPfad,
											PPFKonstanten.ATT_STATUS_MAX);
								}							
							}
							else
							if(methode.equals(PlausibilisierungsMethode.SETZE_MAX)){
								if(implausibelMax){
									neuerWert = max;
									setStatusPlFormalWert(dummy, false, attPfad,
											PPFKonstanten.ATT_STATUS_MIN);
									setStatusPlFormalWert(dummy, true, attPfad,
											PPFKonstanten.ATT_STATUS_MAX);
								}
							}

							if(neuerWert != alterWert){
								plausDatum.asUnscaledValue().set((long)neuerWert);	
							}
						}
						
						/**
						 * Ver�nderung des R�ckgabedatums
						 */
						ergebnis = dummy;
					}
				}catch(Exception ex){
					LOGGER.error("Unerwarteter Fehler beim formalen Plausibilisieren von " + datum, ex); //$NON-NLS-1$
				}
			}else{
				LOGGER.warning("F�r Datum " + datum + " ist die Attributspezifikation "//$NON-NLS-1$ //$NON-NLS-2$
						+ attSpez + " unvollst�ndig.");  //$NON-NLS-1$
			}
		}
		
		return ergebnis;
	}

	/**
	 * F�gt diesem Element einen Listener hinzu
	 * 
	 * @param listener der neue Listener
	 */
	public final void addListener(final IPPFVersorgerListener listener){
		if(this.listenerListe.add(listener)){
			synchronized (this) {
				listener.aktualisiereParameter(this);
			}	
		}
	}
	
	/**
	 * Entfernt einen Listener von diesem Element
	 * 
	 * @param listener der zu entfernende Listener
	 */
	public final void removeListener(final IPPFVersorgerListener listener){
		synchronized (this) {
			this.listenerListe.remove(listener);	
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = Konstante.STRING_UNBEKANNT + "\n"; //$NON-NLS-1$
		
		synchronized (this) {
			if(this.plBeschreibungen != null){
				s += "Plausibilisierungsbeschreibung:\n" + //$NON-NLS-1$
					this.plBeschreibungen;
			}						
		}
		
		return s;
	}

	/**
	 * Setzt den Wert eines bestimmten Status-Flags (MIN/MAX)
	 * f�r ein bistimmtes Attribut innerhalb einer bestimmten
	 * Attributgruppe. (So dieser vorhanden ist!)
	 * 
	 * @param datum der Datensatz, in dem sich der Statuswert
	 * befindet 
	 * @param wert der neue Status-Wert
	 * @param attPfad der Attributpfad bis zum dem Wert, der vom
	 * bewussten Status-Flag flankiert wird.
	 * @param attPfadErsetzung der Attributpfad bis zum Status-
	 * Flag (vom Wert aus gesehen)
	 */
	private final void setStatusPlFormalWert(final Data datum,
 											 final boolean wert,
											 final String attPfad,
											 final String attPfadErsetzung){
		if(datum != null && attPfad != null && attPfadErsetzung != null){
			final String neuerAttPfad = DUAUtensilien.
				ersetzeLetztesElemInAttPfad(attPfad, attPfadErsetzung);
			
			if(neuerAttPfad != null){
				Data status = DUAUtensilien.getAttributDatum(neuerAttPfad, datum);
				if(status != null){
					try{
						status.asUnscaledValue().set(wert?1:0);
					}catch(Exception ex){
						LOGGER.error("Unerwarteter Fehler beim " + //$NON-NLS-1$
								"Setzen des Statusflags", ex); //$NON-NLS-1$
					}
				}else{
					LOGGER.warning("Statuswert konnte nicht" + //$NON-NLS-1$
							" ausgelesen werden:\n" + //$NON-NLS-1$
							"Datum: " + datum + "\nAttr.-Pfad: " + attPfad + //$NON-NLS-1$ //$NON-NLS-2$
							"\nErsetzung: " + attPfadErsetzung); //$NON-NLS-1$					
				}
			}else{
				LOGGER.warning("Attributpfad zum Statuswert konnte nicht" + //$NON-NLS-1$
						" erstellt werden:\n" + //$NON-NLS-1$
						"Datum: " + datum + "\nAttr.-Pfad: " + attPfad + //$NON-NLS-1$ //$NON-NLS-2$
						"\nErsetzung: " + attPfadErsetzung); //$NON-NLS-1$
			}
		}
	}
}

