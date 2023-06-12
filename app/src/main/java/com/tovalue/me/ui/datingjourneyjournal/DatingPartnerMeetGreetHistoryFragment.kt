package com.tovalue.me.ui.datingjourneyjournal

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.adapter.DatingMGHistoryAdapter
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDatingPartnerMeetGreetHistoryBinding
import com.tovalue.me.model.datingJourney.*
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingJourneyMeetGreetHistoryViewModel
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.extensions.replaceFragmentSafely


class DatingPartnerMeetGreetHistoryFragment : Fragment() {

    private var _binding: FragmentDatingPartnerMeetGreetHistoryBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: DatingJourneyMeetGreetHistoryViewModel by activityViewModels()
    private var args: Bundle? = null
    private var datingData: JourneyHomeResponse? = null

    private var experienceType: String = ""
    private var status: String = "pending"
    private var inviationIncluded: Int = 1
    private var adapter:DatingMGHistoryAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatingPartnerMeetGreetHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = arguments

        setUpObserver()

        makeApiCall()
    }

    private fun makeApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        mViewModel!!.getMeetGreetHistory(datingData!!.homeJourney.get(0).group_id)
    }

    private fun setUpObserver() {
        mViewModel.meetGreetHistoryState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyMeetGreetHistoryViewModel.GetMGTestimonialHistory.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyMeetGreetHistoryViewModel.GetMGTestimonialHistory.DataResponse -> {
                    DialogUtils.hideDialog()

                    Log.i("TAG->", "setUpObserver: ${Gson().toJson(it.response)}")

                   setData(it.response)
                   // setClickListener(it.response)

                }
            }
        }
    }



    private fun setData(response: DatingMeetGreetHistoryResponse) {

        binding.subTitleTv.setText(
            Html.fromHtml(
                requireActivity().resources.getString(R.string.meet_greet_history_subtitle)
                    .replace(
                        "xxxxx",
                        "${"<b>" + datingData!!.homeJourney.get(0).partner?:"xxxxx" + "</b>"}",
                        true
                    )
            ))

        if (response.response!!.size >0){

            binding.noTestimonialTv.visibility=View.GONE
            binding.historyRv.visibility=View.VISIBLE

            adapter= DatingMGHistoryAdapter(requireActivity(),response.response,object :DatingMGHistoryAdapter.BtnClickListener{
                override fun onBtnClick(testimonialID: Int) {
                      replaceFragmentSafely(
                DatingPartnerMeetGreetFragment.newInstance(Gson().toJson(datingData),"1","0",testimonialID),
                containerViewId = R.id.navigation_root_container,
                addToStack = true
            )
                }

            })
            binding.historyRv.layoutManager= LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false)
            binding.historyRv.adapter=adapter

        }else{
            binding.historyRv.visibility=View.GONE
            binding.noTestimonialTv.visibility=View.VISIBLE
        }
        DialogUtils.hideDialog()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(param1: String, ): DatingPartnerMeetGreetHistoryFragment {
            val fragment = DatingPartnerMeetGreetHistoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)

            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            datingData = Gson().fromJson(
                arguments?.getString(ARG_PARAM1) ?: "",
                JourneyHomeResponse::class.java
            )
        }
    }
}