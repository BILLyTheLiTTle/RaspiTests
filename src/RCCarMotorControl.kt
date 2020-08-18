import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23S17Pin
import com.pi4j.gpio.extension.mcp.MCP23S17Pin.*
import com.pi4j.io.gpio.*
import com.pi4j.io.spi.SpiChannel
import com.pi4j.util.CommandArgumentParser
import com.pi4j.wiringpi.Gpio

/* == From RasPi to Rear 74HC4053(IC1) == */
private val MOTOR_FRONT_RIGHT_PWM_PIN = RaspiPin.GPIO_24
private val MOTOR_FRONT_LEFT_PWM_PIN = RaspiPin.GPIO_23
private val MOTOR_REAR_RIGHT_PWM_PIN = RaspiPin.GPIO_26
private val MOTOR_REAR_LEFT_PWM_PIN = RaspiPin.GPIO_01

/* == From MCP23S17(IC0) to Rear BTS7960 bridges == */
private val REAR_RIGHT_BRIDGE_ENABLER_PIN = GPIO_B4
private val REAR_LEFT_BRIDGE_ENABLER_PIN = GPIO_B3

/* == From MCP23S17(IC0) to Front BTS7960 bridges == */
private val FRONT_RIGHT_BRIDGE_ENABLER_PIN = GPIO_A4
private val FRONT_LEFT_BRIDGE_ENABLER_PIN = GPIO_A3

/* == From MCP23S17(IC0) to Rear 74HC4053(IC1) == */
private val REAR_MULTIPLEXER_SELECTOR_1 = GPIO_B0
private val REAR_MULTIPLEXER_SELECTOR_2 = GPIO_B1
private val REAR_MULTIPLEXER_SELECTOR_3 = GPIO_B2

/* == From MCP23S17(IC0) to Front 74HC4053(IC0) == */
private val FRONT_MULTIPLEXER_SELECTOR_1 = GPIO_A0
private val FRONT_MULTIPLEXER_SELECTOR_2 = GPIO_A1
private val FRONT_MULTIPLEXER_SELECTOR_3 = GPIO_A2

private lateinit var motorFrontRightPwmPin: GpioPinPwmOutput
private lateinit var motorFrontRightDirPin: GpioPinDigitalOutput
private lateinit var motorFrontRightEnablerPin: GpioPinDigitalOutput
private lateinit var motorFrontLeftPwmPin: GpioPinPwmOutput
private lateinit var motorFrontLeftDirPin: GpioPinDigitalOutput
private lateinit var motorFrontLeftEnablerPin: GpioPinDigitalOutput
private lateinit var motorRearRightPwmPin: GpioPinPwmOutput
private lateinit var motorRearRightDirPin: GpioPinDigitalOutput
private lateinit var motorRearRightEnablerPin: GpioPinDigitalOutput
private lateinit var motorRearLeftPwmPin: GpioPinPwmOutput
private lateinit var motorRearLeftDirPin: GpioPinDigitalOutput
private lateinit var motorRearLeftEnablerPin: GpioPinDigitalOutput

private lateinit var gpio: GpioController
private lateinit var provider: MCP23S17GpioProvider

