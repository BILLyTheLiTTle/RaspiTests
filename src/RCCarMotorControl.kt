import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider
import com.pi4j.gpio.extension.mcp.MCP23S17Pin
import com.pi4j.gpio.extension.mcp.MCP23S17Pin.*
import com.pi4j.io.gpio.*
import com.pi4j.io.spi.SpiChannel
import com.pi4j.util.CommandArgumentParser

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
private lateinit var motorsFrontForwardDirPin: GpioPinDigitalOutput
private lateinit var motorFrontLeftPwmPin: GpioPinPwmOutput
private lateinit var motorsFrontBackwardDirPin: GpioPinDigitalOutput
private lateinit var motorRearRightPwmPin: GpioPinPwmOutput
private lateinit var motorsRearForwardDirPin: GpioPinDigitalOutput
private lateinit var motorRearLeftPwmPin: GpioPinPwmOutput
private lateinit var motorsRearBackwardDirPin: GpioPinDigitalOutput
private lateinit var motorFrontRightEnablerPin: GpioPinDigitalOutput
private lateinit var motorFrontLeftEnablerPin: GpioPinDigitalOutput
private lateinit var motorRearRightEnablerPin: GpioPinDigitalOutput
private lateinit var motorRearLeftEnablerPin: GpioPinDigitalOutput

private lateinit var gpio: GpioController
private lateinit var provider: MCP23S17GpioProvider

fun main(args: Array<String>) {

    // create gpio controller
    gpio = GpioFactory.getInstance()

    println("<--Pi4J--> RCCar motor control ... started.")

    provider = MCP23S17GpioProvider(MCP23S17GpioProvider.ADDRESS_0, SpiChannel.CS0)
    println("MCP23S17 Found: ${provider != null}")

    initialize()

    val direction = 1 // 0 - Forward, 1 - Backward

    if(direction == 0) {
        motorsFrontForwardDirPin.high()
        motorsRearForwardDirPin.high()
    }
    else if (direction == 1) {
        motorsFrontBackwardDirPin.high()
        motorsRearBackwardDirPin.high()
    }
    else {
        motorsFrontForwardDirPin.low()
        motorsRearForwardDirPin.low()
        motorsFrontBackwardDirPin.low()
        motorsRearBackwardDirPin.low()
    }

    for (i in 1..3) {
        motorFrontLeftPwmPin.pwm = 0
        motorFrontRightPwmPin.pwm = 0
        motorRearLeftPwmPin.pwm = 0
        motorRearRightPwmPin.pwm = 0
        Thread.sleep(500)
        motorFrontLeftPwmPin.pwm = 100
        motorFrontRightPwmPin.pwm = 100
        motorRearLeftPwmPin.pwm = 100
        motorRearRightPwmPin.pwm = 100
        Thread.sleep(500)
        motorFrontLeftPwmPin.pwm = 0
        motorFrontRightPwmPin.pwm = 0
        motorRearLeftPwmPin.pwm = 0
        motorRearRightPwmPin.pwm = 0
    }

    motorsFrontForwardDirPin.low()
    motorsRearForwardDirPin.low()
    motorsFrontBackwardDirPin.low()
    motorsRearBackwardDirPin.low()

    //TODO Apply throttle
    /*applyPinValues(
        0, false, false,
        0, false, false,
        0, false, false,
        0, false, false
    )*/

    gpio.apply {
        shutdown()
        unprovisionPin(motorFrontRightPwmPin)
        unprovisionPin(motorsFrontForwardDirPin)
        //unprovisionPin(motorFrontRightEnablerPin)

        unprovisionPin(motorFrontLeftPwmPin)
        unprovisionPin(motorsFrontBackwardDirPin)
        //unprovisionPin(motorFrontLeftEnablerPin)

        unprovisionPin(motorRearRightPwmPin)
        unprovisionPin(motorsRearForwardDirPin)
        //unprovisionPin(motorRearRightEnablerPin)

        unprovisionPin(motorRearLeftPwmPin)
        unprovisionPin(motorsRearBackwardDirPin)
        //unprovisionPin(motorRearLeftEnablerPin)
    }
}

