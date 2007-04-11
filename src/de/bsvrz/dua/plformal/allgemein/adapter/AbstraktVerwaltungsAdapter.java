/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.allgemein.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.ArgumentList;
import sys.funclib.debug.Debug;
import sys.funclib.operatingMessage.MessageGrade;
import sys.funclib.operatingMessage.MessageSender;
import sys.funclib.operatingMessage.MessageState;
import sys.funclib.operatingMessage.MessageType;
import de.bsvrz.dua.plformal.allgemein.DUAUtensilien;
import de.bsvrz.dua.plformal.allgemein.DUAInitialisierungsException;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.allgemein.schnittstellen.IVerwaltung;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungsVersorger;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Adapterklasse für Verwaltungsmodule.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktVerwaltungsAdapter
implements IVerwaltung {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
			
	/**
	 * Die Objekte, die bearbeitet werden sollen.
	 */
	protected SystemObject[] objekte = null;
	
	/**
	 * Verbindung zum Datenverteiler 
	 */
	protected ClientDavInterface verbindung = null;
	
	/**
	 * Diese Klasse versendet Betriebsmeldungen
	 */
	protected MessageSender nachrichtenSender = null;
	
	/**
	 * die Argumente der Kommandozeile
	 */
	protected ArrayList<String> komArgumente = new ArrayList<String>();

	/**
	 * Die Konfigurationsbreiche, deren Objekte bearbeitet werden sollen
	 */
	private Collection<ConfigurationArea> kBereiche = new HashSet<ConfigurationArea>();

	/**
	 * Verbindung zur Datenflusssteuerung
	 */
	protected DatenFlussSteuerungsVersorger dfsHilfe = null;
	
	
	/**
	 * {@inheritDoc}
	 */
	public final Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return this.kBereiche;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final SystemObject[] getSystemObjekte() {
		return this.objekte;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final ClientDavInterface getVerbindung() {
		return this.verbindung;
	}
	
	/**
	 * Erfragt eine Verbindung zum Versenden von Betriebsmeldungen.
	 * 
	 * @return Betriebsmeldungs-Sender.
	 */
	public final MessageSender getBetriebsmeldungsSender(){
		return this.nachrichtenSender;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void sendeBetriebsMeldung(String id,
									 MessageType typ,
									 String nachrichtenTypErweiterung,
									 MessageGrade klasse,
									 MessageState status,
									 String nachricht) {
		this.nachrichtenSender.sendMessage(id, typ, nachrichtenTypErweiterung,
													klasse, status, nachricht);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface dieVerbindung)
	throws Exception {
		try{
			this.verbindung = dieVerbindung;
			this.nachrichtenSender = MessageSender.getInstance();
			this.nachrichtenSender.setApplicationLabel(this.getSWETyp().toString());
			
			if(this.komArgumente != null){
				this.kBereiche = getKonfigurationsBereicheAlsObjekte(
						DUAUtensilien.getArgument(DUAKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID,
								this.komArgumente));				
				this.dfsHilfe = DatenFlussSteuerungsVersorger.getInstanz(this);
			}else{
				throw new DUAInitialisierungsException("Es wurden keine" + //$NON-NLS-1$
						" Kommandozeilenargumente übergeben"); //$NON-NLS-1$
			}
			
			/**
			 * Initialisiere das eigentliche Verwaltungsmodul
			 */
			this.initialisiere();
			
			LOGGER.config(this.toString());
			
		}catch(DUAInitialisierungsException ex){
			String fehler = "Initialisierung der Applikation " + //$NON-NLS-1$ 
							this.getSWETyp().toString() +
							" fehlgeschlagen"; //$NON-NLS-1$
			LOGGER.error(fehler, ex);
			
			if(this.nachrichtenSender != null){
				this.nachrichtenSender.sendMessage(
						MessageType.APPLICATION_DOMAIN, MessageGrade.ERROR,
						fehler);
			}
			
			Runtime.getRuntime().exit(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void parseArguments(ArgumentList argumente)
	throws Exception {
		Debug.init(this.getSWETyp().toString(), argumente);
		
		for(String s:argumente.getArgumentStrings()){
			if(s != null)this.komArgumente.add(s);
		}
		
		argumente.fetchUnusedArguments();
	}
	
	/**
	 * Extrahiert aus einer Zeichenkette alle über Kommata getrennten 
	 * Konfigurationsbereiche und gibt deren Systemobjekte zurück.
	 * 
	 * @param kbString Zeichenkette mit den Konfigurationsbereichen
	 * @return (ggf. leere) <code>ConfigurationArea-Collection</code>
	 * mit allen extrahierten Konfigurationsbereichen.
	 */
	private final Collection<ConfigurationArea> 
				getKonfigurationsBereicheAlsObjekte(final String kbString){
		List<String> resultListe = new ArrayList<String>();
		
		if(kbString != null){
			String[] s = kbString.split(","); //$NON-NLS-1$
			for(String dummy:s){
				if(dummy != null && dummy.length() > 0){
					resultListe.add(dummy);
				}
			}
		}
		Collection<ConfigurationArea> kbListe = new HashSet<ConfigurationArea>();
		
		for(String kb:resultListe){
			try{
				ConfigurationArea area = this.verbindung.getDataModel().
											getConfigurationArea(kb);
				if(area != null)kbListe.add(area);
			}catch(UnsupportedOperationException ex){
				LOGGER.error("Konfigurationsbereich " + kb +  //$NON-NLS-1$
						" konnte nicht identifiziert werden.", ex); //$NON-NLS-1$
			}
		}
		
		return kbListe;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "SWE: " + this.getSWETyp() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		
		String dummy = "---keine Konfigurationsbereiche angegeben---\n"; //$NON-NLS-1$
		if(kBereiche.size() > 0){
			dummy = Konstante.LEERSTRING;
			for(ConfigurationArea kb:kBereiche){
				dummy += kb + "\n"; //$NON-NLS-1$
			}
		}
		
		return s + "Konfigurationsbereiche:\n" + dummy; //$NON-NLS-1$
	}
	

	/**
	 * Diese Methode wird zur Initialisierung aufgerufen,
	 * <b>nachdem</b> sowohl die Argumente der Kommandozeile,
	 * als auch die Datenverteilerverbindung übergeben wurden
	 * (also nach dem Aufruf der Methoden <code>parseArguments(..)</code>
	 * und <code>initialize(..)</code>).
	 * 
	 * @throws DUAInitialisierungsException falls es Probleme bei der
	 * Initialisierung geben sollte
	 */
	protected abstract void initialisiere()
	throws DUAInitialisierungsException;
	
}
