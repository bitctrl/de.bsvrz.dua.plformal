package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.dua.plformal.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;

/**
 * Diese Klasse repr�sentiert die Attributgruppe
 * <code>atg.datenFlussSteuerung</code> des Typs
 * <code>typ.datenFlussSteuerung</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DatenFlussSteuerung
implements IDatenFlussSteuerung {
	
	/**
	 * Liste aller Parameters�tze innerhalb der Attributgruppe
	 */
	private List<ParameterSatz> parameterSaetze =
								new ArrayList<ParameterSatz>();
	
	
	/**
	 * F�gt diesem Objekt einen Parametersatz hinzu
	 * 
	 * @param ps
	 *            der neue Parametersatz
	 */
	protected final void add(final ParameterSatz ps) {
		parameterSaetze.add(ps);
	}

	/**
	 * Erfragt den Parametersatz f�r eine bestimmte SWE<br>
	 * <b>Achtung: Es wird innerhalb dieser Klasse immer nur ein
	 * ParameterSatz-Objekt pro SWE instanziiert werden, auch
	 * wenn mehrere parametriert sind (die Informationen werden
	 * zusammengefasst). Sollten widerspr�chliche Informationen
	 * innerhalb der Parameters�tze enthalten sein, so werden
	 * alle Parameters�tze, die diesen Widerspruch enthalten
	 * ignoriert.</b>
	 * 
	 * @param swe
	 *            die SWE
	 * @return der Parametersatz der Datenflusssteuerung f�r
	 * die �bergebene SWE
	 */
	protected final ParameterSatz getParameterSatzFuerSWE(final SWETyp swe) {
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
	 * Erfragt die Publikationszuordnungen f�r ein bestimmtes Modul und eine
	 * bestimmte SWE
	 * 
	 * @param swe
	 *            die SWE
	 * @param modulId
	 *            das Modul, f�r die die PublikationsZuordnung
	 *            erfragt werden soll
	 * @return die Publikationszuordnung f�r die SWE <code>swe</code>
	 * 		   und das Modul <code>modulId</code> (ggf. leere Menge)
	 */
	private final Collection<PublikationsZuordung> getPublikationsZuordnungenFuerModul(
			final SWETyp swe, final ModulTyp modulId) {
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
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(SWETyp swe,
			ModulTyp modulTyp) {
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
