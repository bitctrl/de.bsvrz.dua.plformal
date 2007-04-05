package de.bsvrz.dua.plformal.adapter;

import java.util.Collection;
import java.util.HashSet;

import stauma.dav.configuration.interfaces.ConfigurationArea;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAHilfe;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
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
	public final Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return this.kBereiche;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere()
	throws DUAInitialisierungsException {		
		if(super.komArgumente != null){
			this.kBereiche = DUAHilfe.getKonfigurationsBereicheAlsObjekte(
					DUAHilfe.getArgument(DUAKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID,
							this.komArgumente), super.verbindung);
			
			String dummy = "---keine Konfigurationsbereiche angegeben---\n"; //$NON-NLS-1$
			if(kBereiche.size() > 0){
				dummy = DUAKonstanten.EMPTY_STR;
				for(ConfigurationArea kb:kBereiche){
					dummy += kb + "\n"; //$NON-NLS-1$
				}
			}
			LOGGER.config("Die Applikation " + this.getSWETyp().toString() +//$NON-NLS-1$ 
					" wurde für folgende" +  //$NON-NLS-1$
					" Konfigurationsbereiche gestartet:\n" + dummy); //$NON-NLS-1$
			
			this.dfsHilfe = DatenFlussSteuerungsHilfe.getInstanz(this);
		}else{
			throw new DUAInitialisierungsException("Es wurden keine" + //$NON-NLS-1$
					" Kommandozeilenargumente übergeben."); //$NON-NLS-1$
		}
	}

}
