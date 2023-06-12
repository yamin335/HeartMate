package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentDateNightCatalogIdeaDetailsBinding
import com.tovalue.me.databinding.FragmentDateNightSeeAllPromotedBinding

class DateNightSeeAllPromotedFragment : Fragment() {


    private var _binding: FragmentDateNightSeeAllPromotedBinding? = null
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
        _binding = FragmentDateNightSeeAllPromotedBinding.inflate(inflater,container,false)
        return binding.root
    }


}