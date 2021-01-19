package ru.netology.lesson3.variouscommision

/**
 * Description
 * Домашнее задание к занятию «1.3. Управляющие конструкции: if, switch, циклы. Работа с отладчиком»
 * Задача №2 - Разная комиссия
 */

enum class CardTypes {
    Mastercard, Maestro, Visa, Mir, VKPay
}

fun main()
{//варианты сумм для перевода
    val paymentSeq          = sequenceOf(10_00L, 100_00L)
 //варианты сумм для переводов за целый месяц
    val lastMonthPaymentSeq = sequenceOf(10_00L, 80_000_00L)
 //перебор по всем сочетаниям и сумм переводов и сумм за месяц и всех видов карт
    for (monthPayment in lastMonthPaymentSeq)
    {
        for (payment  in paymentSeq)
        {
            CardTypes.values().forEach {
                val commission = calcPaymentCommission(
                    cardType = it,
                    lastMonthPayments = monthPayment,
                    currentPayment = payment
                )
                println("Платежная система: $it, " +
                        "текущий перевод: ${payment / 100L} рублей ${payment % 100L} копеек, " +
                        "сумма переводов за месяц: ${monthPayment / 100L} рублей ${monthPayment % 100L} копеек")
                println("Комиссия составит ${commission / 100L} рублей ${commission % 100L} копеек")
                println("")
            }
        }
    }
}

/**
 *
 * Description
 * calcPaymentCommission вычисляет комиссию в копейках
 * cardType       - Тип карты/счёта (по умолчанию - Vk Pay)
 * lastPayments   - Сумма предыдущих переводов в этом месяце (по умолчанию - 0)
 * currentPayment - Сумма совершаемого перевода в копейках
 * результат      - комиссия в копейках
 */

fun calcPaymentCommission (cardType : CardTypes = CardTypes.VKPay,
                           lastMonthPayments : Long = 0, currentPayment : Long) : Long {
    val mastercardMaestroMonthLimit = 75_000_00
    val noCommission = 0L
    return when(cardType) {
        CardTypes.Mastercard, CardTypes.Maestro ->
            if (lastMonthPayments < mastercardMaestroMonthLimit) {
                noCommission
            } else {
                calcMastercardMaestroCommission(currentPayment)
            }
        CardTypes.Visa, CardTypes.Mir -> calcVisaMirCommission(currentPayment)
        else                          -> noCommission
    }
}

/**
 * Description
 * calcMastercardMaestroCommission
 * вычисляет комиссию в копейках для Mastercard, Maestro
 * currentPayment - Сумма совершаемого перевода в копейках
 * результат      - комиссия в копейках
 */
fun calcMastercardMaestroCommission(currentPayment : Long) : Long
{
    val commissionPercent = 0.6F
    val commissionFix     = 20_00.0F
    return (currentPayment * commissionPercent / 100.0F + commissionFix).toLong()
}

/**
 * Description
 * calcVisaMirCommission
 * вычисляет комиссию в копейках для Visa, Mir
 * currentPayment - Сумма совершаемого перевода в копейках
 * результат      - комиссия в копейках
 */
fun calcVisaMirCommission(currentPayment : Long) : Long
{
    val commissionPercent = 0.75F
    val commissionMin     = 35_00L
    val result = (currentPayment * commissionPercent / 100.0F).toLong()
    return if (result >= commissionMin) result else commissionMin
}
