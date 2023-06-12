package com.tovalue.me.ui.datingjourneyjournal

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.FragmentDatingPartnerMeetGreetBinding
import com.tovalue.me.model.datingJourney.*
import com.tovalue.me.ui.dashboard.navigation.NavigationActivity
import com.tovalue.me.ui.dashboard.pushalert.PushAlertsFragment
import com.tovalue.me.ui.datingjourneyjournal.viewmodel.DatingJourneyMeetGreetViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.Utils
import com.tovalue.me.util.extensions.addFragmentSafely
import com.tovalue.me.util.extensions.replaceFragmentSafely
import java.text.SimpleDateFormat
import java.util.*


class DatingPartnerMeetGreetFragment : Fragment() {

    private var _binding: FragmentDatingPartnerMeetGreetBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: DatingJourneyMeetGreetViewModel by activityViewModels()
    private var args: Bundle? = null
    private var datingData: JourneyHomeResponse? = null
    private var getMeetGreetData: DatingMeetGreetResponse? = null
    private var reviewCouplePlanData: DatingReviewCouplePlanResponse? = null
    private var forView: String? = ""
    private var testimonialID: Int? = 0
    private var fromNotification: String? = ""

    private var experienceType: String = ""
    private var status: String = ""
    private var inviationIncluded: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatingPartnerMeetGreetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args = arguments

        setUpObserver()
        setUpDataObserver()


