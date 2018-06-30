import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.RaspiPin


fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()
    val pinDS = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "DS") // DATA
    val pinSTCP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "STCP") // STORE
    val pinSHCP = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "SHCP") // SHIFT

    controlShiftRegister(pinDS, pinSTCP, pinSHCP, "forward")

    Thread.sleep(2000)

    controlShiftRegister(pinDS, pinSTCP, pinSHCP, "backward")

    Thread.sleep(2000)

    resetShiftRegister(pinDS, pinSTCP, pinSHCP)
    gpio.apply {
        shutdown()
        unprovisionPin(pinDS)
        unprovisionPin(pinSTCP)
        unprovisionPin(pinSHCP)
    }
}

fun pulsePin(pin: GpioPinDigitalOutput){
    pin.high()
    pin.low()
}

fun controlShiftRegister(ds: GpioPinDigitalOutput, stcp: GpioPinDigitalOutput, shcp: GpioPinDigitalOutput,
                         direction: String){
    // shift register output 7
    ds.low()
    pulsePin(shcp)
    // shift register output 6
    ds.high()
    pulsePin(shcp)
    // shift register output 5
    ds.low()
    pulsePin(shcp)
    // shift register output 4
    ds.low()
    pulsePin(shcp)
    // shift register output 3
    ds.low()
    pulsePin(shcp)
    // shift register output 2 -> H-bridge input 2
    if(direction == "forward") ds.high() else ds.low()
    pulsePin(shcp)
    // shift register output 1 -> H-bridge input 1
    if(direction == "forward") ds.low() else ds.high()
    pulsePin(shcp)
    // shift register output 0 -> H-bridge enable
    ds.high()
    pulsePin(shcp)

    pulsePin(stcp)
}

fun resetShiftRegister(ds: GpioPinDigitalOutput, stcp: GpioPinDigitalOutput, shcp: GpioPinDigitalOutput){
    for (i in 0..7) {
        ds.low()
        pulsePin(shcp)
    }

    pulsePin(stcp)
}