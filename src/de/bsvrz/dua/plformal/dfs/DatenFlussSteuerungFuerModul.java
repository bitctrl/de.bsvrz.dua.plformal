package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;
import java.util.HashSet;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.allgemein.DAVHilfe;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

public class DatenFlussSteuerungFuerModul implements
		IDatenFlussSteuerungFuerModul {

	/**
	 * Standarddatenflusssteuerung
	 */
	public static final IDatenFlussSteuerungFuerModul STANDARD = new DatenFlussSteuerungFuerModul();

	/**
	 * Liste aller Publikationszuordnungen innerhalb der Attributgruppe
	 */
	private Collection<PublikationsZuordung> publikationsZuordnungen = new HashSet<PublikationsZuordung>();

	/**
	 * Fügt diesem Objekt eine Publikationszuordung hinzu
	 * 
	 * @param ps
	 *            die neue Publikationszuordung
	 */
	public final void add(final PublikationsZuordung ps) {
		publikationsZuordnungen.add(ps);
	}

	/**
	 * Entfernt eine Publikationszuordung aus diesem Objekt
	 * 
	 * @param ps
	 *            die zu entfernende Publikationszuordung
	 */
	public final void remove(final PublikationsZuordung ps) {
		publikationsZuordnungen.remove(ps);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<DAVDatenAnmeldung> getDatenAnmeldungen(
			final SystemObject[] filterObjekte) {
		Collection<DAVDatenAnmeldung> anmeldungen = new HashSet<DAVDatenAnmeldung>();

		if (publikationsZuordnungen != null) {
			for (PublikationsZuordung pz : publikationsZuordnungen) {

				if (pz.isPublizieren()) {
					Collection<SystemObject> anzumeldendeObjekte = new HashSet<SystemObject>();

					if (filterObjekte != null && filterObjekte.length > 0) {
						for (SystemObject obj : pz.getObjekte()) {
							boolean match = false;
							for (SystemObject filterObj : filterObjekte) {
								if (DAVHilfe.hasSchnittMenge(obj, filterObj)) {
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
							DataDescription dd = new DataDescription(atg, pz
									.getAspekt(), (short) 0);
							DAVDatenAnmeldung anmeldung;
							try {
								anmeldung = new DAVDatenAnmeldung(
										anzumeldendeObjekte
												.toArray(new SystemObject[0]), dd);
								anmeldungen.add(anmeldung);
							} catch (Exception e) {
								// TODO Automatisch erstellter Catch-Block
								e.printStackTrace();
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
	public final ResultData getPublikationsDatum(
			final ResultData originalDatum, final Data plausibilisiertesDatum,
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
				boolean atgErfasst = false;
				for (AttributeGroup atg : pz.getAtgs()) {
					if (originalDatum.getDataDescription().getAttributeGroup()
							.equals(atg)) {
						atgErfasst = true;
						break;
					}
				}
				boolean objErfasst = false;
				if (atgErfasst) {
					for (SystemObject obj : pz.getObjekte()) {
						if (DAVHilfe.hasSchnittMenge(obj, originalDatum
								.getObject())) {
							objErfasst = true;
							break;
						}
					}
				}

				/**
				 * Kombination ist erfasst
				 */
				if (atgErfasst && objErfasst) {
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
}
