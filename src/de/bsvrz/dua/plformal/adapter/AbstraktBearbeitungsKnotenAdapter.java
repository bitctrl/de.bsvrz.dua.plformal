package de.bsvrz.dua.plformal.adapter;

import stauma.dav.clientside.ClientSenderInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.clientside.SenderRole;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.Meldungen;
import de.bsvrz.dua.plformal.allgemein.StandardAspekteVersorger;
import de.bsvrz.dua.plformal.av.DAVSendeAnmeldungsVerwaltung;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungsHilfe;
import de.bsvrz.dua.plformal.schnittstellen.IBearbeitungsKnoten;
import de.bsvrz.dua.plformal.schnittstellen.IStandardAspekte;
import de.bsvrz.dua.plformal.schnittstellen.IVerwaltung;

/**
 * Adapterklasse für einen Bearbeitungsknoten.
 * 
 * @author Thierfelder
 *
 */
public abstract class AbstraktBearbeitungsKnotenAdapter
implements IBearbeitungsKnoten, ClientSenderInterface {
	
	/**
	 * der Logger
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
	 * Schnittstelle zu den Informationen über die Standardpublikations-
	 * Aspekte
	 */
	protected IStandardAspekte standardAspekte = null;

	/**
	 * Parameter der Datenflusssteuerung
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
			throw new DUAInitialisierungsException(Meldungen.getString(
					"Es_konnte_keine_Verbindung_zum_Verwaltungsmodul_hergestellt_werden")); //$NON-NLS-1$
		}
		this.verwaltung = dieVerwaltung;
		this.standardAspekte = StandardAspekteVersorger.getInstanz(verwaltung).
					getStandardPubInfos(this.verwaltung.getApplikationsName(), this.getModulName());
		this.publikationsAnmeldungen = new DAVSendeAnmeldungsVerwaltung(this.verwaltung.getVerbindung(),
				SenderRole.source(), this);
		DatenFlussSteuerungsHilfe.getInstanz(verwaltung).addListener(this);
	}

	/**
	 * Publiziert eine <code>ResultData</code>-Datum in den Datenverteiler
	 * 
	 * @param resultat das Datum
	 */
	protected void publiziere(ResultData resultat){
		try {
			this.verwaltung.getVerbindung().sendData(resultat);
		} catch (Exception e) {
			LOGGER.error(this.toString(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Modul-Typ: " + this.getModulName() + " in SWE " + this.verwaltung.getApplikationsName();  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
		// wird nicht gebraucht, da nur Quellenanmeldungen durchgeführt werden
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
		return false;
	}
	
}
