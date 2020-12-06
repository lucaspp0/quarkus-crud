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

    fun gravarFoto(file: File) : String{
        val data = Files.readAllBytes(file.toPath())
        var nameFile = File.separator + System.currentTimeMillis() + "_" + file.name

        if(file.extension != null && !(file.extension.isEmpty() || file.extension.isBlank()) )
            nameFile = "." + file.extension

        val newFile =
            File(Paths.get(
                Paths.get(".").toAbsolutePath().normalize().toString())
                .toString())

        if(!newFile.exists()) newFile.mkdir()

        writeFile(data, newFile.absolutePath + nameFile)
        return newFile.absolutePath + nameFile;
    }

    @Throws(IOException::class)
    fun obterbase64(fullPath: String): String? {
        val file = File(fullPath)
        val encoded: ByteArray = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file))
        return String(encoded, StandardCharsets.US_ASCII)
    }

    @Throws(IOException::class)
    fun writeFile(content: ByteArray, filename: String) {
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