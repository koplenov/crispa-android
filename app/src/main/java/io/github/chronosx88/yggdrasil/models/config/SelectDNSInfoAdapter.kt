package io.github.chronosx88.yggdrasil.models.config

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import io.github.chronosx88.yggdrasil.R
import io.github.chronosx88.yggdrasil.models.DNSInfo

class SelectDNSInfoListAdapter(
    context: Context,
    allDNS: List<DNSInfo>,
    currentDNS: MutableSet<DNSInfo>
) : ArrayAdapter<DNSInfo?> (context, 0, allDNS) {

    private val mContext: Context = context
    private var allDNS: MutableList<DNSInfo> = allDNS as MutableList<DNSInfo>
    private var currentDNS: MutableSet<DNSInfo> = currentDNS

    override fun getItem(position: Int): DNSInfo? {
        return allDNS[position]
    }

    override fun getCount(): Int {
        return allDNS.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var dnsInfoHolder = DNSInfoHolder()
        var listItem: View? = convertView
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.host_list_item_edit, parent, false)
            dnsInfoHolder.checkbox = listItem.findViewById(R.id.checkbox) as CheckBox
            dnsInfoHolder.countryFlag = listItem.findViewById(R.id.countryFlag) as ImageView
            dnsInfoHolder.dnsInfoText = listItem.findViewById(R.id.hostInfoText) as TextView
            dnsInfoHolder.ping = listItem.findViewById(R.id.frame) as TextView
            listItem.tag = dnsInfoHolder
        } else {
            dnsInfoHolder = listItem.tag as DNSInfoHolder
        }
        val currentDNS = allDNS[position]
        dnsInfoHolder.countryFlag.setImageResource(currentDNS.getCountry(mContext)!!.flagID)
        val dnsId = currentDNS.toString()
        if(currentDNS.ping == Int.MAX_VALUE){
            dnsInfoHolder.dnsInfoText.text = dnsId
            dnsInfoHolder.ping.text=""
            dnsInfoHolder.dnsInfoText.setTextColor(Color.GRAY)
        } else {
            dnsInfoHolder.dnsInfoText.text = dnsId
            dnsInfoHolder.ping.text = currentDNS.ping.toString() + " ms"
            dnsInfoHolder.dnsInfoText.setTextColor(Color.WHITE)
        }
        dnsInfoHolder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                if(!this.currentDNS.contains(currentDNS)){
                    this.currentDNS.add(currentDNS)
                }
            } else {
                if(this.currentDNS.contains(currentDNS)){
                    this.currentDNS.remove(currentDNS)
                }
            }
        }
        dnsInfoHolder.checkbox.isChecked = this.currentDNS.contains(currentDNS)
        return listItem!!
    }

    fun getSelectedDNS(): Set<DNSInfo> {
        return currentDNS
    }

    fun addItem(peerInfo: DNSInfo){
        allDNS.add(peerInfo)
    }

    fun sort(){
        allDNS = ArrayList(allDNS.sortedWith(compareBy { it.ping }))
        this.notifyDataSetChanged()
    }

    class DNSInfoHolder {
        lateinit var checkbox: CheckBox
        lateinit var countryFlag: ImageView
        lateinit var dnsInfoText: TextView
        lateinit var ping: TextView
    }
}