package de.bsvrz.dua.plformal.dfs.schnittstellen;

import de.bsvrz.dua.plformal.dfs.typen.ModulTyp;
import de.bsvrz.dua.plformal.dfs.typen.SWETyp;


/**
 * Dieses Interface stellt alle Informationen �ber die
 * aktuelle Datenflusssteuerung zur Verf�gung. Im
 * Wesentlichen stellt es den Zugriff auf ein Objekt
 * des Typs <code>typ.datenflussSteuerung</code> sicher.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IDatenFlussSteuerung {

	/**
	 * Erfragt eine Schnittstelle zu allen Informationen
	 * der Datenflusssteuerung der �bergebenen SWE und des
	 * �bergebenen Modul-Typs
	 * 
	 * @param swe
	 *            die SWE
	 * @param modulTyp
	 *            der Modul-Typ
	 * @return eine Schnittstelle zu allen Informationen der
	 * Datenflusssteuerung der �bergebenen SWE und des �bergebenen
	 * Modul-Typs. Wenn keine Informationen zur SWE/Modul-Typ-
	 * Kombination vorhanden sind, wird ein leeres Objekt 
	 * zur�ckgegeben.
	 */
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(final SWETyp swe,
			final ModulTyp modulTyp);

}
