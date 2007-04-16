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

package de.bsvrz.dua.plformal.allgemein;

import java.util.ArrayList;
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
import de.bsvrz.dua.plformal.plformal.PPFKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;

/**
 * Testklasse für die statischen Methoden der Klasse DUAUtensilien
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class DUAUtensilienTest{
	
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
	 * Test von <code>getArgument()</code>
	 */
	@Test
	public void testGetArgument(){
		ArrayList<String> para1 = new ArrayList<String>();
		para1.add("-a=1"); //$NON-NLS-1$
		para1.add("-b=2"); //$NON-NLS-1$
		String suche1 = "a"; //$NON-NLS-1$
		String soll1 = "1"; //$NON-NLS-1$
		
		ArrayList<String> para2 = new ArrayList<String>();
		String suche2 = "a"; //$NON-NLS-1$
		String soll2 = null;

		ArrayList<String> para3 = null;
		String suche3 = "a"; //$NON-NLS-1$
		String soll3 = null;

		ArrayList<String> para4 = new ArrayList<String>();
		para4.add("-=1"); //$NON-NLS-1$
		para4.add("-b=2"); //$NON-NLS-1$
		String suche4 = "a"; //$NON-NLS-1$
		String soll4 = null;

		ArrayList<String> para5 = new ArrayList<String>();
		para5.add("-a="); //$NON-NLS-1$
		para5.add("-b=2"); //$NON-NLS-1$
		String suche5 = "a"; //$NON-NLS-1$
		String soll5 = null;

		ArrayList<String> para6 = new ArrayList<String>();
		para6.add("-a=1"); //$NON-NLS-1$
		para6.add("-b=2"); //$NON-NLS-1$
		para6.add("-c=3"); //$NON-NLS-1$
		String suche6 = "a"; //$NON-NLS-1$
		String soll6 = "1"; //$NON-NLS-1$

		ArrayList<String> para7 = new ArrayList<String>();
		para7.add("-b=2"); //$NON-NLS-1$
		para7.add("-c=3"); //$NON-NLS-1$
		para7.add("-a=1"); //$NON-NLS-1$
		String suche7 = "a"; //$NON-NLS-1$
		String soll7 = "1"; //$NON-NLS-1$
		
		ArrayList<String> para8 = new ArrayList<String>();
		para8.add("-b=2"); //$NON-NLS-1$
		para8.add("-a=1"); //$NON-NLS-1$
		para8.add("-c=3"); //$NON-NLS-1$
		String suche8 = "a"; //$NON-NLS-1$
		String soll8 = "1"; //$NON-NLS-1$

		Assert.assertEquals("1: ", DUAUtensilien.getArgument(suche1, para1), soll1); //$NON-NLS-1$
		Assert.assertEquals("2: ", DUAUtensilien.getArgument(suche2, para2), soll2); //$NON-NLS-1$
		Assert.assertEquals("3: ", DUAUtensilien.getArgument(suche3, para3), soll3); //$NON-NLS-1$
		Assert.assertEquals("4: ", DUAUtensilien.getArgument(suche4, para4), soll4); //$NON-NLS-1$
		Assert.assertEquals("5: ", DUAUtensilien.getArgument(suche5, para5), soll5); //$NON-NLS-1$
		Assert.assertEquals("6: ", DUAUtensilien.getArgument(suche6, para6), soll6); //$NON-NLS-1$
		Assert.assertEquals("7: ", DUAUtensilien.getArgument(suche7, para7), soll7); //$NON-NLS-1$
		Assert.assertEquals("8: ", DUAUtensilien.getArgument(suche8, para8), soll8); //$NON-NLS-1$
	}
	
	/**
	 * Test von <code>testGetBasisInstanzen2()</code>	
	 */
	@Test
	public void testGetBasisInstanzen2(){
		ConfigurationArea kb1 = VERBINDUNG.getDataModel().
					getConfigurationArea("kb.bitctrl.thierfelder"); //$NON-NLS-1$
		ConfigurationArea kb2 = VERBINDUNG.getDataModel().
					getConfigurationArea("kb.duaTestObjekte"); //$NON-NLS-1$
		ConfigurationArea kb3 = VERBINDUNG.getDataModel().
					getConfigurationArea("kb.duaTestObjekte2"); //$NON-NLS-1$

		Collection<ConfigurationArea> c1 = null;
		Collection<ConfigurationArea> c2 = new HashSet<ConfigurationArea>();
		Collection<ConfigurationArea> c3 = new HashSet<ConfigurationArea>();
		c3.add(kb1);
		Collection<ConfigurationArea> c4 = new HashSet<ConfigurationArea>();
		c4.add(kb2);
		Collection<ConfigurationArea> c5 = new HashSet<ConfigurationArea>();
		c5.add(kb3);
		Collection<ConfigurationArea> c6 = new HashSet<ConfigurationArea>();
		c6.add(kb2);
		c6.add(kb3);

		SystemObject o1 = VERBINDUNG.getDataModel().
							getObject("typ.plausibilitätsPrüfungFormal"); //$NON-NLS-1$
		SystemObject o2 = VERBINDUNG.getDataModel().
							getObject("typ.testPlPrüfungFormal"); //$NON-NLS-1$
		SystemObject o3 = VERBINDUNG.getDataModel().
							getObject("objekt1.testPlPrüfungFormal"); //$NON-NLS-1$
		SystemObject o4 = null;
		SystemObject o5 = VERBINDUNG.getDataModel().
							getObject("objekt2.testPlPrüfungFormal"); //$NON-NLS-1$
		SystemObject o6 = VERBINDUNG.getDataModel().
							getObject("objekt3.testPlPrüfungFormal"); //$NON-NLS-1$

		Collection<SystemObject> ist1_1 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c1);
		Collection<SystemObject> ist2_1 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c1);
		Collection<SystemObject> ist3_1 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c1);
		Collection<SystemObject> ist4_1 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c1);
		Collection<SystemObject> ist5_1 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c1);
		Collection<SystemObject> ist6_1 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c1);

		Collection<SystemObject> ist1_2 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c2);
		Collection<SystemObject> ist2_2 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c2);
		Collection<SystemObject> ist3_2 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c2);
		Collection<SystemObject> ist4_2 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c2);
		Collection<SystemObject> ist5_2 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c2);
		Collection<SystemObject> ist6_2 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c2);

		Collection<SystemObject> ist1_3 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c3);
		Collection<SystemObject> ist2_3 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c3);
		Collection<SystemObject> ist3_3 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c3);
		Collection<SystemObject> ist4_3 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c3);
		Collection<SystemObject> ist5_3 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c3);
		Collection<SystemObject> ist6_3 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c3);

		Collection<SystemObject> ist1_4 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c4);
		Collection<SystemObject> ist2_4 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c4);
		Collection<SystemObject> ist3_4 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c4);
		Collection<SystemObject> ist4_4 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c4);
		Collection<SystemObject> ist5_4 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c4);
		Collection<SystemObject> ist6_4 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c4);

		Collection<SystemObject> ist1_5 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c5);
		Collection<SystemObject> ist2_5 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c5);
		Collection<SystemObject> ist3_5 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c5);
		Collection<SystemObject> ist4_5 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c5);
		Collection<SystemObject> ist5_5 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c5);
		Collection<SystemObject> ist6_5 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c5);

		Collection<SystemObject> ist1_6 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG, c6);
		Collection<SystemObject> ist2_6 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG, c6);
		Collection<SystemObject> ist3_6 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG, c6);
		Collection<SystemObject> ist4_6 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG, c6);
		Collection<SystemObject> ist5_6 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG, c6);
		Collection<SystemObject> ist6_6 = DUAUtensilien.getBasisInstanzen(o6, VERBINDUNG, c6);

		Collection<SystemObject> soll1_1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_1 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_1 = new HashSet<SystemObject>();
		
		Collection<SystemObject> soll1_2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_2 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_2 = new HashSet<SystemObject>();

		Collection<SystemObject> soll1_3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_3 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_3 = new HashSet<SystemObject>();

		Collection<SystemObject> soll1_4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_4 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_4 = new HashSet<SystemObject>();

		Collection<SystemObject> soll1_5 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_5 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_5 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_5 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_5 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_5 = new HashSet<SystemObject>();

		Collection<SystemObject> soll1_6 = new HashSet<SystemObject>();
		Collection<SystemObject> soll2_6 = new HashSet<SystemObject>();
		Collection<SystemObject> soll3_6 = new HashSet<SystemObject>();
		Collection<SystemObject> soll4_6 = new HashSet<SystemObject>();
		Collection<SystemObject> soll5_6 = new HashSet<SystemObject>();
		Collection<SystemObject> soll6_6 = new HashSet<SystemObject>();

		soll1_1.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$
		soll4_1.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$

		soll1_2.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$
		soll4_2.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$

		soll1_3.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$
		soll4_3.add(VERBINDUNG.getDataModel().
				getObject("kv.bitctrl.thierfelder")); //$NON-NLS-1$
		
		soll4_4.add(VERBINDUNG.getDataModel().
				getObject("bitctrl.dfs")); //$NON-NLS-1$
		soll4_4.add(o3);
		soll4_4.add(o5);
		soll4_5.add(VERBINDUNG.getDataModel().
				getObject("ppfTest")); //$NON-NLS-1$
		soll4_5.add(o6);

		soll2_4.add(o3);
		soll2_4.add(o5);
		soll3_4.add(o3);
		soll5_4.add(o5);

		soll1_5.add(VERBINDUNG.getDataModel().
				getObject("ppfTest")); //$NON-NLS-1$
		soll2_5.add(o6);
		soll6_5.add(o6);

		soll1_6.add(VERBINDUNG.getDataModel().
				getObject("ppfTest")); //$NON-NLS-1$
		soll2_6.add(o3);
		soll2_6.add(o5);
		soll3_6.add(o3);
		soll4_6.add(VERBINDUNG.getDataModel().
				getObject("ppfTest")); //$NON-NLS-1$
		soll4_6.add(o6);
		soll4_6.add(VERBINDUNG.getDataModel().
				getObject("bitctrl.dfs")); //$NON-NLS-1$
		soll4_6.add(o3);
		soll4_6.add(o5);

		soll5_6.add(o5);
		soll2_6.add(o6);
		soll6_6.add(o6);
		
		Assert.assertEquals("1.1: ", soll1_1, ist1_1); //$NON-NLS-1$
		Assert.assertEquals("2.1: ", soll2_1, ist2_1); //$NON-NLS-1$
		Assert.assertEquals("3.1: ", soll3_1, ist3_1); //$NON-NLS-1$
		Assert.assertEquals("4.1: ", soll4_1, ist4_1); //$NON-NLS-1$
		Assert.assertEquals("5.1: ", soll5_1, ist5_1); //$NON-NLS-1$
		Assert.assertEquals("6.1: ", soll6_1, ist6_1); //$NON-NLS-1$
		
		Assert.assertEquals("1.2: ", soll1_2, ist1_2); //$NON-NLS-1$
		Assert.assertEquals("2.2: ", soll2_2, ist2_2); //$NON-NLS-1$
		Assert.assertEquals("3.2: ", soll3_2, ist3_2); //$NON-NLS-1$
		Assert.assertEquals("4.2: ", soll4_2, ist4_2); //$NON-NLS-1$
		Assert.assertEquals("5.2: ", soll5_2, ist5_2); //$NON-NLS-1$
		Assert.assertEquals("6.2: ", soll6_2, ist6_2); //$NON-NLS-1$

		Assert.assertEquals("1.3: ", soll1_3, ist1_3); //$NON-NLS-1$
		Assert.assertEquals("2.3: ", soll2_3, ist2_3); //$NON-NLS-1$
		Assert.assertEquals("3.3: ", soll3_3, ist3_3); //$NON-NLS-1$
		Assert.assertEquals("4.3: ", soll4_3, ist4_3); //$NON-NLS-1$
		Assert.assertEquals("5.3: ", soll5_3, ist5_3); //$NON-NLS-1$
		Assert.assertEquals("6.3: ", soll6_3, ist6_3); //$NON-NLS-1$

		Assert.assertEquals("1.4: ", soll1_4, ist1_4); //$NON-NLS-1$
		Assert.assertEquals("2.4: ", soll2_4, ist2_4); //$NON-NLS-1$
		Assert.assertEquals("3.4: ", soll3_4, ist3_4); //$NON-NLS-1$
		Assert.assertEquals("4.4: ", soll4_4, ist4_4); //$NON-NLS-1$
		Assert.assertEquals("5.4: ", soll5_4, ist5_4); //$NON-NLS-1$
		Assert.assertEquals("6.4: ", soll6_4, ist6_4); //$NON-NLS-1$

		Assert.assertEquals("1.5: ", soll1_5, ist1_5); //$NON-NLS-1$
		Assert.assertEquals("2.5: ", soll2_5, ist2_5); //$NON-NLS-1$
		Assert.assertEquals("3.5: ", soll3_5, ist3_5); //$NON-NLS-1$
		Assert.assertEquals("4.5: ", soll4_5, ist4_5); //$NON-NLS-1$
		Assert.assertEquals("5.5: ", soll5_5, ist5_5); //$NON-NLS-1$
		Assert.assertEquals("6.5: ", soll6_5, ist6_5); //$NON-NLS-1$

		Assert.assertEquals("1.6: ", soll1_6, ist1_6); //$NON-NLS-1$
		Assert.assertEquals("2.6: ", soll2_6, ist2_6); //$NON-NLS-1$
		Assert.assertEquals("3.6: ", soll3_6, ist3_6); //$NON-NLS-1$
		Assert.assertEquals("4.6: ", soll4_6, ist4_6); //$NON-NLS-1$
		Assert.assertEquals("5.6: ", soll5_6, ist5_6); //$NON-NLS-1$
		Assert.assertEquals("6.6: ", soll6_6, ist6_6); //$NON-NLS-1$
	}

	
	/**
	 * Test von <code>testGetBasisInstanzen1()</code>
	 */
	@Test
	public void testGetBasisInstanzen1(){
		SystemObject o1 = VERBINDUNG.getDataModel().
							getObject("typ.messQuerschnittAllgemein"); //$NON-NLS-1$
		SystemObject o2 = VERBINDUNG.getDataModel().
							getObject("typ.messQuerschnitt"); //$NON-NLS-1$
		SystemObject o3 = VERBINDUNG.getDataModel().
							getObject("mq.a100.0010"); //$NON-NLS-1$
		SystemObject o4 = null;
		SystemObject o5 = VERBINDUNG.getDataModel().
								getObject("typ.typ"); //$NON-NLS-1$

		Collection<SystemObject> ist1 = DUAUtensilien.getBasisInstanzen(o1, VERBINDUNG);
		Collection<SystemObject> ist2 = DUAUtensilien.getBasisInstanzen(o2, VERBINDUNG);
		Collection<SystemObject> ist3 = DUAUtensilien.getBasisInstanzen(o3, VERBINDUNG);
		Collection<SystemObject> ist4 = DUAUtensilien.getBasisInstanzen(o4, VERBINDUNG);
		Collection<SystemObject> ist5 = DUAUtensilien.getBasisInstanzen(o5, VERBINDUNG);

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
					elem.getClass().equals(stauma.dav.configuration.meta.DynamicObject.class) || 
					elem.getClass().equals(stauma.dav.configuration.meta.ConfigurationAuthority.class)){
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

		SystemObject kObj = VERBINDUNG.getDataModel().getObject("typ.testPlPrüfungFormal");   //$NON-NLS-1$
		DataDescription kDataDesc = new DataDescription(
				VERBINDUNG.getDataModel().getAttributeGroup("atg.testPlPrüfungFormal"),   //$NON-NLS-1$
				VERBINDUNG.getDataModel().getAspect("asp.testEingang"), (short)0);   //$NON-NLS-1$
		boolean kSoll = false;
		
		Assert.assertTrue("A:", (DUAUtensilien.isKombinationOk(aObj, aDataDesc) == null) == aSoll);   //$NON-NLS-1$
		Assert.assertTrue("B:", (DUAUtensilien.isKombinationOk(bObj, bDataDesc) == null) == bSoll);   //$NON-NLS-1$
		Assert.assertTrue("C:", (DUAUtensilien.isKombinationOk(cObj, cDataDesc) == null) == cSoll);   //$NON-NLS-1$
		Assert.assertTrue("D:", (DUAUtensilien.isKombinationOk(dObj, dDataDesc) == null) == dSoll);   //$NON-NLS-1$
		Assert.assertTrue("E:", (DUAUtensilien.isKombinationOk(eObj, eDataDesc) == null) == eSoll);   //$NON-NLS-1$
		Assert.assertTrue("F:", (DUAUtensilien.isKombinationOk(fObj, fDataDesc) == null) == fSoll);   //$NON-NLS-1$
		Assert.assertTrue("G:", (DUAUtensilien.isKombinationOk(gObj, gDataDesc) == null) == gSoll);   //$NON-NLS-1$
		Assert.assertTrue("H:", (DUAUtensilien.isKombinationOk(hObj, hDataDesc) == null) == hSoll);   //$NON-NLS-1$
		Assert.assertTrue("I:", (DUAUtensilien.isKombinationOk(iObj, iDataDesc) == null) == iSoll);   //$NON-NLS-1$
		Assert.assertTrue("J:", (DUAUtensilien.isKombinationOk(jObj, jDataDesc) == null) == jSoll);   //$NON-NLS-1$
		Assert.assertTrue("K:", (DUAUtensilien.isKombinationOk(kObj, kDataDesc) == null) == kSoll);   //$NON-NLS-1$
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
		
		Assert.assertEquals("A:", aNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(aVorher, aErsetz));
		Assert.assertEquals("B:", bNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(bVorher, bErsetz));
		Assert.assertEquals("C:", cNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(cVorher, cErsetz));
		Assert.assertEquals("D:", dNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(dVorher, dErsetz));
		Assert.assertEquals("E:", eNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(eVorher, eErsetz));
		Assert.assertEquals("F:", fNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(fVorher, fErsetz));
		Assert.assertEquals("G:", gNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(gVorher, gErsetz));
		Assert.assertEquals("H:", hNachher, DUAUtensilien.   //$NON-NLS-1$
				ersetzeLetztesElemInAttPfad(hVorher, hErsetz));

	}

	/**
	 * Testet, ob bestimmte Eingabevarianten für den Attributpfad 
	 * zum richtigen Ergebnis führen.
	 */
	@Test
	public void testGetAttributDatum() {
		Data testDatum = VERBINDUNG.createData(VERBINDUNG.getDataModel().
				getAttributeGroup(PPFKonstanten.ATG));
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
			Data dummy = DUAUtensilien.getAttributDatum(pfade[j], testDatum);
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
