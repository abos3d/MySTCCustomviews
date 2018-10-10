package sa.com.stc.customviews.ChartViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

import java.util.ArrayList;

import sa.com.stc.customviews.ChartState;
import sa.com.stc.customviews.R;
import sa.com.stc.customviews.Utils;


/**
 * Created by fafdalotaibi on 13/03/2016.
 */
public class CircleChart extends BaseChart {

    public final int STARTING_ANGLE = 270;

    //rectangle used to draw the circle
    protected RectF arcRect = new RectF();

    //circle X and Y points
    protected PointF circleCoordinates = new PointF();
    protected float circleRadius;

    //Paints for the dash and the circle
    protected Paint DashChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint circleChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //animation Time
    protected int animationDuration;

    //Chart data paints
    protected Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint unitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint headerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint footerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //Chart data
    protected String unit;
    protected String header;
    protected String footer;
    protected String value;
    protected float arcValue = 0;

    //chart data coordinates
    protected PointF valueCoordinates = new PointF();
    protected PointF unitCoordinates = new PointF();
    protected PointF headerCoordinates = new PointF();
    protected PointF footerCoordinates = new PointF();
    private float valueSize;
    private ChartState chartState = ChartState.LIMITED;

    private int finishedValueColor;
    private int finishedArcColor;
    private int valueColor;
    private int arcColor;

