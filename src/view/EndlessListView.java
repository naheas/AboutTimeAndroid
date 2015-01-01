package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class EndlessListView extends ListView {
	private boolean is_block = false;
	
	public EndlessListView(Context context) {
		super(context);
	}

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    public void setBlockLayoutChildren(boolean is_block) {
    	this.is_block = is_block;
    }
    
    @Override
    protected void layoutChildren() {
        if (!is_block) {	
            super.layoutChildren();
        }
    }
}