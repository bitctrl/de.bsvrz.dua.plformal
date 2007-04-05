package de.bsvrz.dua.plformal.plformal;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;

import stauma.dav.clientside.Data;
import stauma.dav.clientside.DataDescription;
import stauma.dav.clientside.ResultData;
import stauma.dav.configuration.interfaces.Aspect;
import stauma.dav.configuration.interfaces.AttributeGroup;
import stauma.dav.configuration.interfaces.SystemObject;
import stauma.dav.configuration.interfaces.SystemObjectType;
import sys.funclib.debug.Debug;
import de.bsvrz.dua.plformal.allgemein.DUAKonstanten;
import de.bsvrz.dua.plformal.av.DAVObjektAnmeldung;

/**
 * Diese Klasse stellt die Informationen der Attributgruppe
 * <code>atg.plausibilitätsPrüfungFormal</code> für einen
 * möglichst schnellen Zugriff zur Verfügung. Mittels der
 * Methode <code>getAttributSpezifikationenFuer(..)</code>
 * kann für ein ResultData-Objekt erfragt werden, wie dieses
 * formal zu plausibilisieren ist.
 * 
 * @author Thierfelder
 *
 */
public class PPFDatenBeschreibungen {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();
	
	/**
	 * Nach Attributgruppen sortierter Suchbaum. Jedes Element enthält alle Informationen,
	 * die zur formalen Plausibilisierung eines beliebigen Datums der gesuchten Attributgruppe
	 * notwendig sind.
	 */
	private TreeMap<AttributeGroup, BeschreibungFuerAttributgruppe> atgInfos =
				new TreeMap<AttributeGroup, BeschreibungFuerAttributgruppe>(); 
	
	/**
	 * Fügt diesem Objekt einen neuen Parametersatz der Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code>
	 * hinzu. Sollte die innerhalb des Parametersatzes angegebene Attributgruppe schon innerhalb des Baums
	 * <code>atgInfos</code> enthalten sein, so wird die Beschreibung dieser Attributgruppe lediglich
	 * ergänzt.  
	 * 
	 * @param parameterSatz ein Parametersatz der Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code>
	 * @throws Exception falls Fehler beim Auslesen des DAV-Datensatzes auftreten
	 */
	public final void addParameterSatz(final Data parameterSatz)
	throws Exception{
		AttributeGroup atg = (AttributeGroup)parameterSatz.getReferenceValue(
				DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ATG).getSystemObject();
		BeschreibungFuerAttributgruppe dummy = atgInfos.get(atg);
		if(dummy != null){
			dummy.addParameterSatz(parameterSatz);
		}else{
			dummy = new BeschreibungFuerAttributgruppe();
			dummy.addParameterSatz(parameterSatz);
			this.atgInfos.put(atg, dummy);
		}
	}
	
