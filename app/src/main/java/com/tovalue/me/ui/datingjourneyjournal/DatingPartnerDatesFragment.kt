package com.tovalue.me.ui.datingjourneyjournal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tovalue.me.R
import com.tovalue.me.adapter.DatingJournalDateAdapter
import com.tovalue.me.databinding.FragmentDatingPartnerDatesBinding


class DatingPartnerDatesFragment : Fragment() {

    private var _binding: FragmentDatingPartnerDatesBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatingPartnerDatesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerview()
    }



    /* Set RecyclerView*/
    private fun setRecyclerview() {

        binding.stepView.apply {

            adapter = DatingJournalDateAdapter(requireContext())
            hasFixedSize()
        }

    }

//    private fun setIndicator() {
//
//
//
//        val list0: MutableList<View> = ArrayList()
//        val child: View = layoutInflater.inflate(R.layout.dating_journal_date_rv_item, null)
//        val child2: View = layoutInflater.inflate(R.layout.dating_journal_date_rv_item, null)
//        val child3: View = layoutInflater.inflate(R.layout.dating_journal_date_rv_item, null)
//        val child4: View = layoutInflater.inflate(R.layout.dating_journal_date_rv_item, null)
//        val child5: View = layoutInflater.inflate(R.layout.dating_journal_date_rv_item, null)
//        list0.add(child)
//        list0.add(child2)
//        list0.add(child3)
//        list0.add(child4)
//
//        binding.stepView.setStepsViewIndicatorComplectingPosition(list0.size - 2)
//            .reverseDraw(false) //default is true
//            .setStepViewTexts(list0)
//            .setLinePaddingProportion(0.85f)
//            .setStepsViewIndicatorCompletedLineColor(
//                ContextCompat.getColor(
//                  requireActivity(),
//                    android.R.color.black
//                )
//            ) //设置StepsViewIndicator完成线的颜色
//            .setStepsViewIndicatorUnCompletedLineColor(
//                ContextCompat.getColor(
//                  requireActivity(),
//                    R.color.uncompleted_text_color
//                )
//            ) //设置StepsViewIndicator未完成线的颜色
//            .setStepViewComplectedTextColor(
//                ContextCompat.getColor(
//                  requireActivity(),
//                    android.R.color.black
//                )
//            ) //设置StepsView text完成线的颜色
//            .setStepViewUnComplectedTextColor(
//                ContextCompat.getColor(
//                  requireActivity(),
//                    R.color.uncompleted_text_color
//                )
//            ) //设置StepsView text未完成线的颜色
//            .setStepsViewIndicatorCompleteIcon(
//                ContextCompat.getDrawable(
//                  requireActivity(),
//                    R.drawable.complted
//                )
//            ) //设置StepsViewIndicator CompleteIcon
//            .setStepsViewIndicatorDefaultIcon(
//                ContextCompat.getDrawable(
//                    requireActivity(),
//                    R.drawable.default_icon
//                )
//            ) //设置StepsViewIndicator DefaultIcon
//            .setStepsViewIndicatorAttentionIcon(
//                ContextCompat.getDrawable(
//                  requireActivity(),
//                    R.drawable.attention
//                )
//            ) //设置StepsViewIndicator AttentionIcon
//
//
//    }

}