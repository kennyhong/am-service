package com.hcilab.mobilityservice;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.app.NotificationManagerCompat;

import com.epson.moverio.hardware.sensor.SensorData;
import com.epson.moverio.hardware.sensor.SensorDataListener;
import com.epson.moverio.hardware.sensor.SensorManager;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext = null;

    private SensorManager mSensorManager = null;

    public static UnitySensorData sensorData = new UnitySensorData();

    private CheckBox mCheckBox_acc = null;
    private CheckBox mCheckBox_gyro = null;
    private CheckBox mCheckBox_la = null;
    private CheckBox mCheckBox_rv = null;
    private TextView mTextView_accResult = null;
    private TextView mTextView_gyroResult = null;
    private TextView mTextView_laResult = null;
    private TextView mTextView_rvResult = null;
    private SensorDataListener mSensorDataListener_acc = null;
    private SensorDataListener mSensorDataListener_gyro = null;
    private SensorDataListener mSensorDataListener_la = null;
    private SensorDataListener mSensorDataListener_rv = null;

    public static boolean sendData = false;
    public static String accXVal = "nil";

    private final Handler handler = new Handler();

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private NotificationManagerCompat notificationManager;

    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        mSensorManager = new SensorManager(mContext);

        myIntent = new Intent(this, BroadcastService.class);
        myIntent.putExtra("inputExtra" , "SensorService");
        startForegroundService(myIntent);
        Log.i("Kenny", "request for start service placed");

        try {
            sensorData.initialSetup();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        // Accelerometer sensor.
        mTextView_accResult = (TextView) findViewById(R.id.textView_accResult);
        mCheckBox_acc = (CheckBox) findViewById(R.id.checkBox_acc);
        mCheckBox_acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendData = true;
                    mSensorDataListener_acc = new SensorDataListener() {
                        @Override
                        public void onSensorDataChanged(final SensorData data) {
                            handler.post(new Runnable() {
                                public void run() {
                                    mTextView_accResult.setText(String.format("%.4f", data.values[0]) + "," + String.format("%.4f", data.values[1]) + "," + String.format("%.4f", data.values[2]));
                                    try {
                                        sensorData.setAcceleration(data.values);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    try {
                        mSensorManager.open(SensorManager.TYPE_ACCELEROMETER, mSensorDataListener_acc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mSensorManager.close(mSensorDataListener_acc);
                    mSensorDataListener_acc = null;
                    sendData = false;
                    try {
                        sensorData.stopAcceleration();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Gyroscope sensor.
        mTextView_gyroResult = (TextView) findViewById(R.id.textView_gyroResult);
        mCheckBox_gyro = (CheckBox) findViewById(R.id.checkBox_gyro);
        mCheckBox_gyro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSensorDataListener_gyro = new SensorDataListener() {
                        @Override
                        public void onSensorDataChanged(final SensorData data) {
                            handler.post(new Runnable() {
                                public void run() {
                                    mTextView_gyroResult.setText(String.format("%.4f", data.values[0]) + "," + String.format("%.4f", data.values[1]) + "," + String.format("%.4f", data.values[2]));
                                    try {
                                        sensorData.setGyro(data.values);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    try {
                        mSensorManager.open(SensorManager.TYPE_GYROSCOPE, mSensorDataListener_gyro);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mSensorManager.close(mSensorDataListener_gyro);
                    mSensorDataListener_gyro = null;
                    try {
                        sensorData.stopGyro();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Linear accelerometer sensor.
        mTextView_laResult = (TextView) findViewById(R.id.textView_laResult);
        mCheckBox_la = (CheckBox) findViewById(R.id.checkBox_la);
        mCheckBox_la.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSensorDataListener_la = new SensorDataListener() {
                        @Override
                        public void onSensorDataChanged(final SensorData data) {
                            handler.post(new Runnable() {
                                public void run() {
                                    mTextView_laResult.setText(String.format("%.4f", data.values[0]) + "," + String.format("%.4f", data.values[1]) + "," + String.format("%.4f", data.values[2]));
                                    try {
                                        sensorData.setLinearAcceleration(data.values);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    try {
                        mSensorManager.open(SensorManager.TYPE_LINEAR_ACCELERATION, mSensorDataListener_la);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        sensorData.stopLinearAcceleration();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSensorManager.close(mSensorDataListener_la);
                    mSensorDataListener_la = null;
                }
            }
        });

        // Rotation vector sensor.
        mTextView_rvResult = (TextView) findViewById(R.id.textView_rvResult);
        mCheckBox_rv = (CheckBox) findViewById(R.id.checkBox_rv);
        mCheckBox_rv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSensorDataListener_rv = new SensorDataListener() {
                        @Override
                        public void onSensorDataChanged(final SensorData data) {
                            handler.post(new Runnable() {
                                public void run() {
                                    mTextView_rvResult.setText(String.format("%.4f", data.values[0]) + "," + String.format("%.4f", data.values[1]) + "," + String.format("%.4f", data.values[2]) + "," + String.format("%.4f", data.values[3]));
                                    try {
                                        sensorData.setRotationVector(data.values);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    try {
                        mSensorManager.open(SensorManager.TYPE_ROTATION_VECTOR, mSensorDataListener_rv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mSensorManager.close(mSensorDataListener_rv);
                    mSensorDataListener_rv = null;
                    try {
                        sensorData.stopRotationVector();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
