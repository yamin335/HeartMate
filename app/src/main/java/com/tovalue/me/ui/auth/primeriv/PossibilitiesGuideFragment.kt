package com.tovalue.me.ui.auth.primeriv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPossibilitiesGuideBinding
import com.tovalue.me.model.TvmReport
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.util.livedata.EventObserver

class PossibilitiesGuideFragment : Fragment() {
	private var _binding: FragmentPossibilitiesGuideBinding? = null
	private val binding get() = _binding!!
	private val authViewModel: AuthViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPossibilitiesGuideBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		observeEvents()
	}
	
	private fun observeEvents() {
		authViewModel.guideState.observe(viewLifecycleOwner) {
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
		with(binding.possibilityRv) {
			adapter = GuideAdapter(data.possibilityTab)
			addItemDecoration(
				DividerItemDecoration(
					requireContext(),
					LinearLayoutManager.VERTICAL
				)
			)
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}