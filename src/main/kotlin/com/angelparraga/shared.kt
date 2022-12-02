package com.angelparraga

import io.andrewohara.dynamokt.DynamoKtPartitionKey
import kotlinx.serialization.Serializable
import java.util.*


const val BASE_URL = "https://tb-api.xyz/stream/get?"
const val MY_KEY = "CDGNSTVY4"
const val MY_STEAMID = "76561198087280179"

@Serializable
data class NTResponse(
    val current: NTRun?,
    val previous: NTRun?
)

@Serializable
data class NTResponseDto(
    val current: NTRunDto?,
    val previous: NTRunDto?
)

fun NTResponse.toDto(): NTResponseDto = NTResponseDto(current?.toDto(), previous?.toDto())

@Serializable
data class NTRun(
    val char: Int,
    val lasthit: Int,
    val world: Int,
    val level: Int,
    val crown: Int,
    val wepA: Int,
    val wepB: Int,
    val skin: Int,
    val ultra: Int,
    val charlvl: Int,
    val loops: Int,
    val win: Boolean,
    val mutations: String,
    val kills: Int,
    val health: Int,
    val steamid: Long,
    val type: String,
    val timestamp: Long
)

@Serializable
data class NTRunDto(
    val character: String,
    val lastHit: String,
    val world: String,
    val worldLevel: Int,
    val crown: String,
    val weaponA: String,
    val weaponB: String,
    val skin: Char,
    val ultraMutation: String,
    val characterLvl: Int,
    val loops: Int,
    val win: Boolean,
    val mutations: List<String>,
    val kills: Int,
    val health: Int,
    val steamId: String,
    val type: String,
    val timestamp: Long //From seconds to milliseconds
)


data class NuclearRunDB(
    @DynamoKtPartitionKey
    val id: String,
    val character: String,
    val lastHit: String,
    val world: String,
    val worldLevel: Int,
    val crown: String,
    val weaponA: String,
    val weaponB: String,
    val skin: Char,
    val ultraMutation: String,
    val characterLvl: Int,
    val loops: Int,
    val win: Boolean,
    val mutations: List<String>,
    val kills: Int,
    val health: Int,
    val steamId: String,
    val type: String,
    val timestamp: Long
) {
    //fun getKey(): Key = Key.builder().partitionValue(id).build()
}

fun NTRun.toNuclearRunDB(): NuclearRunDB {
    val character = Character.values()[char - 1]
    return NuclearRunDB(
        id = UUID.randomUUID().toString(),
        character = character.charName,
        lastHit = Enemy.values()[lasthit + 1].enemyName,
        world = World.values().find { it.id == world }?.worldName ?: "World not found",
        worldLevel = level,
        crown = Crown.values()[crown - 1].crownName,
        weaponA = Weapon.values().find { it.id == wepA }?.weapName ?: "Weapon A not found",
        weaponB = Weapon.values().find { it.id == wepB }?.weapName ?: "Weapon B not found",
        skin = if (skin == 0) 'A' else 'B',
        ultraMutation = getUltraName(character, ultra),
        characterLvl = charlvl,
        loops = loops,
        win = win,
        mutations = getMutationNameList(mutations),
        kills = kills,
        health = health,
        steamId = steamid.toString(),
        type = type,
        timestamp = timestamp * 1000
    )
}

fun NTRun.toDto(): NTRunDto {
    val character = Character.values()[char - 1]
    return NTRunDto(
        character = character.charName,
        lastHit = Enemy.values()[lasthit + 1].enemyName,
        world = World.values().find { it.id == world }?.worldName ?: "World not found",
        worldLevel = level,
        crown = Crown.values()[crown - 1].crownName,
        weaponA = Weapon.values().find { it.id == wepA }?.weapName ?: "Weapon A not found",
        weaponB = Weapon.values().find { it.id == wepB }?.weapName ?: "Weapon B not found",
        skin = if (skin == 0) 'A' else 'B',
        ultraMutation = getUltraName(character, ultra),
        characterLvl = charlvl,
        loops = loops,
        win = win,
        mutations = getMutationNameList(mutations),
        kills = kills,
        health = health,
        steamId = steamid.toString(),
        type = type,
        timestamp = timestamp * 1000
    )
}

//region Enums
/*
1	Fish
2	Crystal
3	Eyes
4	Melting
5	Plant
6	Y.V.
7	Steroids
8	Robot
9	Chicken
10	Rebel
11	Horror
12	Rogue
14	Skeleton
15	Frog
 */
