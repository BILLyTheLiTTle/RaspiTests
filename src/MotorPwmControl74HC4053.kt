import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

// TIP 1: GND pin should be connected to Raspi GND or else the motor rotates to the same direction
// TIP 2: IN/OUT pins should be pulled down with a 10KΩ although the motor had no problem without them
// TIP 3: ENABLE pin could be connected to GND or pulled down with a 10KΩ instead of using a GPIO

fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    val pinMuxBPwmMotor = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_24, // default pin if no pin argument found
        null
    )
    val pwmB = gpio.provisionPwmOutputPin(pinMuxBPwmMotor)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    Gpio.pwmSetRange(100)
    Gpio.pwmSetClock(500)

    val pinMuxEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "74HC4053 Enable")

    val pinMuxS0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "74HC4053 S0")
    val pinMuxS1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "74HC4053 S1")
    val pinMuxS2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "74HC4053 S2")

    pinMuxEnable.low()

    // Activate C0, B0, A0 - I have the pwm to B channel
    pinMuxS0.low()
    pinMuxS1.low()
    pinMuxS2.low()

    var i = 0
    while(i<100) {
        pwmB.pwm = i
        println("M1 speed (i): $i and Speed(pwm): ${pwmB.pwm}")
        pwmB.pwm = i
        println("M2 speed (i): $i and Speed(pwm): ${pwmB.pwm}")
        Thread.sleep(2000)
        i += 20
    }

    Thread.sleep(3000)

    // Activate C1, B1, A1 - I have the pwm to B channel
    pinMuxS0.high()
    pinMuxS1.high()
    pinMuxS2.high()

    var j = 0
    while(j<100) {
        pwmB.pwm = j
        println("M1 speed (j): $j and Speed(pwm): ${pwmB.pwm}")
        pwmB.pwm = j
        println("M2 speed (j): $j and Speed(pwm): ${pwmB.pwm}")
        Thread.sleep(2000)
        j += 20
    }

    Thread.sleep(3000)

    pwmB.pwm =0
    pinMuxEnable.low()
    pinMuxS0.low()
    pinMuxS1.low()
    pinMuxS2.low()

    gpio.apply {
        shutdown()
        unprovisionPin(pwmB)
        unprovisionPin(pinMuxEnable)
        unprovisionPin(pinMuxS0)
        unprovisionPin(pinMuxS1)
        unprovisionPin(pinMuxS2)
    }

}
