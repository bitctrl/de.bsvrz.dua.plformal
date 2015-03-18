/*
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.1 Plausibilitätsprüfung formal
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

	// /**
	// * Verbindungsdaten.
	// */
	// private static final String[] CON_DATA = new String[] {
	// "-datenverteiler=localhost:8083",
	// "-benutzer=Tester",
	// "-authentifizierung=c:\\passwd" };

	/**
	 * Verbindungsdaten.
	 */
	private static final String[] CON_DATA = new String[] {
		"-datenverteiler=localhost:8083", "-benutzer=Tester",
	"-authentifizierung=passwd" };

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected static ClientDavInterface verbindung;

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

		if (DAVTest.verbindung == null) {
			StandardApplicationRunner.run(new StandardApplication() {

				@Override
				public void initialize(final ClientDavInterface connection)
						throws Exception {
					DAVTest.verbindung = connection;
				}

				@Override
				public void parseArguments(final ArgumentList argumentList)
						throws Exception {
					//
				}

			}, DAVTest.CON_DATA);
		}

		return DAVTest.verbindung;
	}

	/**
	 * Erfragt einen Array mit zufälligen Zahlen von 0 bis <code>anzahl</code>.
	 * Jede Zahl darf nur einmal im Array vorkommen.
	 *
	 * @param anzahl
	 *            die Obergrenze
	 * @return Array mit zufälligen Zahlen von 0 bis <code>anzahl</code>
	 */
	public static int[] getZufaelligeZahlen(final int anzahl) {
		int belegt = 0;
		final int[] zahlen = new int[anzahl];
		for (int i = 0; i < anzahl; i++) {
			zahlen[i] = -1;
		}

		while (belegt < anzahl) {
			final int index = DAVTest.random.nextInt(anzahl);
			if (zahlen[index] == -1) {
				zahlen[index] = belegt++;
			}
		}

		return zahlen;
	}

}
