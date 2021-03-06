package koma.gui.element.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;
import koma.gui.element.control.skin.KVirtualFlow;
import org.controlsfx.tools.Utils;

/**
 * This custom ScrollBar is used to map the increment & decrement features
 * to pixel based scrolling rather than thumb/track based scrolling, if the
 * "virtual" attribute is true.
 */
public class KVirtualScrollBar extends ScrollBar {

    /**************************************************************************
     *
     * Private fields
     *
     **************************************************************************/

    private final KVirtualFlow flow;

    private boolean adjusting;



    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    /**
     * Creates a new VirtualScrollBar, for use by the VirtualFlow control.
     */
    public KVirtualScrollBar(final KVirtualFlow flow) {
        this.flow = flow;

        super.valueProperty().addListener(valueModel -> {
            if (isVirtual()/* && oldValue != newValue*/) {
                if (adjusting) {
                    // no-op
                } else {
                    flow.setPosition(getValue());
                }
            }
        });
    }



    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    // --- virtual
    private BooleanProperty virtual = new SimpleBooleanProperty(this, "virtual");
    public final void setVirtual(boolean value) {
        virtual.set(value);
    }

    public final boolean isVirtual() {
        return virtual.get();
    }

    public final BooleanProperty virtualProperty() {
        return virtual;
    }


    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /** {@inheritDoc} */
    @Override public void decrement() {
        if (isVirtual()) {
            flow.scrollPixels(-10);
        } else {
            super.decrement();
        }
    }

    /** {@inheritDoc} */
    @Override public void increment() {
        if (isVirtual()) {
            flow.scrollPixels(10);
        } else {
            super.increment();
        }
    }

    // this method is called when the user clicks in the scrollbar track, so
    // we special-case it to allow for page-up and page-down clicking to work
    // as expected.
    /** {@inheritDoc} */
    @Override public void adjustValue(double pos) {
        if (isVirtual()) {
            adjusting = true;
            double oldValue = flow.getPosition();

            double newValue = ((getMax() - getMin()) * Utils.clamp(0, pos, 1))+getMin();
            if (newValue < oldValue) {
                IndexedCell cell = flow.getFirstVisibleCell();
                if (cell == null) return;
                flow.scrollToBottom(cell);
            } else if (newValue > oldValue) {
                IndexedCell cell = flow.getLastVisibleCell();
                if (cell == null) return;
                flow.scrollToTop(cell);
            }

            adjusting = false;
        } else {
            super.adjustValue(pos);
        }
    }
}