    public CircleChart(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CircleChartStyle);
    }

    public CircleChart(Context context) {
        this(context, null);
    }

    public CircleChart(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Utils.setLanguageForContext(context);
        super.FINISHING_ANGLE = 360;
        super.STARTING_ANGLE = 270;
        super.mCurrValue = FINISHING_ANGLE;
        final int defaultDashCircleColor = ContextCompat.getColor(context, R.color.default_empty_arc_color);
        final int defaultFilledChartCircleColor = ContextCompat.getColor(context, R.color.default_filled_arc_color);
        final int defaultFinishedColor = ContextCompat.getColor(context, R.color.default_finished_color);

        final int defaultHeaderColor = ContextCompat.getColor(context, android.R.color.primary_text_light);
        final int defaultFooterColor = ContextCompat.getColor(context, android.R.color.secondary_text_light);
        final int defaultValueColor = ContextCompat.getColor(context, R.color.default_value_color);
        final int defaultUnitColor = ContextCompat.getColor(context, R.color.default_unit_color);
        final int defaultArcWidth = context.getResources().getDimensionPixelSize(R.dimen.default_arc_width);
        final int defaultDuration = context.getResources().getInteger(R.integer.default_animation_duration);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleChart, defStyleAttr, 0);
        finishedValueColor = a.getColor(R.styleable.CircleChart_finished_color, defaultFinishedColor);
        finishedArcColor = a.getColor(R.styleable.CircleChart_finished_color, defaultFinishedColor);
        arcColor = a.getColor(R.styleable.CircleChart_filled_arc_color, defaultFilledChartCircleColor);
        valueColor = a.getColor(R.styleable.CircleChart_value_color, defaultValueColor);

        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 1.0f);
        DashChartPaint.setColor((defaultDashCircleColor));
        DashChartPaint.setStyle(Paint.Style.STROKE);
        DashChartPaint.setPathEffect(dashPathEffect);

        circleChartPaint.setColor(a.getColor(R.styleable.CircleChart_filled_arc_color, defaultFilledChartCircleColor));
        circleChartPaint.setStyle(Paint.Style.STROKE);
        circleChartPaint.setStrokeCap(Paint.Cap.ROUND);

        valuePaint.setStyle(Paint.Style.FILL);
        valuePaint.setColor(a.getColor(R.styleable.CircleChart_value_color, defaultValueColor));

        unitPaint.setStyle(Paint.Style.FILL);
        unitPaint.setColor(a.getColor(R.styleable.CircleChart_unit_color, defaultUnitColor));

        headerPaint.setStyle(Paint.Style.FILL);
        headerPaint.setColor(a.getColor(R.styleable.CircleChart_header_color, defaultHeaderColor));
        headerPaint.setFakeBoldText(true);

        footerPaint.setStyle(Paint.Style.FILL);
        footerPaint.setColor(a.getColor(R.styleable.CircleChart_footer_color, defaultFooterColor));

        animationDuration = a.getInteger(R.styleable.CircleChart_animation_duration, defaultDuration);


        valueSize = 6f;

        if (isInEditMode()) {
            header = "Local Internet";
            footer = "8 GB will end after 10 days";
            value = "437.45";
            unit = "GB";
        }

        a.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int min = getWidth() > getHeight() ? getHeight() : getWidth();

        float padding = getPaddingStart() != 0 ? getPaddingStart()
                : getPaddingEnd() != 0 ? getPaddingEnd()
                : getPaddingTop() != 0 ? getPaddingTop()
                : getPaddingBottom() != 0 ? getPaddingBottom() : 0;

        float arcWidth = min / 20f;

        circleRadius = ((min) / 2) - (padding + arcWidth);

        circleChartPaint.setStrokeWidth(arcWidth);
        DashChartPaint.setStrokeWidth(arcWidth / 5);
        arcRect.set((getWidth() / 2) - circleRadius, (getHeight() / 2) - circleRadius, getWidth() - (getWidth() / 2 - circleRadius), getHeight() - (getHeight() / 2 - circleRadius));

        circleCoordinates.set(getWidth() / 2, getHeight() / 2);

        valuePaint.setTextSize(min / valueSize - padding);
        unitPaint.setTextSize(min / 10f);
        headerPaint.setTextSize(min / 12f);
        footerPaint.setTextSize(min / 14f);

        try {

            float valueSize = 0;
            float headerSize = 0;
            float footerSize = 0;
            float unitSize = 0;

            if (value != null && !value.isEmpty())
                valueSize = valuePaint.measureText(value, 0, value.length());

            if (header != null && !header.isEmpty())
                headerSize = headerPaint.measureText(header, 0, header.length());

            if (footer != null && !footer.isEmpty())
                footerSize = footerPaint.measureText(footer, 0, footer.length());

            if (unit != null && !unit.isEmpty())
                unitSize = unitPaint.measureText(unit, 0, unit.length());

            if (Utils.isArabicSelected()) {
                valueCoordinates.x = unitCoordinates.x + unitSize;
                unitCoordinates.x = (getWidth() / 2) - (valueSize + unitSize) / 2;
            } else {
                valueCoordinates.x = (getWidth() / 2) - (valueSize + unitSize) / 2;
                unitCoordinates.x = valueCoordinates.x + valueSize;
            }

            valueCoordinates.y = (getHeight() / 2) - ((valuePaint.descent() + valuePaint.ascent()) / 2);

            unitCoordinates.y = valueCoordinates.y;

            headerCoordinates.x = (getWidth() / 2) - headerSize / 2;
            headerCoordinates.y = arcRect.top + (circleRadius) / 2;

            footerCoordinates.x = (getWidth() / 2) - circleRadius / 2;
            footerCoordinates.y = arcRect.bottom - (circleRadius + arcWidth) / 2;

        } catch (Exception e) {
            e.printStackTrace();
        }

        canvas.drawCircle(circleCoordinates.x, circleCoordinates.y, circleRadius, DashChartPaint);

        switch (chartState) {
            case LIMITED:
                canvas.drawArc(arcRect, STARTING_ANGLE, mCurrValue, false, circleChartPaint);
                break;
            case UNLIMITED:
                canvas.drawArc(arcRect, STARTING_ANGLE, FINISHING_ANGLE, false, circleChartPaint);
                break;
            case FINISHED:
                canvas.drawArc(arcRect, STARTING_ANGLE, 0, false, circleChartPaint);
                break;
        }


        if (value != null && !value.isEmpty())
            canvas.drawText(value, valueCoordinates.x, valueCoordinates.y, valuePaint);

        if (unit != null && !unit.isEmpty())
            canvas.drawText(unit, unitCoordinates.x, unitCoordinates.y, unitPaint);

        if (header != null && !header.isEmpty())
            canvas.drawText(header, headerCoordinates.x, headerCoordinates.y, headerPaint);

        if (footer != null && !footer.isEmpty()) {
            DynamicLayout dynamicLayout = new DynamicLayout(footer, footer, new TextPaint(footerPaint), (int) circleRadius, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, false);
            canvas.save();
            canvas.translate(footerCoordinates.x, footerCoordinates.y - (dynamicLayout.getLineCount() - 1) * 12);
            dynamicLayout.draw(canvas);
            canvas.restore();
        }
    }

    private ArrayList<String> drawMultiLinesText(String text, float width, Paint textPaint) {
        String[] words = text.split(" ");
        ArrayList<String> lines = new ArrayList<>();

        String space = "";
        String currentLine = "";
        String prevLine = "";

        for (int i = 0; i < words.length; i++) {
            currentLine += space + words[i];
            space = " ";
            if (textPaint.measureText(currentLine, 0, currentLine.length()) > width) {
                lines.add(prevLine);
                currentLine = "";
                prevLine = "";
            } else
                prevLine = currentLine;
        }

        if (currentLine.isEmpty() == false)
            lines.add(currentLine);
        return lines;
    }

    public void animateChart() {
        FillingAnimation animation = new FillingAnimation(this, arcValue, animationDuration);
        this.startAnimation(animation);
    }

    public void setValues(double arcValue, double maxArcValue) {

        if (maxArcValue == -1)
            this.arcValue = FINISHING_ANGLE;

        else
            this.arcValue = (float) ((arcValue / maxArcValue) * FINISHING_ANGLE);

        animateChart();
        invalidate();
    }

    public void setValues(String header, String footer, String value, String unit, float arcValue, double maxArcValue) {
        this.value = value;
        this.unit = unit;
        this.header = header;
        this.footer = footer;
        if (maxArcValue == -1) {
            this.arcValue = arcValue;
            chartState = ChartState.UNLIMITED;
        } else if (arcValue == 0.0) {
            chartState = ChartState.FINISHED;
            circleChartPaint.setColor(finishedArcColor);
            DashChartPaint.setColor(finishedArcColor);
            valuePaint.setColor(finishedValueColor);
        } else {
            chartState = ChartState.LIMITED;
            circleChartPaint.setColor(arcColor);
            DashChartPaint.setColor(arcColor);
            valuePaint.setColor(valueColor);
            this.arcValue = (float) ((arcValue / maxArcValue) * FINISHING_ANGLE);
        }

        animateChart();
        invalidate();
    }

    public void setValues(String header, String footer, String value, ChartState chartState) {
        this.header = header;
        this.footer = footer;
        this.chartState = chartState;
        this.value = value;
        this.unit = null;
        animateChart();
        invalidate();
    }

    public void setValues(String value, ChartState chartState) {
        this.value = value;
        this.header = null;
        this.footer = null;
        this.unit = null;
        this.chartState = chartState;
    }

    public void setValuesForWidget(String header, String footer, String value, String unit, double arcValue, double maxArcValue) {
        this.value = value;
        this.unit = unit;
        this.header = header;
        this.footer = footer;

        if (maxArcValue == -1)
            this.mCurrValue = FINISHING_ANGLE;
        else
            this.mCurrValue = (float) ((arcValue / maxArcValue) * FINISHING_ANGLE);
        invalidate();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public void setAccentColor(int accentColor) {
        circleChartPaint.setColor(accentColor);
        valuePaint.setColor(accentColor);
        unitPaint.setColor(accentColor);
    }

    public void setSizeValue(float size) {
        valueSize = size;
    }

}
