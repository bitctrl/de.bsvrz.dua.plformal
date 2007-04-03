package de.bsvrz.dua.plformal.allgemein;

import java.util.HashMap;

import stauma.dav.clientside.ClientDavInterface;
import stauma.dav.configuration.interfaces.SystemObjectType;

/**
 * Konstanten, die im Zusammenhang mit der Kommunikation mit dem
 * Datenverteiler (innerhalb der DUA) benötigt werden.
 * 
 * @author Thierfelder
 *
 */
public class DUAKonstanten {
	
		
	/*************************************************************
	 *                                                           *
	 *                       Allgemein                           *
	 *                                                           *
	 *************************************************************/

	public static final String ASP_PARA_SOLL = "asp.parameterSoll"; //$NON-NLS-1$
	
	public static final String ASP_PARA_VORGABE = "asp.parameterVorgabe"; //$NON-NLS-1$
	
	public static final long LONG_UNDEFINIERT = -1;
	
	public static final String STR_UNDEFINIERT = "undefiniert"; //$NON-NLS-1$
	
	public static final String TYP_TYP = "typ.typ"; //$NON-NLS-1$
	
	public static final String TYP_ATG = "typ.attributgruppe"; //$NON-NLS-1$
	
	public static final String EMPTY_STR = ""; //$NON-NLS-1$
	
	public static final String NULL = "<<null>>"; //$NON-NLS-1$
	
	public static SystemObjectType TYP_GANZ_ZAHL = null;
	
	public static SystemObjectType TYP_KOMMA_ZAHL = null;
	
	/**
	 * Kommandozeilenargument: KonfigurationsBereichsPid
	 */
	public static final String ARG_KONFIGURATIONS_BEREICHS_PID = "KonfigurationsBereichsPid"; //$NON-NLS-1$
		
	
	/*************************************************************
	 *                                                           *
	 *               Plausibilitätsprüfung Formal                *
	 *                                                           *
	 *************************************************************/
	
	public static final String TYP_PL_FORMAL = "typ.plausibilitätsPrüfungFormal"; //$NON-NLS-1$
	public static final String ATG_PL_FORMAL = "atg.plausibilitätsPrüfungFormal"; //$NON-NLS-1$
	public static final String ATL_PL_FORMAL_PARA_SATZ = "ParameterSatzPlausibilitätsPrüfungFormal"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ATG = "Attributgruppe"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ASP = "Aspekt"; //$NON-NLS-1$
	public static final String ATL_PL_FORMAL_PARA_SATZ_OBJ = "Objekt"; //$NON-NLS-1$
	public static final String ATL_PL_FORMAL_PARA_SATZ_ATT_SPEZ = "AttributSpezifikation"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_PFAD = "AttributPfad"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MIN = "Min"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_MAX = "Max"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_PARA_SATZ_ATT_SPEZ_OPT = "Optionen"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_STATUS_MIN = "Status.PlFormal.WertMin"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_STATUS_MAX = "Status.PlFormal.WertMax"; //$NON-NLS-1$
	public static final String ATT_PL_FORMAL_STATUS_IMPLAUSIBEL = "Status.MessWertErsetzung.Implausibel"; //$NON-NLS-1$

	/**
	 * Wertebereichsprüfungwird NICHT durchgeführt. Wert wird nicht verändert,
	 * es werden keine Statusflags gesetzt.
	 */
	public static final long ATT_PL_FORMAL_WERT_KEINE_PRUEFUNG = 0;
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Fehlerhafte Werte werden nicht
	 * verändert, es werden nur die Statusflags gesetzt.
	 */
	public static final long ATT_PL_FORMAL_WERT_NUR_PRUEFUNG = 1;
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsunter- bzw.
	 * überschreitung wird der Wert auf den parametrierten Min- bzw.
	 * /Max-Wert korrigiert und die Statusflags gesetzt.
	 */
	public static final long ATT_PL_FORMAL_WERT_SETZE_MINMAX = 2;
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsunterschreitung
	 * wird der Wert auf den parametrierten Min-Wert korrigiert und die
	 * Statusflags gesetzt, ansonsten Verhalten wie bei Option"NurPrüfen". 
	 */
	public static final long ATT_PL_FORMAL_WERT_SETZE_MIN = 3;
	
	/**
	 * Wertebereichsprüfung wird durchgeführt. Bei Bereichsüberschreitung
	 * wird der Wert auf den parametrierten Max-Wert korrigiert und die
	 * Statusflags gesetzt, ansonsten Verhalten wie bei Option"NurPrüfen".
	 */
	public static final long ATT_PL_FORMAL_WERT_SETZE_MAX = 4;

	/**
	 * Zuordnung von Methoden-ID zum Namen der Methode
	 */
	public static final HashMap<Long, String> ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT = new HashMap<Long, String>();
	static{
		ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.put(ATT_PL_FORMAL_WERT_KEINE_PRUEFUNG, "Keine Prüfung"); //$NON-NLS-1$
		ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.put(ATT_PL_FORMAL_WERT_NUR_PRUEFUNG, "Nur Prüfung"); //$NON-NLS-1$
		ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.put(ATT_PL_FORMAL_WERT_SETZE_MINMAX, "Setze MinMax"); //$NON-NLS-1$
		ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.put(ATT_PL_FORMAL_WERT_SETZE_MIN, "Setze Min"); //$NON-NLS-1$
		ATT_PL_PRUEFUNG_FORMAL_METHODEN_TEXT.put(ATT_PL_FORMAL_WERT_SETZE_MAX, "Setze Max");		 //$NON-NLS-1$
	}


	/*************************************************************
	 *                                                           *
	 *                    Datenflusssteuerung                    *
	 *                                                           *
	 *************************************************************/

	public static final String TYP_DFS = "typ.datenflussSteuerung"; //$NON-NLS-1$
		
	public static final String ATG_DFS = "atg.datenflussSteuerung"; //$NON-NLS-1$
	
	public static final String ATL_DFS_PARA_SATZ = "ParameterSatz"; //$NON-NLS-1$
	
	public static final String ATT_DFS_OBJ = "Objekt"; //$NON-NLS-1$
	
	public static final String ATT_DFS_ATG = "AttributGruppe"; //$NON-NLS-1$
	
	public static final String ATT_DFS_ASP = "PublikationsAspekt"; //$NON-NLS-1$
	
	public static final String ATT_DFS_SWE = "SWE"; //$NON-NLS-1$
	
	public static final String ATT_DFS_MODUL_TYP = "ModulTyp"; //$NON-NLS-1$
	
	public static final String ATT_DFS_PUB_ZUORDNUNG = "PublikationsZuordnung"; //$NON-NLS-1$
	
	public static final String ATT_DFS_PUBLIZIEREN = "Publizieren"; //$NON-NLS-1$
		
	/**
	 * Initialisiert diese Klasse statisch und liest alle Konstanten ein,
	 * die nur in Zusammenhang mit einer Datenverteilerverbindung ermittelt 
	 * werden können.
	 * 
	 * @param dav Verbindung zum Datenverteiler
	 */
	public static final void initialisiere(final ClientDavInterface dav){
		TYP_GANZ_ZAHL = dav.getDataModel().getType("typ.ganzzahlAttributTyp"); //$NON-NLS-1$
		TYP_KOMMA_ZAHL = dav.getDataModel().getType("typ.kommazahlAttributTyp"); //$NON-NLS-1$
	}

}
