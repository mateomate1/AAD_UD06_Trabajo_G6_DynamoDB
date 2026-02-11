package g6.dynamodb.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class UsersPasswordsData implements Serializable{
    private static final long serialVersionUID = 1L;

    public static final int MAX_USERS = 10, MAX_CHARS = 10, MIN_CHARS = 5;
    public static final String
            USABLE_CHARS =
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + // Letras mayúsculas
                    "abcdefghijklmnopqrstuvwxyz" + // Letras minúsculas
                    "0123456789" + // Números
                    "!#$%&()*+,-./:;<=>?@[]^_`{|}~", // Caracteres especiales permitidos; '=0
            usersDataFile = "UsersData.dat";
    private Map<String, String> users = new HashMap<>();

    public UsersPasswordsData() {
        users = getUsers();
        if(users == null){
            updateUsers();
            users = new HashMap<>();
        }
    }

    public Map<String, String> getUsers() {
        Map<String, String> salida = new HashMap<>();
        File file = new File(usersDataFile);
        if (!file.exists()) {
            updateUsers();
        }
        try {
            FileInputStream fileIn = new FileInputStream(usersDataFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            // Leer el Map directamente desde el archivo
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
        if(salida == null){
            return new HashMap<>();
        }
        return salida;
    }

    /**
     * Metodo que agrega un usuario al Map y al archivo ".dat"
     *
     * Este metodo verifica si el usuario ya existe en el mapa de usuarios. Si el usuario no existe y no se ha alcanzado
     * el limite de usuarios, el nuevo usuario es añadido al mapa y posteriormente guardado en el archivo ".dat".
     *
     * @param user Nombre del usuario que se desea agregar.
     * @param pass Contraseña del usuario.
     * @return Un código entero que indica el estado de la operación:
     *         -1: El usuario ya existe en el sistema.
     *          0: El usuario fue agregado correctamente.
     *          1: El número máximo de usuarios ha sido alcanzado.
     */
    public int addUser(String user, String pass){
        if(users.containsKey(user))
            return -1; // Codigo usuario repetido
        else if(users.size() >= MAX_USERS) {
            return 1; // Codigo limite de usuarios alcanzado
        }
        else {
            users.put(user, pass);
            updateUsers();
            return 0; // Codigo usuario añadido
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
     *         -1: El nombre de usuario o la contraseña contienen caracteres no validos.
     *         0: El nombre de usuario o la contraseña no cumplen con las especificaciones necesarias.
     *         1: El nombre de usuario y la contrasena son validos.
     */
    public static int validar(String user, String pass){
        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            return -1; // Usuario o contraseña vacíos
        }

        if (user.length() < MIN_CHARS || user.length() > MAX_CHARS || pass.length() < MIN_CHARS || pass.length() > MAX_CHARS) {
            return 0; // Longitud fuera del rango permitido
        }

        for(Character c : user.toCharArray()){
            if (USABLE_CHARS.indexOf(c) == -1) {
                return -1; // Codigo usuario no valido
            }
        }
        boolean hasUppercase = false;
        boolean hasDigit = false;
        for(Character c : pass.toCharArray()){
            if (USABLE_CHARS.indexOf(c) == -1) {
                return -1; // Codigo contraseña no valida
            }
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        if (!hasUppercase || !hasDigit) {
            return 0; // La contraseña no cumple con los requisitos de seguridad
        }
        return 1; // Codigo validado
    }

    /**
     * Metodo que actualiza el archivo de usuarios con los datos actuales del mapa de usuarios.
     *
     * Este metodo guarda la informacion de los usuarios en un archivo ".dat" utilizando streams de objetos.
     * Si ocurre algun error durante el proceso de escritura (por ejemplo, archivo no encontrado o error de I/O),
     * el metodo devuelve un codigo de error correspondiente.
     *
     * @return Un codigo entero que indica el estado de la operacion:
     *         0: El archivo no fue encontrado/no existe.
     *         -1: Ocurrio un error de entrada/salida al intentar guardar los datos.
     *         1: Los datos fueron guardados correctamente.
     */
    public int updateUsers(){
        if (users == null)
            users = new HashMap<>();
        try {
            FileOutputStream fileOut = new FileOutputStream(usersDataFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
        } catch (FileNotFoundException e) {
            return 0;
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public String encode(String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(pass.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xFF & hash[i]);
                if(hex.length()==1){
                    hexString.append('0');
                    hexString.append(hex);
                }
                pass = hexString.toString();
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            pass = "FAILED HASH";
            throw e;
        }
        return pass;
    }

    /**
     * Metodo que autentica a un usuario verificando su nombre de usuario y contraseña.
     *
     * Este metodo recupera los usuarios almacenados en el archivo, verifica si el usuario existe
     * y compara la contraseña ingresada con la almacenada en el sistema tras codificarla.
     *
     * @param user Nombre del usuario que se desea autenticar.
     * @param pass Contraseña del usuario en <b>texto plano</b>.
     * @return Un código entero que indica el resultado de la autenticación:
     *         -1: El usuario no existe en el sistema.
     *          0: La contraseña ingresada es incorrecta.
     *          1: La autenticación fue exitosa (usuario y contraseña correctos).
     *         -2: Se produjo un error en la codificación de la contraseña.
     */
    public int authenticateUser(String user, String pass) {
        // Recuperar los usuarios del archivo
        Map<String, String> usersMap = getUsers();
        String temp = pass;
        // Comprobar si el usuario existe
        if (!usersMap.containsKey(user)) {
            return -1; // El usuario no existe
        }

        try {
            pass = encode(pass); // Codificar la contraseña ingresada
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            return -2;
        }

        // Comparar la contraseña codificada con la guardada en el mapa
        String storedPass = usersMap.get(user);
        if (pass.equals(storedPass)) {
            return 1; // Contraseña correcta
        } else {
            System.out.println("Tuya: " + temp + " Cifrada: " + pass + "Recojida: " + storedPass);
            return 0; // Contraseña incorrecta
        }
    }

}
