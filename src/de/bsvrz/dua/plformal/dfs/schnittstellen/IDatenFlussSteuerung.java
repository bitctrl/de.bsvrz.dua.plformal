package de.bsvrz.dua.plformal.dfs.schnittstellen;

import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;


/**
 * Dieses Interface stellt alle Informationen über die
 * aktuelle Datenflusssteuerung zur Verfügung. Im
 * Wesentlichen stellt es den Zugriff auf ein Objekt
 * des Typs <code>typ.datenflussSteuerung</code> sicher.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IDatenFlussSteuerung {

	/**
	 * Erfragt eine Schnittstelle zu allen Informationen
	 * der Datenflusssteuerung der übergebenen SWE und des
	 * übergebenen Modul-Typs
	 * 
	 * @param swe
	 *            die SWE
	 * @param modulTyp
	 *            der Modul-Typ
	 * @return eine Schnittstelle zu allen Informationen der
	 * Datenflusssteuerung der übergebenen SWE und des übergebenen
	 * Modul-Typs. Wenn keine Informationen zur SWE/Modul-Typ-
	 * Kombination vorhanden sind, wird ein leeres Objekt 
	 * zurückgegeben.
	 */
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(final SWETyp swe,
			final ModulTyp modulTyp);

}
