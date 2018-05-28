
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.spi.SpiChannel
import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23017Pin
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.i2c.I2CBus
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio
import sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.setState




fun main(args: Array<String>){
    // create gpio controller
    val gpio = GpioFactory.getInstance()

    println("<--Pi4J--> MCP23017 GPIO Example ... started.")

    // create custom MCP23017 GPIO provider

    // Use "i2cdetect -y 1" on Raspberry Pi 3 to detect the address
    val address = 0x20
    val provider = MCP23017GpioProvider(I2CBus.BUS_1, address)
    println("MCP23017 Found: ${ provider != null }")

    // provision gpio output pins and make sure they are all LOW at startup
    val myBOutputs = arrayOf(
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B0, "MyInput-B0", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B1, "MyInput-B1", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B2, "MyInput-B2", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B3, "MyInput-B3", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B4, "MyInput-B4", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B5, "MyInput-B5", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B6, "MyInput-B6", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_B7, "MyInput-B7", PinState.LOW)
    )

    // provision gpio output pins and make sure they are all LOW at startup
    val myAOutputs = arrayOf(
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A0, "MyOutput-A0", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A1, "MyOutput-A1", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A2, "MyOutput-A2", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A3, "MyOutput-A3", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A4, "MyOutput-A4", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A5, "MyOutput-A5", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A6, "MyOutput-A6", PinState.LOW),
        gpio.provisionDigitalOutputPin(provider, MCP23017Pin.GPIO_A7, "MyOutput-A7", PinState.LOW))

    // start one motor
    myAOutputs[0].low()
    myAOutputs[1].high()
    myAOutputs[2].high()

    Thread.sleep(3000)

    // start the other motor
    myBOutputs[5].high()
    myBOutputs[6].high()
    myBOutputs[7].low()

    Thread.sleep(3000)

    // all pin to LOW
    for (count in 0..9) {
        gpio.setState(false, *myAOutputs)
        gpio.setState(false, *myBOutputs)
    }


    // stop all GPIO activity/threads by shutting down the GPIO controller
    // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
    gpio.shutdown()

    println("Exiting MCP23017GpioExample")
}