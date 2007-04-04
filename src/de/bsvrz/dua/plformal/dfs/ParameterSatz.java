package de.bsvrz.dua.plformal.dfs;

import java.util.ArrayList;
import java.util.List;

import sys.funclib.debug.Debug;

/**
 * Diese Klasse enthält alle Parameter, die innerhalb eines Datensatzes
 * <code>ParameterSatz</code> der Attributgruppe
 * <code>atg.datenflussSteuerung</code> vorkommen. Pro SWE wird nur ein
 * Parametersatz vorgehalten. Sollten also innerhalb dieser Attributgruppe
 * mehrere Parametersätze für die gleiche SWE vorkommen, so werden diese
 * (später) gemischt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
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
	private SWETyp swe = null;

	/**
	 * alle Publikationszuordnungen dieses Parametersatzes
	 */
	private List<PublikationsZuordung> pubZuordnungen =
					new ArrayList<PublikationsZuordung>();

	
	/**
	 * Erfragt die SWE, für die Publikationsparameter in dieser Klasse stehen.
	 * 
	 * @return die ID der SWE
	 */
	public final SWETyp getSwe() {
		return swe;
	}

	/**
	 * Setzt die SWE, für die Publikationsparameter
	 * in dieser Klasse stehen.
	 * 
	 * @param swe
	 *            Die SWE
	 */
	public final void setSwe(final SWETyp swe) {
		this.swe = swe;
	}

	/**
	 * Erfragt eine Liste mit allen Publikationszuordnungen dieses
	 * Parametersatzes
	 * 
	 * @return alle Publikationszuordnungen dieses Parametersatzes
	 * (oder eine leere Liste)
	 */
	public final List<PublikationsZuordung> getPubZuordnung() {
		return pubZuordnungen;
	}

	/**
	 * Fügt der Liste aller Publikationszuordnungen eine
	 * neue Publikationszuordnung hinzu. Bevor dies geschieht,
	 * werden alle schon vorhandenen Publikationszuordnungen
	 * auf Konsistenz mit der neuen Publikationszuordnung getestet.
	 * Fällt dieser Test negativ aus, so wird die neue
	 * Publikationszuordnung ignoriert und eine den Fehler
	 * dokumentierende Warung ausgegeben.  
	 * 
	 * @param pubZuordnung
	 *            neue Publikationszuordnung
	 */
	public final void add(final PublikationsZuordung pubZuordnung) {
		boolean addErlaubt = true;

		for (PublikationsZuordung altePz:this.pubZuordnungen) {
			String fehler = altePz.isKompatibelMit(pubZuordnung);
			if (fehler != null) {
				LOGGER.warning(fehler);
				addErlaubt = false;
				break;
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
