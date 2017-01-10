package lin.demo.swiperefresh;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 06/01/2017.
 */

public class SwiperefreshPresenter implements SwiperefreshContract.Presenter {

    private SwiperefreshContract.View mView;

    private List<Data> items = new ArrayList<>();

    public SwiperefreshPresenter(SwiperefreshContract.View view){
        mView = view;

    }
    @Override
    public void loadData() {

        int N = items.size();
        Data data = null;
        for(int n=0;n<24;n++){
            data = new Data();
            data.setName(getDigit(n+1+N));
            items.add(data);
        }

        mView.show(items);
    }

    @Override
    public void clean() {
        items.clear();
        mView.show(items);
    }


    private static String[] digits = new String[]{
            "","one","two","three","four","five","six","seven","eight","nine","ten",
            "eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen","twenty",
            "twenty-one","twenty-two","twenty-three","twenty-four","twenty-five","twenty-six","twenty-seven","twenty-eight","twenty-nine","thirty",
            "thirty-one","thirty-two","thirty-three","thirty-four","thirty-five","thirty-six","thirty-seven","thirty-eight","thirty-nine","forty",
            "forty-one","forty-two","forty-three","forty-four","forty-five","forty-six","forty-seven","forty-eight","forty-nine","fifty",
            "fifty-one","fifty-two","fifty-three","fifty-four","fifty-five","fifty-six","fifty-seven","fifty-eight","fifty-nine","sixty",
            "sixty-one","sixty-two","sixty-three","sixty-four","sixty-five","sixty-six","sixty-seven","sixty-eight","sixty-nine","seventy",
            "seventy-one","seventy-two","seventy-three","seventy-four","seventy-five","seventy-six","seventy-seven","seventy-eight","seventy-nine","eighty",
            "eighty-one","eighty-two","eighty-three","eighty-four","eighty-five","eighty-six","eighty-seven","eighty-eight","eighty-nine","ninety",
            "ninety-one","ninety-two","ninety-three","ninety-four","ninety-five","ninety-six","ninety-seven","ninety-eight","ninety-nine","one hundred"
    };

    private static String getDigit(int digit){
        int hundreds = (digit / 100) % 10;
        int d = digit % 100;
        if(hundreds > 0){
            return digits[hundreds] + " hundred and " + digits[d];
        }
        return digits[d];
    }
    @Override
    public void start() {
        this.loadData();
    }
}
