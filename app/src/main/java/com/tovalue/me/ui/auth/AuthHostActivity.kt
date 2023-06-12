package com.tovalue.me.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tovalue.me.R
import com.tovalue.me.databinding.ActivityAuthHostBinding
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.ui.auth.primer.LandingPageFragment
import com.tovalue.me.ui.auth.primerone.EmailFragment
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.util.Constants
import com.tovalue.me.util.extensions.replaceFragmentSafely

class AuthHostActivity : AppCompatActivity() {
	private lateinit var _binding: ActivityAuthHostBinding
	private val viewModel: AuthViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		goToSplash()
		
		_binding = ActivityAuthHostBinding.inflate(layoutInflater)
		setContentView(_binding.root)
	}
	
	private fun setUpView() {
		goToLandingPageFrag()
	}
	
	private fun goToSplash() {
		installSplashScreen().apply {
			var isSplashScreenVisible = true
			setKeepOnScreenCondition { isSplashScreenVisible }
			
			FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
				if (!task.isSuccessful) return@OnCompleteListener
				viewModel.setDeviceToken(task.result)
				
				isSplashScreenVisible = false
			})
			
			if (viewModel.isUserLoggedIn()) {
				startActivity(Intent(this@AuthHostActivity, DashboardActivity::class.java))
				finish()
			} else goToLandingPageFrag()
		}
	}
	
	// change back to landing fragment
	private fun goToLandingPageFrag() {
		replaceFragmentSafely(
			LandingPageFragment(),
			containerViewId = R.id.host_root
		)
	}
	
	public companion object {
		public fun createAuthIntent(context: Context): Intent {
			return Intent(context, AuthHostActivity::class.java).apply {
			
			}
		}
	}
}