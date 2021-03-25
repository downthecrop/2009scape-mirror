package org.rs09.client.constants.login

object LoginMessageToDisplay {

    const val CONNECTION_TIMED_OUT = -5
    const val ERROR_CONNECTING_TO_SERVER = -4
    const val PERFORMING_LOGIN = -3
    const val ENTER_USERNAME_AND_PASSWORD = -2

    const val COULD_NOT_DISPLAY_VIDEO_AD = 1
    const val UNEXPECTED_SERVER_RESPONSE_2 = 2
    const val INVALID_USER_OR_PASSWORD = 3
    const val ACCOUNT_DISABLED = 4
    const val ACCOUNT_STILL_LOGGED_IN = 5
    const val GAME_HAS_UPDATED = 6
    const val WORLD_IS_FULL = 7
    const val LOGIN_SERVER_OFFLINE = 8
    const val TOO_MANY_CONNECTIONS = 9
    const val BAD_SESSION_ID = 10
    const val WEAK_PASSWORD_ALERT = 11
    const val NON_MEMBERS_ACCOUNT = 12
    const val COULD_NOT_COMPLETE_LOGIN = 13
    const val SERVER_BEING_UPDATED_WAIT = 14
    const val UNEXPECTED_SERVER_RESPONSE_15 = 15
    const val MAX_INCORRECT_LOGIN_AMOUNT = 16
    const val FREE_ACCOUNT_IN_MEMBERS_AREA = 17
    const val LOCKED_ACCOUNT_STOLEN = 18
    const val FULLSCREEN_MEMBERS_ONLY = 19
    const val MALFORMED_LOGIN_PACKET = 22
    const val NO_LOGIN_SERVER_REPLY = 23
    const val ERROR_LOADING_PROFILE = 24
    const val MAC_BANNED = 26
    const val SERVICE_UNAVAILABLE = 27
    const val NON_MEMBER_MEMBER_LOGIN = 30

    //Additive/Specialized responses displayed || LoginServerResponse REQUIREMENT_WORLD (29)
    const val PVP_COMBAT_MINIMUM_LEVEL_20 = 0
    const val PVP_CARRYING_LENT_ITEMS = 1
}