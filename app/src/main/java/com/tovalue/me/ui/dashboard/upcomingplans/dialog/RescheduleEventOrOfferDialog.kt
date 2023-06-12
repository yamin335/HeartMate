package com.tovalue.me.ui.dashboard.upcomingplans.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.tovalue.me.R
import com.tovalue.me.common.showToast
import com.tovalue.me.databinding.DialogFragmentResheduleEventOrOfferBinding
import com.tovalue.me.ui.dashboard.upcomingplans.models.DateNightEvent
import com.tovalue.me.ui.dashboard.upcomingplans.models.DateNightOffer
import com.tovalue.me.ui.dashboard.upcomingplans.models.UpcomingPlanDetailResponse
import com.tovalue.me.ui.dashboard.upcomingplans.viewmodel.UpcomingPlansViewModel
import com.tovalue.me.util.Constants
import com.tovalue.me.util.DialogUtils
import com.tovalue.me.util.Utils
import java.text.SimpleDateFormat
import java.util.*

class RescheduleEventOrOfferDialog : DialogFragment() {


    //    private var isBtnPressed = false
    private val upcomingPlansViewModel: UpcomingPlansViewModel by activityViewModels()

    private var _binding: DialogFragmentResheduleEventOrOfferBinding? = null
    private val binding get() = _binding!!

    private var startTime = ""
    private var startDate = ""
    private var endTime = ""
    private var endDate = ""
    private var eventId = -1

    private var dateFormat = "yyyy-MM-dd"
    private var timeFormat = "hh:mm:ss"
    private var dateTimeFormat = "E, MMM dd, yyyy h:mm a"

    private var upcomingPlanDetailResponse: UpcomingPlanDetailResponse? = null
    private var planType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentResheduleEventOrOfferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (eventId != -1) {
            upcomingPlansViewModel.getEventDetail(eventId)
        } else {

            if (upcomingPlanDetailResponse?.dateNightOffer != null) {
                setDataToViewForOffer(upcomingPlanDetailResponse?.dateNightOffer!!)
            } else
                setDataToView(upcomingPlanDetailResponse?.dateNightEvent!!)
        }

        binding.startTimeText.setOnClickListener {
            pickDateTime { cal ->
                startTime = SimpleDateFormat(timeFormat).format(cal.time)
                startDate = SimpleDateFormat(dateFormat).format(cal.time)
                val startTimeToShow = SimpleDateFormat(dateTimeFormat).format(cal.time)
                binding.startTimeText.text = startTimeToShow
            }
        }

        binding.endTimeText.setOnClickListener {
            pickDateTime { cal ->
                endTime = SimpleDateFormat(timeFormat).format(cal.time)
                endDate = SimpleDateFormat(dateFormat).format(cal.time)
                val endTimeToShow = SimpleDateFormat(dateTimeFormat).format(cal.time)
                binding.endTimeText.text = endTimeToShow
            }
        }

        binding.sendBtn.setOnClickListener {
            if (upcomingPlanDetailResponse?.dateNightOffer != null) {
                val dayNightOfferId =
                    upcomingPlanDetailResponse?.dateNightOffer?.dateNightOfferId?.toInt()
                if (isDataValid(true)) {
                    // isBtnPressed = true
                    DialogUtils.showDialog(requireContext(), false)
                    upcomingPlansViewModel.submitDateNightOfferDecisionForRescheduling(
                        dayNightOfferId!!,
                        3,
                        startTime = startTime,
                        startDate = startDate,
                        endTime = endTime,
                        endDate = endDate
                    )
                }
            } else {

                val idToPass = if (planType.equals(Constants.PLAN_TYPE_RESCHEDULE_OFFER, true))
                    upcomingPlanDetailResponse?.dateNightEvent?.parentEventId?.toInt()
                else upcomingPlanDetailResponse?.dateNightEvent?.eventId?.toInt()

                if (isDataValid(false)) {
                    DialogUtils.showDialog(requireContext(), false)
                    upcomingPlansViewModel.offerRescheduleDateNightEvent(
                        idToPass!!,
                        startTime = startTime,
                        startDate = startDate,
                        endTime = endTime,
                        endDate = endDate
                    )
                }
            }
        }


        binding.backBtn.setOnClickListener {
            if (eventId != -1) {
                requireActivity().onBackPressed()
            } else
                dismiss()
        }

