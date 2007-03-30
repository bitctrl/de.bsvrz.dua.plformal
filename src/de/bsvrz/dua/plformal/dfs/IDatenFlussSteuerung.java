package de.bsvrz.dua.plformal.dfs;

import java.util.Collection;

import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * Dieses Interface stellt alle Informationen �ber die aktuelle
 * Datenflusssteuerung zur Verf�gung. Im Wesentlichen stellt es den Zugriff auf
 * ein Objekt des Typs <code>DatenFlussSteuerung</code> sicher.
 * 
 * @author Thierfelder
 * 
 */
public interface IDatenFlussSteuerung {

	/**
	 * Erfragt die Menge aller Datenanmeldungen die in Bezug auf die �bergebenen
	 * Objekte durchgef�hrt werden m�ssen, um diese nach der Plausibilisierung
	 * in der �bergebenen SWE und im �bergebenen Modul-Typ publizieren zu
	 * k�nnen.
	 * 
	 * @param swe
	 *            DAV-ID der SWE
	 * @param modulTyp
	 *            DAV-ID des gesuchten Modul-Typs
	 * @param filterObjekte
	 *            Liste mit Objekten bzw. Typen. Diese Liste gilt als Filter,
	 *            durch den alle innerhalb dieser Publikationszuordnung
	 *            definierten Datenanmeldungen geschickt werden, bevor diese
	 *            Methode ein Ergebnis zur�ckgibt. <code>null</code> = kein
	 *            Filter
	 * @return eine Menge von Datenanmeldungen oder <code>null</code> wenn
	 *         keine Anmeldungen durchgef�hrt werden m�ssen.
	 */
	public Collection<DAVDatenAnmeldung> getDatenAnmeldungen(final String swe,
			final String modulTyp, final SystemObject[] filterObjekte);

	/**
	 * Erfragt eine Schnittstelle zu allen Informationen der Datenflusssteuerung
	 * der �bergebenen SWE und des �bergebenen Modul-Typs
	 * 
	 * @param swe
	 *            die DAV-ID der SWE
	 * @param modulTyp
	 *            die DAV-ID des Modul-Typs
	 * @return eine Schnittstelle zu allen Informationen der Datenflusssteuerung
	 *         der �bergebenen SWE und des �bergebenen Modul-Typs
	 */
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(final String swe,
			final String modulTyp);

}
