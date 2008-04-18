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

package de.bsvrz.dua.plformal.test;

import org.junit.Assert;
import org.junit.Before;
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
public class PlPruefungFormalTest implements ClientSenderInterface,
		ClientReceiverInterface {

	/**
	 * Kumulation der verschiedenen Kennungen in einen Wert. Da die Kennungen
	 * nach der Plausibilisierung nicht als ein Wert sondern als drei (boolsche)
	 * Werte zur Verfügung stehen muss hier eine Übersetzung dieser Werte
	 * vorgenommen werden. Dabei gilt:
	 * 
	 * kennung[0] = Attribut --> Status.PlFormal.WertMin kennung[1] = Attribut
	 * --> Status.PlFormal.WertMax kennung[2] = Attribut -->
	 * Status.MessWertErsetzung.Implausibel
	 * 
	 */
	private static final boolean[] WERT_MIN = new boolean[] { true, false,
			false };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] WERT_MAX = new boolean[] { false, true,
			false };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] IMPLAUSIBEL = new boolean[] { false, false,
			true };

	/**
	 * siehe WERT_MIN.
	 */
	private static final boolean[] NICHTS = new boolean[] { false, false, false };

	/**
	 * Pid von TestObj1.
	 */
	private static final String OBJ1_PID = "objekt1.testPlPrüfungFormal"; //$NON-NLS-1$

	/**
	 * Pid von TestObj2.
	 */
	private static final String OBJ2_PID = "objekt2.testPlPrüfungFormal"; //$NON-NLS-1$

	/**
	 * Pid von Test-Atg.
	 */
	private static final String ATG_PID = "atg.testPlPrüfungFormal"; //$NON-NLS-1$

	/**
	 * Pid von Test-Empfangs-Aspekt.
	 */
	private static final String ASP_EINGANG_PID = "asp.testEingang"; //$NON-NLS-1$

	/**
	 * Pid von Test-Sende-Aspekt.
	 */
	private static final String ASP_AUSGANG_PID = "asp.testAusgang"; //$NON-NLS-1$

	/**
	 * Pid von Test-Objekt-Typ.
	 */
	private static final String TYP = "typ.testPlPrüfungFormal"; //$NON-NLS-1$

	/**
	 * Verbindung zum Datenverteiler.
	 */
	private ClientDavInterface dav;

	/**
	 * Instanz von TestObj1.
	 */
	private SystemObject obj1 = null;

	/**
	 * Instanz von TestObj1.
	 */
	private SystemObject obj2 = null;

	/**
	 * Datenbeschreibung für zu sendende Daten.
	 */
	DataDescription ddAusgang = null;

	/**
	 * Datenbeschreibung für zu empfangene Daten.
	 */
	DataDescription ddEingang = null;

	/**
	 * Parameter der Pl-Prüfung (Vorgabe).
	 */
	DataDescription ddParamVor = null;

	/**
	 * Parameter der Pl-Prüfung (Soll).
	 */
	DataDescription ddParamSoll = null;

	/**
	 * Testdatensätze.
	 */
	private RohdatenSatz[] testDatenSaetze = null;

	/**
	 * Durchläufe.
	 */
	private Durchlauf[] durchlaeufe = null;

	/**
	 * Erwartete Ergebnisse.
	 */
	private TestErgebnis[][] ergebnisse = null;

	/**
	 * das aktuelle Ergebnis eines Tests.
	 */
	private TestErgebnis aktuellesErgebnis = null;

	/**
	 * zu dieser Zeit wurde das letzte Testergebnis empfangen.
	 */
	private long ergebnisZeit = 0;

	/**
	 * zu dieser Zeit wurde der letzte Parameter empfangen.
	 */
	private long paraZeit = 0;

	/**
	 * aktuelle Testparameter.
	 */
	private ParameterSatz[] parameter = null;

	/**
	 * Das Objekt der PL-Prüfung formal.
	 */
	private SystemObject ppfObjekt = null;

	/**
	 * Setzt die Datenflusssteuerung auf die Parameter, wie innerhalb der
	 * Pruefspezifikation dokumentiert ist.
	 * 
	 * @param dav1
	 *            Verbindung zum Datenverteiler
	 */
	public final void setzeDfsParameter(ClientDavInterface dav1) {
		DataDescription datenBeschreibung = new DataDescription(dav1
				.getDataModel().getAttributeGroup("atg.datenflussSteuerung"),
				dav1.getDataModel().getAspect("asp.parameterVorgabe"));

		ClientSenderInterface parameterSender = new ClientSenderInterface() {

			/**
			 * {@inheritDoc}
			 */
			public void dataRequest(SystemObject object,
					DataDescription dataDescription, byte state) {
				//
			}

			/**
			 * {@inheritDoc}
			 */
			public boolean isRequestSupported(SystemObject object,
					DataDescription dataDescription) {
				return false;
			}

		};

		try {
			dav1.subscribeSender(parameterSender, dav1.getDataModel().getType(
					"typ.datenflussSteuerung").getElements().get(0),
					datenBeschreibung, SenderRole.sender());
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException ex) {
				//
			}
		} catch (OneSubscriptionPerSendData e) {
			throw new RuntimeException(e);
		}

		Data atgData = dav1.createData(datenBeschreibung.getAttributeGroup());

		atgData.getItem("Urlasser").getReferenceValue("BenutzerReferenz")
				.setSystemObject(null);
		atgData.getItem("Urlasser").getTextValue("Ursache").setText("");
		atgData.getItem("Urlasser").getTextValue("Veranlasser").setText("");

		atgData.getArray("ParameterSatz").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getUnscaledValue("SWE")
				.setText("SWE_PL_Prüfung_formal");
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0)
				.getUnscaledValue("ModulTyp").setText("PlPrüfungFormal");
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getReferenceValue(
				"PublikationsAspekt").setSystemObject(
				dav1.getDataModel().getAspect("asp.testEingang"));
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getReferenceArray("Objekt")
				.setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getReferenceArray("Objekt")
				.getReferenceValue(0).setSystemObject(
						dav1.getDataModel().getType("typ.testPlPrüfungFormal"));
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getReferenceArray(
				"AttributGruppe").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getReferenceArray(
				"AttributGruppe").getReferenceValue(0).setSystemObject(
				dav1.getDataModel().getObject("atg.testPlPrüfungFormal"));
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"PublikationsZuordnung").getItem(0).getUnscaledValue(
				"Publizieren").set(DUAKonstanten.JA);

		ResultData resultat = new ResultData(dav1.getDataModel().getType(
		"typ.datenflussSteuerung").getElements().get(0), datenBeschreibung, System
				.currentTimeMillis(), atgData);

		try {
			dav1.sendData(resultat);
		} catch (DataNotSubscribedException e) {
			throw new RuntimeException(e);
		} catch (SendSubscriptionNotConfirmed e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Before
	public void setUp() throws Exception {
		dav = DAVTest.getDav();

		DUAUtensilien.setAlleParameter(dav);

		setzeDfsParameter(dav);

		AttributeGroup atg = this.dav.getDataModel().getAttributeGroup(ATG_PID);
		Aspect eingang = this.dav.getDataModel().getAspect(ASP_EINGANG_PID);
		Aspect ausgang = this.dav.getDataModel().getAspect(ASP_AUSGANG_PID);
		SystemObjectType typ = this.dav.getDataModel().getType(TYP);

		AttributeGroup atgPara = this.dav.getDataModel().getAttributeGroup(
				PPFKonstanten.ATG);
		Aspect aspParaVor = this.dav.getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE);
		Aspect aspParaSoll = this.dav.getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_SOLL);

		ddAusgang = new DataDescription(atg, ausgang, (short) 0);
		ddEingang = new DataDescription(atg, eingang, (short) 0);
		ddParamVor = new DataDescription(atgPara, aspParaVor, (short) 0);
		ddParamSoll = new DataDescription(atgPara, aspParaSoll, (short) 0);
		this.obj1 = this.dav.getDataModel().getObject(OBJ1_PID);
		this.obj2 = this.dav.getDataModel().getObject(OBJ2_PID);

		for (SystemObject elem : this.dav.getDataModel().getType(
				PPFKonstanten.TYP).getElements()) {
			System.out.println(elem);
			if (elem.getPid().equals("ppfTest")) { //$NON-NLS-1$
				this.ppfObjekt = elem;
			}
		}

		/**
		 * Daten zum Senden anmelden
		 */
		dav.subscribeSender(this, this.ppfObjekt, ddParamVor, SenderRole
				.sender());
		dav.subscribeSender(this, typ.getObjects(), ddAusgang, SenderRole
				.source());

		/**
		 * Auf Empfang von Daten/Parametern anmelden
		 */
		dav.subscribeReceiver(this, typ.getObjects(), ddEingang, ReceiveOptions
				.normal(), ReceiverRole.receiver());
		dav.subscribeReceiver(this, this.ppfObjekt, ddParamSoll, ReceiveOptions
				.normal(), ReceiverRole.receiver());

		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) { //

		}

		/**
		 * Testdatensätze wie in Tabelle 5-3 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.testDatenSaetze = new RohdatenSatz[] {
				new RohdatenSatz(obj1, -4, -2.00000901),
				new RohdatenSatz(obj1, -3, -2.0),
				new RohdatenSatz(obj1, 17, 73.1087001),
				new RohdatenSatz(obj1, 18, 73.0),
				new RohdatenSatz(obj1, -2, 13), new RohdatenSatz(obj2, -2, 13),
				new RohdatenSatz(obj1, 5, 67), new RohdatenSatz(obj2, 5, 67), };

		/**
		 * Testparameter wie in Tabelle 5-2 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.parameter = new ParameterSatz[] {
				new ParameterSatz(this.obj1, -3, 17, -2, 73),
				new ParameterSatz(this.obj2, -251, -2, -2, 63) };

		/**
		 * Durchläufe wie in Tabelle 5-4
		 */
		this.durchlaeufe = new Durchlauf[] {
				new Durchlauf(PlausibilisierungsMethode.KEINE_PRUEFUNG
						.getCode(), PlausibilisierungsMethode.SETZE_MIN
						.getCode()),
				new Durchlauf(
						PlausibilisierungsMethode.SETZE_MIN_MAX.getCode(),
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
						new TestErgebnis(-4, NICHTS, -2.0, WERT_MIN),
						new TestErgebnis(-3, NICHTS, -2.0, NICHTS),
						new TestErgebnis(17, NICHTS, 73.1087001, NICHTS),
						new TestErgebnis(18, NICHTS, 73.0, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS) },
				new TestErgebnis[] {
						new TestErgebnis(-3, WERT_MIN, -2.00000901, IMPLAUSIBEL),
						new TestErgebnis(-3, NICHTS, -2.0, NICHTS),
						new TestErgebnis(17, NICHTS, 73.1087001, IMPLAUSIBEL),
						new TestErgebnis(17, WERT_MAX, 73.0, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS),
						new TestErgebnis(-2, WERT_MAX, 67, IMPLAUSIBEL) },
				new TestErgebnis[] {
						new TestErgebnis(-4, NICHTS, -2.00000901, NICHTS),
						new TestErgebnis(-3, NICHTS, -2.0, NICHTS),
						new TestErgebnis(17, NICHTS, 73.1087001, NICHTS),
						new TestErgebnis(17, WERT_MAX, 73.0, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS),
						new TestErgebnis(-2, WERT_MAX, 67, NICHTS) },
				new TestErgebnis[] {
						new TestErgebnis(-3, WERT_MIN, -2.0, WERT_MIN),
						new TestErgebnis(-3, NICHTS, -2.0, NICHTS),
						new TestErgebnis(17, NICHTS, 73.0, WERT_MAX),
						new TestErgebnis(18, NICHTS, 73.0, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS),
						new TestErgebnis(5, NICHTS, 63, WERT_MAX) },
				new TestErgebnis[] {
						new TestErgebnis(-4, IMPLAUSIBEL, -2.00000901, NICHTS),
						new TestErgebnis(-3, NICHTS, -2.0, NICHTS),
						new TestErgebnis(17, NICHTS, 73.0, WERT_MAX),
						new TestErgebnis(18, IMPLAUSIBEL, 73.0, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(-2, NICHTS, 13, NICHTS),
						new TestErgebnis(5, NICHTS, 67, NICHTS),
						new TestErgebnis(5, IMPLAUSIBEL, 63, WERT_MAX) } };
	}

	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		// mache nichts
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		return false;
	}

	/**
	 * Alle Testfälle durchführen und Auswertung anzeigen.
	 */
	@Test
	public void testeAlles() {
		for (int iDurchlauf = 0; iDurchlauf < 5; iDurchlauf++) {
			for (int iDatensatz = 0; iDatensatz < 8; iDatensatz++) {
				TestErgebnis soll = this.ergebnisse[iDurchlauf][iDatensatz];

				TestErgebnis ist = this.testLauf(this.durchlaeufe[iDurchlauf],
						this.testDatenSaetze[iDatensatz]);

				System.out.println("Test " + (iDurchlauf + 1) + "/" //$NON-NLS-1$ //$NON-NLS-2$
						+ (iDatensatz + 1) + ":"); //$NON-NLS-1$
				System.out.println("Soll: " + soll); //$NON-NLS-1$
				System.out.println("Ist : " + ist); //$NON-NLS-1$
				System.out.println("Ergebnis: ---" + (soll.equals(ist) ? //$NON-NLS-1$
				"erfolgreich" //$NON-NLS-1$
						: "nicht erfolgreich") + "---\n"); //$NON-NLS-1$ //$NON-NLS-2$

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
	private TestErgebnis testLauf(Durchlauf dl, RohdatenSatz ds) {
		/**
		 * Parameter setzen
		 */
		Data data = this.dav.createData(this.dav.getDataModel()
				.getAttributeGroup(PPFKonstanten.ATG));
		Data.Array ps = data
				.getItem("ParameterSatzPlausibilitätsPrüfungFormal").asArray(); //$NON-NLS-1$
		ps.setLength(2);

		Data ps0 = ps.getItem(0);
		ps0
				.getReferenceValue("Attributgruppe").setSystemObject(this.dav.getDataModel().getAttributeGroup(ATG_PID)); //$NON-NLS-1$
		ps0
				.getReferenceValue("Aspekt").setSystemObject(this.dav.getDataModel().getAspect(ASP_AUSGANG_PID)); //$NON-NLS-1$
		Data.Array objekte = ps0.getArray("Objekt"); //$NON-NLS-1$
		objekte.setLength(1);
		objekte.getItem(0).asReferenceValue().setSystemObject(
				this.parameter[0].obj);
		Data.Array attribut = ps0.getArray("AttributSpezifikation"); //$NON-NLS-1$
		attribut.setLength(2);
		Data attSpez1 = attribut.getItem(0);
		attSpez1.getTextValue("AttributPfad").setText("Attribut1.Wert"); //$NON-NLS-1$ //$NON-NLS-2$
		attSpez1.getUnscaledValue("Min").set(this.parameter[0].min1); //$NON-NLS-1$
		attSpez1.getUnscaledValue("Max").set(this.parameter[0].max1); //$NON-NLS-1$
		attSpez1.getUnscaledValue("Optionen").set(dl.testAtt1); //$NON-NLS-1$
		Data attSpez2 = attribut.getItem(1);
		attSpez2.getTextValue("AttributPfad").setText("Attribut2.Wert"); //$NON-NLS-1$ //$NON-NLS-2$
		attSpez2.getUnscaledValue("Min").set(this.parameter[0].min2); //$NON-NLS-1$
		attSpez2.getUnscaledValue("Max").set(this.parameter[0].max2); //$NON-NLS-1$
		attSpez2.getUnscaledValue("Optionen").set(dl.testAtt2); //$NON-NLS-1$

		Data ps1 = ps.getItem(1);
		ps1
				.getReferenceValue("Attributgruppe").setSystemObject(this.dav.getDataModel().getAttributeGroup(ATG_PID)); //$NON-NLS-1$
		ps1
				.getReferenceValue("Aspekt").setSystemObject(this.dav.getDataModel().getAspect(ASP_AUSGANG_PID)); //$NON-NLS-1$
		Data.Array objekte1 = ps1.getArray("Objekt"); //$NON-NLS-1$
		objekte1.setLength(1);
		objekte1.getItem(0).asReferenceValue().setSystemObject(
				this.parameter[1].obj);
		Data.Array attribut1 = ps1.getArray("AttributSpezifikation"); //$NON-NLS-1$
		attribut1.setLength(2);
		Data attSpez3 = attribut1.getItem(0);
		attSpez3.getTextValue("AttributPfad").setText("Attribut1.Wert"); //$NON-NLS-1$ //$NON-NLS-2$
		attSpez3.getUnscaledValue("Min").set(this.parameter[1].min1); //$NON-NLS-1$
		attSpez3.getUnscaledValue("Max").set(this.parameter[1].max1); //$NON-NLS-1$
		attSpez3.getUnscaledValue("Optionen").set(dl.testAtt1); //$NON-NLS-1$
		Data attSpez4 = attribut1.getItem(1);
		attSpez4.getTextValue("AttributPfad").setText("Attribut2.Wert"); //$NON-NLS-1$ //$NON-NLS-2$
		attSpez4.getUnscaledValue("Min").set(this.parameter[1].min2); //$NON-NLS-1$
		attSpez4.getUnscaledValue("Max").set(this.parameter[1].max2); //$NON-NLS-1$
		attSpez4.getUnscaledValue("Optionen").set(dl.testAtt2); //$NON-NLS-1$
		ResultData parameter1 = new ResultData(this.ppfObjekt, this.ddParamVor,
				System.currentTimeMillis(), data);

		System.out.println("Setze PL-Parameter für " + this.parameter[0]); //$NON-NLS-1$
		System.out.println("Setze PL-Parameter für " + this.parameter[1]); //$NON-NLS-1$
		System.out
				.println("Methode für Obj1: " + PlausibilisierungsMethode.getZustand((int) dl.testAtt1) //$NON-NLS-1$
						+ ", Methode für Obj2: " + PlausibilisierungsMethode.getZustand((int) dl.testAtt2)); //$NON-NLS-1$

		long letzteZeit = paraZeit;
		try {
			this.dav.sendData(parameter1);
		} catch (Exception e) {
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
			} catch (InterruptedException ex) {
				// wird vernachlässigt
			}
		}

		aktuellesErgebnis = null;
		letzteZeit = ergebnisZeit;
		System.out.println("Sende: " + ds); //$NON-NLS-1$
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
			} catch (InterruptedException ex) {
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
	private void sendeDatum(SystemObject obj, long att1, double att2) {
		Data data = this.dav.createData(this.dav.getDataModel()
				.getAttributeGroup(ATG_PID));
		data.getItem("Attribut1").getUnscaledValue("Wert").set(att1); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("Attribut1").getItem("Status").//$NON-NLS-1$ //$NON-NLS-2$
				getItem("PlFormal").getItem("WertMin").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("Attribut1").getItem("Status").//$NON-NLS-1$ //$NON-NLS-2$
				getItem("PlFormal").getItem("WertMax").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$
		data
				.getItem("Attribut1").getItem("Status").//$NON-NLS-1$ //$NON-NLS-2$
				getItem("MessWertErsetzung").getItem("Implausibel").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$
		data
				.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getItem("Interpoliert").asUnscaledValue().set(0); //$NON-NLS-1$
		data.getItem("Attribut2").getUnscaledValue("Wert").set(att2); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getItem("WertMin").asUnscaledValue().set(0); //$NON-NLS-1$
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getItem("WertMax").asUnscaledValue().set(0); //$NON-NLS-1$
		data
				.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getItem("Implausibel").asUnscaledValue().set(0); //$NON-NLS-1$
		data
				.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				getItem("Interpoliert").asUnscaledValue().set(0); //$NON-NLS-1$

		final ResultData resultat = new ResultData(obj, ddAusgang, System
				.currentTimeMillis(), data);
		try {
			this.dav.sendData(resultat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		if (resultate != null) {
			for (ResultData resultat : resultate) {
				if (resultat != null && resultat.getData() != null) {
					if (resultat.getDataDescription().equals(ddParamSoll)) {
						this.paraZeit = resultat.getDataTime();
					} else {
						Data data = resultat.getData();

						long wert1 = data
								.getItem("Attribut1").getUnscaledValue("Wert").longValue(); //$NON-NLS-1$ //$NON-NLS-2$
						boolean[] kennung1 = new boolean[3];
						kennung1[0] = data
								.getItem("Attribut1").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						kennung1[1] = data
								.getItem("Attribut1").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						kennung1[2] = data
								.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

						double wert2 = data
								.getItem("Attribut2").getUnscaledValue("Wert").doubleValue(); //$NON-NLS-1$ //$NON-NLS-2$ 
						boolean[] kennung2 = new boolean[3];
						kennung2[0] = data
								.getItem("Attribut2").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						kennung2[1] = data
								.getItem("Attribut2").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						kennung2[2] = data
								.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

						this.ergebnisZeit = resultat.getDataTime();
						this.aktuellesErgebnis = new TestErgebnis(wert1,
								kennung1, wert2, kennung2);
					}
				}
			}
		}
	}
}
