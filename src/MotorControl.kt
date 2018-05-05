import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

lateinit var pwmPinEnable: GpioPinPwmOutput
lateinit var pinInput1: GpioPinDigitalOutput
lateinit var pinInput2: GpioPinDigitalOutput

fun main(args: Array<String>){
    val gpio = GpioFactory.getInstance()

    //Initialize the "Enable" pin as PWM
    val pinEnable = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        RaspiPin.GPIO_01, // default pin if no pin argument found
        null
    )
    pwmPinEnable = gpio.provisionPwmOutputPin(pinEnable)

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
    Gpio.pwmSetRange(100);
    Gpio.pwmSetClock(500);

    //Initialize "Input" pins as simple GPIOs
    pinInput1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "Pin_Input_1")
    pinInput2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Pin_Input_2")

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
        unprovisionPin(pwmPinEnable)
        unprovisionPin(pinInput1)
        unprovisionPin(pinInput2)
    }
}

fun throttle(front: Boolean){
    println("ACTION ${::throttle.name}")
    if (front) {
        pinInput1.high()
        pinInput2.low()
    }
    else {
        pinInput1.low()
        pinInput2.high()
    }
    var i = 0
    while(i<=100) {
        pwmPinEnable.pwm = i
        println("Speed (i): $i and Speed(pwm): ${pwmPinEnable.pwm}")
        Thread.sleep(3000)
        i += 20
    }
}

// It is not working. Need Another type of H-bridge
fun freeWheel(){
    println("ACTION ${::freeWheel.name}")
    pwmPinEnable.pwm = 0
    Thread.sleep(5000)
}

//http://forum.arduino.cc/index.php?topic=346537.0
fun brake() {
    println("ACTION ${::brake.name}")
    pwmPinEnable.pwm = 100
    pinInput1.low()
    pinInput2.low()
    //pinInput1.high()
    //pinInput2.high()
    Thread.sleep(5000)
}

fun reset() {
    println("ACTION ${::reset.name}")
    pinInput1.low()
    pinInput2.low()
    pwmPinEnable.pwm = 0
}

