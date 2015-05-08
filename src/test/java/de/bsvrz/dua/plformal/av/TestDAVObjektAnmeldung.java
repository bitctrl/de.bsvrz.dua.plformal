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

package de.bsvrz.dua.plformal.av;

import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dua.plformal.DAVTest;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;

/**
 * Tests für die Klasse <code>DAVObjektAnmeldung</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
@Ignore("Datenverteileranmeldung prüfen")
public class TestDAVObjektAnmeldung {

	/**
	 * Datenverteiler-Verbindung.
	 */
	private static ClientDavInterface verbindung;

	/**
	 * Anzahl der Durchläufe im Aufbau von zufälligen DAVObjektAnmeldung-Bäumen.
	 */
	private static final int GENAUIGKEIT = 20;

	@Before
	public void setUp() throws Exception {
		TestDAVObjektAnmeldung.verbindung = DAVTest.getDav();
	}

	/**
	 * Testet das Verhalten von mehreren Objekten der Klasse
	 * <code>DAVObjektAnmeldung</code> in einer <code>TreeMap</code> bzw. einem
	 * <code>TreeSet</code>.
	 */
	@Test
	public void testVerhaltenInTreeSet() {
		final TreeSet<DAVObjektAnmeldung> testSet = new TreeSet<DAVObjektAnmeldung>();

		final SystemObject[] o = new SystemObject[] {
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0001"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0002"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0003"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0004"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0005"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0006"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0007"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0008"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getObject("mq.a100.0009") };

		final DataDescription[] d = new DataDescription[6];
		d[0] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.agregation1Minute"));

		d[1] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel()
						.getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.parameterVorgabe"));

		d[2] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.agregation5Minuten"));

		d[3] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.agregation15Minuten"));

		d[4] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel()
						.getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.parameterSoll"));

		d[5] = new DataDescription(
				TestDAVObjektAnmeldung.verbindung.getDataModel()
						.getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),
				TestDAVObjektAnmeldung.verbindung.getDataModel().getAspect("asp.parameterIst"));

		try {
			final DAVObjektAnmeldung[] a = { new DAVObjektAnmeldung(o[0], d[0]), new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]), new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]), new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]), new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0]),

					new DAVObjektAnmeldung(o[0], d[1]), new DAVObjektAnmeldung(o[1], d[1]),
					new DAVObjektAnmeldung(o[2], d[1]), new DAVObjektAnmeldung(o[3], d[1]),
					new DAVObjektAnmeldung(o[4], d[1]), new DAVObjektAnmeldung(o[5], d[1]),
					new DAVObjektAnmeldung(o[6], d[1]), new DAVObjektAnmeldung(o[7], d[1]),
					new DAVObjektAnmeldung(o[8], d[1]),

					new DAVObjektAnmeldung(o[0], d[2]), new DAVObjektAnmeldung(o[1], d[2]),
					new DAVObjektAnmeldung(o[2], d[2]), new DAVObjektAnmeldung(o[3], d[2]),
					new DAVObjektAnmeldung(o[4], d[2]), new DAVObjektAnmeldung(o[5], d[2]),
					new DAVObjektAnmeldung(o[6], d[2]), new DAVObjektAnmeldung(o[7], d[2]),
					new DAVObjektAnmeldung(o[8], d[2]),

					new DAVObjektAnmeldung(o[0], d[3]), new DAVObjektAnmeldung(o[1], d[3]),
					new DAVObjektAnmeldung(o[2], d[3]), new DAVObjektAnmeldung(o[3], d[3]),
					new DAVObjektAnmeldung(o[4], d[3]), new DAVObjektAnmeldung(o[5], d[3]),
					new DAVObjektAnmeldung(o[6], d[3]), new DAVObjektAnmeldung(o[7], d[3]),
					new DAVObjektAnmeldung(o[8], d[3]),

					new DAVObjektAnmeldung(o[0], d[4]), new DAVObjektAnmeldung(o[1], d[4]),
					new DAVObjektAnmeldung(o[2], d[4]), new DAVObjektAnmeldung(o[3], d[4]),
					new DAVObjektAnmeldung(o[4], d[4]), new DAVObjektAnmeldung(o[5], d[4]),
					new DAVObjektAnmeldung(o[6], d[4]), new DAVObjektAnmeldung(o[7], d[4]),
					new DAVObjektAnmeldung(o[8], d[4]),

					new DAVObjektAnmeldung(o[0], d[5]), new DAVObjektAnmeldung(o[1], d[5]),
					new DAVObjektAnmeldung(o[2], d[5]), new DAVObjektAnmeldung(o[3], d[5]),
					new DAVObjektAnmeldung(o[4], d[5]), new DAVObjektAnmeldung(o[5], d[5]),
					new DAVObjektAnmeldung(o[6], d[5]), new DAVObjektAnmeldung(o[7], d[5]),
					new DAVObjektAnmeldung(o[8], d[5]) };

			final DAVObjektAnmeldung[] doppel = { new DAVObjektAnmeldung(o[0], d[0]),
					new DAVObjektAnmeldung(o[1], d[0]), new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]), new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]), new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]), new DAVObjektAnmeldung(o[8], d[0]),

					new DAVObjektAnmeldung(o[0], d[1]), new DAVObjektAnmeldung(o[1], d[1]),
					new DAVObjektAnmeldung(o[2], d[1]), new DAVObjektAnmeldung(o[3], d[1]),
					new DAVObjektAnmeldung(o[4], d[1]), new DAVObjektAnmeldung(o[5], d[1]),
					new DAVObjektAnmeldung(o[6], d[1]), new DAVObjektAnmeldung(o[7], d[1]),
					new DAVObjektAnmeldung(o[8], d[1]),

					new DAVObjektAnmeldung(o[0], d[2]), new DAVObjektAnmeldung(o[1], d[2]),
					new DAVObjektAnmeldung(o[2], d[2]), new DAVObjektAnmeldung(o[3], d[2]),
					new DAVObjektAnmeldung(o[4], d[2]), new DAVObjektAnmeldung(o[5], d[2]),
					new DAVObjektAnmeldung(o[6], d[2]), new DAVObjektAnmeldung(o[7], d[2]),
					new DAVObjektAnmeldung(o[8], d[2]),

					new DAVObjektAnmeldung(o[0], d[3]), new DAVObjektAnmeldung(o[1], d[3]),
					new DAVObjektAnmeldung(o[2], d[3]), new DAVObjektAnmeldung(o[3], d[3]),
					new DAVObjektAnmeldung(o[4], d[3]), new DAVObjektAnmeldung(o[5], d[3]),
					new DAVObjektAnmeldung(o[6], d[3]), new DAVObjektAnmeldung(o[7], d[3]),
					new DAVObjektAnmeldung(o[8], d[3]),

					new DAVObjektAnmeldung(o[0], d[4]), new DAVObjektAnmeldung(o[1], d[4]),
					new DAVObjektAnmeldung(o[2], d[4]), new DAVObjektAnmeldung(o[3], d[4]),
					new DAVObjektAnmeldung(o[4], d[4]), new DAVObjektAnmeldung(o[5], d[4]),
					new DAVObjektAnmeldung(o[6], d[4]), new DAVObjektAnmeldung(o[7], d[4]),
					new DAVObjektAnmeldung(o[8], d[4]),

					new DAVObjektAnmeldung(o[0], d[5]), new DAVObjektAnmeldung(o[1], d[5]),
					new DAVObjektAnmeldung(o[2], d[5]), new DAVObjektAnmeldung(o[3], d[5]),
					new DAVObjektAnmeldung(o[4], d[5]), new DAVObjektAnmeldung(o[5], d[5]),
					new DAVObjektAnmeldung(o[6], d[5]), new DAVObjektAnmeldung(o[7], d[5]),
					new DAVObjektAnmeldung(o[8], d[5]) };

			for (int i = 0; i < TestDAVObjektAnmeldung.GENAUIGKEIT; i++) {
				testSet.clear();
				final int[] zahlen = DAVTest.getZufaelligeZahlen(a.length);
				for (final int element : zahlen) {
					testSet.add(a[element]);
				}
				for (int j = 0; j < zahlen.length; j++) {
					testSet.add(doppel[j]);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", " + zahlen.length,
						testSet.size() == zahlen.length);
				for (final DAVObjektAnmeldung anmeldung : a) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for (final DAVObjektAnmeldung anmeldung : doppel) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}

			final DAVObjektAnmeldung[] a1 = { new DAVObjektAnmeldung(o[0], d[0]),

					new DAVObjektAnmeldung(o[0], d[1]),

					new DAVObjektAnmeldung(o[0], d[2]),

					new DAVObjektAnmeldung(o[0], d[3]),

					new DAVObjektAnmeldung(o[0], d[4]),

					new DAVObjektAnmeldung(o[0], d[5]) };

			final DAVObjektAnmeldung[] doppel1 = { new DAVObjektAnmeldung(o[0], d[0]),
					new DAVObjektAnmeldung(o[1], d[0]), new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]), new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]), new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]), new DAVObjektAnmeldung(o[8], d[0]) };

			for (int i = 0; i < TestDAVObjektAnmeldung.GENAUIGKEIT; i++) {
				testSet.clear();
				final int[] zahlen = DAVTest.getZufaelligeZahlen(a1.length);
				for (int j = 0; j < a1.length; j++) {
					testSet.add(a1[zahlen[j]]);
				}
				for (final DAVObjektAnmeldung element : doppel1) {
					testSet.add(element);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", 14", testSet.size() == 14);
				for (final DAVObjektAnmeldung anmeldung : a1) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for (final DAVObjektAnmeldung anmeldung : doppel1) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}

			final DAVObjektAnmeldung[] a2 = { new DAVObjektAnmeldung(o[0], d[0]), new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]), new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]), new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]), new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0]) };

			final DAVObjektAnmeldung[] doppel2 = { new DAVObjektAnmeldung(o[0], d[1]),
					new DAVObjektAnmeldung(o[0], d[0]), new DAVObjektAnmeldung(o[0], d[2]),
					new DAVObjektAnmeldung(o[1], d[3]), new DAVObjektAnmeldung(o[2], d[3]),
					new DAVObjektAnmeldung(o[0], d[4]), new DAVObjektAnmeldung(o[4], d[4]),
					new DAVObjektAnmeldung(o[8], d[5]) };

			for (int i = 0; i < TestDAVObjektAnmeldung.GENAUIGKEIT; i++) {
				testSet.clear();
				final int[] zahlen = DAVTest.getZufaelligeZahlen(a2.length);
				for (int j = 0; j < a2.length; j++) {
					testSet.add(a2[zahlen[j]]);
				}
				for (final DAVObjektAnmeldung element : doppel2) {
					testSet.add(element);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", 16", testSet.size() == 16);
				for (final DAVObjektAnmeldung anmeldung : a2) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for (final DAVObjektAnmeldung anmeldung : doppel2) {
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
