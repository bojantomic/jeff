package org.goodoldai.jeff.report.rtf;

import java.awt.Color;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.TwoDimData;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.table.RtfCell;

public class RTFDataChunkBuilder {
	
	public RTFDataChunkBuilder() {
	}
	
	public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        if (!(echunk instanceof DataExplanationChunk)) {
            throw new ExplanationException("The entered chunk must be a DataExplanationChunk instance");
        }

        if (!(stream instanceof com.lowagie.text.Document)) {
            throw new ExplanationException("The entered stream must be a com.lowagie.text.Document instance");
        }

        com.lowagie.text.Document doc = ((com.lowagie.text.Document) stream);

        //Insert general chunk data
        if (insertHeaders)
            RTFChunkUtility.insertChunkHeader(echunk, doc);

        //Insert chunk content
        if (echunk.getContent() instanceof SingleData) {
            inputSingleDataContent((SingleData) (echunk.getContent()), doc);

        }/* else if (echunk.getContent() instanceof OneDimData) {
            inputOneDimDataContent((OneDimData) (echunk.getContent()), doc);

        } else if (echunk.getContent() instanceof TwoDimData) {
            inputTwoDimDataContent((TwoDimData) (echunk.getContent()), doc);

        } else if (echunk.getContent() instanceof ThreeDimData) {
            inputThreeDimDataContent((ThreeDimData) (echunk.getContent()), doc);

        }*/
    }
	
	private void inputSingleDataContent(SingleData data, Document document) {
        String value = String.valueOf(data.getValue());

        Table table = null;
        RtfCell cell1 = null;
        RtfCell cell2 = null;
		try {
			table = new Table(1);
			
			table.setWidth(30);
			table.setAlignment(Table.ALIGN_LEFT);
			table.setOffset(15.0F);
			
			cell1 = new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension())));

			cell1.setBackgroundColor(Color.lightGray);
			cell2 = new RtfCell(new Paragraph(value));
		} catch (BadElementException e) {
			throw new ExplanationException(e.getMessage());
		}
		table.addCell(cell1);
		table.addCell(cell2);

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }
	 private String turnDimensionIntoHeader(Dimension dimension) {

	        String dimensionName = dimension.getName();
	        String dimensionUnit = dimension.getUnit();

	        if (dimensionUnit != null && !dimensionUnit.isEmpty()) {
	            return dimensionName + " [" + dimensionUnit + "]";
	        } else {
	            return dimensionName;
	        }

	 }
}