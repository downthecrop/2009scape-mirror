package org.rs09.client.constants.login

object LoginStage {

    const val ERROR_CHECK_SET_PORTS = 0
    const val INITIALIZE_SOCKET = 1
    const val INITIATE_USER_LOGIN = 2
    const val SEND_ENCRYPT_USER_CREDENTIALS = 3
    const val RECEIVE_SERVER_RESPONSE = 4
    const val SEND_ADVERTISEMENT_TO_USER = 5
    const val FINISH_DISPLAYING_ADVERTISEMENT = 6
    const val DISPLAY_PROFILE_TRANSFERRING = 7
    const val LOGIN_ACCEPTED_GET_USER_DATA = 8
    const val FINISH_USER_LOGIN_SETUP = 9
    const val DISPLAY_REQUIREMENT_WORLD_RESPONSE = 10

}