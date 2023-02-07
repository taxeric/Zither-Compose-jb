package utils

import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> obtainFlow1() = MutableSharedFlow<T>(extraBufferCapacity = 1)
fun <T> obtainFlow2() = MutableSharedFlow<T>(replay = 1)