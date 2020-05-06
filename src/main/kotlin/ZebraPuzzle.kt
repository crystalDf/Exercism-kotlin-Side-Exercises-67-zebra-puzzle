import kotlin.math.abs

class ZebraPuzzle() {

    private val residentRecordListGroupByHouseIndex =
            getResidentRecordList(Resident.values().size).groupBy { it.houseIndex }
    private val chosenSet =
            getResidentRecordSetList(residentRecordListGroupByHouseIndex).first()

    fun drinksWater(): String =
            chosenSet.first { it.beverage == Beverage.WATER}.resident.name.toLowerCase().capitalize()

    fun ownsZebra(): String =
            chosenSet.first { it.pet == Pet.ZEBRA}.resident.name.toLowerCase().capitalize()

    private fun getResidentRecordList(numberOfHouse: Int): List<ResidentRecord> {

        val residentRecordList = mutableListOf<ResidentRecord>()

        (0 until numberOfHouse).forEach { houseIndex ->
            Resident.values().forEach { resident ->
                HouseColor.values().forEach { houseColor ->
                    Pet.values().forEach { pet ->
                        Beverage.values().forEach { beverage ->
                            CigaretteBrand.values().forEach { cigaretteBrand ->
                                ResidentRecord(houseIndex, resident, houseColor, pet, beverage, cigaretteBrand)
                                        .let { if (it.isValid()) residentRecordList.add(it) }
                            }
                        }
                    }
                }
            }
        }

        return residentRecordList
    }

    private fun ResidentRecord.isValid(): Boolean =
            when {
                resident == Resident.ENGLISHMAN && houseColor != HouseColor.RED -> false
                houseColor == HouseColor.RED && resident != Resident.ENGLISHMAN -> false
                resident == Resident.SPANIARD && pet != Pet.DOG -> false
                pet == Pet.DOG && resident != Resident.SPANIARD -> false
                beverage == Beverage.COFFEE && houseColor != HouseColor.GREEN -> false
                houseColor == HouseColor.GREEN && beverage != Beverage.COFFEE -> false
                resident == Resident.UKRAINIAN && beverage != Beverage.TEA -> false
                beverage == Beverage.TEA && resident != Resident.UKRAINIAN -> false
                houseColor == HouseColor.IVORY && houseIndex == 4 -> false
                houseColor == HouseColor.GREEN && houseIndex == 0 -> false
                cigaretteBrand == CigaretteBrand.OLD_GOLD && pet != Pet.SNAILS -> false
                pet == Pet.SNAILS && cigaretteBrand != CigaretteBrand.OLD_GOLD -> false
                cigaretteBrand == CigaretteBrand.KOOLS && houseColor != HouseColor.YELLOW -> false
                houseColor == HouseColor.YELLOW && cigaretteBrand != CigaretteBrand.KOOLS -> false
                beverage == Beverage.MILK && houseIndex != 2 -> false
                houseIndex == 2 && beverage != Beverage.MILK -> false
                resident == Resident.NORWEGIAN && houseIndex != 0 -> false
                houseIndex == 0 && resident != Resident.NORWEGIAN -> false
                cigaretteBrand == CigaretteBrand.CHESTERFIELDS && pet == Pet.FOX -> false
                cigaretteBrand == CigaretteBrand.KOOLS && pet == Pet.HORSE -> false
                cigaretteBrand == CigaretteBrand.LUCKY_STRIKE && beverage != Beverage.ORANGE_JUICE -> false
                beverage == Beverage.ORANGE_JUICE && cigaretteBrand != CigaretteBrand.LUCKY_STRIKE -> false
                resident == Resident.JAPANESE && cigaretteBrand != CigaretteBrand.PARLIAMENTS -> false
                cigaretteBrand == CigaretteBrand.PARLIAMENTS && resident != Resident.JAPANESE -> false
                houseColor == HouseColor.BLUE && houseIndex != 1 -> false
                houseIndex == 1 && houseColor != HouseColor.BLUE -> false
                else -> true
            }

    private fun getResidentRecordSetList(residentRecordListGroupByHouseIndex: Map<Int, List<ResidentRecord>>)
            : List<Set<ResidentRecord>> {

        val residentRecordSetList = mutableListOf<Set<ResidentRecord>>()

        (residentRecordListGroupByHouseIndex[0])?.forEach { residentRecord0 ->
            (residentRecordListGroupByHouseIndex[1])?.forEach { residentRecord1 ->
                (residentRecordListGroupByHouseIndex[2])?.forEach { residentRecord2 ->
                    (residentRecordListGroupByHouseIndex[3])?.forEach { residentRecord3 ->
                        (residentRecordListGroupByHouseIndex[4])?.forEach { residentRecord4 ->
                            setOf(residentRecord0, residentRecord1, residentRecord2, residentRecord3, residentRecord4)
                                    .let { if (it.isValid()) residentRecordSetList.add(it) }
                        }
                    }
                }
            }
        }

        return residentRecordSetList
    }

    private fun Set<ResidentRecord>.isValid(): Boolean {

        forEach {
            minus(it).forEach {
                other -> if (it.isInConflictWith(other)) return false
            }
        }

        return when {
            first { it.houseColor == HouseColor.GREEN }.houseIndex !=
                    first { it.houseColor == HouseColor.IVORY }.houseIndex + 1 -> false
            abs(first { it.cigaretteBrand == CigaretteBrand.CHESTERFIELDS }.houseIndex -
                    first { it.pet == Pet.FOX }.houseIndex) != 1 -> false
            abs(first { it.cigaretteBrand == CigaretteBrand.KOOLS }.houseIndex -
                    first { it.pet == Pet.HORSE }.houseIndex) != 1 -> false
            else -> true
        }
    }

    private fun ResidentRecord.isInConflictWith(other: ResidentRecord): Boolean =
            houseIndex == other.houseIndex ||
                    resident == other.resident ||
                    houseColor == other.houseColor ||
                    pet == other.pet ||
                    beverage == other.beverage ||
                    cigaretteBrand == other.cigaretteBrand

}

data class ResidentRecord(
        val houseIndex: Int,
        val resident: Resident,
        val houseColor: HouseColor,
        val pet: Pet,
        val beverage: Beverage,
        val cigaretteBrand: CigaretteBrand)

enum class Resident { ENGLISHMAN, SPANIARD, UKRAINIAN, NORWEGIAN, JAPANESE, }
enum class HouseColor { RED, GREEN, IVORY, YELLOW, BLUE, }
enum class Pet { DOG, SNAILS, FOX, HORSE, ZEBRA, }
enum class Beverage { COFFEE, TEA, MILK, ORANGE_JUICE, WATER, }
enum class CigaretteBrand { OLD_GOLD, KOOLS, CHESTERFIELDS, LUCKY_STRIKE, PARLIAMENTS, }
