package de.bsvrz.dua.plformal.allgemein;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.Data;
import stauma.dav.configuration.interfaces.IntegerAttributeType;
import stauma.dav.configuration.interfaces.ReferenceAttributeType;
import de.bsvrz.dua.plformal.DAVTest;

/**
 * Testklasse für die statischen Methoden der Klasse DAVHilfe
 * 
 * @author Thierfelder
 *
 */
public class DAVHilfeTest{
	
	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface VERBINDUNG = null;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Before
	public void setUp()
	throws Exception{
		VERBINDUNG = DAVTest.getDav();
	}
	
	/**
	 * Testet verschiedene Varianten der Ersetzung
	 * des letzten Attributs in einem Attributpfad
	 **/
	@Test
	public void testErsetzeLetztesElemInAttPfad(){
		final String aVorher =  "",   //$NON-NLS-1$ 
					 aNachher = null,
					 aErsetz =  "hallo";   //$NON-NLS-1$
		final String bVorher =  "a",   //$NON-NLS-1$ 
					 bNachher = "hallo",   //$NON-NLS-1$
					 bErsetz =  "hallo";   //$NON-NLS-1$
		final String cVorher =  "a.a",   //$NON-NLS-1$ 
					 cNachher = "a.hallo",   //$NON-NLS-1$
					 cErsetz =  "hallo";   //$NON-NLS-1$
		final String dVorher =  "",   //$NON-NLS-1$ 
					 dNachher = null,
					 dErsetz =  "h";   //$NON-NLS-1$
		final String eVorher =  "a",   //$NON-NLS-1$ 
					 eNachher = "h",   //$NON-NLS-1$
					 eErsetz =  "h";   //$NON-NLS-1$
		final String fVorher =  "a.a",   //$NON-NLS-1$ 
					 fNachher = "a.h",   //$NON-NLS-1$
					 fErsetz =  "h";   //$NON-NLS-1$
		final String gVorher =  "a.a",   //$NON-NLS-1$ 
					 gNachher = null,
					 gErsetz =  "";   //$NON-NLS-1$
		final String hVorher =  "a.a",   //$NON-NLS-1$ 
					 hNachher = null,
					 hErsetz =  null;
		
		Assert.assertEquals("A:", aNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(aVorher, aErsetz));
		Assert.assertEquals("B:", bNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(bVorher, bErsetz));
		Assert.assertEquals("C:", cNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(cVorher, cErsetz));
		Assert.assertEquals("D:", dNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(dVorher, dErsetz));
		Assert.assertEquals("E:", eNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(eVorher, eErsetz));
		Assert.assertEquals("F:", fNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(fVorher, fErsetz));
		Assert.assertEquals("G:", gNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(gVorher, gErsetz));
		Assert.assertEquals("H:", hNachher, DAVHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(hVorher, hErsetz));

	}

	/**
	 * Testet, ob bestimmte Eingabevarianten für den Attributpfad 
	 * zum richtigen Ergebnis führen.
	 */
	@Test
	public void testGetAttributDatum() {
		Data testDatum = VERBINDUNG.createData(VERBINDUNG.getDataModel().
				getAttributeGroup(DAVKonstanten.ATG_PL_FORMAL));
		Data.Array ps = testDatum.getItem("ParameterSatzPlausibilitätsPrüfungFormal").asArray(); //$NON-NLS-1$
		ps.setLength(2);
		Data ps0 = ps.getItem(0);
		Data ps1 = ps.getItem(1);
		String[] pfade = new String[30];
		String[] soll = new String[30];
		
		pfade[0] = "ParameterSatzPlausibilitätsPrüfungFormal.0.Attributgruppe"; //$NON-NLS-1$
		soll[0] = VERBINDUNG.getDataModel().getAttributeGroup(
				"atg.verkehrsDatenKurzZeitIntervall").toString(); //$NON-NLS-1$
		ps0.getReferenceValue("Attributgruppe").setSystemObject( //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAttributeGroup(
						"atg.verkehrsDatenKurzZeitIntervall")); //$NON-NLS-1$
		
		pfade[1]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.Aspekt"; //$NON-NLS-1$
		soll[1] = VERBINDUNG.getDataModel().getAspect("asp.externeErfassung").toString(); //$NON-NLS-1$
		ps0.getReferenceValue("Aspekt").setSystemObject(VERBINDUNG. //$NON-NLS-1$
				getDataModel().getAspect("asp.externeErfassung")); //$NON-NLS-1$
		
		pfade[2]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.Objekt.0"; //$NON-NLS-1$
		soll[2] = VERBINDUNG.getDataModel().getObject("typ.fahrStreifen").toString();	 //$NON-NLS-1$
		Data.Array objekte = ps0.getArray("Objekt"); //$NON-NLS-1$
		objekte.setLength(1);
		objekte.getItem(0).asReferenceValue().setSystemObject(VERBINDUNG.
				getDataModel().getObject("typ.fahrStreifen")); //$NON-NLS-1$
		Data.Array attribut = ps0.getArray("AttributSpezifikation"); //$NON-NLS-1$
		
		pfade[3]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.AttributPfad"; //$NON-NLS-1$
		soll[3] = "a11"; //$NON-NLS-1$
		attribut.setLength(2);
		Data attSpez1 = attribut.getItem(0);
		attSpez1.getTextValue("AttributPfad").setText("a11"); //$NON-NLS-1$ //$NON-NLS-2$
		
		pfade[4]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Min"; //$NON-NLS-1$
		soll[4] = "10";	 //$NON-NLS-1$	
		attSpez1.getUnscaledValue("Min").set(10); //$NON-NLS-1$
		
		
		pfade[5]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Max"; //$NON-NLS-1$
		soll[5] = "20";		 //$NON-NLS-1$
		attSpez1.getUnscaledValue("Max").set(20); //$NON-NLS-1$
		
