package org.goodoldai.jeff.report.rtf;

import java.util.ArrayList;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Rectangle;

/**
 * This class represents a dummy document listener (mock-object). It was created
 * in order to enable testing of the RTF related actions by capturing events
 * related to the com.lowagie.text.Document class.
 *
 * @author Anisja Kijevcanin
 */
public class DummyDocListener implements com.lowagie.text.DocListener{

    private ArrayList<Object[]> capturedEvents = new ArrayList<Object[]>();

    public void open() {
        Object[] event = new Object[1];
        event[0] = "open";
        capturedEvents.add(event);
    }

    public void close() {
        Object[] event = new Object[1];
        event[0] = "close";
        capturedEvents.add(event);
    }

    public boolean newPage() {
        Object[] event = new Object[1];
        event[0] = "new page";
        capturedEvents.add(event);
        return true;
    }

    public boolean setPageSize(Rectangle arg0) {
        Object[] event = new Object[2];
        event[0] = "set page size";
        event[1] = arg0;
        capturedEvents.add(event);
        return true;
    }

    public boolean setMargins(float arg0, float arg1, float arg2, float arg3) {
        Object[] event = new Object[5];
        event[0] = "set margins";
        event[1] = arg0;
        event[2] = arg1;
        event[3] = arg2;
        event[4] = arg3;
        capturedEvents.add(event);
        return true;
    }

    public boolean setMarginMirroring(boolean arg0) {
        Object[] event = new Object[2];
        event[0] = "set margin mirroring";
        event[1] = arg0;
        capturedEvents.add(event);
        return true;
    }

    public boolean setMarginMirroringTopBottom(boolean arg0) {
        Object[] event = new Object[2];
        event[0] = "set margin mirroring top bottom";
        event[1] = arg0;
        capturedEvents.add(event);
        return true;
    }

    public void setPageCount(int arg0) {
        Object[] event = new Object[2];
        event[0] = "set page count";
        event[1] = arg0;
        capturedEvents.add(event);
    }

    public void resetPageCount() {
        Object[] event = new Object[1];
        event[0] = "reset page count";
        capturedEvents.add(event);
    }

    public void setHeader(HeaderFooter arg0) {
        Object[] event = new Object[2];
        event[0] = "set header";
        event[1] = arg0;
        capturedEvents.add(event);
    }

    public void resetHeader() {
        Object[] event = new Object[1];
        event[0] = "reset header";
        capturedEvents.add(event);
    }

    public void setFooter(HeaderFooter arg0) {
        Object[] event = new Object[2];
        event[0] = "set footer";
        event[1] = arg0;
        capturedEvents.add(event);
    }

    public void resetFooter() {
        Object[] event = new Object[1];
        event[0] = "reset footer";
        capturedEvents.add(event);
    }

    public boolean add(Element arg0) throws DocumentException {
        Object[] event = new Object[2];
        event[0] = "add";
        event[1] = arg0;
        capturedEvents.add(event);
        return true;
    }

    public ArrayList<Object[]> getCapturedEvents(){
        return capturedEvents;
    }

}
