package com.tovalue.me.ui.visionboard.internal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.shape.CornerFamily
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.customviews.VisionBoardCard
import com.tovalue.me.databinding.FragmentMyMindsetBinding
import com.tovalue.me.databinding.ItemRadioBtnBinding
import com.tovalue.me.ui.visionboard.VisionBoardFragment
import com.tovalue.me.ui.visionboard.VisionBoardViewModel
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver


class MyMindsetFragment : Fragment() {
	private var _binding: FragmentMyMindsetBinding ?= null
	private val binding get() = _binding!!
	private val viewModel: VisionBoardViewModel by activityViewModels()
	private var selectedMsg: String = ""
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentMyMindsetBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpCardView()
		setUpClickListener()
		addRadioBtn()
		setUpObserver()
	}
	
	private fun setUpObserver() {
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
		binding.viewVisionBtn.setOnClickListener {
			if (selectedMsg.isEmpty()) showToast("Please select the option")
			else submitMindsetData(selectedMsg)
		}
	}
	
	private fun submitMindsetData(msg: String) {
		DialogUtils.showDialog(requireContext(), false)
		viewModel.submitMindsetData(msg)
	}
	
	@SuppressLint("InflateParams")
	private fun addRadioBtn() {
		binding.radioGroup.removeAllViews()
		val boardObj = viewModel.sharedData.value
		boardObj?.let { setCurrentTabTagline(it.userMindset) }
		for (i in boardObj?.mindsetForm?.indices!!) {
			val customRadioBtn: View = layoutInflater.inflate(R.layout.item_radio_btn, null)
			val radio = customRadioBtn.findViewById<RadioButton>(R.id.radioButton1)
			radio.id = i
			radio.text = boardObj.mindsetForm[i].title
			
			// refactor click
			if (boardObj.mindsetForm[i].title.equals(boardObj.userMindset, ignoreCase = true)) {
				radio.performClick()
				selectedMsg = radio.text.toString()
			}
			radio.setOnClickListener {
				selectedMsg = radio.text.toString()
				setCurrentTabTagline(radio.text.toString())
			}
			binding.radioGroup.addView(radio)
		}
	}
	
	private fun setCurrentTabTagline(msg: String) {
		binding.tagTv.text = msg
	}
	
	private fun setUpCardView() {
		binding.mindsetCard.shapeAppearanceModel = binding.mindsetCard.shapeAppearanceModel.toBuilder()
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