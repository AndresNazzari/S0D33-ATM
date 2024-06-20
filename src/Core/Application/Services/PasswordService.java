package Core.Application.Services;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {

    public static String hashPassword(String password) {
       return  BCrypt.hashpw(password, BCrypt.gensalt(12) );
    }

    public static boolean checkPassword(String candidate, String hashedPassword){
        return BCrypt.checkpw(candidate, hashedPassword);
    }
}
