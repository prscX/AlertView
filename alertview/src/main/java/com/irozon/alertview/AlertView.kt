package com.irozon.alertview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import com.irozon.alertview.objects.AlertAction
import com.irozon.alertview.fragments.BottomSheetFragment
import com.irozon.alertview.fragments.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


/**
 * Created by hammad.akram on 3/14/18.
 */
class AlertView(private var title: String, private var message: String, private var style: AlertStyle) {

    private var theme: AlertTheme = AlertTheme.LIGHT
    private var actions: ArrayList<AlertAction> = ArrayList()

    /**
     * Add Actions to AlertView
     * @param action: AlertAction
     */
    fun addAction(action: AlertAction) {
        actions.add(action)
    }

    /**
     * Show AlertView
     * @param activity: AppCompatActivity
     */
    fun show(activity: Activity) {
        when (style) {
            AlertStyle.BOTTOM_SHEET -> {
                var bottomSheetDialog = BottomSheetDialog(activity, R.style.CustomBottomSheetDialogTheme)

                var view = createView(activity, bottomSheetDialog)
                bottomSheetDialog.setContentView(view)

                bottomSheetDialog.show()
            }
            AlertStyle.IOS -> {
                var bottomSheetDialog = BottomSheetDialog(activity, R.style.CustomBottomSheetDialogTheme)

                var view = createView(activity, bottomSheetDialog)
                bottomSheetDialog.setContentView(view)

                bottomSheetDialog.show()
            }
            AlertStyle.DIALOG -> {
                val bottomSheet = DialogFragment(title, message, actions, theme)
                bottomSheet.show(activity.fragmentManager, bottomSheet.tag)
            }
        }
    }

    private fun createView(activity: Activity, bottomSheetDialog: BottomSheetDialog): View? {

        // Inflate view according to theme selected. Default is AlertTheme.LIGHT
        var view: View? = null
        if (theme == AlertTheme.LIGHT)
            view = LayoutInflater.from(activity).inflate(R.layout.alert_layout_light, null)
        else if (theme == AlertTheme.DARK)
            view = LayoutInflater.from(activity).inflate(R.layout.alert_layout_dark, null)


        // Set up view
        initView(activity, bottomSheetDialog, view)

        return view
    }

    private fun initView(activity: Activity, bottomSheetDialog: BottomSheetDialog, view: View?) {
        view!!.findViewById<TextView>(R.id.tvTitle).text = title
        view.findViewById<TextView>(R.id.tvMessage).text = message

        // In case of title or message is empty
        if (title.isEmpty()) view.findViewById<TextView>(R.id.tvTitle).visibility = View.GONE
        if (message.isEmpty()) view.findViewById<TextView>(R.id.tvMessage).visibility = View.GONE

        // Change view according to selected Style
        if (style == AlertStyle.BOTTOM_SHEET)
            view.findViewById<TextView>(R.id.tvCancel).visibility = View.GONE
        else if (style == AlertStyle.IOS)
            view.findViewById<TextView>(R.id.tvCancel).visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.tvCancel).setOnClickListener({ bottomSheetDialog.dismiss() })

        // Inflate action views
        inflateActionsView(activity, bottomSheetDialog, view.findViewById(R.id.actionsLayout), actions)
    }

    /**
     * Inflate action views
     */
    private fun inflateActionsView(activity: Activity, bottomSheetDialog: BottomSheetDialog, actionsLayout: LinearLayout, actions: java.util.ArrayList<AlertAction>) {
        for (action in actions) {

            // Inflate action view according to theme selected
            var view: View? = null
            if (theme == AlertTheme.LIGHT)
                view = LayoutInflater.from(activity).inflate(R.layout.action_layout_light, null)
            else if (theme == AlertTheme.DARK)
                view = LayoutInflater.from(activity).inflate(R.layout.action_layout_dark, null)

            view!!.findViewById<TextView>(R.id.tvAction).text = action.title

            // Click listener for action.
            view.findViewById<TextView>(R.id.tvAction).setOnClickListener({
                bottomSheetDialog.dismiss()

                // For Kotlin
                action.action?.invoke(action)

                // For Java
                action.actionListener?.onActionClick(action)
            })

            // Action text color according to AlertActionStyle
            if (action != null) {
                when (action.style) {
                    AlertActionStyle.POSITIVE -> {
                        view.findViewById<TextView>(R.id.tvAction).setTextColor(ContextCompat.getColor(activity!!, R.color.green))
                    }
                    AlertActionStyle.NEGATIVE -> {
                        view.findViewById<TextView>(R.id.tvAction).setTextColor(ContextCompat.getColor(activity!!, R.color.red))
                    }
                    AlertActionStyle.DEFAULT -> {
                        if (theme == AlertTheme.LIGHT)
                            view.findViewById<TextView>(R.id.tvAction).setTextColor(ContextCompat.getColor(activity!!, R.color.darkGray))
                        else if (theme == AlertTheme.DARK)
                            view.findViewById<TextView>(R.id.tvAction).setTextColor(ContextCompat.getColor(activity!!, R.color.lightWhite))
                    }

                }
            }

            // Add view to layout
            actionsLayout.addView(view)
        }
    }

    /**
     * Set theme for the AlertView
     * @param theme: AlertTheme
     */
    fun setTheme(theme: AlertTheme) {
        this.theme = theme
    }
}
