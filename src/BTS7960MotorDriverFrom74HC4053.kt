import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    // initialize pins for 74HC4053
    val pinMuxEnable = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "74HC4053 Enable")
    val pinMuxS0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "74HC4053 S0")
    val pinMuxS1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "74HC4053 S1")
    val pinMuxS2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "74HC4053 S2")

    // initialize pin for PWM control
    val pinMuxBPwmMotor = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_23, // default pin if no pin argument found
        null
    )
    val pinPwmB = gpio.provisionPwmOutputPin(pinMuxBPwmMotor)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    Gpio.pwmSetRange(100)
    Gpio.pwmSetClock(500)

    // initialize pins for BTS7960
    val pinLEn = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "L_EN")
    val pinREn = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "R_EN")

    pinMuxEnable.low()

    // braking
    println("Braking")
    // 74HC4053 control
    pinMuxEnable.high() // disable the mux, so no output will go to the h-bridge (both PWMs will be near to GND)
    // BTS7970 control
    pinLEn.high()
    pinREn.high()
    pinPwmB.pwm=0 // I don't want accidental throttling when brakes are off
    Thread.sleep(5000)

    // forward throttle
    println("Forward Throttle")
    // 74HC4053 control
    pinMuxEnable.low()
    // Activate C0, B0, A0 - I have the pwm to B channel
    pinMuxS0.low()
    pinMuxS1.low()
    pinMuxS2.low()
    // BTS7960 control
    pinLEn.high()
    pinREn.high()
    var i = 0
    while(i<100) {
        pinPwmB.pwm = i
        println("FORWARD speed (i): $i and Speed(pwm): ${pinPwmB.pwm}")
        Thread.sleep(2000)
        i += 20
    }
    Thread.sleep(5000)

    // free wheeling
    println("Free Wheeling")
    // 74HC4053 control
    pinMuxEnable.low()
    // BTS7960 control
    pinPwmB.pwm = 0
    pinLEn.low()
    pinREn.low()
    Thread.sleep(5000)

    // backward throttle
    println("Backward Throttle")
    // 74HC4053 control
    pinMuxEnable.low()
    // Activate C1, B1, A1 - I have the pwm to B channel
    pinMuxS0.high()
    pinMuxS1.high()
    pinMuxS2.high()
    // BTS7960 control
    pinLEn.high()
    pinREn.high()
    var j = 0
    while(j<100) {
        pinPwmB.pwm = j
        println("BACKWARD speed (j): $j and Speed(pwm): ${pinPwmB.pwm}")
        Thread.sleep(2000)
        j += 20
    }
    Thread.sleep(5000)

    // braking
    println("Braking")
    // 74HC4053 control
    pinMuxEnable.high() // disable the mux, so no output will go to the h-bridge (both PWMs will be near to GND)
    // BTS7970 control
    pinLEn.high()
    pinREn.high()
    pinPwmB.pwm=0 // I don't want accidental throttling when brakes are off
    Thread.sleep(5000)

    gpio.apply {
        shutdown()
        unprovisionPin(pinMuxEnable)
        unprovisionPin(pinMuxS0)
        unprovisionPin(pinMuxS1)
        unprovisionPin(pinMuxS2)
        unprovisionPin(pinPwmB)
        unprovisionPin(pinLEn)
        unprovisionPin(pinREn)
    }
}