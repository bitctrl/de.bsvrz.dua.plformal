package de.bsvrz.dua.plformal.adapter;

import stauma.dav.clientside.ClientSenderInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.clientside.SenderRole;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IBearbeitungsKnoten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IStandardAspekte;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.av.DAVSendeAnmeldungsVerwaltung;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungsHilfe;

/**
 * Adapterklasse für einen Bearbeitungsknoten.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktBearbeitungsKnotenAdapter
implements IBearbeitungsKnoten, ClientSenderInterface {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * nächster Bearbeitungsknoten
	 */
	protected IBearbeitungsKnoten knoten = null;
	
	/**
	 * <b>FLAG</b>: Soll publiziert werden?
	 */
	protected boolean publizieren = false;
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	protected IVerwaltung verwaltung = null;
	
	/**
	 * Schnittstelle zu den Informationen über die
	 * Standard-Publikationsaspekte
	 */
	protected IStandardAspekte standardAspekte = null;

	/**
	 * Anmeldungen zum Publizieren von verarbeiteten
	 * Daten
	 */
	protected DAVSendeAnmeldungsVerwaltung publikationsAnmeldungen = null;
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setPublikation(boolean publizieren) {
		this.publizieren = publizieren;	
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNaechstenBearbeitungsKnoten(IBearbeitungsKnoten knoten) {
		this.knoten = knoten;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialisiere(IVerwaltung dieVerwaltung) 
	throws DUAInitialisierungsException {
		if(dieVerwaltung == null || dieVerwaltung.getVerbindung() == null){
			throw new DUAInitialisierungsException("Es konnte keine Verbindung" + //$NON-NLS-1$
					" zum Verwaltungsmodul (bzw. zum Datenverteiler" + //$NON-NLS-1$
					") hergestellt werden"); //$NON-NLS-1$
		}
		this.verwaltung = dieVerwaltung;
		this.publikationsAnmeldungen = new DAVSendeAnmeldungsVerwaltung(
				this.verwaltung.getVerbindung(),
				SenderRole.source(), this);
		DatenFlussSteuerungsHilfe.getInstanz(verwaltung).addListener(this);
	}

	
	/**
	 * Publiziert ein <code>ResultData</code>-Datum
	 * in den Datenverteiler
	 * 
	 * @param resultat das Datum
	 */
	protected void publiziere(final ResultData resultat){
		if(resultat != null){
			try {
				this.verwaltung.getVerbindung().sendData(resultat);
			} catch (Exception e) {
				LOGGER.error("Fehler beim Publizieren von" + //$NON-NLS-1$
						" Daten innerhalb von " + //$NON-NLS-1$
						this.toString() + "\nDatum: " + resultat, e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Modul-Typ: " + this.getModulTyp() + //$NON-NLS-1$
				" in SWE " + this.verwaltung.getSWETyp();  //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object, DataDescription
			dataDescription, byte state) {
		/**
		 * wird nicht gebraucht, da nur Quellenanmeldungen
		 * durchgeführt werden
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		return false;
	}
	
}
