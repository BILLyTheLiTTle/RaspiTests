import com.pi4j.gpio.extension.pca.PCA9685GpioProvider
import com.pi4j.gpio.extension.pca.PCA9685Pin
import com.pi4j.io.gpio.*
import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CFactory
import com.pi4j.io.gpio.GpioPinPwmOutput
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.Pin
import java.math.BigDecimal


private lateinit var pca9685GpioProvider: PCA9685GpioProvider
private lateinit var myOutputs: Array<GpioPinPwmOutput>

fun main(args: Array<String>){

    Thread.sleep(3000)

    val bus = I2CFactory.getInstance(I2CBus.BUS_1)
    println("Connected to bus: ${bus != null}")
    pca9685GpioProvider = PCA9685GpioProvider(bus, 0x40)
    println("PCA9685 Found: ${pca9685GpioProvider != null}")
    //pca9685GpioProvider.reset()

    val gpio = GpioFactory.getInstance()
    println("General GPIO Found: ${gpio != null}")

    val pinInput1M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "Pin_Input_1_M1")
    val pinInput2M1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Pin_Input_2_M1")
    val pinInput1M2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Pin_Input_1_M2")
    val pinInput2M2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "Pin_Input_2_M2")

    pinInput1M1.high()
    pinInput2M1.low()
    pinInput1M2.high()
    pinInput2M2.low()

    myOutputs = provisionPwmOutputs(pca9685GpioProvider)

    showInfo("Pre-reset state")

    pca9685GpioProvider.reset()
    showInfo("Post-reset state")

    for (i in 1..4095 step 30) {
        pca9685GpioProvider.setPwm(PCA9685Pin.PWM_03, 0, i);
        pca9685GpioProvider.setPwm(PCA9685Pin.PWM_11, 0, i);
        showInfo("PWM 3 / 11 | Increase position set: $i")
        Thread.sleep(100)
    }
    for (i in 4095 downTo 1 step 30) {
        pca9685GpioProvider.setPwm(PCA9685Pin.PWM_03, 0, i);
        showInfo("PWM 3 | Decrease position set: $i")
        Thread.sleep(100)
    }
    for (i in 4095 downTo 1 step 30) {
        Thread.sleep(100)
        pca9685GpioProvider.setPwm(PCA9685Pin.PWM_11, 0, i);
        showInfo("PWM 11 | Decrease position set: $i")
    }

    Thread.sleep(300)

    gpio.shutdown()
}

private fun showInfo(customMsg: String){
println("===========$customMsg===========")
val onOffValuesMotor1 = pca9685GpioProvider.getPwmOnOffValues(myOutputs[0].pin)
val onOffValuesMotor2 = pca9685GpioProvider.getPwmOnOffValues(myOutputs[1].pin)
println(myOutputs[0].pin.name + " (" + myOutputs[0].name + "): ON value [" + onOffValuesMotor1[0] + "], OFF value [" + onOffValuesMotor1[1] + "]")
println(myOutputs[1].pin.name + " (" + myOutputs[1].name + "): ON value [" + onOffValuesMotor2[0] + "], OFF value [" + onOffValuesMotor2[1] + "]")
println("=====================================\n")
}

private fun provisionPwmOutputs(gpioProvider: PCA9685GpioProvider): Array<GpioPinPwmOutput> {
val gpio = GpioFactory.getInstance()
return arrayOf<GpioPinPwmOutput>(
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Motor1"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Motor2"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Always ON"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Servo pulse MIN"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Servo pulse NEUTRAL"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Servo pulse MAX"),
    gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "not used")
)
}

private fun checkForOverflow(position: Int): Int {
var result = position
if (position > PCA9685GpioProvider.PWM_STEPS - 1) {
    result = position - PCA9685GpioProvider.PWM_STEPS - 1
}
return result
}