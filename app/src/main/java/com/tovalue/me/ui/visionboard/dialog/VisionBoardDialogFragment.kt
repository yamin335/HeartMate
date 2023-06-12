package com.tovalue.me.ui.visionboard.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tovalue.me.databinding.FragmentVisionBoardDialogBinding
import com.tovalue.me.dialog.BaseDialogFragment

class VisionBoardDialogFragment : BaseDialogFragment() {
	private var _binding: FragmentVisionBoardDialogBinding? = null
	private val binding get() = _binding!!
	private lateinit var msg: String
	private var color: Int = 0
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentVisionBoardDialogBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		if(savedInstanceState == null) {
			setUpData()
			setUPClickListener()
		}
		else dismiss()
	}
	
	private fun setUpData() {
		binding.titleTv.text = msg
		binding.tabBg.backgroundTintList = ContextCompat.getColorStateList(requireContext(), color)
	}
	
	private fun setUPClickListener() {
		binding.cancelImg.setOnClickListener {
			dismiss()
		}
	}
	
	private fun setMessage(msg: String, color: Int) {
		this.msg = msg
		this.color = color
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	companion object {
		const val TAG = "VisionBoardDialog"
		fun newInstance(msg: String, color: Int): VisionBoardDialogFragment {
			return VisionBoardDialogFragment().apply {
				setMessage(msg, color)
			}
		}
	}
}