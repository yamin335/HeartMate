package com.tovalue.me.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tovalue.me.R
import com.tovalue.me.common.applyTint
import com.tovalue.me.databinding.DatingJourneyCardBinding
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.model.datingJourney.Partner
import com.tovalue.me.ui.catalog.UpGradeToLevelFragment
import com.tovalue.me.util.Constants
import com.tovalue.me.util.Utils

enum class DatingCardClickAction {
    CANCEL,
    CATALOG,
    UPGRADE,
    DATING_JOURNEY
}

class DatingJourneyCardAdapter constructor(
    val context: Context, private val datingJourneyList: ArrayList<Journey>,
    private val callback: (DatingCardClickAction, Journey?) -> Unit
) : RecyclerView.Adapter<DatingJourneyCardAdapter.DatingJourneyCardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingJourneyCardViewHolder {

        val vb =
            DatingJourneyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val devicewidth = (Utils.getScreenWidth(parent.context as Activity) * 0.8).toInt()
        vb.root.getLayoutParams().width = devicewidth
        return DatingJourneyCardViewHolder(vb)
    }

    override fun onBindViewHolder(holder: DatingJourneyCardViewHolder, position: Int) {

        val datingJourney = datingJourneyList[position]


        Glide.with(context).load(datingJourney.avatar)
            .error(R.drawable.default_avatar)
            .into(holder.vb.icon)


        holder.vb.firstName.setText(datingJourney.first_name)
        holder.vb.deniesTXT.text = datingJourney.first_name

        if (datingJourney.last_name.isNotEmpty()) {
            holder.vb.lastName.text = datingJourney.last_name
        }


        datingJourney.our_status?.let {
            holder.vb.levelCount.text = it.level
            holder.vb.levelStatus.text = it.title
            
            // hollow heart icon if level = Level 1
            // solid hear icon if leve = Level 2 || Level 3
            // [@refactor same size icon]
            if (it.level == "Level 2" || it.level == "Level 3")
                holder.vb.upGradeBtn.setImageResource(R.drawable.ic_heart_red)
            else {
                holder.vb.upGradeBtn.setImageResource(R.drawable.double_like)
            }
        }


        holder.vb.valueMeScore.text =
            context.resources.getString(R.string.toValueMe, datingJourney.value_me_score.toString())

        datingJourney.discoveries?.let {

            it.user.let {

                holder.vb.meLikeCount.text = it!!.me.toString()
            }

        }

        holder.vb.exitDatingJourneyBtn.setOnClickListener {
            callback.invoke(DatingCardClickAction.CANCEL, null)
        }
        holder.vb.upGradeBtn.setOnClickListener {
            callback.invoke(DatingCardClickAction.UPGRADE, datingJourney)
        }
        holder.vb.datanightCatalogBtn.setOnClickListener {
            callback.invoke(DatingCardClickAction.CATALOG,datingJourney)
        }

        datingJourney.discoveries?.let {
            val empMapType =
                object : TypeToken<HashMap<String, Int>>() {}.type
            val data: HashMap<String, Int> = Gson().fromJson(it.partner, empMapType)

            data.keys.forEach{
                holder.vb.meDeniesCount.text = data.get(it).toString()
            }
        }

        holder.vb.root.setOnClickListener {
            callback.invoke(DatingCardClickAction.DATING_JOURNEY,datingJourney)
        }


    }


    override fun getItemCount(): Int = datingJourneyList.size

    inner class DatingJourneyCardViewHolder constructor(val vb: DatingJourneyCardBinding) :
        RecyclerView.ViewHolder(vb.root)

}