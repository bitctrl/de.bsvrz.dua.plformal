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


import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.SystemObject;
import de.bsvrz.dua.plformal.extra.DAVTest;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;

/**
 * Tests für die Klasse <code>DAVObjektAnmeldung</code>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class TestDAVObjektAnmeldung{
	
	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface VERBINDUNG = null;
	
	/**
	 * Anzahl der Durchläufe im Aufbau von zufälligen 
	 * DAVObjektAnmeldung-Bäumen
	 */
	private static final int GENAUIGKEIT = 20;
	
	/**
	 * {@inheritDoc}
	 */
	@Before
	public void setUp() throws Exception {
		VERBINDUNG = DAVTest.getDav();
	}

	/**
	 * Testet das Verhalten von mehreren Objekten
	 * der Klasse <code>DAVObjektAnmeldung</code>
	 * in einer <code>TreeMap</code> bzw einem
	 * <code>TreeSet</code>
	 */
	@Test
	public void testVerhaltenInTreeSet(){
		TreeSet<DAVObjektAnmeldung> testSet = 
			new TreeSet<DAVObjektAnmeldung>();

		SystemObject[] o = new SystemObject[]{
				VERBINDUNG.getDataModel().getObject("mq.a100.0001"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0002"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0003"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0004"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0005"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0006"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0007"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0008"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getObject("mq.a100.0009")};   //$NON-NLS-1$

		DataDescription d[] = new DataDescription[6];
		d[0] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.agregation1Minute"), (short)0);   //$NON-NLS-1$

		d[1] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.parameterVorgabe"), (short)0);   //$NON-NLS-1$

		d[2] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.agregation5Minuten"), (short)0);   //$NON-NLS-1$

		d[3] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.agregation15Minuten"), (short)0);   //$NON-NLS-1$

		d[4] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.parameterSoll"), (short)0);   //$NON-NLS-1$

		d[5] = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitAnalyseMq"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.parameterIst"), (short)0);   //$NON-NLS-1$


		try {
			DAVObjektAnmeldung[] a = {
					new DAVObjektAnmeldung(o[0], d[0]),					
					new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0]),

					new DAVObjektAnmeldung(o[0], d[1]),					
					new DAVObjektAnmeldung(o[1], d[1]),
					new DAVObjektAnmeldung(o[2], d[1]),
					new DAVObjektAnmeldung(o[3], d[1]),
					new DAVObjektAnmeldung(o[4], d[1]),
					new DAVObjektAnmeldung(o[5], d[1]),
					new DAVObjektAnmeldung(o[6], d[1]),
					new DAVObjektAnmeldung(o[7], d[1]),
					new DAVObjektAnmeldung(o[8], d[1]),

					new DAVObjektAnmeldung(o[0], d[2]),					
					new DAVObjektAnmeldung(o[1], d[2]),
					new DAVObjektAnmeldung(o[2], d[2]),
					new DAVObjektAnmeldung(o[3], d[2]),
					new DAVObjektAnmeldung(o[4], d[2]),
					new DAVObjektAnmeldung(o[5], d[2]),
					new DAVObjektAnmeldung(o[6], d[2]),
					new DAVObjektAnmeldung(o[7], d[2]),
					new DAVObjektAnmeldung(o[8], d[2]),

					new DAVObjektAnmeldung(o[0], d[3]),					
					new DAVObjektAnmeldung(o[1], d[3]),
					new DAVObjektAnmeldung(o[2], d[3]),
					new DAVObjektAnmeldung(o[3], d[3]),
					new DAVObjektAnmeldung(o[4], d[3]),
					new DAVObjektAnmeldung(o[5], d[3]),
					new DAVObjektAnmeldung(o[6], d[3]),
					new DAVObjektAnmeldung(o[7], d[3]),
					new DAVObjektAnmeldung(o[8], d[3]),

					new DAVObjektAnmeldung(o[0], d[4]),					
					new DAVObjektAnmeldung(o[1], d[4]),
					new DAVObjektAnmeldung(o[2], d[4]),
					new DAVObjektAnmeldung(o[3], d[4]),
					new DAVObjektAnmeldung(o[4], d[4]),
					new DAVObjektAnmeldung(o[5], d[4]),
					new DAVObjektAnmeldung(o[6], d[4]),
					new DAVObjektAnmeldung(o[7], d[4]),
					new DAVObjektAnmeldung(o[8], d[4]),

					new DAVObjektAnmeldung(o[0], d[5]),					
					new DAVObjektAnmeldung(o[1], d[5]),
					new DAVObjektAnmeldung(o[2], d[5]),
					new DAVObjektAnmeldung(o[3], d[5]),
					new DAVObjektAnmeldung(o[4], d[5]),
					new DAVObjektAnmeldung(o[5], d[5]),
					new DAVObjektAnmeldung(o[6], d[5]),
					new DAVObjektAnmeldung(o[7], d[5]),
					new DAVObjektAnmeldung(o[8], d[5]) };


			DAVObjektAnmeldung[] doppel = {
					new DAVObjektAnmeldung(o[0], d[0]),					
					new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0]),

					new DAVObjektAnmeldung(o[0], d[1]),					
					new DAVObjektAnmeldung(o[1], d[1]),
					new DAVObjektAnmeldung(o[2], d[1]),
					new DAVObjektAnmeldung(o[3], d[1]),
					new DAVObjektAnmeldung(o[4], d[1]),
					new DAVObjektAnmeldung(o[5], d[1]),
					new DAVObjektAnmeldung(o[6], d[1]),
					new DAVObjektAnmeldung(o[7], d[1]),
					new DAVObjektAnmeldung(o[8], d[1]),

					new DAVObjektAnmeldung(o[0], d[2]),					
					new DAVObjektAnmeldung(o[1], d[2]),
					new DAVObjektAnmeldung(o[2], d[2]),
					new DAVObjektAnmeldung(o[3], d[2]),
					new DAVObjektAnmeldung(o[4], d[2]),
					new DAVObjektAnmeldung(o[5], d[2]),
					new DAVObjektAnmeldung(o[6], d[2]),
					new DAVObjektAnmeldung(o[7], d[2]),
					new DAVObjektAnmeldung(o[8], d[2]),

					new DAVObjektAnmeldung(o[0], d[3]),					
					new DAVObjektAnmeldung(o[1], d[3]),
					new DAVObjektAnmeldung(o[2], d[3]),
					new DAVObjektAnmeldung(o[3], d[3]),
					new DAVObjektAnmeldung(o[4], d[3]),
					new DAVObjektAnmeldung(o[5], d[3]),
					new DAVObjektAnmeldung(o[6], d[3]),
					new DAVObjektAnmeldung(o[7], d[3]),
					new DAVObjektAnmeldung(o[8], d[3]),

					new DAVObjektAnmeldung(o[0], d[4]),					
					new DAVObjektAnmeldung(o[1], d[4]),
					new DAVObjektAnmeldung(o[2], d[4]),
					new DAVObjektAnmeldung(o[3], d[4]),
					new DAVObjektAnmeldung(o[4], d[4]),
					new DAVObjektAnmeldung(o[5], d[4]),
					new DAVObjektAnmeldung(o[6], d[4]),
					new DAVObjektAnmeldung(o[7], d[4]),
					new DAVObjektAnmeldung(o[8], d[4]),

					new DAVObjektAnmeldung(o[0], d[5]),					
					new DAVObjektAnmeldung(o[1], d[5]),
					new DAVObjektAnmeldung(o[2], d[5]),
					new DAVObjektAnmeldung(o[3], d[5]),
					new DAVObjektAnmeldung(o[4], d[5]),
					new DAVObjektAnmeldung(o[5], d[5]),
					new DAVObjektAnmeldung(o[6], d[5]),
					new DAVObjektAnmeldung(o[7], d[5]),
					new DAVObjektAnmeldung(o[8], d[5]) };

			for(int i = 0; i < GENAUIGKEIT; i++){
				testSet.clear();
				int[] zahlen = DAVTest.getZufaelligeZahlen(a.length);
				for(int j = 0; j < zahlen.length; j++){
					testSet.add(a[zahlen[j]]);
				}
				for(int j = 0; j < zahlen.length; j++){
					testSet.add(doppel[j]);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", " + //$NON-NLS-1$ //$NON-NLS-2$
						zahlen.length, testSet.size() == zahlen.length);
				for(DAVObjektAnmeldung anmeldung:a){
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for(DAVObjektAnmeldung anmeldung:doppel){
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}
			

			DAVObjektAnmeldung[] a1 = {
					new DAVObjektAnmeldung(o[0], d[0]),					

					new DAVObjektAnmeldung(o[0], d[1]),					

					new DAVObjektAnmeldung(o[0], d[2]),					

					new DAVObjektAnmeldung(o[0], d[3]),					

					new DAVObjektAnmeldung(o[0], d[4]),					

					new DAVObjektAnmeldung(o[0], d[5])};


			DAVObjektAnmeldung[] doppel1 = {
					new DAVObjektAnmeldung(o[0], d[0]),					
					new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0])};

			for(int i = 0; i < GENAUIGKEIT; i++){
				testSet.clear();
				int[] zahlen = DAVTest.getZufaelligeZahlen(a1.length);
				for(int j = 0; j < a1.length; j++){
					testSet.add(a1[zahlen[j]]);
				}
				for(int j = 0; j < doppel1.length; j++){
					testSet.add(doppel1[j]);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", 14", //$NON-NLS-1$ //$NON-NLS-2$
						testSet.size() == 14);
				for(DAVObjektAnmeldung anmeldung:a1){
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for(DAVObjektAnmeldung anmeldung:doppel1){
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}


			DAVObjektAnmeldung[] a2 = {
					new DAVObjektAnmeldung(o[0], d[0]),					
					new DAVObjektAnmeldung(o[1], d[0]),
					new DAVObjektAnmeldung(o[2], d[0]),
					new DAVObjektAnmeldung(o[3], d[0]),
					new DAVObjektAnmeldung(o[4], d[0]),
					new DAVObjektAnmeldung(o[5], d[0]),
					new DAVObjektAnmeldung(o[6], d[0]),
					new DAVObjektAnmeldung(o[7], d[0]),
					new DAVObjektAnmeldung(o[8], d[0])};

			DAVObjektAnmeldung[] doppel2 = {
					new DAVObjektAnmeldung(o[0], d[1]),
					new DAVObjektAnmeldung(o[0], d[0]),
					new DAVObjektAnmeldung(o[0], d[2]),					
					new DAVObjektAnmeldung(o[1], d[3]),
					new DAVObjektAnmeldung(o[2], d[3]),
					new DAVObjektAnmeldung(o[0], d[4]),					
					new DAVObjektAnmeldung(o[4], d[4]),
					new DAVObjektAnmeldung(o[8], d[5]) };

			for(int i = 0; i < GENAUIGKEIT; i++){
				testSet.clear();
				int[] zahlen = DAVTest.getZufaelligeZahlen(a2.length);
				for(int j = 0; j < a2.length; j++){
					testSet.add(a2[zahlen[j]]);
				}
				for(int j = 0; j < doppel2.length; j++){
					testSet.add(doppel2[j]);
				}

				Assert.assertTrue("Größe der Menge: " + testSet.size() + ", 16", //$NON-NLS-1$ //$NON-NLS-2$
						testSet.size() == 16);
				for(DAVObjektAnmeldung anmeldung:a2){
					Assert.assertTrue(testSet.contains(anmeldung));
				}
				for(DAVObjektAnmeldung anmeldung:doppel2){
					Assert.assertTrue(testSet.contains(anmeldung));
				}

			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
