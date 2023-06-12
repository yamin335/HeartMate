package com.tovalue.me.ui.auth.primerv

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentPrimerFiveBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.util.Constants.PRIMERX_STAGE
import com.tovalue.me.util.Constants.TVMNUMBER_STAGE
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PrimerFiveFragment : Fragment() {
	private var _binding: FragmentPrimerFiveBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPrimerFiveBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.startBtn.setOnClickListener {
		
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}