package Modules;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by cheng ying on 2016/10/8.
 */
public interface DirectionFinderListener {
    void onConnected(@Nullable Bundle bundle);

    void onLocationChanged(Location location);

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}