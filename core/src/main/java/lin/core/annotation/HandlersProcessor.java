package lin.core.annotation;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by lin on 7/7/16.
 */
public class HandlersProcessor  implements ClassProcessor{

    @Override
    public void process(Object target, View view, Annotation annotation, Package pack) {

        ViewDataBinding binding = DataBindingUtil.getBinding(view); //view.getBinding();
        if(binding != null){
            int id = Utils.getBindingId(pack, "handlers");
            if(id != 0){
                binding.setVariable(id,target);
            }
        }
    }
}