/*private fun applyPinValues(
    motorFrontRightPwm: Int? = null, motorFrontRightMovingForward: Boolean? = null, motorFrontRightEnable: Boolean? = null,
    motorFrontLeftPwm: Int? = null, motorFrontLeftMovingForward: Boolean? = null, motorFrontLeftEnable: Boolean? = null,
    motorRearRightPwm: Int? = null, motorRearRightMovingForward: Boolean? = null, motorRearRightEnable: Boolean? = null,
    motorRearLeftPwm: Int? = null, motorRearLeftMovingForward: Boolean? = null, motorRearLeftEnable: Boolean? = null){

    motorFrontRightPwm?.let { motorFrontRightPwmPin.pwm = it }
    motorFrontRightMovingForward?.let { motorFrontRightDirPin.setState(it) }
    motorFrontRightEnable?.let { motorFrontRightEnablerPin.setState(it) }
    motorFrontLeftPwm?.let { motorFrontLeftPwmPin.pwm = it }
    motorFrontLeftMovingForward?.let { motorFrontLeftDirPin.setState(it) }
    motorFrontLeftEnable?.let { motorFrontLeftEnablerPin.setState(it) }
    motorRearRightPwm?.let { motorRearRightPwmPin.pwm = it }
    motorRearRightMovingForward?.let { motorRearRightDirPin.setState(it) }
    motorRearRightEnable?.let { motorRearRightEnablerPin.setState(it) }
    motorRearLeftPwm?.let { motorRearLeftPwmPin.pwm = it }
    motorRearLeftMovingForward?.let { motorRearLeftDirPin.setState(it) }
    motorRearLeftEnable?.let { motorRearLeftEnablerPin.setState(it) }
}*/

private fun initialize() {

    /* == Front Motors Direction == */
    motorsFrontBackwardDirPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_MULTIPLEXER_SELECTOR_2,
        "Front Motors Backward Dir Pin", PinState.LOW)

    motorsFrontForwardDirPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_MULTIPLEXER_SELECTOR_1,
        "Front Motors Forward Dir Pin", PinState.LOW)

    /* == Front Right Motor == */
    val motorFrontRightPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_FRONT_RIGHT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorFrontRightPwmPin = gpio.provisionPwmOutputPin(motorFrontRightPin)
    motorFrontRightPwmPin.setPwmRange(100)

    motorFrontRightEnablerPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_RIGHT_BRIDGE_ENABLER_PIN,
        "Front Right Motor En Pin", PinState.HIGH)

    /* == Front Left Motor == */
    val motorFrontLeftPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_FRONT_LEFT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorFrontLeftPwmPin = gpio.provisionPwmOutputPin(motorFrontLeftPin)
    motorFrontLeftPwmPin.setPwmRange(100)

    motorFrontLeftEnablerPin = gpio.provisionDigitalOutputPin(
        provider, FRONT_LEFT_BRIDGE_ENABLER_PIN,
        "Front Right Motor En Pin", PinState.HIGH)

    /* == Rear Motors Direction == */
    motorsRearBackwardDirPin = gpio.provisionDigitalOutputPin(
        provider, REAR_MULTIPLEXER_SELECTOR_2,
        "Rear Motors Backward Dir Pin", PinState.LOW)

    motorsRearForwardDirPin = gpio.provisionDigitalOutputPin(
        provider, REAR_MULTIPLEXER_SELECTOR_1,
        "Rear Motors Forward Dir Pin", PinState.LOW)

    /* == Rear Right Motor == */
    val motorRearRightPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_REAR_RIGHT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorRearRightPwmPin = gpio.provisionPwmOutputPin(motorRearRightPin)
    motorRearRightPwmPin.setPwmRange(100)

    motorRearRightEnablerPin = gpio.provisionDigitalOutputPin(
        provider, REAR_RIGHT_BRIDGE_ENABLER_PIN,
        "Rear Right Motor En Pin", PinState.HIGH)

    /* == Rear Left Motor == */
    val motorRearLeftPin = CommandArgumentParser.getPin(
        RaspiPin::class.java, // pin provider class to obtain pin instance from
        MOTOR_REAR_LEFT_PWM_PIN, // default pin if no pin argument found
        null
    )
    motorRearLeftPwmPin = gpio.provisionPwmOutputPin(motorRearLeftPin)
    motorRearLeftPwmPin.setPwmRange(100)

    motorRearLeftEnablerPin = gpio.provisionDigitalOutputPin(
        provider, REAR_LEFT_BRIDGE_ENABLER_PIN,
        "Rear Left Motor En Pin", PinState.HIGH)
}