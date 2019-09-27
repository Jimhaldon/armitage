package com.wavproductions.www.armitage

import java.io.Closeable
import java.io.InputStream
import java.net.Socket
import java.nio.file.Files
import java.nio.file.Path

class Metasploit : Closeable {
    private val console: Process?
    private val temp: Path = Files.createTempFile("console", "MCArmitage")
    private val fileIn: InputStream
    private var rpc: Socket? = null //will be a direct connection, so most likely a socket

    init {
        fileIn = Files.newInputStream(temp)
        console = null
    }

    fun consoleActive(): Boolean {
        return console?.isAlive ?: false
    }

    fun startRPC(): Boolean {
        if (!consoleActive()) { //make sure console is active to init rpc
            return false
        }
        return false //rpc failed to init!
    }

    fun sendConsoleCommand(command: ByteArray, flush: Boolean = false) {
        console?.outputStream?.write(command)
        if (flush) {
            console?.outputStream?.flush()
        }
    }

    fun readConsole(): ByteArray {
        return fileIn.readAllBytes()
    }

    fun readRPC(): ByteArray {
        return rpc?.inputStream?.readAllBytes() ?: ByteArray(0)
    }

    fun sendRPCCommand(command: ByteArray, flush: Boolean = false) {
        rpc?.outputStream?.write(command)
        if (flush) {
            rpc?.outputStream?.flush()
        }
    }

    override fun close() { //destroy resources
        console?.destroyForcibly()
        rpc?.close()
        fileIn.close()
        Files.deleteIfExists(temp)
    }
}