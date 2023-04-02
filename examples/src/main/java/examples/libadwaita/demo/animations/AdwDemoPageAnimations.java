package examples.libadwaita.demo.animations;

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
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.GobjectConstants;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.ObjectClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.ParamFlags;
import ch.bailu.gtk.gobject.ParamSpec;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.CustomLayout;
import ch.bailu.gtk.gtk.SpinButton;
import ch.bailu.gtk.gtk.Stack;
import ch.bailu.gtk.gtk.TextDirection;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageAnimations extends Bin {
    private final static Str  TYPE_NAME = new Str(AdwDemoPageAnimations.class.getSimpleName());
    private final static long PARENT_TYPE = Bin.getTypeID();
    private final static int  OFFSET = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private final static int  PROP_TIMED_ANIMATION = 1;
    private final static int  PROP_SPRING_ANIMATION = 2;

    private final static Str  PROP_TIMED_ANIMATION_NAME  = new Str("timed-animation");
    private final static Str  PROP_SPRING_ANIMATION_NAME = new Str("spring-animation");

    public AdwDemoPageAnimations(PointerContainer self) {
        super(self);
        instance = new Instance(self.asCPointer());
    }

    public AdwDemoPageAnimations(TypeInstance self) {
        super(self.cast());
        initTemplate();
        instance = new Instance(asCPointer());
        var target = new CallbackAnimationTarget((__self, value, user_data) -> AnimationsUtil.onTimedAnimation(new Widget(user_data.cast())), asPointer(instance.timed_animation_sample), null);

        var timedAnimation = new TimedAnimation(
                new Widget(cast(instance.timed_animation_sample)),
                0, 1, 100, target
        );
        instance.timed_animation = timedAnimation.asCPointer();
        instance.writeField("timed_animation");

        instance.spring_animation = new SpringAnimation(
                new Widget(cast(instance.timed_animation_sample)),
                0, 1,
                SpringParams.newFullSpringParams(10,1,100), target
        ).asCPointer();
        instance.writeField("spring_animation");

        notifySpringParamsChange();

        AnimationsUtil.bindProperty (instance.timed_animation_repeat_count, "value",
                instance.timed_animation, "repeat-count");
        AnimationsUtil.bindProperty (instance.timed_animation_reverse, "state",
                instance.timed_animation, "reverse");
        AnimationsUtil.bindProperty (instance.timed_animation_alternate, "state",
                instance.timed_animation, "alternate");
        AnimationsUtil.bindProperty (instance.timed_animation_duration, "value",
                instance.timed_animation, "duration");
        AnimationsUtil.bindProperty (instance.timed_animation_easing, "selected",
                instance.timed_animation, "easing");

        AnimationsUtil.bindProperty (instance.spring_animation_velocity, "value",
                instance.spring_animation, "initial_velocity");
        AnimationsUtil.bindProperty (instance.spring_animation_epsilon, "value",
                instance.spring_animation, "epsilon");
        AnimationsUtil.bindProperty (instance.spring_animation_clamp_switch, "active",
                instance.spring_animation, "clamp");


        timedAnimation.setEasing(Easing.EASE_IN_OUT_CUBIC);
        //TODO: adw_animation_set_follow_enable_animations_setting (instance.timed_animation, FALSE);
        //TODO: adw_animation_set_follow_enable_animations_setting (instance.spring_animation, FALSE);

        notifyByPspec(specTimedAnimation);
        notifyByPspec(specSpringAnimation);

        var manager = new CustomLayout(null,
                (__self, widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline) -> AnimationsUtil.timedAnimationMeasure(widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline),
                (__self, widget, width, height, baseline) -> new AdwDemoPageAnimations(widget.getAncestor(getTypeID()).cast()).getTimedAnimationAllocate(widget, width, height, baseline));

        new Widget(cast(instance.timed_animation_sample)).setLayoutManager(manager);
        new Widget(cast(instance.timed_animation_button_box)).setDirection(TextDirection.LTR);
    }


    @Structure.FieldOrder({"parent",
            "animation_preferences_stack",
            "timed_animation_sample",
            "timed_animation_button_box",
            "timed_animation_repeat_count",
            "timed_animation_reverse",
            "timed_animation_alternate",
            "timed_animation_duration",
            "timed_animation_easing",
            "spring_animation_velocity",
            "spring_animation_damping",
            "spring_animation_mass",
            "spring_animation_stiffness",
            "spring_animation_epsilon",
            "spring_animation_clamp_switch",
            "timed_animation",
            "spring_animation"
    })
    public static class Instance extends Structure {
        public Instance(long self) {
            super(asJnaPointer(self));
            read();
        }

        public byte[] parent = new byte[OFFSET];

        public long animation_preferences_stack;

        public long timed_animation_sample;
        public long timed_animation_button_box;
        public long timed_animation_repeat_count;
        public long timed_animation_reverse;
        public long timed_animation_alternate;
        public long timed_animation_duration;
        public long timed_animation_easing;
        public long spring_animation_velocity;
        public long spring_animation_damping;
        public long spring_animation_mass;
        public long spring_animation_stiffness;
        public long spring_animation_epsilon;
        public long spring_animation_clamp_switch;

        public long timed_animation;
        public long spring_animation;

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

                objectClass.setFieldGetProperty((__self12, object, property_id, value, pspec) -> new AdwDemoPageAnimations(object.cast()).getProperty(property_id, value));
                objectClass.setFieldSetProperty((__self13, object, property_id, value, pspec) -> new AdwDemoPageAnimations(object.cast()).setProperty(property_id, value));
                objectClass.setFieldDispose((__self1, object) -> new AdwDemoPageAnimations(object.cast()).onDispose(objectClass.getParentClass()));
                specTimedAnimation  = Gobject.paramSpecObject(PROP_TIMED_ANIMATION_NAME , Str.NULL, Str.NULL, Animation.getTypeID(), ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                specSpringAnimation = Gobject.paramSpecObject(PROP_SPRING_ANIMATION_NAME, Str.NULL, Str.NULL, Animation.getTypeID(), ParamFlags.READWRITE | GobjectConstants.PARAM_STATIC_STRINGS);
                objectClass.installProperty(PROP_TIMED_ANIMATION,  specTimedAnimation);
                objectClass.installProperty(PROP_SPRING_ANIMATION, specSpringAnimation);

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-animations.ui");

                widgetClass.bindTemplateChildFull("animation_preferences_stack",   true, OFFSET);
                widgetClass.bindTemplateChildFull("timed_animation_sample",        true, OFFSET + 1 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_button_box",    true, OFFSET + 2 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_repeat_count",  true, OFFSET + 3 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_reverse",       true, OFFSET + 4 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_alternate",     true, OFFSET + 5 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_duration",      true, OFFSET + 6 * 8);
                widgetClass.bindTemplateChildFull("timed_animation_easing",        true, OFFSET + 7 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_velocity",     true, OFFSET + 8 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_damping",      true, OFFSET + 9 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_mass",         true, OFFSET + 10 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_stiffness",    true, OFFSET + 11 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_epsilon",      true, OFFSET + 12 * 8);
                widgetClass.bindTemplateChildFull("spring_animation_clamp_switch", true, OFFSET + 13 * 8);

                widgetClass.bindTemplateCallback("animations_easing_name", new Callback() {
                    public long invoke(long item, long user_data) {
                        return AnimationsUtil.getEasingName(new EnumListItem(cast(item))).asCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_reset", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(cast(self)).timedAnimationReset();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_play_pause", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(cast(self)).timedAnimationPlayPause();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_skip", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(cast(self)).timedAnimationSkip();
                    }
                });
                widgetClass.bindTemplateCallback("get_play_pause_icon_name", new Callback() {
                    public long invoke(long user_data, int timed_state, int spring_state) {
                        return AnimationsUtil.getPlayPauseIconName(timed_state, spring_state).asCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_can_reset", new Callback() {
                    public boolean invoke(long user_data, int timed_state, int spring_state) {
                        return AnimationsUtil.timedAnimationCanReset(timed_state, spring_state);
                    }
                });
                widgetClass.bindTemplateCallback("timed_animation_can_skip", new Callback() {
                    public boolean invoke(long user_data, int timed_state, int spring_state) {
                        return AnimationsUtil.timedAnimationCanSkip(timed_state, spring_state);
                    }
                });
                widgetClass.bindTemplateCallback("notify_spring_params_change", new Callback() {
                    public void invoke(long self) {
                        new AdwDemoPageAnimations(cast(self)).notifySpringParamsChange();
                    }
                });
            }, (__self, self, g_class) -> new AdwDemoPageAnimations(self));
        }
        return type;
    }

    private Animation getCurrentAnimation() {
        var stack = new Stack(cast(instance.animation_preferences_stack));
        var currentAnimation = stack.getVisibleChildName().toString();

        if (currentAnimation.equals("Timed")) {
            return new Animation(cast(instance.timed_animation));
        }
        return new Animation((cast(instance.spring_animation)));
    }

    private void getTimedAnimationAllocate(Widget widget, int width, int height, int baseline) {
        var child = widget.getFirstChild();
        if (child.isNotNull()) {
            var offset = getOffset(width, AnimationsUtil.getMeasuredWidth(child), getCurrentAnimation().getValue());
            var point = AnimationsUtil.getOffsetPoint(offset);
            child.allocate(width, height, baseline, AnimationsUtil.translate(point));
            point.destroy();
        }
    }

    private static int getOffset(int width, int childWidth, double progress) {
        final double max = width - childWidth;
        return (int) (max * (progress - 0.5d));
    }

    private void timedAnimationReset() {
        new Animation(cast(instance.timed_animation)).reset();
        new Animation(cast(instance.spring_animation)).reset();
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
        new Animation(cast(instance.timed_animation)).skip();
        new Animation(cast(instance.spring_animation)).skip();
    }


    private void notifySpringParamsChange() {
        var springParams = new SpringParams(
                new SpinButton(cast(instance.spring_animation_damping)).getValue(),
                new SpinButton(cast(instance.spring_animation_mass)).getValue(),
                new SpinButton(cast(instance.spring_animation_stiffness)).getValue());

        new SpringAnimation(cast(instance.spring_animation)).setSpringParams(springParams);
        springParams.unref();
    }


    private void getProperty(int propId, Value value) {
        if (propId == PROP_TIMED_ANIMATION) {
            value.setObject(asPointer(instance.timed_animation));
        } else if (propId == PROP_SPRING_ANIMATION) {
            value.setObject(asPointer(instance.spring_animation));
        } else {
            System.err.println("Invalid property: " + propId);
        }
    }

    private void setProperty(int propId, Value value) {
        if (propId == PROP_TIMED_ANIMATION) {
            instance.timed_animation = value.getObject().asCPointer(); // Why does C depend on unreadable macros for banal things like this? (#define g_set_object ...)
            instance.writeField("timed_animation");
        } else if (propId == PROP_SPRING_ANIMATION) {
            instance.spring_animation = value.getObject().asCPointer();
            instance.writeField("spring_animation");
        } else {
            System.err.println("Invalid property: " + propId);
        }
    }

    private void onDispose(ObjectClassExtended parentClass) {
        new Animation(cast(instance.spring_animation)).unref();
        new Animation(cast(instance.timed_animation)).unref();

        parentClass.onDispose(this);
    }
}
