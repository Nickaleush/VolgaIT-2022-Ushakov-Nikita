package com.example.volgaitushakov.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.volgaitushakov.R
import com.example.volgaitushakov.utils.dpToPxF
import com.example.volgaitushakov.utils.getColorCompat

class ViewWithEdges : LinearLayout {

    var enableTopEdge = false
    var enableBottomEdge = false
    var enableMarginBottomLine = false
    var enableMarginTopLine = false
    var bottomMargin = context.dpToPxF(16)

    private var p : Paint? = null

    constructor(context : Context) : super(context){
        init()
    }
    constructor(context : Context, attrs : AttributeSet) : super(context, attrs){
        init()
        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewWithEdges)
        try {
            enableBottomEdge = a.getBoolean(R.styleable.ViewWithEdges_enableBottomEdge, false)
            enableTopEdge = a.getBoolean(R.styleable.ViewWithEdges_enableTopEdge, false)
            enableMarginBottomLine = a.getBoolean(R.styleable.ViewWithEdges_enableMarginBottomLine, false)
            enableMarginTopLine = a.getBoolean(R.styleable.ViewWithEdges_enableMarginTopLine, false)
        } finally {
            a.recycle();
        }
    }

    private fun init(){
        setWillNotDraw(false)
        p = Paint()
        p?.color = context.getColorCompat(R.color.viewEdge)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(enableTopEdge){
            p?.let {
                canvas?.drawRect(
                    if (enableMarginTopLine) context.dpToPxF(16) else 0f,
                    0f,
                    if (enableMarginTopLine) width.toFloat() - context.dpToPxF(16) else width.toFloat(),
                    context.dpToPxF(1), it
                )
            }
        }
        if(enableBottomEdge){
            p?.let {
                canvas?.drawRect(
                    if (enableMarginBottomLine) bottomMargin else 0f,
                    height - context.dpToPxF(1),
                    if (enableMarginBottomLine) width.toFloat() - context.dpToPxF(16) else width.toFloat(),
                    height.toFloat(), it
                )
            }
        }

    }
}