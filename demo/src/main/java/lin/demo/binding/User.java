package lin.demo.binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import lin.demo.BR;

/**
 * Created by lin on 7/4/16.
 */
public class User extends BaseObservable {

    private String firstName;
    private String lastName;
    private String displayName;
    private int age;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, int age) {
        this(firstName, lastName);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Bindable
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.notifyChange();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.notifyPropertyChanged(BR.displayName);
    }

    public boolean isAdult() {
        return age >= 18;
    }
}
