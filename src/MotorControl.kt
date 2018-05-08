import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

lateinit var pwmPinEnableM1: GpioPinPwmOutput
lateinit var pwmPinEnableM2: GpioPinPwmOutput
lateinit var pinInput1M1: GpioPinDigitalOutput
lateinit var pinInput2M1: GpioPinDigitalOutput
lateinit var pinInput1M2: GpioPinDigitalOutput
lateinit var pinInput2M2: GpioPinDigitalOutput

fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    //Initialize the "Enable" pin as PWM
    val pinEnableM1 = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_01, // default pin if no pin argument found
        null
    )
    pwmPinEnableM1 = gpio.provisionPwmOutputPin(pinEnableM1)
    val pinEnableM2 = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_26, // default pin if no pin argument found
        null
    )
    pwmPinEnableM2 = gpio.provisionPwmOutputPin(pinEnableM2)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
    Gpio.pwmSetRange(100);
    Gpio.pwmSetClock(500);

    //Initialize "Input" pins as simple GPIOs
    pinInput1M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Pin_Input_1_M1")
    pinInput2M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin_Input_2_M1")
    pinInput1M2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "Pin_Input_1_M2")
    pinInput2M2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "Pin_Input_2_M2")

    freeWheel()
    brake()
    throttle(true)
    brake()
    throttle(false)
    brake()
    freeWheel()
    reset()

    gpio.apply {
        shutdown()
        unprovisionPin(pwmPinEnableM1)
        unprovisionPin(pinInput1M1)
        unprovisionPin(pinInput2M1)
        unprovisionPin(pwmPinEnableM2)
        unprovisionPin(pinInput1M2)
        unprovisionPin(pinInput2M2)
    }
}

fun throttle(front: Boolean){
    println("ACTION ${::throttle.name}")
    if (front) {
        pinInput1M1.high()
        pinInput2M1.low()
        pinInput1M2.high()
        pinInput2M2.low()
    }
    else {
        pinInput1M1.low()
        pinInput2M1.high()
        pinInput1M2.low()
        pinInput2M2.high()
    }
    var i = 0
    while(i<=100) {
        pwmPinEnableM1.pwm = i
        pwmPinEnableM2.pwm = i
        println("Speed (i): $i and Speed(pwm): ${pwmPinEnableM1.pwm}")
        println("Speed (i): $i and Speed(pwm): ${pwmPinEnableM2.pwm}")
        Thread.sleep(3000)
        i += 20
    }
}

// It is not working. Need Another type of H-bridge?
fun freeWheel(){
    println("ACTION ${::freeWheel.name}")
    pinInput1M1.high()
    pinInput2M1.low()
    pinInput1M2.high()
    pinInput2M2.low()
    pwmPinEnableM1.pwm = 0
    pwmPinEnableM2.pwm = 0
    Thread.sleep(5000)
}

//http://forum.arduino.cc/index.php?topic=346537.0
fun brake() {
    println("ACTION ${::brake.name}")
    pwmPinEnableM1.pwm = 100
    pinInput1M1.low()
    pinInput2M1.low()
    pwmPinEnableM2.pwm = 100
    pinInput1M2.low()
    pinInput2M2.low()
    //pinInput1.high()
    //pinInput2.high()
    Thread.sleep(5000)
}

fun reset() {
    println("ACTION ${::reset.name}")
    pinInput1M1.low()
    pinInput2M1.low()
    pwmPinEnableM1.pwm = 0
    pinInput1M2.low()
    pinInput2M2.low()
    pwmPinEnableM2.pwm = 0
}

