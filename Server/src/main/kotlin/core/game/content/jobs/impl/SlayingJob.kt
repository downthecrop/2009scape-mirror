import core.tools.RandomFunction

enum class SlayingJob(val lower: Int, val upper: Int, vararg val ids: Int) {
    CHICKEN(25,26,41,1017),
    COW(25,25,397,1766,81,1767),
    GIANT_SPIDER(22,25,59,60),
    SCORPION(25,25,107,108,109,144,4402,4403),
    GOBLIN(25,25,100,101,102,298,299,444,445,489,745,1769,1770,1771,1772,1773,1774,1775,1776,2274,2275,2276,2277,2278,2279,2280,2281,2678,2679,2680,2681,3060,3264,3265,3266,3267);

    fun getAmount(): Int {
        return RandomFunction.random(lower,upper + 1)
    }
}