        makeApiCall()

    }

    private fun makeApiCall() {
        DialogUtils.showDialog(requireActivity(), false)
        mViewModel.getMeetGreetQuestions(datingData!!.homeJourney[0].group_id)
    }

    private fun makeApiViewCall() {

        mViewModel.getReviewCouplePlan(testimonialID!!)
    }

    private fun setUpObserver() {
        mViewModel.meetGreetQuestionState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyMeetGreetViewModel.GetMGTestimonial.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyMeetGreetViewModel.GetMGTestimonial.DataResponse -> {
                    if (forView.equals("0")) {
                        DialogUtils.hideDialog()
                    }

                    Log.i("TAG->", "setUpObserver: ${Gson().toJson(it.response)}")
                    getMeetGreetData = it.response
                    setData()
                    setClickListener()

                }
            }
        }
    }

    private fun setUpDataObserver() {
        mViewModel.reviewCouplePlanState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyMeetGreetViewModel.GetReviewCouplePlanEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is DatingJourneyMeetGreetViewModel.GetReviewCouplePlanEvent.DataResponse -> {
                    DialogUtils.hideDialog()

                    Log.i("TAG->", "setUpObserver------------>: ${Gson().toJson(it.response)}")
                    reviewCouplePlanData = it.response

                    binding.subTitleTv.setText(Html.fromHtml("<b>ON</b> " + reviewCouplePlanData!!.results!!.date + " - " + reviewCouplePlanData!!.results!!.title))

                    binding.question0ET.setText(reviewCouplePlanData!!.results!!.field1.toString())
                    binding.question1ET.setText(reviewCouplePlanData!!.results!!.field2.toString())
                    binding.question2ET.setText(reviewCouplePlanData!!.results!!.field3.toString())
                    //  setData()
                    // setClickListener()

                }
            }
        }
    }

    private fun setUpSendMGObserver() {
        mViewModel.submitMGTestimonialState.observe(viewLifecycleOwner) {
            when (it) {
                is DatingJourneyMeetGreetViewModel.SubmitMGTestimonialEvent.Error -> {
                    DialogUtils.hideDialog()

                    it.errorMsg?.let { it1 ->
                        DialogUtils.hideDialog()
                        showToast("Offer a level 2 commitment to your dating partner before proceeding.")
                    }
                }

                is DatingJourneyMeetGreetViewModel.SubmitMGTestimonialEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    Toast.makeText(
                        requireActivity(),
                        "Meet & Greet Testimonial submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (forView.equals("1") && fromNotification.equals("1")) {
                        forView = "0"
                        setData()
                    }
                    binding.question0ET.text!!.clear()
                    binding.question1ET.text!!.clear()
                    binding.question2ET.text!!.clear()
                    binding.experienceTypeSpinner.setSelection(0)
                    binding.cbTerms.isChecked = false
                }
            }
        }
    }

    private fun setClickListener() {
        binding.tvClose.setOnClickListener {
            if (forView == "1" && fromNotification.equals("1")) {
               requireActivity().onBackPressed()
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        binding.viewHistoryTv.setOnClickListener {
            replaceFragmentSafely(
                DatingPartnerMeetGreetHistoryFragment.newInstance(Gson().toJson(datingData)),
                containerViewId = R.id.navigation_root_container,
                addToStack = false
            )
        }

        binding.cbTerms.setOnClickListener {
            val level = datingData!!.homeJourney[0].level.toInt()
            if (level >= 2) {
                if (binding.cbTerms.isChecked) {
                    binding.cbTerms.isChecked = true
                    inviationIncluded = 1
                } else {
                    binding.cbTerms.isChecked = false
                    inviationIncluded = 2
                }
            } else {
                binding.cbTerms.isChecked = true
                inviationIncluded = 1
                startActivity(
                    NavigationActivity.createIntent(
                        requireContext(),
                        Constants.SUBSCRIPTION
                    )
                )
            }
        }

//        binding.cbTerms.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                val level = datingData!!.homeJourney[0].level.toInt()
//                if (level >= 2){
//                    if (isChecked){
//                        buttonView!!.isChecked = false
//                        inviationIncluded = 1
//                    }
//                    else{
//                        buttonView!!.isChecked = true
//                        inviationIncluded = 2
//                    }
//                }
//                else{
//                    buttonView!!.isChecked = false
//                    inviationIncluded = 1
//                    startActivity(
//                        NavigationActivity.createIntent(
//                            requireContext(),
//                            Constants.SUBSCRIPTION
//                        )
//                    )
//                }
//            }
//        })

        binding.shareBtn.setOnClickListener {

            val monthFormat = SimpleDateFormat("MM")
            val yearFormat = SimpleDateFormat("yyyy")

            val month = monthFormat.format(Date())
            val year = yearFormat.format(Date())
            val ans0 = binding.question0ET.text.toString()
            val ans1 = binding.question1ET.text.toString()
            val ans2 = binding.question2ET.text.toString()

            if (ans0.isNotEmpty()) {
                if (ans1.isNotEmpty()) {
                    if (ans2.isNotEmpty()) {

                        DialogUtils.showDialog(requireActivity(), false)
                        if (fromNotification.equals("0")) {
                            status = "pending"
                            mViewModel.sendMGTestimonial(
                                datingData!!.homeJourney[0].group_id,
                                month,
                                year,
                                experienceType,
                                ans0,
                                ans1,
                                ans2, inviationIncluded, status
                            )

                            setUpSendMGObserver()
                        } else {
                            status = "acknowledge"
                            mViewModel.sendMGAckTestimonial(
                                reviewCouplePlanData!!.results!!.groupId!!,
                                testimonialID!!,
                                reviewCouplePlanData!!.results!!.forMonth!!.toString(),
                                reviewCouplePlanData!!.results!!.ofYear!!.toString(),
                                reviewCouplePlanData!!.results!!.title!!,
                                reviewCouplePlanData!!.results!!.field1!!,
                                reviewCouplePlanData!!.results!!.field2!!,
                                reviewCouplePlanData!!.results!!.field3!!,
                                status
                            )
                            setUpSendMGObserver()
                        }

                    } else {
                        binding.question2ET.setError("Please enter third answer")
                    }
                } else {
                    binding.question1ET.setError("Please enter second answer")
                }
            } else {
                binding.question0ET.setError("Please enter first answer")
            }
        }
    }

    private fun setData() {
        binding.question0Tv.setText(
            Html.fromHtml(
                "• " + getMeetGreetData!!.results!!.question0!!.question!!.replace(
                    datingData!!.homeJourney.get(0).partner,
                    "${"<b>" + datingData!!.homeJourney.get(0).partner + "</b>"}",
                    true
                )
            )
        )
        binding.question1Tv.setText(
            Html.fromHtml(
                "• " + getMeetGreetData!!.results!!.question1!!.question!!.replace(
                    datingData!!.homeJourney.get(0).partner,
                    "${"<b>" + datingData!!.homeJourney.get(0).partner + "</b>"}",
                    true
                )
            )
        )
        binding.question2Tv.setText(
            Html.fromHtml(
                "• " + getMeetGreetData!!.results!!.question2!!.question!!.replace(
                    datingData!!.homeJourney.get(0).partner,
                    "${"<b>" + datingData!!.homeJourney.get(0).partner + "</b>"}",
                    true
                )
            )
        )


        binding.cbTerms.setText(
            Html.fromHtml(
                requireActivity().resources.getString(R.string.meet_greet_condition).replace(
                    "xxxxx",
                    "${"<b>" + datingData!!.homeJourney.get(0).partner + "</b>"}",
                    true
                )
            )
        )

        setUpSpinnerAdapter(getMeetGreetData!!)

        if (forView.equals("1") && fromNotification.equals("1")) {
            makeApiViewCall()
            binding.llExperienceType.visibility = View.GONE
            binding.cbTerms.visibility = View.GONE
            binding.shareBtn.visibility = View.VISIBLE

            binding.question0ET.isEnabled = false
            binding.question1ET.isEnabled = false
            binding.question2ET.isEnabled = false

            binding.viewHistoryTv.visibility = View.GONE
            binding.tvClose.visibility = View.VISIBLE


        } else if (forView.equals("1")) {
            makeApiViewCall()
            binding.llExperienceType.visibility = View.GONE
            binding.cbTerms.visibility = View.GONE
            binding.shareBtn.visibility = View.GONE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.question0ET.focusable = View.NOT_FOCUSABLE
                binding.question1ET.focusable = View.NOT_FOCUSABLE
                binding.question2ET.focusable = View.NOT_FOCUSABLE
            }

            binding.viewHistoryTv.visibility = View.GONE
            binding.tvClose.visibility = View.VISIBLE


        } else {
            binding.question0ET.isEnabled = true
            binding.question1ET.isEnabled = true
            binding.question2ET.isEnabled = true

            binding.question0ET.text!!.clear()
            binding.question1ET.text!!.clear()
            binding.question2ET.text!!.clear()
            binding.experienceTypeSpinner.setSelection(0)
            binding.cbTerms.isChecked = false

            binding.subTitleTv.setText(
                Html.fromHtml(
                    requireActivity().resources.getString(R.string.primer_v_meet_greet_sub_title)
                        .replace(
                            "xxxxx",
                            "${"<b>" + datingData!!.homeJourney.get(0).partner + "</b>"}",
                            true
                        )
                )
            )
            binding.llExperienceType.visibility = View.VISIBLE
            binding.cbTerms.visibility = View.VISIBLE
            binding.shareBtn.visibility = View.VISIBLE
            binding.tvClose.visibility = View.GONE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.question0ET.focusable = View.FOCUSABLE
                binding.question1ET.focusable = View.FOCUSABLE
                binding.question2ET.focusable = View.FOCUSABLE
            }
            binding.viewHistoryTv.visibility = View.VISIBLE
        }
    }

    private fun setUpSpinnerAdapter(response: DatingMeetGreetResponse) {

        binding.ivDropdown.setOnClickListener {
            binding.experienceTypeSpinner.performClick()
        }

        response.results!!.experienceTypes?.let {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            binding.experienceTypeSpinner.adapter = adapter
        }

        binding.experienceTypeSpinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>, v: View?, i: Int, lng: Long) {
                experienceType = adapter.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(
            param1: String,
            forView: String,
            fromNotification: String,
            testimonialId: Int
        ): DatingPartnerMeetGreetFragment {
            val fragment = DatingPartnerMeetGreetFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(FOR_VIEW, forView)
            args.putInt(TESTIMONIAL_ID, testimonialId)
            args.putString(FROM_NOTIFICATION, fromNotification)

            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"
        private const val FOR_VIEW = "For_VIEW"
        private const val TESTIMONIAL_ID = "TESTIMONIAL_ID"
        private const val FROM_NOTIFICATION = "FROM_NOTIFICATION"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {

            datingData = Gson().fromJson(
                arguments?.getString(ARG_PARAM1) ?: "",
                JourneyHomeResponse::class.java
            )

            fromNotification = arguments?.getString(FROM_NOTIFICATION) ?: ""
            forView = arguments?.getString(FOR_VIEW) ?: ""
            testimonialID = arguments?.getInt(TESTIMONIAL_ID) ?: 0
        }
    }


}