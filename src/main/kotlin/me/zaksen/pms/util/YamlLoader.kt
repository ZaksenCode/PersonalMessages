package me.zaksen.pms.util

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import java.io.File

class YamlLoader(
    configuration: YamlConfiguration = YamlConfiguration(strictMode = false),
    serializersModule: SerializersModule = SerializersModule {  }
) {
    val yaml by lazy { Yaml(
        configuration = configuration,
        serializersModule = serializersModule
    )}

    fun loadFile(folder: File, name: String): File {
        try {
            folder.mkdirs()
            val file = File(folder, name)
            if(!file.exists()) file.createNewFile()
            return file
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }

    inline fun <reified T> loadData(folder: File, dataName: String): T {
        val file = loadFile(folder, dataName)

        if(file.length() == 0L) {
            val data = T::class.java.getDeclaredConstructor().newInstance()
            saveData(folder, dataName, data)
            return data
        }

        return yaml.decodeFromString(file.readText())
    }

    inline fun <reified T> saveData(folder: File, dataName: String, value: T) {
        val text = yaml.encodeToString<T>(value)
        loadFile(folder, dataName).writeText(text)
    }
}