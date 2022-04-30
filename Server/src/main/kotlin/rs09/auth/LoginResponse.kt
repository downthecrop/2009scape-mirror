package rs09.auth

enum class LoginResponse {
    UnexpectedError,
    CouldNotAd,
    Success,
    InvalidCredentials,
    AccountDisabled,
    AlreadyOnline,
    Updated,
    FullWorld,
    LoginServerOffline,
    LoginLimitExceeded,
    BadSessionID,
    WeakPassword,
    MembersWorld,
    CouldNotLogin,
    Updating,
    TooManyIncorrectLogins,
    StandingInMembersArea,
    AccountLocked,
    ClosedBeta,
    InvalidLoginServer,
    MovingWorld,
    ErrorLoadingProfile,
    BannedUser
}
