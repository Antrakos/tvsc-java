package com.tvsc.service

import com.tvsc.core.model.Serial
import rx.Observable
import rx.Single

/**
 *
 * @author Taras Zubrei
 */
interface SerialService {
    fun getSerialInfo(id: Long): Single<Serial>
    fun getSerial(id: Long): Single<Serial>
    fun restoreAllData(): Observable<Serial>
    fun count(): Long
    fun addSerial(id: Long)
    fun deleteSerial(id: Long)
}