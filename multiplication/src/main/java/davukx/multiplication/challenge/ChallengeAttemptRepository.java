package davukx.multiplication.challenge;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChallengeAttemptRepository extends CrudRepository<ChallengeAttempt, Long> {
    List<ChallengeAttempt> findTop10ByUserAliasOrderByIdDesc(String userAlias);
    @Query("SELECT a FROM ChallengeAttempt a WHERE a.user.alias = :userAlias ORDER BY a.id DESC")
    List<ChallengeAttempt> lastAttempts(@Param("userAlias") String userAlias);
}
