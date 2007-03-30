package de.bsvrz.dua.plformal.allgemein;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Meldungen {
	private static final String BUNDLE_NAME = "de.bsvrz.dua.plformal.allgemein.meldungen"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Meldungen() {
		//
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
