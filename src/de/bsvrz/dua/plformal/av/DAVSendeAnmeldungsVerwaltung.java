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

package de.bsvrz.dua.plformal.av;

import java.util.Collection;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.ClientSenderInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.clientside.SenderRole;
import stauma.dav.configuration.interfaces.SystemObject;
import sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Verwaltungsklasse für Datenanmeldungen zum Senden von Daten.
 * Über die Methode <code>modifiziereDatenAnmeldung(..)</code> lassen
 * sich Daten anmelden bzw. abmelden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *  
 */
public class DAVSendeAnmeldungsVerwaltung 
extends DAVAnmeldungsVerwaltung
implements ClientSenderInterface{

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Rolle des Senders
	 */
	private SenderRole rolle = null;
		
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteilerverbindung
	 * @param rolle Rolle
	 */
	public DAVSendeAnmeldungsVerwaltung(final ClientDavInterface dav,
									    final SenderRole rolle){
		super(dav);
		this.rolle = rolle;
	}
	
	/**
	 * Sendet ein Datum in den Datenverteiler unter der 
	 * Vorraussetzung, dass die Sendesteuerung für dieses
	 * Datum einen Empfänger bzw. eine Senke festgestellt hat
	 * 
	 * @param resultat ein zu sendendes Datum
	 */
	public final void sende(final ResultData resultat){	
		try {
			DAVObjektAnmeldung anmeldung = new DAVObjektAnmeldung(resultat);
			Byte status = this.aktuelleObjektAnmeldungen.get(anmeldung);
			if(status == null || 
					status == ClientSenderInterface.START_SENDING){
				this.dav.sendData(resultat);
			}			
		} catch (Exception e) {
			LOGGER.error(Konstante.LEERSTRING, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String abmelden(final Collection<DAVObjektAnmeldung> abmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(abmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung abmeldung:abmeldungen){
			try{
				synchronized (this.aktuelleObjektAnmeldungen) {
					this.dav.unsubscribeSender(this, abmeldung.getObjekt(),
							abmeldung.getDatenBeschreibung());
					this.aktuelleObjektAnmeldungen.remove(abmeldung);
					info += abmeldung;
				}
			}catch(Exception e){
				LOGGER.error("Probleme beim" + //$NON-NLS-1$
						" Abmelden als Sender/Quelle", e); //$NON-NLS-1$
			}
		}
		return info;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String anmelden(final Collection<DAVObjektAnmeldung> anmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(anmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		} 
		for(DAVObjektAnmeldung anmeldung:anmeldungen){
			try {
				synchronized (this.aktuelleObjektAnmeldungen) {
					this.dav.subscribeSender(this, anmeldung.getObjekt(),
							anmeldung.getDatenBeschreibung(), this.rolle);
					this.aktuelleObjektAnmeldungen.put(anmeldung, null);
					info += anmeldung;					
				}
			} catch (Exception e) {
				LOGGER.error("Probleme beim" + //$NON-NLS-1$
						" Anmelden als Sender/Quelle", e); //$NON-NLS-1$
			}		
		}
		return info;
	}

	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object, DataDescription
			dataDescription, byte state) {
		try {
			synchronized (this.aktuelleObjektAnmeldungen) {
				DAVObjektAnmeldung anmeldung = 
						new DAVObjektAnmeldung(object, dataDescription);
				this.aktuelleObjektAnmeldungen.put(anmeldung, state);
			}
		} catch (Exception e) {
			LOGGER.error("Problem" + //$NON-NLS-1$
					" innerhalb der Sendesteuerung", e); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(final SystemObject object,
									  final DataDescription dataDescription) {
		boolean resultat = false;
		
		try {
			DAVObjektAnmeldung anmeldung = 
				new DAVObjektAnmeldung(object, dataDescription);
			if(this.aktuelleObjektAnmeldungen.containsKey(anmeldung)){
				resultat = true;	
			}
		} catch (Exception e) {
			LOGGER.error("Problem" + //$NON-NLS-1$
					" innerhalb der Sendesteuerung", e); //$NON-NLS-1$
		}
		
		return resultat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getInfo() {
		return this.rolle.toString();
	}
}
