import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.ParseException;

/**
 * This example code demonstrates how to access a few of the system information properties and
 * network information from the Raspberry Pi.
 *
 * @author Robert Savage
 */
object SystemInfo {

    @Throws(InterruptedException::class, IOException::class, ParseException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        // display a few of the available system information properties
        println("----------------------------------------------------")
        println("PLATFORM INFO")
        println("----------------------------------------------------")
        try {
            System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId())
        } catch (ex: UnsupportedOperationException) {
        }

        println("----------------------------------------------------")
        println("HARDWARE INFO")
        println("----------------------------------------------------")
        try {
            println("Serial Number     :  " + SystemInfo.getSerial())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Revision      :  " + SystemInfo.getCpuRevision())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Part          :  " + SystemInfo.getCpuPart())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Temperature   :  " + SystemInfo.getCpuTemperature())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CPU Model Name    :  " + SystemInfo.getModelName())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Processor         :  " + SystemInfo.getProcessor())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Hardware          :  " + SystemInfo.getHardware())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Hardware Revision :  " + SystemInfo.getRevision())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Board Type        :  " + SystemInfo.getBoardType().name)
        } catch (ex: UnsupportedOperationException) {
        }

        println("----------------------------------------------------")
        println("MEMORY INFO")
        println("----------------------------------------------------")
        try {
            println("Total Memory      :  " + SystemInfo.getMemoryTotal())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Used Memory       :  " + SystemInfo.getMemoryUsed())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Free Memory       :  " + SystemInfo.getMemoryFree())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Shared Memory     :  " + SystemInfo.getMemoryShared())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Cached Memory     :  " + SystemInfo.getMemoryCached())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P())
        } catch (ex: UnsupportedOperationException) {
        }

        println("----------------------------------------------------")
        println("OPERATING SYSTEM INFO")
        println("----------------------------------------------------")
        try {
            println("OS Name           :  " + SystemInfo.getOsName())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("OS Version        :  " + SystemInfo.getOsVersion())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("OS Architecture   :  " + SystemInfo.getOsArch())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate())
        } catch (ex: UnsupportedOperationException) {
        }

        println("----------------------------------------------------")
        println("JAVA ENVIRONMENT INFO")
        println("----------------------------------------------------")
        println("Java Vendor       :  " + SystemInfo.getJavaVendor())
        println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl())
        println("Java Version      :  " + SystemInfo.getJavaVersion())
        println("Java VM           :  " + SystemInfo.getJavaVirtualMachine())
        println("Java Runtime      :  " + SystemInfo.getJavaRuntime())

        println("----------------------------------------------------")
        println("NETWORK INFO")
        println("----------------------------------------------------")

        // display some of the network information
        println("Hostname          :  " + NetworkInfo.getHostname())
        for (ipAddress in NetworkInfo.getIPAddresses())
            println("IP Addresses      :  $ipAddress")
        for (fqdn in NetworkInfo.getFQDNs())
            println("FQDN              :  $fqdn")
        for (nameserver in NetworkInfo.getNameservers())
            println("Nameserver        :  $nameserver")

        println("----------------------------------------------------")
        println("CODEC INFO")
        println("----------------------------------------------------")
        try {
            println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled())
        } catch (ex: UnsupportedOperationException) {
        }

        println("----------------------------------------------------")
        println("CLOCK INFO")
        println("----------------------------------------------------")
        try {
            println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI())
        } catch (ex: UnsupportedOperationException) {
        }

        try {
            println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI())
        } catch (ex: UnsupportedOperationException) {
        }

        println()
        println()
        println("Exiting SystemInfoExample")
    }
}