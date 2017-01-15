// Copyright 2017 (C) BNORM Software. All rights reserved.
package com.bnorm.barkeep.db

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalDateTimeAttributeConverter : AttributeConverter<LocalDateTime, Timestamp> {

  override fun convertToDatabaseColumn(locDateTime: LocalDateTime?): Timestamp? {
    return locDateTime?.let { Timestamp.valueOf(it) }
  }

  override fun convertToEntityAttribute(sqlTimestamp: Timestamp?): LocalDateTime? {
    return sqlTimestamp?.toLocalDateTime()
  }
}