package de.bsvrz.dua.plformal.plformal;

import java.util.List;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * Dieses Interface stellt alle Informationen der Attributgruppe
 * <code>atg.plausibilit�tsPr�fungFormal</code> eines Objekts vom
 * Typ <code>typ.plausibilit�tsPr�fungFormal</code> zur Verf�gung.
 * 
 * @author Thierfelder
 *
 */
public interface IPPFHilfe {

	/**
	 * Erfragt, ob f�r eine bestimmtes Datum Parameter zur formalen
	 * Plausibilisierung vorliegen.
	 * 
	 * @param resultat das Datum
	 * @return kann das Datum formal plausibilisiert werden?
	 */
	public boolean isZurPausibilisierungVorgesehen(final ResultData resultat);
	
	/**
	 * F�hrt die formale Plausibilisierung f�r ein bestimmtes Datum durch,
	 * welches innerhalb des �bergebenen <code>ResultData</code>-Objektes
	 * enthalten ist. 
	 * 
	 * @param resultat das Datum
	 * @return wenn die formale Plausibilisierung dieses Datums  in irgend
	 * einer Weise ver�ndert haben sollte, so wird dieses ver�nderte
	 * Datum zur�ckgegeben. Sonst <code>null</code>.
	 */
	public Data plausibilisiere(final ResultData resultat);

	/**
	 * Erfragt alle Datenbeschreibungen, die innerhalb der Attributgruppe
	 * <code>atg.plausibilit�tsPr�fungFormal</code> definiert sind.
	 * 
	 * @return alle Datenbeschreibungen, die innerhalb der Attributgruppe
	 * <code>atg.plausibilit�tsPr�fungFormal</code> definiert sind.
	 */
	public List<DAVDatenAnmeldung> getDatenAnmeldungen();
	
	/**
	 * Erfragt alle zur formalen Plausibilisierung vorgesehenen
	 * (finalen) Objekte. Diese Objekte werden von den Parametern der
	 * Attributgruppe <code>atg.plausibilit�tsPr�fungFormal</code>
	 * bereitgestellt.
	 * 
	 * @return alle zur formalen Plausibilisierung vorgesehenen
	 * (finalen) Objekte in einem Array oder ein leerer Array.
	 */
	public SystemObject[] getBetrachteteObjekte();
	
}
