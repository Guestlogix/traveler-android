package com.guestlogix.traveleruikit.widgets;


import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.guestlogix.traveleruikit.R;

public class ActionStrip extends FrameLayout {

    // Views
    private Button actionButton;
    private TextView actionValue;
    private TextView actionLabel;
    private ProgressBar progressBar;

    // State of this widget.
    private ActionStripState state;


    /**
     * Listener used to dispatch click events on the button.
     */
    private OnClickListener onClickListener;

    /**
     * Callback interface for click events on the action strip.
     */
    public interface OnClickListener {
        void onClick();
    }

    public enum ActionStripState {
        ENABLED, DISABLED, LOADING
    }

    public ActionStrip(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ActionStrip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ActionStrip(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActionStrip(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_action_strip, this, true);
            actionButton = view.findViewById(R.id.actionButton);
            actionValue = view.findViewById(R.id.actionValue);
            actionLabel = view.findViewById(R.id.actionLabel);
            progressBar = view.findViewById(R.id.progress_bar);

            state = ActionStripState.ENABLED; // Default behaviour.
            onStateChange();

            actionButton.setOnClickListener(v -> {
                if (onClickListener != null) onClickListener.onClick();
            });
        }
    }

    public void setButtonText(@Nullable String text) {
        actionButton.setText(text);
    }

    public void setLabel(@Nullable String text) {
        actionLabel.setText(text);
    }

    public void setValue(@Nullable String text) {
        actionValue.setText(text);
    }

    public void setStripValues(@Nullable String buttonText, @Nullable String labelText, @Nullable String valueText) {
        setButtonText(buttonText);
        setLabel(labelText);
        setValue(valueText);
    }

    public void changeState(ActionStripState newState) {
        this.state = newState;
        onStateChange();
    }

    /**
     * Registers callback for click events on the action strip.
     *
     * @param onClickListener Interface to propagate callbacks.
     */
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void onStateChange() {
        switch (state) {
            case LOADING:
                actionButton.setVisibility(INVISIBLE);
                progressBar.setVisibility(VISIBLE);
                break;
            case ENABLED:
                actionButton.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                actionButton.setEnabled(true);
                actionButton.setAlpha(1f);
                break;
            case DISABLED:
                actionButton.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                actionButton.setEnabled(false);
                actionButton.setAlpha(0.5f);
                break;
        }
    }
}
