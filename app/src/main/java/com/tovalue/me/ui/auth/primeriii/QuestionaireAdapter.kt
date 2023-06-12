package com.tovalue.me.ui.auth.primeriii

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.tovalue.me.databinding.ItemQuestionaireBinding

class QuestionaireAdapter : RecyclerView.Adapter<QuestionaireAdapter.QuestionViewHolder>() {
	private val questionItem = mutableListOf<Questionaire>()
	
	fun setQuestionaireData(questions: List<Questionaire>) {
		questionItem.clear()
		questionItem.addAll(questions)
		notifyDataSetChanged()
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
		return QuestionViewHolder(
			ItemQuestionaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		)
	}
	
	override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
		var item = questionItem[position]
		holder.questionTv.text = item.question
		holder.progressTv.text = item.min.toString()
		
		if (item.isProgressValueEnabled) holder.sliderSb.progress = item.answerValues
		else holder.sliderSb.progress = item.min
		
		holder.sliderSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
			override fun onProgressChanged(seekbar: SeekBar?, progress: Int, isEnabled: Boolean) {
				holder.progressTv.text = seekbar!!.progress.toString()
				if (isEnabled) {
					item.answerValues = seekbar.progress
					item.isProgressValueEnabled = true
				}
			}
			
			override fun onStartTrackingTouch(p0: SeekBar?) {
			}
			
			override fun onStopTrackingTouch(p0: SeekBar?) {
			}
			
		})
	}
	
	override fun getItemCount(): Int = questionItem.size
	
	fun collectAllAnswers() = questionItem
	
	class QuestionViewHolder(private val binding: ItemQuestionaireBinding) :
		RecyclerView.ViewHolder(binding.root) {
		var questionTv = binding.questionTv
		var progressTv = binding.valueTv
		var sliderSb = binding.valueSb
		
		}
}