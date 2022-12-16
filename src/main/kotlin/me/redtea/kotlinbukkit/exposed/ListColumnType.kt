package me.redtea.kotlinbukkit.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

fun <T> Table.list(name: String, columnType: ColumnType): Column<List<T>> = registerColumn(name, ListColumnType(columnType))

open class ListColumnType(type: ColumnType) : ColumnType() {
    private val array = ArrayColumnType(type)

    override fun sqlType(): String = buildString {
        append(array.sqlType())
    }

    override fun valueToDB(value: Any?): Any? {
        return array.valueToDB((value as List<*>).toTypedArray())
    }

    override fun valueFromDB(value: Any): Any {
        return (array.valueFromDB(value) as Array<*>).toList()
    }

    override fun notNullValueToDB(value: Any): Any {
        return array.notNullValueToDB((value as List<*>).toTypedArray())
    }
}