package com.tovalue.me.ui.visionboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentIceBreakersBinding
import com.tovalue.me.ui.visionboard.response.IceBreakerResponseModel
import com.tovalue.me.util.extensions.copyToClipboard

class IceBreakersFragment : Fragment() {
	private var _binding: FragmentIceBreakersBinding ?= null
	private val binding get() = _binding!!
	private val viewModel: VisionBoardViewModel by activityViewModels()
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentIceBreakersBinding.inflate(inflater, container, false)
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
		viewModel.getIceBreakerBoard()
		
		Glide.with(requireContext()).load(viewModel.iceBreakerBackgroundImage())
			.transform(CenterCrop(), RoundedCorners(20))
			.into(binding.backImg)
	}
	
	private fun setUpObserver() {
		viewModel.iceBreaker.observe(viewLifecycleOwner) {
			when (it) {
				is VisionBoardViewModel.IceBreakerState.Data -> {
					binding.loader.isVisible = false
					if (it.iceBreakerObj.response.isNotEmpty()) {
						binding.iceBreakerLayout.isVisible = true
						setUpData(it.iceBreakerObj)
					}
				}
				is VisionBoardViewModel.IceBreakerState.Error -> {
					binding.loader.isVisible = false
					showToast(it.errorMsg.toString())
				}
			}
		}
	}
	
	// recyclerview could be used for better performance
	private fun setUpData(iceBreakerObj: IceBreakerResponseModel) {
		binding.iceBreakerLayout.removeAllViews()
		for (i in iceBreakerObj.response.indices) {
			val linearLayout: View = layoutInflater.inflate(R.layout.item_ice_breaker, null)
			val descriptionTv = linearLayout.findViewById<TextView>(R.id.description_tv)
			val copyBtn = linearLayout.findViewById<TextView>(R.id.copy_btn)
			
			descriptionTv.text = iceBreakerObj.response[i].icebreakerQuestion
			copyBtn.setOnClickListener {
				copyBtn.context.copyToClipboard(descriptionTv.text.toString())
				showToast("Text Copied to Clipboard!")
			}
			
			binding.iceBreakerLayout.addView(linearLayout)
		}
	}
	
	private fun setUpClickListener() {
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}