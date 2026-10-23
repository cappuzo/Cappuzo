package com.example.data.model

enum class Era(
    val id: String,
    val arabicName: String,
    val period: String,
    val description: String,
    val emblem: String
) {
    PRE_ISLAMIC(
        id = "pre_islamic",
        arabicName = "العصر الجاهلي",
        period = "قبل 622 م",
        description = "عصر المعلقات الخالدة والفروسية والفصاحة العربية الأصيلة ونقاء اللغة.",
        emblem = "⚔️"
    ),
    UMAYYAD(
        id = "umayyad",
        arabicName = "العصر الأموي",
        period = "661 - 750 م",
        description = "عصر النقائض والغزل العذري وبروز شعراء الحماسة والسياسة في دمشق وبادية الشام.",
        emblem = "🏛️"
    ),
    ABBASID(
        id = "abbasid",
        arabicName = "العصر العباسي",
        period = "750 - 1258 م",
        description = "العصر الذهبي للحضارة والأدب، والتجديد الشعري والحكمة والفلسفة في بغداد.",
        emblem = "📜"
    ),
    ANDALUSIAN(
        id = "andalusian",
        arabicName = "العصر الأندلسي",
        period = "711 - 1492 م",
        description = "شعر الموشحات ورثاء الممالك ووصف الطبيعة الغناء في قرطبة وغرناطة وإشبيلية.",
        emblem = "🏰"
    ),
    MODERN(
        id = "modern",
        arabicName = "العصر الحديث والمعاصر",
        period = "منذ القرن التاسع عشر",
        description = "مدرسة الإحياء والبعث، شعر النهضة، التفعيلة وقضايا الوطن والوجدان الإنساني.",
        emblem = "✨"
    );

    companion object {
        fun fromId(id: String): Era = values().firstOrNull { it.id == id } ?: ABBASID
    }
}
