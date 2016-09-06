package com.tvsc.service.util.impl

import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.PaginationUtils
import org.springframework.stereotype.Component
import rx.Observable

/**
 *
 * @author Taras Zubrei
 */
@Component
open class PaginationUtilsImpl constructor(val jsonUtils: JsonUtils, val httpUtils: HttpUtils) : PaginationUtils {
    override fun <T> getFullResponse(url: String, clazz: Class<T>): Observable<T> {
        val urlWithPage = url.plus(if (url.contains('?')) '&' else '?').plus("page=%d")
        return httpUtils.get(url)
                .map { jsonUtils.getPage(it, clazz) }
                .map { pair ->
                    Array<Observable<T>>(pair.second) {
                        if (it == 0)
                            Observable.just(pair.first).flatMap { Observable.from(it) }
                        else
                            getPage(urlWithPage, it + 1, clazz)
                    }
                }
                .flatMapObservable { Observable.from(it) }
                .flatMap { it }
    }

    private fun <T> getPage(url: String, page: Int, clazz: Class<T>): Observable<T> =
            httpUtils.get(String.Companion.format(url, page))
                    .map { jsonUtils.getListData(it, clazz) }
                    .flatMapObservable { Observable.from(it) }
}
