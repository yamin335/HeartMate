package com.tovalue.me.ui.visionboard.internal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.shape.CornerFamily
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.customviews.VisionBoardCard
import com.tovalue.me.databinding.FragmentAdultingTabBinding
import com.tovalue.me.ui.visionboard.VisionBoardViewModel
import com.tovalue.me.ui.visionboard.dialog.InternalTabInfoDialogFragment
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver

class AdultSeasonTabFragment : Fragment() {
	private var _binding: FragmentAdultingTabBinding ?= null
	private val binding get() = _binding!!
	private val viewModel: VisionBoardViewModel by activityViewModels()
	private var radioGroupOneMsg: String = ""
	private var radioGroupTwoMsg: String = ""
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAdultingTabBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpCardView()
		setUPData()
		setUpClickListener()
		observeEvents()
	}
	
	private fun observeEvents() {
		viewModel.internalTabState.observe(viewLifecycleOwner, EventObserver{
			when(it) {
				is VisionBoardViewModel.InternalTabEvent.RedirectToNextScreen -> {
					DialogUtils.hideDialog()
					showToast("Updated Successfully.")
					requireActivity().onBackPressed()
				}
				is VisionBoardViewModel.InternalTabEvent.Error -> {
					DialogUtils.hideDialog()
					showToast(it.errorMsg.toString())
				}
			}
		})
	}
	
	private fun setUpClickListener() {
		binding.adultTabBtn.setOnClickListener {
			if (radioGroupOneMsg.isEmpty() || radioGroupTwoMsg.isEmpty())
				showToast("Please select option")
			else
				submitSeasonData()
		}
	}
	
	private fun submitSeasonData() {
		DialogUtils.showDialog(requireContext(), false)
		viewModel.submitSeasonData(radioGroupOneMsg, radioGroupTwoMsg)
	}
	
	@SuppressLint("InflateParams")
	private fun setUPData() {
		// remove any child from parent view if any exists
		binding.seasonLayout.removeAllViews()
		binding.radioGroup.removeAllViews()
		binding.radioGroupSecondary.removeAllViews()
		
		val boardObj = viewModel.sharedData.value
		boardObj?.let { setCurrentTabTagline(it.userAdultingSeasonPrimary) }
		boardObj?.let {
			for (i in it.adultingSeasonForm.indices) {
				val seasonCardLayout: View =
					layoutInflater.inflate(R.layout.item_vision_board_season, null)
				val primaryRadioLayout: View = layoutInflater.inflate(R.layout.item_radio_btn, null)
				val secondaryRadioLayout: View =
					layoutInflater.inflate(R.layout.item_radio_btn, null)
				
				
				// cardView for title
				val seasonTitle = seasonCardLayout.findViewById<TextView>(R.id.name_tv)
				val descriptionTv = seasonCardLayout.findViewById<TextView>(R.id.see_description_tv)
				val definition = it.adultingSeasonForm[i].definition
				seasonTitle.text = it.adultingSeasonForm[i].title
				descriptionTv.setOnClickListener {
					InternalTabInfoDialogFragment.newInstance(
						seasonTitle.text.toString(), true, definition
					).show(parentFragmentManager, InternalTabInfoDialogFragment.TAG)
				}
				
				// primary radio group
				val primaryRadio = primaryRadioLayout.findViewById<RadioButton>(R.id.radioButton1)
				primaryRadio.id = i
				primaryRadio.text = it.adultingSeasonForm[i].title
				
				// refactor check
				if (it.adultingSeasonForm[i].title.equals(it.userAdultingSeasonPrimary, ignoreCase = true)) {
					primaryRadio.performClick()
					radioGroupOneMsg = primaryRadio.text.toString()
				}
				primaryRadio.setOnClickListener {
					radioGroupOneMsg = primaryRadio.text.toString()
					setCurrentTabTagline(radioGroupOneMsg)
				}
				
				// secondary radio group
				val secondaryRadio =
					secondaryRadioLayout.findViewById<RadioButton>(R.id.radioButton1)
				secondaryRadio.id = i
				secondaryRadio.text = it.adultingSeasonForm[i].title
				
				// refactor check
				if (it.adultingSeasonForm[i].title.equals(it.adultingSeasonSecondary, ignoreCase = true)) {
					secondaryRadio.performClick()
					radioGroupTwoMsg = secondaryRadio.text.toString()
				}
				secondaryRadio.setOnClickListener {
					radioGroupTwoMsg = secondaryRadio.text.toString()
					setCurrentTabTagline(radioGroupTwoMsg)
				}
				
				// add to specific views
				binding.seasonLayout.addView(seasonCardLayout)
				binding.radioGroup.addView(primaryRadio)
				binding.radioGroupSecondary.addView(secondaryRadio)
			}
		}
	}
	
	private fun setCurrentTabTagline(msg: String) {
		binding.tagTv.text = msg
	}
	
	private fun setUpCardView() {
		binding.cardView.shapeAppearanceModel = binding.cardView.shapeAppearanceModel.toBuilder()
			.setTopLeftCorner(VisionBoardCard())
			.setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
			.setBottomRightCorner(CornerFamily.ROUNDED, 0f)
			.build()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}