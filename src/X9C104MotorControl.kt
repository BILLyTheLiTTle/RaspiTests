import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin

fun main(args: Array<String>){

    val gpio = GpioFactory.getInstance()
    val pinIn1M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "Input1Motor1")
    val pinIn2M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "Input2Motor1")

    val pinInc = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "X9C104_INC")
    val pinUD = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "X9C104_U_D")
    val pinCS = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "X9C104_CS")
    //val pinWiper = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "X9C104_Wiper")
    // pinWiper.low()

    pinIn1M1.high()
    pinIn2M1.low()

    //pinInc.high()
    //pinUD.high()
    //pinCS.low()

    //pinUD.high()

    // It seems that this is working
    // More or less I followed these guidelines (http://www.instructables.com/id/X9C103P-Basic-Operation/)
    pinCS.high()
    pinUD.high()
    pinInc.high()
    pinCS.low()
    pinInc.low()
    pinCS.high()

    // TODO Find out how pinUD (Up / Down) is working

    // TODO Add an analog IC to read the values to make sure everything is working just fine

    Thread.sleep(5000)

    pinIn1M1.low()
    pinIn2M1.low()
    pinInc.low()
    pinCS.low()

    gpio.apply {
        shutdown()
        unprovisionPin(pinIn1M1)
        unprovisionPin(pinIn2M1)
        unprovisionPin(pinInc)
        unprovisionPin(pinUD)
        unprovisionPin(pinCS)
    }
}