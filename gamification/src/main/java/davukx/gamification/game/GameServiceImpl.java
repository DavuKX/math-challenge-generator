package davukx.gamification.game;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import davukx.gamification.challenge.ChallengeSolvedEvent;
import davukx.gamification.game.badgeProcessors.BadgeProcessor;
import davukx.gamification.game.domain.BadgeCard;
import davukx.gamification.game.domain.BadgeType;
import davukx.gamification.game.domain.ScoreCard;

@Service
@Slf4j
@RequiredArgsConstructor
class GameServiceImpl implements GameService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;
    private final List<BadgeProcessor> badgeProcessors;

    @Override
    @Transactional
    public GameResult newAttemptForUser(final ChallengeSolvedEvent challenge) {
        if (challenge.isCorrect()) {
            ScoreCard scoreCard = new ScoreCard(challenge.getUserId(),
                    challenge.getAttemptId());
            scoreRepository.save(scoreCard);
            log.info("User {} scored {} points for attempt id {}",
                    challenge.getUserAlias(), scoreCard.getScore(),
                    challenge.getAttemptId());
            List<BadgeCard> badgeCards = processForBadges(challenge);
            return new GameResult(scoreCard.getScore(),
                    badgeCards.stream().map(BadgeCard::getBadgeType)
                            .collect(Collectors.toList()));
        } else {
            log.info("Attempt id {} is not correct. " +
                            "User {} does not get score.",
                    challenge.getAttemptId(),
                    challenge.getUserAlias());
            return new GameResult(0, List.of());
        }
    }

    private List<BadgeCard> processForBadges(
            final ChallengeSolvedEvent solvedChallenge) {
        Optional<Integer> optTotalScore = scoreRepository.
                getTotalScoreForUser(solvedChallenge.getUserId());
        if (optTotalScore.isEmpty()) return Collections.emptyList();
        int totalScore = optTotalScore.get();

        List<ScoreCard> scoreCardList = scoreRepository
                .findByUserIdOrderByScoreTimestampDesc(solvedChallenge.getUserId());
        Set<BadgeType> alreadyGotBadges = badgeRepository
                .findByUserIdOrderByBadgeTimestampDesc(solvedChallenge.getUserId())
                .stream()
                .map(BadgeCard::getBadgeType)
                .collect(Collectors.toSet());

        List<BadgeCard> newBadgeCards = badgeProcessors.stream()
                .filter(bp -> !alreadyGotBadges.contains(bp.badgeType()))
                .map(bp -> bp.processForOptionalBadge(totalScore,
                        scoreCardList, solvedChallenge)
                ).flatMap(Optional::stream)
                .map(badgeType ->
                        new BadgeCard(solvedChallenge.getUserId(), badgeType)
                )
                .collect(Collectors.toList());

        badgeRepository.saveAll(newBadgeCards);

        return newBadgeCards;
    }
}
