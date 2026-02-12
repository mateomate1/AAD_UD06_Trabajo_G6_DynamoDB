package util;

/**
Gestor persistente usuarios/contrasenas (serializacion .dat).
* Almacena max 10 usuarios con validacion estricta (5-10 chars, mayusculas/digitos).
* Autenticacion SHA-256 + USABLE_CHARS restringidos. Compatible login basico.
* @author Mario Garcia
* @author Mateo Ayarra
* @author Samuel Cobreros
* @author Zacaria Daghri
* @version 1.0
* @since 1.0
 */
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UsersPasswordsData implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int MAX_USERS = 10, MAX_CHARS = 10, MIN_CHARS = 5;
    public static final String
            USABLE_CHARS =
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +     // Letras mayusculas
                    "abcdefghijklmnopqrstuvwxyz" +     // Letras minusculas
                    "0123456789" +                   // Numeros
                    "!#$%&()*+,-./:;<=>?@[]^_`{|}~", // Especiales permitidos
            usersDataFile = "UsersData.dat";
    private Map<String, String> users = new HashMap<>();

    /**
    Constructor carga usuarios desde UsersData.dat.
    * Inicializa vacio si archivo corrupto/invalido.
    */
    public UsersPasswordsData() {
        users = getUsers();
        if (users == null) {
            updateUsers();
            users = new HashMap<>();
        }
    }

    /**
    Lee usuarios serializados desde UsersData.dat.
    * Crea archivo vacio si no existe.
    * @return Map<String,String> username=hash o vacio
    */
    public Map<String, String> getUsers() {
        Map<String, String> salida = new HashMap<>();
        File file = new File(usersDataFile);
        if (!file.exists()) {
            updateUsers();
        }
        try {
            FileInputStream fileIn = new FileInputStream(usersDataFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            salida = (Map<String, String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada");
        } catch (IOException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return salida != null ? salida : new HashMap<>();
    }

    /**
    Agrega usuario validado al mapa/archivo.
    * @param user nombre usuario (unico)
    * @param pass contrasena texto plano (se hashea internamente)
    * @return -1=existe, 1=max usuarios, 0=OK
    */
    public int addUser(String user, String pass) {
        if (users.containsKey(user))
            return -1; // Usuario repetido
        else if (users.size() >= MAX_USERS)
            return 1;  // Limite alcanzado
        else {
            users.put(user, pass);
            updateUsers();
            return 0;  // Aniadido OK
        }
    }

    /**
    Valida user/pass segun politicas seguridad.
    * Requisitos: 5-10 chars, USABLE_CHARS, pass requiere mayuscula+digito.
    * @param user nombre validar
    * @param pass contrasena validar
    * @return -1=caracteres invalidos, 0=longitud/requisitos, 1=valido
    */
    public static int validar(String user, String pass) {
        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            return -1; // Vacios
        }

        if (user.length() < MIN_CHARS || user.length() > MAX_CHARS || 
            pass.length() < MIN_CHARS || pass.length() > MAX_CHARS) {
            return 0; // Longitud invalida
        }

        for (Character c : user.toCharArray()) {
            if (USABLE_CHARS.indexOf(c) == -1) {
                return -1; // Usuario caracteres invalidos
            }
        }
        
        boolean hasUppercase = false;
        boolean hasDigit = false;
        for (Character c : pass.toCharArray()) {
            if (USABLE_CHARS.indexOf(c) == -1) {
                return -1; // Contrasena caracteres invalidos
            }
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUppercase || !hasDigit) {
            return 0; // Contrasena insegura
        }
        return 1; // Valido
    }

    /**
    Persiste mapa usuarios a UsersData.dat.
    * @return 0=archivo no encontrado, -1=IO error, 1=OK
    */
    public int updateUsers() {
        if (users == null)
            users = new HashMap<>();
        try {
            FileOutputStream fileOut = new FileOutputStream(usersDataFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
            return 1; // Guardado OK
        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
    Codifica contrasena SHA-256 hex.
    * @param pass texto plano UTF-8
    * @return hash hex 64 chars o "FAILED HASH"
    * @throws UnsupportedEncodingException UTF-8 no soportado
    * @throws NoSuchAlgorithmException SHA-256 ausente
    */
    public String encode(String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            pass = "FAILED HASH";
            throw e;
        }
    }

    /**
    Autentica usuario comparando hash.
    * @param user nombre usuario
    * @param pass texto plano (se hashea)
    * @return -1=no existe, 0=pass incorrecta, 1=OK, -2=hash error
    */
    public int authenticateUser(String user, String pass) {
        Map<String, String> usersMap = getUsers();
        String temp = pass;
        
        if (!usersMap.containsKey(user)) {
            return -1; // Usuario inexistente
        }

        try {
            pass = encode(pass); // Hash input
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            return -2; // Error codificacion
        }

        String storedPass = usersMap.get(user);
        if (pass.equals(storedPass)) {
            return 1; // Autenticado OK
        } else {
            System.out.println("Tuya: " + temp + " Cifrada: " + pass + " Recogida: " + storedPass);
            return 0; // Pass incorrecta
        }
    }
}
