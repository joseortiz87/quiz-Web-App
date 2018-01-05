package server.security;

import server.utility.Globals;

public class XORController {


        /**
         Kryptering skal kunne slås til og fra i configfilen
         Fremgansmåde:
         Klienten sender et krypteret Json objekt til serveren.
         Krypteringen hos klienten medfører, at objektet ikke længere er JSON, men blot en ciffertekst.
         Derfor skal cifferteksten parses til JSON, således at serveren kan modtage det.
         Det modtagede JSON objekt unparses fra JSON til ciffertekst, således at det kan dekrypteres.
         Efter objektet er dekrypteret er det igen JSON.
         Herefter unparses objektet fra JSON igen, således at vi kan bruge objektet i serveren.
         */

        // Samzme metode skal laves på klienten for at dekryptere!!
        public static String encryptDecryptXOR(String willBeEncrypted) {
            //If: HER SKAL VI HAVE json.get("ENCRYPTION").getAsBoolean();
            if(Globals.config.getEncryption()) {
                //Vi vælger selv værdierne til nøglen
                char[] key = {'K', 'O', 'C', 'H'};
                //En StringBuilder er en klasse, der gør det muligt at ændre en string
                StringBuilder thisIsEncrypted = new StringBuilder();

                for (int i = 0; i < willBeEncrypted.length(); i++) {
                    thisIsEncrypted.append((char) (willBeEncrypted.charAt(i) ^ key[i % key.length]));
                }
                return thisIsEncrypted.toString();
            } else {
                return willBeEncrypted;
            }

        }

}
