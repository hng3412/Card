
package android.databinding;
import com.example.card.BR;
class DataBinderMapperImpl extends android.databinding.DataBinderMapper {
    public DataBinderMapperImpl() {
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case com.example.card.R.layout.item_1:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/item_1_0".equals(tag)) {
                            return new com.example.card.databinding.Item1Binding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for item_1 is invalid. Received: " + tag);
                }
                case com.example.card.R.layout.activity_digit:
 {
                        final Object tag = view.getTag();
                        if(tag == null) throw new java.lang.RuntimeException("view must have a tag");
                    if ("layout/activity_digit_0".equals(tag)) {
                            return new com.example.card.databinding.ActivityDigitBinding(bindingComponent, view);
                    }
                        throw new java.lang.IllegalArgumentException("The tag for activity_digit is invalid. Received: " + tag);
                }
        }
        return null;
    }
    @Override
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    @Override
    public int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -870317637: {
                if(tag.equals("layout/item_1_0")) {
                    return com.example.card.R.layout.item_1;
                }
                break;
            }
            case 1080903859: {
                if(tag.equals("layout/activity_digit_0")) {
                    return com.example.card.R.layout.activity_digit;
                }
                break;
            }
        }
        return 0;
    }
    @Override
    public String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"};
    }
}