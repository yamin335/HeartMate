package com.tovalue.me.ui.whatworkguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tovalue.me.adapter.WhatWorksDataListAdapter
import com.tovalue.me.databinding.ActivityWorkWordsDataBinding
import com.tovalue.me.model.whatworksguide.WhatWorksGuideDataRespose
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.ui.whatworkguide.ViewModel.VoteViewModel
import com.tovalue.me.ui.whatworkguide.ViewModel.WhatWorksGuideDataViewModel
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver

class WorkWordsDataActivity : AppCompatActivity() {
    private var _binding: ActivityWorkWordsDataBinding? = null
    private val binding get() = _binding!!
    private var guideId: Int = 0
    private val viewModel: WhatWorksGuideDataViewModel by viewModels()
    private val vote_vieweModel: VoteViewModel by viewModels()
    private var adapter: WhatWorksDataListAdapter? = null
    private val tipList: MutableList<WhatWorksGuideDataRespose.TipData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkWordsDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            guideId = intent.getIntExtra("guideID", 0)
        }

        init()


        fetchWhatWorksGuide(guideId)
        setUpObserver()
        setUpVoteObserver()
    }

    private fun init() {
        if (guideId == 1) {
            binding.tvBottomBtn.setText("Update Inventory of My Life")
        } else if (guideId == 2) {
            binding.tvBottomBtn.setText("Start a dating journey today")
        } else if (guideId == 3) {
            binding.tvBottomBtn.setText("Update Dating Journal")
        } else if (guideId == 4) {
            binding.tvBottomBtn.setText("Review my Date Night Catalog")
        }

        binding.tvBottomBtn.setOnClickListener {
            var intent: Intent = Intent(this@WorkWordsDataActivity, DashboardActivity::class.java)
            if (guideId == 1) {
                intent.putExtra("isFromGuideActivity", true)
                intent.putExtra("guideId", guideId)
                startActivity(intent)
                finishAffinity()
            } else if (guideId == 2) {
                intent.putExtra("isFromGuideActivity", true)
                intent.putExtra("guideId", guideId)
                startActivity(intent)
                finishAffinity()
            } else if (guideId == 3) {
                intent.putExtra("isFromGuideActivity", true)
                intent.putExtra("guideId", guideId)
                startActivity(intent)
                finishAffinity()
            } else if (guideId == 4) {
                intent.putExtra("isFromGuideActivity", true)
                intent.putExtra("guideId", guideId)
                startActivity(intent)
                finishAffinity()

            }

        }

        binding.llYes.setOnClickListener {
            sendVote(guideId, 1)
        }

        binding.llNo.setOnClickListener {
            sendVote(guideId, 2)
        }


        binding.backBtn.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun setUpObserver() {
        tipList.clear()
        viewModel.worksGuideDataResponse.observe(this,
            EventObserver {
                when (it) {
                    is WhatWorksGuideDataViewModel.EventWhatWordsGuideData.GuideData -> {


                        binding.tvHeader.text = it.response.guide!!.get(0)?.headerTitle
                        binding.tvHeaderName.text = it.response.guide!!.get(0)?.headerTitle
                        binding.tvExcerpt.text = it.response.guide!!.get(0)?.summary
                        Glide.with(this@WorkWordsDataActivity)
                            .load(it.response.guide!!.get(0)?.headerImage)
                            .into(binding.ivHeaderImage)


                        tipList.add(it.response.guide!!.get(0)?.tipOne!!)
                        tipList.add(it.response.guide!!.get(0)?.tipTwo!!)
                        tipList.add(it.response.guide!!.get(0)?.tipThree!!)
                        if(guideId==4) {
                            tipList.add(it.response.guide!!.get(0)?.tipFour!!)
                        }

                        if (tipList.size > 0) {

                            adapter = WhatWorksDataListAdapter(this@WorkWordsDataActivity, tipList)
                            binding.rvCardData.layoutManager = LinearLayoutManager(
                                this, RecyclerView.VERTICAL, false
                            )
                            binding.rvCardData.adapter = adapter

                        }
                        DialogUtils.hideDialog()
                        binding.llHelpful.visibility= View.VISIBLE
                    }
                    is WhatWorksGuideDataViewModel.EventWhatWordsGuideData.Error -> {
                        DialogUtils.hideDialog()
                        binding.llHelpful.visibility= View.GONE
                        Log.d("DashboardActivity", it.errorMsg)
                    }
                }
            })
    }

    private fun setUpVoteObserver() {
        tipList.clear()
        vote_vieweModel.sendVoteResponse.observe(this,
            EventObserver {
                when (it) {
                    is VoteViewModel.EventVote.voteData -> {

                        Toast.makeText(
                            this@WorkWordsDataActivity,
                            "Successfully submitted",
                            Toast.LENGTH_SHORT
                        ).show()
                        DialogUtils.hideDialog()
                    }
                    is VoteViewModel.EventVote.Error -> {
                        DialogUtils.hideDialog()
                        Log.d("DashboardActivity", it.errorMsg)
                    }
                }
            })
    }

    private fun fetchWhatWorksGuide(guideId: Int) {
        DialogUtils.showDialog(this@WorkWordsDataActivity, false)
        viewModel.getWhatWorksData(guideId)
    }

    private fun sendVote(guideId: Int, vote: Int) {
        DialogUtils.showDialog(this@WorkWordsDataActivity, false)
        vote_vieweModel.sendVote(guideId, vote)
    }

}