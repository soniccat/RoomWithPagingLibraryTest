package com.sample.roomandpaginlibrary.model

sealed class Resource<T>(needShowNext: Boolean) {
    val canLoadNextPage: Boolean = needShowNext

    class Uninitialized<T>(): Resource<T>(false)
    class Loaded<T>(val data: T, canLoadNext: Boolean = false) : Resource<T>(canLoadNext)
    class Loading<T>(val data: T? = null, canLoadNext: Boolean = false) : Resource<T>(canLoadNext)
    class Error<T>(val message: String, val canTryAgain: Boolean, val data: T? = null, canLoadNext: Boolean = false) : Resource<T>(canLoadNext)

    fun toLoading(data: T? = data(), canLoadNext: Boolean = this.canLoadNextPage) = Loading(data, canLoadNext)
    fun toLoaded(data: T, canLoadNext: Boolean = this.canLoadNextPage) = Loaded(data, canLoadNext)
    fun toError(message: String, canTryAgain: Boolean, data: T? = data(), canLoadNext: Boolean = this.canLoadNextPage) = Error(message, canTryAgain, data, canLoadNext)

    // Getters

    fun isUninitialized(): Boolean {
        return when(this) {
            is Uninitialized -> true
            else -> false
        }
    }

    fun data(): T? {
        val data = when (this) {
            is Loaded -> data
            is Loading -> data
            is Error -> data
            else -> null
        }
        return data
    }

    fun copy(data: T? = this.data()): Resource<T> {
        return when(this) {
            is Uninitialized -> Uninitialized()
            is Loaded -> Loaded(data!!, canLoadNextPage)
            is Loading -> Loading(data, canLoadNextPage)
            is Error -> Error(message, canTryAgain, data, canLoadNextPage)
        }
    }

    fun <R> copyWith(data: R?): Resource<R> {
        return when(this) {
            is Uninitialized -> Uninitialized()
            is Loaded -> Loaded(data!!, canLoadNextPage)
            is Loading -> Loading(data, canLoadNextPage)
            is Error -> Error(message, canTryAgain, data, canLoadNextPage)
        }
    }
}

fun <T> Resource<T>?.data(): T? {
    return if (this == null) null else this.data()
}

fun Resource<*>?.isUninitialized(): Boolean {
    return if (this == null) true else this.isUninitialized()
}

fun Resource<*>?.isError(): Boolean {
    return if (this == null) false else this is Resource.Error
}

fun Resource<*>?.isLoaded(): Boolean {
    return if (this == null) false else this is Resource.Loaded
}

fun Resource<*>?.isLoading(): Boolean {
    return if (this == null) false else this is Resource.Loading
}

/**
 * Useful when we need to start loading first time or when an error has happened
 */
fun Resource<*>?.isNotLoadedAndNotLoading(): Boolean {
    return if (this == null) false else this !is Resource.Loaded && this !is Resource.Loading
}

fun Resource<*>?.hasData(): Boolean {
    return if (this == null) false else this.data() != null
}

fun <T> Resource<T>?.isLoadedAndEmpty(): Boolean where T : Collection<*> {
    return if (this == null) false else (this is Resource.Loaded && data()?.isEmpty() == true)
}

fun <T> Resource<T>?.isLoadedAndNotEmpty(): Boolean where T : Collection<*> {
    return if (this == null) false else (this is Resource.Loaded && data()?.isNotEmpty() == true)
}

fun Resource<*>?.isLoadedOrError(): Boolean {
    return if (this == null) false else (this is Resource.Loaded || this is Resource.Error)
}

fun <T, D, P> Resource<T>.merge(res2: Resource<D>, res3: Resource<P>): Resource<Triple<T?, D?, P?>> {
    if (isUninitialized() || res2.isUninitialized() || res3.isUninitialized()) {
        return Resource.Uninitialized()
    }

    if (isLoading() || res2.isLoading() || res3.isLoading()) {
        return Resource.Loading(toTriple(res2, res3))
    }

    if (isError() || res2.isError()) {
        val message = if (this is Resource.Error) message
        else if (res2 is Resource.Error) res2.message
        else if (res3 is Resource.Error) res3.message
        else ""
        return Resource.Error(message, true, toTriple(res2, res3))
    }

    return Resource.Loaded(toTriple(res2, res3))
}

fun <T, D> Resource<T>.merge(res2: Resource<D>): Resource<Pair<T?, D?>> {
    if (isUninitialized() || res2.isUninitialized()) {
        return Resource.Uninitialized()
    }

    if (isLoading() || res2.isLoading()) {
        return Resource.Loading(toPair(res2))
    }

    if (isError() || res2.isError()) {
        val message = if (this is Resource.Error) message else if (res2 is Resource.Error) res2.message else ""
        return Resource.Error(message, true, toPair(res2))
    }

    return Resource.Loaded(toPair(res2))
}

fun <D, T> Resource<T>.toPair(res2: Resource<D>) = Pair(data(), res2.data())

fun <D, T, P> Resource<T>.toTriple(res2: Resource<D>, res3: Resource<P>) = Triple(data(), res2.data(), res3.data())