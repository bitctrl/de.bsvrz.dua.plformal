package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;

/**
 * Diese Klasse stellt über die Schnittstelle <code>IDatenFlussSteuerungFuerModul</code>
 * alle Informationen über die Datenflusssteuerung einer bestimmten SWE in Zusammenhang
 * mit einem bestimmten Modul-Typ zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class DatenFlussSteuerungFuerModul implements
IDatenFlussSteuerungFuerModul {

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Standarddatenflusssteuerung
	 */
	public static final IDatenFlussSteuerungFuerModul STANDARD =
						new DatenFlussSteuerungFuerModul();

	/**
	 * Liste aller Publikationszuordnungen innerhalb der Attributgruppe
	 */
	private Collection<PublikationsZuordung> publikationsZuordnungen = 
			new ArrayList <PublikationsZuordung>();
	

	/**
	 * Fügt diesem Objekt eine Publikationszuordung hinzu
	 * 
	 * @param pz
	 *            die neue Publikationszuordung
	 */
	public final void add(final PublikationsZuordung pz) {
		//for(pz.get)
		publikationsZuordnungen.add(pz);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<DAVObjektAnmeldung> getDatenAnmeldungen(
			final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen) {
		Collection<DAVObjektAnmeldung> anmeldungen = new ArrayList<DAVObjektAnmeldung>();
		Collection<DAVObjektAnmeldung> anmeldungenStd = new TreeSet<DAVObjektAnmeldung>();
		anmeldungenStd.addAll(standardAnmeldungen);
		
		if (publikationsZuordnungen != null) {
			for (PublikationsZuordung pz : publikationsZuordnungen) {

				if (pz.isPublizieren()) {					
					Collection<SystemObject> anzumeldendeObjekte = new HashSet<SystemObject>();

					if (filterObjekte != null && filterObjekte.length > 0) {
						for (SystemObject obj : pz.getObjekte()) {
							boolean match = false;
							for (SystemObject filterObj : filterObjekte) {
								if (DUAHilfe.hasSchnittMenge(obj, filterObj) != null) {
									match = true;
									break;
								}
							}
							if (match){
								anzumeldendeObjekte.add(obj);								
							}
						}
					} else {
						anzumeldendeObjekte.addAll(pz.getObjekte());
					}

					if (anzumeldendeObjekte.size() > 0) {
						for (AttributeGroup atg : pz.getAtgs()) {
							DataDescription dd = new DataDescription(atg, pz
									.getAspekt(), (short) 0);
							DAVDatenAnmeldung anmeldung;
							try {
								anmeldung = new DAVDatenAnmeldung(
										anzumeldendeObjekte
												.toArray(new SystemObject[0]), dd,
						DatenFlussSteuerungsHilfe.INSTANZ.getVerwaltung().getVerbindung());
								
								anmeldungen.addAll(anmeldung.getObjektAnmeldungen());
							} catch (Exception e) {
								LOGGER.error(DUAKonstanten.EMPTY_STR, e);
							}
						}
					}
				}else{
					Collection<SystemObject> anzumeldendeObjekte = new HashSet<SystemObject>();

					if (filterObjekte != null && filterObjekte.length > 0) {
						for (SystemObject obj : pz.getObjekte()) {
							boolean match = false;
							for (SystemObject filterObj : filterObjekte) {
								if (DUAHilfe.hasSchnittMenge(obj, filterObj) != null) {
									match = true;
									break;
								}
							}
							if (match){
								anzumeldendeObjekte.add(obj);								
							}
						}
					} else {
						anzumeldendeObjekte.addAll(pz.getObjekte());
					}

					if (anzumeldendeObjekte.size() > 0) {
						for (AttributeGroup atg : pz.getAtgs()) {
							DataDescription dd = new DataDescription(atg, pz
									.getAspekt(), (short) 0);
							DAVDatenAnmeldung anmeldung;
							try {
								anmeldung = new DAVDatenAnmeldung(
										anzumeldendeObjekte
												.toArray(new SystemObject[0]), dd,
					DatenFlussSteuerungsHilfe.INSTANZ.getVerwaltung().getVerbindung());
								
								anmeldungenStd.removeAll(anmeldung.getObjektAnmeldungen());
							} catch (Exception e) {
								LOGGER.error(DUAKonstanten.EMPTY_STR, e);
							}
						}
					}
				}
			}
		}
		anmeldungen.addAll(anmeldungenStd);

		return anmeldungen;
	}

	/**
	 * {@inheritDoc}
	 */
	public final ResultData getPublikationsDatum(
							final ResultData originalDatum,
							final Data plausibilisiertesDatum,
							final Aspect standardAspekt) {
		ResultData ergebnis = null;
		boolean publizierenUnterStandardAspekt = standardAspekt != null;

		if (publikationsZuordnungen != null) {
			for (PublikationsZuordung pz : publikationsZuordnungen) {
				/**
				 * Zunächst muss heraus gefunden werden, ob die im
				 * Original-Datum übergebene (Objekt-Atg)-Kombination innerhalb
				 * dieser Publikationszuordnung überhaupt erfasst ist.
				 */
				boolean erfasst = false;
				for (AttributeGroup atg : pz.getAtgs()) {
					if (originalDatum.getDataDescription().getAttributeGroup()
							.equals(atg)) {
						erfasst = true;
						break;
					}
				}
				if (erfasst) {
					erfasst = false;
					for (SystemObject obj : pz.getObjekte()) {
						if (DUAHilfe.hasSchnittMenge(obj, originalDatum
								.getObject()) != null) {
							erfasst = true;
							break;
						}
					}
				}

				/**
				 * Kombination ist erfasst
				 */
				if (erfasst) {
					if (pz.isPublizieren()) {
						DataDescription dd = new DataDescription(originalDatum
								.getDataDescription().getAttributeGroup(), pz
								.getAspekt(), (short) 0);
						ergebnis = new ResultData(originalDatum.getObject(),
								dd, System.currentTimeMillis(),
								plausibilisiertesDatum);
						publizierenUnterStandardAspekt = false;
						break;
					} else if (pz.getAspekt().equals(standardAspekt)) {
						publizierenUnterStandardAspekt = false;
					}
				}
			}
		}

		if (publizierenUnterStandardAspekt) {
			DataDescription dd = new DataDescription(originalDatum
					.getDataDescription().getAttributeGroup(), standardAspekt,
					(short) 0);
			ergebnis = new ResultData(originalDatum.getObject(), dd, System
					.currentTimeMillis(), plausibilisiertesDatum);
		}

		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "\nDatenflusssteuerung für Modul:\n"; //$NON-NLS-1$

		int i = 0;
		for (PublikationsZuordung pz : publikationsZuordnungen) {
			s += "Publikationszuordnung: " + (i++) + "\n" + pz; //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
	
	protected class PublikationsZustand{
		
		public Aspect publikationsAspekt = null;
		
		public boolean publizieren = false;
		
		public PublikationsZustand(final boolean publizieren,
								   final Aspect publikationsAspekt){
			this.publizieren = publizieren;
			this.publikationsAspekt = publikationsAspekt;
		}
		
	}
}
