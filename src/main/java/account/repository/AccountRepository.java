package account.repository;

import account.entity.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findUserByEmailIgnoreCase(String email);

    long count();

    long deleteAccountByEmail(String email);

    @Query("UPDATE Account u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String email);

}
