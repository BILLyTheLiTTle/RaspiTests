
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
    val pinPwm = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_01, // default pin if no pin argument found
        null
    )
    val pwm = gpio.provisionPwmOutputPin(pinPwm)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
    Gpio.pwmSetRange(100);
    Gpio.pwmSetClock(500);

    val pinDig2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "PinDig2")
    val pinEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Enable")

    pinEnable.high()
    pinDig2.low()

    var i = 0
    while(i<100) {
        pwm.pwm = i
        println("Speed (i): $i and Speed(pwm): ${pwm.pwm}")
        Thread.sleep(3000)
        i += 20
    }

    Thread.sleep(5000)

    while(i>0) {
        pwm.pwm = i
        println("Speed (i): $i and Speed(pwm): ${pwm.pwm}")
        Thread.sleep(3000)
        i -= 20
    }

    pwm.pwm =0

    pinEnable.low()

    gpio.apply {
        shutdown()
        unprovisionPin(pinDig2)
        unprovisionPin(pinEnable)
    }
}