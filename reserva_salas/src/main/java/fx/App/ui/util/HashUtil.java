package fx.App.ui.util;

import java.security.MessageDigest;

public class HashUtil {

    // Atributos de la clase
    public static final int MAX_USERS = 10, MAX_CHARS = 10, MIN_CHARS = 5;

    // Conjunto de caracteres permitidos para nombres de usuario y contraseñas, incluyendo letras mayúsculas, minúsculas, números y caracteres especiales.
    public static final String
            USABLE_CHARS =
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + // Letras mayúsculas
                    "abcdefghijklmnopqrstuvwxyz" + // Letras minúsculas
                    "0123456789" + // Números
                    "!#$%&()*+,-./:;<=>?@[]^_`{|}~", // Caracteres especiales permitidos; '=0
            usersDataFile = "UsersData.dat"; // Archivo donde se almacenarán los datos de usuarios y contraseñas


    /**
     * Método estático que codifica una contraseña utilizando el algoritmo de hash SHA-256. 
      * Este método toma una cadena de texto (la contraseña) como entrada, la codifica utilizando SHA-256 y devuelve el resultado como una cadena hexadecimal.
      * @param pass La contraseña que se desea codificar.
      * @return La contraseña codificada en formato hexadecimal.
      * @throws RuntimeException Si ocurre un error durante el proceso de codificación, se lanza una RuntimeException con un mensaje descriptivo del error.
      */ 
    public static String encode (String pass){
        try {
            // Crear una instancia de MessageDigest utilizando el algoritmo SHA-256 para codificar la contraseña.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Codificar la contraseña proporcionada por el usuario utilizando el método digest() de MessageDigest, que devuelve un array de bytes con el hash resultante.
            byte[] hash = digest.digest(pass.getBytes("UTF-8"));

            // Convertir el array de bytes del hash a una representación hexadecimal utilizando un StringBuilder para construir la cadena resultante.
            StringBuilder hexString = new StringBuilder();
            // Iterar sobre cada byte del hash y convertirlo a su representación hexadecimal utilizando String.format() para formatear cada byte como un par de dígitos hexadecimales.
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al codificar la contraseña. " +e.getMessage());        
        }
    }

    /**
     * Metodo que valida un nombre de usuario y una contrasena segun los caracteres permitidos.
     *
     * Este metodo revisa que cada caracter del nombre de usuario y de la contrasena
     * pertenezca a un conjunto de caracteres validos. Si alguno de los caracteres no es valido,
     * se devuelve un codigo de error correspondiente.
     *
     * @param user El nombre de usuario a validar.
     * @param pass La contrasena a validar.
     * @return Un codigo entero que indica el estado de la validacion:
     *        ERROR_CARACTERES -1: El nombre de usuario o la contraseña contienen caracteres no validos.
     *       ERROR_REQUISITOS  0: El nombre de usuario o la contraseña no cumplen con las especificaciones necesarias.
     *         OK 1: El nombre de usuario y la contrasena son validos.
     */
    public static int validar(String user, String pass){

        final int ERROR_CARACTERES = -1;
        final int ERROR_REQUISITOS = 0;
        final int OK = 1;

        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            return ERROR_CARACTERES; // Usuario o contraseña vacíos
        }

        if (user.length() < MIN_CHARS || user.length() > MAX_CHARS || pass.length() < MIN_CHARS || pass.length() > MAX_CHARS) {
            return ERROR_REQUISITOS; // Longitud fuera del rango permitido
        }

        for(Character c : user.toCharArray()){
            if (USABLE_CHARS.indexOf(c) == -1) {
                return ERROR_CARACTERES; // Codigo usuario no valido
            }
        }
        boolean hasUppercase = false;
        boolean hasDigit = false;
        for(Character c : pass.toCharArray()){
            if (USABLE_CHARS.indexOf(c) == -1) {
                return ERROR_CARACTERES; // Codigo contraseña no valida
            }
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUppercase || !hasDigit) {
            return ERROR_REQUISITOS; // La contraseña no cumple con los requisitos de seguridad
        }
        return OK; // Codigo validado
    }
}
