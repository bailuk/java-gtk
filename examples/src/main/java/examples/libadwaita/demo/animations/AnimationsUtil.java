package examples.libadwaita.demo.animations;

import ch.bailu.gtk.adw.AnimationState;
import ch.bailu.gtk.adw.Easing;
import ch.bailu.gtk.adw.EnumListItem;
import ch.bailu.gtk.gobject.BindingFlags;
import ch.bailu.gtk.graphene.Point;
import ch.bailu.gtk.gsk.Transform;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;

public class AnimationsUtil {


    public static Str getEasingName(EnumListItem item) {
        switch (item.getValue()) {
            case Easing.LINEAR:
                return new Str("Linear");
            case Easing.EASE_IN_QUAD:
                return new Str("Ease-in (Quadratic)");
            case Easing.EASE_OUT_QUAD:
                return new Str("Ease-out (Quadratic)");
            case Easing.EASE_IN_OUT_QUAD:
                return new Str("Ease-in-out (Quadratic)");
            case Easing.EASE_IN_CUBIC:
                return new Str("Ease-in (Cubic)");
            case Easing.EASE_OUT_CUBIC:
                return new Str("Ease-out (Cubic)");
            case Easing.EASE_IN_OUT_CUBIC:
                return new Str("Ease-in-out (Cubic)");
            case Easing.EASE_IN_QUART:
                return new Str("Ease-in (Quartic)");
            case Easing.EASE_OUT_QUART:
                return new Str("Ease-out (Quartic)");
            case Easing.EASE_IN_OUT_QUART:
                return new Str("Ease-in-out (Quartic)");
            case Easing.EASE_IN_QUINT:
                return new Str("Ease-in (Quintic)");
            case Easing.EASE_OUT_QUINT:
                return new Str("Ease-out (Quintic)");
            case Easing.EASE_IN_OUT_QUINT:
                return new Str("Ease-in-out (Quintic)");
            case Easing.EASE_IN_SINE:
                return new Str("Ease-in (Sine)");
            case Easing.EASE_OUT_SINE:
                return new Str("Ease-out (Sine)");
            case Easing.EASE_IN_OUT_SINE:
                return new Str("Ease-in-out (Sine)");
            case Easing.EASE_IN_EXPO:
                return new Str("Ease-in (Exponential)");
            case Easing.EASE_OUT_EXPO:
                return new Str("Ease-out (Exponential)");
            case Easing.EASE_IN_OUT_EXPO:
                return new Str("Ease-in-out (Exponential)");
            case Easing.EASE_IN_CIRC:
                return new Str("Ease-in (Circular)");
            case Easing.EASE_OUT_CIRC:
                return new Str("Ease-out (Circular)");
            case Easing.EASE_IN_OUT_CIRC:
                return new Str("Ease-in-out (Circular)");
            case Easing.EASE_IN_ELASTIC:
                return new Str("Ease-in (Elastic)");
            case Easing.EASE_OUT_ELASTIC:
                return new Str("Ease-out (Elastic)");
            case Easing.EASE_IN_OUT_ELASTIC:
                return new Str("Ease-in-out (Elastic)");
            case Easing.EASE_IN_BACK:
                return new Str("Ease-in (Back)");
            case Easing.EASE_OUT_BACK:
                return new Str("Ease-out (Back)");
            case Easing.EASE_IN_OUT_BACK:
                return new Str("Ease-in-out (Back)");
            case Easing.EASE_IN_BOUNCE:
                return new Str("Ease-in (Bounce)");
            case Easing.EASE_OUT_BOUNCE:
                return new Str("Ease-out (Bounce)");
            case Easing.EASE_IN_OUT_BOUNCE:
                return new Str("Ease-in-out (Bounce)");
            default:
                return Str.NULL;
        }
    }


    public static void timedAnimationMeasure(Widget widget, int orientation, int forSize, Int minimum, Int natural, Int minimumBaseline, Int naturalBaseline) {
        var child = widget.getFirstChild();
        if (child.isNotNull()) {
            child.measure(orientation, forSize, minimum, natural, minimumBaseline, naturalBaseline);
        }
    }

    public static Str getPlayPauseIconName(int timedState, int springState) {
        var playing = timedState == AnimationState.PLAYING || springState == AnimationState.PLAYING;
        return playing ? new Str("media-playback-pause-symbolic") : new Str("media-playback-start-symbolic");
    }

    public static boolean timedAnimationCanReset(int timedState, int springState) {
        return timedState != AnimationState.IDLE || springState != AnimationState.IDLE;
    }

    public static boolean timedAnimationCanSkip(int timedState, int springState) {
        return timedState != AnimationState.FINISHED && springState != AnimationState.FINISHED;
    }

    public static void onTimedAnimation(Widget target) {
        target.queueAllocate();
    }

    public static void bindProperty(long sourcePointer, String sourceProperty, long targetPointer, String targetProperty) {
        var source = new Widget(Pointer.toCPointer(sourcePointer));
        var target = new Pointer(Pointer.toCPointer(targetPointer));
        source.bindProperty(sourceProperty, target, targetProperty, BindingFlags.SYNC_CREATE | BindingFlags.BIDIRECTIONAL);
    }

    public static Transform translate(Point point) {
        // Whatever this does
        return new Transform(CPointer.NULL).translate(point);
    }

    public static Point getOffsetPoint(int offset) {
        var point = new Point();
        point.setFieldX(offset);
        point.setFieldY(0);
        return point;
    }

    public static int getMeasuredWidth(Widget child) {
        final Int width = new Int();
        child.measure(Orientation.HORIZONTAL,-1, width, Int.NULL, Int.NULL, Int.NULL);
        int result = width.get();
        width.destroy();
        return result;
    }
}
