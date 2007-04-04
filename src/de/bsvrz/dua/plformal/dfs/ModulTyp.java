package de.bsvrz.dua.plformal.dfs;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * �ber diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.modulTyp</code> beschriebenen Werte zur
 * Verf�gung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class ModulTyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, ModulTyp> WERTE_BEREICH = 
						new HashMap<Integer, ModulTyp>();

	/**
	 * Alle Werte
	 */
	public static final ModulTyp PL_PRUEFUNG_FORMAL = 
		new ModulTyp("PlPr�fungFormal", 1); //$NON-NLS-1$
	
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_UFD = 
		new ModulTyp("PlPr�fungLogischUFD", 2); //$NON-NLS-1$
	
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_WZG = 
		new ModulTyp("PlPr�fungLogischWZG", 3); //$NON-NLS-1$
	
	public static final ModulTyp MESSWERTERSETZUNG_LVE = 
		new ModulTyp("PlPr�fungMesswertErsetzungLVE", 4); //$NON-NLS-1$

	public static final ModulTyp MESSWERTERSETZUNG_UFD = 
		new ModulTyp("PlPr�fungMesswertErsetzungUFD", 5); //$NON-NLS-1$

	public static final ModulTyp PL_PRUEFUNG_LANGZEIT_UFD = 
		new ModulTyp("PlPr�fungLangZeitUFD", 6); //$NON-NLS-1$
		
	
	/**
	 * {@inheritDoc}
	 */
	private ModulTyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem �bergebenen Code
	 *
	 * @param der Code des Enumerations-Wertes
	 */
	public static final ModulTyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}