		pfade[6]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.0.Optionen"; //$NON-NLS-1$
		soll[6] = "0";	 //$NON-NLS-1$	
		attSpez1.getUnscaledValue("Optionen").set(0); //$NON-NLS-1$
		
		Data attSpez2 = attribut.getItem(1);
		pfade[7]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.AttributPfad"; //$NON-NLS-1$
		soll[7] = "a12";	 //$NON-NLS-1$	
		attSpez2.getTextValue("AttributPfad").setText("a12"); //$NON-NLS-1$ //$NON-NLS-2$

		pfade[8]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Min"; //$NON-NLS-1$
		soll[8] = "30";	 //$NON-NLS-1$	
		attSpez2.getUnscaledValue("Min").set(30); //$NON-NLS-1$
		
		pfade[9]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Max"; //$NON-NLS-1$
		soll[9] = "40";	 //$NON-NLS-1$	
		attSpez2.getUnscaledValue("Max").set(40); //$NON-NLS-1$
		
		pfade[10]  = "ParameterSatzPlausibilitätsPrüfungFormal.0.AttributSpezifikation.1.Optionen"; //$NON-NLS-1$
		soll[10] = "1";		 //$NON-NLS-1$
		attSpez2.getUnscaledValue("Optionen").set(1); //$NON-NLS-1$


		
		pfade[11] = "ParameterSatzPlausibilitätsPrüfungFormal.1.Attributgruppe"; //$NON-NLS-1$
		soll[11] = VERBINDUNG.getDataModel().getAttributeGroup(
				"atg.verkehrsDatenLangZeitIntervall").toString(); //$NON-NLS-1$
		ps1.getReferenceValue("Attributgruppe").setSystemObject( //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall")); //$NON-NLS-1$
		
		pfade[12]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.Aspekt"; //$NON-NLS-1$
		soll[12] = VERBINDUNG.getDataModel().getAspect("asp.messWertErsetzung").toString(); //$NON-NLS-1$
		ps1.getReferenceValue("Aspekt").setSystemObject( //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.messWertErsetzung")); //$NON-NLS-1$
		
		pfade[13]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.Objekt.0"; //$NON-NLS-1$
		soll[13] = VERBINDUNG.getDataModel().getObject("typ.fahrStreifenLangZeit").toString();		 //$NON-NLS-1$
		Data.Array objekte1 = ps1.getArray("Objekt"); //$NON-NLS-1$
		objekte1.setLength(1);
		objekte1.getItem(0).asReferenceValue().setSystemObject(
				VERBINDUNG.getDataModel().getObject("typ.fahrStreifenLangZeit")); //$NON-NLS-1$
		Data.Array attribut1 = ps1.getArray("AttributSpezifikation"); //$NON-NLS-1$
		
		pfade[14]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.AttributPfad"; //$NON-NLS-1$
		soll[14] = "a21"; //$NON-NLS-1$
		attribut1.setLength(2);
		Data attSpez3 = attribut1.getItem(0);
		attSpez3.getTextValue("AttributPfad").setText("a21"); //$NON-NLS-1$ //$NON-NLS-2$
		
		pfade[15]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Min"; //$NON-NLS-1$
		soll[15] = "50"; //$NON-NLS-1$		
		attSpez3.getUnscaledValue("Min").set(50); //$NON-NLS-1$
		
		
		pfade[16]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Max"; //$NON-NLS-1$
		soll[16] = "60";	 //$NON-NLS-1$	
		attSpez3.getUnscaledValue("Max").set(60); //$NON-NLS-1$
		
		pfade[17]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.0.Optionen"; //$NON-NLS-1$
		soll[17] = "2";		 //$NON-NLS-1$
		attSpez3.getUnscaledValue("Optionen").set(2); //$NON-NLS-1$
		
		Data attSpez4 = attribut1.getItem(1);
		pfade[18]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.AttributPfad"; //$NON-NLS-1$
		soll[18] = "a22";		 //$NON-NLS-1$
		attSpez4.getTextValue("AttributPfad").setText("a22"); //$NON-NLS-1$ //$NON-NLS-2$

		pfade[19]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Min"; //$NON-NLS-1$
		soll[19] = "70";		 //$NON-NLS-1$
		attSpez4.getUnscaledValue("Min").set(70); //$NON-NLS-1$
		
		pfade[20]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Max"; //$NON-NLS-1$
		soll[20] = "80";		 //$NON-NLS-1$
		attSpez4.getUnscaledValue("Max").set(80); //$NON-NLS-1$
		
		pfade[21]  = "ParameterSatzPlausibilitätsPrüfungFormal.1.AttributSpezifikation.1.Optionen"; //$NON-NLS-1$
		soll[21] = "3";		 //$NON-NLS-1$
		attSpez4.getUnscaledValue("Optionen").set(3); //$NON-NLS-1$

		for(int  j = 0; j<22; j++){
			Data dummy = DAVHilfe.getAttributDatum(pfade[j], testDatum);
			String ist = dummy.valueToString();
			
			if(dummy.getAttributeType() instanceof ReferenceAttributeType){
				ist = dummy.asReferenceValue().getSystemObject().toString();
			}
			if(dummy.getAttributeType() instanceof IntegerAttributeType && dummy.asUnscaledValue().isState()){
				ist = new Long(dummy.asUnscaledValue().longValue()).toString();
			}
			
			System.out.println("Überprüfe: " + pfade[j]); //$NON-NLS-1$
			Assert.assertEquals("Fehler in " + pfade[j], ist, soll[j]); //$NON-NLS-1$
		}
	}

}
