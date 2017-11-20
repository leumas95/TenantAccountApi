package windall.console.account.api.utils

import javax.persistence.*
import java.time.LocalDate
import java.sql.Date

@Converter(autoApply = true)
class LocalDateAttributeConverter : AttributeConverter <LocalDate, Date> {
	
    override fun convertToDatabaseColumn(localDate: LocalDate?) = localDate?.let { Date.valueOf(it) }
	
	override fun convertToEntityAttribute(date: Date?) = date?.toLocalDate()
	
}