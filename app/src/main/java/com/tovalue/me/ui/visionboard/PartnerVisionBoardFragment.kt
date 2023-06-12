package com.tovalue.me.ui.visionboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentPartnerVisionBoardBinding
import com.tovalue.me.ui.visionboard.response.PartnerVisionBoardModel
import com.tovalue.me.util.extensions.replaceFragmentSafely

class PartnerVisionBoardFragment : Fragment() {
	private var _binding: FragmentPartnerVisionBoardBinding? = null
	private val binding get() = _binding!!
	private val viewModel: VisionBoardViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentPartnerVisionBoardBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpView()
		setUpClickListener()
		setUpObserver()
	}
	
	private fun setUpView() {
		binding.loader.isVisible = true
		viewModel.getPartnerVisionBoard()
	}
	
	private fun setUpClickListener() {
		binding.backImg.setOnClickListener {
			requireActivity().onBackPressed()
		}
		binding.iceBtn.setOnClickListener {
			goToIceBreakerFrag()
		}
		binding.iceBtnOne.setOnClickListener {
			goToIceBreakerFrag()
		}
	}
	
	private fun goToIceBreakerFrag() {
		replaceFragmentSafely(
			IceBreakersFragment(),
			containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0, popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun setUpObserver() {
		viewModel.partnerBoard.observe(viewLifecycleOwner) {
			when (it) {
				is VisionBoardViewModel.PartnerBoardState.Data -> {
					binding.loader.isVisible = false
					setUpData(it.partnerObj)
				}
				is VisionBoardViewModel.PartnerBoardState.Error -> {
					binding.loader.isVisible = false
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	
	private fun setUpData(partnerObj: PartnerVisionBoardModel) {
		binding.partnerNameTv.text = viewModel.getName()
		binding.partnerMinsetTv.text = partnerObj.response.partnerMindset
		binding.blissSeasonTv.text = partnerObj.response.seasonOfBliss
		binding.recognizeTv.text = partnerObj.response.datingStyle
		partnerObj.response.datingMomentum?.let { txt ->
			binding.datingMomentumTv.text = txt.toString()
		}
		partnerObj.response.beAware?.let { txt ->
			binding.beWareTv.text = txt.toString()
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}