enum class Character(val charName: String, val ultraMutations: Array<String>) {
    FISH("Fish", arrayOf("Confiscate", "Gun Warrant")),
    CRYSTAL("Crystal", arrayOf("Fortress", "Juggernaut")),
    EYES("Eyes", arrayOf("Projectile Style", "Monster Style")),
    MELTING("Melting", arrayOf("Brain Capacity", "Detachment")),
    PLANT("Plant", arrayOf("Trapper", "Killer")),
    YV("Y.V.", arrayOf("Ima Gun God", "Back 2 Bizniz")),
    STEROIDS("Steroids", arrayOf("Ambidextrous", "Get Loaded")),
    ROBOT("Robot", arrayOf("Refined Taste", "Regurgitate")),
    CHICKEN("Chicken", arrayOf("Harder to kill", "Determination")),
    REBEL("Rebel", arrayOf("Personal Guard", "Riot")),
    HORROR("Horror", arrayOf("Stalker", "Anomaly", "Meltdown")),
    ROGUE("Rogue", arrayOf("Super Portal Strike", "Super Blast Armor")),
    SKELETON("Skeleton", arrayOf("Redemption", "Damnation")),
    FROG("Frog", arrayOf("Distance", "Intimacy"))
}

/*
-1	Nothing
0	Bandit
1	Maggot
2	Rad Maggot
3	Big Maggot
4	Scorpion
5	Gold Scorpion
6	Big Bandit
7	Rat
8	Rat King
9	Green Rat
10	Gator
11	Exploder
12	Toxic Frog
13	Mom
14	Assassin
15	Raven
16	Salamander
17	Sniper
18	Big Dog
19	Spider
20	(Not in game)
21	Laser Crystal
22	Hyper Crystal
23	Snow Bandit
24	Snowbot
25	Wolf
26	Snowtank
27	Lil Hunter
28	Freak
29	Explo Freak
30	Rhino Freak
31	Necromancer
32	Turret
33	Technomancer
34	Guardian
35	Explo Guardian
36	Dog Guardian
37	Throne
38	Throne II
39	Bone Fish
40	Crab
41	Turtle
42	Venus Grunt
43	Venus Sarge
44	Fireballer
45	Super Fireballer
46	Jock
47	Cursed Spider
48	Cursed Crystal
49	Mimic
50	Health Mimic
51	Grunt
52	Inspector
53	Shielder
54	Crown Guardian
55	Explosion
56	Small Explosion
57	Fire Trap
58	Shield
59	Toxin
60	Horror
61	Barrel
62	Toxic Barrel
63	Golden Barrel
64	Car
65	Venus Car
66	Venus Car Fixed
67	Venuz Car 2
68	Icy Car
69	Thrown Car
70	Mine
71	Crown of Death
72	Rogue Strike
73	Blood Launcher
74	Blood Cannon
75	Blood Hammer
76	Disc
77	Curse Eat
78	Big Dog Missile
79	Halloween Bandit
80	Lil Hunter Fly
81	Throne Death
82	Jungle Bandit
83	Jungle Assassin
84	Jungle Fly
85	Crown of Hatred
86	Ice Flower
87	Cursed Ammo Pickup
88	Underwater Lightning
89	Elite Grunt
90	Blood Gamble
91	Elite Shielder
92	Elite Inspector
93	Captain
94	Van
95	Buff Gator
96	Generator
97	Lightning Crystal
98	Golden Snowtank
99	Green Explosion
100	Small Generator
101	Golden Disc
102	Big Dog Explosion
103	IDPD Freak
104	Throne II Death
105	(Not in game)
 */
