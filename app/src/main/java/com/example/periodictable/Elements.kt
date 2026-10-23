package com.example.periodictable

data class Element(val z: Int, val symbol: String, val en: String, val ar: String, val mass: String)

enum class Cat(val ar: String, val color: Long) {
    ALKALI("فلز قلوي", 0xFFEF9A9A), ALKALINE("فلز قلوي أرضي", 0xFFFFCC80),
    TRANSITION("فلز انتقالي", 0xFFFFF59D), POST("فلز ما بعد الانتقالي", 0xFFC5E1A5),
    METALLOID("شبه فلز", 0xFF80CBC4), NONMETAL("لافلز", 0xFF81D4FA),
    HALOGEN("هالوجين", 0xFFB39DDB), NOBLE("غاز نبيل", 0xFFF48FB1),
    LANTH("لانثانيدات", 0xFFA5D6A7), ACTIN("أكتينيدات", 0xFFBCAAA4)
}

fun category(z: Int): Cat = when (z) {
    2, 10, 18, 36, 54, 86, 118 -> Cat.NOBLE
    9, 17, 35, 53, 85, 117 -> Cat.HALOGEN
    3, 11, 19, 37, 55, 87 -> Cat.ALKALI
    4, 12, 20, 38, 56, 88 -> Cat.ALKALINE
    in 57..71 -> Cat.LANTH
    in 89..103 -> Cat.ACTIN
    5, 14, 32, 33, 51, 52 -> Cat.METALLOID
    1, 6, 7, 8, 15, 16, 34 -> Cat.NONMETAL
    in 21..30, in 39..48, in 72..80, in 104..112 -> Cat.TRANSITION
    else -> Cat.POST
}

/** (صف، عمود) في الجدول: الصفان 9 و10 للائنثانيدات والأكتينيدات */
fun pos(z: Int): Pair<Int, Int> = when (z) {
    1 -> 1 to 1
    2 -> 1 to 18
    in 3..4 -> 2 to z - 2
    in 5..10 -> 2 to z + 8
    in 11..12 -> 3 to z - 10
    in 13..18 -> 3 to z
    in 19..36 -> 4 to z - 18
    in 37..54 -> 5 to z - 36
    in 55..56 -> 6 to z - 54
    in 57..71 -> 9 to z - 54
    in 72..86 -> 6 to z - 68
    in 87..88 -> 7 to z - 86
    in 89..103 -> 10 to z - 86
    else -> 7 to z - 100
}

fun period(z: Int) = pos(z).first.let { if (it == 9) 6 else if (it == 10) 7 else it }
fun group(z: Int): String = if (z in 57..71 || z in 89..103) "—" else pos(z).second.toString()

fun state(z: Int) = when (z) {
    1, 2, 7, 8, 9, 10, 17, 18, 36, 54, 86 -> "غاز"
    35, 80 -> "سائل"
    in 104..118 -> "غير معروف"
    else -> "صلب"
}

private val SUP = "⁰¹²³⁴⁵⁶⁷⁸⁹"
fun electronConfig(z: Int): String {
    val order = listOf("1s","2s","2p","3s","3p","4s","3d","4p","5s","4d","5p","6s","4f","5d","6p","7s","5f","6d","7p")
    var left = z
    val sb = StringBuilder()
    for (o in order) {
        if (left <= 0) break
        val cap = when (o.last()) { 's' -> 2; 'p' -> 6; 'd' -> 10; else -> 14 }
        val n = minOf(cap, left); left -= n
        sb.append(o).append(n.toString().map { SUP[it - '0'] }.joinToString("")).append(' ')
    }
    return sb.toString().trim()
}

