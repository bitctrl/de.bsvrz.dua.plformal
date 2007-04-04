package de.bsvrz.dua.plformal.dfs.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Dieses Interface stellt alle Informationen über die aktuelle
 * Datenflusssteuerung <b>für eine bestimmte SWE und einen bestimmten Modul-Typ</b>
 * zur Verfügung. Im Wesentlichen stellt es den Zugriff auf ein Objekt des Typs
 * <code>DatenFlussSteuerung</code> sicher.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * @version 1.0
 */
public interface IDatenFlussSteuerungFuerModul {

	/**
	 * Erfragt die Menge aller Datenanmeldungen die in Bezug auf die übergebenen
	 * Objekte durchgeführt werden müssen, um diese nach der Plausibilisierung
	 * publizieren zu können.
	 * 
	 * @param filterObjekte
	 *            Liste mit Objekten bzw. Typen. Diese Liste gilt als Filter,
	 *            durch den alle innerhalb dieser Publikationszuordnung
	 *            definierten Datenanmeldungen geschickt werden, bevor diese
	 *            Methode ein Ergebnis zurückgibt. <code>null</code> = kein
	 *            Filter
	 * @return eine ggf. leere Menge mit Datenanmeldungen
	 */
	public Collection<DAVObjektAnmeldung> getDatenAnmeldungen(
			final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen);

	/**
	 * Erfragt eine publikationsfähige Modifikation des übergebenen
	 * Original-Datums. Es wird ein Datum zurückgegeben, das nach der
	 * Plausibilisierung so publiziert werden muss.
	 * 
	 * @param originalDatum
	 *            das Originaldatum, wie es vom plausibilisierenden Modul
	 *            empfangen wurde
	 * @param plausibilisiertesDatum
	 *            dessen <code>Data</code>-Objekt nach der Plausibilisierung
	 * @param standardAspekt
	 *            der Standardaspekt der Publikation für dieses Datum oder
	 *            <code>null</code>, wenn es keinen Standardaspekt gibt
	 * @return ein <code>ResultData</code>-Objekt, das so publiziert werden
	 *         kann oder <code>null</code>, wenn keine Publikation notwendig
	 *         ist (dies ist z.B. auch der Fall, wenn innerhalb der
	 *         Datenflusssteuerung der übergebene Standardaspekt explizit von
	 *         der Publikation ausgeschlossen wurde)
	 */
	public ResultData getPublikationsDatum(final ResultData originalDatum,
			final Data plausibilisiertesDatum, final Aspect standardAspekt);

}
