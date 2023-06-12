package com.tovalue.me.ui.auth.primeriv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentInvestmentGuideBinding
import com.tovalue.me.model.TvmReport
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.livedata.EventObserver

class InvestmentGuideFragment : Fragment() {
	private var _binding: FragmentInvestmentGuideBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentInvestmentGuideBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		setUpViews()
		observeEvents()
	}
	
	private fun observeEvents() {
		authViewModel.guideState.observe(viewLifecycleOwner) {
			binding.loader.isVisible = false
			when(it) {
				is AuthViewModel.UiEventTvmReport.Report -> {
					setAdapterData(it.data)
				}
				is AuthViewModel.UiEventTvmReport.Error -> {
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	private fun setAdapterData(data: TvmReport) {
		with(binding.investmentRv) {
			adapter = GuideAdapter(data.dayNightTab)
			addItemDecoration(
				DividerItemDecoration(
					requireContext(),
					LinearLayoutManager.VERTICAL
				)
			)
		}
	}
	
	private fun setUpViews() {
		binding.loader.isVisible = true
		authViewModel.getDayNightCataLog()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}