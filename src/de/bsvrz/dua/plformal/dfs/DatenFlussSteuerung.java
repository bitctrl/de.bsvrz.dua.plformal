package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DAVHilfe;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * Diese Klasse repräsentiert die Attributgruppe
 * <code>atg.datenFlussSteuerung</code> des Typs
 * <code>typ.datenFlussSteuerung</code>.
 * 
 * @author Thierfelder
 * 
 */
public class DatenFlussSteuerung
implements IDatenFlussSteuerung {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Liste aller Parametersätze innerhalb der Attributgruppe
	 */
	private List<ParameterSatz> parameterSaetze = new ArrayList<ParameterSatz>();

	/**
	 * Fügt diesem Objekt einen Parametersatz hinzu
	 * 
	 * @param ps
	 *            der neue Parametersatz
	 */
	public final void add(final ParameterSatz ps) {
		parameterSaetze.add(ps);
	}

	/**
	 * Entfernt einen Parametersatz aus diesem Objekt
	 * 
	 * @param ps
	 *            der zu entfernende Parametersatz
	 */
	public final void remove(final ParameterSatz ps) {
		parameterSaetze.remove(ps);
	}

	/**
	 * Erfragt den Parametersatz für eine bestimmte SWE <br>
	 * <b>Achtung: Es wird innerhalb dieser Klasse immer nur ein
	 * ParameterSatz-Objekt pro SWE instanziiert werden, auch wenn mehrere
	 * parametriert sind (die Informationen werden zusammengefasst). Sollten
	 * widersprüchliche Informationen innerhalb der Parametersätze enthalten
	 * sein, so werden alle Parametersätze, die diesen Widerspruch enthalten
	 * ignoriert.</b>
	 * 
	 * 
	 * @param swe
	 *            die DAV-ID der SWE
	 * @return der Parametersatz der Datenflusssteuerung für die übergebene SWE
	 */
	public final ParameterSatz getParameterSatzFuerSWE(final String swe) {
		ParameterSatz ps = null;

		for (ParameterSatz psDummy : parameterSaetze) {
			if (psDummy.getSwe().equals(swe)) {
				ps = psDummy;
				break;
			}
		}

		return ps;
	}

	/**
	 * Erfragt die Publikationszuordnungen für ein bestimmtes Modul und eine
	 * bestimmte SWE
	 * 
	 * @param swe
	 *            die DAV-ID der SWE
	 * @param modulId
	 *            die DAV-ID des Moduls, für die die PublikationsZuordnung
	 *            erfragt werden soll
	 * @return die Publikationszuordnung für die SWE <code>swe</code> und das
	 *         Modul <code>modulId</code>
	 */
	private final Collection<PublikationsZuordung> getPublikationsZuordnungenFuerModul(
			final String swe, final String modulId) {
		ParameterSatz ps = getParameterSatzFuerSWE(swe);
		Collection<PublikationsZuordung> ergebnis = new HashSet<PublikationsZuordung>();

		if (ps != null) {
			for (PublikationsZuordung pzFuerModul : ps.getPubZuordnung()) {
				if (pzFuerModul.getModulTyp().equals(modulId)) {
					ergebnis.add(pzFuerModul);
				}
			}
		}

		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<DAVDatenAnmeldung> getDatenAnmeldungen(final String swe,
			final String modulTyp, final SystemObject[] filterObjekte) {
		Collection<DAVDatenAnmeldung> anmeldungen = new HashSet<DAVDatenAnmeldung>();
		Collection<PublikationsZuordung> publikationsZuordnungen = this
				.getPublikationsZuordnungenFuerModul(swe, modulTyp);

		if (publikationsZuordnungen != null) {
			for (PublikationsZuordung pz : publikationsZuordnungen) {
				if (pz.getModulTyp().equals(modulTyp)) {

					if (pz.isPublizieren()) {
						Collection<SystemObject> anzumeldendeObjekte = new HashSet<SystemObject>();

						if (filterObjekte != null && filterObjekte.length > 0) {
							for (SystemObject obj : pz.getObjekte()) {
								boolean match = false;
								for (SystemObject filterObj : filterObjekte) {
									if (DAVHilfe
											.hasSchnittMenge(obj, filterObj)) {
										match = true;
										break;
									}
								}
								if (match)
									anzumeldendeObjekte.add(obj);
							}
						} else {
							anzumeldendeObjekte.addAll(pz.getObjekte());
						}

						if (anzumeldendeObjekte.size() > 0) {
							for (AttributeGroup atg : pz.getAtgs()) {
								DataDescription dd = new DataDescription(atg,
										pz.getAspekt(), (short) 0);
								DAVDatenAnmeldung anmeldung;
								try {
									anmeldung = new DAVDatenAnmeldung(
											anzumeldendeObjekte
													.toArray(new SystemObject[0]),
											dd);
									anmeldungen.add(anmeldung);
								} catch (Exception e) {
									LOGGER.error("Es konnten keine Daten" + //$NON-NLS-1$
											" angemeldet werden", e); //$NON-NLS-1$
								}
							}
						}
					}
				}
			}
		}

		return anmeldungen.size() > 0 ? anmeldungen : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(String swe,
			String modulTyp) {
		DatenFlussSteuerungFuerModul dfsModul = new DatenFlussSteuerungFuerModul();
		for (PublikationsZuordung pz : this
				.getPublikationsZuordnungenFuerModul(swe, modulTyp)) {
			dfsModul.add(pz);
		}
		return dfsModul;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "\nDatenflusssteuerung:\n"; //$NON-NLS-1$

		for (int i = 0; i < parameterSaetze.size(); i++) {
			s += "ParamaterSatz: " + i + "\n" + parameterSaetze.get(i); //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
}
