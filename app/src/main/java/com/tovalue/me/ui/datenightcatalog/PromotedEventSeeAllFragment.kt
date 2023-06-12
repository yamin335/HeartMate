package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tovalue.me.adapter.DataNightWeekConst
import com.tovalue.me.adapter.PromotedEventAdapter
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeasBinding
import com.tovalue.me.databinding.PromotedEventSeeAllFragmentBinding
import com.tovalue.me.model.datenightcatalog.HolderData
import com.tovalue.me.model.datingJourney.Journey

import com.tovalue.me.util.Constants

class PromotedEventSeeAllFragment : Fragment() {

    private var _binding: PromotedEventSeeAllFragmentBinding? = null
    private val binding get() = _binding!!
    private var args: Bundle? = null
    private var events:List<HolderData.Event>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PromotedEventSeeAllFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args?.let {
            events = Gson().fromJson(it.getString(Constants.PROMOTED_EVENT_RESPONSE_LIST), HolderData.PromotedEventResponse::class.java).events
        }

        setClickListener()

        events?.let {
            setView(it)
        }

    }

    private fun setView(listEvent: List<HolderData.Event>) {
       binding.allPromotedRv.apply {
            adapter = PromotedEventAdapter(context,listEvent) { postion ->
            }
           layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }
    }

    /* Set Click :Listener*/
    private fun setClickListener() {

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }


}