enum class Enemy(val enemyName: String) {
    NOTHING("Nothing"),
    BANDIT("Bandit"),
    MAGGOT("Maggot"),
    RAD_MAGGOT("Rad Maggot"),
    BIG_MAGGOT("Big Maggot"),
    SCORPION("Scorpion"),
    GOLD_SCORPION("Gold Scorpion"),
    BIG_BANDIT("Big Bandit"),
    RAT("Rat"),
    RAT_KING("Rat King"),
    GREEN_RAT("Green Rat"),
    GATOR("Gator"),
    EXPLODER("Exploder"),
    TOXIC_FROG("Toxic Frog"),
    MOM("Mom"),
    ASSASSIN("Assassin"),
    RAVEN("Raven"),
    SALAMANDER("Salamander"),
    SNIPER("Sniper"),
    BIG_DOG("Big Dog"),
    SPIDER("Spider"),
    NOT_IN_GAME("(Not in game)"),
    LASER_CRYSTAL("Laser Crystal"),
    HYPER_CRYSTAL("Hyper Crystal"),
    SNOW_BANDIT("Snow Bandit"),
    SNOWBOT("Snowbot"),
    WOLF("Wolf"),
    SNOWTANK("Snowtank"),
    LIL_HUNTER("Lil Hunter"),
    FREAK("Freak"),
    EXPLO_FREAK("Explo Freak"),
    RHINO_FREAK("Rhino Freak"),
    NECROMANCER("Necromancer"),
    TURRET("Turret"),
    TECHNOMANCER("Technomancer"),
    GUARDIAN("Guardian"),
    EXPLO_GUARDIAN("Explo Guardian"),
    DOG_GUARDIAN("Dog Guardian"),
    THRONE("Throne"),
    THRONE_II("Throne II"),
    BONE_FISH("Bone Fish"),
    CRAB("Crab"),
    TURTLE("Turtle"),
    VENUS_GRUNT("Venus Grunt"),
    VENUS_SARGE("Venus Sarge"),
    FIREBALLER("Fireballer"),
    SUPER_FIREBALLER("Super Fireballer"),
    JOCK("Jock"),
    CURSED_SPIDER("Cursed Spider"),
    CURSED_CRYSTAL("Cursed Crystal"),
    MIMIC("Mimic"),
    HEALTH_MIMIC("Health Mimic"),
    GRUNT("Grunt"),
    INSPECTOR("Inspector"),
    SHIELDER("Shielder"),
    CROWN_GUARDIAN("Crown Guardian"),
    EXPLOSION("Explosion"),
    SMALL_EXPLOSION("Small Explosion"),
    FIRE_TRAP("Fire Trap"),
    SHIELD("Shield"),
    TOXIN("Toxin"),
    HORROR("Horror"),
    BARREL("Barrel"),
    TOXIC_BARREL("Toxic Barrel"),
    GOLDEN_BARREL("Golden Barrel"),
    CAR("Car"),
    VENUZ_CAR("Venuz Car"),
    VENUZ_CAR_FIXED("Venuz Car Fixed"),
    VENUZ_CAR_2("Venuz Car 2"),
    ICY_CAR("Icy Car"),
    THROWN_CAR("Thrown Car"),
    MINE("Mine"),
    CROWN_OF_DEATH("Crown of Death"),
    ROGUE_STRIKE("Rogue Strike"),
    BLOOD_LAUNCHER("Blood Launcher"),
    BLOOD_CANNON("Blood Cannon"),
    BLOOD_HAMMER("Blood Hammer"),
    DISC("Disc"),
    CURSE_EAT("Curse Eat"),
    BIG_DOG_MISSILE("Big Dog Missile"),
    HALLOWEEN_BANDIT("Halloween Bandit"),
    LIL_HUNTER_FLY("Lil Hunter Fly"),
    THRONE_DEATH("Throne Death"),
    JUNGLE_BANDIT("Jungle Bandit"),
    JUNGLE_ASSASSIN("Jungle Assassin"),
    JUNGLE_FLY("Jungle Fly"),
    CROWN_OF_HATRED("Crown of Hatred"),
    ICE_FLOWER("Ice Flower"),
    CURSED_AMMO_PICKUP("Cursed Ammo Pickup"),
    UNDERWATER_LIGHTNING("Underwater Lightning"),
    ELITE_GRUNT("Elite Grunt"),
    BLOOD_GAMBLE("Blood Gamble"),
    ELITE_SHIELDER("Elite Shielder"),
    ELITE_INSPECTOR("Elite Inspector"),
    CAPTAIN("Captain"),
    VAN("Van"),
    BUFF_GATOR("Buff Gator"),
    GENERATOR("Generator"),
    LIGHTNING_CRYSTAL("Lightning Crystal"),
    GOLDEN_SNOWTANK("Golden Snowtank"),
    GREEN_EXPLOSION("Green Explosion"),
    SMALL_GENERATOR("Small Generator"),
    GOLDEN_DISC("Golden Disc"),
    BIG_DOG_EXPLOSION("Big Dog Explosion"),
    IDPD_FREAK("IDPD Freak"),
    THRONE_II_DEATH("Throne II Death"),
    NOT_IN_GAME_2("(Not in game)")
}

