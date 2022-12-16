package me.redtea.kotlinbukkit.exposed

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.jdbc.JdbcConnectionImpl
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.util.*

fun <T> Table.array(name: String, columnType: ColumnType): Column<Array<T>> = registerColumn(name, ArrayColumnType(columnType))

open class ArrayColumnType(private val type: ColumnType) : ColumnType() {
    companion object {
        var textArrays: Boolean = false
    }

    private fun supportsArrays() = !textArrays

    override fun sqlType(): String = buildString {
        if (!supportsArrays()) {
            append("TEXT")
        } else {
            append(type.sqlType())
            append(" ARRAY")
        }
    }

    private fun <T : Any>encodeArray(value: Array<*>): Array<T> {
        return value as Array<T>
    }
    override fun valueToDB(value: Any?): Any? {
        return if (value is Array<*>) {
            val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
            if (!supportsArrays()) {
                if (value.isArrayOf<String>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<String>(value)))
                else if(value.isArrayOf<Char>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Char>(value)))
                else if(value.isArrayOf<Boolean>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Boolean>(value)))
                else if(value.isArrayOf<Int>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Int>(value)))
                else if(value.isArrayOf<Long>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Long>(value)))
                else if(value.isArrayOf<UInt>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<UInt>(value)))
                else if(value.isArrayOf<ULong>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<ULong>(value)))
                else if(value.isArrayOf<Float>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Float>(value)))
                else if(value.isArrayOf<Double>())
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<Double>(value)))
                else if(value.isArrayOf<UUID>()) {
                    TextColumnType().valueToDB(Json.encodeToString(encodeArray<String>((value as Array<UUID>).map { it.toString() }.toTypedArray() )))
                }

                else {
                    error("Type of elements in this array is not supported!")
                }
            } else {
                val columnType = type.sqlType().split("(")[0]
                return jdbcConnection.createArrayOf(columnType, value)
            }
        } else {
            super.valueToDB(value)
        }
    }


    @OptIn(ExperimentalUnsignedTypes::class)
    override fun valueFromDB(value: Any): Any {
        if (!supportsArrays()) {
            if(value is String) {
                return when(type) {
                    is TextColumnType ->  Json.decodeFromString<Array<String>>(value)
                    is CharColumnType ->  Json.decodeFromString<Array<Char>>(value)
                    is BooleanColumnType ->  Json.decodeFromString<Array<Boolean>>(value)
                    is IntegerColumnType ->  Json.decodeFromString<Array<Int>>(value)
                    is LongColumnType ->  Json.decodeFromString<Array<Long>>(value)
                    is UIntegerColumnType ->  Json.decodeFromString<Array<UInt>>(value)
                    is ULongColumnType ->  Json.decodeFromString<Array<ULong>>(value)
                    is FloatColumnType ->  Json.decodeFromString<Array<Float>>(value)
                    is DoubleColumnType ->  Json.decodeFromString<Array<Double>>(value)
                    is UUIDColumnType -> Json.decodeFromString<Array<String>>(value).map { UUID.fromString(it) }.toTypedArray()
                    else -> {
                        error("Unknown type: ${type.sqlType()}")
                    }
                }

            }
        }

        if (value is java.sql.Array) {
            return value.array
        }
        if (value is Array<*>) {
            return value
        }
        error("Array does not support for this database and type ${value.javaClass}")
    }

    override fun notNullValueToDB(value: Any): Any {
        if (!supportsArrays()) {
            val result = valueToDB(value)
            return result ?: error("Can't create non null array for $value")
        }

        if (value is Array<*>) {
            if (value.isEmpty())
                return "'{}'"

            val columnType = type.sqlType().split("(")[0]
            val jdbcConnection = (TransactionManager.current().connection as JdbcConnectionImpl).connection
            return jdbcConnection.createArrayOf(columnType, value) ?: error("Can't create non null array for $value")
        } else {
            return super.notNullValueToDB(value)
        }
    }
}