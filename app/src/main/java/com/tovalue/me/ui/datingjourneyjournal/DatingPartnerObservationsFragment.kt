package com.tovalue.me.ui.datingjourneyjournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.R
import com.tovalue.me.adapter.DateJournalObservationAdapter
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeaDetailsBinding
import com.tovalue.me.databinding.FragmentDatingPartnerObservationsBinding


class DatingPartnerObservationsFragment : Fragment() {

    private var _binding: FragmentDatingPartnerObservationsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatingPartnerObservationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
    }

    private fun setRv() {


        binding.observationRV.apply {
            adapter = DateJournalObservationAdapter(requireContext())
            hasFixedSize()
        }

    }


}