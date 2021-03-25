package org.rs09.client.constants

object Parameter {

    /**
     * Modewhat
     * DEFAULT: LIVE_SERVER 0
     */
    const val LIVE_SERVER = 0
    const val RELEASE_CANDIDATE = 1
    const val WORK_IN_PROGRESS = 2

    /**
     * Modewhere
     * DEFAULT: LIVE_ENVIRONMENT 0
     */
    const val LIVE_ENVIRONMENT = 0
    const val OFFICE_ENVIRONMENT = 1
    const val LOCAL_ENVIRONMENT = 2

    /**
     * Language
     * DEFAULT: LANGUAGE_ENGLISH 0
     */
    const val LANGUAGE_ENGLISH = 0
    const val LANGUAGE_GERMAN = 1
    const val LANGUAGE_FRENCH = 2
    const val LANGUAGE_PORTUGUESE = 3

    /**
     * Game type
     * DEFAULT: GAME_TYPE_RUNESCAPE 0 (game0)
     *
     * Runescape    | Game type 0 has always been Runescape (current)
     * Mechscape    | Assuming this is what the game1 was (2007 - Oct 2009)
     * Stellar Dawn | Overhaul of 'Mechscape' (July 2010 - Stuck in corporate hell)
     */
    const val GAME_TYPE_RUNESCAPE = 0
    const val GAME_TYPE_MECHSCAPE = 1

    /**
     * Affiliate ID
     * DEFAULT: NO_AFFILIATE 0
     */
    const val NO_AFFILIATE = 0
    const val SHARE_DETAILS_AFFILIATE = 99

}