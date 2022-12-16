package me.redtea.kotlinbukkit.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

fun <T> Table.set(name: String, columnType: ColumnType): Column<List<T>> = registerColumn(name, ListColumnType(columnType))

open class SetColumnType(type: ColumnType) : ColumnType() {
    private val array = ArrayColumnType(type)

    override fun sqlType(): String = buildString {
        append(array.sqlType())
    }

    override fun valueToDB(value: Any?): Any? {
        return array.valueToDB((value as Set<*>).toTypedArray())
    }

    override fun valueFromDB(value: Any): Any {
        return (array.valueFromDB(value) as Array<*>).toSet();
    }

    override fun notNullValueToDB(value: Any): Any {
        return array.notNullValueToDB((value as Set<*>).toTypedArray());
    }
}