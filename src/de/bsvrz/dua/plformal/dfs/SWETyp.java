package de.bsvrz.dua.plformal.dfs;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.sweTyp</code> beschriebenen Werte zur
 * Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class SWETyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, SWETyp> WERTE_BEREICH = 
						new HashMap<Integer, SWETyp>();

	/**
	 * Alle Werte
	 */
	public static final SWETyp PL_PRUEFUNG_FORMAL = 
		new SWETyp("SWE_PL_Prüfung_formal", 1); //$NON-NLS-1$
	
	public static final SWETyp PL_PRUEFUNG_LOGISCH_LVE = 
		new SWETyp("SWE_PL_Prüfung_logisch_LVE", 2); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_UFD = 
		new SWETyp("SWE_PL_Prüfung_logisch_UFD", 3); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_WZG = 
		new SWETyp("SWE_PL_Prüfung_logisch_WZG", 4); //$NON-NLS-1$
	
	public static final SWETyp SWE_MESSWERTERSETZUNG_LVE = 
		new SWETyp("SWE_Messwertersetzung_LVE", 5); //$NON-NLS-1$
	
	public static final SWETyp SWE_ABFRAGE_PUFFERDATEN = 
		new SWETyp("SWE_Abfrage_Pufferdaten", 6); //$NON-NLS-1$
	
	public static final SWETyp SWE_DATENAUFBEREITUNG_LVE = 
		new SWETyp("SWE_Datenaufbereitung_LVE", 7); //$NON-NLS-1$
	
	public static final SWETyp SWE_DATENAUFBEREITUNG_UFD = 
		new SWETyp("SWE_Datenaufbereitung_UFD", 8); //$NON-NLS-1$
	
	public static final SWETyp SWE_AGGREGATION_LVE = 
		new SWETyp("SWE_Aggregation_LVE", 9); //$NON-NLS-1$
	
	public static final SWETyp SWE_ERGAENZUNG_BASt = 
		new SWETyp("SWE_Ergänzung_BASt", 10); //$NON-NLS-1$
	
	public static final SWETyp SWE_GUETEBERECHNUNG = 
		new SWETyp("SWE_Güteberechnung", 11); //$NON-NLS-1$
	
	public static final SWETyp SWE_MESSWERTERSETZUNG_UFD = 
		new SWETyp("SWE_Messwertersetzung_UFD", 12); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LANGZEIT_UFD = 
		new SWETyp("SWE_PL_Prüfung_Langzeit_UFD", 13); //$NON-NLS-1$
	
	public static final SWETyp SWE_GLAETTEWARNUNG_UND_PROGNOSE = 
		new SWETyp("SWE_DuA_Glättewarnung_und_Prognose", 14); //$NON-NLS-1$
	
	
	/**
	 * {@inheritDoc}
	 */
	private SWETyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param der Code des Enumerations-Wertes
	 */
	public static final SWETyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}

}
