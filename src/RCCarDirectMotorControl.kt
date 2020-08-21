import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

fun main(args: Array<String>) {
    // Initialize pins
    val gpio = GpioFactory.getInstance()
    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    //Gpio.pwmSetRange(100)
    //Gpio.pwmSetClock(500)

    val enablerCW = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "CW Enabler")
    val enablerCCW = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "CCW Enabler")

    val pinEnableM2 = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_24, // default pin if no pin argument found
        null
    )
    val motorControl = gpio.provisionPwmOutputPin(pinEnableM2)
    //motorControl.setPwmRange(100)

    // Control motor
    enablerCCW.low()
    enablerCW.high()
    motorControl.pwm = 0

    motorControl.pwm = 250
    println("Moving @250...")
    Thread.sleep(5000)
    motorControl.pwm = 500
    println("Moving @500...")
    Thread.sleep(5000)
    motorControl.pwm = 750
    println("Moving @750...")
    Thread.sleep(5000)
    motorControl.pwm = 800
    println("Moving @800...")
    Thread.sleep(5000)
    motorControl.pwm = 900
    println("Moving @900...")
    Thread.sleep(5000)
    motorControl.pwm = 1024
    println("Moving @1024...")
    Thread.sleep(5000)

    motorControl.pwm = 0

    gpio.apply {
        shutdown()
        unprovisionPin(enablerCW)
        unprovisionPin(enablerCCW)
        unprovisionPin(motorControl)
    }
}