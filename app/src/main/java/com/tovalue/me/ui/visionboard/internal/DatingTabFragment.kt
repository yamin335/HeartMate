package com.tovalue.me.ui.visionboard.internal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.CornerFamily
import com.tovalue.me.common.showToast
import com.tovalue.me.customviews.VisionBoardCard
import com.tovalue.me.databinding.FragmentDatingTabBinding
import com.tovalue.me.helper.DragDropItemHelper
import com.tovalue.me.ui.visionboard.DragDropListAdapter
import com.tovalue.me.ui.visionboard.VisionBoardViewModel
import com.tovalue.me.ui.visionboard.dialog.InternalTabInfoDialogFragment
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.livedata.EventObserver

class DatingTabFragment : Fragment(), DragDropListAdapter.StartDragListener {
	private var _binding: FragmentDatingTabBinding ?= null
	private val binding get() = _binding!!
	private val viewModel: VisionBoardViewModel by activityViewModels()
	private lateinit var itemHelper: ItemTouchHelper
	private var adapter: DragDropListAdapter ?= null
	private lateinit var definitionList: HashMap<String, String>
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentDatingTabBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpCardView()
		setUPData()
		setUpClickListener()
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
			val data: List<String> = adapter?.getUpdatedOrderListed()!!
			setCurrentTabTagline(data[0])
			var definition = ""
			definitionList.forEach { (k, v) ->
				if (k.contains(data[0], ignoreCase = true)) {
					definition = v
					return@forEach
				}
			}
			InternalTabInfoDialogFragment.newInstance(data[0], false, definition)
				.apply {
					btnClickListener = InternalTabInfoDialogFragment.BtnClickListener {
					DialogUtils.showDialog(requireContext(), false)
					viewModel.submitDatingFormData(data[0], data[1], data[2])
				}
			}.show(parentFragmentManager, InternalTabInfoDialogFragment.TAG)
		}
	}
	
	private fun setUPData() {
		val boardObj = viewModel.sharedData.value
		boardObj?.let {
			val list = ArrayList<String>()
			definitionList = HashMap()
			if (boardObj.dateStyleList.isNotEmpty()) {
				setCurrentTabTagline(boardObj.dateStyleList[0].title)
				for (i in boardObj.dateStyleList.indices) {
					list.add(boardObj.dateStyleList[i].title)
					definitionList[boardObj.dateStyleList[i].title] = boardObj.dateStyleList[i].definition
				}
			} else {
				setCurrentTabTagline(boardObj.datingStyleForm[0].title)
				for (i in boardObj.datingStyleForm.indices) {
					list.add(boardObj.datingStyleForm[i].title)
					definitionList[boardObj.datingStyleForm[i].title] = boardObj.datingStyleForm[i].definition
				}
			}
			
			
			adapter = DragDropListAdapter(list, this)
			itemHelper = ItemTouchHelper(DragDropItemHelper(adapter!!))
			itemHelper.attachToRecyclerView(binding.dateStyleRv)
			binding.dateStyleRv.adapter = adapter
		}
	}
	
	private fun setCurrentTabTagline(msg: String) {
		binding.tagTv.text = msg
	}
	
	private fun setUpCardView() {
		binding.dateCard.shapeAppearanceModel = binding.dateCard.shapeAppearanceModel.toBuilder()
			.setTopLeftCorner(VisionBoardCard())
			.setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
			.setBottomRightCorner(CornerFamily.ROUNDED, 0f)
			.build()
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	override fun requestDrag(viewHolder: RecyclerView.ViewHolder?) {
		viewHolder?.let { itemHelper.startDrag(it) }
	}
}