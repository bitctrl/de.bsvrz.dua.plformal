/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.1 Plausibilit�tspr�fung formal
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.dua.plformal.vew;

import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import sys.funclib.application.StandardApplicationRunner;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.plformal.PPFVersorger;
import de.bsvrz.dua.plformal.plformal.PlPruefungFormal;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorger;
import de.bsvrz.dua.plformal.plformal.schnittstellen.IPPFVersorgerListener;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktVerwaltungsAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVEmpfangsAnmeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;

/**
 * Implementierung des Moduls Verwaltung der SWE PL-Pr�fung formal.
 * Dieses Modul erfragt die zu �berpr�fenden Daten aus der Parametrierung
 * und initialisiert damit das Modul PL-Pr�fung formal, das dann die 
 * eigentliche Pr�fung durchf�hrt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class VerwaltungPlPruefungFormal
extends AbstraktVerwaltungsAdapter
implements IPPFVersorgerListener{
	
	/**
	 * Debug-Logger
	 */
	protected static final Debug LOGGER = Debug.getLogger();

	/**
	 * Instanz des Moduls PL-Pr�fung formal
	 */
	private PlPruefungFormal plPruefungFormal = null;
		
	/**
	 * Verwaltung f�r alle Empfangsanmeldungen dieses Moduls
	 */
	private DAVEmpfangsAnmeldungsVerwaltung empfangsVerwaltung = null; 
	
	
	/**
	 * {@inheritDoc}
	 */
	public SWETyp getSWETyp() {
		return SWETyp.PL_PRUEFUNG_FORMAL;
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereParameter(IPPFVersorger parameter) {
		/**
		 * Die Menge der f�r diese SWE betrachteten
		 * Objekte �ndert sich dynamisch 
		 */
		this.objekte = parameter.getBetrachteteObjekte();
		
		this.empfangsVerwaltung.modifiziereObjektAnmeldung(
				parameter.getObjektAnmeldungen());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialisiere()
	throws DUAInitialisierungsException {
		this.empfangsVerwaltung = new DAVEmpfangsAnmeldungsVerwaltung(
										this.verbindung,
										ReceiverRole.receiver(),
										ReceiveOptions.delayed(),
										this);
		
		this.plPruefungFormal = new PlPruefungFormal(
				new PPFStandardAspekteVersorger(this).getStandardPubInfos());
		this.plPruefungFormal.setPublikation(true);
		this.plPruefungFormal.initialisiere(this);
		
		/**
		 * An dieser Stelle werden die Parameter der formalen Plausibilisierung 
		 * auch ausgewertet, da sich hier aus Gr�nden der Systemarchitektur auf
		 * die Daten angemeldet werden muss, die innerhalb der Untermodule 
		 * plausibilisiert werden sollen.
		 */
		PPFVersorger.getInstanz(this).addListener(this);			
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		this.plPruefungFormal.aktualisiereDaten(resultate);
	}
	
	/**
	 * Startet diese Applikation
	 * 
	 * @param args Argumente der Kommandozeile
	 */
	public static void main(String argumente[]){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.
        				UncaughtExceptionHandler(){
            public void uncaughtException(@SuppressWarnings("unused")
			Thread t, Throwable e) {
                LOGGER.error("Applikation wird wegen" +  //$NON-NLS-1$
                		" unerwartetem Fehler beendet", e);  //$NON-NLS-1$
                Runtime.getRuntime().exit(0);
            }
        });
		StandardApplicationRunner.run(
					new VerwaltungPlPruefungFormal(),argumente);
	}
	
}
