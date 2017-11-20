package windall.console.account.api.utils

import java.sql.Date
import java.time.LocalDate
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalDateAttributeConverter : AttributeConverter <LocalDate, Date> {
	
    override fun convertToDatabaseColumn(localDate: LocalDate?) = localDate?.let { Date.valueOf(it) }
	
	override fun convertToEntityAttribute(date: Date?) = date?.toLocalDate()
	
}