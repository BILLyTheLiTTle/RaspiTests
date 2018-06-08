import com.pi4j.system.SystemInfo
import java.io.InputStreamReader
import java.io.BufferedReader



fun main(args: Array<String>) {
    // CPU temp
    try {
        val cpuTemp = SystemInfo.getCpuTemperature().toInt()
        println(cpuTemp)
    } catch (ex: UnsupportedOperationException) {
    }

    // GPU temp
    val args = arrayOf("vcgencmd", "measure_temp")
    val proc = ProcessBuilder(*args).start()
    // Read the output
    val reader = BufferedReader(InputStreamReader(proc.inputStream))

    var line = reader.readLine()
    val gpuTemp = line.subSequence(5, 7).toString().toInt()
    println(gpuTemp)
}