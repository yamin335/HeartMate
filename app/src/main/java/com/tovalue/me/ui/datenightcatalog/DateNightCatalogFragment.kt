package com.tovalue.me.ui.datenightcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tovalue.me.R
import com.tovalue.me.databinding.FragmentDateNightCatalogBinding
import com.tovalue.me.databinding.FragmentMoodRingHistoryBinding
import com.tovalue.me.model.moodRingModels.MoodRingHistory
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primervi.PrimerSixFragment
import com.tovalue.me.ui.dashboard.DashboardViewModel
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.util.*
import com.tovalue.me.util.Constants.PRIMERVI_STAGE
import com.tovalue.me.util.extensions.animateVisibility
import com.tovalue.me.util.extensions.replaceFragmentSafely
import java.util.*

class DateNightCatalogFragment : Fragment() {
	private var _binding: FragmentDateNightCatalogBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by viewModels()
	
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentDateNightCatalogBinding.inflate(layoutInflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnBack.setOnClickListener {
			if (authViewModel.isUserLoggedIn())
				requireActivity().onBackPressed()
			else
				goToPrimerSixFrag()
		}

		binding.btnGuideMe.setOnClickListener {
			startActivity(NavigationActivity.createIntent(requireActivity(), Constants.DATE_NIGHT_CATALOG_IDEAS_LIST))
		}
	}
	
	private fun goToPrimerSixFrag() {
		authViewModel.setBookMarkProgress(PRIMERVI_STAGE)
		replaceFragmentSafely(
			PrimerSixFragment(),
			containerViewId = R.id.host_root,
			addToStack = false
		)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}