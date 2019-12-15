import com.pi4j.component.servo.impl.RPIServoBlasterProvider


// INSTALL: It looks like I just cloned the repo, cd inside user folder and only run "make install" to autostart
// I may need to run sudo ./servod as well.
// The Holy Grail: https://github.com/richardghirst/PiBits/tree/master/ServoBlaster
fun main(args: Array<String>){

    // start servo blaster
    //Runtime.getRuntime().exec("sudo ~/PiBits/ServoBlaster/user/servod --p1pins=7")
    //Thread.sleep(2000)



    // Set raw value for this servo driver - 50 to 250

    // Example 1.
    /*val servoProvider = RPIServoBlasterProvider()
    val servo0 = servoProvider.getServoDriver(servoProvider.definedServoPins[5])

    println("Go to 150") //middle
    servo0.servoPulseWidth = 150
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(1550)
    println("Go to 65") //min value 50
    servo0.servoPulseWidth = 65
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(1550)
    println("Go to 235") //max value 250
    servo0.servoPulseWidth = 235
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(1550)
    println("Go to 150")
    servo0.servoPulseWidth = 150
    println("Went to ${servo0.servoPulseWidth}")
    Thread.sleep(1550)*/
    //println("Go to 0") // wrong value
    //servo0.servoPulseWidth = 0
    //println("Went to ${servo0.servoPulseWidth}")
    //Thread.sleep(1550)

    // Example 2
    /*val out = PrintWriter(FileOutputStream("/dev/servoblaster"), true)
    println("Go to 65 again")
    out.println("5=65")
    out.flush()
    Thread.sleep(3000)
    println("Go to 235 again")
    out.println("5=235")
    out.flush()
    Thread.sleep(3000)
    println("Go to 150 again")
    out.println("5=150")
    out.flush()
    Thread.sleep(3000)
    //println("Go to 0 again")
    //out.println("5=0")
    //out.flush()
    //Thread.sleep(3000)*/

    // Example 3
    val servoId = 5
    val script = "/home/pi/ServoHardwareSteering.sh"
    val cmdMinPosition = "$script $servoId 65"
    val cmdMidPosition = "$script $servoId 150"
    val cmdMaxPosition = "$script $servoId 235"

    val runtime = Runtime.getRuntime()
    println(cmdMidPosition)
    runtime.exec(cmdMidPosition)//.waitFor()
    Thread.sleep(1550)
    println(cmdMinPosition)
    runtime.exec(cmdMinPosition)//.waitFor()
    Thread.sleep(1550)
    println(cmdMaxPosition)
    runtime.exec(cmdMaxPosition)//.waitFor()
    Thread.sleep(1550)
    println(cmdMidPosition)
    runtime.exec(cmdMidPosition)//.waitFor()
    Thread.sleep(1550)

    // kill servo blaster
    //Runtime.getRuntime().exec("killall servod")
    //Thread.sleep(2000)
}