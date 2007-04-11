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

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dua.plformal.dfs.typen.SWETyp;

import sys.funclib.debug.Debug;

/**
 * Diese Klasse enthält alle Parameter, die innerhalb
 * eines Datensatzes <code>ParameterSatz</code> der
 * Attributgruppe <code>atg.datenflussSteuerung</code>
 * vorkommen. Pro SWE wird nur ein Parametersatz vorgehalten.
 * Sollten also innerhalb dieser Attributgruppe mehrere
 * Parametersätze für die gleiche SWE vorkommen, so werden
 * diese (später) gemischt.
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
	 * Erfragt die SWE, für die Publikationsparameter
	 * in dieser Klasse stehen.
	 * 
	 * @return die SWE
	 */
	public final SWETyp getSwe() {
		return swe;
	}

	/**
	 * Setzt die SWE, für die Publikationsparameter
	 * in dieser Klasse stehen.
	 * 
	 * @param swe
	 *            die SWE
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
	 * dokumentierende Warnung ausgegeben.  
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
