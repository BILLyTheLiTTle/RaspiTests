import com.pi4j.io.gpio.*
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

// GPIO21 - P05 - L_EN
// GPIO22 - P06 - R_EN
// GPIO24 - P19 - L_PWM
// GPIO23 - P13 - R_PWM

fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    val pinLEn = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "L_EN")
    val pinREn = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "R_EN")

    val pinLPwm = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "L_PWM")
    val pinRPwm = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "R_PWM")

    /*val pinL = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_24, // default pin if no pin argument found
        null
    )
    val pinLPwm = gpio.provisionPwmOutputPin(pinL, "L_PWM")

    val pinR = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_23, // default pin if no pin argument found
        null
    )
    val pinRPwm = gpio.provisionPwmOutputPin(pinR, "R_PWM")*/


    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    Gpio.pwmSetRange(100)
    Gpio.pwmSetClock(500)

    // brake
    println("Braking")
    pinRPwm.low()
    pinLPwm.low()
    pinLEn.high()
    pinREn.high()
    Thread.sleep(5000)

    // throttle forward
    println("Forward Throttle")
    pinRPwm.low()
    pinLPwm.high()
    pinLEn.high()
    pinREn.high()
    /*var i = 0
    while(i<100) {
        pinLPwm.pwm = i
        println("FORWARD speed (i): $i and Speed(pwm): ${pinLPwm.pwm}")
        Thread.sleep(2000)
        i += 20
    }*/
    Thread.sleep(5000)

    // free spinning
    println("Free Spinning")
    pinLEn.low()
    pinREn.low()
    Thread.sleep(5000)

    // throttle backward
    println("Backward Throttle")
    pinLPwm.low()
    pinLEn.high()
    pinREn.high()
    pinRPwm.high()
    /*var j = 0
    while(j<100) {
        pinRPwm.pwm = j
        println("BACKWARD speed (j): $j and Speed(pwm): ${pinRPwm.pwm}")
        Thread.sleep(2000)
        j += 20
    }*/
    Thread.sleep(5000)

    // brake
    println("Braking")
    pinRPwm.low()
    pinLPwm.low()
    pinLEn.high()
    pinREn.high()
    Thread.sleep(5000)

    // blow the capacitor
    // Just kidding, not gonna try it!

    gpio.apply {
        shutdown()
        unprovisionPin(pinLEn)
        unprovisionPin(pinREn)
        unprovisionPin(pinLPwm)
        unprovisionPin(pinRPwm)
    }
}