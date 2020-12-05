package ead.experience.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object FileUtil {

    fun GravarFoto(file: File) : String{
        val data = Files.readAllBytes(file.toPath())
        val nameFile = File.separator + System.currentTimeMillis() + "_" + file.name + "." + file.extension
        val newFile = File(Paths.get(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "data" ).toString())
        if(!newFile.exists()) file.mkdir()

        writeFile(data, newFile.absolutePath + "img" + newFile.absolutePath + nameFile)
        return newFile.absolutePath + "img" + nameFile;
    }

    @Throws(IOException::class)
    fun Obterbase64(fullPath: String): String? {
        val file = File(fullPath)
        val encoded: ByteArray = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file))
        return String(encoded, StandardCharsets.US_ASCII)
    }

    @Throws(IOException::class)
    private fun writeFile(content: ByteArray, filename: String) {
        val file = File(filename)
        if (!file.exists()) {
            file.createNewFile()
        }
        val fop = FileOutputStream(file)
        fop.write(content)
        fop.flush()
        fop.close()
    }
}