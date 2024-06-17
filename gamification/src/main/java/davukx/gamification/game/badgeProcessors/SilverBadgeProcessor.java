package davukx.gamification.game.badgeProcessors;

import davukx.gamification.challenge.ChallengeSolvedEvent;
import davukx.gamification.game.domain.BadgeType;
import davukx.gamification.game.domain.ScoreCard;

import java.util.List;
import java.util.Optional;

public class SilverBadgeProcessor implements BadgeProcessor{
    @Override
    public Optional<BadgeType> processForOptionalBadge(int currentScore,
                                                       List<ScoreCard> scoreCardList,
                                                       ChallengeSolvedEvent solved) {
        return currentScore > 150 ?
                Optional.of(BadgeType.SILVER) :
                Optional.empty();
    }

    @Override
    public BadgeType badgeType() {
        return BadgeType.SILVER;
    }
}