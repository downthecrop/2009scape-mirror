package rs09.game.content.activity.gnomecooking

enum class GnomeCookingJob(val level: GnomeTipper.LEVEL, val npc_id: Int, val tip: String) {
    CPT_ERRDO(GnomeTipper.LEVEL.EASY,3811, "at the top level of the Grand Tree."),
    DALILAH(GnomeTipper.LEVEL.EASY,4588, "sitting in the Gnome Restaurant."),
    GULLUCK(GnomeTipper.LEVEL.EASY,602, "on the third level of the Grand Tree."),
    ROMETTI(GnomeTipper.LEVEL.EASY,601, "on the second level of the Grand Tree."),
    NARNODE(GnomeTipper.LEVEL.EASY,670, "at the base of the Grand Tree."),
    MEEGLE(GnomeTipper.LEVEL.EASY,4597, "in the terrorbird enclosure."),
    PERRDUR(GnomeTipper.LEVEL.EASY,4587,"sitting in the Gnome Restaurant."),
    SARBLE(GnomeTipper.LEVEL.EASY,4599, "in the swamp west of the Grand Tree."),
    GIMLEWAP(GnomeTipper.LEVEL.HARD,4580, "upstairs in Ardougne castle."),
    BLEEMADGE(GnomeTipper.LEVEL.HARD,3810, "at the top of White Wolf Mountain."),
    DALBUR(GnomeTipper.LEVEL.HARD,3809, "by the gnome glider in Al Kharid"),
    BOLREN(GnomeTipper.LEVEL.HARD,469, "next to the Spirit Tree in Tree Gnome Village"),
    SCHEPBUR(GnomeTipper.LEVEL.HARD,3817, "in the battlefield of Khazar, south of the river."),
    IMBLEWYN(GnomeTipper.LEVEL.HARD,4586, "on the ground floor of the Magic Guild."),
    ONGLEWIP(GnomeTipper.LEVEL.HARD,4585, "in the Wizard's Tower south of Draynor.")
}