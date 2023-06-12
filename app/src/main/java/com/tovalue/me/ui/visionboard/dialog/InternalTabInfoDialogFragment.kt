package com.tovalue.me.ui.visionboard.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.tovalue.me.databinding.FragmentInternalTabInfoDialogBinding
import com.tovalue.me.dialog.BaseDialogFragment

class InternalTabInfoDialogFragment : BaseDialogFragment() {
	private var _binding: FragmentInternalTabInfoDialogBinding ?= null
	private val binding get() = _binding!!
	private lateinit var msg: String
	private var isEnabled: Boolean = false
	private lateinit var definition: String
	var btnClickListener: BtnClickListener ?= null
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentInternalTabInfoDialogBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpDialogData()
		setUpClickListener()
	}
	
	private fun setUpClickListener() {
		binding.frameLayout.setOnClickListener {
			dismiss()
		}
		binding.continueBtn.setOnClickListener {
			btnClickListener?.onBtnClick()
			dismiss()
		}
		binding.adultTabBtn.setOnClickListener {
			dismiss()
		}
	}
	
	private fun setUpDialogData() {
		binding.titleTv.text = msg
		binding.definitionTv.text = definition
		binding.titleVisibleTv.isVisible = isEnabled

		if (isEnabled) {
			binding.adultTabBtn.isVisible = true
			binding.continueBtn.isVisible = false
		} else {
			binding.adultTabBtn.isVisible = false
			binding.continueBtn.isVisible = true
		}
	}
	
	fun interface BtnClickListener {
		fun onBtnClick()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	override fun onDetach() {
		super.onDetach()
		btnClickListener = null
	}
	
	private fun setData(msg: String, isEnabled: Boolean, definition: String) {
		this.msg = msg
		this.isEnabled = isEnabled
		this.definition = definition
	}
	
	companion object {
		const val TAG = "InternalTabDialog"
		fun newInstance(msg: String, isEnabled: Boolean, definition: String): InternalTabInfoDialogFragment {
			return InternalTabInfoDialogFragment().apply {
				setData(msg, isEnabled, definition)
			}
		}
	}
}