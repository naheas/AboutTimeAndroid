package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class EndlessGridView extends GridView {
	private boolean is_block = false;
	private int first_vis_pos = 0;
	private int top_margin = 0;

	public EndlessGridView(Context context) {
		super(context);
	}

    public EndlessGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
    public EndlessGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    public void setBlockLayoutChildren(boolean is_block, int first_vis_pos, int top_margin) {
    	this.is_block = is_block;
    	this.first_vis_pos = first_vis_pos; 
    	this.top_margin = top_margin;
    }
    
    @Override
    protected void layoutChildren() {
        if (!is_block) {	
            super.layoutChildren();
        }else{      	
			super.smoothScrollToPositionFromTop(first_vis_pos, top_margin, 0); 
        	is_block = false;
        }
    }
}