package com.tvsc.service

import com.tvsc.core.model.Serial
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface SerialService {
    fun getSerialInfo(id: Long): CompletableFuture<Serial>
    fun getSerial(id: Long): CompletableFuture<Serial>
    fun restoreAllData(): CompletableFuture<List<Serial>>
    fun count(): Long
    fun addSerial(id: Long)
    fun deleteSerial(id: Long)
}