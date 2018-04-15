import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.wiringpi.Gpio
import com.pi4j.wiringpi.SoftPwm

fun main(args: Array<String>){
    Gpio.wiringPiSetup();
    SoftPwm.softPwmCreate(4, 0, 100);

    val gpio = GpioFactory.getInstance()
    val pinDig2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "PinDig2")
    val pinEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Enable")

    pinEnable.high()
    pinDig2.low()

    var i = 0
    while(i<100) {
        SoftPwm.softPwmWrite(4, i)
        println("Speed: $i")
        Thread.sleep(3000)
        i += 20
    }

    Thread.sleep(5000)

    while(i>0) {
        SoftPwm.softPwmWrite(4, i)
        println("Speed: $i")
        Thread.sleep(3000)
        i -= 20
    }

    SoftPwm.softPwmStop(4);

    pinEnable.low()
    gpio.apply {
        shutdown()
        unprovisionPin(pinDig2)
        unprovisionPin(pinEnable)
    }
}