package de.bsvrz.dua.plformal.schnittstellen;

import java.util.Collection;

import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import de.bsvrz.dua.plformal.av.DAVDatenAnmeldung;

/**
 * Über diese Schnittstelle sollen die Standardaspekte der Publikation von
 * bestimmten SWE-Modultyp-Kombinationen zur Verfügung gestellt werden. 
 * 
 * @author Thierfelder
 *
 */
public interface IStandardAspekte {

	/**
	 * Erfragt den Standardaspekt der Publikation für ein
	 * bestimmtes empfangenes Originaldatum
	 * 
	 * @param originalDatum das Originaldatum
	 * @return der Standardpublikationsaspekt
	 */
	public Aspect getStandardAspekt(final ResultData originalDatum);

	/**
	 * Erfragt die Datenanmeldungen, die für die Publikation unter
	 * den Standardaspekten durchgeführt werden müssen. 
	 * 
	 * @return die Datenanmeldungen, die für die Publikation unter
	 * den Standardaspekten durchgeführt werden müssen
	 */
	public Collection<DAVDatenAnmeldung> getStandardAnmeldungen();

}
