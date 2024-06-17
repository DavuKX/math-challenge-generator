package davukx.gamification.game;

import lombok.RequiredArgsConstructor;
import davukx.gamification.challenge.ChallengeSolvedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attempts")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    void postResult(@RequestBody ChallengeSolvedEvent dto) {
        gameService.newAttemptForUser(dto);
    }
}
