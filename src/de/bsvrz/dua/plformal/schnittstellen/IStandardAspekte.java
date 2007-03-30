package de.bsvrz.dua.plformal.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * �ber diese Schnittstelle sollen die Standardaspekte der Publikation von
 * bestimmten SWE-Modultyp-Kombinationen zur Verf�gung gestellt werden. 
 * 
 * @author Thierfelder
 *
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation f�r ein
	 * bestimmtes empfangenes Originaldatum
	 * 
	 * @param originalDatum das Originaldatum
	 * @return der Standardpublikationsaspekt
	 */
	public Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die f�r die Publikation unter
	 * den Standardaspekten durchgef�hrt werden m�ssen. 
	 * 
	 * @return die Datenanmeldungen, die f�r die Publikation unter
	 * den Standardaspekten durchgef�hrt werden m�ssen
	 */
	public Collection<DAVDatenAnmeldung> getStandardAnmeldungen();

}