private const val DATA = """
H,Hydrogen,هيدروجين,1.008
He,Helium,هيليوم,4.0026
Li,Lithium,ليثيوم,6.94
Be,Beryllium,بيريليوم,9.0122
B,Boron,بورون,10.81
C,Carbon,كربون,12.011
N,Nitrogen,نيتروجين,14.007
O,Oxygen,أكسجين,15.999
F,Fluorine,فلور,18.998
Ne,Neon,نيون,20.180
Na,Sodium,صوديوم,22.990
Mg,Magnesium,مغنيسيوم,24.305
Al,Aluminium,ألومنيوم,26.982
Si,Silicon,سيليكون,28.085
P,Phosphorus,فوسفور,30.974
S,Sulfur,كبريت,32.06
Cl,Chlorine,كلور,35.45
Ar,Argon,أرغون,39.948
K,Potassium,بوتاسيوم,39.098
Ca,Calcium,كالسيوم,40.078
Sc,Scandium,سكانديوم,44.956
Ti,Titanium,تيتانيوم,47.867
V,Vanadium,فاناديوم,50.942
Cr,Chromium,كروم,51.996
Mn,Manganese,منغنيز,54.938
Fe,Iron,حديد,55.845
Co,Cobalt,كوبالت,58.933
Ni,Nickel,نيكل,58.693
Cu,Copper,نحاس,63.546
Zn,Zinc,زنك,65.38
Ga,Gallium,غاليوم,69.723
Ge,Germanium,جرمانيوم,72.630
As,Arsenic,زرنيخ,74.922
Se,Selenium,سيلينيوم,78.971
Br,Bromine,بروم,79.904
Kr,Krypton,كريبتون,83.798
Rb,Rubidium,روبيديوم,85.468
Sr,Strontium,سترونشيوم,87.62
Y,Yttrium,إيتريوم,88.906
Zr,Zirconium,زركونيوم,91.224
Nb,Niobium,نيوبيوم,92.906
Mo,Molybdenum,موليبدنوم,95.95
Tc,Technetium,تكنيشيوم,98
Ru,Ruthenium,روثينيوم,101.07
Rh,Rhodium,روديوم,102.91
Pd,Palladium,بلاديوم,106.42
Ag,Silver,فضة,107.87
Cd,Cadmium,كادميوم,112.41
In,Indium,إنديوم,114.82
Sn,Tin,قصدير,118.71
Sb,Antimony,أنتيمون,121.76
Te,Tellurium,تيلوريوم,127.60
I,Iodine,يود,126.90
Xe,Xenon,زينون,131.29
Cs,Caesium,سيزيوم,132.91
Ba,Barium,باريوم,137.33
La,Lanthanum,لانثانوم,138.91
Ce,Cerium,سيريوم,140.12
Pr,Praseodymium,براسيوديميوم,140.91
Nd,Neodymium,نيوديميوم,144.24
Pm,Promethium,بروميثيوم,145
Sm,Samarium,ساماريوم,150.36
Eu,Europium,أوروبيوم,151.96
Gd,Gadolinium,غادولينيوم,157.25
Tb,Terbium,تربيوم,158.93
Dy,Dysprosium,ديسبروسيوم,162.50
Ho,Holmium,هولميوم,164.93
Er,Erbium,إربيوم,167.26
Tm,Thulium,ثوليوم,168.93
Yb,Ytterbium,إيتربيوم,173.05
Lu,Lutetium,لوتيتيوم,174.97
Hf,Hafnium,هافنيوم,178.49
Ta,Tantalum,تانتالوم,180.95
W,Tungsten,تنغستن,183.84
Re,Rhenium,رينيوم,186.21
Os,Osmium,أوزميوم,190.23
Ir,Iridium,إريديوم,192.22
Pt,Platinum,بلاتين,195.08
Au,Gold,ذهب,196.97
Hg,Mercury,زئبق,200.59
Tl,Thallium,ثاليوم,204.38
Pb,Lead,رصاص,207.2
Bi,Bismuth,بزموت,208.98
Po,Polonium,بولونيوم,209
At,Astatine,أستاتين,210
Rn,Radon,رادون,222
Fr,Francium,فرانسيوم,223
Ra,Radium,راديوم,226
Ac,Actinium,أكتينيوم,227
Th,Thorium,ثوريوم,232.04
Pa,Protactinium,بروتأكتينيوم,231.04
U,Uranium,يورانيوم,238.03
Np,Neptunium,نبتونيوم,237
Pu,Plutonium,بلوتونيوم,244
Am,Americium,أمريسيوم,243
Cm,Curium,كوريوم,247
Bk,Berkelium,بركيليوم,247
Cf,Californium,كاليفورنيوم,251
Es,Einsteinium,أينشتاينيوم,252
Fm,Fermium,فيرميوم,257
Md,Mendelevium,مندليفيوم,258
No,Nobelium,نوبليوم,259
Lr,Lawrencium,لورنسيوم,266
Rf,Rutherfordium,رذرفورديوم,267
Db,Dubnium,دوبنيوم,268
Sg,Seaborgium,سيبورغيوم,269
Bh,Bohrium,بوهريوم,270
Hs,Hassium,هاسيوم,277
Mt,Meitnerium,مايتنريوم,278
Ds,Darmstadtium,دارمشتاتيوم,281
Rg,Roentgenium,رونتجينيوم,282
Cn,Copernicium,كوبرنيسيوم,285
Nh,Nihonium,نيهونيوم,286
Fl,Flerovium,فليروفيوم,289
Mc,Moscovium,موسكوفيوم,290
Lv,Livermorium,ليفرموريوم,293
Ts,Tennessine,تينيسين,294
Og,Oganesson,أوغانيسون,294
"""

val ELEMENTS: List<Element> = DATA.trim().lines().mapIndexed { i, l ->
    val p = l.split(","); Element(i + 1, p[0], p[1], p[2], p[3])
}
