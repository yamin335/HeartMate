package com.tovalue.me.helper

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

class MomensityBingoApp : Application() {
	override fun onCreate() {
		super.onCreate()
		
		applicationMomensityBingo = this
		preferencesManager = PreferencesManager.getInstance(this)

		leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
		exoDatabaseProvider = StandaloneDatabaseProvider(this)
		simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)

	}
	
	companion object {
		@JvmField
        var preferencesManager: PreferencesManager? = null
		var applicationMomensityBingo: MomensityBingoApp? = null

		lateinit var simpleCache: SimpleCache
		const val exoPlayerCacheSize: Long = 90 * 1024 * 1024
		lateinit var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor
		lateinit var exoDatabaseProvider: StandaloneDatabaseProvider

	}
}