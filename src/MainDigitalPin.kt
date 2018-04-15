import com.pi4j.system.SystemInfo
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.GpioPinDigitalOutput


//val pinPwm = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PinPwm")


fun main(args: Array<String>){
    println("Serial: " + SystemInfo.getSerial())
    val gpio = GpioFactory.getInstance()
    val pinDig1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "PinDig1")
    val pinDig2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "PinDig2")
    val pinEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Enable")

    pinEnable.high()

    pinDig1.high()
    pinDig2.low()

    Thread.sleep(2000)

    pinDig1.low()
    pinDig2.low()

    pinEnable.low()

    gpio.apply {
        shutdown()
        unprovisionPin(pinDig1)
        unprovisionPin(pinDig2)
        unprovisionPin(pinEnable)
    }

}