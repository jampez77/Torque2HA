package uk.co.jampez.torque2ha.utils

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uk.co.jampez.torque2ha.PluginActivity
import uk.co.jampez.torque2ha.R
import java.text.NumberFormat

class PIDRecyclerAdapter(private val context: Context, private var pids: ArrayList<PID>) : RecyclerView.Adapter<PIDRecyclerAdapter.ViewHolder>() {

    private val nf = NumberFormat.getInstance()

    private var onItemClickListener: View.OnClickListener? = null
    fun setItemClickListener(onItemClickListener: View.OnClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return pids.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.valuelayout, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val mainLayout: RelativeLayout = iv.findViewById(R.id.main_layout)
        val firstLine: TextView = iv.findViewById(R.id.afirstLine)
        val secondLine: TextView = iv.findViewById(R.id.asecondLine)
        val thirdLine: TextView = iv.findViewById(R.id.athirdLine)
        val icon: ImageView = iv.findViewById(R.id.aicon)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*
         * Bind data to layout
         * */
        try {
            val pid = pids[position]
            holder.mainLayout.setOnClickListener(onItemClickListener)
            holder.mainLayout.tag = position

            var pidName = pid.fullName
            if (pidName.isNullOrEmpty()) {
                pidName = "[Unnamed]"
            }

            val latestVal: String
            val torqueService = PluginActivity.instance.torqueService

            val mPids = torqueService.listActivePIDs()
            val sPids = torqueService.listECUSupportedPIDs()
            val result = torqueService.getPIDValues(arrayOf(pid.pid))[0]

            var unit: String? = ""
            if (pid.unit != null) unit = pid.unit
            val color: Int
            val icon: Bitmap? = null

            when {
                containsPID(mPids, pid.pid) -> {
                    color = ContextCompat.getColor(context, R.color.dk_green)
                    latestVal = "Latest raw value: " + nf.format(result as Number) + unit
                }
                containsPID(sPids, pid.pid) -> {
                    color = ContextCompat.getColor(context, R.color.dk_green_b)
                    latestVal = "Waiting for data"
                }
                else -> {
                    color = ContextCompat.getColor(context, R.color.dk_red)
                    latestVal = "No data received"
                }
            }
            val left = pid.pid!!.substring(0, pid.pid!!.indexOf(","))

            holder.firstLine.text = String.format(context.getString(R.string.pid_name), pidName, left)

            if(pid.equation != null)
                holder.secondLine.text = pid.equation
            else
                holder.secondLine.visibility = GONE

            holder.thirdLine.text = latestVal

            holder.mainLayout.setBackgroundColor(color)
            holder.icon.setImageBitmap(icon)


        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    fun setPids(pids: ArrayList<PID>){
        this.pids = pids
        notifyDataSetChanged()
    }

    private fun containsPID(pids: Array<String>, pid: String?): Boolean {
        for (p in pids) {
            if (pid == p) return true
        }
        return false
    }
}
