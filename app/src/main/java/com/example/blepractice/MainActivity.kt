package com.example.blepractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

//    val BLE_UUID = "0x1816"
//    val SERVICE_UUID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gatt.disconnect()
        gatt.close()
    }
}

    fun conenct() {
        val gatt = device.connectGatt(context, autoConnect = false).let { result ->
            when (result) {
                is Success -> result.gatt
                is Canceled -> throw IllegalStateException("接続キャンセル", result.cause)
                is Failure -> IllegalStateException("接続失敗" result . cause)
            }
        }

        if (gatt.discoverServcices() != BluetoothGatt.GATT_SUCCESS) {
        }

        val characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(BLE_UUID)

        val result = gatt.readCharacteristic(characteristic)
        if(result.status == BluetoothGatt.GATT_SUCCESS) {
            println("result.value = ${result.value}")
        }  else {
            //キャラクタリスティックの読み取りに失敗したとき
        }
    }

    suspend fun BluetoothDevice.logGattServices(tags: String = "BleGattCoroutines"){
        val devicesConnection = GattConnection(bluetoothDevice = this@logGattServices)
        try {
            devicesConnection.connect()
            val gattServices = devicesConnection.discoverServices()
            gattServices.forEach {
                it.characteristics.forEach {
                    try {
                        devicesConnection.readCharacteristic(it)
                    } catch (e: Exception) {
                        log.e(tag, "s",e)
                    }
                }
                Log.d(tag,it.print(printCharacteristics = true))
            }
        } finally {
            device connection.close()
        }
    }

    companion object {
        val BLE_UUID = "0x1816"
        val SERVICE_UUID = ""
        val scanFilter = ScanFilter.Builder().setDeviceName("DuoTrap S").build()
        private var mBluetoothManager: BluetoothManager? = null
        private var mBluetoothAdapter: BluetoothAdapter? = null
        private val mBluetoothLeScanner: BluetoothLeScanner? = null
        private val mScanCallback: ScanCallback? = null
    }
}