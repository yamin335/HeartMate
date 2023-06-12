package com.tovalue.me.ui.datingjourneyjournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.tovalue.me.R

import com.tovalue.me.adapter.ViewPager2Adapter
import com.tovalue.me.databinding.FragmentDatingPartnerDatingJournalBinding
import com.tovalue.me.model.datingJourney.Journey
import com.tovalue.me.util.Constants


class DatingPartnerDatingJournalFragment : Fragment() {


    private var _binding: FragmentDatingPartnerDatingJournalBinding? = null
    private val binding get() = _binding!!
    private var datingData: Journey? = null

    private var args: Bundle? = null

    private val pagerAdapter: ViewPager2Adapter by lazy {
        ViewPager2Adapter(
            requireActivity()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView() {
        args?.let {

            datingData = Gson().fromJson(
                it.getString(Constants.DATING_OBJECT),
                Journey::class.java
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatingPartnerDatingJournalBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        setTabViewPager()
    }


    /* Set on click Listener */
    private fun setOnClickListener() {
        binding.backBtn.setOnClickListener { requireActivity().onBackPressed() }
    }

    /* Set Tab view Pager */
    private fun setTabViewPager() {


        pagerAdapter.apply {
            add(ViewPager2Adapter.FragmentName.DATE_JOURNAL)
            add(ViewPager2Adapter.FragmentName.OBSERVATION_JOURNAL)

        }

        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = pagerAdapter
            offscreenPageLimit = 2
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.datingGuide_tab)[position]

            binding.viewPager.setCurrentItem(tab.position, true)

        }.attach()

    }

}