/*
100	Crown Vault
1	Desert
101	Oasis
2	Sewers
102	Pizza Sewers
3	Scrapyard
103	Y.V's Mansion
4	Crystal Caves
104	Cursed Crystal Caves
5	Frozen City
105	Jungle
6	Labs
7	The Palace
0	Campfire
107	Y.V's Crib
106	I.D.P.D. Headquarters
 */
enum class World(val id: Int, val worldName: String) {
    CROWN_VAULT(100, "Crown Vault"),
    DESERT(1, "Desert"),
    OASIS(101, "Oasis"),
    SEWERS(2, "Sewers"),
    PIZZA_SEWERS(102, "Pizza Sewers"),
    SCRAPYARD(3, "Scrapyard"),
    YV_MANSION(103, "Y.V's Mansion"),
    CRYSTAL_CAVES(4, "Crystal Caves"),
    CURSED_CRYSTAL_CAVES(104, "Cursed Crystal Caves"),
    FROZEN_CITY(5, "Frozen City"),
    JUNGLE(105, "Jungle"),
    LABS(6, "Labs"),
    THE_PALACE(7, "The Palace"),
    CAMPFIRE(0, "Campfire"),
    YV_CRIB(107, "Y.V's Crib"),
    IDPD_HEADQUARTERS(106, "I.D.P.D. Headquarters")
}

/*
1	No Crown
2	Crown of Death
3	Crown of Life
4	Crown of Haste
5	Crown of Guns
6	Crown of Hatred
7	Crown of Blood
8	Crown of Destiny
9	Crown of Love
10	Crown of Luck
11	Crown of Curses
12	Crown of Risk
13	Crown of Protection
 */
enum class Crown(val crownName: String) {
    NO_CROWN("No Crown"),
    DEATH("Crown of Death"),
    LIFE("Crown of Life"),
    HASTE("Crown of Haste"),
    GUNS("Crown of Guns"),
    HATRED("Crown of Hatred"),
    BLOOD("Crown of Blood"),
    DESTINY("Crown of Destiny"),
    LOVE("Crown of Love"),
    LUCK("Crown of Luck"),
    CURSES("Crown of Curses"),
    RISK("Crown of Risk"),
    PROTECTION("Crown of Protection")
}

