package account.bruteforce;

public interface BruteForceProtectionService {

    void registerLoginFailure(String username);
    void resetBruteForceCounter(String username);
//    void isBruteForceAttack();
}
