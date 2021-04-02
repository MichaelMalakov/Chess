package com.vadmax.chess

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //initKoin()
        initLogger()
        //preventAndroid7SSLCrash()
    }

    private fun initLogger() {
        //if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

//    private fun initKoin() {
//        startKoin {
//            androidContext(this@App)
//            fragmentFactory()
//            modules(
//                listOf(
//
//                )
//            )
//        }
//    }

//    private fun preventAndroid7SSLCrash() {
//        try {
//            ProviderInstaller.installIfNeeded(applicationContext)
//            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
//            sslContext.init(null, null, null)
//            sslContext.createSSLEngine()
//        } catch (e: GooglePlayServicesRepairableException) {
//            e.printStackTrace()
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyManagementException) {
//            e.printStackTrace()
//        }
//    }

//    companion object {
//        const val APP_TIME_PATTERN = "dd.MM.yyyy"
//    }
}