enum class Weapon(val id: Int, val weapName: String) {
    NOTHING(0, "Nothing"),
    REVOLVER(1, "Revolver"),
    TRIPLE_MACHINEGUN(2, "Triple Machinegun"),
    WRENCH(3, "Wrench"),
    MACHINEGUN(4, "Machinegun"),
    SHOTGUN(5, "Shotgun"),
    CROSSBOW(6, "Crossbow"),
    GRENADE_LAUNCHER(7, "Grenade Launcher"),
    DOUBLE_SHOTGUN(8, "Double Shotgun"),
    MINIGUN(9, "Minigun"),
    AUTO_SHOTGUN(10, "Auto Shotgun"),
    AUTO_CROSSBOW(11, "Auto Crossbow"),
    SUPER_CROSSBOW(12, "Super Crossbow"),
    SHOVEL(13, "Shovel"),
    BAZOOKA(14, "Bazooka"),
    STICKY_LAUNCHER(15, "Sticky Launcher"),
    SMG(16, "SMG"),
    ASSAULT_RIFLE(17, "Assault Rifle"),
    DISC_GUN(18, "Disc Gun"),
    LASE_PISTOL(19, "Laser Pistol"),
    LASER_RIFLE(20, "Laser Rifle"),
    SLUGGER(21, "Slugger"),
    GATLING_SLUGGER(22, "Gatling Slugger"),
    ASSAULT_SLUGGER(23, "Assault Slugger"),
    ENERGY_SWORD(24, "Energy Sword"),
    SUPER_SLUGGER(25, "Super Slugger"),
    HYPER_RIFLE(26, "Hyper Rifle"),
    SCREWDRIVER(27, "Screwdriver"),
    LASER_MINIGUN(28, "Laser Minigun"),
    BLOOD_LAUNCHER(29, "Blood Launcher"),
    SPLINTER_GUN(30, "Splinter Gun"),
    TOXIC_BOW(31, "Toxic Bow"),
    SENTRY_GUN(32, "Sentry Gun"),
    WAVE_GUN(33, "Wave Gun"),
    PLASMA_GUN(34, "Plasma Gun"),
    PLASMA_CANNON(35, "Plasma Cannon"),
    ENERGY_HAMMER(36, "Energy Hammer"),
    JACKHAMMER(37, "Jackhammer"),
    FLAK_CANNON(38, "Flak Cannon"),
    GOLDEN_REVOLVER(39, "Golden Revolver"),
    GOLDEN_WRENCH(40, "Golden Wrench"),
    GOLDEN_MACHINEGUN(41, "Golden Machinegun"),
    GOLDEN_SHOTGUN(42, "Golden Shotgun"),
    GOLDEN_CROSSBOW(43, "Golden Crossbow"),
    GOLDEN_GRENADE_LAUNCHER(44, "Golden Grenade Launcher"),
    GOLDEN_LASER_PISTOL(45, "Golden Laser Pistol"),
    CHICKEN_SWORD(46, "Chicken Sword"),
    NUKE_LAUNCHER(47, "Nuke Launcher"),
    ION_CANNON(48, "Ion Cannon"),
    QUADRUPLE_MACHINEGUN(49, "Quadruple Machinegun"),
    FLAMETHROWER(50, "Flamethrower"),
    DRAGON(51, "Dragon"),
    FLARE_GUN(52, "Flare Gun"),
    ENERGY_SCREWDRIVER(53, "Energy Screwdriver"),
    HYPER_LAUNCHER(54, "Hyper Launcher"),
    LASER_CANNON(55, "Laser Cannon"),
    RUSTY_REVOLVER(56, "Rusty Revolver"),
    LIGHTNING_PISTOL(57, "Lightning Pistol"),
    LIGHTNING_RIFLE(58, "Lightning Rifle"),
    LIGHTNING_SHOTGUN(59, "Lightning Shotgun"),
    SUPER_FLAK_CANNON(60, "Super Flak Cannon"),
    SAWEDOFF_SHOTGUN(61, "Sawed-off Shotgun"),
    SPLINTER_PISTOL(62, "Splinter Pistol"),
    SUPER_SPLINTER_GUN(63, "Super Splinter Gun"),
    LIGHTNING_SMG(64, "Lighting SMG"),
    SMART_GUN(65, "Smart Gun"),
    HEAVY_CROSSBOW(66, "Heavy Crossbow"),
    BLOOD_HAMMER(67, "Blood Hammer"),
    LIGHTNING_CANNON(68, "Lightning Cannon"),
    POP_GUN(69, "Pop Gun"),
    PLASMA_RIFLE(70, "Plasma Rifle"),
    POP_RIFLE(71, "Pop Rifle"),
    TOXIC_LAUNCHER(72, "Toxic Launcher"),
    FLAME_CANNON(73, "Flame Cannon"),
    LIGHTNING_HAMMER(74, "Lightning Hammer"),
    FLAME_SHOTGUN(75, "Flame Shotgun"),
    DOUBLE_FLAME_SHOTGUN(76, "Double Flame Shotgun"),
    AUTO_FLAME_SHOTGUN(77, "Auto Flame Shotgun"),
    CLUSTER_LAUNCHER(78, "Cluster Launcher"),
    GRENADE_SHOTGUN(79, "Grenade Shotgun"),
    GRENADE_RIFLE(80, "Grenade Rifle"),
    ROGUE_RIFLE(81, "Rogue Rifle"),
    PARTY_GUN(82, "Party Gun"),
    DOUBLE_MINIGUN(83, "Double Minigun"),
    GATLING_BAZOOKA(84, "Gatling Bazooka"),
    AUTO_GRENADE_SHOTGUN(85, "Auto Grenade Shotgun"),
    ULTRA_REVOLVER(86, "Ultra Revolver"),
    ULTRA_LASER_PISTOL(87, "Ultra Laser Pistol"),
    SLEDGEHAMMER(88, "Sledgehammer"),
    HEAVY_REVOLVER(89, "Heavy Revolver"),
    HEAVY_MACHINEGUN(90, "Heavy Machinegun"),
    HEAVY_SLUGGER(91, "Heavy Slugger"),
    ULTRA_SHOVEL(92, "Ultra Shovel"),
    ULTRA_SHOTGUN(93, "Ultra Shotgun"),
    ULTRA_CROSSBOW(94, "Ultra Crossbow"),
    ULTRA_GRENADE_LAUNCHER(95, "Ultra Grenade Launcher"),
    PLASMA_MINIGUN(96, "Plasma Minigun"),
    DEVASTATOR(97, "Devastator"),
    GOLDEN_PLASMA_GUN(98, "Golden Plasma Gun"),
    GOLDEN_SLUGGER(99, "Golden Slugger"),
    GOLDEN_SPLINTER_GUN(100, "Golden Splinter Gun"),
    GOLDEN_SCREWDRIVER(101, "Golden Screwdriver"),
    GOLDEN_BAZOOKA(102, "Golden Bazooka"),
    GOLDEN_ASSAULT_RIFLE(103, "Golden Assault Rifle"),
    SUPER_DISC_GUN(104, "Super Disc Gun"),
    HEAVY_AUTO_CROSSBOW(105, "Heavy Auto Crossbow"),
    HEAVY_ASSAULT_RIFLE(106, "Heavy Assault Rifle"),
    BLOOD_CANNON(107, "Blood Cannon"),
    DOG_SPIN_ATTACK(108, "Dog Spin Attack"),
    DOG_MISSILE(109, "Dog Missile"),
    INCINERATOR(110, "Incinerator"),
    SUPER_PLASMA_CANNON(111, "Super Plasma Cannon"),
    SEEKER_PISTOL(112, "Seeker Pistol"),
    SEEKER_SHOTGUN(113, "Seeker Shotgun"),
    ERASER(114, "Eraser"),
    GUITAR(115, "Guitar"),
    BOUNCER_SMG(116, "Bouncer SMG"),
    BOUNCER_SHOTGUN(117, "Bouncer Shotgun"),
    HYPER_SLUGGER(118, "Hyper Slugger"),
    SUPER_BAZOOKA(119, "Super Bazooka"),
    FROG_PISTOL(120, "Frog Pistol"),
    BLACK_SWORD(121, "Black Sword"),
    GOLDEN_NUKE_LAUNCHER(122, "Golden Nuke Launcher"),
    GOLDEN_DISC_GIM(123, "Golden Disc Gun"),
    HEAVY_GRENADE_LAUNCHER(124, "Heavy Grenade Launcher"),
    GUN_GUN(125, "Gun Gun"),
    GOLDEN_FROG_PISTOL(201, "Golden Frog Pistol")
}

