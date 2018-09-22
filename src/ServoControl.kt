import com.pi4j.component.servo.impl.RPIServoBlasterProvider
import java.io.FileOutputStream
import java.io.PrintWriter

fun main(args: Array<String>){

    // start servo blaster
    //Runtime.getRuntime().exec("sudo ~/PiBits/ServoBlaster/user/servod --p1pins=7")
    //Thread.sleep(2000)

    val servoProvider = RPIServoBlasterProvider()

    val servo0 = servoProvider.getServoDriver(servoProvider.definedServoPins[0])

    // Set raw value for this servo driver - 50 to 250

    // Example 1
    println("Go to 50")
    servo0.servoPulseWidth = 50
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(3000)
    println("Go to 250")
    servo0.servoPulseWidth = 250
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(3000)
    println("Go to 150")
    servo0.servoPulseWidth = 150
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(3000)
    println("Go to 0")
    servo0.servoPulseWidth = 0
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(3000)

    // Example 2
    val out = PrintWriter(FileOutputStream("/dev/servoblaster"), true)
    println("Go to 50 again")
    out.println("0=50")
    out.flush()
    Thread.sleep(3000)
    println("Go to 250 again")
    out.println("0=250")
    out.flush()
    Thread.sleep(3000)
    println("Go to 150 again")
    out.println("0=150")
    out.flush()
    Thread.sleep(3000)
    println("Go to 0 again")
    out.println("0=0")
    out.flush()
    Thread.sleep(3000)

    // kill servo blaster
    //Runtime.getRuntime().exec("killall servod")
    //Thread.sleep(2000)
}