fun main(args: Array<String>) {

    // create gpio controller
    gpio = GpioFactory.getInstance()

    Gpio.pwmSetMode(Gpio.PWM_MODE_MS)
    //Gpio.pwmSetClock(2)
    //Gpio.pwmSetRange(200)

    println("<--Pi4J--> RCCar motor control ... started.")

    provider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)
    println("MCP23S17 Found: ${provider != null}")

    initialize()

    /* The right motors should rotate CW (R_PWM=1, Sn=0) and the left CCW (L_PWM=1, Sn=1) for forward movement.

     */

    /*val direction = 0 // 0 - Forward, 1 - Backward

    if(direction == 0) {
        motorFrontRightDirPin.low()
        motorFrontLeftDirPin.high()
        motorRearRightDirPin.low()
        motorRearLeftDirPin.high()
    }
    else if (direction == 1) {
        motorFrontRightDirPin.high()
        motorFrontLeftDirPin.low()
        motorRearRightDirPin.high()
        motorRearLeftDirPin.low()
    }
    else {
        motorFrontRightEnablerPin.low()
        motorFrontLeftEnablerPin.low()
        motorRearRightEnablerPin.low()
        motorRearLeftEnablerPin.low()
    }

    for (i in 1..1) {
        motorFrontRightPwmPin.pwm = 0
        motorFrontLeftPwmPin.pwm = 0
        motorRearRightPwmPin.pwm = 0
        motorRearLeftPwmPin.pwm = 0
        Thread.sleep(500)

        // In order to have movement to 4 motors simultaneously the minimum value is 543/1024
        motorFrontRightPwmPin.pwm = 544 //543
        motorFrontLeftPwmPin.pwm = 544 //513
        motorRearRightPwmPin.pwm = 544 //513
        motorRearLeftPwmPin.pwm = 544 //513
        println("Moving...")
        Thread.sleep(4200)

        motorFrontRightPwmPin.pwm = 0
        motorFrontLeftPwmPin.pwm = 0
        motorRearRightPwmPin.pwm = 0
        motorRearLeftPwmPin.pwm = 0
    }*/

    val actions = arrayOf("forward", "brake", "backward", "freely") // forward, backward, freely, brake, error
    val delay = 3000L
    for (action in actions) {
        println( when (action) {
            "forward" -> {
                // TODO set forward direction firstly
                applyPinValues(
                    motorFrontRightMovingCW = PinState.LOW, motorFrontRightEnable = PinState.HIGH,
                    motorFrontLeftMovingCW = PinState.HIGH, motorFrontLeftEnable = PinState.HIGH,
                    motorRearRightMovingCW = PinState.LOW, motorRearRightEnable = PinState.HIGH,
                    motorRearLeftMovingCW = PinState.HIGH, motorRearLeftEnable = PinState.HIGH
                )
                // TODO set throttle
                applyPinValues(
                    motorFrontRightPwm = 544, motorFrontLeftPwm = 544,
                    motorRearRightPwm = 544, motorRearLeftPwm = 544
                )
                Thread.sleep(1000)
                /*applyPinValues(
                    motorFrontRightPwm = 800, motorFrontLeftPwm = 800,
                    motorRearRightPwm = 800, motorRearLeftPwm = 800
                )
                Thread.sleep(1000)*/
                applyPinValues(
                    motorFrontRightPwm = 1024, motorFrontLeftPwm = 1024,
                    motorRearRightPwm = 1024, motorRearLeftPwm = 1024
                )
                Thread.sleep(1000)
                "FORWARD"
            }
            "backward" -> {
                // TODO set backward direction firstly
                applyPinValues(
                    motorFrontRightMovingCW = PinState.HIGH, motorFrontRightEnable = PinState.HIGH,
                    motorFrontLeftMovingCW = PinState.LOW, motorFrontLeftEnable = PinState.HIGH,
                    motorRearRightMovingCW = PinState.HIGH, motorRearRightEnable = PinState.HIGH,
                    motorRearLeftMovingCW = PinState.LOW, motorRearLeftEnable = PinState.HIGH
                )
                // TODO set throttle
                applyPinValues(
                    motorFrontRightPwm = 0, motorFrontLeftPwm = 0,
                    motorRearRightPwm = 1024, motorRearLeftPwm = 0
                )
                "BACKWARD"
            }
            "freely" -> {
                // TODO move freely
                applyPinValues(
                    0, PinState.LOW, PinState.LOW,
                    0, PinState.LOW, PinState.LOW,
                    0, PinState.LOW, PinState.LOW,
                    0, PinState.LOW, PinState.LOW
                )
                "FREELY"
            }
            "brake" -> {
                // TODO brake
                applyPinValues(
                    0, PinState.LOW, PinState.HIGH,
                    0, PinState.LOW, PinState.HIGH,
                    0, PinState.LOW, PinState.HIGH,
                    0, PinState.LOW, PinState.HIGH
                )
                "BRAKE"
            }
            else -> "Should not happen"
        })
        Thread.sleep(delay)
    }

    // Reset
    applyPinValues(
        0, PinState.LOW, PinState.LOW,
        0, PinState.HIGH, PinState.LOW,
        0, PinState.LOW, PinState.LOW,
        0, PinState.HIGH, PinState.LOW
    )

    motorFrontRightEnablerPin.low()
    motorFrontLeftEnablerPin.low()
    motorRearRightEnablerPin.low()
    motorRearLeftEnablerPin.low()


    gpio.apply {
        shutdown()
        unprovisionPin(motorFrontRightPwmPin)
        unprovisionPin(motorFrontRightDirPin)
        unprovisionPin(motorFrontRightEnablerPin)

        unprovisionPin(motorFrontLeftPwmPin)
        unprovisionPin(motorFrontLeftDirPin)
        unprovisionPin(motorFrontLeftEnablerPin)

        unprovisionPin(motorRearRightPwmPin)
        unprovisionPin(motorRearRightDirPin)
        unprovisionPin(motorRearRightEnablerPin)

        unprovisionPin(motorRearLeftPwmPin)
        unprovisionPin(motorRearLeftDirPin)
        unprovisionPin(motorRearLeftEnablerPin)
    }
}

