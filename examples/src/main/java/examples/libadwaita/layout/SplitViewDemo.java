package examples.libadwaita.layout;

import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.Breakpoint;
import ch.bailu.gtk.adw.BreakpointBin;
import ch.bailu.gtk.adw.BreakpointCondition;
import ch.bailu.gtk.adw.BreakpointConditionLengthType;
import ch.bailu.gtk.adw.HeaderBar;
import ch.bailu.gtk.adw.NavigationPage;
import ch.bailu.gtk.adw.NavigationSplitView;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.PositionType;
import ch.bailu.gtk.gtk.Unit;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class SplitViewDemo {



    public static void main(String[] args) {
        var app = new Application(new Str("org.example.SplitViewDemo"), 0);

        app.onActivate(() -> {
            var headerBar = new HeaderBar();
            var applicationWindow = new ApplicationWindow(app);
            applicationWindow.setContent(createNavigationSplitView(applicationWindow, headerBar));
            applicationWindow.present();
            headerBar.setShowEndTitleButtons(true);
        });

        app.run(0, Strs.NULL);
    }

    private static NavigationSplitView createNavigationSplitView(ApplicationWindow applicationWindow, HeaderBar headerBar) {
        var navigationSplitView = new NavigationSplitView();
        var toLevel2Button = Button.newWithLabelButton("Show level 2");
        var toLevel1Button = Button.newWithLabelButton("Show level 1");


        var breakpoint = new Breakpoint(BreakpointCondition.newLengthBreakpointCondition(BreakpointConditionLengthType.MAX_WIDTH, 400d, Unit.POINTS));
        breakpoint.onApply(() -> {
            navigationSplitView.setCollapsed(true);
            toLevel2Button.setVisible(true);
            toLevel1Button.setVisible(true);
        });
        breakpoint.onUnapply(() -> {
            navigationSplitView.setCollapsed(false);
            toLevel2Button.setVisible(false);
            toLevel1Button.setVisible(false);
        });
        applicationWindow.addBreakpoint(breakpoint);

        toLevel1Button.onClicked(()->navigationSplitView.setShowContent(false));
        toLevel2Button.onClicked(()->navigationSplitView.setShowContent(true));

        navigationSplitView.setSidebar(createLeftPage(toLevel2Button, "Level 1", headerBar));
        navigationSplitView.setContent(createRightPage(toLevel1Button, "Level 2 & 3"));
        return navigationSplitView;
    }

    private static NavigationPage createRightPage(Button button, String title) {

        var breakpointBin = new BreakpointBin();
        var box = new Box(Orientation.VERTICAL, 0);
        var navigationSplitView = new NavigationSplitView();
        var toLevel3Button = Button.newWithLabelButton("Show level 3");
        var toLevel2Button = Button.newWithLabelButton("Show level 2");


        var breakpoint = new Breakpoint(BreakpointCondition.newLengthBreakpointCondition(BreakpointConditionLengthType.MAX_WIDTH, 400d, Unit.POINTS));
        breakpoint.onApply(() -> {
            navigationSplitView.setCollapsed(true);
            toLevel3Button.setVisible(true);
            toLevel2Button.setVisible(true);
        });
        breakpoint.onUnapply(() -> {
            navigationSplitView.setCollapsed(false);
            toLevel3Button.setVisible(false);
            toLevel2Button.setVisible(false);
        });

        var headerBar = new HeaderBar();
        System.out.println(headerBar.getDecorationLayout());
        box.append(headerBar);

        toLevel3Button.onClicked(()->navigationSplitView.setShowContent(true));
        toLevel2Button.onClicked(()->navigationSplitView.setShowContent(false));

        breakpointBin.addBreakpoint(breakpoint);
        breakpointBin.setIntProperty("width-request", 100);
        breakpointBin.setIntProperty("height-request", 100);
        navigationSplitView.setContent(createInnerPage(toLevel2Button, "Level 3"));
        navigationSplitView.setSidebar(createInnerPage(toLevel3Button, "Level 2"));

        navigationSplitView.setSidebarPosition(PositionType.RIGHT);

        box.append(navigationSplitView);
        box.append(button);
        breakpointBin.setChild(box);

        return new NavigationPage(breakpointBin, title);
    }

    private static NavigationPage createInnerPage(Button button, String title) {
        var box = new Box(Orientation.VERTICAL, 0);
        box.append(new Label(title));
        box.append(button);
        return new NavigationPage(box, title);
    }

    private static NavigationPage createLeftPage(Button button, String title, HeaderBar headerBar) {
        var box = new Box(Orientation.VERTICAL, 0);

        box.append(headerBar);
        box.append(new Label(title));
        box.append(button);
        return new NavigationPage(box, title);
    }
}
