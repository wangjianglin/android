package lin.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import lin.core.ImagePicker;

public class ImagesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_images);

        ImagePicker images = (ImagePicker) this.findViewById(R.id.imagePickerId);

        images.setImagePaths(imagePaths);
        images.setEdited(false);
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
