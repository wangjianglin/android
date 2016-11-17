package lin.core.annotation;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import lin.core.ResourceView;

/**
 * Created by lin on 7/6/16.
 */
public class ViewModelProcessor implements FieldProcessor,ClassProcessor{
    @Override
    public void process(Object target, View view, Field field, Annotation annotation, Package pack) {
        ViewModel item = (ViewModel) annotation;

        String name = item.value();
        if("".equals(name)){
            name = field.getName();
        }
        try {
            field.setAccessible(true);
            setBind(target,view,name,field.get(target),pack);
        } catch (IllegalAccessException e) {
        }
    }

    @Override
    public void process(Object target,View view, Annotation annotation, Package pack) {
        ViewModel item = (ViewModel) annotation;
        String name = item.value();
        if("".equals(name)){
            name = view.getClass().getName();
        }
        setBind(target,view,name,target,pack);
    }

    private void setBind(Object target,View view, String name, Object value, Package pack){
        if(value == null){
            return;
        }

        ViewDataBinding binding = null;

        if(target == view && view instanceof ResourceView){
            binding = DataBindingUtil.getBinding(((ResourceView)view).getNodeView());
        }else {
            binding = DataBindingUtil.getBinding(view);
        }
//        ViewDataBinding binding = view.getBinding();
        if(binding != null){
            int id = Utils.getBindingId(pack, name);
            if(id != 0){
                binding.setVariable(id,value);
            }
        }
    }
}
