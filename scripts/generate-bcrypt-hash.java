// Script simple pour générer un hash BCrypt
// Compiler et exécuter: javac -cp "path/to/spring-security-crypto.jar" generate-bcrypt-hash.java && java -cp ".:path/to/spring-security-crypto.jar" GenerateBcryptHash

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateBcryptHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = args.length > 0 ? args[0] : "admin123";
        String hash = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
    }
}

