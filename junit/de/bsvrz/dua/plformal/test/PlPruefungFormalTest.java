package de.bsvrz.dua.plformal.test;

import java.util.List;

import de.bsvrz.dua.plformal.allgemein.DAVKonstanten;
import de.bsvrz.dua.plformal.dfs.DatenFlussSteuerungsHilfe;
import de.bsvrz.dua.plformal.plformal.PPFHilfe;
import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.ClientReceiverInterface;
import stauma.dav.clientside.ClientSenderInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.DataNotSubscribedException;
import stauma.dav.clientside.ReceiveOptions;
import stauma.dav.clientside.ReceiverRole;
import stauma.dav.clientside.ResultData;
import stauma.dav.clientside.SenderRole;
import stauma.dav.common.SendSubscriptionNotConfirmed;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.ConfigurationException;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.ArgumentList;
import sys.funclib.application.StandardApplication;
import sys.funclib.application.StandardApplicationRunner;

/**
 * Diese Applikation testet die SWE 4.1 (PL-Prüfung formal) nach den
 * Vorgaben der Prüfspezifikation (PID: QS-02.04.00.00.00-PrSpez-2.0)
 * 
 * @author Thierfelder
 *
 */
public class PlPruefungFormalTest
implements StandardApplication, ClientSenderInterface, ClientReceiverInterface{
	
	/**
	 * Kumulation der verschiedenen Kennungen in einen Wert.
	 * Da die Kennungen nach der Plausibilisierung nicht als ein
	 * Wert sondern als drei (boolsche) Werte zur Verfügung stehen
	 * muss hier eine Übersetzung dieser Werte vorgenommen werden.
	 * Dabei gilt:
	 * 
	 * kennung[0] = Attribut --> Status.PlFormal.WertMin
	 * kennung[1] = Attribut --> Status.PlFormal.WertMax
	 * kennung[2] = Attribut --> Status.MessWertErsetzung.Implausibel
	 * 
	 */
	private static final boolean[] WertMin = 		new boolean[]{true , false, false};
	private static final boolean[] WertMax = 		new boolean[]{false, true , false};
	private static final boolean[] Implausibel = 	new boolean[]{false, false, true };
	private static final boolean[] Nichts = 		new boolean[]{false, false, false};
	
	/**
	 * Pid von TestObj1
	 */
	private static final String OBJ1_PID = "objekt1.testPlPrüfungFormal"; //$NON-NLS-1$
	
	/**
	 * Pid von TestObj2
	 **/
	private static final String OBJ2_PID = "objekt2.testPlPrüfungFormal"; //$NON-NLS-1$
	
	/**
	 * Pid von Test-Atg
	 */
	private static final String ATG_PID = "atg.testPlPrüfungFormal"; //$NON-NLS-1$
	
	/**
	 * Pid von Test-Empfangs-Aspekt
	 */
	private static final String ASP_EINGANG_PID = "asp.testEingang"; //$NON-NLS-1$

	/**
	 * Pid von Test-Sende-Aspekt
	 */
	private static final String ASP_AUSGANG_PID = "asp.testAusgang"; //$NON-NLS-1$

	/**
	 * Pid von Test-Objekt-Typ
	 */
	private static final String TYP = "typ.testPlPrüfungFormal"; //$NON-NLS-1$
	
	/**
	 * Verbindung zum Datenverteiler
	 */
	private ClientDavInterface dav;
	
	/**
	 * Objekt1
	 */
	private SystemObject obj1 = null;
	
	/**
	 * Objekt2
	 */
	private SystemObject obj2 = null;

	/**
	 * 
	 */
	DataDescription ddAusgang = null;
	
	/**
	 * 
	 */
	DataDescription ddEingang = null;
	
	/**
	 * 
	 */
	DataDescription ddParam = null;
	
	/**
	 * Testdatensätze
	 */
	private RohdatenSatz[] testDatenSaetze = null;

	/**
	 * Durchläufe
	 */
	private Durchlauf[] durchlaeufe = null;
	
	/**
	 * Erwartete Ergebnisse
	 */
	private TestErgebnis[][] ergebnisse = null;
	
	/**
	 * das aktuelle Ergebnis eines Tests
	 */
	private TestErgebnis aktuellesErgebnis = null;
	
	/**
	 * aktuelle Testparameter
	 */
	private ParameterSatz[] parameter = null;
	
	/**
	 * Das Objekt der PL-Prüfung formal
	 */
	private SystemObject ppfObjekt = null;

	
	/**
	 * {@inheritDoc}
	 */
	public void dataRequest(SystemObject object, DataDescription dataDescription, byte state) {
		// mache nichts		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequestSupported(SystemObject object, DataDescription dataDescription) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface connection) throws Exception {
		this.dav = connection;
		
		AttributeGroup atg = this.dav.getDataModel().getAttributeGroup(ATG_PID);
		Aspect eingang = this.dav.getDataModel().getAspect(ASP_EINGANG_PID);
		Aspect ausgang = this.dav.getDataModel().getAspect(ASP_AUSGANG_PID);
		SystemObjectType typ = this.dav.getDataModel().getType(TYP);

		AttributeGroup atgPara = this.dav.getDataModel().getAttributeGroup(DAVKonstanten.ATG_PL_FORMAL);
		Aspect aspPara = this.dav.getDataModel().getAspect(DAVKonstanten.ASP_PARA_VORGABE);
		
		ddAusgang = new DataDescription(atg, ausgang, (short)0);
		ddEingang = new DataDescription(atg, eingang, (short)0);
		ddParam = new DataDescription(atgPara, aspPara, (short)0);
		this.obj1 = this.dav.getDataModel().getObject(OBJ1_PID);
		this.obj2 = this.dav.getDataModel().getObject(OBJ2_PID);
		

			
		for(SystemObject elem:this.dav.getDataModel().getType(DAVKonstanten.TYP_PL_FORMAL).getObjects()){
			if(elem.getPid().equals("kv.bitctrl.thierfelder")){
				this.ppfObjekt = elem;
			}
		}
		
		/**
		 * Daten zum Senden anmelden
		 */
		List<SystemObject> a = typ.getObjects();
		List<SystemObject> b = typ.getObjects();
		dav.subscribeSender(this, this.ppfObjekt, ddParam, SenderRole.sender());
		dav.subscribeSender(this, typ.getObjects(), ddAusgang, SenderRole.source());
				
				
		/**
		 * Auf Empfang von Daten anmelden
		 */
		dav.subscribeReceiver(this, typ.getObjects(), ddEingang, ReceiveOptions.normal(), ReceiverRole.receiver());
		
		/**
		 * Testdatensätze wie in Tabelle 5-3 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.testDatenSaetze = new RohdatenSatz[]{
				new RohdatenSatz(obj1, -4, -2.00000901),
				new RohdatenSatz(obj1, -3, -2.0),
				new RohdatenSatz(obj1, 17, 73.1087001),
				new RohdatenSatz(obj1, 18, 73.0),
				new RohdatenSatz(obj1, -2, 13),
				new RohdatenSatz(obj2, -2, 13),
				new RohdatenSatz(obj1,  5, 67),
				new RohdatenSatz(obj2,  5, 67),
		};
		
		/**
		 * Testparameter wie in Tabelle 5-2 (bzw. Änderungsantrag
		 * AeA_SWE4.1_VRZ3_Nr001)
		 */
		this.parameter = new ParameterSatz[]{
			new ParameterSatz(this.obj1, -3, 17, -2, 73),
			new ParameterSatz(this.obj2, -251, -2, -2, 63)
		};
		
		/**
		 * Durchläufe wie in Tabelle 5-4
		 */
		this.durchlaeufe = new Durchlauf[]{
			new Durchlauf(DAVKonstanten.ATT_PL_FORMAL_WERT_KEINE_PRUEFUNG, 	DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MIN),
			new Durchlauf(DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MINMAX, 	DAVKonstanten.ATT_PL_FORMAL_WERT_NUR_PRUEFUNG),
			new Durchlauf(DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MAX, 		DAVKonstanten.ATT_PL_FORMAL_WERT_KEINE_PRUEFUNG),
			new Durchlauf(DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MIN, 		DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MINMAX),
			new Durchlauf(DAVKonstanten.ATT_PL_FORMAL_WERT_NUR_PRUEFUNG, 	DAVKonstanten.ATT_PL_FORMAL_WERT_SETZE_MAX)
		};
		
		/**
		 * Erwartete Ergebnisse
		 */
		this.ergebnisse = new TestErgebnis[][]{
				new TestErgebnis[]{
					new TestErgebnis(-4, Nichts, -2.0,			WertMin),
					new TestErgebnis(-3, Nichts, -2.0, 			Nichts),
					new TestErgebnis(17, Nichts, 73.1087001, 	Nichts),
					new TestErgebnis(18, Nichts, 73.0,	 		Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis( 5, Nichts, 67, 			Nichts),
					new TestErgebnis( 5, Nichts, 67,			Nichts)
				}, 
				new TestErgebnis[]{
					new TestErgebnis(-3, WertMin, -2.00000901,	Implausibel),
					new TestErgebnis(-3, Nichts, -2.0,		 	Nichts),
					new TestErgebnis(17, Nichts, 73.1087001, 	Implausibel),
					new TestErgebnis(17, WertMax, 73.0, 		Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis(-2, Nichts, 13,			Nichts),
					new TestErgebnis( 5, Nichts, 67, 			Nichts),
					new TestErgebnis(-2, WertMax, 67, 			Implausibel)
				},
				new TestErgebnis[]{
					new TestErgebnis(-4, Nichts, -2.00000901, 	Nichts),
					new TestErgebnis(-3, Nichts, -2.0, 		 	Nichts),
					new TestErgebnis(17, Nichts, 73.1087001, 	Nichts),
					new TestErgebnis(17, WertMax, 73.0, 		Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis( 5, Nichts, 67, 			Nichts),
					new TestErgebnis(-2, WertMax, 67, 			Nichts)
				},
				new TestErgebnis[]{				
					new TestErgebnis(-3, WertMin, -2.0,		 	WertMin),
					new TestErgebnis(-3, Nichts, -2.0,		 	Nichts),
					new TestErgebnis(17, Nichts, 73.0,	 		WertMax),
					new TestErgebnis(18, Nichts, 73.0,			Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis( 5, Nichts, 67, 			Nichts),
					new TestErgebnis( 5, Nichts, 63, 			WertMax)
				},
				new TestErgebnis[]{
					new TestErgebnis(-4, Implausibel, -2.00000901,Nichts),
					new TestErgebnis(-3, Nichts, -2.0,		 	Nichts),
					new TestErgebnis(17, Nichts, 73.0, 			WertMax),
					new TestErgebnis(18, Implausibel, 73.0, 	Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis(-2, Nichts, 13, 			Nichts),
					new TestErgebnis( 5, Nichts, 67,			Nichts),
					new TestErgebnis( 5, Implausibel, 63,		WertMax)
				}
		};
		
		testeAlles();
	}
	
	/**
	 * Alle Testfälle durchführen und Auswertung anzeigen
	 */
	private void testeAlles(){
		for(int iDurchlauf = 0; iDurchlauf < 5; iDurchlauf++){
			for(int iDatensatz = 0; iDatensatz < 8; iDatensatz++){
				TestErgebnis soll = this.ergebnisse[iDurchlauf][iDatensatz];
				TestErgebnis ist = this.testLauf(this.durchlaeufe[iDurchlauf], this.testDatenSaetze[iDatensatz]);
				System.out.println("Test " + (iDurchlauf+1) + "/" + (iDatensatz+1) + ":"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				System.out.println("Soll: " + soll); //$NON-NLS-1$
				System.out.println("Ist : " + ist);				 //$NON-NLS-1$
				System.out.println("Ergebnis: ---" + (soll.equals(ist)?  //$NON-NLS-1$
						"erfolgreich":"nicht erfolgreich") + "---\n");  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			}	
		}
	}
	
	private TestErgebnis testLauf(Durchlauf dl, RohdatenSatz ds){
		
		/**
		 * Parameter setzen
		 */
		Data data = this.dav.createData(this.dav.getDataModel().getAttributeGroup(DAVKonstanten.ATG_PL_FORMAL));
		Data.Array ps = data.getItem("ParameterSatzPlausibilitätsPrüfungFormal").asArray(); //$NON-NLS-1$
		ps.setLength(2);
		
		Data ps0 = ps.getItem(0);
		ps0.getReferenceValue("Attributgruppe").setSystemObject(this.dav.getDataModel().getAttributeGroup(ATG_PID)); //$NON-NLS-1$
		ps0.getReferenceValue("Aspekt").setSystemObject(this.dav.getDataModel().getAspect(ASP_AUSGANG_PID)); //$NON-NLS-1$
		Data.Array objekte = ps0.getArray("Objekt"); //$NON-NLS-1$
		objekte.setLength(1);
		objekte.getItem(0).asReferenceValue().setSystemObject(this.parameter[0].obj);
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
		ps1.getReferenceValue("Attributgruppe").setSystemObject(this.dav.getDataModel().getAttributeGroup(ATG_PID)); //$NON-NLS-1$
		ps1.getReferenceValue("Aspekt").setSystemObject(this.dav.getDataModel().getAspect(ASP_AUSGANG_PID)); //$NON-NLS-1$
		Data.Array objekte1 = ps1.getArray("Objekt"); //$NON-NLS-1$
		objekte1.setLength(1);
		objekte1.getItem(0).asReferenceValue().setSystemObject(this.parameter[1].obj);
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
		ResultData parameter = new ResultData(this.ppfObjekt, this.ddParam, System.currentTimeMillis(), data);
		
		System.out.println("Setze PL-Parameter für " + this.parameter[0]); //$NON-NLS-1$
		System.out.println("Setze PL-Parameter für " + this.parameter[1]); //$NON-NLS-1$
		System.out.println(DAVKonstanten.ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.get(dl.testAtt1) + ", "  + DAVKonstanten.ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.get(dl.testAtt2)); //$NON-NLS-1$
		
		try {
			this.dav.sendData(parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try{
			Thread.sleep(500L);
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
		
		/**
		 * Jetzt sollten die Parameter angekommen sein. Es können Daten gesendet werden
		 */
		aktuellesErgebnis = null;
		System.out.println("Sende: " + ds); //$NON-NLS-1$
		sendeDatum(ds.obj, ds.att1, ds.att2);
		try{
			Thread.sleep(500L);
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}

		return aktuellesErgebnis;
	}
	
	/**
	 * Sendet ein Datum für ein bestimmtes Objekt an den Datenverteiler
	 * 
	 * @param obj Das Objekt 
	 * @param att1 das Attribut 1
	 * @param att2 das Attribut 2
	 */
	private void sendeDatum(SystemObject obj, long att1, double att2){
		Data data = this.dav.createData(this.dav.getDataModel().getAttributeGroup(ATG_PID));
		data.getItem("Attribut1").getUnscaledValue("Wert").set(att1); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getItem("WertMin").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getItem("WertMax").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getItem("Implausibel").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getItem("Interpoliert").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		data.getItem("Attribut2").getUnscaledValue("Wert").set(att2); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getItem("WertMin").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getItem("WertMax").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getItem("Implausibel").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getItem("Interpoliert").asUnscaledValue().set(0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		final ResultData resultat = new ResultData(obj, ddAusgang, System.currentTimeMillis(), data);
		try {
			this.dav.sendData(resultat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void parseArguments(ArgumentList argumentList) throws Exception {
		 //	
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		if(resultate != null){
			for(ResultData resultat:resultate){
				if(resultat != null && resultat.getData() != null){
					Data data = resultat.getData();
					
					long wert1 = data.getItem("Attribut1").getUnscaledValue("Wert").longValue(); //$NON-NLS-1$ //$NON-NLS-2$
					boolean[] kennung1 = new boolean[3];
					kennung1[0] = data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					kennung1[1] = data.getItem("Attribut1").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					kennung1[2] = data.getItem("Attribut1").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					
					double wert2 = data.getItem("Attribut2").getUnscaledValue("Wert").doubleValue(); //$NON-NLS-1$ //$NON-NLS-2$ 
					boolean[] kennung2 = new boolean[3];
					kennung2[0] = data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					kennung2[1] = data.getItem("Attribut2").getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					kennung2[2] = data.getItem("Attribut2").getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").longValue() == 1; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

					this.aktuellesErgebnis = new TestErgebnis(wert1, kennung1, wert2, kennung2);
				}
			}
		}
	}

	/**
	 * Startet den Test
	 * 
	 * @param args Kommandozeile
	 */
	public static void main(String[] args){
		StandardApplicationRunner.run(new PlPruefungFormalTest(), args);
	}

	
	
	
	/**
	 * Wie Rohdatensatz in Tabelle 5-3 (S.15)
	 * 
	 * @author Thierfelder
	 *
	 */
	private class RohdatenSatz{
		
		/**
		 * Das Testobjekt
		 */
		public SystemObject obj;
		
		/**
		 * Der Wert, der in das 'AttributTest1' geschrieben werden soll
		 */
		public long att1;

		/**
		 * Der Wert, der in das 'AttributTest2' geschrieben werden soll
		 */
		public double att2;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param obj das Testobjekt
		 * @param att1 Wert, der in das 'AttributTest1' geschrieben werden soll
		 * @param att2 Wert, der in das 'AttributTest2' geschrieben werden soll
		 */
		public RohdatenSatz(SystemObject obj, long att1, double att2){
			this.obj = obj;
			this.att1 = att1;
			this.att2 = att2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Objekt: " + obj + ", Wert1: " +  //$NON-NLS-1$ //$NON-NLS-2$
					att1 + ", Wert2: " + att2 + "\n";  //$NON-NLS-1$//$NON-NLS-2$
		}
	}
	
	
	/**
	 * Wie Durchlauf in Tabelle 5-4
	 * 
	 * @author Thierfelder
	 *
	 */
	private class Durchlauf{
		
		/**
		 * Parameter für die Plausibilisierung (Manipulation)
		 * eines als implausibel erkannten Wertes von AttributTest1
		 */
		public long testAtt1;
		
		/**
		 * Parameter für die Plausibilisierung (Manipulation)
		 * eines als implausibel erkannten Wertes von AttributTest2
		 */
		public long testAtt2;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param testAtt1 Plausibilisierungsparameter für AttributTest1
		 * @param testAtt2 Plausibilisierungsparameter für AttributTest2
		 */
		public Durchlauf(long testAtt1, long testAtt2){
			this.testAtt1 = testAtt1;
			this.testAtt2 = testAtt2;
		}
				
	}
	
	
	/**
	 * Klasse für die Parametersätze, die zur formalen
	 * Prüfung der Testobjekte herangezogen werden.
	 * 
	 * @author Thierfelder
	 *
	 */
	private class ParameterSatz{
		
		/**
		 * das Testobjekt
		 */
		public SystemObject obj;
		
		/**
		 * Test-Intervall für Ganzzahl-Attribut 
		 */
		public long min1, max1;
		
		/**
		 * Test-Intervall für Kommazahl-Attribut
		 */
		public long min2, max2;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param obj das Testobjekt
		 * @param min1 Test-Intervallanfang für Ganzzahl-Attribut
		 * @param max1 Test-Intervallende für Ganzzahl-Attribut
		 * @param min2 Test-Intervallanfang für Kommazahl-Attribut
		 * @param max2 Test-Intervallende für Kommazahl-Attribut
		 */
		public ParameterSatz(SystemObject obj, long min1, long max1, long min2, long max2){
			this.obj = obj;
			this.min1 = min1;
			this.max1 = max1;
			this.min2 = min2;
			this.max2 = max2;			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return obj + " --> Attribut1[" + min1+ ", " + max1 + "], " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"Attribut2[" + min2+ ", " + max2 + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
		
	
	/**
	 * Klasse zur Repräsentation eines Ergebnisses wie in 
	 * Tabelle 5-5 (S.16)
	 * 
	 * @author Thierfelder
	 *
	 */
	private class TestErgebnis{
		
		/**
		 * Ergebniswert von Attribut1
		 */
		public long wert1;
		
		/**
		 * Ergebniskennung von Attribut1
		 */
		public boolean[] kennung1;
		
		/**
		 * Ergebniswert von Attribut2
		 */
		public double wert2;
		
		/**
		 * Ergebniskennung von Attribut2
		 */
		public boolean[] kennung2;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param wert1 Ergebniswert von Attribut1
		 * @param kennung1 Ergebniskennung von Attribut1
		 * @param wert2 Ergebniswert von Attribut2
		 * @param kennung2 Ergebniskennung von Attribut2
		 */
		public TestErgebnis(long wert1, boolean[] kennung1, double wert2, boolean[] kennung2){
			this.wert1 = wert1;
			this.kennung1 = kennung1;
			this.wert2 = wert2;
			this.kennung2 = kennung2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			boolean ergebnis = false;
			
			if(obj != null && obj instanceof TestErgebnis){
				TestErgebnis that = (TestErgebnis)obj;
				
				ergebnis = this.wert1 == that.wert1 &
						   this.wert2 == that.wert2;
				
				for(int i=0; i<3; i++){
					ergebnis &= this.kennung1[i] == that.kennung1[i] & this.kennung2[i] == that.kennung2[i];
				}
			}
			
			return ergebnis;
		}

		/**
		 * Ermittelt die Kennung aus dem Bool-Array
		 * 
		 * @param k Bool-Array mit verschlüsselter Kennung
		 * @return die Kennung
		 */
		private String getKennung(boolean[] k){
			String s = "UNBEKANNT"; //$NON-NLS-1$
			
			if(k[0] & !k[1] & !k[2]){
				s = "WertMin"; //$NON-NLS-1$
			}
			if(!k[0] & k[1] & !k[2]){
				s = "WertMax"; //$NON-NLS-1$
			}
			if(!k[0] & !k[1] & k[2]){
				s = "Implausibel"; //$NON-NLS-1$
			}
			if(!k[0] & !k[1] & !k[2]){
				s = "\"-\""; //$NON-NLS-1$
			}
			
			return s; 
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Wert1: " + wert1 + " - "   //$NON-NLS-1$//$NON-NLS-2$
					+ getKennung(kennung1) + ", Wert2: " + wert2 + //$NON-NLS-1$
					" - " + getKennung(kennung2); //$NON-NLS-1$
		}
	}
}
