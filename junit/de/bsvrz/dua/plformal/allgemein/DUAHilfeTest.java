package de.bsvrz.dua.plformal.allgemein;

import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.configuration.interfaces.ConfigurationArea;
import stauma.dav.configuration.interfaces.IntegerAttributeType;
import stauma.dav.configuration.interfaces.ReferenceAttributeType;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import de.bsvrz.dua.plformal.DAVTest;

/**
 * Testklasse für die statischen Methoden der Klasse DUAHilfe
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class DUAHilfeTest{
	
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
	 * Test von <code>getAlleObjektAnmeldungen()</code>
	 */
	@Test
	public void testGetAlleObjektAnmeldungen(){
		SystemObject o1 = VERBINDUNG.getDataModel().
		getObject("typ.messQuerschnittAllgemein"); //$NON-NLS-1$
		SystemObject o2 = VERBINDUNG.getDataModel().
		getObject("typ.messQuerschnitt"); //$NON-NLS-1$
		SystemObject o3 = VERBINDUNG.getDataModel().
		getObject("mq.a100.0010"); //$NON-NLS-1$
		SystemObject o4 = null;
		SystemObject o5 = VERBINDUNG.getDataModel().
		getObject("typ.typ"); //$NON-NLS-1$
		
		

		Collection<SystemObject> ist1 = DUAHilfe.getFinaleObjekte(o1, VERBINDUNG);
		Collection<SystemObject> ist2 = DUAHilfe.getFinaleObjekte(o2, VERBINDUNG);
		Collection<SystemObject> ist3 = DUAHilfe.getFinaleObjekte(o3, VERBINDUNG);
		Collection<SystemObject> ist4 = DUAHilfe.getFinaleObjekte(o4, VERBINDUNG);
		Collection<SystemObject> ist5 = DUAHilfe.getFinaleObjekte(o5, VERBINDUNG);

		Collection<SystemObject> soll1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5 = new HashSet<SystemObject>();

		soll1.addAll(((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.messQuerschnitt")).getElements()); //$NON-NLS-1$
		soll2.addAll(((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.messQuerschnitt")).getElements()); //$NON-NLS-1$
		soll3.add(VERBINDUNG.getDataModel().
				getObject("mq.a100.0010")); //$NON-NLS-1$
		for(SystemObject obj:((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.typ")).getElements()){ //$NON-NLS-1$
			for(SystemObject elem:((SystemObjectType)obj).getElements()){

				if( elem.getClass().equals(stauma.dav.configuration.meta.ConfigurationObject.class) ||
						elem.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class) ){
					soll4.add(elem);

				}

			}

		}
		soll5 = soll4;
	}

	/**
	 * Test von <code>testGetFinaleObjekte()</code>
	 */
	@Test
	public void testGetFinaleObjekte(){
		SystemObject o1 = VERBINDUNG.getDataModel().
							getObject("typ.messQuerschnittAllgemein"); //$NON-NLS-1$
		SystemObject o2 = VERBINDUNG.getDataModel().
							getObject("typ.messQuerschnitt"); //$NON-NLS-1$
		SystemObject o3 = VERBINDUNG.getDataModel().
							getObject("mq.a100.0010"); //$NON-NLS-1$
		SystemObject o4 = null;
		SystemObject o5 = VERBINDUNG.getDataModel().
								getObject("typ.typ"); //$NON-NLS-1$

		Collection<SystemObject> ist1 = DUAHilfe.getFinaleObjekte(o1, VERBINDUNG);
		Collection<SystemObject> ist2 = DUAHilfe.getFinaleObjekte(o2, VERBINDUNG);
		Collection<SystemObject> ist3 = DUAHilfe.getFinaleObjekte(o3, VERBINDUNG);
		Collection<SystemObject> ist4 = DUAHilfe.getFinaleObjekte(o4, VERBINDUNG);
		Collection<SystemObject> ist5 = DUAHilfe.getFinaleObjekte(o5, VERBINDUNG);

		Collection<SystemObject> soll1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5 = new HashSet<SystemObject>();

		soll1.addAll(((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.messQuerschnitt")).getElements()); //$NON-NLS-1$
		soll2.addAll(((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.messQuerschnitt")).getElements()); //$NON-NLS-1$
		soll3.add(VERBINDUNG.getDataModel().
				getObject("mq.a100.0010")); //$NON-NLS-1$
		for(SystemObject obj:((SystemObjectType)VERBINDUNG.getDataModel().
				getObject("typ.typ")).getElements()){ //$NON-NLS-1$
			for(SystemObject elem:((SystemObjectType)obj).getElements()){
				
				if( elem.getClass().equals(stauma.dav.configuration.meta.ConfigurationObject.class) ||
					elem.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class) ){
						soll4.add(elem);
						
				}

			}

		}
		soll5 = soll4;
		
		Assert.assertEquals("1: ", soll1, ist1); //$NON-NLS-1$
		Assert.assertEquals("2: ", soll2, ist2); //$NON-NLS-1$
		Assert.assertEquals("3: ", soll3, ist3); //$NON-NLS-1$
		Assert.assertEquals("4: ", soll4, ist4); //$NON-NLS-1$
		Assert.assertEquals("5: ", soll5, ist5); //$NON-NLS-1$
	}
	
	/**
	 * Test von getKonfigurationsBereiche()
	 */
	@Test
	public void testGetKonfigurationsBereiche(){
		String aVorher = "a,b";   //$NON-NLS-1$
		String[] aNachher = {"a", "b"};  //$NON-NLS-1$  //$NON-NLS-2$
		String bVorher = "a";   //$NON-NLS-1$
		String[] bNachher = {"a"};  //$NON-NLS-1$
		String cVorher = "äöfdsa,dasfsdf";   //$NON-NLS-1$
		String[] cNachher = {"äöfdsa", "dasfsdf"};  //$NON-NLS-1$  //$NON-NLS-2$
		String dVorher = "äöfdsa,dasfsdf,a";   //$NON-NLS-1$
		String[] dNachher = {"äöfdsa", "dasfsdf", "a"};  //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		String eVorher = "äöfdsa,dasfsdf,,a";   //$NON-NLS-1$
		String[] eNachher = {"äöfdsa", "dasfsdf", "a"};  //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$
		String fVorher = "";   //$NON-NLS-1$
		String[] fNachher = new String[0];
		String gVorher = null;
		String[] gNachher = new String[0];
		String hVorher = ",b";   //$NON-NLS-1$
		String[] hNachher = {"b"};  //$NON-NLS-1$
		String iVorher = "a,";   //$NON-NLS-1$
		String[] iNachher = {"a"};  //$NON-NLS-1$
		
		Assert.assertTrue("A:", equalStringArray(aNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(aVorher)));
		Assert.assertTrue("B:", equalStringArray(bNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(bVorher)));
		Assert.assertTrue("C:", equalStringArray(cNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(cVorher)));
		Assert.assertTrue("D:", equalStringArray(dNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(dVorher)));
		Assert.assertTrue("E:", equalStringArray(eNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(eVorher)));
		Assert.assertTrue("F:", equalStringArray(fNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(fVorher)));
		Assert.assertTrue("G:", equalStringArray(gNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(gVorher)));
		Assert.assertTrue("H:", equalStringArray(hNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(hVorher)));
		Assert.assertTrue("I:", equalStringArray(iNachher, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereiche(iVorher)));
	}
	
	/**
	 * Test auf Gleichheit
	 * 
	 * @param a Str1
	 * @param b Str2
	 * @return gleich?
	 */
	private boolean equalStringArray(String[] a, String b[]){
		boolean result = false;
		
		if(a != null && b != null){
			int match = 0;
			for(String aStr:a){
				for(String bStr:b){
					if(aStr.equals(bStr)){
						match++;
						break;
					}
				}
			}
			result = a.length == b.length && a.length == match;
		}else if(a == null && b == null){
			result = true;
		}
		
		return result;
	}
	
	/**
	 * Vergleicht zwei Objekte 
	 * 
	 * @param obj1 Objekt Nr.1
	 * @param obj2 Objekt Nr.2
	 * @return <code>true</code>, wenn beide Objekte identisch oder
	 * beide <code>null</code> sind
	 */
	public static final boolean objectEquals(Object obj1, Object obj2){
		boolean eq = false;
		
		if(obj1 == null && obj2 == null){
			eq = true;
		}else if(obj1 != null && obj2 != null){
			eq = obj1.equals(obj2);
		}
		
		return eq;
	}	
	
	/**
	 * Test von getKonfigurationsBereicheAlsObjekte() und getKonfigurationsBereiche()
	 */
	@Test
	public void testGetKonfigurationsBereicheAlsObjekte(){
		String aInput = "kb.fachModellGlobal,kb.systemModellGlobal,kb.metaModellGlobal";   //$NON-NLS-1$
		Collection<ConfigurationArea> aSoll = new HashSet<ConfigurationArea>();
		aSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.fachModellGlobal"));   //$NON-NLS-1$
		aSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.systemModellGlobal"));   //$NON-NLS-1$
		aSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.metaModellGlobal"));   //$NON-NLS-1$
		String bInput = "kb.fachModellGlobal,kb.systemMXXXXGlobal,kb.metaModellGlobal";   //$NON-NLS-1$
		Collection<ConfigurationArea> bSoll = new HashSet<ConfigurationArea>();
		bSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.fachModellGlobal"));   //$NON-NLS-1$
		bSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.metaModellGlobal"));   //$NON-NLS-1$
		String cInput = ",kb.fachModellGlobal,kb.metaModellGlobal";   //$NON-NLS-1$
		Collection<ConfigurationArea> cSoll = new HashSet<ConfigurationArea>();
		cSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.fachModellGlobal"));   //$NON-NLS-1$
		cSoll.add(VERBINDUNG.getDataModel().getConfigurationArea("kb.metaModellGlobal"));   //$NON-NLS-1$
		String dInput = "";   //$NON-NLS-1$
		Collection<ConfigurationArea> dSoll = new HashSet<ConfigurationArea>();
		String eInput = null;
		Collection<ConfigurationArea> eSoll = new HashSet<ConfigurationArea>();
		String fInput = "kb.fachXXXGlobal";   //$NON-NLS-1$
		Collection<ConfigurationArea> fSoll = new HashSet<ConfigurationArea>();

		Assert.assertTrue("A:", objectEquals(aSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(aInput, VERBINDUNG)));
		Assert.assertTrue("B:", objectEquals(bSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(bInput, VERBINDUNG)));
		Assert.assertTrue("C:", objectEquals(cSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(cInput, VERBINDUNG)));
		Assert.assertTrue("D:", objectEquals(dSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(dInput, VERBINDUNG)));
		Assert.assertTrue("E:", objectEquals(eSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(eInput, VERBINDUNG)));
		Assert.assertTrue("F:", objectEquals(fSoll, DUAHilfe.   //$NON-NLS-1$
				getKonfigurationsBereicheAlsObjekte(fInput, VERBINDUNG)));
	}
	
	
	/**
	 * Test von isKombinationOk()
	 */
	@Test
	public void testIsKombinationOk(){
		SystemObject aObj = VERBINDUNG.getDataModel().getObject("typ.fahrStreifen");   //$NON-NLS-1$
		DataDescription aDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean aSoll = false;
		
		SystemObject bObj = VERBINDUNG.getDataModel().getObject("atg.verkehrsDatenLangZeitIntervall");   //$NON-NLS-1$
		DataDescription bDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.info"),   //$NON-NLS-1$
				null, (short)0);
		boolean bSoll = false;
		
		SystemObject cObj = null;
		DataDescription cDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean cSoll = false;

		SystemObject dObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription dDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean dSoll = false;

		SystemObject eObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription eDataDesc = new DataDescription(
				null,
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean eSoll = false;

		SystemObject fObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription fDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall"),   //$NON-NLS-1$
				null, (short)0);
		boolean fSoll = false;
		
		SystemObject gObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription gDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.testPlPrüfungFormal"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean gSoll = false;

		SystemObject hObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription hDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.verkehrsDatenLangZeitIntervall"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.testEingang"), (short)0);   //$NON-NLS-1$
		boolean hSoll = false;

		SystemObject iObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription iDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.XXX"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.externeErfassung"), (short)0);   //$NON-NLS-1$
		boolean iSoll = false;
		
		SystemObject jObj = VERBINDUNG.getDataModel().getObject("objekt1.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription jDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.testPlPrüfungFormal"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.testEingang"), (short)0);   //$NON-NLS-1$
		boolean jSoll = true;

		Assert.assertTrue("A:", (DUAHilfe.isKombinationOk(aObj, aDataDesc) == null) == aSoll);   //$NON-NLS-1$
		Assert.assertTrue("B:", (DUAHilfe.isKombinationOk(bObj, bDataDesc) == null) == bSoll);   //$NON-NLS-1$
		Assert.assertTrue("C:", (DUAHilfe.isKombinationOk(cObj, cDataDesc) == null) == cSoll);   //$NON-NLS-1$
		Assert.assertTrue("D:", (DUAHilfe.isKombinationOk(dObj, dDataDesc) == null) == dSoll);   //$NON-NLS-1$
		Assert.assertTrue("E:", (DUAHilfe.isKombinationOk(eObj, eDataDesc) == null) == eSoll);   //$NON-NLS-1$
		Assert.assertTrue("F:", (DUAHilfe.isKombinationOk(fObj, fDataDesc) == null) == fSoll);   //$NON-NLS-1$
		Assert.assertTrue("G:", (DUAHilfe.isKombinationOk(gObj, gDataDesc) == null) == gSoll);   //$NON-NLS-1$
		Assert.assertTrue("H:", (DUAHilfe.isKombinationOk(hObj, hDataDesc) == null) == hSoll);   //$NON-NLS-1$
		Assert.assertTrue("I:", (DUAHilfe.isKombinationOk(iObj, iDataDesc) == null) == iSoll);   //$NON-NLS-1$
		Assert.assertTrue("J:", (DUAHilfe.isKombinationOk(jObj, jDataDesc) == null) == jSoll);   //$NON-NLS-1$
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
		
		Assert.assertEquals("A:", aNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(aVorher, aErsetz));
		Assert.assertEquals("B:", bNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(bVorher, bErsetz));
		Assert.assertEquals("C:", cNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(cVorher, cErsetz));
		Assert.assertEquals("D:", dNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(dVorher, dErsetz));
		Assert.assertEquals("E:", eNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(eVorher, eErsetz));
		Assert.assertEquals("F:", fNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(fVorher, fErsetz));
		Assert.assertEquals("G:", gNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(gVorher, gErsetz));
		Assert.assertEquals("H:", hNachher, DUAHilfe.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(hVorher, hErsetz));

	}

	/**
	 * Testet, ob bestimmte Eingabevarianten für den Attributpfad 
	 * zum richtigen Ergebnis führen.
	 */
	@Test
	public void testGetAttributDatum() {
		Data testDatum = VERBINDUNG.createData(VERBINDUNG.getDataModel().
				getAttributeGroup(DUAKonstanten.ATG_PL_FORMAL));
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
			Data dummy = DUAHilfe.getAttributDatum(pfade[j], testDatum);
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
