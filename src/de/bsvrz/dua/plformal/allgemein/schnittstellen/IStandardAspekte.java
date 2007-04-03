package de.bsvrz.dua.plformal.allgemein.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Über diese Schnittstelle sollen die Standardaspekte für
 * die Publikation von bestimmten SWE-Modultyp-Kombinationen
 * zur Verfügung gestellt werden. 
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation für ein
	 * bestimmtes empfangenes Originaldatum.
	 * 
	 * @param originalDatum das Originaldatum
	 * @return der Standardpublikationsaspekt oder
	 * <code>null</code>, wenn kein Aspekt ermittelt
	 * werden konnte
	 */
	public Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die für die Publikation
	 * unter den Standardaspekten durchgeführt werden müssen. 
	 * 
	 * @return die Datenanmeldungen, die für die Publikation unter
	 * den Standardaspekten durchgeführt werden müssen (ggf. leere
	 * Menge)
	 */
	public Collection<DAVObjektAnmeldung> getStandardAnmeldungen();

}
