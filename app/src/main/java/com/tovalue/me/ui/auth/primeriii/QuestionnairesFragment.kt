package com.tovalue.me.ui.auth.primeriii

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentQuestionaireBinding
import com.tovalue.me.ui.auth.AuthViewModel
import com.tovalue.me.ui.auth.primeriii.spectrum.GreatJobFragment
import com.tovalue.me.util.extensions.replaceFragmentSafely
import com.tovalue.me.util.livedata.EventObserver

class QuestionnairesFragment : Fragment() {
	private var _binding: FragmentQuestionaireBinding? = null
	private val binding get() = _binding!!
	private val authViewMode: AuthViewModel by activityViewModels()
	private val questioniareAdapter = QuestionaireAdapter()
	private var categoryId: Int? = null
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentQuestionaireBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		loadQuestions()
		setUpViews()
		setUpClickListener()
		observeState()
	}
	
	private fun loadQuestions() {
		showLoader()
		authViewMode.categoryKey?.let { authViewMode.getQuestionsViaGroupKey(it) }
	}
	
	private fun observeState() {
		authViewMode.question.observe(viewLifecycleOwner, EventObserver  {
			hideLoader()
			when(it) {
				is AuthViewModel.UiEventQuestionLoad.Inventory -> {
					binding.progress.progress = authViewMode.getSidePosition()
					questioniareAdapter.setQuestionaireData(it.data.questions)
					categoryId = it.data.questions[0].categoryId
				}
				is AuthViewModel.UiEventQuestionLoad.Error -> showToast(it.errorMsg.toString())
			}
		})
		
		authViewMode.answer.observe(viewLifecycleOwner, EventObserver{
			hideLoader()
			when(it) {
				is AuthViewModel.UiEventQuestionaire.RedirectToSideScreen -> goBackToNextScreen()
				is AuthViewModel.UiEventQuestionaire.Error -> showToast(it.errorMsg.toString())
			}
		})
	}
	
	// need to create extension fun
	private fun hideLoader() {
		binding.loader.isVisible = false
		binding.continueBtn.isEnabled = true
		binding.continueBtn.isClickable = true
	}
	
	private fun showLoader() {
		binding.loader.isVisible = true
		binding.continueBtn.isEnabled = false
		binding.continueBtn.isClickable = false
	}
	
	// 8 ~= magic number | total side page | need to remove later with dynamic value
	private fun goBackToNextScreen() {
//		if (authViewMode.getSidePosition() == 8 && authViewMode.isEditMode) {
//			clearBackStack()
//			authViewMode.setBookMarkProgress(PRIMERIV_STAGE)
//			goToPrimerFourFragment()
//		}
//		else requireActivity().supportFragmentManager.popBackStack()
		if (authViewMode.isEditMode) {
			replaceFragmentSafely(
				GreatJobFragment(),
				containerViewId = R.id.host_root,
				addToStack = false
			)
		} else {
			replaceFragmentSafely(
				GreatJobFragment(),
				containerViewId = R.id.navigation_root_container,
				addToStack = false
			)
		}
	}
	
	private fun setUpViews() {
		val inventory = authViewMode.inventoryData.value
		binding.titleTv.text = inventory?.category
		with(binding.questionaireRv) {
			adapter = questioniareAdapter
		}
		binding.icHelp.setOnClickListener {
//			showToast(inventory.moreInfo)
		}
	}
	
	private fun setUpClickListener() {
		binding.continueBtn.setOnClickListener {
			val serializedJson = filterAnswerValues(questioniareAdapter.collectAllAnswers())
			sendFilteredValues(serializedJson)
		}
	}
	
	private fun sendFilteredValues(serializedJson: String) {
		showLoader()
		categoryId?.let { authViewMode.updateInventoryValues(it, serializedJson, binding.titleTv.text.toString()) }
	}
	
	private fun filterAnswerValues(list: MutableList<Questionaire>): String {
		val map = HashMap<String, Int>()
		for (i in list.indices) {
			map[list[i].topicId.toString()] = list[i].answerValues
		}
		return Gson().toJson(map)
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}