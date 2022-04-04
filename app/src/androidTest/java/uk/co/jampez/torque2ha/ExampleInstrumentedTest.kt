package uk.co.jampez.torque2ha

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.apache.commons.net.telnet.TelnetClient
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.PrintStream

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {


        // Context of the app under test.
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        Assert.assertEquals("uk.co.jampez.torque2ha", appContext.packageName)
    }

    @Test
    fun testAcceleration(){
        val scenario = launchActivity<PluginActivity>()


        scenario.onActivity {
            val appContext = ApplicationProvider.getApplicationContext<Context>()

            val sensorManager = appContext.getSystemService(SENSOR_SERVICE) as SensorManager

            val acceleration = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER).also { accelerationSensor ->
                sensorManager.registerListener(object : SensorEventListener {

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                        Log.d("SE accuracy", accuracy.toString())
                    }

                    override fun onSensorChanged(event: SensorEvent) {
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]

                        Log.d("SE Values", event.values.toString())
                    }

                },
                        accelerationSensor,
                        SensorManager.SENSOR_DELAY_FASTEST
                )
                Thread.sleep(5000)
                it.telnetCommands()
            }

            print(acceleration)





            Thread.sleep(50000)
        }



    }





}