        setUpObserver()

    }

    private fun setDataToView(dateNightEvent: DateNightEvent) {

        binding.detailsTv.text = dateNightEvent.eventDescription
        binding.urlTitleTv.text = dateNightEvent.eventWebsite
        binding.destinationTv.text = dateNightEvent.event_address
        binding.headerTv.text = dateNightEvent.eventTitle

//       2022-11-01 09:30:00
        val startDateTime =
            dateNightEvent.eventStartDate.plus(" ").plus(dateNightEvent.eventStartTime)
        if (startDateTime.isNotEmpty())
            binding.startTimeText.text =
                Utils.stringDateTimeToRequiredDateTime(startDateTime, dateTimeFormat)

        val endDateTime = dateNightEvent.eventEndDate.plus(" ").plus(dateNightEvent.eventEndTime)

        if (endDateTime.isNullOrEmpty().not())
            binding.endTimeText.text =
                Utils.stringDateTimeToRequiredDateTime(endDateTime, dateTimeFormat)
    }

    private fun setDataToViewForOffer(dateNightOffer: DateNightOffer) {
        binding.detailsTv.text = dateNightOffer.dateNightDescription
        binding.urlTitleTv.text = dateNightOffer.eventWebsite
        binding.destinationTv.text = dateNightOffer.location
        binding.headerTv.text = dateNightOffer.dateNightTitle

        val startDateTime =
            dateNightOffer.eventStartDate.plus(" ").plus(dateNightOffer.eventStartTime)

        if (startDateTime.isNotEmpty())
            binding.startTimeText.text =
                Utils.stringDateTimeToRequiredDateTime(startDateTime, dateTimeFormat)

        val endDateTime = dateNightOffer.eventEndDate.plus(" ").plus(dateNightOffer.eventEndTime)

        if (endDateTime.isNotEmpty())
            binding.endTimeText.text =
                Utils.stringDateTimeToRequiredDateTime(endDateTime, dateTimeFormat)
    }


    private fun setUpObserver() {
        upcomingPlansViewModel.rescheduleEvent.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.RescheduleEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.RescheduleEvent.DataResponse -> {
                    showToast(it.response.planStatus.message)
                    DialogUtils.hideDialog()
                    if (eventId != -1) {
                        requireActivity().onBackPressed()
                    } else
                        dismiss()
                }
            }
        }
        upcomingPlansViewModel.submitDateNightOfferDecisionForReSchedulingState.observe(
            viewLifecycleOwner
        ) {
            //if (isBtnPressed) {
            when (it) {
                is UpcomingPlansViewModel.SubmitDateNightOfferDecisionForRescheduling.DataResponse -> {
                    showToast(it.response.offerStatus.message)
                    DialogUtils.hideDialog()
                    dismiss()
                }
                is UpcomingPlansViewModel.SubmitDateNightOfferDecisionForRescheduling.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { msg -> showToast(msg) }
                }
            }
            //  isBtnPressed = false
            //   }
        }

        upcomingPlansViewModel.upcomingPlanDetail.observe(viewLifecycleOwner) {
            when (it) {
                is UpcomingPlansViewModel.UpcomingPlanEventDetailEvent.Error -> {
                    DialogUtils.hideDialog()
                    it.errorMsg?.let { it1 -> showToast(it1) }
                }
                is UpcomingPlansViewModel.UpcomingPlanEventDetailEvent.DataResponse -> {
                    DialogUtils.hideDialog()
                    upcomingPlanDetailResponse = it.response
                    if (it.response.dateNightOffer != null) {
                        setDataToViewForOffer(it.response.dateNightOffer!!)
                    } else
                        setDataToView(it.response.dateNightEvent!!)
                }
            }
        }
    }


    private fun isDataValid(endTimeOption: Boolean): Boolean {
        if (startDate.isEmpty() or startTime.isEmpty()) {
            showToast("Please enter start time")
            return false
        } else if (endTimeOption.not() && endDate.isEmpty() or endTime.isEmpty()) {
            showToast("Please enter end time")
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)

        if (arguments != null && requireArguments().containsKey(ARG_PARAM1) && arguments?.getSerializable(
                ARG_PARAM1
            ) != null
        ) {
            upcomingPlanDetailResponse =
                arguments?.getSerializable(ARG_PARAM1) as UpcomingPlanDetailResponse
        }

        planType = arguments?.getString(ARG_PARAM2) ?: ""
        eventId = arguments?.getInt(ARG_PARAM3) ?: -1
    }


    private fun pickDateTime(callback: (Calendar) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), R.style.DatePickerTheme, { _, year, month, day ->
            TimePickerDialog(requireContext(), R.style.DatePickerTheme, { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                callback.invoke(pickedDateTime)
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(
            param1: UpcomingPlanDetailResponse?,
            planType: String,
            eventId: Int?
        ): RescheduleEventOrOfferDialog {
            val fragment = RescheduleEventOrOfferDialog()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, planType)
            args.putInt(ARG_PARAM3, eventId ?: -1)
            fragment.arguments = args
            return fragment
        }

        private const val ARG_PARAM1 = "ARG_PARAM1"
        private const val ARG_PARAM2 = "ARG_PARAM2"
        private const val ARG_PARAM3 = "ARG_PARAM3"
    }


}