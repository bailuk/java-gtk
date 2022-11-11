package examples;

import ch.bailu.gtk.geoclue.AccuracyLevel;
import ch.bailu.gtk.geoclue.Simple;
import ch.bailu.gtk.glib.MainLoop;
import ch.bailu.gtk.type.Str;

/**
 * https://gitlab.freedesktop.org/geoclue/geoclue
 * https://gitlab.freedesktop.org/geoclue/geoclue/-/tree/master/demo
 */
public class GeoclueSample {
    public static void main(String[] args) {
        Simple.newWithThresholds(new Str("geoclue-where-am-i"),
                AccuracyLevel.EXACT, 0, 0, null, (__self, source_object, res, user_data) -> {

                    try {
                        var simple = Simple.newWithThresholdsFinishSimple(res);
                        var client = simple.getClient();
                        client.ref();
                        System.out.println("Client object: %s " + client.getObjectPath());

                        client.onNotify(pspec -> System.out.println("Signal received"));

                        printLocation(simple);

                    } catch (Exception e) {
                        System.err.println("Failed to connect to GeoClue2 service: " + e.getMessage());
                    }

                    __self.unregister();
                }, null);

        new MainLoop(null, false).run();
    }

    private static void printLocation(Simple simple) {
        var location = simple.getLocation();
        System.out.println("New location:");
        System.out.println("Latitude: "  + location.getLatitude());
        System.out.println("Longitude: " + location.getLongitude());
        System.out.println("Accuracy: "  + location.getAccuracy() + "meters");
    }
}
