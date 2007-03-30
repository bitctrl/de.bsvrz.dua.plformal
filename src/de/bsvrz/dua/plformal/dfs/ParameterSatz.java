package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.List;

import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;

/**
 * Diese Klasse enthält alle Parameter, die innerhalb eines Datensatzes
 * <code>ParameterSatz</code> der Attributgruppe
 * <code>atg.datenflussSteuerung</code> vorkommen. Pro SWE wird nur ein
 * Parametersatz vorgehalten. Sollten also innerhalb dieser Attributgruppe
 * mehrere Parametersätze für die gleiche SWE vorkommen, so werden diese
 * gemischt.
 * 
 * @author Thierfelder
 * 
 */
public class ParameterSatz {

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die SWE, deren Publikationsparameter in dieser Klasse stehen
	 */
	private String swe = DAVKonstanten.STR_UNDEFINIERT;

	/**
	 * alle Publikationszuordnungen dieses Parametersatzes
	 */
	private List<PublikationsZuordung> pubZuordnungen = new ArrayList<PublikationsZuordung>();

	
	/**
	 * Erfragt die SWE, für die Publikationsparameter in dieser Klasse stehen.
	 * 
	 * @return die ID der SWE
	 */
	public final String getSwe() {
		return swe;
	}

	/**
	 * Setzt die SWE, für die Publikationsparameter
	 * in dieser Klasse stehen.
	 * 
	 * @param swe
	 *            ide ID der SWE
	 */
	public final void setSwe(final String swe) {
		this.swe = swe;
	}

	/**
	 * Erfragt eine Liste mit allen Publikationszuordnungen dieses
	 * Parametersatzes
	 * 
	 * @return alle Publikationszuordnungen dieses Parametersatzes
	 */
	public final List<PublikationsZuordung> getPubZuordnung() {
		return pubZuordnungen;
	}

	/**
	 * Fügt der Liste aller Publikationszuordnungen eine hinzu
	 * 
	 * @param pubZuordnung
	 *            neue Publikationszuordnung
	 */
	public final void add(final PublikationsZuordung pubZuordnung) {
		boolean addErlaubt = true;

		for (PublikationsZuordung altePz : this.pubZuordnungen) {
			String fehler = altePz.isKompatibelMit(pubZuordnung);
			if (fehler != null) {
				LOGGER.warning(fehler);
				addErlaubt = false;
			}
		}

		if (addErlaubt)
			this.pubZuordnungen.add(pubZuordnung);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "SWE: " + swe + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

		for (PublikationsZuordung pz : pubZuordnungen) {
			s += pz + "\n"; //$NON-NLS-1$
		}

		return s;
	}

}
