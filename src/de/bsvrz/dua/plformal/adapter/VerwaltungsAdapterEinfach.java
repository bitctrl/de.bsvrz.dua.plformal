package de.bsvrz.dua.plformal.adapter;

import java.util.Collection;
import java.util.HashSet;

import stauma.dav.configuration.interfaces.ConfigurationArea;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DAVHilfe;
import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungsHilfe;

/**
 * Einfacher Verwaltungsadapter, der automatisch die Konfigurationsbereiche ausliest.
 * Diese Klasse kann für die meisten SWE innerhalb der DUA als Basisklasse für die
 * Verwaltung benutzt werden.
 * 
 * @author Thierfelder
 *
 */
public abstract class VerwaltungsAdapterEinfach 
extends AbstraktVerwaltungsAdapter{
	
	/**
	 * der Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Die Konfigurationsbreiche, deren Objekte bearbeitet werden sollen
	 */
	private Collection<ConfigurationArea> kBereiche = new HashSet<ConfigurationArea>();

	/**
	 * Verbindung zur Datenflusssteuerung
	 */
	protected DatenFlussSteuerungsHilfe dfsHilfe = null;

	/**
	 * {@inheritDoc}
	 */
	public Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return this.kBereiche;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere()
	throws DUAInitialisierungsException {		
		if(super.komArgumente != null){
			this.kBereiche = DAVHilfe.getKonfigurationsBereicheAlsObjekte(
					DAVHilfe.getArgument(DAVKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID,
							this.komArgumente), super.verbindung);
			
			String dummy = "---keine Konfigurationsbereiche angegeben---\n"; //$NON-NLS-1$
			if(kBereiche.size() > 0){
				dummy = DAVKonstanten.EMPTY_STR;
				for(ConfigurationArea kb:kBereiche){
					dummy += kb + "\n"; //$NON-NLS-1$
				}
			}
			LOGGER.config("Die Applikation " + this.getApplikationsName() +//$NON-NLS-1$ 
					" wurde für folgende" +  //$NON-NLS-1$
					" Konfigurationsbereiche gestartet:\n" + dummy); //$NON-NLS-1$
			
			this.dfsHilfe = DatenFlussSteuerungsHilfe.getInstanz(this);
		}else{
			throw new DUAInitialisierungsException("Es wurden keine" + //$NON-NLS-1$
					" Kommandozeilenargumente übergeben."); //$NON-NLS-1$
		}
	}

}
