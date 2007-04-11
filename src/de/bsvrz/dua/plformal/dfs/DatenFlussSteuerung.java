/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x 
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
