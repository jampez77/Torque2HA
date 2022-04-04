package uk.co.jampez.torque2ha


import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import org.prowl.torque.remote.ITorqueService
import uk.co.jampez.torque2ha.utils.PID
import uk.co.jampez.torque2ha.utils.PIDComparator
import uk.co.jampez.torque2ha.utils.PIDRecyclerAdapter
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlinx.android.synthetic.main.main.*
import org.apache.commons.net.telnet.TelnetClient
import java.io.PrintStream

/**
 * This is a very rough, sample plugin that displays a list of all the currently available
 * sensors in Torque (that your ECU supports).
 *
 * Use the "Display Test Mode" in the main Torque app settings to simulate values.
 *
 * The plugin name and icon used in Torque are that of the apk icon and name.
 *
 * Note the intent-filter entries in the manifest - one for this code (an actual activity based plugin)
 * and one for allowing extensions of PID lists for manufacturer specific ECUs. The PID lists are a simple
 * file based resource you can throw together in a normal text-editor.
 *
 * Have fun!
 *
 * @author Ian Hawkins http://torque-bhp.com/
 */
class PluginActivity : AppCompatActivity() {
    lateinit var torqueService: ITorqueService
    private var handler: Handler? = null
    private lateinit var nf: NumberFormat
    private val pids: ArrayList<PID> = ArrayList()
    private var listViewArrayAdapter: PIDRecyclerAdapter? = null
    private var updateTimer: Timer? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        instance = this

        // Max of 2 digits for readings.
        nf = NumberFormat.getInstance()
        nf.maximumFractionDigits = 2

        handler = Handler(Looper.getMainLooper())
    }

    override fun onResume() {
        super.onResume()
        if (updateTimer != null) updateTimer!!.cancel()
        updateTimer = Timer()
        updateTimer!!.schedule(object : TimerTask() {
            override fun run() {
                refreshListItems()
            }
        }, 1000, 1000)
        textview!!.visibility = GONE
        // Bind to the torque service
        val intent = Intent()
        intent.setClassName("org.prowl.torque", "org.prowl.torque.remote.TorqueService")
        val successfulBind = bindService(intent, connection, 0)
        if (successfulBind) {
            setupList()
            // Not really anything to do here.  Once you have bound to the service, you can start calling
            // methods on torqueService.someMethod()  - look at the aidl file for more info on the calls
        } else {
            textview!!.visibility = VISIBLE
            textview!!.text = getString(R.string.unable_to_connect_to_torque)
        }
    }

    fun telnetCommands(){
        Thread{
            val telnet = TelnetClient()
            telnet.connect("10.0.2.2", 5554)
            val out = PrintStream(telnet.outputStream)
            out.println("auth yavQqdsKRDOl1Z6W")
            out.println("sensor set acceleration 0:0:0")
            Thread.sleep(1000)
            out.println("sensor set acceleration 99:99:99")
            Thread.sleep(1000)
            out.println("sensor set acceleration 0:0:0")
            out.flush()
            telnet.disconnect()
        }.start()
    }

    override fun onPause() {
        super.onPause()
        if (updateTimer != null) updateTimer!!.cancel()
        unbindService(connection)
    }

    /**
     * Create the list that we are going to show to the user containing PIDS from torque
     */
    fun setupList() {
        if (listViewArrayAdapter == null)
        listViewArrayAdapter = PIDRecyclerAdapter(this, pids)

        val gridLayoutManager = GridLayoutManager(this, 1, VERTICAL, false)
        recycler_view.layoutManager = gridLayoutManager
        recycler_view.setHasFixedSize(true)

        recycler_view.adapter = listViewArrayAdapter
        listViewArrayAdapter!!.setItemClickListener {
            val pid = pids[it.tag as Int]
            toast(pid.fullName, this)
        }

    }

    fun refreshListItems() {
        if (listViewArrayAdapter == null) return
        try {
            val mPids = torqueService.listAllPIDs()
            val pidInfo = torqueService.getPIDInformation(mPids)
            pids.clear()
            for (i in mPids.indices) {
                val info = pidInfo[i].split(",").toTypedArray()
                val newPid = PID(mPids[i])
                newPid.fullName = info[0]
                newPid.shortName = info[1]
                newPid.unit = info[2]
                newPid.isUserPid = false
                pids.add(newPid)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        pids.distinct()
        Collections.sort(pids, PIDComparator())
        handler!!.post {
            listViewArrayAdapter!!.setPids(pids)
        }
    }

    private fun toast(message: String?, c: Context?) {
        handler!!.post {
            try {
                // try { Thread.sleep(100); } catch(InterruptedException e) { }
                Toast.makeText(c, message, Toast.LENGTH_LONG).show()
            } catch (e: Throwable) {
                // Do nothing
            }
        }
    }

    /**
     * Quick popup message
     *
     */
    fun popupMessage(title: String?, message: String?, finishOnClose: Boolean) {
        handler!!.post {
            try {
                val alertDialog = AlertDialog.Builder(this@PluginActivity).create()
                alertDialog.setButton(BUTTON_POSITIVE, "OK") { _, _ ->
                    alertDialog.dismiss()
                    if (finishOnClose) {
                        finish()
                    }
                }
                val svMessage = ScrollView(this@PluginActivity)
                val tvMessage = TextView(this@PluginActivity)
                val spanText = SpannableString(message)
                Linkify.addLinks(spanText, Linkify.ALL)
                tvMessage.text = spanText
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                tvMessage.movementMethod = LinkMovementMethod.getInstance()
                svMessage.setPadding(14, 2, 10, 12)
                svMessage.addView(tvMessage)
                alertDialog.setTitle(title)
                alertDialog.setView(svMessage)
                alertDialog.show()
            } catch (e: Throwable) {
               e.printStackTrace()
            }
        }
    }

    /**
     * Bits of service code. You usually won't need to change this.
     */
    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, service: IBinder) {
            torqueService = ITorqueService.Stub.asInterface(service)
            try {
                if (torqueService.version < 19) {
                    popupMessage("Incorrect version", "You are using an old version of Torque with this plugin.\n\nThe plugin needs the latest version of Torque to run correctly.\n\nPlease upgrade to the latest version of Torque from Google Play", true)
                    return
                }
            } catch (e: RemoteException) {
            }
            setupList()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            //torqueService.des
        }
    }

    companion object {
        lateinit var instance: PluginActivity
    }
}
