package de.bsvrz.dua.plformal.allgemein.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * �ber diese Schnittstelle sollen die Standardaspekte f�r
 * die Publikation von bestimmten SWE-Modultyp-Kombinationen
 * zur Verf�gung gestellt werden. 
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation f�r ein
	 * bestimmtes empfangenes Originaldatum.
	 * 
	 * @param originalDatum das Originaldatum
	 * @return der Standardpublikationsaspekt oder
	 * <code>null</code>, wenn kein Aspekt ermittelt
	 * werden konnte
	 */
	public Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die f�r die Publikation
	 * unter den Standardaspekten durchgef�hrt werden m�ssen. 
	 * 
	 * @return die Datenanmeldungen, die f�r die Publikation unter
	 * den Standardaspekten durchgef�hrt werden m�ssen (ggf. leere
	 * Menge)
	 */
	public Collection<DAVObjektAnmeldung> getStandardAnmeldungen();

}
