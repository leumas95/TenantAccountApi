package windall.console.account.api.utils

import javax.persistence.*
import java.time.LocalDateTime
import java.sql.Timestamp

@Converter(autoApply = true)
class LocalDateTimeAttributeConverter : AttributeConverter <LocalDateTime, Timestamp> {
	
    override fun convertToDatabaseColumn(localDateTime: LocalDateTime?) = localDateTime?.let { Timestamp.valueOf(it) }
	
	override fun convertToEntityAttribute(timestamp: Timestamp?) = timestamp?.toLocalDateTime()
	
}