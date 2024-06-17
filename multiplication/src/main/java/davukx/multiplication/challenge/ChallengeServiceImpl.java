package davukx.multiplication.challenge;

import davukx.multiplication.user.User;
import davukx.multiplication.user.UserRepository;
import davukx.multiplication.serviceClients.GamificationServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeAttemptRepository attemptRepository;
    private final GamificationServiceClient gameClient;

    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
        User user = userRepository.findByAlias(attemptDTO.getUserAlias())
                .orElseGet(() -> {
                    log.info("Creating new users with alias {}",
                            attemptDTO.getUserAlias());
                    return userRepository.save(
                            new User(attemptDTO.getUserAlias())
                    );
                });

        boolean isCorrect = attemptDTO.getGuess() ==
                attemptDTO.getFactorA() * attemptDTO.getFactorB();

        ChallengeAttempt checkedAttempt = new ChallengeAttempt(null,
                user,
                attemptDTO.getFactorA(),
                attemptDTO.getFactorB(),
                attemptDTO.getGuess(),
                isCorrect
        );

        attemptRepository.save(checkedAttempt);
        boolean status = gameClient.sendAttempt(checkedAttempt);
        log.info("Gamification service response: {}", status);
        return checkedAttempt;
    }

    @Override
    public List<ChallengeAttempt> getStatsForUser(final String alias) {
        return attemptRepository.findTop10ByUserAliasOrderByIdDesc(alias);
    }
}