private fun applyPinValues(
    motorFrontRightPwm: Int? = null, motorFrontRightMovingCW: PinState? = null, motorFrontRightEnable: PinState? = null,
    motorFrontLeftPwm: Int? = null, motorFrontLeftMovingCW: PinState? = null, motorFrontLeftEnable: PinState? = null,
    motorRearRightPwm: Int? = null, motorRearRightMovingCW: PinState? = null, motorRearRightEnable: PinState? = null,
    motorRearLeftPwm: Int? = null, motorRearLeftMovingCW: PinState? = null, motorRearLeftEnable: PinState? = null){

    motorFrontRightPwm?.let { motorFrontRightPwmPin.pwm = it }
    motorFrontRightMovingCW?.let { motorFrontRightDirPin.state = it }
    motorFrontRightEnable?.let { motorFrontRightEnablerPin.state = it }
    motorFrontLeftPwm?.let { motorFrontLeftPwmPin.pwm = it }
    motorFrontLeftMovingCW?.let { motorFrontLeftDirPin.state = it }
    motorFrontLeftEnable?.let { motorFrontLeftEnablerPin.state = it }
    motorRearRightPwm?.let { motorRearRightPwmPin.pwm = it }
    motorRearRightMovingCW?.let { motorRearRightDirPin.state = it }
    motorRearRightEnable?.let { motorRearRightEnablerPin.state = it }
    motorRearLeftPwm?.let { motorRearLeftPwmPin.pwm = it }
    motorRearLeftMovingCW?.let { motorRearLeftDirPin.state = it }
    motorRearLeftEnable?.let { motorRearLeftEnablerPin.state = it }
}

private fun initialize() {

    /* = Front Motors = */

    /* == Front Right Motor == */
    val motorFrontRightPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_FRONT_RIGHT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorFrontRightPwmPin = gpio.provisionPwmOutputPin(motorFrontRightPin)
    //motorFrontRightPwmPin.setPwmRange(1000)

    motorFrontRightEnablerPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_RIGHT_BRIDGE_ENABLER_PIN,
        "Front Right Motor En Pin", PinState.HIGH)

    motorFrontRightDirPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_MULTIPLEXER_SELECTOR_2,
        "Front Right Motor Dir Pin", PinState.LOW)

    /* == Front Left Motor == */
    val motorFrontLeftPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_FRONT_LEFT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorFrontLeftPwmPin = gpio.provisionPwmOutputPin(motorFrontLeftPin)
    //motorFrontLeftPwmPin.setPwmRange(100)

    motorFrontLeftEnablerPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_LEFT_BRIDGE_ENABLER_PIN,
        "Front Left Motor En Pin", PinState.HIGH)

    motorFrontLeftDirPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_MULTIPLEXER_SELECTOR_1,
        "Front Left Motor Dir Pin", PinState.LOW)


    /* = Rear Motors = */

    /* == Rear Right Motor == */
    val motorRearRightPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_REAR_RIGHT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorRearRightPwmPin = gpio.provisionPwmOutputPin(motorRearRightPin)
    //motorRearRightPwmPin.setPwmRange(100)

    motorRearRightEnablerPin = gpio.provisionDigitalOutputPin(
        provider, REAR_RIGHT_BRIDGE_ENABLER_PIN,
        "Rear Right Motor En Pin", PinState.HIGH)

    motorRearRightDirPin = gpio.provisionDigitalOutputPin(
        provider, REAR_MULTIPLEXER_SELECTOR_2,
        "Rear Right Motor Dir Pin", PinState.LOW)

    /* == Rear Left Motor == */
    val motorRearLeftPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_REAR_LEFT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorRearLeftPwmPin = gpio.provisionPwmOutputPin(motorRearLeftPin)
    //motorRearLeftPwmPin.setPwmRange(100)

    motorRearLeftEnablerPin = gpio.provisionDigitalOutputPin(
        provider, REAR_LEFT_BRIDGE_ENABLER_PIN,
        "Rear Left Motor En Pin", PinState.HIGH)

    motorRearLeftDirPin = gpio.provisionDigitalOutputPin(
        provider, REAR_MULTIPLEXER_SELECTOR_1,
        "Rear Left Motor Dir Pin", PinState.LOW)
}