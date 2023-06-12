package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.tovalue.me.R
import com.tovalue.me.databinding.DateNightCatalogEventOfferDialogBinding
import com.tovalue.me.databinding.DateNightCoverDialogBinding
import com.tovalue.me.model.datingJourney.Journey


class DataNightCatalogCoverDialog constructor(val datingData: Journey?, val bgUrlLink:String,val callback:()->Unit) : DialogFragment() {

    private lateinit var vb: DateNightCoverDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        isCancelable = false

        vb = DateNightCoverDialogBinding.inflate(inflater,container,false)
        return vb.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setClickListener()
    }

    private fun setClickListener() {

        vb.backBtn.setOnClickListener { dismiss() }
        vb.guideMeBtn.setOnClickListener{
            callback.invoke()
            dismiss()
        }

    }

    private fun setView() {


        datingData?.let {
            vb.firstName.text = it.first_name
            Glide.with(this).load(bgUrlLink)
                .error(R.drawable.default_avatar)
                .into(vb.userImage)

        }



    }


}