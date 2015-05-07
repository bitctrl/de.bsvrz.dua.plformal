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

package de.bsvrz.dua.plformal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dua.plformal.DAVTest;
import de.bsvrz.dua.plformal.plformal.PPFKonstanten;
import de.bsvrz.dua.plformal.plformal.typen.PlausibilisierungsMethode;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;

/**
 * Diese Applikation testet die SWE 4.1 (PL-Prüfung formal) nach den Vorgaben
 * der Prüfspezifikation (PID: QS-02.04.00.00.00-PrSpez-2.0) bzw.
 * Änderungsantrag (PID: AeA_SWE4.1_VRZ3_Nr001)
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
@Ignore("Datenverteilerverbindung prüfen")
public class PlPruefungFormalTest implements ClientSenderInterface, ClientReceiverInterface {

	/**
	 * Kumulation der verschiedenen Kennungen in einen Wert. Da die Kennungen
	 * nach der Plausibilisierung nicht als ein Wert sondern als drei (boolsche)
	 * Werte zur Verfügung stehen muss hier eine Übersetzung dieser Werte
	 * vorgenommen werden. Dabei gilt:
	 *
	 * kennung[0] = Attribut --&gt; Status.PlFormal.WertMin kennung[1] =
	 * Attribut --&gt; Status.PlFormal.WertMax kennung[2] = Attribut --&gt;
	 * Status.MessWertErsetzung.Implausibel
	 *
	 */
	private static final boolean[] WERT_MIN = new boolean[] { true, false, false };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] WERT_MAX = new boolean[] { false, true, false };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] IMPLAUSIBEL = new boolean[] { false, false, true };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] NICHTS = new boolean[] { false, false, false };

	/**
	 * Pid von TestObj1.
	 */
	private static final String OBJ1_PID = "objekt1.testPlPrüfungFormal";

	/**
	 * Pid von TestObj2.
	 */
	private static final String OBJ2_PID = "objekt2.testPlPrüfungFormal";

	/**
	 * Pid von Test-Atg.
	 */
	private static final String ATG_PID = "atg.testPlPrüfungFormal";

	/**
	 * Pid von Test-Empfangs-Aspekt.
	 */
	private static final String ASP_EINGANG_PID = "asp.testEingang";

	/**
	 * Pid von Test-Sende-Aspekt.
	 */
	private static final String ASP_AUSGANG_PID = "asp.testAusgang";

	/**
	 * Pid von Test-Objekt-Typ.
	 */
	private static final String TYP = "typ.testPlPrüfungFormal";

	/**
	 * Verbindung zum Datenverteiler.
	 */
	private ClientDavInterface dav;

	/**
	 * Instanz von TestObj1.
	 */
	private SystemObject obj1;

	/**
	 * Instanz von TestObj1.
	 */
	private SystemObject obj2;

	/**
	 * Datenbeschreibung für zu sendende Daten.
	 */
	private DataDescription ddAusgang;

	/**
	 * Datenbeschreibung für zu empfangene Daten.
	 */
	private DataDescription ddEingang;

	/**
	 * Parameter der Pl-Prüfung (Vorgabe).
	 */
	private DataDescription ddParamVor;

	/**
	 * Parameter der Pl-Prüfung (Soll).
	 */
	private DataDescription ddParamSoll;

	/**
	 * Testdatensätze.
	 */
	private RohdatenSatz[] testDatenSaetze;

	/**
	 * Durchläufe.
	 */
	private Durchlauf[] durchlaeufe;

	/**
	 * Erwartete Ergebnisse.
	 */
	private TestErgebnis[][] ergebnisse;

	/**
	 * das aktuelle Ergebnis eines Tests.
	 */
	private TestErgebnis aktuellesErgebnis;

	/**
	 * zu dieser Zeit wurde das letzte Testergebnis empfangen.
	 */
	private long ergebnisZeit;

	/**
	 * zu dieser Zeit wurde der letzte Parameter empfangen.
	 */
	private long paraZeit;

	/**
	 * aktuelle Testparameter.
	 */
	private ParameterSatz[] parameter;

	/**
	 * Das Objekt der PL-Prüfung formal.
	 */
	private SystemObject ppfObjekt;

	/**
	 * Setzt die Datenflusssteuerung auf die Parameter, wie innerhalb der
	 * Pruefspezifikation dokumentiert ist.
	 *
	 * @param dav1
	 *            Verbindung zum Datenverteiler
	 */
	public final void setzeDfsParameter(final ClientDavInterface dav1) {
		final DataDescription datenBeschreibung = new DataDescription(
				dav1.getDataModel().getAttributeGroup("atg.datenflussSteuerung"),
				dav1.getDataModel().getAspect("asp.parameterVorgabe"));

		final ClientSenderInterface parameterSender = new ClientSenderInterface() {

			@Override
			public void dataRequest(final SystemObject object, final DataDescription dataDescription,
					final byte state) {
				//
			}

			@Override
			public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
				return false;
			}

		};

		try {
			dav1.subscribeSender(parameterSender,
					dav1.getDataModel().getType("typ.datenflussSteuerung").getElements().get(0), datenBeschreibung,
					SenderRole.sender());
			try {
				Thread.sleep(10000L);
			} catch (final InterruptedException ex) {
				//
			}
		} catch (final OneSubscriptionPerSendData e) {
			throw new RuntimeException(e);
		}

		final Data atgData = dav1.createData(datenBeschreibung.getAttributeGroup());

		atgData.getItem("Urlasser").getReferenceValue("BenutzerReferenz").setSystemObject(null);
		atgData.getItem("Urlasser").getTextValue("Ursache").setText("");
		atgData.getItem("Urlasser").getTextValue("Veranlasser").setText("");

		atgData.getArray("ParameterSatz").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getUnscaledValue("SWE").setText("SWE_PL_Prüfung_formal");
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getUnscaledValue("ModulTyp").setText("PlPrüfungFormal");
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getReferenceValue("PublikationsAspekt")
		.setSystemObject(dav1.getDataModel().getAspect("asp.testEingang"));
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getReferenceArray("Objekt").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getReferenceArray("Objekt").getReferenceValue(0)
		.setSystemObject(dav1.getDataModel().getType("typ.testPlPrüfungFormal"));
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getReferenceArray("AttributGruppe").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getReferenceArray("AttributGruppe").getReferenceValue(0)
		.setSystemObject(dav1.getDataModel().getObject("atg.testPlPrüfungFormal"));
		atgData.getArray("ParameterSatz").getItem(0).getArray("PublikationsZuordnung").getItem(0)
		.getUnscaledValue("Publizieren").set(DUAKonstanten.JA);

		final ResultData resultat = new ResultData(
				dav1.getDataModel().getType("typ.datenflussSteuerung").getElements().get(0), datenBeschreibung,
				System.currentTimeMillis(), atgData);

		try {
			dav1.sendData(resultat);
		} catch (final DataNotSubscribedException e) {
			throw new RuntimeException(e);
		} catch (final SendSubscriptionNotConfirmed e) {
			throw new RuntimeException(e);
		}

	}

	@Before
	public void setUp() throws Exception {
		dav = DAVTest.getDav();

		DUAUtensilien.setAlleParameter(dav);

		setzeDfsParameter(dav);

		final AttributeGroup atg = this.dav.getDataModel().getAttributeGroup(PlPruefungFormalTest.ATG_PID);
		final Aspect eingang = this.dav.getDataModel().getAspect(PlPruefungFormalTest.ASP_EINGANG_PID);
		final Aspect ausgang = this.dav.getDataModel().getAspect(PlPruefungFormalTest.ASP_AUSGANG_PID);
		final SystemObjectType typ = this.dav.getDataModel().getType(PlPruefungFormalTest.TYP);

		final AttributeGroup atgPara = this.dav.getDataModel().getAttributeGroup(PPFKonstanten.ATG);
		final Aspect aspParaVor = this.dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE);
		final Aspect aspParaSoll = this.dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);

		ddAusgang = new DataDescription(atg, ausgang);
		ddEingang = new DataDescription(atg, eingang);
		ddParamVor = new DataDescription(atgPara, aspParaVor);
		ddParamSoll = new DataDescription(atgPara, aspParaSoll);
		this.obj1 = this.dav.getDataModel().getObject(PlPruefungFormalTest.OBJ1_PID);
		this.obj2 = this.dav.getDataModel().getObject(PlPruefungFormalTest.OBJ2_PID);

		for (final SystemObject elem : this.dav.getDataModel().getType(PPFKonstanten.TYP).getElements()) {
			System.out.println(elem);
			if ("ppfTest".equals(elem.getPid())) {
				this.ppfObjekt = elem;
			}
		}

		/**
		 * Daten zum Senden anmelden
		 */
		dav.subscribeSender(this, this.ppfObjekt, ddParamVor, SenderRole.sender());
		dav.subscribeSender(this, typ.getObjects(), ddAusgang, SenderRole.source());

		/**
		 * Auf Empfang von Daten/Parametern anmelden
		 */
		dav.subscribeReceiver(this, typ.getObjects(), ddEingang, ReceiveOptions.normal(), ReceiverRole.receiver());
		dav.subscribeReceiver(this, this.ppfObjekt, ddParamSoll, ReceiveOptions.normal(), ReceiverRole.receiver());

		try {
			Thread.sleep(1000L);
		} catch (final InterruptedException e) { //

		}

		/**
		 * Testdatensätze wie in Tabelle 5-3 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.testDatenSaetze = new RohdatenSatz[] { new RohdatenSatz(obj1, -4, -2.00000901),
				new RohdatenSatz(obj1, -3, -2.0), new RohdatenSatz(obj1, 17, 73.1087001),
				new RohdatenSatz(obj1, 18, 73.0), new RohdatenSatz(obj1, -2, 13), new RohdatenSatz(obj2, -2, 13),
				new RohdatenSatz(obj1, 5, 67), new RohdatenSatz(obj2, 5, 67), };

		/**
		 * Testparameter wie in Tabelle 5-2 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.parameter = new ParameterSatz[] { new ParameterSatz(this.obj1, -3, 17, -2, 73),
				new ParameterSatz(this.obj2, -251, -2, -2, 63) };

		/**
		 * Durchläufe wie in Tabelle 5-4
		 */
		this.durchlaeufe = new Durchlauf[] {
				new Durchlauf(PlausibilisierungsMethode.KEINE_PRUEFUNG.getCode(),
						PlausibilisierungsMethode.SETZE_MIN.getCode()),
						new Durchlauf(PlausibilisierungsMethode.SETZE_MIN_MAX.getCode(),
								PlausibilisierungsMethode.NUR_PRUEFUNG.getCode()),
								new Durchlauf(PlausibilisierungsMethode.SETZE_MAX.getCode(),
										PlausibilisierungsMethode.KEINE_PRUEFUNG.getCode()),
										new Durchlauf(PlausibilisierungsMethode.SETZE_MIN.getCode(),
												PlausibilisierungsMethode.SETZE_MIN_MAX.getCode()),
												new Durchlauf(PlausibilisierungsMethode.NUR_PRUEFUNG.getCode(),
														PlausibilisierungsMethode.SETZE_MAX.getCode()) };

		/**
		 * Erwartete Ergebnisse
		 */
		this.ergebnisse = new TestErgebnis[][] {
				new TestErgebnis[] {
						new TestErgebnis(-4, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.WERT_MIN),
						new TestErgebnis(-3, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(17, PlPruefungFormalTest.NICHTS, 73.1087001, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(18, PlPruefungFormalTest.NICHTS, 73.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS) },
						new TestErgebnis[] {
						new TestErgebnis(-3, PlPruefungFormalTest.WERT_MIN, -2.00000901,
								PlPruefungFormalTest.IMPLAUSIBEL),
								new TestErgebnis(-3, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(17, PlPruefungFormalTest.NICHTS, 73.1087001, PlPruefungFormalTest.IMPLAUSIBEL),
								new TestErgebnis(17, PlPruefungFormalTest.WERT_MAX, 73.0, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-2, PlPruefungFormalTest.WERT_MAX, 67, PlPruefungFormalTest.IMPLAUSIBEL) },
								new TestErgebnis[] {
						new TestErgebnis(-4, PlPruefungFormalTest.NICHTS, -2.00000901, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-3, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(17, PlPruefungFormalTest.NICHTS, 73.1087001, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(17, PlPruefungFormalTest.WERT_MAX, 73.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.WERT_MAX, 67, PlPruefungFormalTest.NICHTS) },
						new TestErgebnis[] {
						new TestErgebnis(-3, PlPruefungFormalTest.WERT_MIN, -2.0, PlPruefungFormalTest.WERT_MIN),
						new TestErgebnis(-3, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(17, PlPruefungFormalTest.NICHTS, 73.0, PlPruefungFormalTest.WERT_MAX),
						new TestErgebnis(18, PlPruefungFormalTest.NICHTS, 73.0, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS),
						new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 63, PlPruefungFormalTest.WERT_MAX) },
						new TestErgebnis[] {
						new TestErgebnis(-4, PlPruefungFormalTest.IMPLAUSIBEL, -2.00000901,
								PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-3, PlPruefungFormalTest.NICHTS, -2.0, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(17, PlPruefungFormalTest.NICHTS, 73.0, PlPruefungFormalTest.WERT_MAX),
								new TestErgebnis(18, PlPruefungFormalTest.IMPLAUSIBEL, 73.0, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(-2, PlPruefungFormalTest.NICHTS, 13, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(5, PlPruefungFormalTest.NICHTS, 67, PlPruefungFormalTest.NICHTS),
								new TestErgebnis(5, PlPruefungFormalTest.IMPLAUSIBEL, 63, PlPruefungFormalTest.WERT_MAX) } };
	}

	@Override
	public void dataRequest(final SystemObject object, final DataDescription dataDescription, final byte state) {
		// mache nichts
	}

	@Override
	public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
		return false;
	}

	/**
	 * Alle Testfälle durchführen und Auswertung anzeigen.
	 */
	@Test
	public void testeAlles() {
		for (int iDurchlauf = 0; iDurchlauf < 5; iDurchlauf++) {
			for (int iDatensatz = 0; iDatensatz < 8; iDatensatz++) {
				final TestErgebnis soll = this.ergebnisse[iDurchlauf][iDatensatz];

				final TestErgebnis ist = this.testLauf(this.durchlaeufe[iDurchlauf], this.testDatenSaetze[iDatensatz]);

				System.out.println("Test " + (iDurchlauf + 1) + "/" + (iDatensatz + 1) + ":");
				System.out.println("Soll: " + soll);
				System.out.println("Ist : " + ist);
				System.out
				.println("Ergebnis: ---" + (soll.equals(ist) ? "erfolgreich" : "nicht erfolgreich") + "---\n");

				Assert.assertEquals(soll, ist);
			}
		}
	}

	/**
	 * Führt einen Testlauf durch und produziert eine Ergebniszeile analog der
	 * Tabelle 5-5.
	 *
	 * @param dl
	 *            ein Durchlauf
	 * @param ds
	 *            ein Rohdatensatz
	 * @return das Ergebnis der Prüfung
	 */
	private TestErgebnis testLauf(final Durchlauf dl, final RohdatenSatz ds) {
		/**
		 * Parameter setzen
		 */
		final Data data = this.dav.createData(this.dav.getDataModel().getAttributeGroup(PPFKonstanten.ATG));
		final Data.Array ps = data.getItem("ParameterSatzPlausibilitätsPrüfungFormal").asArray();
		ps.setLength(2);

		final Data ps0 = ps.getItem(0);
		ps0.getReferenceValue("Attributgruppe")
		.setSystemObject(this.dav.getDataModel().getAttributeGroup(PlPruefungFormalTest.ATG_PID));
		ps0.getReferenceValue("Aspekt")
		.setSystemObject(this.dav.getDataModel().getAspect(PlPruefungFormalTest.ASP_AUSGANG_PID));
		final Data.Array objekte = ps0.getArray("Objekt");
		objekte.setLength(1);
		objekte.getItem(0).asReferenceValue().setSystemObject(this.parameter[0].obj);
		final Data.Array attribut = ps0.getArray("AttributSpezifikation");
		attribut.setLength(2);
		final Data attSpez1 = attribut.getItem(0);
		attSpez1.getTextValue("AttributPfad").setText("Attribut1.Wert");
		attSpez1.getUnscaledValue("Min").set(this.parameter[0].min1);
		attSpez1.getUnscaledValue("Max").set(this.parameter[0].max1);
		attSpez1.getUnscaledValue("Optionen").set(dl.testAtt1);
		final Data attSpez2 = attribut.getItem(1);
		attSpez2.getTextValue("AttributPfad").setText("Attribut2.Wert");
		attSpez2.getUnscaledValue("Min").set(this.parameter[0].min2);
		attSpez2.getUnscaledValue("Max").set(this.parameter[0].max2);
		attSpez2.getUnscaledValue("Optionen").set(dl.testAtt2);

		final Data ps1 = ps.getItem(1);
		ps1.getReferenceValue("Attributgruppe")
		.setSystemObject(this.dav.getDataModel().getAttributeGroup(PlPruefungFormalTest.ATG_PID));
		ps1.getReferenceValue("Aspekt")
		.setSystemObject(this.dav.getDataModel().getAspect(PlPruefungFormalTest.ASP_AUSGANG_PID));
		final Data.Array objekte1 = ps1.getArray("Objekt");
		objekte1.setLength(1);
		objekte1.getItem(0).asReferenceValue().setSystemObject(this.parameter[1].obj);
		final Data.Array attribut1 = ps1.getArray("AttributSpezifikation");
		attribut1.setLength(2);
		final Data attSpez3 = attribut1.getItem(0);
		attSpez3.getTextValue("AttributPfad").setText("Attribut1.Wert");
		attSpez3.getUnscaledValue("Min").set(this.parameter[1].min1);
		attSpez3.getUnscaledValue("Max").set(this.parameter[1].max1);
		attSpez3.getUnscaledValue("Optionen").set(dl.testAtt1);
		final Data attSpez4 = attribut1.getItem(1);
		attSpez4.getTextValue("AttributPfad").setText("Attribut2.Wert");
		attSpez4.getUnscaledValue("Min").set(this.parameter[1].min2);
		attSpez4.getUnscaledValue("Max").set(this.parameter[1].max2);
		attSpez4.getUnscaledValue("Optionen").set(dl.testAtt2);
		final ResultData parameter1 = new ResultData(this.ppfObjekt, this.ddParamVor, System.currentTimeMillis(), data);

		System.out.println("Setze PL-Parameter für " + this.parameter[0]);
		System.out.println("Setze PL-Parameter für " + this.parameter[1]);
		System.out.println("Methode für Obj1: " + PlausibilisierungsMethode.getZustand((int) dl.testAtt1)
		+ ", Methode für Obj2: " + PlausibilisierungsMethode.getZustand((int) dl.testAtt2));

		long letzteZeit = paraZeit;
		try {
			this.dav.sendData(parameter1);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		/**
		 * Warten bis die Parameter über den Datenverteiler angekommen ist
		 */
		int timeout = 100;
		while (this.paraZeit <= letzteZeit) {
			if (timeout-- == 0) {
				break;
			}
			try {
				Thread.sleep(50L);
			} catch (final InterruptedException ex) {
				// wird vernachlässigt
			}
		}

		aktuellesErgebnis = null;
		letzteZeit = ergebnisZeit;
		System.out.println("Sende: " + ds);
		sendeDatum(ds.obj, ds.att1, ds.att2);

		/**
		 * Warten bis das Ergebnis über den Datenverteiler angekommen ist
		 */
		timeout = 100;
		while (this.ergebnisZeit <= letzteZeit) {
			if (timeout-- == 0) {
				break;
			}
			try {
				Thread.sleep(50L);
			} catch (final InterruptedException ex) {
				// wird vernachlässigt
			}
		}

		return aktuellesErgebnis;
	}

	/**
	 * Sendet ein Datum für ein bestimmtes Objekt an den Datenverteiler.
	 *
	 * @param obj
	 *            Das Objekt
	 * @param att1
	 *            das Attribut 1
	 * @param att2
	 *            das Attribut 2
	 */
	private void sendeDatum(final SystemObject obj, final long att1, final double att2) {
		final Data data = this.dav.createData(this.dav.getDataModel().getAttributeGroup(PlPruefungFormalTest.ATG_PID));
		data.getItem("Attribut1").getUnscaledValue("Wert").set(att1);
		data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getItem("WertMin").asUnscaledValue().set(0);
		data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getItem("WertMax").asUnscaledValue().set(0);
		data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getItem("Implausibel")
		.asUnscaledValue().set(0);
		data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getItem("Interpoliert")
		.asUnscaledValue().set(0);
		data.getItem("Attribut2").getUnscaledValue("Wert").set(att2);
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getItem("WertMin").asUnscaledValue().set(0);
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getItem("WertMax").asUnscaledValue().set(0);
		data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getItem("Implausibel")
		.asUnscaledValue().set(0);
		data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getItem("Interpoliert")
		.asUnscaledValue().set(0);

		final ResultData resultat = new ResultData(obj, ddAusgang, System.currentTimeMillis(), data);
		try {
			this.dav.sendData(resultat);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(final ResultData[] resultate) {
		if (resultate != null) {
			for (final ResultData resultat : resultate) {
				if ((resultat != null) && (resultat.getData() != null)) {
					if (resultat.getDataDescription().equals(ddParamSoll)) {
						this.paraZeit = resultat.getDataTime();
					} else {
						final Data data = resultat.getData();

						final long wert1 = data.getItem("Attribut1").getUnscaledValue("Wert").longValue();
						final boolean[] kennung1 = new boolean[3];
						kennung1[0] = data.getItem("Attribut1").getItem("Status").getItem("PlFormal")
								.getUnscaledValue("WertMin").longValue() == 1;
						kennung1[1] = data.getItem("Attribut1").getItem("Status").getItem("PlFormal")
								.getUnscaledValue("WertMax").longValue() == 1;
						kennung1[2] = data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung")
								.getUnscaledValue("Implausibel").longValue() == 1;

						final double wert2 = data.getItem("Attribut2").getUnscaledValue("Wert").doubleValue();
						final boolean[] kennung2 = new boolean[3];
						kennung2[0] = data.getItem("Attribut2").getItem("Status").getItem("PlFormal")
								.getUnscaledValue("WertMin").longValue() == 1;
						kennung2[1] = data.getItem("Attribut2").getItem("Status").getItem("PlFormal")
								.getUnscaledValue("WertMax").longValue() == 1;
						kennung2[2] = data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung")
								.getUnscaledValue("Implausibel").longValue() == 1;

						this.ergebnisZeit = resultat.getDataTime();
						this.aktuellesErgebnis = new TestErgebnis(wert1, kennung1, wert2, kennung2);
					}
				}
			}
		}
	}
}
