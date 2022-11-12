package examples.geoclue;

import ch.bailu.gtk.geoclue.AccuracyLevel;
import ch.bailu.gtk.geoclue.Simple;
import ch.bailu.gtk.glib.DateTime;
import ch.bailu.gtk.glib.MainLoop;
import ch.bailu.gtk.type.Int64;
import ch.bailu.gtk.type.Str;

/**
 * <a href="https://gitlab.freedesktop.org/geoclue/geoclue">GitLab: GeoClue</a>
 * <a href="https://gitlab.freedesktop.org/geoclue/geoclue/-/tree/master/demo">GitLab: GeoClue demos</a>
 */
public class WhereAmI {

    private final static Int64 sec = new Int64();
    private final static Int64 usec = new Int64();
    private final static Str ttFormat = new Str("(tt)");
    private final static Str dateFormatString = new Str("%c (%s seconds since the Epoch)");

    public static void main(String[] args) {
        try {
            Simple.newWithThresholds(new Str("geoclue-where-am-i"),
                    AccuracyLevel.EXACT, 0, 0, null, (__self, source_object, res, user_data) -> {

                        try {
                            var simple = Simple.newWithThresholdsFinishSimple(res);
                            var client = simple.getClient();
                            client.ref();
                            System.out.println("Client object: " + client.getObjectPath());

                            client.onNotify(pspec -> {
                                System.out.println();
                                System.out.println("__");
                                System.out.println("Signal received: " + pspec.getName());
                                printLocation(simple);
                            });
                            printLocation(simple);

                        } catch (Exception e) {
                            printError("Failed to connect to GeoClue2 service", e);
                            System.exit(-1);
                        }
                    }, null);

            new MainLoop(null, false).run();
        } catch (Exception e) {
            printError("Failed to initialize GeoClue2", e);
            System.exit(-1);
        }
    }

    private static void printError(String message, Exception e) {
        System.err.println(message);
        if (e.getLocalizedMessage() != null) {
            System.err.println(e.getLocalizedMessage());
        } else if (e.getMessage() != null) {
            System.err.println(e.getMessage());
        }
    }

    private static void printLocation(Simple simple) {
        var location = simple.getLocation();
        System.out.println();
        System.out.println("__");
        System.out.println("Latitude: "  + location.getLatitude());
        System.out.println("Longitude: " + location.getLongitude());
        System.out.println("Accuracy: "  + Math.round(location.getAccuracy()) + " m");

        var speed = location.getSpeed();
        if (speed > 0) System.out.println("Speed: " + speed);

        var heading = location.getHeading();
        if (heading > 0) System.out.println("Heading: " + heading);

        var descriptionStr = location.getDescription();
        if (descriptionStr.isNotNull()) {
            var description = descriptionStr.toString();
            if (!description.isBlank()) System.out.println("Description: " + description);
        }

        var timestamp = location.getTimestamp();
        if (timestamp.isNotNull()) {

            // Pointer conversion because java-gtk only supports Object in varargs
            timestamp.get(ttFormat, sec.getCPointer(), usec.getCPointer());

            var dateTime = DateTime.newFromUnixLocalDateTime(sec.get());
            var formattedDate = dateTime.format(dateFormatString);
            System.out.println(formattedDate);
            formattedDate.destroy();
        }
    }
}