enum class Mutations(val mutationName: String) {
    HEAVY_HEART("Heavy Heart"),
    RHINO_SKIN("Rhino Skin"),
    EXTRA_FEET("Extra Feet"),
    PLUTONIUM_HUNGER("Plutonium Hunger"),
    RABBIT_PAW("Rabbit Paw"),
    THRONE_BUTT("Throne Butt"),
    LUCKY_SHOT("Lucky Shot"),
    BLOODLUST("Bloodlust"),
    GAMMA_GUTS("Gamma Guts"),
    SECOND_STOMACH("Second Stomach"),
    BACK_MUSCLE("Back Muscle"),
    SCARIER_FACE("Scarier Face"),
    EUPHORIA("Euphoria"),
    LONG_ARMS("Long Arms"),
    BOILING_VEINS("Boiling Veins"),
    SHOTGUN_SHOULDERS("Shotgun Shoulders"),
    RECYCLE_GLAND("Recycle Gland"),
    LASER_BRAIN("Laser Brain"),
    LAST_WISH("Last Wish"),
    EAGLE_EYES("Eagle Eyes"),
    IMPACT_WRISTS("Impact Wrists"),
    BOLT_MARROW("Bolt Marrow"),
    STRESS("Stress"),
    TRIGGER_FINGERS("Trigger Fingers"),
    SHARP_TEETH("Sharp Teeth"),
    PATIENCE("Patience"),
    HAMMERHEAD("Hammerhead"),
    STRONG_SPIRIT("Strong Spirit"),
    OPEN_MIND("Open Mind")
}

//endregion

/*
fun main() {
    println(getMutationNameList("00001000000000000010000001000"))
}

private fun getText(mutations: String): String {
    val mutationList = mutations.split("\n")
    var response = ""

    mutationList.forEach {
        val mutationRow = it.split("\t")
        val enumName = mutationRow[1].replace(" ", "_").uppercase()

        response += "$enumName(\"${mutationRow[1]}\"),\n"
    }

    return response
}*/
