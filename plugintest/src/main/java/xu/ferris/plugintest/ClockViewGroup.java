package xu.ferris.plugintest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * test插件化
 * Created by xff on 2017/10/19.
 */

public class ClockViewGroup extends FrameLayout {
    public ClockViewGroup(Context context) {
        super(context);
        init(context);
    }



    public ClockViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context mContext){
        View.inflate(mContext,R.layout.plugin_sample_clock_group,this);
    }
}
