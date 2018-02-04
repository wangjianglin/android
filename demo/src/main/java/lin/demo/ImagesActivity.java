package lin.demo;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import lin.core.ImagePicker;
import lin.core.ViewActivity;

public class ImagesActivity extends ViewActivity {

    private static final String[] imagePaths = new String[]{
            "http://i.feicuibaba.com/upload/store/61/29483A7B-2AAA-41D5-8730-7C734668A413.jpg",
            "http://i.feicuibaba.com/upload/store/342/336B39B8-2221-43F6-AAAD-0D3A55C18944.jpg",
            "http://i.feicuibaba.com/upload/store/313/2ac50571-582d-4f64-bc38-d19fbcebdd65.jpg",
            "http://i.feicuibaba.com/upload/store/341/1C4232C5-02F2-4234-9E25-25D250D2330C.jpg",
            "http://i.feicuibaba.com/upload/store/109/9509466f-e60c-48e7-80a0-da41f97a3df0.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_picker);

        ImagePicker images = (ImagePicker) this.findViewById(R.id.imagePickerId);

        images.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("=================== touch "+event.getAction()+" ===================");
                return false;
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("=================== click ===================");
            }
        });

        images.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("=================== long click ===================");
                return false;
            }
        });
        images.setImagePaths(imagePaths);
        images.setEdited(false);
String url = "http://i.feicuibaba.com/upload/store/1393/333FF40C-5B5C-49D0-B558-EBFCC878ED72.mp4";
//url = "http://i.feicuibaba.com/upload/buyers/12345/2018-02-04/VID_20180201_174154.mp4";
//url = "http://i.feicuibaba.com/upload/store/1988/B91584BE-742D-4E6E-97C4-85257C1758B7.mp4";
//        url="http://i.feicuibaba.com/upload/store/2318/FE0463A2-3115-4502-B758-E89091943A4D.mp4";
        images.setVedioUrl(url);
        images.setVedio(true);
        images.setDotFlag(ImagePicker.DotFlag.DOWN_RIGHT);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
