class ChallengeApiClient {
    static SERVER_URL = 'http://localhost:8000';
    static GET_CHALLENGE = '/challenges/random';
    static POST_RESULT = '/attempts';
    static GET_ATTEMPTS_BY_ALIAS = '/attempts?alias=';
    static GET_USERS_BY_IDS = '/users';

    static challenge() {
        return fetch(ChallengeApiClient.SERVER_URL + ChallengeApiClient.GET_CHALLENGE);
    }
    static sendGuess(user,
                     a,
                     b,
                     guess) {
        return fetch(ChallengeApiClient.SERVER_URL + ChallengeApiClient.POST_RESULT,
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(
                    {
                        userAlias: user,
                        factorA: a,
                        factorB: b,
                        guess: guess
                    }
                )
            });
    }

    static getAttempts(alias) {
        console.log('Get attempts for '+alias);
        return fetch(ChallengeApiClient.SERVER_URL +
            ChallengeApiClient.GET_ATTEMPTS_BY_ALIAS + alias);
    }

    static getUsers(userIds) {
        return fetch(ChallengeApiClient.SERVER_URL +
            ChallengeApiClient.GET_USERS_BY_IDS +
            '/' + userIds.join(','));
    }

}
export default ChallengeApiClient;