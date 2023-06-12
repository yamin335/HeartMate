package com.tovalue.me.ui.visionboard

import android.content.res.Resources.Theme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentVisionBoardBinding
import com.tovalue.me.ui.visionboard.dialog.VisionBoardDialogFragment
import com.tovalue.me.ui.visionboard.internal.AdultSeasonTabFragment
import com.tovalue.me.ui.visionboard.internal.BlissSeasonTabFragment
import com.tovalue.me.ui.visionboard.internal.DatingTabFragment
import com.tovalue.me.ui.visionboard.internal.MyMindsetFragment
import com.tovalue.me.ui.visionboard.response.VisionBoardModel
import com.tovalue.me.util.extensions.makeStatusBarTransparent
import com.tovalue.me.util.extensions.replaceFragmentSafely

class VisionBoardFragment : Fragment() {
	private var _binding: FragmentVisionBoardBinding? = null
	private val binding get() = _binding!!
	
	// share data between other fragment within parent activity
	// could be expensive to pass huge data via bundle or serializable object
	private val boardViewModel: VisionBoardViewModel by activityViewModels()
	private lateinit var boardObj: VisionBoardModel
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentVisionBoardBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().makeStatusBarTransparent()
		visionBoardData()
		setUpObserver()
		setUpClickListener()
	}
	
	// refactor api, call only when data update from one of the form
	private fun visionBoardData() {
		binding.loader.isVisible = true
		boardViewModel.getVisionBoardDetails()
	}
	
	private fun setUpObserver() {
		boardViewModel.boardState.observe(viewLifecycleOwner) {
			when (it) {
				is VisionBoardViewModel.VisionBoardState.Data -> {
					binding.loader.isVisible = false
					boardObj = it.responseObj
					setData()
					boardViewModel.setClickedDataForObserve(boardObj.response)
				}
				is VisionBoardViewModel.VisionBoardState.Error -> {
					binding.loader.isVisible = false
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	private fun setData() {
		binding.mindsetSubtitleTv.text = boardObj.response.userMindset
		binding.adultSubtitleTv.text = boardObj.response.userAdultingSeasonPrimary
		binding.blissSubtitleTv.text = boardObj.response.userBlissPrimary
		binding.dateSubtitleTv.text =
			if (boardObj.response.dateStyleList.isEmpty()) UNDECIDED else boardObj.response.dateStyleList[0].title
	}
	
	private fun setUpClickListener() {
		binding.mindsetLayout.setOnClickListener {
			goToMindsetTabFrag()
		}
		
		binding.adultFrameLayout.setOnClickListener {
			if (isMindsetFormValid())
				goToDialogFrag(resources.getString(R.string.mindset_tab), R.color.orange_dark)
			else
				goToAdultSeasonTabFrag()
		}
		
		binding.blissFrameLayout.setOnClickListener {
			if (isMindsetFormValid())
				goToDialogFrag(resources.getString(R.string.mindset_tab), R.color.orange_dark)
			else if (isAdultFormValid())
				goToDialogFrag(resources.getString(R.string.adult_tab), R.color.purple_dark)
			else
				goToBlissTabFragment()
		}
		
		binding.dateFrameLayout.setOnClickListener {
			if (isMindsetFormValid())
				goToDialogFrag(resources.getString(R.string.mindset_tab), R.color.orange_dark)
			else if (isAdultFormValid())
				goToDialogFrag(resources.getString(R.string.adult_tab), R.color.purple_dark)
			else if (isBlissFormValid())
				goToDialogFrag(resources.getString(R.string.bliss_tab), R.color.mild_blue)
			else
				goToDateStyleTabFrag()
		}
		binding.viewVisionBtn.setOnClickListener {
			filterTabState()
		}
	}
	
	private fun filterTabState() {
		if (isMindsetFormValid()) {
			goToDialogFrag(resources.getString(R.string.mindset_tab), R.color.orange_dark)
			return
		}
		if (isAdultFormValid()) {
			goToDialogFrag(resources.getString(R.string.adult_tab), R.color.purple_dark)
			return
		}
		if (isBlissFormValid()) {
			goToDialogFrag(resources.getString(R.string.bliss_tab), R.color.mild_blue)
			return
		}
		if (isDateStyleFormValid()) {
			goToDialogFrag(resources.getString(R.string.date_tab), R.color.light_golden)
			return
		}
		goToPartnerVisionBoardFrag()
	}
	
	private fun isDateStyleFormValid(): Boolean = boardObj.response.dateStyleList.isEmpty()
	
	
	private fun isBlissFormValid(): Boolean =
		boardObj.response.userBlissPrimary.equals(UNDECIDED, ignoreCase = true)
	
	private fun isMindsetFormValid(): Boolean =
		boardObj.response.userMindset.equals(TAP, ignoreCase = true) ||
				boardObj.response.userMindset.equals(UNDECIDED, ignoreCase = true)
	
	private fun isAdultFormValid(): Boolean =
		boardObj.response.userAdultingSeasonPrimary.equals(UNDECIDED, ignoreCase = true)
	
	private fun goToDateStyleTabFrag() {
		replaceFragmentSafely(
			DatingTabFragment(), containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0,
			popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun goToBlissTabFragment() {
		replaceFragmentSafely(
			BlissSeasonTabFragment(), containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0,
			popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun goToAdultSeasonTabFrag() {
		replaceFragmentSafely(
			AdultSeasonTabFragment(), containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0,
			popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun goToMindsetTabFrag() {
		replaceFragmentSafely(
			MyMindsetFragment(), containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0,
			popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun goToPartnerVisionBoardFrag() {
		replaceFragmentSafely(
			PartnerVisionBoardFragment(), containerViewId = R.id.navigation_root_container,
			enterAnimation = 0, exitAnimation = 0,
			popEnterAnimation = 0, popExitAnimation = 0
		)
	}
	
	private fun goToDialogFrag(msg: String, color: Int) {
		VisionBoardDialogFragment.newInstance(msg, color)
			.show(parentFragmentManager, VisionBoardDialogFragment.TAG)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val UNDECIDED = "UNDECIDED"
		const val TAP = "TAP HERE TO START"
	}
}