	/**
	 * 
	 * @param resultat
	 * @return
	 */
	protected final Collection<PPFAttributSpezifikation> getAttributSpezifikationenFuer(ResultData resultat){
		Collection<PPFAttributSpezifikation> ergebnis = null;
		
		if(resultat != null){
			final DataDescription dd = resultat.getDataDescription();
			if(dd != null){
				final AttributeGroup atg = dd.getAttributeGroup();
				if(atg != null){
					final BeschreibungFuerAttributgruppe atgBeschreibung = this.atgInfos.get(atg);
					if(atgBeschreibung != null){
						final Aspect asp = dd.getAspect();
						if(asp != null){
							final BeschreibungFuerAspekt aspBeschreibung = atgBeschreibung.getBeschreibungFuerAspekt(asp);
							if(aspBeschreibung != null){
								final BeschreibungFuerObjekt objBeschreibung = aspBeschreibung.getBeschreibungFuerObjekt(resultat.getObject());
								if(objBeschreibung != null){
									ergebnis = objBeschreibung.getAttributSpezifikationen();
								}else{
									LOGGER.info("Für Objekt " + resultat.getObject() + " wurde nichts gefunden."); //$NON-NLS-1$ //$NON-NLS-2$
								}
							}else{
								LOGGER.info("Aspekt " + asp + " wurde nicht gefunden."); //$NON-NLS-1$ //$NON-NLS-2$
							}
						}else{
							LOGGER.info("Übergebenet Aspekt ist " + DUAKonstanten.NULL); //$NON-NLS-1$
						}						
					}else{
						LOGGER.info("Attributgruppe " + atg + " wurde nicht gefunden."); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}else{
					LOGGER.info("Übergebene Attributgruppe ist " + DUAKonstanten.NULL); //$NON-NLS-1$
				}
			}else{
				LOGGER.info("Übergebene DataDescription ist " + DUAKonstanten.NULL); //$NON-NLS-1$
			}
		}else{
			LOGGER.info("Übergebenes ResultData-Objekt ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}
		
		return ergebnis;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = DUAKonstanten.STR_UNDEFINIERT;
		
		if(atgInfos.keySet().size() > 0){
			s = DUAKonstanten.EMPTY_STR;
			for(SystemObject atg:atgInfos.keySet()){
				s += "Attributgruppe: " + atg + "\n";  //$NON-NLS-1$//$NON-NLS-2$
				s += this.atgInfos.get(atg);
			}
		}
		
		return s;
	}			
	
	
	/*****************************************************************************
	 * Interne Klassen                                                           *
	 *****************************************************************************/
	
	protected class DAVObjektPlausibilisierung
	extends DAVObjektAnmeldung{
		
		protected DAVObjektPlausibilisierung(final SystemObject obj,
								final DataDescription datenBeschreibung)
		throws Exception{
			super(obj, datenBeschreibung);
		}
		
				
	}
	
	/**
	 * Diese Klasse stellt die Informationen der Attributgruppe <code>atg.plausibilitätsPrüfungFormal</code>
	 * in Bezug auf einen bestimmten Aspekt für einen möglichst schnellen Zugriff zur Verfügung. Mittels
	 * der Methode <code>getBeschreibungFuerAspekt(..)</code> kann für einen bestimmten Aspekt erfragt
	 * werden, wie 
	 * 
	 * @author Thierfelder
	 *
	 */
	protected class BeschreibungFuerAttributgruppe{
			
		/**
		 * 
		 */
		private TreeMap<Aspect, BeschreibungFuerAspekt> aspInfos =
				  new TreeMap<Aspect, BeschreibungFuerAspekt>();
		
		/**
		 * 
		 * @param parameterSatz
		 */
		protected void addParameterSatz(final Data parameterSatz)
		throws Exception{
			Aspect asp = (Aspect)parameterSatz.getReferenceValue(DUAKonstanten.ATT_PL_FORMAL_PARA_SATZ_ASP).getSystemObject();
			BeschreibungFuerAspekt dummy = aspInfos.get(asp);
			if(dummy != null){
				dummy.addParameterSatz(parameterSatz);
			}else{
				dummy = new BeschreibungFuerAspekt();
				dummy.addParameterSatz(parameterSatz);
				this.aspInfos.put(asp, dummy);
			}
		}
		
		/**
		 * 
		 * @param aspekt
		 * @return
		 */
		protected final BeschreibungFuerAspekt getBeschreibungFuerAspekt(final Aspect aspekt){
			return this.aspInfos.get(aspekt);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = DUAKonstanten.STR_UNDEFINIERT;
			
			if(aspInfos.keySet().size() > 0){
				s = DUAKonstanten.EMPTY_STR;
				for(SystemObject asp:aspInfos.keySet()){
					s += "Aspekt: " + asp + "\n";  //$NON-NLS-1$//$NON-NLS-2$
					s += this.aspInfos.get(asp);
				}
			}
			
			return s;
		}				
	}
	
	
	/**
	 * 
	 * @author Thierfelder
	 *
	 */
	protected class BeschreibungFuerAspekt{

		/**
		 * 
		 */
		private TreeMap<SystemObject, BeschreibungFuerObjekt> objInfos =
			  new TreeMap<SystemObject, BeschreibungFuerObjekt>();
		
		/**
		 * 
		 * @param parameterSatz
		 */
		protected final void addParameterSatz(final Data parameterSatz)
		throws Exception{
			Data.Array objekte = parameterSatz.getArray(DUAKonstanten.ATL_PL_FORMAL_PARA_SATZ_OBJ);
			for(int i = 0; i<objekte.getLength(); i++){
				SystemObject obj = objekte.getItem(i).asReferenceValue().getSystemObject();
				BeschreibungFuerObjekt dummy = objInfos.get(obj);
				if(dummy != null){
					dummy.addParameterSatz(parameterSatz);
				}else{
					dummy = new BeschreibungFuerObjekt();
					dummy.addParameterSatz(parameterSatz);
					this.objInfos.put(obj, dummy);
				}				
			}
		}
		
		/**
		 * Erfragt die Beschreibung zur formalen Plausibilisierung eines bestimmten
		 * Objektes. Solltes dieses Objekt nicht direkt parametriert sein, so wird ggf.
		 * die Beschreibung des Typen zurückgegeben, von dem das übergebene Objekt eine
		 * Instanz ist. 
		 * 
		 * @param objekt das formal zu plausibilisierende Objekt 
		 * @return die Beschreibung zur formalen Plausibilisierung dieses Objektes
		 */
		public final BeschreibungFuerObjekt getBeschreibungFuerObjekt(final SystemObject objekt){
			BeschreibungFuerObjekt ergebnis = this.objInfos.get(objekt);
			
			if(ergebnis == null){
				for(SystemObject objAusBaum:objInfos.keySet()){
					if(objAusBaum.isOfType(DUAKonstanten.TYP_TYP)){
						SystemObjectType typ = (SystemObjectType)objAusBaum;
						if(objekt.isOfType(typ)){
							ergebnis = this.objInfos.get(objAusBaum);
							break;
						}
					}
				}
			}
			
			return ergebnis;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = DUAKonstanten.STR_UNDEFINIERT;
			
			if(objInfos.keySet().size() > 0){
				s = DUAKonstanten.EMPTY_STR;
				for(SystemObject obj:objInfos.keySet()){
					s += "Objekt: " + obj + "\n";  //$NON-NLS-1$//$NON-NLS-2$
					s += this.objInfos.get(obj);
				}
			}
			
			return s;
		}				
	}
	
	
	/**
	 * 
	 * @author Thierfelder
	 *
	 */
	protected class BeschreibungFuerObjekt{

		/**
		 * 
		 */
		private Collection<PPFAttributSpezifikation> attInfos =
					new HashSet<PPFAttributSpezifikation>();
		
		/**
		 * 
		 * @param parameterSatz
		 */
		protected final void addParameterSatz(final Data parameterSatz)
		throws Exception{
			final Data.Array attribut = parameterSatz.getArray(DUAKonstanten.ATL_PL_FORMAL_PARA_SATZ_ATT_SPEZ);
			
			for(int i = 0; i<attribut.getLength(); i++){
				final Data attributSpezifikation = attribut.getItem(i);
				PPFAttributSpezifikation dummy = new PPFAttributSpezifikation(attributSpezifikation);
				attInfos.add(dummy);
			}
		}

		/**
		 * 
		 * @param resultat
		 * @return
		 */
		protected final Collection<PPFAttributSpezifikation> getAttributSpezifikationen(){
			return this.attInfos;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = DUAKonstanten.STR_UNDEFINIERT;
			
			if(attInfos.size() > 0){
				s = DUAKonstanten.EMPTY_STR;
				for(PPFAttributSpezifikation attBeschreibung:attInfos){
					s += attBeschreibung;
				}
			}
			
			return s;
		}
	}
}
