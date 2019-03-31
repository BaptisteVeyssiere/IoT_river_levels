package jsplink;

public class DBDevices {
    public String getMbed_hex() {
        return mbed_hex;
    }

    public void setMbed_hex(String mbed_hex) {
        this.mbed_hex = mbed_hex;
    }

    String mbed_hex;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    int code;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    float latitude;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    float longitude;
}
