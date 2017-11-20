package windall.console.account.api.utils

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalDateTimeAttributeConverter : AttributeConverter <LocalDateTime, Timestamp> {
	
    override fun convertToDatabaseColumn(localDateTime: LocalDateTime?) = localDateTime?.let { Timestamp.valueOf(it) }
	
	override fun convertToEntityAttribute(timestamp: Timestamp?) = timestamp?.toLocalDateTime()
	
}