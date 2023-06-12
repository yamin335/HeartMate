package com.tovalue.me.ui.datingjourneycalandar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.databinding.FragmentDatingJourneyCalanderBinding


class DatingJourneyCalanderFragment : Fragment() {


    private var _binding: FragmentDatingJourneyCalanderBinding? = null
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
        _binding = FragmentDatingJourneyCalanderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

}