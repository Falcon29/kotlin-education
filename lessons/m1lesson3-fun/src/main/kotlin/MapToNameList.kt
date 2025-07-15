fun mapToNameList(listOfMaps: List<Map<String, String>>) = listOfMaps.map {
    "${it["last"] ?: ""} ${it["first"] ?: ""} ${it["middle"] ?: ""}".trim()
}
