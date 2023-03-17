package examples.libadwaita.demo_official;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Animation;
import ch.bailu.gtk.adw.AnimationState;
import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.CallbackAnimationTarget;
import ch.bailu.gtk.adw.Easing;
import ch.bailu.gtk.adw.EnumListItem;
import ch.bailu.gtk.adw.SpringAnimation;
import ch.bailu.gtk.adw.SpringParams;
import ch.bailu.gtk.adw.TimedAnimation;
import ch.bailu.gtk.gobject.BindingFlags;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.graphene.Point;
import ch.bailu.gtk.gsk.Transform;
import ch.bailu.gtk.gtk.CustomLayout;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.SpinButton;
import ch.bailu.gtk.gtk.Stack;
import ch.bailu.gtk.gtk.TextDirection;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageAnimations extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageAnimations.class.getSimpleName());
    private final static long PARENT_TYPE = Bin.getTypeID();
    private final static int PARENT_INSTANCE_SIZE = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private final static int PROP_TIMED_ANIMATION = 1;
    private final static int PROP_SPRING_ANIMATION = 2;

    private final static Str PROP_TIMED_ANIMATION_NAME = new Str("timed-animation");
    private final static Str PROP_SPRING_ANIMATION_NAME = new Str("spring-animation");

    public AdwDemoPageAnimations(CPointer self) {
        super(self);
        instance = new Instance(self.getCPointer());
    }

    public AdwDemoPageAnimations(TypeInstance self) {
        super(self.cast());
        initTemplate();
        instance = new Instance(getCPointer());
        var target = new CallbackAnimationTarget((__self, value, user_data) -> onTimedAnimation(new Widget(user_data.cast())), toPointer(instance.timed_animation_sample), null);

        var timedAnimation = new TimedAnimation(
                new Widget(toCPointer(instance.timed_animation_sample)),
                0, 1, 100, target
        );
        instance.timed_animation = timedAnimation.getCPointer();
        instance.writeField("timed_animation");

        instance.spring_animation = new SpringAnimation(
                new Widget(toCPointer(instance.timed_animation_sample)),
                0, 1,
                new SpringParams(10d,1d,100d), target
        ).getCPointer();
        instance.writeField("spring_animation");

        notifySpringParamsChange();

        bindProperty (instance.timed_animation_repeat_count, "value",
                instance.timed_animation, "repeat-count");
        bindProperty (instance.timed_animation_reverse, "state",
                instance.timed_animation, "reverse");
        bindProperty (instance.timed_animation_alternate, "state",
                instance.timed_animation, "alternate");
        bindProperty (instance.timed_animation_duration, "value",
                instance.timed_animation, "duration");
        bindProperty (instance.timed_animation_easing, "selected",
                instance.timed_animation, "easing");

        bindProperty (instance.spring_animation_velocity, "value",
                instance.spring_animation, "initial_velocity");
        bindProperty (instance.spring_animation_epsilon, "value",
                instance.spring_animation, "epsilon");
        bindProperty (instance.spring_animation_clamp_switch, "active",
                instance.spring_animation, "clamp");


        timedAnimation.setEasing(Easing.EASE_IN_OUT_CUBIC);
        //TODO: adw_animation_set_follow_enable_animations_setting (instance.timed_animation, FALSE);
        //TODO: adw_animation_set_follow_enable_animations_setting (instance.spring_animation, FALSE);

        notifyByPspec(specTimedAnimation);
        notifyByPspec(specSpringAnimation);

        var manager = new CustomLayout(null,
                (__self, widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline) -> timedAnimationMeasure(widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline),
                (__self, widget, width, height, baseline) -> timedAnimationAllocate(widget, width, height, baseline));

        new Widget(toCPointer(instance.timed_animation_sample)).setLayoutManager(manager);
        new Widget(toCPointer(instance.timed_animation_button_box)).setDirection(TextDirection.LTR);
    }


    private static void bindProperty(long sourcePointer, String sourceProperty, long targetPointer, String targetProperty) {
        var source = new Widget(toCPointer(sourcePointer));
        var target = new Pointer(toCPointer(targetPointer));
        source.bindProperty(sourceProperty, target, targetProperty, BindingFlags.SYNC_CREATE | BindingFlags.BIDIRECTIONAL);
    }

    @Structure.FieldOrder({"parent",
            "animation_preferences_stack",
            "timed_animation",
            "timed_animation_sample",
            "timed_animation_button_box",
            "timed_animation_repeat_count",
            "timed_animation_reverse",
            "timed_animation_alternate",
            "timed_animation_duration",
            "timed_animation_easing",
            "spring_animation",
            "spring_animation_velocity",
            "spring_animation_damping",
            "spring_animation_mass",
            "spring_animation_stiffness",
            "spring_animation_epsilon",
            "spring_animation_clamp_switch"
    })
    public static class Instance extends Structure {
        public Instance(long self) {
            super(toJnaPointer(self));
            read();
        }

        public byte[] parent = new byte[PARENT_INSTANCE_SIZE];
        public long animation_preferences_stack;
        public long timed_animation;
        public long timed_animation_sample;
        public long timed_animation_button_box;
        public long timed_animation_repeat_count;
        public long timed_animation_reverse;
        public long timed_animation_alternate;
        public long timed_animation_duration;
        public long timed_animation_easing;
        public long spring_animation;
        public long spring_animation_velocity;
        public long spring_animation_damping;
        public long spring_animation_mass;
        public long spring_animation_stiffness;
        public long spring_animation_epsilon;
        public long spring_animation_clamp_switch;
    }

    private final Instance instance;

    private static ParamSpec specTimedAnimation;
    private static ParamSpec specSpringAnimation;

    private static long type = 0;
    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 16 * 8, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                objectClass.overridePropertyAccess(
                        (object, property_id, value, pspec) -> new AdwDemoPageAnimations(toCPointer(object)).getProperty(property_id, new Value(toCPointer(value))),
                        (object, property_id, value, pspec) -> new AdwDemoPageAnimations(toCPointer(object)).setProperty(property_id, new Value(toCPointer(value)))
                );

                objectClass.overrideDispose(pointer -> new AdwDemoPageAnimations(toCPointer(pointer)).dispose());

                specTimedAnimation = Gobject.paramSpecObject(PROP_TIMED_ANIMATION_NAME, Str.NULL, Str.NULL, Animation.getTypeID(), ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                specSpringAnimation = Gobject.paramSpecObject(PROP_SPRING_ANIMATION_NAME, Str.NULL, Str.NULL, Animation.getTypeID(), ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);

                objectClass.installProperty(PROP_TIMED_ANIMATION, specTimedAnimation);
                objectClass.installProperty(PROP_SPRING_ANIMATION, specSpringAnimation);

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-animations.ui");

                widgetClass.bindTemplateChildFull("animation_preferences_stack", true, PARENT_INSTANCE_SIZE);
                widgetClass.bindTemplateChildFull("timed_animation_sample", true, PARENT_INSTANCE_SIZE + 2 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_button_box", true, PARENT_INSTANCE_SIZE + 3 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_repeat_count", true, PARENT_INSTANCE_SIZE + 4 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_reverse", true, PARENT_INSTANCE_SIZE + 5 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_alternate", true, PARENT_INSTANCE_SIZE + 6 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_duration", true, PARENT_INSTANCE_SIZE + 7 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_easing", true, PARENT_INSTANCE_SIZE + 8 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_velocity", true, PARENT_INSTANCE_SIZE + 10 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_damping", true, PARENT_INSTANCE_SIZE + 11 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_mass", true, PARENT_INSTANCE_SIZE + 12 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_stiffness", true, PARENT_INSTANCE_SIZE + 13 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_epsilon", true, PARENT_INSTANCE_SIZE + 14 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_clamp_switch", true, PARENT_INSTANCE_SIZE + 15 * 8);

                widgetClass.bindTemplateCallback("animations_easing_name", new Callback() {
                    public long invoke(long item, long user_data) {
                        return animationsEasingName(new EnumListItem(toCPointer(item))).getCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_reset", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(toCPointer(self)).timedAnimationReset();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_play_pause", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(toCPointer(self)).timedAnimationPlayPause();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_skip", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(toCPointer(self)).timedAnimationSkip();
                    }
                });
                widgetClass.bindTemplateCallback("get_play_pause_icon_name", new Callback() {
                    public long invoke(long user_data, int timed_state, int spring_state) {
                        return getPlayPauseIconName(timed_state, spring_state).getCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_can_reset", new Callback() {
                    public boolean invoke(long user_data, int timed_state, int spring_state) {
                        return timedAnimationCanReset(timed_state, spring_state);
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_can_skip", new Callback() {
                    public boolean invoke(long user_data, int timed_state, int spring_state) {
                        return timedAnimationCanSkip(timed_state, spring_state);
                    }
                });
                widgetClass.bindTemplateCallback("notify_spring_params_change", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(toCPointer(self)).notifySpringParamsChange();
                    }
                });
            }, (__self, self, g_class) -> new AdwDemoPageAnimations(self));
        }
        return type;
    }

    private Animation getCurrentAnimation() {
        var stack = new Stack(toCPointer(instance.animation_preferences_stack));
        var currentAnimation = stack.getVisibleChildName().toString();

        if (currentAnimation.equals("Timed")) {
            return new Animation(toCPointer(instance.timed_animation));
        }
        return new Animation((toCPointer(instance.spring_animation)));
    }

    static Str animationsEasingName (EnumListItem item) {
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

    public static void timedAnimationAllocate(Widget widget, int width, int height, int baseline) {
        new AdwDemoPageAnimations(widget.getAncestor(getTypeID()).cast()).getTimedAnimationAllocate(widget, width, height, baseline);
    }

    private void getTimedAnimationAllocate(Widget widget, int width, int height, int baseline) {
        var child = widget.getFirstChild();
        var animation = getCurrentAnimation();


        if (child.isNotNull()) {
            Int childWidth = new Int();

            var progress = animation.getValue();
            child.measure(Orientation.HORIZONTAL,-1, childWidth, Int.NULL, Int.NULL, Int.NULL);

            var offset = (int) (width - childWidth.get() * (progress - 0.5));

            var point = new Point();
            point.setFieldX(offset);
            point.setFieldY(0);

            child.allocate(width, height, baseline, new Transform(CPointer.NULL).translate(point)); // <-- WTF
            //point.destroy();
            //childWidth.destroy();
        }
    }

    private void timedAnimationReset() {
        new Animation(toCPointer(instance.timed_animation)).reset();
        new Animation(toCPointer(instance.spring_animation)).reset();
    }

    private void timedAnimationPlayPause() {
        var animation = getCurrentAnimation();
        switch (animation.getState()) {
            case AnimationState.IDLE:
            case AnimationState.FINISHED:
                animation.play();
                break;
            case AnimationState.PAUSED:
                animation.resume();
                break;
            case AnimationState.PLAYING:
                animation.pause();
                break;
        }
    }

    private void timedAnimationSkip() {
        new Animation(toCPointer(instance.timed_animation)).skip();
        new Animation(toCPointer(instance.spring_animation)).skip();
    }

    private static Str getPlayPauseIconName(int timedState, int springState) {
        var playing = timedState == AnimationState.PLAYING || springState == AnimationState.PLAYING;
        return playing ? new Str("media-playback-pause-symbolic") : new Str("media-playback-start-symbolic");
    }

    private static boolean timedAnimationCanReset(int timedState, int springState) {
        return timedState != AnimationState.IDLE || springState != AnimationState.IDLE;
    }

    private static boolean timedAnimationCanSkip(int timedState, int springState) {
        return timedState != AnimationState.FINISHED && springState != AnimationState.FINISHED;
    }

    private static void onTimedAnimation(Widget target) {
        System.out.println("onTimedAnimation");
        target.queueAllocate();
    }

    private void notifySpringParamsChange() {
        var springParams = new SpringParams(
                new SpinButton(toCPointer(instance.spring_animation_damping)).getValue(),
                new SpinButton(toCPointer(instance.spring_animation_mass)).getValue(),
                new SpinButton(toCPointer(instance.spring_animation_stiffness)).getValue());

        new SpringAnimation(toCPointer(instance.spring_animation)).setSpringParams(springParams);
        springParams.unref();
    }


    private void getProperty(int propId, Value value) {
        if (propId == PROP_TIMED_ANIMATION) {
            value.setObject(toPointer(instance.timed_animation));
        } else if (propId == PROP_SPRING_ANIMATION) {
            value.setObject(toPointer(instance.spring_animation));
        }
    }

    private void setProperty(int propId, Value value) {
        if (propId == PROP_TIMED_ANIMATION) {
            instance.timed_animation = value.getObject().getCPointer(); // Why does C depend on unreadable macros for banal things like this? (#define g_set_object ...)
        } else if (propId == PROP_SPRING_ANIMATION) {
            instance.spring_animation = value.getObject().getCPointer();
        }
    }

    private void dispose() {
        new Animation(toCPointer(instance.timed_animation)).unref(); // Why #define g_clear_pointer ???
        new Animation(toCPointer(instance.spring_animation)).unref();

        // TODO: Call super
        //G_OBJECT_CLASS (adw_demo_page_animations_parent_class)->dispose (object);
    }
}
