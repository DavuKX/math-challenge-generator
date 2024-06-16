package davukx.multiplication.user;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class User {

    private Long id;
    private String alias;

    public User(final String userAlias) {
        this(null, userAlias);
    }
}