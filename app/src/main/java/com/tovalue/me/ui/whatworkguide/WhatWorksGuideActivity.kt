package com.tovalue.me.ui.whatworkguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tovalue.me.R
import com.tovalue.me.adapter.WhatWorksListAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.ActivityWhatWorksGuideBinding
import com.tovalue.me.databinding.FragmentUpGradeToLevelBinding
import com.tovalue.me.model.whatworksguide.CardData
import com.tovalue.me.model.whatworksguide.WhatWorksGuideRespose
import com.tovalue.me.ui.catalog.viewmodel.UpGradeToLevelViewModel
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.pushalert.PushAlertViewModel
import com.tovalue.me.ui.whatworkguide.ViewModel.WhatWorksGuideViewModel
import com.tovalue.me.ui.whatworkguide.ViewModel.WhatWorksGuideViewModel.EventWhatWordsGuide
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver

class WhatWorksGuideActivity : AppCompatActivity() {

    private var _binding: ActivityWhatWorksGuideBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WhatWorksGuideViewModel by viewModels()
    private var adapter:WhatWorksListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWhatWorksGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { onBackPressed()
            finish()}

        fetchWhatWorksGuide()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.worksGuideResponse.observe(this,
            EventObserver {
                when (it) {
                    is EventWhatWordsGuide.GuideData -> {

                        var cardList: MutableList<CardData> = ArrayList()

                        cardList.add(it.response.guides!![0].inventoryCard!!)
                        cardList.add(it.response.guides!![0].invitationsCard!!)
                        cardList.add(it.response.guides!![0].levelingCard!!)
                        cardList.add(it.response.guides!![0].experienceCard!!)

                         if (cardList.size >0){

                             adapter= WhatWorksListAdapter(this@WhatWorksGuideActivity,cardList)
                             binding.rvCardData.layoutManager=GridLayoutManager(this,2,
                                 RecyclerView.VERTICAL,false)
                             binding.rvCardData.adapter=adapter

                         }
                        DialogUtils.hideDialog()
                    }
                    is EventWhatWordsGuide.Error -> {
                        DialogUtils.hideDialog()
                        Log.d("DashboardActivity", it.errorMsg)
                    }
                }
            })
    }


    private fun fetchWhatWorksGuide() {
        DialogUtils.showDialog(this@WhatWorksGuideActivity, false)
        viewModel.getWhatWorksData()
    }
}