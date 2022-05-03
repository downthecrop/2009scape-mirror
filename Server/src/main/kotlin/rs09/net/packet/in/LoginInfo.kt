package rs09.net.packet.`in`

import core.cache.crypto.ISAACCipher
import core.cache.crypto.ISAACPair

class LoginInfo(
    var showAds: Boolean, //Unused
    var windowMode : Int,
    var screenWidth: Int,
    var screenHeight: Int,
    var displayMode: Int,
    var adAffiliateId: Int, //Unused
    var settingsHash: Int,
    var currentPacketCount: Int,
    var username: String,
    var password: String,
    var isaacPair: ISAACPair,
    var opcode: Int,
    var crcSums: IntArray
) {
    companion object {
        fun createDefault(): LoginInfo {
            return LoginInfo(false, 0, 0, 0, 0, 0, 0, 0, "", "", ISAACPair(ISAACCipher(intArrayOf()), ISAACCipher(intArrayOf())), 0, IntArray(Login.CACHE_INDEX_COUNT))
        }
    }

    override fun toString(): String {
        return "ads:$showAds,wm:$windowMode,sw:$screenWidth,sh:$screenHeight,dm:$displayMode,adid:$adAffiliateId,settings:$settingsHash,pkt:$currentPacketCount,un:$username,pw:$password"
    }
}
