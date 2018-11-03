
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.util.CommandArgumentParser.getPin
import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.util.CommandArgumentParser.getPin
import com.pi4j.wiringpi.Gpio


fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    val pinPwmMotor1 = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_01, // default pin if no pin argument found
        null
    )
    val pwmM1 = gpio.provisionPwmOutputPin(pinPwmMotor1)

    val pinPwmMotor2 = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_24, // default pin if no pin argument found
        null
    )
    val pwmM2 = gpio.provisionPwmOutputPin(pinPwmMotor2)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    Gpio.pwmSetRange(100)
    Gpio.pwmSetClock(500)

    val pinDig2Motor1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "pinDig2Motor1")
    val pinEnableMotor1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "pinEnableMotor1")

    val pinDig2Motor2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "pinDig2Motor2")
    val pinEnableMotor2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "pinEnableMotor2")

    pinEnableMotor1.high()
    pinDig2Motor1.low()
    pinEnableMotor2.high()
    pinDig2Motor2.low()

    var i = 0
    while(i<100) {
        pwmM1.pwm = i
        println("M1 speed (i): $i and Speed(pwm): ${pwmM1.pwm}")
        pwmM2.pwm = i
        println("M2 speed (i): $i and Speed(pwm): ${pwmM2.pwm}")
        Thread.sleep(3000)
        i += 20
    }

    Thread.sleep(5000)

    while(i>0) {
        pwmM1.pwm = i
        println("M1 speed (i): $i and Speed(pwm): ${pwmM1.pwm}")
        pwmM2.pwm = i
        println("M2 speed (i): $i and Speed(pwm): ${pwmM2.pwm}")
        Thread.sleep(3000)
        i -= 20
    }

    pwmM1.pwm =0
    pwmM2.pwm =0

    pinEnableMotor1.low()
    pinEnableMotor2.low()

    gpio.apply {
        shutdown()
        unprovisionPin(pwmM1)
        unprovisionPin(pwmM2)
        unprovisionPin(pinDig2Motor1)
        unprovisionPin(pinEnableMotor1)
        unprovisionPin(pinDig2Motor2)
        unprovisionPin(pinEnableMotor2)
    }
}