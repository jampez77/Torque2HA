package org.prowl.torque.remote

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log

class TorqueService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate()")
    }

    override fun onBind(intent: Intent): IBinder? {
        // Return the interface
        return binder
    }

    private val binder: ITorqueService.Stub = object : ITorqueService.Stub() {
        @kotlin.Throws(RemoteException::class)
        override fun getVersion(): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getValueForPid(pid: Long, triggersDataRefresh: Boolean): Float {
            return 0f
        }

        @kotlin.Throws(RemoteException::class)
        override fun getDescriptionForPid(pid: Long): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun getShortNameForPid(pid: Long): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun getUnitForPid(pid: Long): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun getMinValueForPid(pid: Long): Float {
            return 0f
        }

        @kotlin.Throws(RemoteException::class)
        override fun getMaxValueForPid(pid: Long): Float {
            return 0f
        }

        @kotlin.Throws(RemoteException::class)
        override fun getListOfActivePids(): LongArray {
            return LongArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getListOfECUSupportedPids(): LongArray {
            return LongArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getListOfAllPids(): LongArray {
            return LongArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun hasFullPermissions(): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun sendCommandGetResponse(header: String, command: String): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPreferredUnit(unit: String): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun setPIDData(
                name: String, shortName: String, unit: String, max: Float, min: Float,
                value: Float,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun isConnectedToECU(): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun setDebugTestMode(activateTestMode: Boolean): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun getVehicleProfileInformation(): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun storeInProfile(key: String, value: String, saveToFileNow: Boolean): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun retrieveProfileData(key: String): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun getDataErrorCount(): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPIDReadSpeed(): Double {
            return 0.0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getConfiguredSpeed(): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun isFileLoggingEnabled(): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun isWebLoggingEnabled(): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun getNumberOfLoggedItems(): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getUpdateTimeForPID(pid: Long): Long {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getScaleForPid(pid: Long): Float {
            return 0f
        }

        @kotlin.Throws(RemoteException::class)
        override fun translate(originalText: String): String? {
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun sendPIDData(
                pluginName: String, name: Array<String>, shortName: Array<String>,
                modeAndPID: Array<String>, equation: Array<String>, minValue: FloatArray, maxValue: FloatArray,
                units: Array<String>, header: Array<String>,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun setPIDInformation(
                name: String, shortName: String, unit: String, max: Float,
                min: Float, value: Float, stringValue: String,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun listAllPIDs(): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun listActivePIDs(): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun listECUSupportedPIDs(): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPIDValues(pidsToRetrieve: Array<String>): FloatArray {
            return FloatArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPIDInformation(pidIDs: Array<String>): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPIDUpdateTime(pidIDs: Array<String>): LongArray {
            return LongArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getValueForPids(pids: LongArray): FloatArray {
            return FloatArray(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun sendPIDDataPrivate(
                pluginName: String, name: Array<String>, shortName: Array<String>,
                modeAndPID: Array<String>, equation: Array<String>, minValue: FloatArray, maxValue: FloatArray,
                units: Array<String>, header: Array<String>,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun requestExclusiveLock(pluginName: String): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun releaseExclusiveLock(
                pluginName: String,
                torqueMustReInitializeTheAdapter: Boolean,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun sendPIDDataV2(
                pluginName: String, name: Array<String>, shortName: Array<String>,
                modeAndPID: Array<String>, equation: Array<String>, minValue: FloatArray, maxValue: FloatArray,
                units: Array<String>, header: Array<String>, startDiagnostic: Array<String>, stopDiagnostic: Array<String>,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun sendPIDDataPrivateV2(
                pluginName: String, name: Array<String>, shortName: Array<String>,
                modeAndPID: Array<String>, equation: Array<String>, minValue: FloatArray, maxValue: FloatArray,
                units: Array<String>, header: Array<String>, startDiagnostic: Array<String>, stopDiagnostic: Array<String>,
        ): Boolean {
            return false
        }

        @kotlin.Throws(RemoteException::class)
        override fun getPIDRawResponse(OBDCommand: String): Array<String?> {
            return arrayOfNulls(0)
        }

        @kotlin.Throws(RemoteException::class)
        override fun getProtocolNumber(): Int {
            return 0
        }

        @kotlin.Throws(RemoteException::class)
        override fun getProtocolName(): String?{
            return null
        }

        @kotlin.Throws(RemoteException::class)
        override fun listAllPIDsIncludingDetectedPIDs(): Array<String?> {
            return arrayOfNulls(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    companion object {
        private const val TAG = "GetDeviceInfoService"
    }
}
