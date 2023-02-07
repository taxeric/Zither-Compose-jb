package ext

import com.google.gson.Gson

inline fun <reified T: Any> String.toObj(): T? {
    if (isEmpty()) {
        return null
    }
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}

fun <T> T.covertStr(): String {
    val gson = Gson()
    return gson.toJson(this)
}