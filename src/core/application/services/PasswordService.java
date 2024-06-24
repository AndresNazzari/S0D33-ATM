package core.application.services;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {

    public static String hashPassword(String password) {
        return password;

        //return  BCrypt.hashpw(password, BCrypt.gensalt() );
    }

    public static boolean checkPassword(String candidate, String hashedPassword){
        return candidate.equals(hashedPassword);
        //return BCrypt.checkpw(candidate, hashedPassword);
    }
}
