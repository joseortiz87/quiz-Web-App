package server.security;
import java.security.MessageDigest;

public class Digester {

private String salt;

private static MessageDigest digester;

static{

    try {
        digester = MessageDigest.getInstance("MD5");
    } catch (Exception e){
        e.printStackTrace();
    }

}

public void setSalt(String salt){
    this.salt = salt;
}

public static String hash(String password){
    return Digester.performHashing(password);
}

public static String hashWithSalt(String password, String username, Long createdTime){
    String salt = Digester.performHashing(username+createdTime.toString());
    password = password + salt;
    return Digester.performHashing(password);
}

private static String performHashing(String str){
        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            if ((0xff & aHash) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & aHash)));
            } else {
                hexString.append(Integer.toHexString(0xFF & aHash));
            }
        }
        System.out.printf(hexString.toString());
        return hexString.toString();
    }

}
