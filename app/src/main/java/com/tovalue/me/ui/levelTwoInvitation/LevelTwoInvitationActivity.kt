package com.tovalue.me.ui.levelTwoInvitation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tovalue.me.R
import com.tovalue.me.databinding.ActivityLevelTwoInvitationBinding
import com.tovalue.me.util.extensions.addFragmentSafely


class LevelTwoInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelTwoInvitationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevelTwoInvitationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            val backStackCount =
                supportFragmentManager.backStackEntryCount
            if (backStackCount == 0)
                finish()
            else
                supportFragmentManager.popBackStack()
        }

        addFragmentSafely(
            tag = LevelTwoInvitationFirstStepFragment.javaClass.name,
            containerViewId = R.id.fragment_container,
            fragment = LevelTwoInvitationFirstStepFragment.newInstance(
                intent.getIntExtra(ARGUMENT_PARAM1, -1)
            )
        )

    }

    companion object {
        fun getIntent(context: Context, itemId: Int): Intent {
            return Intent(context, LevelTwoInvitationActivity::class.java).putExtra(
                ARGUMENT_PARAM1,
                itemId
            )
        }

        const val ARGUMENT_PARAM1 = "argument_param1"

    }


}