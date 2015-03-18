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

package de.bsvrz.dua.plformal.allgemein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.ReferenceAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationAuthority;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationObject;
import de.bsvrz.dav.daf.main.impl.config.DafDynamicObject;
import de.bsvrz.dua.plformal.DAVTest;
import de.bsvrz.dua.plformal.plformal.PPFKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;

/**
 * Testklasse für die statischen Methoden der Klasse DUAUtensilien.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
@Ignore("Datenverteilerverbindung klären")
public class DUAUtensilienTest {

	/**
	 * Datenverteiler-Verbindung.
	 */
	private static ClientDavInterface verbindung;

	@Before
	public void setUp() throws Exception {
		DUAUtensilienTest.verbindung = DAVTest.getDav();
	}

	/**
	 * Test von <code>getArgument()</code>.
	 */
	@Test
	public void testGetArgument() {
		final ArrayList<String> para1 = new ArrayList<String>();
		para1.add("-a=1");
		para1.add("-b=2");
		final String suche1 = "a";
		final String soll1 = "1";

		final ArrayList<String> para2 = new ArrayList<String>();
		final String suche2 = "a";
		final String soll2 = null;

		final ArrayList<String> para3 = null;
		final String suche3 = "a";
		final String soll3 = null;

		final ArrayList<String> para4 = new ArrayList<String>();
		para4.add("-=1");
		para4.add("-b=2");
		final String suche4 = "a";
		final String soll4 = null;

		final ArrayList<String> para5 = new ArrayList<String>();
		para5.add("-a=");
		para5.add("-b=2");
		final String suche5 = "a";
		final String soll5 = null;

		final ArrayList<String> para6 = new ArrayList<String>();
		para6.add("-a=1");
		para6.add("-b=2");
		para6.add("-c=3");
		final String suche6 = "a";
		final String soll6 = "1";

		final ArrayList<String> para7 = new ArrayList<String>();
		para7.add("-b=2");
		para7.add("-c=3");
		para7.add("-a=1");
		final String suche7 = "a";
		final String soll7 = "1";

		final ArrayList<String> para8 = new ArrayList<String>();
		para8.add("-b=2");
		para8.add("-a=1");
		para8.add("-c=3");
		final String suche8 = "a";
		final String soll8 = "1";

		Assert.assertEquals("1: ", DUAUtensilien.getArgument(suche1, para1),
				soll1);
		Assert.assertEquals("2: ", DUAUtensilien.getArgument(suche2, para2),
				soll2);
		Assert.assertEquals("3: ", DUAUtensilien.getArgument(suche3, para3),
				soll3);
		Assert.assertEquals("4: ", DUAUtensilien.getArgument(suche4, para4),
				soll4);
		Assert.assertEquals("5: ", DUAUtensilien.getArgument(suche5, para5),
				soll5);
		Assert.assertEquals("6: ", DUAUtensilien.getArgument(suche6, para6),
				soll6);
		Assert.assertEquals("7: ", DUAUtensilien.getArgument(suche7, para7),
				soll7);
		Assert.assertEquals("8: ", DUAUtensilien.getArgument(suche8, para8),
				soll8);
	}

	/**
	 * Test von <code>testGetBasisInstanzen1()</code>.
	 */
	@Test
	public void testGetBasisInstanzen1() {
		final SystemObject o1 = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.messQuerschnittAllgemein");
		final SystemObject o2 = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.messQuerschnitt");
		final SystemObject o3 = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("mq.a100.0010");
		final SystemObject o4 = null;
		final SystemObject o5 = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.typ");

		final Collection<SystemObject> ist1 = DUAUtensilien.getBasisInstanzen(
				o1, DUAUtensilienTest.verbindung);
		final Collection<SystemObject> ist2 = DUAUtensilien.getBasisInstanzen(
				o2, DUAUtensilienTest.verbindung);
		final Collection<SystemObject> ist3 = DUAUtensilien.getBasisInstanzen(
				o3, DUAUtensilienTest.verbindung);
		final Collection<SystemObject> ist4 = DUAUtensilien.getBasisInstanzen(
				o4, DUAUtensilienTest.verbindung);
		final Collection<SystemObject> ist5 = DUAUtensilien.getBasisInstanzen(
				o5, DUAUtensilienTest.verbindung);

		final Collection<SystemObject> soll1 = new HashSet<SystemObject>();
		final Collection<SystemObject> soll2 = new HashSet<SystemObject>();
		final Collection<SystemObject> soll3 = new HashSet<SystemObject>();
		final Collection<SystemObject> soll4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5 = new HashSet<SystemObject>();

		soll1.addAll(((SystemObjectType) DUAUtensilienTest.verbindung
				.getDataModel().getObject("typ.messQuerschnitt")).getElements());
		soll2.addAll(((SystemObjectType) DUAUtensilienTest.verbindung
				.getDataModel().getObject("typ.messQuerschnitt")).getElements());
		soll3.add(DUAUtensilienTest.verbindung.getDataModel().getObject(
				"mq.a100.0010"));
		for (final SystemObject obj : ((SystemObjectType) DUAUtensilienTest.verbindung
				.getDataModel().getObject("typ.typ")).getElements()) {
			for (final SystemObject elem : ((SystemObjectType) obj)
					.getElements()) {

				if (elem.getClass().equals(DafConfigurationObject.class)
						|| elem.getClass().equals(DafDynamicObject.class)
						|| elem.getClass().equals(
								DafConfigurationAuthority.class)) {
					soll4.add(elem);

				}

			}

		}
		soll5 = soll4;

		Assert.assertEquals("1: ", soll1, ist1);
		Assert.assertEquals("2: ", soll2, ist2);
		Assert.assertEquals("3: ", soll3, ist3);
		Assert.assertEquals("4: ", soll4, ist4);
		Assert.assertEquals("5: ", soll5, ist5);
	}

	/**
	 * Vergleicht zwei Objekte.
	 *
	 * @param obj1
	 *            Objekt Nr.1
	 * @param obj2
	 *            Objekt Nr.2
	 * @return <code>true</code>, wenn beide Objekte identisch oder beide
	 *         <code>null</code> sind
	 */
	public static final boolean objectEquals(final Object obj1,
			final Object obj2) {
		boolean eq = false;

		if ((obj1 == null) && (obj2 == null)) {
			eq = true;
		} else if ((obj1 != null) && (obj2 != null)) {
			eq = obj1.equals(obj2);
		}

		return eq;
	}

	/**
	 * Test von isKombinationOk().
	 */
	@Test
	public void testIsKombinationOk() {
		final SystemObject aObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.fahrStreifen");
		final DataDescription aDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.externeErfassung"));
		final boolean aSoll = false;

		final SystemObject bObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("atg.verkehrsDatenLangZeitIntervall");
		final DataDescription bDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.info"), null);
		final boolean bSoll = false;

		final SystemObject cObj = null;
		final DataDescription cDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.externeErfassung"));
		final boolean cSoll = false;

		final SystemObject dObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription dDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.externeErfassung"));
		final boolean dSoll = false;

		final SystemObject eObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription eDataDesc = new DataDescription(null,
				DUAUtensilienTest.verbindung.getDataModel().getAspect(
						"asp.externeErfassung"));
		final boolean eSoll = false;

		final SystemObject fObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription fDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"), null);
		final boolean fSoll = false;

		final SystemObject gObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription gDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.testPlPrüfungFormal"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.externeErfassung"));
		final boolean gSoll = false;

		final SystemObject hObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription hDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.testEingang"));
		final boolean hSoll = false;

		final SystemObject iObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription iDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.XXX"), DUAUtensilienTest.verbindung.getDataModel()
						.getAspect("asp.externeErfassung"));
		final boolean iSoll = false;

		final SystemObject jObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("objekt1.testPlPrüfungFormal");
		final DataDescription jDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.testPlPrüfungFormal"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.testEingang"));
		final boolean jSoll = true;

		final SystemObject kObj = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.testPlPrüfungFormal");
		final DataDescription kDataDesc = new DataDescription(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.testPlPrüfungFormal"),
						DUAUtensilienTest.verbindung.getDataModel().getAspect(
								"asp.testEingang"));
		final boolean kSoll = false;

		Assert.assertTrue(
				"A:",
				(DUAUtensilien.isKombinationOk(aObj, aDataDesc) == null) == aSoll);
		Assert.assertTrue(
				"B:",
				(DUAUtensilien.isKombinationOk(bObj, bDataDesc) == null) == bSoll);
		Assert.assertTrue(
				"C:",
				(DUAUtensilien.isKombinationOk(cObj, cDataDesc) == null) == cSoll);
		Assert.assertTrue(
				"D:",
				(DUAUtensilien.isKombinationOk(dObj, dDataDesc) == null) == dSoll);
		Assert.assertTrue(
				"E:",
				(DUAUtensilien.isKombinationOk(eObj, eDataDesc) == null) == eSoll);
		Assert.assertTrue(
				"F:",
				(DUAUtensilien.isKombinationOk(fObj, fDataDesc) == null) == fSoll);
		Assert.assertTrue(
				"G:",
				(DUAUtensilien.isKombinationOk(gObj, gDataDesc) == null) == gSoll);
		Assert.assertTrue(
				"H:",
				(DUAUtensilien.isKombinationOk(hObj, hDataDesc) == null) == hSoll);
		Assert.assertTrue(
				"I:",
				(DUAUtensilien.isKombinationOk(iObj, iDataDesc) == null) == iSoll);
		Assert.assertTrue(
				"J:",
				(DUAUtensilien.isKombinationOk(jObj, jDataDesc) == null) == jSoll);
		Assert.assertTrue(
				"K:",
				(DUAUtensilien.isKombinationOk(kObj, kDataDesc) == null) == kSoll);
	}

	/**
	 * Testet verschiedene Varianten der Ersetzung des letzten Attributs in
	 * einem Attributpfad.
	 */
	@Test
	public void testErsetzeLetztesElemInAttPfad() {
		final String aVorher = "", aNachher = null, aErsetz = "hallo";
		final String bVorher = "a", bNachher = "hallo", bErsetz = "hallo";
		final String cVorher = "a.a", cNachher = "a.hallo", cErsetz = "hallo";
		final String dVorher = "", dNachher = null, dErsetz = "h";
		final String eVorher = "a", eNachher = "h", eErsetz = "h";
		final String fVorher = "a.a", fNachher = "a.h", fErsetz = "h";
		final String gVorher = "a.a", gNachher = null, gErsetz = "";
		final String hVorher = "a.a", hNachher = null, hErsetz = null;

		Assert.assertEquals("A:", aNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(aVorher, aErsetz));
		Assert.assertEquals("B:", bNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(bVorher, bErsetz));
		Assert.assertEquals("C:", cNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(cVorher, cErsetz));
		Assert.assertEquals("D:", dNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(dVorher, dErsetz));
		Assert.assertEquals("E:", eNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(eVorher, eErsetz));
		Assert.assertEquals("F:", fNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(fVorher, fErsetz));
		Assert.assertEquals("G:", gNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(gVorher, gErsetz));
		Assert.assertEquals("H:", hNachher,
				DUAUtensilien.ersetzeLetztesElemInAttPfad(hVorher, hErsetz));

	}

	/**
	 * Testet, ob bestimmte Eingabevarianten für den Attributpfad zum richtigen
	 * Ergebnis führen.
	 */
	@Test
	public void testGetAttributDatum() {
		final Data testDatum = DUAUtensilienTest.verbindung
				.createData(DUAUtensilienTest.verbindung.getDataModel()
						.getAttributeGroup(PPFKonstanten.ATG));
		final Data.Array ps = testDatum.getItem(
				"ParameterSatzPlausibilitätsPrüfungFormal").asArray();
		ps.setLength(2);
		final Data ps0 = ps.getItem(0);
		final Data ps1 = ps.getItem(1);
		final String[] pfade = new String[30];
		final String[] soll = new String[30];

		pfade[0] = "ParameterSatzPlausibilitätsPrüfungFormal.0.Attributgruppe";
		soll[0] = DUAUtensilienTest.verbindung.getDataModel()
				.getAttributeGroup("atg.verkehrsDatenKurzZeitIntervall")
				.toString();
		ps0.getReferenceValue("Attributgruppe").setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenKurzZeitIntervall"));

		pfade[1] = "ParameterSatzPlausibilitätsPrüfungFormal.0.Aspekt";
		soll[1] = DUAUtensilienTest.verbindung.getDataModel()
				.getAspect("asp.externeErfassung").toString();
		ps0.getReferenceValue("Aspekt").setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getAspect(
						"asp.externeErfassung"));

		pfade[2] = "ParameterSatzPlausibilitätsPrüfungFormal.0.Objekt.0";
		soll[2] = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.fahrStreifen").toString();
		final Data.Array objekte = ps0.getArray("Objekt");
		objekte.setLength(1);
		objekte.getItem(0)
		.asReferenceValue()
		.setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getObject(
						"typ.fahrStreifen"));
		final Data.Array attribut = ps0.getArray("AttributSpezifikation");

		pfade[3] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.AttributPfad";
		soll[3] = "a11";
		attribut.setLength(2);
		final Data attSpez1 = attribut.getItem(0);
		attSpez1.getTextValue("AttributPfad").setText("a11");

		pfade[4] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Min";
		soll[4] = "10";
		attSpez1.getUnscaledValue("Min").set(10);

		pfade[5] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Max";
		soll[5] = "20";
		attSpez1.getUnscaledValue("Max").set(20);

		pfade[6] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Optionen";
		soll[6] = "0";
		attSpez1.getUnscaledValue("Optionen").set(0);

		final Data attSpez2 = attribut.getItem(1);
		pfade[7] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.AttributPfad";
		soll[7] = "a12";
		attSpez2.getTextValue("AttributPfad").setText("a12");

		pfade[8] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Min";
		soll[8] = "30";
		attSpez2.getUnscaledValue("Min").set(30);

		pfade[9] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Max";
		soll[9] = "40";
		attSpez2.getUnscaledValue("Max").set(40);

		pfade[10] = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Optionen";
		soll[10] = "1";
		attSpez2.getUnscaledValue("Optionen").set(1);

		pfade[11] = "ParameterSatzPlausibilitätsPrüfungFormal.1.Attributgruppe";
		soll[11] = DUAUtensilienTest.verbindung.getDataModel()
				.getAttributeGroup("atg.verkehrsDatenLangZeitIntervall")
				.toString();
		ps1.getReferenceValue("Attributgruppe").setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenLangZeitIntervall"));

		pfade[12] = "ParameterSatzPlausibilitätsPrüfungFormal.1.Aspekt";
		soll[12] = DUAUtensilienTest.verbindung.getDataModel()
				.getAspect("asp.messWertErsetzung").toString();
		ps1.getReferenceValue("Aspekt").setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getAspect(
						"asp.messWertErsetzung"));

		pfade[13] = "ParameterSatzPlausibilitätsPrüfungFormal.1.Objekt.0";
		soll[13] = DUAUtensilienTest.verbindung.getDataModel()
				.getObject("typ.fahrStreifenLangZeit").toString();
		final Data.Array objekte1 = ps1.getArray("Objekt");
		objekte1.setLength(1);
		objekte1.getItem(0)
		.asReferenceValue()
		.setSystemObject(
				DUAUtensilienTest.verbindung.getDataModel().getObject(
						"typ.fahrStreifenLangZeit"));
		final Data.Array attribut1 = ps1.getArray("AttributSpezifikation");

		pfade[14] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.AttributPfad";
		soll[14] = "a21";
		attribut1.setLength(2);
		final Data attSpez3 = attribut1.getItem(0);
		attSpez3.getTextValue("AttributPfad").setText("a21");

		pfade[15] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Min";
		soll[15] = "50";
		attSpez3.getUnscaledValue("Min").set(50);

		pfade[16] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Max";
		soll[16] = "60";
		attSpez3.getUnscaledValue("Max").set(60);

		pfade[17] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Optionen";
		soll[17] = "2";
		attSpez3.getUnscaledValue("Optionen").set(2);

		final Data attSpez4 = attribut1.getItem(1);
		pfade[18] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.AttributPfad";
		soll[18] = "a22";
		attSpez4.getTextValue("AttributPfad").setText("a22");

		pfade[19] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Min";
		soll[19] = "70";
		attSpez4.getUnscaledValue("Min").set(70);

		pfade[20] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Max";
		soll[20] = "80";
		attSpez4.getUnscaledValue("Max").set(80);

		pfade[21] = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Optionen";
		soll[21] = "3";
		attSpez4.getUnscaledValue("Optionen").set(3);

		for (int j = 0; j < 22; j++) {
			final Data dummy = DUAUtensilien.getAttributDatum(pfade[j],
					testDatum);
			String ist = dummy.valueToString();

			if (dummy.getAttributeType() instanceof ReferenceAttributeType) {
				ist = dummy.asReferenceValue().getSystemObject().toString();
			}
			if ((dummy.getAttributeType() instanceof IntegerAttributeType)
					&& dummy.asUnscaledValue().isState()) {
				ist = new Long(dummy.asUnscaledValue().longValue()).toString();
			}

			System.out.println("Überprüfe: " + pfade[j]);
			Assert.assertEquals("Fehler in " + pfade[j], ist, soll[j]);
		}
	}

}
