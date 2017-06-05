package lin.core.form;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lin.core.R;
import lin.core.AttrType;
import lin.core.Attrs;
import lin.core.ResView;
import lin.core.annotation.Click;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;

/**
 * Created by Administrator on 2017/6/1.
 */
@ResCls(R.class)
@ResId(id="lin_core_form_iconrow")
public class IconRow extends ResView {

    public IconRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IconRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconRow(Context context) {
        super(context);
    }

    @ViewById(id="mine_custom_row_icon")
    private ImageView iconView;
    @ViewById(id="mine_custom_row_title")
    private TextView titleView;
    @ViewById(id="mine_custom_row_image")
    private ImageView imageView;

    @Override
    protected void onCreate() {
        Attrs attrs=this.getAttrs();
        this.setTitle(attrs.getString(R.styleable.row,R.styleable.row_mine_row_title));
        this.setIcon(attrs.getDrawable(R.styleable.row,R.styleable.row_mine_row_icon));
        //this.setImage(attrs.getDrawable(R.styleable.row,R.styleable.row_mine_row_image));
        this.setIconWidth((int)attrs.getDimension(R.styleable.row,R.styleable.row_mine_row_icon_width));
        this.setIconHeight((int)attrs.getDimension(R.styleable.row,R.styleable.row_mine_row_icon_height));
        ViewGroup.LayoutParams iconLp=iconView.getLayoutParams();
        if(iconLp!=null){
            if(iconWidth!=0) {
                iconLp.width = iconWidth;
            }
            if(iconHeight!=0) {
                iconLp.height = iconHeight;
            }
        }
        this.setTitleSize(attrs.getDimension(R.styleable.row,R.styleable.row_mine_row_title_size));
    }

    private CharSequence title;
    private Drawable icon;
    private Drawable image;
    private int iconWidth;
    private int iconHeight;
    private float titleSize;

    public CharSequence getTitle() {return title;}
    public void setTitle(CharSequence title) {
        this.title = title;
        titleView.setText(title);
    }
    public Drawable getIcon() {return icon;}
    public void setIcon(Drawable icon) {
        this.icon = icon;
        iconView.setImageDrawable(icon);
    }
    public Drawable getImage() {return image;}
    public void setImage(Drawable image) {
        this.image = image;
        imageView.setImageDrawable(image);
    }
    public int getIconWidth() {
        return iconWidth;
    }
    public void setIconWidth(int iconWidth) {this.iconWidth = iconWidth;}
    public int getIconHeight() {return iconHeight;}
    public void setIconHeight(int iconHeight) {this.iconHeight = iconHeight;}

    public float getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        if(titleSize!=0) {
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
        }
    }


    @Override
    protected void genAttrs() {
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_icon, AttrType.Drawable);
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_title, AttrType.String);
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_image, AttrType.Drawable);
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_icon_width, AttrType.Dimension);
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_icon_height, AttrType.Dimension);
        this.addAttr(R.styleable.row,R.styleable.row_mine_row_title_size, AttrType.Dimension);
    }
}
