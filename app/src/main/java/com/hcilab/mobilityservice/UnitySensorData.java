package com.hcilab.mobilityservice;

import org.json.JSONException;
import org.json.JSONObject;

public class UnitySensorData {
    private JSONObject sensorData;
    private JSONObject accelerationObj;
    private JSONObject rvObj;
    private JSONObject laObj;
    private JSONObject gyroObj;

    public UnitySensorData() {
        this.sensorData = new JSONObject();
        this.accelerationObj = new JSONObject();
        this.rvObj = new JSONObject();
        this.laObj = new JSONObject();
        this.gyroObj = new JSONObject();
    }

    public void initialSetup() throws JSONException {
        accelerationObj.put("x", 0);
        accelerationObj.put("y", 0);
        accelerationObj.put("z", 0);
        rvObj.put("x", 0);
        rvObj.put("y", 0);
        rvObj.put("z", 0);
        laObj.put("x", 0);
        laObj.put("y", 0);
        laObj.put("z", 0);
        gyroObj.put("x", 0);
        gyroObj.put("y", 0);
        gyroObj.put("z", 0);
    }

    public void setAcceleration(float[] vals) throws JSONException {
        accelerationObj.put("x", vals[0]);
        accelerationObj.put("y", vals[1]);
        accelerationObj.put("z", vals[2]);
    }

    public void setRotationVector(float[] vals) throws JSONException {
        rvObj.put("x", vals[0]);
        rvObj.put("y", vals[1]);
        rvObj.put("z", vals[2]);
    }

    public void setLinearAcceleration(float[] vals) throws JSONException {
        laObj.put("x", vals[0]);
        laObj.put("y", vals[1]);
        laObj.put("z", vals[2]);
    }

    public void setGyro(float[] vals) throws JSONException {
        gyroObj.put("x", vals[0]);
        gyroObj.put("y", vals[1]);
        gyroObj.put("z", vals[2]);
    }

    public void stopAcceleration()throws JSONException {
        accelerationObj.put("x", 0);
        accelerationObj.put("y", 0);
        accelerationObj.put("z", 0);
    }

    public void stopRotationVector() throws JSONException{
        rvObj.put("x", 0);
        rvObj.put("y", 0);
        rvObj.put("z", 0);
    }

    public void stopLinearAcceleration() throws JSONException {
        laObj.put("x", 0);
        laObj.put("y", 0);
        laObj.put("z", 0);
    }

    public void stopGyro() throws JSONException {
        gyroObj.put("x", 0);
        gyroObj.put("y", 0);
        gyroObj.put("z", 0);
    }

    public JSONObject getSensorData() throws JSONException {
        return build();
    }

    public void setSensorData(JSONObject sensorData) {
        this.sensorData = sensorData;
    }

    public String toJsonString() {
        try {
            build();
        }catch (JSONException e)   {
            e.printStackTrace();
        }
      return sensorData.toString();
    }

    private JSONObject build() throws JSONException {
        sensorData.put("Acceleration", accelerationObj);
        sensorData.put("LinearAcceleration", laObj);
        sensorData.put("RotationVector", rvObj);
        sensorData.put("Gyroscope", gyroObj);
        return sensorData;
    }
}
