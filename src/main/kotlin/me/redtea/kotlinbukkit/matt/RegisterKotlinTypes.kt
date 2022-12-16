import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.TypeResult
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer


fun CommandManager.registerKotlinTypes() {
    with(parameterHandler) {
        register(Int::class.java) { argument ->
            val res: Int = Integer.parseInt(argument.toString())
            TypeResult(res, argument)
        }
        register(String::class.java) { argument ->
            val res: String = argument.toString()
            TypeResult(res, argument)
        }
        register(Boolean::class.java) { argument ->
            val res: Boolean? = argument.toString().toBooleanStrictOrNull()
            TypeResult(res, argument)
        }
        register(Double::class.java) { argument ->
            val res: Double? = argument.toString().toDoubleOrNull()
            TypeResult(res, argument)
        }
        register(Long::class.java) { argument ->
            val res = argument.toString().toLongOrNull()
            TypeResult(res, argument)
        }
        register(Short::class.java) { argument ->
            val res = argument.toString().toShortOrNull()
            TypeResult(res, argument)
        }
        register(UInt::class.java) { argument ->
            val res = argument.toString().toUIntOrNull()
            TypeResult(res, argument)
        }
        register(ULong::class.java) { argument ->
            val res = argument.toString().toULongOrNull()
            TypeResult(res, argument)
        }
        register(UByte::class.java) { argument ->
            val res = argument.toString().toUByteOrNull()
            TypeResult(res, argument)
        }
    }
}

fun CommandManager.registerOtherTypes() {
    with(parameterHandler) {
        register(OfflinePlayer::class.java) { argument ->
            val res: OfflinePlayer = Bukkit.getOfflinePlayer(argument.toString())
            TypeResult(res, argument)
        }
    }

}