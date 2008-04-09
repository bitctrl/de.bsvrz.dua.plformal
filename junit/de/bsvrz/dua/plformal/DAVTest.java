/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
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

package de.bsvrz.dua.plformal;

import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Stellt eine Datenverteiler-Verbindung zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public final class DAVTest {

	/**
	 * Verbindungsdaten.
	 */
	private static final String[] CON_DATA = new String[] {
			"-datenverteiler=localhost:8083", //$NON-NLS-1$ 
			"-benutzer=Tester", //$NON-NLS-1$
			"-authentifizierung=passwd" }; //$NON-NLS-1$

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected static ClientDavInterface verbindung = null;

	/**
	 * Randomizer.
	 */
	private static Random random = new Random(System.currentTimeMillis());

	
	/**
	 * Standardkonstruktor.
	 */
	private DAVTest() {
		
	}
	
	
	/**
	 * Erfragt bzw. initialisiert eine Datenverteiler-Verbindung
	 * 
	 * @return die Datenverteiler-Verbindung
	 * @throws Exception
	 *             falls die Verbindung nicht hergestellt werden konnte
	 */
	public static ClientDavInterface getDav() throws Exception {

		if (verbindung == null) {
			StandardApplicationRunner.run(new StandardApplication() {

				public void initialize(ClientDavInterface connection)
						throws Exception {
					DAVTest.verbindung = connection;
				}

				public void parseArguments(ArgumentList argumentList)
						throws Exception {
					//
				}

			}, CON_DATA);
		}

		return verbindung;
	}

	/**
	 * Erfragt einen Array mit zufälligen Zahlen von 0 bis <code>anzahl</code>.
	 * Jede Zahl darf nur einmal im Array vorkommen.
	 * 
	 * @param anzahl
	 *            die Obergrenze
	 * @return Array mit zufälligen Zahlen von 0 bis <code>anzahl</code>
	 */
	public static int[] getZufaelligeZahlen(int anzahl) {
		int belegt = 0;
		int[] zahlen = new int[anzahl];
		for (int i = 0; i < anzahl; i++) {
			zahlen[i] = -1;
		}

		while (belegt < anzahl) {
			int index = random.nextInt(anzahl);
			if (zahlen[index] == -1) {
				zahlen[index] = belegt++;
			}
		}

		return zahlen;
	}

}
