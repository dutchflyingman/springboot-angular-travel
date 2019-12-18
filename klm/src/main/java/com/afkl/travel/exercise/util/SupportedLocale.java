package com.afkl.travel.exercise.util;

import java.util.Locale;

public class SupportedLocale {

    // If Future requires french for AF, Make a list as supported locales.
        static public final Locale NL = new Locale.Builder().setLanguage("nl").setRegion("NL").build();

        public static String getLanguage(String languageHeader){
            return (languageHeader!=null && NL.equals(Locale.forLanguageTag(languageHeader)) )? NL.getLanguage() : Locale.ENGLISH.getLanguage();
        }
    }

